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
            Picasso.get().load(RetrofitClient.IMG_URL + "" + mItem[0].image).error(R.drawable.placeholder)
                .into(holder.imgProductFirst)
            holder.txtCategoryName.text = mItem[0].name
            holder.txtProductNameFirst.text = mItem[0].name
            holder.txtShortDescFirst.text = mItem[0].shortDesc
            holder.txtTokenFirst.text = mItem[0].token.toString()

            Log.i("flag cat", "" + mItem[0].wishlist)

            if (position <= 1 && mItem[position].wishlist) {
                holder.wishListView.setImageResource(R.drawable.wishlist_fill)
            } else {
                holder.wishListView.setImageResource(R.drawable.featured)
            }

            holder.wishListView.setOnClickListener {
                wisListener.onWish(position, mItem[position])
            }

            if (position == 1) {
                 holder.cdSecond.visibility=View.VISIBLE
                Picasso.get().load(RetrofitClient.IMG_URL + "" + mItem[1].image).error(R.drawable.placeholder)
                    .into(holder.imgProductSecond)
                holder.txtProductNameSecond.text = mItem[1].name
                holder.txtShortDescSecond.text = mItem[1].shortDesc
                holder.txtTokenSecond.text = mItem[1].token.toString()
                Log.i("flag cat", "" + mItem[1].wishlist)

                holder.txtSeeAll.setOnClickListener {
                    itemClick.onSeeAllClick(position)
                }
            } else{
                holder.cdSecond.visibility=View.GONE
            }
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