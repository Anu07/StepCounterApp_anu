package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.convertToLocal
import com.sd.src.stepcounterapp.model.notificatyionlist.NotificationData

class NotificationsAdapter(mContext: Context?, mData: ArrayList<NotificationData>, var mListener: NotifyItemClickListener) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    var mContext: Context?
    var mData: ArrayList<NotificationData>

    init {
        this.mContext = mContext
        this.mData = mData
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (mData.size > 0) {
            if (mData[position].message != null) {
                holder.textView.text = mData[position].message.capitalize()
                holder.textShort.text = changeDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS",
                    "dd MMM, yyyy",
                    mData[position].createdAt
                ) + ", " + convertToLocal( mData[position].createdAt)
                if (mData[position].readStatus) {
                    holder.parentLayout.setBackgroundColor(mContext!!.resources.getColor(R.color.white))
                } else {
                    holder.parentLayout.setBackgroundColor(mContext!!.resources.getColor(R.color.background_litegray))
                }
            }

            /* holder.imageView.setOnClickListener {
                 mListener!!.onItemClick(position, item)
             }*/
            holder.imageView.setBackgroundResource(R.drawable.nouser)
            /* Picasso.get().load(RetrofitClient.IMG_URL + "" +  mData.data.get(position).challenge.image)
                 .into(holder.imageView)*/

        }
        holder.parentLayout.setOnClickListener {
            if (!mData[position].readStatus) {
                holder.parentLayout.setBackgroundColor(mContext!!.resources.getColor(R.color.white))
                mListener.onItemClick(position)
            }
        }

    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView = v.findViewById<View>(R.id.notificationtitle) as TextView
        var textShort: TextView = v.findViewById<View>(R.id.notificationdate) as TextView
        var imageView: ImageView = v.findViewById<View>(R.id.noti_img) as ImageView
        var parentLayout = v.findViewById<View>(R.id.parentNotification) as LinearLayout
        internal lateinit var item: NotificationData


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_notification_list, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return mData.size
    }


    interface NotifyItemClickListener {
        fun onItemClick(pos: Int)
    }
}