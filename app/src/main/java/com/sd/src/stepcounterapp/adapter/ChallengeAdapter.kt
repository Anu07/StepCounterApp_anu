package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.getDaysDifference
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso


class ChallengeAdapter(
    internal var mContext: Context,
    internal var mValues: MutableList<Data>,
    protected var mListener: ItemClickListener?
) : RecyclerView.Adapter<ChallengeAdapter.ViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.item = mValues[position]
        holder.textView.text = item.name.capitalize()
        holder.textShort.text = item.shortDesc.capitalize()
        var daysDuration:String? = getDaysDifference(changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd/MM/yyyy", item.startDateTime), changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd/MM/yyyy", item.endDateTime)).toString()
        if(daysDuration.equals("0")){
            holder.txtDurDesc.text = "Duration: "+"Today"
        }else{
            holder.txtDurDesc.text = "Duration: $daysDuration"
        }
        holder.imageView.setOnClickListener {
            mListener!!.onItemClick(position,item)
        }
        Picasso.get().load(RetrofitClient.IMG_URL + "" + item.image).placeholder(R.drawable.placeholder)
            .into(holder.imageView)

    }


    private var isClicked: Boolean = false
    private lateinit var item: Data

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var txtDurDesc: TextView
        var textShort: TextView
        var imageView: ImageView
        var cdMain: CardView

        internal lateinit var item: Data

        init {
            textView = v.findViewById<View>(R.id.txtChallengeName) as TextView
            textShort = v.findViewById<View>(R.id.txtShortDesc) as TextView
            txtDurDesc = v.findViewById<View>(R.id.txtDurDesc) as TextView
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
