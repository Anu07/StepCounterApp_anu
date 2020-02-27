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
import com.sd.src.stepcounterapp.model.wallet.walletdetailresponse.Wishlist
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_challenges.challengeImg
import kotlinx.android.synthetic.main.dialog_challenges.txtDepartment
import kotlinx.android.synthetic.main.dialog_challenges.txtName
import kotlinx.android.synthetic.main.dialog_marketplace.*
import kotlinx.android.synthetic.main.vendor_view.*

class WishListDialog(
    context: Context,
    data: Wishlist,
    themeResId: Int,
    private val LayoutId: Int,
    var mListener: WishListInterface
) : BaseDialog(context, themeResId) {
    private var clicked: Boolean = false
    var mData: Wishlist = data

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
        txtDetailLong.text = HtmlCompat.fromHtml(mData.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        wishlistImg.setImageResource(R.drawable.wishlist_fill)
        vendorEmail.text = mData.vendorId.email
        vendorWeb.text = mData.vendorId.websiteUrl
        vendorDetail.text = HtmlCompat.fromHtml(mData.vendorId.description.capitalize(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        Picasso.get().load(IMG_URL + mData.vendorId.image).into(vendorLogo)
        btnPurchaseStart.setOnClickListener {
            mListener.onPurchase(mData)
        }
        wishlistImg.setOnClickListener {
            mListener.onWishlist(mData)
        }

    }


    override fun clickIndex(pos: Int) {
        dismiss()
    }

    private fun checkWishListIcon() {
        if (clicked) {
            wishlistImg.setImageResource(R.drawable.wishlist_fill)
        } else {
            wishlistImg.setImageResource(R.drawable.featured)
        }
    }

    interface WishListInterface {
        fun onPurchase(data: Wishlist)
        fun onWishlist(data: Wishlist)

    }
}
