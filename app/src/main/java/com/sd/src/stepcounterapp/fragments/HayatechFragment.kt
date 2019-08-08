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
import androidx.fragment.app.Fragment
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
import com.sd.src.stepcounterapp.adapter.PatternProgressTextAdapter
import com.sd.src.stepcounterapp.model.DeviceResponse.DashboardResponse
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.syncDevice.Activity
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.utils.DayAxisValueFormatter
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.viewModels.DeviceViewModel
import kotlinx.android.synthetic.main.fragment_hayatech.*


class HayatechFragment : Fragment() {

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


    private lateinit var mViewModel: DeviceViewModel
    var xAxis: XAxis? = null
    private val xVal = arrayOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hayatech, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
        circular_progress.setProgressTextAdapter(PatternProgressTextAdapter())
        setStepsText()
        initBarChart()
        setBarChart()
        mViewModel.getSyncResponse().observe(this,
            Observer<BasicInfoResponse> { mResponse ->
                Toast.makeText(activity, "Data synced successfully", Toast.LENGTH_LONG).show()

            })
        mViewModel.getDashResponse().observe(this,
            Observer<DashboardResponse> { mDashResponse ->
                steps.text = mDashResponse.data.todayToken.toString()
                totalstepsCount.text = mDashResponse.data.todayToken.toString()
                circular_progress.setProgress(mDashResponse.data.todayToken.toDouble(), 10.00)
                company_rank_count.text = mDashResponse.data.companyRank.toString()
                totl_dist.text = mDashResponse.data.totalUserDistance.toString()
                SharedPreferencesManager.setString(mContext, SYNCDATE, mDashResponse.data.lastUpdated)
            })

        mViewModel.fetchSyncData(
            FetchDeviceDataRequest("daily", SharedPreferencesManager.getUserId(mContext))
        )




        if (SharedPreferencesManager.hasKey(mContext, "Wearable")) {
            var android_id = Settings.Secure.getString(
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

        var xAxisFormatter = DayAxisValueFormatter(barchart)
        leftAxis.addLimitLine(ll)
        var XAx = barchart.xAxis
        XAx.valueFormatter = xAxisFormatter

        /* val l = barchart.legend
           l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
           l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
           l.orientation = Legend.LegendOrientation.HORIZONTAL
           l.setDrawInside(false)
           l.form = LegendForm.SQUARE
           l.formSize = 9f
           l.textSize = 11f
           l.xEntrySpace = 4f*/
    }

    private fun setBarChart() {

        val NoOfEmp = ArrayList<BarEntry>()

        NoOfEmp.add(BarEntry(0f, 10f))
        NoOfEmp.add(BarEntry(1f, 3f))
        NoOfEmp.add(BarEntry(2f, 2f))
        NoOfEmp.add(BarEntry(3f, 3f))
        NoOfEmp.add(BarEntry(4f, 1f))
        NoOfEmp.add(BarEntry(5f, 5f))
        NoOfEmp.add(BarEntry(6f, 2f))


        val bardataset = BarDataSet(NoOfEmp, null)
        bardataset.color = Color.parseColor("#8DC540")
        barchart.animateY(5000)
        val data = BarData(bardataset)
        barchart.data = data


    }

}
