package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.*
import com.sd.src.stepcounterapp.model.survey.Datum
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner


class SurveysAdapter(
    internal var context: Context,
    mValues: ArrayList<Datum>,
    var mListener: ItemClickGlobalListner
) :
    RecyclerView.Adapter<SurveysAdapter.ViewHolder>() {

    private var mData: ArrayList<Datum> = mValues
    var mContext: Context = context
    var item: Datum? = null
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_surveys, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.item = mData[position]
        holder.textView.text = item!!.name.capitalize()
        holder.textQues.text = item!!.questions.size.toString().capitalize()
        holder.textEarnToken.text = item!!.earningToken.toString().capitalize()+" TKNS"
        holder.tokenTxtMsg.text = "Survey Reward: "
        var date = dateconvertToLocal("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", item!!.expireOn,"dd MMM, yyyy")
        holder.textSurvey.text = date
        if (item!!.answered) {
            holder.textStart.setBackgroundResource(R.drawable.gray_fill_circle)
        } else {
            holder.textStart.setBackgroundResource(R.drawable.blue_fill_circle)
        }

        holder.textStart.setOnClickListener {
                mListener.onItemClick(position)

        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun swap(mNewData: ArrayList<Datum>) {
        if (this.mData.isNotEmpty())
                        mData.clear()
        mData = mNewData
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var textQues: TextView
        var textEarnToken: TextView
        var textSurvey: TextView
        var textStart: TextView
        var tokenTxtMsg: TextView

        internal lateinit var item: com.sd.src.stepcounterapp.model.challenge.Data

        init {
            textView = v.findViewById<View>(R.id.txtSurveysName) as TextView
            textQues = v.findViewById<View>(R.id.txtTotalQuestion) as TextView
            textEarnToken = v.findViewById<View>(R.id.txtEarningToken) as TextView
            textSurvey = v.findViewById<View>(R.id.datesurvey) as TextView
            textStart = v.findViewById<View>(R.id.customStartTextView) as TextView
            tokenTxtMsg = v.findViewById<View>(R.id.tokenTxtMsg) as TextView
        }


    }

}
