package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.adapter.WalletRedeemListAdapter
import com.sd.src.stepcounterapp.adapter.WalletWishListAdapter
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wallet.Redeemed
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.model.wallet.WalletModel
import com.sd.src.stepcounterapp.model.wallet.Wishlist
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.WalletViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_wallet.*
import kotlinx.android.synthetic.main.item_wallet_wish_list.*

class WalletFragment : BaseFragment(),WalletWishListAdapter.PurchaseListener {
    override fun onPurchaseNow(pos: Int) {
        showPopupProgressSpinner(true)
        mViewModel.hitPurchaseApi(RedeemRequest(mDataWishList[pos]._id.toString(),SharedPreferencesManager.getUserId(mContext)))
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
    }

    lateinit var mWishListAdapter: WalletWishListAdapter
    lateinit var mRedeemListAdapter: WalletRedeemListAdapter
    private var mDataWishList: ArrayList<Wishlist> = ArrayList()
    private var mDataReedemList: ArrayList<Redeemed> = ArrayList()
    private lateinit var mViewModel: WalletViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(WalletViewModel::class.java)

        mViewModel.getStepToken().observe(this,
            Observer<TokenModel> { mData ->
                if (mData.data != null) {
                    txtTokens.text = mData.data!!.totalEarntokens.toString()
                    txtSteps.text = mData.data!!.totalSteps.toString()
                }
            })

        mViewModel.getPurchase().observe(this,
            Observer<BasicInfoResponse> { mData ->
                showPopupProgressSpinner(false)
                if(mData!=null){
                    Toast.makeText(mContext,"Product purchased successfully",Toast.LENGTH_LONG).show()
                    mViewModel.hitWalletApi()
                }
            })

        mViewModel.getWalletData().observe(this,
            Observer<WalletModel> { mData ->
                if (mData != null || mData?.data != null) {
                    txtTokens.text = mData.data?.totalGenerated.toString()
                    txtSteps.text = mData.data?.steps.toString()
                    tokensVal.text = mData.data?.totalEarnings.toString()

                    if (mData.data!!.wishlist != null && mData.data?.wishlist!!.size > 0) {
                        mDataWishList = (mData.data?.wishlist as ArrayList<Wishlist>?)!!

                        setWishListAdapter()
                        setWishListView()

                        llWishListSeeLess.visibility = View.VISIBLE
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

                    if (mData.data!!.redeemed != null && mData.data!!.redeemed!!.size > 0) {
                        mDataReedemList = (mData.data?.purchased as ArrayList<Redeemed>?)!!
                        setReedemListAdapter()
                        setRedeemListView()

                        llRedeemSeeLess.visibility = View.VISIBLE
                        txtRedeemSeeAll.visibility = View.VISIBLE
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

        mViewModel.setTokensFromSteps()
        mViewModel.hitWalletApi()

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

        historyView.setOnClickListener{
//            startActivity(Intent(mContext,LandingActivity::class.java))
        }

    }



    private fun setWishListAdapter() {
        rvWishList.layoutManager = LinearLayoutManager(mContext)
        mWishListAdapter = WalletWishListAdapter(mDataWishList,this)
        rvWishList.adapter = mWishListAdapter
    }

    private fun setWishListView() {
//        if (mDataWishList.size in 1..2) {
        if(mDataWishList.isNotEmpty()){
            cdWishSecond.visibility = View.VISIBLE
            cdWishFirst.visibility = View.VISIBLE

            txtProductNameFirst.text = mDataWishList[0].name
            txtShortDescFirst.text = mDataWishList[0].shortDesc
            txtTokenFirst.text = "${mDataWishList[0].token} TKS"
            Picasso.get().load(RetrofitClient.IMG_URL + "" + mDataWishList[0].image).into(imgProductFirst)
            purchaseFirst.setOnClickListener {
                showPopupProgressSpinner(true)
                mViewModel.hitPurchaseApi(RedeemRequest(SharedPreferencesManager.getUserId(mContext),mDataWishList[0]._id.toString()))
            }

            if (mDataWishList.size > 1) {
                cdWishSecond.visibility = View.VISIBLE
                txtProductNameSecond.text = mDataWishList[1].name
                txtShortDescSecond.text = mDataWishList[1].shortDesc
                txtTokenSecond.text = "${mDataWishList[1].token} TKS"
                Picasso.get().load(RetrofitClient.IMG_URL + "" + mDataWishList[1].image).into(imgProductSecond)
                purchaseSecond.setOnClickListener {
                    showPopupProgressSpinner(true)
                    mViewModel.hitPurchaseApi(RedeemRequest(SharedPreferencesManager.getUserId(mContext),mDataWishList[1]._id.toString()))
                }
            } else {
                cdWishSecond.visibility = View.GONE
            }
            /*  } else {
                  cdWishSecond.visibility = View.GONE
                  cdWishFirst.visibility = View.GONE
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
        if (mDataReedemList.size > 0 ) {
            txtProductNameRedeemFirst.text = mDataReedemList[0].name
            txtShortDescRedeemFirst.text = mDataReedemList[0].shortDesc
            txtTokenRedeemFirst.text = "${mDataReedemList[0].token} TKS"
            Picasso.get().load(RetrofitClient.IMG_URL + "" + mDataReedemList[0].image).into(imgProductRedeemFirst)

            if (mDataReedemList.size > 1) {
                cdRedeemSecond.visibility = View.VISIBLE
                txtProductNameRedeemSecond.text = mDataReedemList[1].name
                txtShortDescRedeemSecond.text = mDataReedemList[1].shortDesc
                txtTokenRedeemSecond.text = "${mDataReedemList[1].token} TKS"
                Picasso.get().load(RetrofitClient.IMG_URL + "" + mDataReedemList[1].image).into(imgProductRedeemSecond)
            } else {
                cdRedeemSecond.visibility = View.GONE
            }
        } else {
            cdRedeemSecond.visibility = View.GONE
            cdRedeemFirst.visibility = View.GONE
        }
    }

}
