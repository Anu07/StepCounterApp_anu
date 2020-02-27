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
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_challenges.challengeImg
import kotlinx.android.synthetic.main.dialog_challenges.txtDepartment
import kotlinx.android.synthetic.main.dialog_challenges.txtName
import kotlinx.android.synthetic.main.dialog_marketplace.*
import kotlinx.android.synthetic.main.vendor_view.*

class MarketPlaceDialog(
    context: Context,
    data: MarketResponse.Products,
    themeResId: Int,
    private val LayoutId: Int,
    var mListener: PurchaseInterface
) : BaseDialog(context, themeResId) {
    private var clicked: Boolean = false
    var mData: MarketResponse.Products = data

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
        Picasso.get().load(IMG_URL + mData.image).into(challengeImg)
        txtName.text = mData.name.capitalize()
        txtDepartment.text = mData.shortDesc.capitalize()
        txtToken.text = mData.token.toString() + " TKNS"
        txtDetailLong.text = HtmlCompat.fromHtml(mData.description.capitalize(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        vendorDetail.text = HtmlCompat.fromHtml(mData.vendors.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        Picasso.get().load(IMG_URL + mData.vendors.image).placeholder(R.drawable.placeholder)
            .resize(150, 150).into(vendorIcon)
        vendorEmail.text = mData.vendors.email
        vendorWeb.text = mData.vendors.websiteUrl
        Picasso.get().load(IMG_URL + mData.vendors.image).placeholder(R.drawable.placeholder)
            .resize(150, 150).into(vendorLogo)
        checkWishListIcon(mData.wishlisted)
        btnPurchaseStart.setOnClickListener {
            mListener.onPurchase(mData)
        }
        wishlistImg.setOnClickListener {
            mData.wishlisted = !mData.wishlisted
            mListener.onWishlist(mData)
        }

    }


    override fun clickIndex(pos: Int) {
        dismiss()
    }

    private fun checkWishListIcon(wishlisted: Boolean) {
        if (wishlisted) {
            wishlistImg.setImageResource(R.drawable.wishlist_fill)
        } else {
            wishlistImg.setImageResource(R.drawable.featured)
        }
    }

    interface PurchaseInterface {
        fun onPurchase(data: MarketResponse.Products)
        fun onWishlist(data: MarketResponse.Products)

    }
}
