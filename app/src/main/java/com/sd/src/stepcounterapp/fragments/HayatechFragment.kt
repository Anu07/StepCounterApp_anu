package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fitpolo.support.entity.DailyStep
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.adapter.PatternProgressTextAdapter
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.dialog.OptionDialog
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.DeviceResponse.DashboardResponse
import com.sd.src.stepcounterapp.model.DeviceResponse.Data
import com.sd.src.stepcounterapp.model.OptionsModel
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.syncDevice.Activity
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.utils.DayAxisValueFormatter
import com.sd.src.stepcounterapp.utils.InterConsts.MONTHLY
import com.sd.src.stepcounterapp.utils.InterConsts.WEEKLY
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.FIREBASETOKEN
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.WEARABLEID
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.utils.Utils.getCurrentDate
import com.sd.src.stepcounterapp.viewModels.DeviceViewModel
import kotlinx.android.synthetic.main.fragment_hayatech.*


class HayatechFragment : BaseFragment() {


    interface SwipeVisibiltyListener {
        fun showSwipe()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: HayatechFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): HayatechFragment {
            instance = HayatechFragment()
            mContext = context
            return instance
        }
    }

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
    val colors: IntArray = intArrayOf(R.color.green_txt, R.color.blue_txt)


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
            swipeListener = (context as LandingActivity)

            if (mViewModel == null) {
                mViewModel = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
            }
            Log.i("attach", "viewmodel")
            (context as LandingActivity).disableSwipe(false)
        } catch (e: Exception) {

            Log.e("exception","message"+e.message)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFilterOptionArray()

        distance.isSelected = true


        setStepsText()
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
        }
