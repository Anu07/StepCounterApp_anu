package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
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
import com.sd.src.stepcounterapp.adapter.WalletRedeemListAdapter
import com.sd.src.stepcounterapp.adapter.WalletWishListAdapter
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.viewModels.WalletViewModel
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment : Fragment() {

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
    private var mData: ArrayList<String> = ArrayList()
    private lateinit var mViewModel: WalletViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(WalletViewModel::class.java)

        mViewModel.getStepToken().observe(this,
            Observer<TokenModel> { mData ->
                if (mData.data != null) {
                    txtTokens.text = mData.data!!.totalEarntokens.toString()
                } else {
                    Toast.makeText(mContext, mData.message, Toast.LENGTH_SHORT).show()
                }
            })

        setWishListAdapter()
        setReedemListAdapter()
        mViewModel.setTokensFromSteps()

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
    }

    private fun setWishListAdapter() {
        rvWishList.layoutManager = LinearLayoutManager(mContext)
        mWishListAdapter = WalletWishListAdapter(mData)
        rvWishList.adapter = mWishListAdapter
    }

    private fun setReedemListAdapter() {
        rvRedeem.layoutManager = LinearLayoutManager(mContext)
        mRedeemListAdapter = WalletRedeemListAdapter(mData)
        rvRedeem.adapter = mRedeemListAdapter
    }

}
