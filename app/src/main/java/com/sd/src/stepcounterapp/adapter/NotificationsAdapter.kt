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
import com.sd.src.stepcounterapp.model.notificatyionlist.Data

class NotificationsAdapter(mContext: Context?, mData: ArrayList<Data>) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    var mContext: Context?
    var mData: ArrayList<Data>

    init {
        this.mContext = mContext
        this.mData = mData
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(mData.size>0){
            holder.textView.text = mData[position].message.capitalize()
            holder.textShort.text = changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM, yyyy", mData[position].createdAt) +", "+mData[position].createdAt.split("T")[1].substring(0,5)
            /* holder.imageView.setOnClickListener {
                 mListener!!.onItemClick(position, item)
             }*/
            holder.imageView.setBackgroundResource(R.drawable.nouser)
            /* Picasso.get().load(RetrofitClient.IMG_URL + "" +  mData.data.get(position).challenge.image)
                 .into(holder.imageView)*/

        }

    }



    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView = v.findViewById<View>(R.id.notificationtitle) as TextView
        var textShort: TextView = v.findViewById<View>(R.id.notificationdate) as TextView
        var imageView: ImageView = v.findViewById<View>(R.id.noti_img) as ImageView

        internal lateinit var item: Data


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_notification_list, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return mData.size
    }


   /* interface ItemClickListener {
        fun onItemClick(pos: Int, item: Data)
    }*/
}