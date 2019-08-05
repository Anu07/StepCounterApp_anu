package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R

class SurveysAdapter(
    internal var context: Context,
    internal var mValues: ArrayList<String>
) :
    RecyclerView.Adapter<SurveysAdapter.ViewHolder>() {

    private var mData: ArrayList<String> = mValues
    var mContext: Context = context

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vhItem: ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_surveys, parent, false)
        vhItem = ViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
