package com.sd.src.stepcounterapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.wallet.walletDetailResponse.Wishlist
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_wallet_wish_list.view.*

class WalletWishListAdapter(var mData: ArrayList<Wishlist>, var mListener: PurchaseListener) :
    RecyclerView.Adapter<WalletWishListAdapter.ViewHolder>() {

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vhItem: ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet_wish_list, parent, false)
        vhItem = ViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtProductName.text = mData[position].name
        holder.txtShortDesc.text = mData[position].shortDesc
        holder.txtToken.text = "${mData[position].token} TKS"
        Picasso.get().load(RetrofitClient.IMG_URL + "" + mData[position].image).into(holder.imgProduct)
        holder.purchaseBttn.setOnClickListener {
            mListener.onPurchaseNow(position)
        }
        holder.imgDeleteWish.setOnClickListener {
            mListener.onDelete(position)
        }
        holder.parentWishLayout.setOnClickListener {
            mListener.onWishClick(position)
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
        val purchaseBttn = itemView.purchaseBttn!!
        val imgDeleteWish = itemView.imgDeleteWish!!
        val parentWishLayout =itemView.parentWishLayout!!
    }


    interface PurchaseListener{
        fun onPurchaseNow(pos:Int)
        fun onDelete(pos:Int)
        fun onWishClick(pos:Int)

    }
}