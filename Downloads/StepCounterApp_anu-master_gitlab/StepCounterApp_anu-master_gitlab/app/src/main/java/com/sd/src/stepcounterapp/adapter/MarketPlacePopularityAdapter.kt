package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_market_place_product.view.imgProduct
import kotlinx.android.synthetic.main.item_market_place_product.view.txtProductName
import kotlinx.android.synthetic.main.item_market_place_product.view.txtShortDesc
import kotlinx.android.synthetic.main.item_market_place_product.view.txtToken
import kotlinx.android.synthetic.main.item_market_place_see_all.view.*


class MarketPlacePopularityAdapter(
    categoryData: ArrayList<PopularProducts.Data>,
    var context: Context,
    var mListener: PopularInterface) :
    RecyclerView.Adapter<MarketPlacePopularityAdapter.ViewHolder>() {
    private var mData: ArrayList<PopularProducts.Data> = categoryData
    var mContext: Context = context

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vhItem: ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_market_place_see_all, parent, false)
        vhItem = ViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var mItem = mData[position]
        holder.txtProductName.text = mItem.name
        holder.txtShortDesc.text = mItem.shortDesc
        holder.txtToken.text = "${mItem.token} TKS"
        Picasso.get().load(RetrofitClient.IMG_URL + "" + mItem.image).placeholder(R.drawable.image_overlay)
            .resize(400, 200).into(holder.imgProduct)
        Log.i("flag", "" + mData[position].wishlist)
        if (mData[position].wishlist) {
            holder.wishListView.setImageResource(R.drawable.wishlist_fill)
        } else {
            holder.wishListView.setImageResource(R.drawable.featured)
        }
        holder.wishListView.setOnClickListener {
            holder.wishListView.setImageResource(R.drawable.wishlist_fill)
            mListener.onPopularItemwishlisted(position, mItem)
        }
        holder.parentLay.setOnClickListener {
            mListener.onPopClick(position, mItem)
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgProduct = itemView.imgProduct!!
        val txtProductName = itemView.txtProductName!!
        val txtShortDesc = itemView.txtShortDesc!!
        val txtToken = itemView.txtToken!!
        val wishListView = itemView.wishBttnall!!
        val parentLay = itemView.parentLaypop
    }


    fun swap(mList: ArrayList<PopularProducts.Data>) {
        if (mData.size > 0)
            mData.clear()
        mData = mList
        notifyDataSetChanged()
    }

    interface PopularInterface {
        fun onPopularItemwishlisted(
            position: Int,
            mItem: PopularProducts.Data
        )

        fun onPopClick(
            position: Int,
            mItem: PopularProducts.Data
        )
    }
}