//        showPopupProgressSpinner(true)
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            mViewModel!!.fetchSyncData(
                if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                    FetchDeviceDataRequest(WEEKLY, SharedPreferencesManager.getUserId(activity!!),Utils.parseTimeOffset())
                } else {
                    FetchDeviceDataRequest(MONTHLY, SharedPreferencesManager.getUserId(activity!!),Utils.parseTimeOffset())
                }
            )
        }

        mViewModel!!.getSyncResponse().observe(this,
            Observer<BasicInfoResponse> {
                Log.i("Sync", "Data synced successfully")
//                afterSync = true
//                callDashboard()
            })
        mViewModel!!.getDashResponse().observe(this,
            Observer<DashboardResponse> { mDashResponse ->
                //                showPopupProgressSpinner(false)

                mDataList = mDashResponse.data
//                if (!afterSync) {
                steps.text = mDashResponse.data.totalUserSteps.toString()
                totalstepsCount.text = mDashResponse.data.todayToken.toString()
                prevTokenEstd = mDashResponse.data.todayToken
                totl_dist.text = mDashResponse.data.totalUserDistance.toString()
                totl_dist_suffix.text = "Km"
                if (Integer.parseInt(steps.text.toString()) > 0)
                    calculateEstdToken(steps.text.toString())
//                }

                WalletFragment.instance.setUpdatedSteps(mDashResponse.data.totalUserSteps.toString())
                Log.i("total", "steps" + mDashResponse.data.totalUserSteps.toString())
                if (mDashResponse.data.closestToken.toString() != "0") {
                    wishListCloseLayout.visibility = View.VISIBLE
                    wishlistTxt.text =
                        "You are only ${mDashResponse.data.closestToken} Tokens away from an item on your wish list! Keep walking!"
                } else {
                    wishListCloseLayout.visibility = View.GONE
                }
                company_rank_count.text = mDashResponse.data.companyRank.toString()
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

                setBarChart("STEPS")

                swipeListener?.showSwipe()          //to enable swipe

            })

        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            if (SharedPreferencesManager.hasKey(activity!!, SharedPreferencesManager.VAR_WEARABLE)) {

                mViewModel!!.syncDevice(
                    SyncRequest(
                        getActivityData(),
                        SharedPreferencesManager.getUserId(activity!!),
                        SharedPreferencesManager.getString(activity!!, WEARABLEID),
                        SharedPreferencesManager.getString(activity!!, FIREBASETOKEN)
                    )
                )
            }
        }


        circular_progress.setProgressTextAdapter(PatternProgressTextAdapter())
        circular_progress.setProgress(prevTokenEstd!!.toDouble(), 10.00)

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
                                if (txtGraphFilter.text.toString().equals(
                                        WEEKLY,
                                        ignoreCase = true
                                    )
                                ) {
                                    FetchDeviceDataRequest(
                                        WEEKLY,
                                        SharedPreferencesManager.getUserId(activity!!)
                                    ,Utils.parseTimeOffset())
                                } else {
                                    FetchDeviceDataRequest(
                                        MONTHLY,
                                        SharedPreferencesManager.getUserId(activity!!),Utils.parseTimeOffset()
                                    )
                                }
                            )
                        }

                    })

            dialog.show()
        }

        steps_title.setOnClickListener {
            steps_title.setTextColor(activity!!.resources.getColor(R.color.colorBlack))
            token_title.setTextColor(activity!!.resources.getColor(R.color.gray_text))
            distance.setTextColor(activity!!.resources.getColor(R.color.gray_text))
            setBarChart("STEPS")

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
            setBarChart("TOKEN")

        }

        distance.setOnClickListener {
            distance.setTextColor(mContext.resources.getColor(R.color.colorBlack))
            steps_title.setTextColor(mContext.resources.getColor(R.color.gray_text))
            token_title.setTextColor(mContext.resources.getColor(R.color.gray_text))
            setBarChart("DISTANCE")

        }
        spndTokens.setOnClickListener {
            //            callback.onFragmentClick(0)
            (mContext as LandingActivity).onFragment(2)
        }


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
            if (SharedPreferencesManager.hasKey(mContext, SYNCDATE)) {
                var lastSyncDate =
                    SharedPreferencesManager.getString(mContext, SYNCDATE)!!.split("T")[0]
                list?.forEachIndexed { index, _ ->
                    if (list[index].date == lastSyncDate) {
                        newList?.addAll(list.subList(index, list.lastIndex))
                    }
                }
            } else {
                newList = list
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
            Log.i("Size", "list" + activityList!!.size)
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

        var leftAxis = barchart.axisLeft
        val ll = LimitLine(10f, "")
        ll.lineColor = Color.GRAY
        ll.lineWidth = 4f
        ll.textSize = 12f
        var xAxisFormatter: DayAxisValueFormatter =
            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                DayAxisValueFormatter(barchart, WEEKLY, mWeekListFormater)
            } else {
                DayAxisValueFormatter(barchart, MONTHLY, mMonthListFormater)
            }

        leftAxis.addLimitLine(ll)
        var XAx = barchart.xAxis
        XAx.valueFormatter = xAxisFormatter

    }


    private fun setBarChart(format: String) {
        if (mDataList!!.activity != null) {
            var weeklyData: ArrayList<BarEntry> = addDataFromServer(format)
            val bardataset = BarDataSet(weeklyData, "")
            barchart.data = null
//            bardataset.setColors(colors, mContext)
            weeklyData.iterator().forEach {
                if (it.y < 10000.0f) {
                    bardataset.valueTextColor = colors[1]
                } else {
                    bardataset.valueTextColor = colors[0]
                }
            }
            barchart.animateY(5000)
            barchart.legend.isEnabled = false   // Hide the legend
            val data = BarData(bardataset)
            barchart.data = data
            barchart.data.isHighlightEnabled = false
        }
    }

    private fun addDataFromServer(format: String): ArrayList<BarEntry> {
        val graphData = ArrayList<BarEntry>()
        if (format == "STEPS") {
            if (txtGraphFilter != null && txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                if (mDataList!!.activity != null) {
                    if(mDataList!!.activity.size>7){
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
                        }
                    } catch (e: Exception) {
                        Log.e("weekly","data invalidate")
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

                    Handler().postDelayed({
                        try {
                            mViewModel!!.syncDevice(
                                SyncRequest(
                                    getLatestActivityData(dailyStep),
                                    SharedPreferencesManager.getUserId(mContext),
                                    SharedPreferencesManager.getString(mContext, WEARABLEID),
                                    SharedPreferencesManager.getString(mContext, FIREBASETOKEN)
                                )
                            )
                        } catch (e: Exception) {
                            Log.e("Viewmodel", "Exception" + e.printStackTrace())
                        }
                    }, 2000)

                }
            }


        }
    }

    /**
     * tokensEstimation
     */

    private fun calculateEstdToken(dailyStep: String) {
        tokenEstd = when {
            (dailyStep.toInt()) > 10000 -> 10
            dailyStep.toInt() > 999 -> dailyStep.toInt() / 1000
            else -> mDataList!!.todayToken
        }
//        Log.i("Calculated", "Token$" + totalstepsCount.text.toString().toInt())

        if ((tokenEstd > 0 && totalstepsCount.text.toString() != "0.0") && tokenEstd != totalstepsCount.text.toString().toInt()) {
            totalstepsCount.text = tokenEstd.toString()
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

            mViewModel!!.syncDevice(
                SyncRequest(
                    getActivityData(),
                    SharedPreferencesManager.getUserId(mContext),
                    SharedPreferencesManager.getString(mContext, WEARABLEID),
                    SharedPreferencesManager.getString(mContext, FIREBASETOKEN)
                )
            )
        }
    }

    fun callDashboard() {
        mViewModel!!.fetchSyncData(
            FetchDeviceDataRequest(WEEKLY, SharedPreferencesManager.getUserId(mContext),Utils.parseTimeOffset())
        )
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
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("visible", "home")
            afterSync = false
            (mContext as LandingActivity).checkDeviceConnection()
//            initBarChart()
//            setBarChart("STEPS")
        }
    }

}