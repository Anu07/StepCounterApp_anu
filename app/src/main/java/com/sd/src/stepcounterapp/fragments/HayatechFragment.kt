package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings
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
import com.fitpolo.support.MokoSupport
import com.fitpolo.support.entity.DailyStep
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.adapter.PatternProgressTextAdapter
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
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.viewModels.DeviceViewModel
import kotlinx.android.synthetic.main.fragment_hayatech.*


class HayatechFragment : BaseFragment() {

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

    private var updating: Boolean = false
    //    internal lateinit var callback: MarketPlaceFragment.FragmentClick
//
//    fun FragmentClickListener(callback: MarketPlaceFragment.FragmentClick) {
//        this.callback = callback
//    }
    var activityList: ArrayList<Activity>? = null
    private var mDataList: Data? = Data()
    private lateinit var mViewModel: DeviceViewModel
    var optionArray = arrayListOf<OptionsModel>()
    var android_id: String? = null
    var xAxis: XAxis? = null
    private var mMonthListFormater = arrayOfNulls<String>(31)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hayatech, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFilterOptionArray()

        mViewModel = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
        circular_progress.setProgressTextAdapter(PatternProgressTextAdapter())
        setStepsText()
        showPopupProgressSpinner(true)
        mViewModel.fetchSyncData(
            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                FetchDeviceDataRequest(WEEKLY, SharedPreferencesManager.getUserId(mContext))
            } else {
                FetchDeviceDataRequest(MONTHLY, SharedPreferencesManager.getUserId(mContext))
            }
        )
        mViewModel.getSyncResponse().observe(this,
            Observer<BasicInfoResponse> { mResponse ->
                Log.i("Sync", "Data synced successfully")
                callDashboard()
            })
        mViewModel.getDashResponse().observe(this,
            Observer<DashboardResponse> { mDashResponse ->
                showPopupProgressSpinner(false)
                mDataList = mDashResponse.data
                if (!updating || MokoSupport.getInstance().isConnDevice(
                        mContext, SharedPreferencesManager.getString(
                            mContext,
                            SharedPreferencesManager.WEARABLEID
                        )
                    )
                ) {
                    steps.text = mDashResponse.data.totalUserSteps.toString()
                    totalstepsCount.text = mDashResponse.data.todayToken.toString()
                    totl_dist.text = String.format("%.2f", mDashResponse.data.totalUserDistance)
                    totl_dist_suffix.text = "Km"
                }
                Log.i("total", "steps" + mDashResponse.data.totalUserSteps.toString())

                circular_progress.setProgress(mDashResponse.data.todayToken.toDouble(), 10.00)
                company_rank_count.text = mDashResponse.data.companyRank.toString()
                tokensVal.text = mDashResponse.data.totalUserToken.toString()
                if (mDashResponse.data.lastUpdated != null) {
                    SharedPreferencesManager.setString(mContext, mDashResponse.data.lastUpdated, SYNCDATE)
//                    SharedPreferencesManager.saveSyncObject(mContext,syncDataFromServer())
                }
                setBarChart("STEPS")
            })

        if (SharedPreferencesManager.hasKey(mContext, "Wearable")) {
            android_id = Settings.Secure.getString(
                mContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )

            mViewModel.syncDevice(
                SyncRequest(
                    getActivityData(),
                    SharedPreferencesManager.getUserId(mContext),
                    android_id
                )
            )
        }

        txtGraphFilter.setOnClickListener {
            val dialog =
                OptionDialog(mContext, R.style.pullBottomfromTop, R.layout.dialog_options,
                    optionArray,
                    "SELECT FILTER",
                    InterfacesCall.Callback { pos ->
                        txtGraphFilter.text = optionArray[pos].name
                        setFilterOptionArray()
                        optionArray[pos].isSelected = true
                        mViewModel.fetchSyncData(
                            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                                FetchDeviceDataRequest(WEEKLY, SharedPreferencesManager.getUserId(mContext))
                            } else {
                                FetchDeviceDataRequest(MONTHLY, SharedPreferencesManager.getUserId(mContext))
                            }
                        )

                    })
            dialog.show()
        }

        steps_title.setOnClickListener {
            steps_title.setTextColor(mContext.resources.getColor(R.color.colorBlack))
            token_title.setTextColor(mContext.resources.getColor(R.color.gray_text))
            distance.setTextColor(mContext.resources.getColor(R.color.gray_text))
            setBarChart("STEPS")

        }

        leaderboardTxt.setOnClickListener {
            (mContext as LandingActivity).onFragmnet(5)
        }

        spndTokens.setOnClickListener {
            //            callback.onFragmentClick(0)
            (mContext as LandingActivity).onFragmnet(0)
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
        if (SharedPreferencesManager.hasKey(mContext, "Wearable")) {
            var list: ArrayList<DailyStep>? = SharedPreferencesManager.getSyncObject(mContext)
            list!!.iterator().forEach {
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
                DayAxisValueFormatter(barchart, WEEKLY)
            } else {
                DayAxisValueFormatter(barchart, MONTHLY, mMonthListFormater)
            }

        leftAxis.addLimitLine(ll)
        var XAx = barchart.xAxis
        XAx.valueFormatter = xAxisFormatter

    }


    private fun setBarChart(format: String) {
        var weeklyData: ArrayList<BarEntry> = addDataFromServer(format)
        val bardataset = BarDataSet(weeklyData, "")
        barchart.data = null
        bardataset.color = Color.parseColor("#8DC540")
        barchart.animateY(5000)
        val data = BarData(bardataset)
        barchart.data = data
    }

    private fun addDataFromServer(format: String): ArrayList<BarEntry> {
        val graphData = ArrayList<BarEntry>()
        if (format == "STEPS") {
            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                if (mDataList!!.activity != null) {
                    mDataList!!.activity.forEachIndexed { index, element ->
                        graphData.add(index, BarEntry(index.toFloat(), element.steps.toFloat()))
                    }
                }
            } else {
                if (mDataList!!.activity != null) {
                    if (mDataList!!.activity.size > 31) {
                        mDataList!!.activity.subList((mDataList!!.activity.size - 32), (mDataList!!.activity.size - 1))
                            .forEachIndexed { index, element ->
                                graphData.add(index, BarEntry(index.toFloat(), element.steps.toFloat()))
                                mMonthListFormater[index] = element.date.toString().replace("2019-", "")
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.steps.toFloat()))
                            mMonthListFormater[index] = element.date.toString().replace("2019-", "")
                        }
                    }
                }
            }
        } else if (format == "TOKEN") {
            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                if (mDataList!!.todayToken != null) {
                    if (mDataList!!.activity.size > 7) {
                        mDataList!!.activity.subList((mDataList!!.activity.size - 8), (mDataList!!.activity.size - 1))
                            .forEachIndexed { index, element ->
                                graphData.add(index, BarEntry(index.toFloat(), element.token.toFloat()))
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
                        mDataList!!.activity.subList((mDataList!!.activity.size - 32), (mDataList!!.activity.size - 1))
                            .forEachIndexed { index, element ->
                                graphData.add(index, BarEntry(index.toFloat(), element.token.toFloat()))
                                mMonthListFormater.set(index, element.date.toString().replace("2019-", ""))
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.token.toFloat()))
                            mMonthListFormater.set(index, element.date.toString().replace("2019-", ""))
                        }
                    }
                }
            }
        } else if (format == "DISTANCE") {
            if (txtGraphFilter.text.toString().equals(WEEKLY, ignoreCase = true)) {
                if (mDataList!!.todayToken != null) {
                    mDataList!!.activity.forEachIndexed { index, element ->
                        graphData.add(index, BarEntry(index.toFloat(), element.distance.toFloat()))
                    }
                }
            } else {
                if (mDataList!!.activity != null) {
                    if (mDataList!!.activity.size > 31) {
                        mDataList!!.activity.subList((mDataList!!.activity.size - 32), (mDataList!!.activity.size - 1))
                            .forEachIndexed { index, element ->
                                graphData.add(index, BarEntry(index.toFloat(), element.distance.toFloat()))
                                mMonthListFormater.set(index, element.date.toString().replace("2019-", ""))
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.distance.toFloat()))
                            mMonthListFormater.set(index, element.date.toString().replace("2019-", ""))
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
            if (steps != null && totl_dist != null){
                steps.text = dailyStep.count.toInt().toString()
                totl_dist.text = dailyStep.distance.toDouble().toString()
                totl_dist_suffix.text = "Km"
            }

            mViewModel.syncDevice(
                SyncRequest(
                    getLatestActivityData(dailyStep),
                    SharedPreferencesManager.getUserId(mContext),
                    android_id
                )
            )
        }
    }


    /**
     * get Latest data request array
     */


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
        mViewModel.syncDevice(
            SyncRequest(
                getActivityData(),
                SharedPreferencesManager.getUserId(mContext),
                android_id
            )
        )
    }

    fun callDashboard() {
        mViewModel.fetchSyncData(
            FetchDeviceDataRequest(WEEKLY, SharedPreferencesManager.getUserId(mContext))
        )
    }

    fun syncingWearableMsg(bool: Boolean) {
        if(syncTxtMsg!=null){
            if(bool){
                syncTxtMsg.visibility = View.VISIBLE
            }else{
                syncTxtMsg.visibility = View.GONE
            }
        }

    }
}