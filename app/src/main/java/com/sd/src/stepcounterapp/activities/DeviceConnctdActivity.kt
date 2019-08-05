package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sdi.sdeiarchitecturemvvm.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_synchronized_device.*

class DeviceConnctdActivity : BaseActivity<BaseViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_synchronized_device
    override
    val viewModel: BaseViewModel
        get() =  ViewModelProviders.of(
            this,
            BaseViewModelFactory { BaseViewModel(application) }).get(BaseViewModel::class.java)
    override val context: Context
        get() = this@DeviceConnctdActivity
    override fun onCreate() {

        gotto.setOnClickListener {
            val intent = Intent(this@DeviceConnctdActivity, LandingActivity::class.java)
            //                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
            intent.putExtra("Steps", getIntent().getStringExtra("Steps"))
            startActivity(intent)
        }

    }

    override fun initListeners() {
    }

}