package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_challenges.challengeImg
import kotlinx.android.synthetic.main.dialog_challenges.txtDepartment
import kotlinx.android.synthetic.main.dialog_challenges.txtName
import kotlinx.android.synthetic.main.dialog_marketplace.*
import kotlinx.android.synthetic.main.vendor_view.*

class MarketPopPlaceDialog(
    context: Context,
    data: PopularProducts.Data,
    themeResId: Int,
    private val LayoutId: Int,
    var mListener: PurchaseInterface)
    : BaseDialog(context, themeResId) {
    private var clicked: Boolean = false
    var mData: PopularProducts.Data = data

    init {
        val wmlp = this.window!!.attributes
        wmlp.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        window!!.attributes = wmlp
    }

    override fun getInterfaceInstance(): InterfacesCall.IndexClick {
        return this
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateStuff() {
        setData()
    }

    override fun getContentView(): Int {
        return LayoutId
    }

    fun setData() {
        Picasso.get().load(IMG_URL+mData.image).into(challengeImg)
        txtName.text = mData.name
        txtDepartment.text = mData.shortDesc
        txtToken.text = mData.token.toString()+" TKNS"
        txtDetailLong.text = HtmlCompat.fromHtml(mData.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        vendorDetail.text = HtmlCompat.fromHtml(mData.vendor.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        Picasso.get().load(IMG_URL+mData.vendor.image).placeholder(R.drawable.placeholder).resize(150,150).into(vendorIcon)
        vendorEmail.text = mData.vendor.email
        vendorWeb.text = mData.vendor.websiteUrl
        Picasso.get().load(IMG_URL+mData.vendor.image).placeholder(R.drawable.placeholder).resize(150,150).into(vendorLogo)
        mData.wishListed = mData.wishlist
        checkWishListIcon()
        btnPurchaseStart.setOnClickListener {
            mListener.onPurchase(mData)
        }
        wishlistImg.setOnClickListener {
            mData.wishListed = !mData.wishListed
            checkWishListIcon()
            mListener.onWishlist(mData)
        }

    }


    override fun clickIndex(pos: Int) {
        dismiss()
    }

    private fun checkWishListIcon() {
        if (mData.wishlist) {
           wishlistImg.setImageResource(R.drawable.wishlist_fill)
        } else {
            wishlistImg.setImageResource(R.drawable.featured)
        }
    }

    interface PurchaseInterface{
        fun onPurchase(data: PopularProducts.Data)
        fun onWishlist(data: PopularProducts.Data)

    }
}
