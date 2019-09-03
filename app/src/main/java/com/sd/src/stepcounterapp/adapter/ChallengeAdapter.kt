package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso


class ChallengeAdapter(
    internal var mContext: Context,
    internal var mValues: MutableList<Data>,
    protected var mListener: ItemClickListener?
) : RecyclerView.Adapter<ChallengeAdapter.ViewHolder>() {
    private lateinit var item: Data

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        item = mValues[position]
        holder.textView.text = item.name
        holder.textShort.text = Html.fromHtml(item.shortDesc)
        Log.i("item","name"+position +"!!!!!"+item.name)
        holder.imageView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.imageView.foreground = mContext.getDrawable(R.drawable.image_selected_overlay)
            }
            mListener!!.onItemClick(position,item)
        }
        Picasso.get().load(RetrofitClient.IMG_URL + "" + item.image).placeholder(R.drawable.placeholder).into(holder.imageView)
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.imageView.foreground = mContext.getDrawable(R.drawable.image_selected_overlay)
        }*/
    }



    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var textShort: TextView
        var imageView: ImageView
        var cdMain: CardView

        init {
            textView = v.findViewById<View>(R.id.txtChallengeName) as TextView
            textShort = v.findViewById<View>(R.id.txtShortDesc) as TextView
            imageView = v.findViewById<View>(R.id.imgChallenge) as ImageView
            cdMain = v.findViewById<View>(R.id.cdMain) as CardView
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.recycler_grid_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return mValues.size
    }


    interface ItemClickListener {
        fun onItemClick(pos:Int, item: Data)
    }

}
