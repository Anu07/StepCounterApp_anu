package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.interfaces.MarketPlaceClickInterface
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_market_place_category.view.*

class MarketPlaceCategoryAdapter(
    categoryData: ArrayList<MarketResponse.Data>,
    var context: Context,
    click: MarketPlaceClickInterface,
    var wisListener: twoItemListener
) :
    RecyclerView.Adapter<MarketPlaceCategoryAdapter.ViewHolder>() {

    private var mCategoryData: ArrayList<MarketResponse.Data> = categoryData
    var mContext: Context = context
    var itemClick: MarketPlaceClickInterface = click

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vhItem: ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_market_place_category, parent, false)
        vhItem = ViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var mItem = mCategoryData[position].products

//        Toast.makeText(mContext, mCategoryData[position].name, Toast.LENGTH_SHORT).show()
        if (mItem != null) {
            mItem.forEachIndexed { _, products ->
                Picasso.get().load(RetrofitClient.IMG_URL + "" + products.image).error(R.drawable.placeholder)
                    .into(holder.imgProductFirst)
                holder.txtCategoryName.text =products.name
                holder.txtProductNameFirst.text =products.name
                holder.txtShortDescFirst.text = products.shortDesc
                holder.txtTokenFirst.text = products.token.toString()

                Log.i("flag cat", "" + position + products.wishlist)
                if(products.wishlist){
                    holder.wishListView.setImageResource(R.drawable.wishlist_fill)
                }else{
                    holder.wishListView.setImageResource(R.drawable.featured)
                }
                holder.wishListView.setOnClickListener {
                    holder.wishListView.setImageResource(R.drawable.wishlist_fill)
                    wisListener.onWish(position, products)
                }
            }

        } else {
            holder.txtSeeAll.visibility = View.GONE
            holder.cdSecond.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mCategoryData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtCategoryName = itemView.txtCategoryName!!
        val txtSeeAll = itemView.txtSeeAll!!

        val imgProductFirst = itemView.imgProductFirst!!
        val txtProductNameFirst = itemView.txtProductNameFirst!!
        val txtShortDescFirst = itemView.txtShortDescFirst!!
        val txtTokenFirst = itemView.txtTokenFirst!!

        val imgProductSecond = itemView.imgProductSecond!!
        val txtProductNameSecond = itemView.txtProductNameSecond!!
        val txtShortDescSecond = itemView.txtShortDescSecond!!
        val txtTokenSecond = itemView.txtTokenSecond!!
        val wishListView = itemView.wishBttn!!
        val cdSecond = itemView.cdSecond!!
    }

    interface twoItemListener {
        fun onWish(
            position: Int,
            mItem: MarketResponse.Products
        )
    }


}