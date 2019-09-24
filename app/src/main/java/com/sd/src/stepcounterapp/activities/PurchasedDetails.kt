package com.sd.src.stepcounterapp.activities

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.fragments.WalletFragment.Companion.DEALDATA
import com.sd.src.stepcounterapp.model.vendor.VendorRequest
import com.sd.src.stepcounterapp.model.wallet.walletDetailResponse.Purchased
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.PurchasedVendorModel
import kotlinx.android.synthetic.main.activity_purchased_details.*

class PurchasedDetails : BaseActivity<PurchasedVendorModel>() {
    private var dealData: Purchased? = null
    override val layoutId: Int
        get() = R.layout.activity_purchased_details
    override val viewModel: PurchasedVendorModel
        get() = ViewModelProviders.of(this,
            BaseViewModelFactory { PurchasedVendorModel(application) }).get(PurchasedVendorModel::class.java)


    override val context: Context
        get() = this@PurchasedDetails


    override fun onCreate() {
        dealData = intent.getParcelableExtra(DEALDATA)
        if (intent.hasExtra(DEALDATA)) {
            txtCodValue.text = dealData?.invoiceNo
            txtTitle.text = dealData?.name?.capitalize()
            txtvalue.text = dealData?.shortDesc?.capitalize()
        }
        mViewModel?.getVendorResponse()!!.observe(this, Observer { mResponse ->
            if(mResponse.data!=null){
                addressDetails.text = mResponse.data.vendor.address
            }
        })
        mViewModel?.getVendorDetails(VendorRequest(dealData!!.invoiceNo))
    }

    override fun initListeners() {
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}