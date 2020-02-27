package com.sd.src.stepcounterapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_market_place_product.view.imgProduct
import kotlinx.android.synthetic.main.item_market_place_product.view.txtProductName
import kotlinx.android.synthetic.main.item_market_place_product.view.txtShortDesc
import kotlinx.android.synthetic.main.item_market_place_product.view.txtToken
import kotlinx.android.synthetic.main.item_market_place_see_all.view.*

class MarketPlaceSeeAllAdapter(
    var mData: ArrayList<MarketResponse.Products>, var mListener: CategoryInterface,
    var clckListener: MarketPlaceCategoryAdapter.ClickMarketListener

) :
    RecyclerView.Adapter<MarketPlaceSeeAllAdapter.ViewHolder>() {


    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vhItem: ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_market_place_see_all, parent, false)
        vhItem = ViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtProductName.text = mData[position].name
        holder.txtShortDesc.text = mData[position].shortDesc
        holder.txtToken.text = "${mData[position].token} TKNS"
        holder.txtQty.text = mData[position].quantity.toString()+" left"
//        mData[position].wishlisted = mData[position].wishlist
        Log.e("Settings", "All adapter"+IMG_URL + "" + mData[position].image)
        Picasso.get().load(IMG_URL + "" + mData[position].image).into(holder.imgProduct)
        checkWishListStatus(position, holder)

        holder.parentLay.setOnClickListener {
            clckListener.onClick(position, mData[position])
        }

        holder.wishListView.setOnClickListener {
            mData[position].wishlisted = !mData[position].wishlisted
            checkWishListStatus(position, holder)
            mListener.onItemwishlisted(position, mData[position])
        }
    }

    fun checkWishListStatus(
        position: Int,
        holder: ViewHolder
    ) {
        if (mData[position].wishlisted) {
            holder.wishListView.setImageResource(R.drawable.wishlist_fill)
        } else {
            holder.wishListView.setImageResource(R.drawable.featured)
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
        val parentLay = itemView.parentLaypop!!
        val txtQty = itemView.txtQty!!

    }


    interface CategoryInterface {
        fun onItemwishlisted(
            position: Int,
            mItem: MarketResponse.Products
        )
    }


    fun swap(mList: ArrayList<MarketResponse.Products>) {
        if (mData.size > 0)
            mData.clear()
        mData=mList
        notifyDataSetChanged()
    }

}