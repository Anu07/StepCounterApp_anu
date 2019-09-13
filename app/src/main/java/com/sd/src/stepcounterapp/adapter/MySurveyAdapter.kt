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
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.survey.mysurvey.MySurveyResponse

class MySurveyAdapter(mContext: Context?, mData: MySurveyResponse) :
    RecyclerView.Adapter<MySurveyAdapter.ViewHolder>() {

    var mContext: Context?
    var mData: MySurveyResponse

    init {
        this.mContext = mContext
        this.mData = mData
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       holder.textView.text = mData.data[0].survey[position].name.capitalize()
       holder.textShort.text = "Earned Tokens: "+mData.data[0].survey[position].earningToken.toString().capitalize()
       /* holder.imageView.setOnClickListener {
            mListener!!.onItemClick(position, item)
        }*/
        holder.imageView.setBackgroundResource(R.drawable.survey_img)
       /* Picasso.get().load(RetrofitClient.IMG_URL + "" +  mData.data.get(position).challenge.image)
            .into(holder.imageView)*/

    }



    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var textShort: TextView
        var imageView: ImageView

        internal lateinit var item: Data

        init {
            textView = v.findViewById<View>(R.id.titleTxt) as TextView
            textShort = v.findViewById<View>(R.id.subtitleTxt) as TextView
            imageView = v.findViewById<View>(R.id.img_challenge) as ImageView
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_mychallenges_list, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return mData.data[0].survey.size
    }


   /* interface ItemClickListener {
        fun onItemClick(pos: Int, item: Data)
    }*/
}