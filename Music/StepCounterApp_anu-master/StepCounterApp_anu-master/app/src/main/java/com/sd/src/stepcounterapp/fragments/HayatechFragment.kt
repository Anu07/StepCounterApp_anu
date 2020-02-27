package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fitpolo.support.MokoSupport
import com.fitpolo.support.callback.MokoConnStateCallback
import com.fitpolo.support.entity.BleDevice
import com.fitpolo.support.entity.DailyStep
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.sd.src.stepcounterapp.*
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.activities.LeaderboardActivity
import com.sd.src.stepcounterapp.activities.SyncDeviceActivity
import com.sd.src.stepcounterapp.adapter.PatternProgressTextAdapter
import com.sd.src.stepcounterapp.dialog.OptionDialog
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.Deviceresponse.DashboardResponse
import com.sd.src.stepcounterapp.model.Deviceresponse.Data
import com.sd.src.stepcounterapp.model.OptionsModel
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.syncDevice.Activity
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.utils.DayAxisValueFormatter
import com.sd.src.stepcounterapp.utils.InterConsts.MONTHLY
import com.sd.src.stepcounterapp.utils.InterConsts.WEEKLY
import com.sd.src.stepcounterapp.utils.MyBarDataSet
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.AVGSTEPS
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.FIREBASETOKEN
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.PREF_CURR_WEARABLE
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_ELCIES_CONNCTED
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_WEARABLE_CONNECTED
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.WEARABLEID
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.viewModels.DeviceViewModel
import kotlinx.android.synthetic.main.dialog_unauthorized_device.view.*
import kotlinx.android.synthetic.main.fragment_hayatech.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HayatechFragment : BaseFragment() {

    interface SwipeVisibiltyListener {
        fun showSwipe()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: HayatechFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
        val INTENT_HOME: String? = "Home"
        fun newInstance(context: Context, deviceAddress: BleDevice?): HayatechFragment {
            instance = HayatechFragment()
            if (deviceAddress != null && deviceAddress?.address?.isNotEmpty()!!) {
                val args = Bundle()
                args.putString("device", deviceAddress.address)
                instance.arguments = args
            }
            mContext = context
            return instance
        }
    }

    private var mWearID: String? = null
    private var cancelledOnce: Boolean = false
    var mAlertDialog: AlertDialog? = null
    private var limit: Int = 0
    private var afterSync: Boolean = false
    var swipeListener: SwipeVisibiltyListener? = null
    var prevTokenEstd: Int? = 0
    private var tokenEstd: Int = 10
    private var updating: Boolean = false
    var activityList: ArrayList<Activity>? = null
    private var mDataList: Data? = Data()
    var mViewModel: DeviceViewModel? = null
    var optionArray = arrayListOf<OptionsModel>()
    var xAxis: XAxis? = null
    private var mWeekListFormater = arrayOfNulls<String>(7)
    private var mMonthListFormater = arrayOfNulls<String>(31)
    val colors: MutableList<Int> = mutableListOf(Color.rgb(141, 196, 64), Color.rgb(87, 199, 219))
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
        return inflater.inflate(R.layout.fragment_hayatech, container, false)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            if (mViewModel == null) {
                mViewModel = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
            }

            updateProgressCircle()
        } catch (e: Exception) {

            Log.e("exception", "message" + e.message)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFilterOptionArray()

        distance.isSelected = true

        swipeLayout.isEnabled = !(SharedPreferencesManager.hasKey(
            requireContext(),
            VAR_ELCIES_CONNCTED
        ) && SharedPreferencesManager.getBoolean(
            requireContext(),
            VAR_ELCIES_CONNCTED
        ))

        checkElcies()
        setStepsText()
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
        }
