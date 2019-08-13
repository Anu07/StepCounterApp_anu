package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.OptionsModel
import kotlinx.android.synthetic.main.dialog_options.*


class OptionDialog(
    context: Context,
    themeResId: Int,
    private val LayoutId: Int,
    var list: ArrayList<OptionsModel>,
    title: String,
    private val callback: InterfacesCall.Callback
) : BaseDialog(context, themeResId) {

    lateinit var mAdapter: SelectCityAdapter
    var title: String
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
        if (list.isNotEmpty()) {
            recyclerView!!.layoutManager = LinearLayoutManager(context)
            mAdapter = SelectCityAdapter(context, list, indexClick)
            recyclerView!!.adapter = mAdapter
            txtTitle.text = title
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
