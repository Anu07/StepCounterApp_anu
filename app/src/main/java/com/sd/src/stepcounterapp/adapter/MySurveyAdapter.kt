package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.survey.mysurvey.MySurveyResponse
import com.sd.src.stepcounterapp.model.survey.mysurveyresponse.MySurveyResponseModel

class MySurveyAdapter(mContext: Context?, mData: MySurveyResponseModel) :
    RecyclerView.Adapter<MySurveyAdapter.ViewHolder>() {

    var mContext: Context?
    var mData: MySurveyResponseModel

    init {
        this.mContext = mContext
        this.mData = mData
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textQues.text = mData.data[position].survey[0].questions.size.toString()
        holder.datesurvey.text = changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM, yyyy", mData.data[position].attendAt)
       holder.textView.text = mData.data[position].survey[0].name.capitalize()
       holder.txtEarningToken.text = "Earned Tokens: "+mData.data[position].survey[0].earningToken.toString().capitalize()
        holder.customStartTextView.visibility = View.GONE
    }



    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var textQues: TextView
        var txtEarningToken: TextView
        var datesurvey: TextView
        var customStartTextView: TextView = v.findViewById(R.id.customStartTextView)

        internal lateinit var item: Data

        init {
            textView = v.findViewById<View>(R.id.txtSurveysName) as TextView
            textQues = v.findViewById<View>(R.id.txtTotalQuestion) as TextView
            txtEarningToken = v.findViewById<View>(R.id.txtEarningToken) as TextView
            datesurvey = v.findViewById<View>(R.id.datesurvey) as TextView
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_surveys, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return mData.data.size
    }


   /* interface ItemClickListener {
        fun onItemClick(pos: Int, item: Data)
    }*/
}