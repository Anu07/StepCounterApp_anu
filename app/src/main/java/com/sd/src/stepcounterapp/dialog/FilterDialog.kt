package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import kotlinx.android.synthetic.main.dialog_filter.*
import android.widget.AdapterView
import com.sd.src.stepcounterapp.R


class FilterDialog(context: Context, themeResId: Int, private val LayoutId: Int,var mListener:MarketFilterInterface, var catList: ArrayList<String>)
    : BaseDialog(context, themeResId) {

    init {
        val wmlp = this.window!!.attributes
        wmlp.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        window!!.attributes = wmlp
    }

    override fun getInterfaceInstance(): InterfacesCall.IndexClick {
        return this
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateStuff() {
        setData()
    }

    override fun getContentView(): Int {
        return LayoutId
    }

    fun setData() {
        btnSave.setOnClickListener {
            dismiss()
        }

        categoryList.setOnClickListener {
            filterView.visibility = View.GONE
            filterCategoryList.visibility = View.VISIBLE
        }
        val adapter = ArrayAdapter<String>(context, R.layout.spinner_item, catList)
        filterCategoryList.adapter = adapter
        filterCategoryList.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, id ->
           mListener.onCategoryList(catList[position])
        }
    }

    override fun clickIndex(pos: Int) {
        dismiss()
    }


    interface MarketFilterInterface{
        fun onCategoryList(queryCat: String)

    }
}