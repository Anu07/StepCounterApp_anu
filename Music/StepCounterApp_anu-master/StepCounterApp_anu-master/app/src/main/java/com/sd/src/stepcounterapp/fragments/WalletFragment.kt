package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.activities.PurchasedDetails
import com.sd.src.stepcounterapp.adapter.WalletRedeemListAdapter
import com.sd.src.stepcounterapp.adapter.WalletWishListAdapter
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.convertToLocal
import com.sd.src.stepcounterapp.dialog.WishListDialog
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.model.wallet.purchase.PurchaseResponse
import com.sd.src.stepcounterapp.model.wallet.walletdetailresponse.Purchased
import com.sd.src.stepcounterapp.model.wallet.walletdetailresponse.WalletModel
import com.sd.src.stepcounterapp.model.wallet.walletdetailresponse.Wishlist
import com.sd.src.stepcounterapp.model.wishlist.AddWishRequest
import com.sd.src.stepcounterapp.model.wishlist.basicwishlist.BasicWishListResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.WISHCOUNT
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.viewModels.WalletViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment : BaseFragment(), WalletWishListAdapter.PurchaseListener,
    WalletRedeemListAdapter.RedeemListener {
    override fun onWishClick(pos: Int) {
        marketDialog =
            WishListDialog(mContext,
                mDataWishList[pos],
                R.style.pullBottomfromTop,
                R.layout.dialog_marketplace,
                object : WishListDialog.WishListInterface {
                    override fun onWishlist(data: Wishlist) {
                        showPopupProgressSpinner(true)
                        mViewModel.removeWishList(
                            AddWishRequest(
                                SharedPreferencesManager.getUserId(context!!),
                                data._id
                            )
                        )
                    }

                    override fun onPurchase(data: Wishlist) {
                        if (data != null) {
                            var alertbuilder = Utils.purchaseNowdialog(requireContext())
                            alertbuilder.setPositiveButton("Yes") { _, _ ->
                                showPopupProgressSpinner(true)
                                mViewModel.hitPurchaseApi(
                                    RedeemRequest(
                                        data._id, SharedPreferencesManager.getUserId(
                                            mContext
                                        )
                                    )
                                )
                            }
                            alertbuilder.create().show()

                        } else {
                            Toast.makeText(
                                mContext,
                                "This product has been out of stock, please try another product.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                })
        marketDialog.show()
    }

    override fun onRedeem(position: Int) {
        startActivity(
            Intent(mContext, PurchasedDetails::class.java).putExtra(
                DEALDATA,
                mDataReedemList[position]
            )
        )
    }

    override fun onDelete(pos: Int) {
        showPopupProgressSpinner(true)
        mViewModel.hitDeleteApi(
            RedeemRequest(
                mDataWishList[pos]._id.toString(),
                SharedPreferencesManager.getUserId(mContext)
            )
        )
    }

    override fun onPurchaseNow(pos: Int) {
        if (mDataWishList[pos].quantity > 0) {
            var alertbuilder = Utils.purchaseNowdialog(requireContext())
            alertbuilder.setPositiveButton("Yes") { _, _ ->
                showPopupProgressSpinner(true)
                mViewModel.hitPurchaseApi(
                    RedeemRequest(
                        mDataWishList[pos]._id.toString(),
                        SharedPreferencesManager.getUserId(mContext)
                    )
                )
            }
            alertbuilder.create().show()

        } else {
            Toast.makeText(
                mContext,
                "This product has been out of stock, please try another product.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: WalletFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        @JvmStatic
        fun newInstance(context: Context): WalletFragment {
            instance = WalletFragment()
            mContext = context
            return instance
        }

        val DEALDATA: String = "DealData"

    }

    lateinit var marketDialog: WishListDialog
    lateinit var mWishListAdapter: WalletWishListAdapter
    lateinit var mRedeemListAdapter: WalletRedeemListAdapter
    private var mDataWishList: ArrayList<Wishlist> = ArrayList()
    private var mDataReedemList: ArrayList<Purchased> = ArrayList()
    private lateinit var mViewModel: WalletViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            (activity as LandingActivity).showDisconnection(false)
        } catch (e: Exception) {

            Log.e("message","message"+e.message)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(WalletViewModel::class.java)

        mViewModel.removeWishList().observe(this,
            Observer<BasicWishListResponse> {
                showPopupProgressSpinner(false)
                try {
                    if (marketDialog != null && marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                    if (marketDialog != null && marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                } catch (e: Exception) {
                }
                if (it != null) {
                    if (it.status == 200) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            mViewModel.hitWalletApi()
                        }
                    }
                }
            })

//        {"status":200,"message":"Success","data":{"totalEarnings":27,"remainingSteps":104,"lastUpdated":"2019-09-05T09:34:23.420Z"}}
        mViewModel.getStepToken().observe(this,
            Observer<TokenModel> { mData ->
                try {
                    showPopupProgressSpinner(false)
                } catch (e: Exception) {
                }
                if (mData.data != null) {
                    txtTokens.text = mData.data!!.totalEarnings.toString()
                    txtSteps.text = mData.data!!.remainingSteps.toString()
                    if (mData.data!!.lastUpdated != null) {
                        customTextView4.visibility = View.VISIBLE
                        txtUpdateTime.text = changeDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSS",
                            "dd MMM, yyyy",
                            mData.data!!.lastUpdated
                        ) + convertToLocal(mData.data!!.lastUpdated)
                    }else{
                        customTextView4.visibility = View.GONE
                    }
                    if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                        mViewModel.hitWalletApi()
                    }
                } else {
                    Toast.makeText(mContext, mData.message, Toast.LENGTH_LONG).show()
                }
            })

        mViewModel.getDeleteResponse().observe(this,
            Observer<BasicWishListResponse> { mData ->
                try {
                    showPopupProgressSpinner(false)
                } catch (e: Exception) {
                }
                Toast.makeText(mContext, mData.message, Toast.LENGTH_LONG).show()
                if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                    mViewModel.hitWalletApi()
                }
            })

        mViewModel.getPurchase().observe(this,
            Observer<PurchaseResponse> { mData ->
                try {
                    if (marketDialog != null) {
                        marketDialog.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("trace", e.message)
                }



                showPopupProgressSpinner(false)
                if (mData != null) {
                    if (mData.status == 200) {
                        Toast.makeText(
                            mContext,
                            "Product purchased successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            mViewModel.hitWalletApi()
                        }
                    } else {
                        Toast.makeText(mContext, mData.message, Toast.LENGTH_LONG).show()
                    }
                }
            })

        mViewModel.getWalletData().observe(this,
            Observer<WalletModel> { mData ->
                showPopupProgressSpinner(false)
                if (mData != null && mData.data != null) {
                    txtTokens.text = mData.data.totalGenerated.toString()
                    txtSteps.text = mData.data?.steps.toString()
                    tokensVal.text = mData.data?.totalEarnings.toString()
                    if (mData.data!!.updatedAt != null) {
                        customTextView4.visibility = View.VISIBLE
//                        txtUpdateTime.text = Utils.formateDateFromstring("yyyy-MM-dd", "dd MMM, yyyy",mData.data!!.updatedAt.split("T")[0])
                        txtUpdateTime.text = changeDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSS",
                            "dd MMM, yyyy",
                            mData.data!!.updatedAt
                        ) + " | " + convertToLocal(mData.data!!.updatedAt)
                    }else{

                        customTextView4.visibility = View.GONE

                    }
                    if (mData.data!!.wishlist != null && mData.data?.wishlist!!.size > 0) {
                        mDataWishList = (mData.data?.wishlist as ArrayList<Wishlist>?)!!
                        SharedPreferencesManager.setInt(mContext, WISHCOUNT, mDataWishList.size)
                        setWishListView()
                        setWishListAdapter()
                        if (txtWishSeeAll.text.toString().equals("See Less", true)) {
                            txtWishSeeAll.text = "See Less"
                            rvWishList.visibility = View.VISIBLE
                            llWishListSeeLess.visibility = View.GONE

                        } else {
                            txtWishSeeAll.text = "See All"
                            rvWishList.visibility = View.GONE
                            llWishListSeeLess.visibility = View.VISIBLE
                        }
//                        llWishListSeeLess.visibility = View.VISIBLE
                        txtWishSeeAll.visibility = View.VISIBLE
                        txtNoWishList.visibility = View.GONE

                    } else {
                        mDataWishList = arrayListOf()
                        setWishListAdapter()
                        setWishListView()


                        llWishListSeeLess.visibility = View.GONE
                        txtWishSeeAll.visibility = View.GONE
                        txtNoWishList.visibility = View.VISIBLE
                    }

                    if (mData.data!!.purchased != null && mData.data!!.purchased!!.size > 0) {
                        mDataReedemList = (mData.data?.purchased as ArrayList<Purchased>?)!!
                        setReedemListAdapter()
                        setRedeemListView()
                        if (txtRedeemSeeAll.text.toString().equals("See Less", true)) {
                            txtRedeemSeeAll.text = "See Less"
                            rvRedeem.visibility = View.VISIBLE
                            llRedeemSeeLess.visibility = View.GONE

                        } else {
                            txtRedeemSeeAll.text = "See All"
                            rvRedeem.visibility = View.GONE
                            llRedeemSeeLess.visibility = View.VISIBLE
                        }
                        txtNoRedeemList.visibility = View.GONE
                    } else {
                        mDataReedemList = arrayListOf()
                        setReedemListAdapter()
                        setRedeemListView()
                        llRedeemSeeLess.visibility = View.GONE
                        txtRedeemSeeAll.visibility = View.GONE
                        txtNoRedeemList.visibility = View.VISIBLE
                    }
                } else {
                    mDataWishList = arrayListOf()
                    setWishListAdapter()
                    setWishListView()
                    llWishListSeeLess.visibility = View.GONE
                    txtWishSeeAll.visibility = View.GONE
                    txtNoWishList.visibility = View.VISIBLE

                    mDataReedemList = arrayListOf()
                    setReedemListAdapter()
                    setRedeemListView()
                    llRedeemSeeLess.visibility = View.GONE
                    txtRedeemSeeAll.visibility = View.GONE
                    txtNoRedeemList.visibility = View.VISIBLE
                }

            })

//        mViewModel.setTokensFromSteps()
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            showPopupProgressSpinner(true)
            mViewModel.hitWalletApi()
        }

        txtWishSeeAll.setOnClickListener {
            if (!rvWishList.isVisible) {
                txtWishSeeAll.text = "See Less"
                rvWishList.visibility = View.VISIBLE
                llWishListSeeLess.visibility = View.GONE
            } else {
                txtWishSeeAll.text = "See All"
                rvWishList.visibility = View.GONE
                llWishListSeeLess.visibility = View.VISIBLE
            }
        }

        txtRedeemSeeAll.setOnClickListener {
            if (!rvRedeem.isVisible) {
                txtRedeemSeeAll.text = "See Less"
                rvRedeem.visibility = View.VISIBLE
                llRedeemSeeLess.visibility = View.GONE
            } else {
                txtRedeemSeeAll.text = "See All"
                rvRedeem.visibility = View.GONE
                llRedeemSeeLess.visibility = View.VISIBLE
            }
        }

        imgDeleteFirst.setOnClickListener {
            onDelete(0)
        }
        imgDeleteSecond.setOnClickListener {
            onDelete(1)
        }

        historyView.setOnClickListener {
            (mContext as LandingActivity).hideBottomLayout(true)
            openFragment()
        }

        convertTokens.setOnClickListener {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                if(txtSteps.text.toString().toInt()>999){
                    showPopupProgressSpinner(true)
                    mViewModel.setTokensFromSteps()
                }else{
                    Toast.makeText(requireActivity(),"You do not have enough steps to convert token.",Toast.LENGTH_SHORT).show()
                }

            }
        }

        imgProductFirst.setOnClickListener {
            onWishClick(0)
        }

        imgProductSecond.setOnClickListener {
            onWishClick(1)
        }

        wishListinfo.setOnClickListener {
            Snackbar.make(
                wishListinfo,
                "Wishlist products would be displayed.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        purchasedInfo.setOnClickListener {
            Snackbar.make(
                purchasedInfo,
                "Purchased products would be displayed.",
                Snackbar.LENGTH_SHORT
            ).show()
        }

//        (mContext as LandingActivity).showDisconnection(false)
    }


    private fun setWishListAdapter() {
        rvWishList.layoutManager = LinearLayoutManager(mContext)
        mWishListAdapter = WalletWishListAdapter(mDataWishList, this)
        rvWishList.adapter = mWishListAdapter
    }

    private fun setWishListView() {
//        if (mDataWishList.size in 1..2) {


        if (mDataWishList.isNotEmpty()) {
            if (mDataWishList.size >= 1) {
                cdWishFirst.visibility = View.VISIBLE
                rvWishList.visibility = View.GONE
                txtWishSeeAll.visibility = View.GONE
                txtProductNameFirst.text = mDataWishList[0].name
                txtShortDescFirst.text = mDataWishList[0].shortDesc
                txtTokenFirst.text = "${mDataWishList[0].token} TKNS"
                txtFirProdQty.text =  mDataWishList[0].quantity.toString()+" left"
                Log.i("Img","url"+IMG_URL + "" + mDataWishList[0].image)
                Picasso.get().load(IMG_URL + "" + mDataWishList[0].image)
                    .resize(200, 200)
                    .into(imgProductFirst)
                purchaseFirst.setOnClickListener {
                    if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
                        var alertbuilder = Utils.purchaseNowdialog(requireContext())
                        alertbuilder.setPositiveButton("Yes") { _, _ ->
                            showPopupProgressSpinner(true)

                            mViewModel.hitPurchaseApi(
                                RedeemRequest(
                                    mDataWishList[0]._id.toString(),
                                    SharedPreferencesManager.getUserId(mContext)
                                )
                            )
                        }
                        alertbuilder.create().show()

                    }
                }

                if (mDataWishList.size >= 2) {
                    cdWishSecond.visibility = View.VISIBLE
                    txtProductNameSecond.text = mDataWishList[1].name
                    txtShortDescSecond.text = mDataWishList[1].shortDesc
                    txtTokenSecond.text = "${mDataWishList[1].token} TKNS"
                    txtSecProdQty.text =  mDataWishList[1].quantity.toString()+" left"
                    Log.i("Img","url"+IMG_URL + "" + mDataWishList[1].image)
                    Picasso.get().load(IMG_URL + "" + mDataWishList[1].image)
                        .resize(200, 200)
                        .into(imgProductSecond)
                    purchaseSecond.setOnClickListener {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
                            var alertbuilder = Utils.purchaseNowdialog(requireContext())
                            alertbuilder.setPositiveButton("Yes") { _, _ ->
                                showPopupProgressSpinner(true)
                                mViewModel.hitPurchaseApi(
                                    RedeemRequest(
                                        mDataWishList[1]._id.toString(),
                                        SharedPreferencesManager.getUserId(mContext)
                                    )
                                )
                            }
                            alertbuilder.create().show()

                        }
                    }
                } else {
                    cdWishSecond.visibility = View.GONE
                }
                /*  } else {
                  cdWishSecond.visibility = View.GONE
                  cdWishFirst.visibility = View.GONE
                  rvWishList.visibility = View.VISIBLE
              }*/

            }/* else {
                rvWishList.visibility = View.VISIBLE
            }*/
        }
    }

    private fun setReedemListAdapter() {
        rvRedeem.layoutManager = LinearLayoutManager(mContext)
        mRedeemListAdapter = WalletRedeemListAdapter(mDataReedemList)
        rvRedeem.adapter = mRedeemListAdapter
    }

    private fun setRedeemListView() {
        if (mDataReedemList.size > 0) {
            txtProductNameRedeemFirst.text = mDataReedemList[0].name
            txtShortDescRedeemFirst.text = mDataReedemList[0].shortDesc
            txtTokenRedeemFirst.text = "${mDataReedemList[0].token} TKNS"
            Picasso.get().load(IMG_URL + "" + mDataReedemList[0].image)
                .resize(200, 200)
                .placeholder(R.drawable.placeholder).into(imgProductRedeemFirst)
            firsLayout.setOnClickListener {
                onRedeem(0)
            }



            if (mDataReedemList.size > 1) {
                cdRedeemSecond.visibility = View.VISIBLE
                txtProductNameRedeemSecond.text = mDataReedemList[1].name
                txtShortDescRedeemSecond.text = mDataReedemList[1].shortDesc
                txtTokenRedeemSecond.text = "${mDataReedemList[1].token} TKNS"
                Picasso.get().load(IMG_URL + "" + mDataReedemList[1].image)
                    .resize(200, 200)
                    .placeholder(R.drawable.placeholder).into(imgProductRedeemSecond)
                secondLayout.setOnClickListener {
                    onRedeem(1)
                }
            } else {
                cdRedeemSecond.visibility = View.GONE
            }
        } else {
            cdRedeemSecond.visibility = View.GONE
            cdRedeemFirst.visibility = View.GONE
        }
    }


    private fun openFragment() {
        var fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.container, TransactionHistoryFragment.newInstance(mContext))
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()


    }

    fun setUpdatedSteps(steps: String) {
        if (txtSteps != null)
            txtSteps.text = steps
    }

    override fun onResume() {
        super.onResume()
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            mViewModel.hitWalletApi()
        }
    }

}