//        showPopupProgressSpinner(true)

        mViewModel!!.fetchSyncData(getFetchDeviceDataRequest())

        mViewModel!!.getSyncResponse().observe(this,
            Observer<BasicInfoResponse> {
                Log.i("Sync", "Data synced successfully")
                //                afterSync = true
//                mViewModel!!.fetchSyncData(getFetchDeviceDataRequest())
            })
        mViewModel!!.getDashResponse().observe(this,
            Observer<DashboardResponse> { mDashResponse ->
                mDataList = mDashResponse.data
                steps.text = mDashResponse.data.totalUserSteps.toString()
                totalstepsCount.text = mDashResponse.data.todayToken.toString()
                prevTokenEstd = mDashResponse.data.todayToken
                Log.e("Tokens", "of user$prevTokenEstd")
                totl_dist.text = mDashResponse.data.totalUserDistance.toString()
                totl_dist_suffix.text = "Km"
                when {
                    mDashResponse.data.wearableDevice != null -> {
                        SharedPreferencesManager.setString(
                            requireContext(),
                            mDashResponse.data.wearableDevice,
                            PREF_CURR_WEARABLE
                        )
                        mWearID = SharedPreferencesManager.getString(
                            requireContext(),
                            PREF_CURR_WEARABLE
                        )
                    }
                    mDashResponse.data.wearableDevice == null -> {
                        SharedPreferencesManager.setString(
                            requireContext(), null,
                            PREF_CURR_WEARABLE
                        )
                        mWearID = null
                        Log.e("Wear Id nulll home", "" + mWearID)

                    }
                    else -> if (SharedPreferencesManager.hasKey(
                            requireContext(),
                            PREF_CURR_WEARABLE
                        )
                    ) {
                        mWearID =
                            SharedPreferencesManager.getString(requireContext(), PREF_CURR_WEARABLE)
                    }

                }
                Log.e("Wear saved currently", "" + mWearID)
                if (Integer.parseInt(steps.text.toString()) > 0)
                    calculateEstdToken(steps.text.toString())
//                }
                avgSteps_count.text = mDashResponse.data.averageSteps.toString()
                WalletFragment.instance.setUpdatedSteps(mDashResponse.data.totalUserSteps.toString())
                Log.i("total", "steps" + mDashResponse.data.totalUserSteps.toString())
                if (mDashResponse.data.closestToken.toString() != "0") {
                    wishListCloseLayout.visibility = View.VISIBLE
                    wishlistTxt.text =
                        "You are only ${mDashResponse.data.closestToken} Tokens away from an item on your wish list! Keep walking!"
                } else {
                    wishListCloseLayout.visibility = View.GONE
                }
                company_rank_count.text = ordinal(mDashResponse.data.companyRank)
                tokensVal.text = mDashResponse.data.totalUserToken.toString()
                SharedPreferencesManager.setString(
                    mContext, mDashResponse.data.totalUserToken.toString(),
                    SharedPreferencesManager.EARNEDTOKENS
                )

                if (mDashResponse.data.lastUpdated != null) {
                    SharedPreferencesManager.setString(
                        activity!!,
                        mDashResponse.data.lastUpdated,
                        SYNCDATE
                    )
//                    SharedPreferencesManager.saveSyncObject(activity!!,syncDataFromServer())
                }
                if (mDashResponse.data.activity.isEmpty()) {
                    Toast.makeText(requireContext(), "No historic data found", Toast.LENGTH_LONG)
                        .show()
                }
                limit = 10000
                setBarChart("STEPS", limit)

            })
        checkIfDeviceApprovedOrNot()


        /**
         * Swipe layout listener
         */

        swipeLayout.setOnRefreshListener {
            if (SharedPreferencesManager.hasKey(
                    requireActivity(),
                    WEARABLEID
                )
            ) {
                if (SharedPreferencesManager.getBoolean(
                        requireContext(),
                        VAR_WEARABLE_CONNECTED
                    ) && mWearID == null
                ) {
                    showReconnection()
                } else if (mWearID != null && (SharedPreferencesManager.getString(
                        requireActivity(),
                        WEARABLEID
                    ) == mWearID && !MokoSupport.getInstance().isConnDevice(
                        requireContext(),
                        mWearID
                    ))
                ) {
                    /*try {
                        MokoSupport.getInstance().connDevice(requireContext(), mWearID, this)
                    } catch (e: Exception) {
                        Log.e("reconnect", "failed" + e.message)
                    }*/
                    (mContext as LandingActivity).reconnectDevice()

                } else if ( mWearID!=null && SharedPreferencesManager.getString(
                        mContext,
                        WEARABLEID
                    ).isNotEmpty() && SharedPreferencesManager.getString(
                        requireActivity(), WEARABLEID
                    ) != mWearID
                ) {
                    if (mAlertDialog == null || !mAlertDialog?.isShowing!!) {
                        showUnauthorizedDialog()
                    }
                }
            } else {
                showReconnection()
            }
            dismissSwipe()
        }

        leaderboardTxt.setOnClickListener {

            startActivity(
                Intent(ChallengesFragment.mContext, LeaderboardActivity::class.java).putExtra(
                    INTENT_HOME,
                    true
                )
            )

        }


        txtGraphFilter.setOnClickListener {
            val dialog =
                OptionDialog(activity!!, R.style.pullBottomfromTop, R.layout.dialog_options,
                    optionArray,
                    "SELECT FILTER",
                    InterfacesCall.Callback { pos ->
                        txtGraphFilter.text = optionArray[pos].name
                        setFilterOptionArray()
                        optionArray[pos].isSelected = true
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            mViewModel!!.fetchSyncData(
                                getFetchDeviceDataRequest()
                            )
                        }

                    })

            dialog.show()
        }

        steps_title.setOnClickListener {
            steps_title.setTextColor(activity!!.resources.getColor(R.color.colorBlack))
            token_title.setTextColor(activity!!.resources.getColor(R.color.gray_text))
            distance.setTextColor(activity!!.resources.getColor(R.color.gray_text))
            limit = 10000
            setBarChart("STEPS", limit)

        }

        leaderDash.setOnClickListener {
            (activity!! as LandingActivity).onFragment(5)
        }

        spndTokens.setOnClickListener {
            //            callback.onFragmentClick(0)
            (mContext as LandingActivity).onFragment(0)
        }

        token_title.setOnClickListener {
            token_title.setTextColor(mContext.resources.getColor(R.color.colorBlack))
            steps_title.setTextColor(mContext.resources.getColor(R.color.gray_text))
            distance.setTextColor(mContext.resources.getColor(R.color.gray_text))
            limit = 10
            setBarChart("TOKEN", limit)

        }

        distance.setOnClickListener {
            distance.setTextColor(mContext.resources.getColor(R.color.colorBlack))
            steps_title.setTextColor(mContext.resources.getColor(R.color.gray_text))
            token_title.setTextColor(mContext.resources.getColor(R.color.gray_text))
            limit = 0
            setBarChart("DISTANCE", limit)

        }
        spndTokens.setOnClickListener {
            //            callback.onFragmentClick(0)
            (mContext as LandingActivity).onFragment(2)
        }


    }

    private fun checkIfDeviceApprovedOrNot() {
        if (SharedPreferencesManager.hasKey(requireContext(), PREF_CURR_WEARABLE)) {
            mWearID =
                SharedPreferencesManager.getString(requireContext(), PREF_CURR_WEARABLE)
        }
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            if (mWearID != null && SharedPreferencesManager.getString(
                    mContext,
                    WEARABLEID
                ).isNotEmpty()&&SharedPreferencesManager.getString(
                    mContext,
                    WEARABLEID
                ) != mWearID
            ) {
                if (mAlertDialog == null || !mAlertDialog?.isShowing!! && !cancelledOnce) {
                    showUnauthorizedDialog()
                }
            } else {
                if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
                    try {
                        if (SharedPreferencesManager.getString(
                                mContext,
                                WEARABLEID
                            ) == mWearID || mWearID.isNullOrEmpty()
                        ) {
                            cancelledOnce = false
                            dismissAuthorieDialog()
                            Log.i("Steps", "request when reconnecting" + getActivityData())

                            (activity as LandingActivity).reconnectDevice()

                           /* mViewModel !!. syncDevice (
                                        SyncRequest(
                                            getActivityData(),
                                            SharedPreferencesManager.getUserId(mContext),
                                            SharedPreferencesManager.getString(
                                                mContext,
                                                WEARABLEID
                                            ),
                                            SharedPreferencesManager.getString(
                                                mContext,
                                                FIREBASETOKEN
                                            )
                                        )
                                        )*/
                        }

                    } catch (e: Exception) {
                        Log.e("Viewmodel", "reconnecting Exception" + e.printStackTrace())
                    }
                }
            }
        }
    }

    private fun checkElcies() {
        if (SharedPreferencesManager.hasKey(
                requireContext(),
                VAR_ELCIES_CONNCTED
            )
        ) {
            swipeLayout.isEnabled = false
        }
    }

    private fun getFetchDeviceDataRequest(): FetchDeviceDataRequest {
        return if (txtGraphFilter.text.toString().equals(
                WEEKLY,
                ignoreCase = true
            )
        ) {
            FetchDeviceDataRequest(
                WEEKLY,
                SharedPreferencesManager.getUserId(activity!!)
                , Utils.parseTimeOffset()
            )
        } else {
            FetchDeviceDataRequest(
                MONTHLY,
                SharedPreferencesManager.getUserId(activity!!),
                Utils.parseTimeOffset()
            )
        }
    }

    private fun updateProgressCircle() {
        Handler().postDelayed({
            swipeListener?.showSwipe()          //to enable swipe
            if (circular_progress != null) {
                circular_progress.setProgressTextAdapter(PatternProgressTextAdapter())
                circular_progress.setProgress(prevTokenEstd!!.toDouble(), 10.00)
            }

        }, 2000)
    }


    private fun setFilterOptionArray() {
        optionArray.clear()
        if (txtGraphFilter.text.toString().equals(WEEKLY, true)) {
            optionArray.add(OptionsModel(0, WEEKLY, true))
        } else {
            optionArray.add(OptionsModel(0, WEEKLY, false))
        }

        if (txtGraphFilter.text.toString().equals(MONTHLY, true)) {
            optionArray.add(OptionsModel(1, MONTHLY, true))
        } else {
            optionArray.add(OptionsModel(1, MONTHLY, false))
        }
    }


    private fun getActivityData(): ArrayList<Activity>? {
        activityList = ArrayList()
        var newList: ArrayList<DailyStep>? = ArrayList()

        if (SharedPreferencesManager.hasKey(mContext, SharedPreferencesManager.VAR_WEARABLE)) {
            var list: ArrayList<DailyStep>? = SharedPreferencesManager.getSyncObject(mContext)
            if (!list.isNullOrEmpty()) {
                if (SharedPreferencesManager.hasKey(mContext, SYNCDATE)) {
                    var lastSyncDate = changeDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS",
                        "yyyy-MM-dd",
                        SharedPreferencesManager.getString(requireContext(), SYNCDATE)
                    )
                    list?.forEachIndexed { index, _ ->
                        if (list[index].date == lastSyncDate) {
                            if (index == list.lastIndex) {
                                newList?.add(list[index])
                            } else {
                                newList?.addAll(list.subList(index, list.lastIndex))
                            }
                        }
                    }
                } else {
                    newList?.clear()
                    newList?.addAll(list)
                }

                newList!!.iterator().forEach {
                    activityList!!.add(
                        Activity(
                            it.date,
                            it.distance.toDouble(),
                            it.duration.toInt(),
                            it.count.toInt(),
                            it.calories.toInt()
                        )
                    )
                }
                Log.i("Size", "list home 1" + activityList!!.size)
            }
        }
        return activityList
    }


    /**
     * display health data from server
     */
    private fun setStepsText() {
        val stepCount = SpannableString("" + circular_progress.progress)
        val spannable = SpannableString("TODAY")
        val tokensMsg = SpannableString("Tokens")
        tokensMsg.setSpan(StyleSpan(Typeface.BOLD), 0, tokensMsg.length, 0)

        spannable.setSpan(
            ForegroundColorSpan(Color.CYAN),
            0, 5,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        stepCount.setSpan(StyleSpan(Typeface.BOLD), 0, stepCount.length, 0)
        totalsteps.text = spannable
        totalstepsCount.text = stepCount
        stepsmsg.text = tokensMsg
    }

    private fun initBarChart() {
        barchart.setDrawBarShadow(false)
//        barchart.setDrawValueAboveBar(true)

        barchart.description.isEnabled = false
        barchart.setMaxVisibleValueCount(10)

        barchart.setPinchZoom(false)

        barchart.setDrawGridBackground(false)

        xAxis = barchart.xAxis
        xAxis!!.position = XAxisPosition.BOTTOM
        xAxis!!.setDrawGridLines(false)
        xAxis!!.granularity = 1f // only intervals of 1 day
        xAxis!!.labelCount = 7

        var xAxisFormatter: DayAxisValueFormatter =
            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                DayAxisValueFormatter(barchart, WEEKLY, mWeekListFormater)
            } else {
                DayAxisValueFormatter(barchart, MONTHLY, mMonthListFormater)
            }

        var XAx = barchart.xAxis
        XAx.valueFormatter = xAxisFormatter

    }


    private fun setBarChart(format: String, limit: Int) {
        if (mDataList!!.activity != null) {
            var weeklyData: ArrayList<BarEntry> = addDataFromServer(format)
            val bardataset = MyBarDataSet(weeklyData, "", limit)
            bardataset.colors = colors
            barchart.data = null
            barchart.animateY(5000)
            barchart.legend.isEnabled = false   // Hide the legend
            var data = BarData(bardataset)
            barchart.data = data
            barchart.data.isHighlightEnabled = false
        }
    }

    private fun addDataFromServer(format: String): ArrayList<BarEntry> {
        var avgDailySteps = 0
        var numDays = 0
        val graphData = ArrayList<BarEntry>()
        if (format == "STEPS") {
            if (txtGraphFilter != null && txtGraphFilter.text.toString().equals(
                    WEEKLY,
                    ignoreCase = true
                )
            ) {
                if (mDataList!!.activity != null) {
                    if (mDataList!!.activity.size > 7) {
                        mDataList!!.activity.removeAt(0)
                    }
                    try {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.steps.toFloat()))
                            mWeekListFormater[index] =
                                changeDateFormat(
                                    "yyyy-MM-dd",
                                    "E_dd MMM, yyyy",
                                    element.date
                                ).split("_")[0]
                            if (!mWeekListFormater[index].equals("Fri") && (!mWeekListFormater[index].equals(
                                    "Sat"
                                ))
                            ) {
                                if (element.steps > 0) {
                                    numDays++
                                    avgDailySteps += element.steps
                                }
                            }
                        }

                        Log.e("Avg user", "steps" + avgDailySteps / numDays)
                        SharedPreferencesManager.setString(
                            requireContext(),
                            (avgDailySteps / numDays).toString(),
                            AVGSTEPS
                        )

                    } catch (e: Exception) {
                        Log.e("weekly", "data invalidate")
                    }
                }
            } else {
                if (mDataList!!.activity != null) {
                    if (mDataList!!.activity.size > 31) {
                        mDataList!!.activity.subList(
                            (mDataList!!.activity.size - 32),
                            (mDataList!!.activity.size - 1)
                        )
                            .forEachIndexed { index, element ->
                                graphData.add(
                                    index,
                                    BarEntry(index.toFloat(), element.steps.toFloat())
                                )
                                mMonthListFormater[index] =
                                    changeDateFormat(
                                        "yyyy-MM-dd",
                                        "dd-MMM",
                                        element.date
                                    ).split("_")[0]

                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.steps.toFloat()))
                            mMonthListFormater[index] = changeDateFormat(
                                "yyyy-MM-dd",
                                "dd-MMM",
                                element.date
                            ).split("_")[0]

                        }
                    }
                }
            }
        } else if (format == "TOKEN") {
            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                if (mDataList!!.todayToken != null) {
                    if (mDataList!!.activity.size > 7) {
                        mDataList!!.activity.subList(
                            (mDataList!!.activity.size - 8),
                            (mDataList!!.activity.size - 1)
                        )
                            .forEachIndexed { index, element ->
                                graphData.add(
                                    index,
                                    BarEntry(index.toFloat(), element.token.toFloat())
                                )
                                mWeekListFormater[index] =
                                    changeDateFormat(
                                        "yyyy-MM-dd",
                                        "E_dd MMM, yyyy",
                                        element.date
                                    ).split("_")[0]
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.token.toFloat()))
                        }
                    }
                }

            } else {
                if (mDataList!!.activity != null) {
                    if (mDataList!!.activity.size > 31) {
                        mDataList!!.activity.subList(
                            (mDataList!!.activity.size - 32),
                            (mDataList!!.activity.size - 1)
                        )
                            .forEachIndexed { index, element ->
                                graphData.add(
                                    index,
                                    BarEntry(index.toFloat(), element.token.toFloat())
                                )
                                mMonthListFormater.set(
                                    index,
                                    element.date.toString().replace("2019-", "")
                                )
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.token.toFloat()))
                            mMonthListFormater.set(
                                index,
                                element.date.toString().replace("2019-", "")
                            )
                        }
                    }
                }
            }
        } else if (format == "DISTANCE") {
            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                if (mDataList!!.todayToken != null) {
                    mDataList!!.activity.forEachIndexed { index, element ->
                        graphData.add(index, BarEntry(index.toFloat(), element.distance.toFloat()))
                        mWeekListFormater[index] =
                            changeDateFormat(
                                "yyyy-MM-dd",
                                "E_dd MMM, yyyy",
                                element.date
                            ).split("_")[0]
                    }
                }
            } else {
                if (mDataList!!.activity != null) {
                    if (mDataList!!.activity.size > 31) {
                        mDataList!!.activity.subList(
                            (mDataList!!.activity.size - 32),
                            (mDataList!!.activity.size - 1)
                        )
                            .forEachIndexed { index, element ->
                                graphData.add(
                                    index,
                                    BarEntry(index.toFloat(), element.distance.toFloat())
                                )
                                mMonthListFormater.set(
                                    index,
                                    element.date.toString().replace("2019-", "")
                                )
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(
                                index,
                                BarEntry(index.toFloat(), element.distance.toFloat())
                            )
                            mMonthListFormater.set(
                                index,
                                element.date.toString().replace("2019-", "")
                            )
                        }
                    }
                }
            }
        }
        initBarChart()
        return graphData
    }


    fun setCurrentSteps(dailyStep: DailyStep) {
        if (dailyStep != null) {
            updating = true
            Log.e(
                "Updating",
                "steps" + dailyStep.count.toInt().toString()
            )

            if ((steps != null && totl_dist != null)) {
                steps.text = dailyStep.count.toInt().toString()
                totl_dist.text = dailyStep.distance.toString()
                totl_dist_suffix.text = "Km"
                if (Integer.parseInt(steps.text.toString()) > 0) {
                    calculateEstdToken(steps.text.toString())
                }
                if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

//                    Handler().postDelayed({
                    try {
                        if (SharedPreferencesManager.getString(
                                mContext,
                                WEARABLEID
                            ) == mWearID || mWearID.isNullOrEmpty()
                        ) {
                            cancelledOnce = false
                            Log.i("Steps", "request home" + getLatestActivityData(dailyStep))
                            mViewModel!!.syncDevice(
                                SyncRequest(
                                    getLatestActivityData(dailyStep),
                                    SharedPreferencesManager.getUserId(mContext),
                                    SharedPreferencesManager.getString(mContext, WEARABLEID),
                                    SharedPreferencesManager.getString(mContext, FIREBASETOKEN)
                                )
                            )
                        }

                    } catch (e: Exception) {
                        Log.e("Viewmodel", "Exception" + e.printStackTrace())
                    }
//                    }, 2000)
                }
            }


        }
    }

    /**
     * tokensEstimation
     */

    private fun calculateEstdToken(dailyStep: String) {
        if (mDataList?.todayToken != null) {
            tokenEstd = when {
                (dailyStep.toInt()) > 10000 -> 10
                dailyStep.toInt() > 999 -> dailyStep.toInt() / 1000
                else -> mDataList?.todayToken!!
            }
        }

        if ((tokenEstd > 0 && totalstepsCount.text.toString() != "0.0") && tokenEstd != totalstepsCount.text.toString().toInt()) {
            Log.e("Tokens chnaging", "of user$prevTokenEstd")
            prevTokenEstd = tokenEstd
            totalstepsCount.text = tokenEstd.toString()
            updateProgressCircle()
        }

    }

    private fun getLatestActivityData(dailyStep: DailyStep): ArrayList<Activity>? {
        var activityList: ArrayList<Activity>? = ArrayList()
        activityList!!.add(
            Activity(
                dailyStep.date,
                dailyStep.distance.toDouble(),
                dailyStep.duration.toInt(),
                dailyStep.count.toInt(),
                dailyStep.calories.toInt()
            )
        )
        return activityList
    }

    override fun onResume() {
        super.onResume()

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
        }
        (mContext as LandingActivity).checkDeviceConnection()
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            mViewModel!!.fetchSyncData(
                getFetchDeviceDataRequest()
            )
        }

    }


    fun syncingWearableMsg(bool: Boolean) {
        if (syncTxtMsg != null) {
            if (bool) {
                syncTxtMsg.visibility = View.VISIBLE
            } else {
                syncTxtMsg.visibility = View.GONE
            }
        }

    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            Log.i("visible", "home")
            afterSync = false
            (mContext as LandingActivity).checkDeviceConnection()
//            initBarChart()
//            setBarChart("STEPS")
        }
    }

    /**
     * To dismiss swipe refresh
     */


    private fun dismissSwipe() {
        if (swipeLayout.isRefreshing) {
            swipeLayout.isRefreshing = false
        }

    }

    private fun showReconnection() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("You are not connected with wearable device, press OK for connection")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, id ->
                startActivity(
                    Intent(requireActivity(), SyncDeviceActivity::class.java).putExtra(
                        AppConstants.INTENT_RECONNECT,
                        true
                    )
                )
            }
            .setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    /**
     * Popup to display when connected device is different tahn previous device
     */


    private fun showUnauthorizedDialog() {

        (mContext as LandingActivity).showDisconnection(true)
        val mDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_unauthorized_device, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
        //show dialog
        mAlertDialog = mBuilder.show()
        mDialogView.contactBttn.setOnClickListener {
            //dismiss dialog
            /* startActivity(
                 Intent(
                     requireContext(),
                     LandingActivity::class.java
                 ).putExtra("contactUs", true)
             )*/
            (mContext as LandingActivity).openContactUsFragment(true)
            mAlertDialog?.dismiss()

        }
        mDialogView.cancelBttn.setOnClickListener {
            //dismiss dialog
            cancelledOnce = true
            mAlertDialog?.dismiss()
        }
    }


    fun dismissAuthorieDialog() {
        try {
            mAlertDialog?.dismiss()
        } catch (e: Exception) {
            Log.e("dismiss", "popup")
        }
    }

}