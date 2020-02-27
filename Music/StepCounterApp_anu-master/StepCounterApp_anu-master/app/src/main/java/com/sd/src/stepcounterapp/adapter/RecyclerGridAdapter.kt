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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.rewards.Data
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso


class RecyclerGridAdapter(
    internal var mContext: Context,
    internal var mValues: MutableList<Data>,
    protected var mListener: ItemListener?
) : RecyclerView.Adapter<RecyclerGridAdapter.ViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.item = mValues[position]
        holder.textView.text = item.name.capitalize()
        Log.i("Photo url", "" + position)
        checkSelection(position, holder)
        holder.imageView.setOnClickListener {
            Log.i("Photo position", "" + position)
            isClicked = !isClicked
            mValues[position].selectedItem = isClicked
            notifyDataSetChanged()
            checkSelection(position, holder)
            mListener!!.onItemClick(item,position)
        }
        Picasso.get().load(IMG_URL + "" + item.image).placeholder(R.drawable.placeholder).resize(200,200)
            .into(holder.imageView)

    }

    private fun checkSelection(
        pos: Int,
        holder: ViewHolder
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (mValues[pos].selectedItem) {
                holder.imageView.foreground = mContext.getDrawable(R.drawable.image_selected_overlay)
                holder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.image_selected_overlay))
            } else {
                holder.imageView.foreground = mContext.getDrawable(R.drawable.image_overlay)
                holder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.image_overlay))
                Picasso.get().load(IMG_URL + "" + item.image).placeholder(R.drawable.placeholder).resize(200,200)
                    .into(holder.imageView)
            }
        }else{
            if (mValues[pos].selectedItem) {
                holder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.image_selected_overlay))
            } else {
                holder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.image_overlay))
                Picasso.get().load(IMG_URL + "" + item.image).placeholder(R.drawable.placeholder).resize(200,200)
                    .into(holder.imageView)
            }
        }
    }


    private var isClicked: Boolean = false
    private lateinit var item: Data

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        var textView: TextView
        var imageView: ImageView
        var frameLayout: FrameLayout
        var cdMain: CardView

        internal lateinit var item: Data

        init {
            textView = v.findViewById<View>(R.id.catName) as TextView
            imageView = v.findViewById<View>(R.id.categoryImage) as ImageView
            frameLayout = v.findViewById<View>(R.id.parentLayout) as FrameLayout
            cdMain = v.findViewById<View>(R.id.cdMain) as CardView
        }
    }


    fun swap(mList:  MutableList<Data>){
        if(mValues.size>0)
            mValues.clear()
        mValues  = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return mValues.size
    }

    interface ItemListener {
        fun onItemClick(item: Data, position: Int)
    }
}
