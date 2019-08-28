package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.survey.Data
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner

class SurveysAdapter(
    internal var context: Context,
     mValues: ArrayList<Data>,
    var mListener:ItemClickGlobalListner) :
    RecyclerView.Adapter<SurveysAdapter.ViewHolder>() {

    private var mData: ArrayList<Data> = mValues
    var mContext: Context = context
    var item: Data? = null
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_surveys, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.item = mData[position]
        holder.textView.text = item!!.name
        holder.textQues.text = item!!.products.size.toString()
        holder.textEarnToken.text = item!!.earningToken.toString()
        var date = item!!.expireOn.split("T")[0]
        holder.textSurvey.text = date
        holder.textStart.setOnClickListener {
            mListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun swap(mNewData: MutableList<Data>) {
        if(this.mData.isNotEmpty()){
            mData.clear()
            mData.addAll(mNewData)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var textQues: TextView
        var textEarnToken: TextView
        var textSurvey: TextView
        var textStart:TextView

        internal lateinit var item: com.sd.src.stepcounterapp.model.challenge.Data

        init {
            textView = v.findViewById<View>(R.id.txtSurveysName) as TextView
            textQues = v.findViewById<View>(R.id.txtTotalQuestion) as TextView
            textEarnToken = v.findViewById<View>(R.id.txtEarningToken) as TextView
            textSurvey = v.findViewById<View>(R.id.datesurvey) as TextView
            textStart = v.findViewById<View>(R.id.customStartTextView) as TextView
        }


    }

}
