package com.sd.src.stepcounterapp.adapter.multiselection

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.RecyclerGridAdapter
import com.sd.src.stepcounterapp.model.rewards.Data
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso


class MultiSelectionCategoryAdapter(
    internal var mContext: Context,
    internal var mValues: MutableList<Data>,
    protected var mListener: RecyclerGridAdapter.ItemListener?
) :
    RecyclerView.Adapter<MultiSelectionCategoryAdapter.MultiViewHolder>() {

    private lateinit var item: Data


    override fun onBindViewHolder(multiViewHolder: MultiViewHolder, position: Int) {
        multiViewHolder.bind(all!![position])

    }

    var all: ArrayList<Data>? = null

    val selected: ArrayList<Data>
        get() {
            val selected = ArrayList<Data>()
            all!!.forEachIndexed { index, data ->
                if (all!![index].selectedItem) {
                    selected.add(all!![index])
                }
            }
            return selected
        }

    init {
        this.all = mValues as ArrayList<Data>
    }

    fun setData(employees: ArrayList<Data>) {
        this.all = ArrayList()
        this.all = employees
        notifyDataSetChanged()
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): MultiViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, viewGroup, false)
        return MultiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return all!!.size
    }

    inner class MultiViewHolder(@NonNull v: View) : RecyclerView.ViewHolder(v) {

        var textView: TextView
        var imageView: ImageView
        var frameLayout: FrameLayout
        var cdMain: CardView

        internal lateinit var item: Data

        init {
            textView = v.findViewById(R.id.catName) as TextView
            imageView = v.findViewById<View>(R.id.categoryImage) as ImageView
            frameLayout = v.findViewById<View>(R.id.parentLayout) as FrameLayout
            cdMain = v.findViewById<View>(R.id.cdMain) as CardView
        }

        fun bind(data: Data) {
            this.item = mValues[position]
            textView.text = item.name.capitalize()
            Log.i("Photo url", "" + position)
            checkSelection(data)
            Picasso.get().load(IMG_URL + "" + item.image).placeholder(R.drawable.placeholder)
                .resize(200, 200)
                .into(imageView)
            itemView.setOnClickListener {
                data.selectedItem = !data.selectedItem
                checkSelection(data)
                mListener!!.onItemClick(item, position)
            }
        }

        private fun checkSelection(
            dataItem: Data
        ) {
            if (dataItem.selectedItem) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageView.foreground = mContext.getDrawable(R.drawable.image_selected_overlay)
                } else {
                    frameLayout.foreground = mContext.getDrawable(R.drawable.image_selected_overlay)

                }
                Picasso.get().load(IMG_URL + "" + dataItem.image).placeholder(R.drawable.placeholder)
                    .resize(200, 200)
                    .into(imageView)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageView.foreground = mContext.getDrawable(R.drawable.image_black_overlay)
                } else {
                    frameLayout.foreground = mContext.getDrawable(R.drawable.image_black_overlay)
                }
                Picasso.get().load(IMG_URL + "" + dataItem.image).placeholder(R.drawable.placeholder)
                    .resize(200, 200)
                    .into(imageView)

            }
        }
    }
}