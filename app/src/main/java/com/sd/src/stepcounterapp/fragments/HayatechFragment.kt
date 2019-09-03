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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
//    internal lateinit var callback: MarketPlaceFragment.FragmentClick
//
//    fun FragmentClickListener(callback: MarketPlaceFragment.FragmentClick) {
//        this.callback = callback
//    }

    private var mDataList: Data? = Data()
    private lateinit var mViewModel: DeviceViewModel
    var optionArray = arrayListOf<OptionsModel>()

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
                Toast.makeText(activity, "Data synced successfully", Toast.LENGTH_LONG).show()

            })
        mViewModel.getDashResponse().observe(this,
            Observer<DashboardResponse> { mDashResponse ->
                showPopupProgressSpinner(false)
                mDataList = mDashResponse.data
                steps.text = (mDashResponse.data.activity.sumBy { it.steps }).toString()
                totalstepsCount.text = mDashResponse.data.todayToken.toString()
                circular_progress.setProgress(mDashResponse.data.todayToken.toDouble(), 10.00)
                company_rank_count.text = mDashResponse.data.companyRank.toString()
                totl_dist.text = String.format("%.2f", mDashResponse.data.totalUserDistance)
                totl_dist_suffix.text = "Km"
                tokensVal.text = mDashResponse.data.totalUserToken.toString()
                if (mDashResponse.data.lastUpdated != null) {
                    SharedPreferencesManager.setString(mContext, mDashResponse.data.lastUpdated, SYNCDATE)
                }
                setBarChart("STEPS")
            })

        if (SharedPreferencesManager.hasKey(mContext, "Wearable")) {
            val android_id = Settings.Secure.getString(
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


    private fun getActivityData(): java.util.ArrayList<Activity>? {
        var list: ArrayList<DailyStep>? = SharedPreferencesManager.getSyncObject(mContext)
        var activityList: ArrayList<Activity>? = ArrayList()
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
        return activityList
    }

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


    fun resetBarChat(){
        barchart.invalidate()
//        mMonthListFormater= arrayOfNulls<String>(31)
    }
    private fun setBarChart(format: String) {
        var weeklyData: ArrayList<BarEntry> = addDataFromServer(format)
        val bardataset = BarDataSet(weeklyData, "")
        barchart.data = null
        bardataset.color = Color.parseColor("#FFFFFF")
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
                                mMonthListFormater[index] = element.date.toString().replace("2019-","")
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.steps.toFloat()))
                            mMonthListFormater[index] = element.date.toString().replace("2019-","")
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
                                mMonthListFormater.set(index, element.date.toString().replace("2019-",""))
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.token.toFloat()))
                            mMonthListFormater.set(index, element.date.toString().replace("2019-",""))
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
                                mMonthListFormater.set(index, element.date.toString().replace("2019-",""))
                            }
                    } else {
                        mDataList!!.activity.forEachIndexed { index, element ->
                            graphData.add(index, BarEntry(index.toFloat(), element.distance.toFloat()))
                            mMonthListFormater.set(index, element.date.toString().replace("2019-",""))
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
            Log.e(
                "Updating",
                "steps" + ((mDataList!!.activity.sumBy { it.steps }) + dailyStep.count.toInt()).toString()
            )
            steps.text = ((mDataList!!.activity.sumBy { it.steps }) + dailyStep.count.toInt()).toString()
            totl_dist.text = (mDataList!!.totalUserDistance + dailyStep.distance.toDouble()).toString()
            totl_dist_suffix.text = "Km"
        }
    }


}