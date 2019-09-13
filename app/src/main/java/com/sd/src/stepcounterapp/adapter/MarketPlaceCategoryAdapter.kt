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
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_market_place_category.view.*

class MarketPlaceCategoryAdapter(
    categoryData: ArrayList<MarketResponse.Data>,
    var context: Context,
    click: MarketPlaceClickInterface,
    var wisListener: twoItemListener,
    var clckListener: ClickMarketListener
) :
    RecyclerView.Adapter<MarketPlaceCategoryAdapter.ViewHolder>() {

    private var clicked: Boolean = false
    private var clickedSec: Boolean = false
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
        var mItem = mCategoryData[position]
        holder.txtCategoryName.text = mItem.name

//        Toast.makeText(mContext, mCategoryData[position].name, Toast.LENGTH_SHORT).show()
        if (mItem != null) {
            Picasso.get().load(RetrofitClient.IMG_URL + "" + mItem.products[0].image).error(R.drawable.placeholder)
                .into(holder.imgProductFirst)
            holder.txtProductNameFirst.text = mItem.products[0].name
            holder.txtShortDescFirst.text = mItem.products[0].shortDesc
            holder.txtTokenFirst.text = mItem.products[0].token.toString()+" TKS"
            holder.firstParentLay.setOnClickListener {
                clckListener.onClick(position,mItem.products[0])
            }

            Log.i("flag cat", "" + mItem.products[0].wishlist)


            holder.wishBttnFirst.setOnClickListener {
                clicked = !clicked
                checkWishListIcon(holder)
                wisListener.onWish(position, mItem.products[0])
            }
            if (mItem.products[0].wishlist) {
                holder.wishBttnFirst.setImageResource(R.drawable.wishlist_fill)
            } else {
                holder.wishBttnFirst.setImageResource(R.drawable.featured)
            }

            if (mItem.products.size >= 2) {
                Picasso.get().load(RetrofitClient.IMG_URL + "" + mItem.products[1].image).error(R.drawable.placeholder)
                    .into(holder.imgProductSecond)
                holder.txtProductNameSecond.text = mItem.products[1].name
                holder.txtShortDescSecond.text = mItem.products[1].shortDesc
                holder.txtTokenSecond.text = mItem.products[1].token.toString()+" TKS"
                Log.i("flag cat", "" + mItem.products[1].wishlist)
                holder.cdSecond.visibility = View.VISIBLE
                holder.txtSeeAll.visibility = View.VISIBLE
                holder.secParentLay.setOnClickListener {
                    clckListener.onClick(position,mItem.products[1])
                }

                holder.txtSeeAll.setOnClickListener {
                    itemClick.onSeeAllClick(position)
                }

                holder.wishBttnSecond.setOnClickListener {
                    clickedSec = !clickedSec
                    checkWishListIcon(holder)
                    wisListener.onWish(position, mItem.products[1])
                }
                if (mItem.products[1].wishlist) {
                    holder.wishBttnSecond.setImageResource(R.drawable.wishlist_fill)
                } else {
                    holder.wishBttnSecond.setImageResource(R.drawable.featured)
                }

            }


        }
    }


    fun swap(mList: ArrayList<MarketResponse.Data>){
        if(mCategoryData.size>0)
            mCategoryData.clear()
        mCategoryData  = mList
        notifyDataSetChanged()
    }

    private fun checkWishListIcon(holder: ViewHolder) {
        if (clickedSec) {
            holder.wishBttnSecond.setImageResource(R.drawable.wishlist_fill)
        } else {
            holder.wishBttnSecond.setImageResource(R.drawable.featured)
        }
    }

    override fun getItemCount(): Int {
        return mCategoryData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtCategoryName = itemView.txtCategoryName!!
        val txtSeeAll = itemView.txtSeeAll!!
        val firstParentLay = itemView.firstCatItem
        val secParentLay = itemView.cdSecond
        val imgProductFirst = itemView.imgProductFirst!!
        val txtProductNameFirst = itemView.txtProductNameFirst!!
        val txtShortDescFirst = itemView.txtShortDescFirst!!
        val txtTokenFirst = itemView.txtTokenFirst!!

        val imgProductSecond = itemView.imgProductSecond!!
        val txtProductNameSecond = itemView.txtProductNameSecond!!
        val txtShortDescSecond = itemView.txtShortDescSecond!!
        val txtTokenSecond = itemView.txtTokenSecond!!
        val wishBttnFirst = itemView.wishBttnFirst!!
        val wishBttnSecond = itemView.wishBttnSecond!!
        val cdSecond = itemView.cdSecond!!
    }

    interface twoItemListener {
        fun onWish(
            position: Int,
            mItem: MarketResponse.Products
        )
    }
    interface ClickMarketListener {
        fun onClick(
            position: Int,
            mItem: MarketResponse.Products
        )


    }

}