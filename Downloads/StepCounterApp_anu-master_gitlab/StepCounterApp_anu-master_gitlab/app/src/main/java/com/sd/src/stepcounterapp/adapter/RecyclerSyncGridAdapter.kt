package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.R.drawable.full_border_cyanbgreen
import com.sd.src.stepcounterapp.model.rewards.Data
import com.sd.src.stepcounterapp.model.syncDevice.SyncDeviceSelectionArray


class RecyclerSyncGridAdapter(
    internal var mContext: Context,
    internal var mValues: Array<SyncDeviceSelectionArray?>,
    protected var mListener: ItemListener?
) : RecyclerView.Adapter<RecyclerSyncGridAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageDrawable(mValues[position]!!.image)
        selectedItem(position,holder)
        holder.parent.setOnClickListener {
            mListener?.onItemClick(position,mValues[position])
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var imageView: ImageView
        var checkImg: ImageView
        var parent:FrameLayout
        internal lateinit var item: Data

        init {
            imageView = v.findViewById<View>(R.id.categoryImage) as ImageView
            checkImg = v.findViewById<View>(R.id.checkMark) as ImageView
            parent = v.findViewById<View>(R.id.parent) as FrameLayout
        }
    }

    /**
     * show selection
     */
    private fun selectedItem(position: Int, holder: ViewHolder){
        if(mValues[position]!!.selected){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.imageView.foreground = mContext.getDrawable(full_border_cyanbgreen)
                holder.checkImg.visibility = View.VISIBLE
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.imageView.foreground = null
                holder.checkImg.visibility = View.GONE
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

    fun swapData(newData:Array<SyncDeviceSelectionArray?>){
        if(mValues.isNotEmpty()) {
            mValues = arrayOfNulls(6)
        }
        mValues = newData
        notifyDataSetChanged()
    }


    interface ItemListener {
        fun onItemClick(pos: Int, item: SyncDeviceSelectionArray?)
    }
}
