package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.rewards.MyRedeemedResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso

class MyRedeemedRewardsAdapter(mContext: Context?, mData: MyRedeemedResponse) :
    RecyclerView.Adapter<MyRedeemedRewardsAdapter.ViewHolder>() {

    var mContext: Context?
    var mData: MyRedeemedResponse

    init {
        this.mContext = mContext
        this.mData = mData
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       holder.textView.text = mData.data.get(position).name
       holder.textShort.text = Html.fromHtml(mData.data.get(position).description)
        holder.tokenTxt.visibility = View.VISIBLE
        holder.tokenTxt.text = mData.data.get(position).token.toString() + " TKS"
       /* holder.imageView.setOnClickListener {
            mListener!!.onItemClick(position, item)
        }*/
        Picasso.get().load(RetrofitClient.IMG_URL + "" +  mData.data.get(position).image)
            .into(holder.imageView)

    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var textShort: TextView
        var tokenTxt : TextView
        var imageView: ImageView

        internal lateinit var item: Data

        init {
            textView = v.findViewById<View>(R.id.titleTxt) as TextView
            textShort = v.findViewById<View>(R.id.subtitleTxt) as TextView
            tokenTxt = v.findViewById<View>(R.id.tokenTxt) as TextView
            imageView = v.findViewById<View>(R.id.img_challenge) as ImageView
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_mychallenges_list, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return mData.data.size
    }


   /* interface ItemClickListener {
        fun onItemClick(pos: Int, item: Data)
    }*/
}