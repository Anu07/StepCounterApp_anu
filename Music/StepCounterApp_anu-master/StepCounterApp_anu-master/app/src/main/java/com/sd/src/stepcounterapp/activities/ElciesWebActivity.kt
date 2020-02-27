package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.fragment_faq.*


class ElciesWebActivity : BaseActivity<BaseViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_elcies
    override val viewModel: BaseViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { BaseViewModel(application) }).get(BaseViewModel::class.java)
    override val context: Context
        get() = this@ElciesWebActivity
    val USER_AGENT_FAKE =
        "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19"
    override fun onCreate() {
        var user = SharedPreferencesManager.getUserObject(this@ElciesWebActivity)
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(user.data.elciesCallbackurl)
        webView.settings.userAgentString = USER_AGENT_FAKE
        img_back.setOnClickListener {
            openSyncDeviceActivity()
        }
    }

    private fun openSyncDeviceActivity() {
        var intent = Intent(this@ElciesWebActivity, SyncDeviceActivity::class.java)
        intent.putExtra("disconnect", true)
        intent.putExtra("elcies", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    override fun initListeners() {

    }

    override fun onBackPressed() {
        openSyncDeviceActivity()
    }

}