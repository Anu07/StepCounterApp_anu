package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import kotlinx.android.synthetic.main.dialog_filter.*


class FilterDialog(
    context: Context,
    themeResId: Int,
    private val LayoutId: Int,
    var mListener: MarketFilterInterface,
    var catList: ArrayList<String>,
    var tabtype: Boolean,
    var selectedToken: String,
    var selectedCategory: String
) : BaseDialog(context, themeResId) {

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


        var tokenList:ArrayList<String> = ArrayList()
        tokenList.addAll(listOf("0 - 100", "100 - 500", "500 - 1000", "1000+"))
        btnSave.setOnClickListener {
            dismiss()
        }
        if(selectedCategory.isNotEmpty()){
            catList.forEachIndexed { index, s ->
                if(s == selectedCategory){
                    filterCategoryList.setSelection(index)
                    categoryList.text = catList[index]
                }
            }
        }else{
            categoryList.text = "All"
        }


        if(selectedToken.isNotEmpty()){
            tokenList.forEachIndexed { index, s ->
                if(s == selectedToken){
                    filterTokenList.setSelection(index)
                    tokenFilter.text = tokenList[index]
                }
            }
        }else{
            tokenFilter.text = "0 - 100"
        }
        categoryList.setOnClickListener {
            if(tabtype){
                filterView.visibility = View.GONE
                filterTokenList.visibility = View.GONE
                filterCategoryList.visibility = View.VISIBLE

                setCategoryAdapter()
            }else{
                filterCategoryList.visibility = View.VISIBLE
            }
        }

        reset.setOnClickListener {
            mListener.onCategoryList("All")
            mListener.onTokenList(tokenList[0])
            setCategoryAdapter()
            setTokenAdapter(tokenList)
        }

        tokenFilter.setOnClickListener {
            filterView.visibility = View.GONE
            filterTokenList.visibility = View.VISIBLE
            filterCategoryList.visibility = View.GONE
            setTokenAdapter(tokenList)
        }

    }

    private fun setTokenAdapter(tokenList: ArrayList<String>) {
        val tokenAdapter = ArrayAdapter<String>(context, R.layout.spinner_item, tokenList)
        filterTokenList.adapter = tokenAdapter
        filterTokenList.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, position, id ->
                dismiss()
                mListener.onTokenList(tokenList[position])
            }
    }

    private fun setCategoryAdapter(): ArrayAdapter<String> {
        if (!catList.contains("All"))
            catList.add(0, "All")
        val adapter = ArrayAdapter<String>(context, R.layout.spinner_item, catList)
        filterCategoryList.adapter = adapter
        filterCategoryList.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, position, id ->
                dismiss()
                mListener.onCategoryList(catList[position])
            }
        return adapter
    }

    override fun clickIndex(pos: Int) {
        dismiss()
    }


    interface MarketFilterInterface {
        fun onCategoryList(queryCat: String)
        fun onTokenList(tokenStr: String)
    }
}