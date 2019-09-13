package com.sd.src.stepcounterapp.activities

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.fragments.WalletFragment.Companion.DEALDATA
import com.sd.src.stepcounterapp.model.wallet.walletDetailResponse.Purchased
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import kotlinx.android.synthetic.main.activity_purchased_details.*

class PurchasedDetails : BaseActivity<BaseViewModel>() {
    private var dealData: Purchased? = null
    override val layoutId: Int
        get() = R.layout.activity_purchased_details
    override val viewModel: BaseViewModel
        get() = ViewModelProviders.of(this,
            BaseViewModelFactory { BaseViewModel(application) }).get(BaseViewModel::class.java)

    override val context: Context
        get() = this@PurchasedDetails


    override fun onCreate() {
        dealData = intent.getParcelableExtra(DEALDATA)
        if(intent.hasExtra(DEALDATA)){
            txtCodValue.text = dealData?.rewardId
            txtTitle.text = dealData?.name?.capitalize()
            txtvalue.text = dealData?.shortDesc?.capitalize()
        }
    }

    override fun initListeners() {
    }

}