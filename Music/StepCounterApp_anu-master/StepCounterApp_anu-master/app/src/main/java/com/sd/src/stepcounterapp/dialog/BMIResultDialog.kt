package com.sd.src.stepcounterapp.dialog


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.OptionsModel
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.IndicatorStayLayout
import com.warkiz.widget.IndicatorType
import com.warkiz.widget.TickMarkType
import kotlinx.android.synthetic.main.dialog_bmi_result.*
import kotlinx.android.synthetic.main.dialog_options.*
import java.util.*


class BMIResultDialog(
    context: Context,
    themeResId: Int,
    private val LayoutId: Int,
    var list: ArrayList<OptionsModel>,
    var bundle: Bundle,
    title: String,
    private val callback: InterfacesCall.Callback
) : BaseDialog(context, themeResId) {

    lateinit var mAdapter: SelectCityAdapter
    var title: String
    var states = arrayOf(
        intArrayOf(android.R.attr.state_enabled), // enabled
        intArrayOf(-android.R.attr.state_enabled), // disabled
        intArrayOf(-android.R.attr.state_checked), // unchecked
        intArrayOf(android.R.attr.state_pressed)  // pressed
    )

    var colors = intArrayOf(Color.BLACK, Color.RED, Color.GREEN, Color.BLUE)
    init {
        val wmlp = this.window!!.attributes
        wmlp.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        window!!.attributes = wmlp
        this.title = title
    }

    override fun getInterfaceInstance(): InterfacesCall.IndexClick {
        return this
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateStuff() {
        if(list.isNotEmpty()){
            recyclerView!!.layoutManager = LinearLayoutManager(context)
            mAdapter = SelectCityAdapter(context, list, indexClick)
            recyclerView!!.adapter = mAdapter
            txtTitle.text = title
        }
        if(bundle!=null){
            val myList = ColorStateList(states, colors)

            bmival.text =(String.format("%.2f",bundle.getFloat("bmi")))
            weightVal.text = bundle.getString("Weigth")
            heightVal.text = bundle.getString("heigth")


            val content = findViewById<LinearLayout>(R.id.resultLayout)



            //CONTINUOUS
            val continuous = IndicatorSeekBar
                .with(context)
                .max(100f)
                .min(10f)
                .progress(bundle.getFloat("bmi"))
                .indicatorColor(Color.parseColor("#8DC540"))
                .showIndicatorType(IndicatorType.CIRCULAR_BUBBLE)
                .thumbSize(1)
                .indicatorTextSize(0)
                .thumbColor(Color.parseColor("#8DC540"))
                .trackProgressColor(Color.parseColor("#8DC540"))
                .trackProgressSize(4)
                .trackBackgroundSize(2)
                .trackBackgroundColor(Color.parseColor("#FF5454"))
                .showThumbText(false)
                .onlyThumbDraggable(false)
                .showTickTexts(false)
                .build()


            val indicatorLayout = IndicatorStayLayout(context)

            indicatorLayout.attachTo(continuous)

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100
            )
            lp.setMargins(5, 0, 10, 10)
            continuous.layoutParams = lp
            content.addView(indicatorLayout, 3)

            continuebttn.setOnClickListener {
                dismiss()
                callback.selected(0)
            }
            cancelbttn.setOnClickListener {
                dismiss()
            }

        }

    }

    override fun getContentView(): Int {
        return LayoutId
    }

     fun setData() {

    }


    override fun clickIndex(pos: Int) {
        dismiss()
        callback.selected(pos)
        mAdapter.notifyDataSetChanged()
    }
}
