package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.challenge.departmentchallengeresponse.Datum
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner

class LeaderboardDepartmentAdapter(
    internal var context: Context,
    mValues: ArrayList<Datum>,
    var mListener:ItemClickGlobalListner) :
    RecyclerView.Adapter<LeaderboardDepartmentAdapter.ViewHolder>() {

    private var mData: ArrayList<Datum> = mValues
    var mContext: Context = context
    var item: Datum? = null
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            this.item = mData[position]
            holder.textViewPosition.text = (position+4).toString()      //list displays leadrebaord from position 3
            holder.textName.text = item!!.departmentName.toString()
            holder.textSteps.text = item!!.steps.toString()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun swap(mNewData: ArrayList<Datum>) {
        if(mData.size>0){
            mData.clear()
        }
        mData.addAll(mNewData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textViewPosition: TextView = v.findViewById<View>(R.id.positionTxt) as TextView
        var textName: TextView = v.findViewById<View>(R.id.nameLeader) as TextView
        var textSteps: TextView = v.findViewById<View>(R.id.stepsValTxt) as TextView

        internal lateinit var item: Datum


    }

}
