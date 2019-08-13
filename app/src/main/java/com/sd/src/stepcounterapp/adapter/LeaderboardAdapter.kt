package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.leaderboard.Challenge
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner

class LeaderboardAdapter(
    internal var context: Context,
    mValues: ArrayList<Challenge>,
    var mListener:ItemClickGlobalListner) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    private var mData: ArrayList<Challenge> = mValues
    var mContext: Context = context
    var item: Challenge? = null
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.item = mData[position]
        holder.textViewPosition.text = (3+position).toString()      //list displays leadrebaord from position 3
        holder.textName.text = item!!.name.toString()
        holder.textSteps.text = item!!.steps.toString()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun swap(mNewData: MutableList<Challenge>) {
        if(this.mData.isNotEmpty()){
            mData.clear()
            mData.addAll(mNewData)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textViewPosition: TextView
        var textName: TextView
        var textSteps: TextView

        internal lateinit var item: com.sd.src.stepcounterapp.model.challenge.Data

        init {
            textViewPosition = v.findViewById<View>(R.id.positionTxt) as TextView
            textName = v.findViewById<View>(R.id.nameLeader) as TextView
            textSteps = v.findViewById<View>(R.id.stepsValTxt) as TextView
        }


    }

}
