package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_market_place_product.view.imgProduct
import kotlinx.android.synthetic.main.item_market_place_product.view.txtProductName
import kotlinx.android.synthetic.main.item_market_place_product.view.txtShortDesc
import kotlinx.android.synthetic.main.item_market_place_product.view.txtToken
import kotlinx.android.synthetic.main.item_market_place_see_all.view.*
import java.util.stream.Collectors


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
       /* if(mData[position].wishlist!=null){
            mData[position].wishListed =mData[position].wishlist
        }else{
            mData[position].wishListed = false
        }*/
        holder.txtQty.text = mData[position].quantity.toString()+" left"
        holder.txtProductName.text = mItem.name
        holder.txtShortDesc.text = mItem.shortDesc
        holder.txtToken.text = "${mItem.token} TKNS"
        Picasso.get().load(IMG_URL + "" + mItem.image).placeholder(R.drawable.image_overlay).into(holder.imgProduct)
        Log.i("flag", "" + mData[position]._id)
        checkItemStatus(position, holder)
        holder.wishListView.setOnClickListener {
            mData[position].wishListed = !mData[position].wishListed
            checkItemStatus(position,holder)
            mListener.onPopularItemwishlisted(position, mItem)
        }
        holder.parentLay.setOnClickListener {
            mListener.onPopClick(position, mItem)
        }

    }

    private fun checkItemStatus(
        position: Int,
        holder: ViewHolder
    ) {
        if (mData[position].wishListed) {
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
        val parentLay = itemView.parentLaypop
        val txtQty = itemView.txtQty!!
    }


    fun swap(mList: ArrayList<PopularProducts.Data>) {
       /* if (mData.size > 0){
            mData.clear()
        }*/
        mData=mList
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