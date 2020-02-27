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
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.getDaysDifference
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.challenge.Trending
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.Utils.getCurrentDate
import com.squareup.picasso.Picasso
import java.util.ArrayList


class ChallengeTrendingAdapter(
    internal var mContext: Context,
    internal var mValues: MutableList<Data>,
    protected var mListener: ItemTrendClickListener?
) : RecyclerView.Adapter<ChallengeTrendingAdapter.ViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.item = mValues[position]
        holder.textView.text = item.name.capitalize()
        holder.textShort.text = HtmlCompat.fromHtml(item.description.capitalize(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        var daysDuration: String? = getDaysDifference(
            getCurrentDate().toString(),
            changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd/MM/yyyy", item.endDateTime)
        ).toString()
        if (daysDuration.equals("0")) {
            holder.txtDuration.text = "Duration: " + "Today"
        } else {
            holder.txtDuration.text = "Duration: $daysDuration Days"
        }
        Picasso.get().load(IMG_URL + "" + item.image).placeholder(R.drawable.placeholder)
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            mListener!!.onTrendItemClick(position,mValues[position])
        }
    }


    private lateinit var item: Data

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var textShort: TextView
        var imageView: ImageView
        var cdMain: CardView
        var txtDuration:TextView

        internal lateinit var item: Trending

        init {
            txtDuration = v.findViewById<View>(R.id.txtDuration) as TextView
            textView = v.findViewById<View>(R.id.txtChallengeName) as TextView
            textShort = v.findViewById<View>(R.id.txtShortDesc) as TextView
            imageView = v.findViewById<View>(R.id.imgChallenge) as ImageView
            cdMain = v.findViewById<View>(R.id.cdMain) as CardView
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.recycler_trend_grid_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return mValues.size
    }
    fun swap(updatedList: ArrayList<Data>) {
        if (mValues.isNotEmpty()) {
            mValues.clear()
        }
        mValues = updatedList
    }

    interface ItemTrendClickListener {
        fun onTrendItemClick(pos:Int, item: Data)
    }
}
