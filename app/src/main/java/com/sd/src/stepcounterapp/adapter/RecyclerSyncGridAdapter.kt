package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.rewards.Data
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso


class RecyclerSyncGridAdapter(
    internal var mContext: Context,
    internal var mValues: IntArray,
    protected var mListener: ItemListener?
) : RecyclerView.Adapter<RecyclerSyncGridAdapter.ViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(mValues[position])
    }



    private var isClicked: Boolean = false
    private var item: Data = Data()

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        var imageView: ImageView
        internal lateinit var item: Data

        init {
            v.setOnClickListener(this)
            imageView = v.findViewById<View>(R.id.categoryImage) as ImageView
        }

        override fun onClick(view: View) {
            if (mListener != null) {
//                mListener!!.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.recycler_sync_view_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return mValues.size
    }

    interface ItemListener {
        fun onItemClick(item: Data)
    }
}
