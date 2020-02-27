package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.OptionsModel
import kotlinx.android.synthetic.main.item_options.view.*
import java.util.*

class SelectCityAdapter(
    internal var con: Context,
    internal var mData: ArrayList<OptionsModel>,
    internal var mClick: InterfacesCall.IndexClick
) : RecyclerView.Adapter<SelectCityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_options, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtOption.text = mData[position].name
        holder.llOption.setOnClickListener {
            mClick.clickIndex(position)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llOption = itemView.llOption!!
        val txtOption = itemView.txtOption!!
        val imgOption = itemView.imgOption!!
    }

}