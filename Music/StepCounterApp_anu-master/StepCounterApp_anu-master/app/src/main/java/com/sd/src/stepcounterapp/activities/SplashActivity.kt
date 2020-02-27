package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_USEREXISTING
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_USEROBJECT
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory

class SplashActivity : BaseActivity<BaseViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val viewModel: BaseViewModel
        get() = ViewModelProviders.of(this,
            BaseViewModelFactory { BaseViewModel(application) }).get(BaseViewModel::class.java)
    override val context: Context
        get() = this@SplashActivity

    override fun onCreate() {
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)


    }

    override fun initListeners() {
    }

    private lateinit var userObj: LoginResponseJ
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            if (Utils.retryInternet(this@SplashActivity)) {
                if (!SharedPreferencesManager.hasKey(this@SplashActivity, VAR_USEREXISTING)) {
                    val intent = Intent(applicationContext, OnboardingActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    if(SharedPreferencesManager.hasKey(this@SplashActivity, VAR_USEROBJECT)){
                        userObj = SharedPreferencesManager.getUserObject(this@SplashActivity)
                        if (userObj.data.basicFlag && userObj.data.rewardFlag) {
//                    if(SharedPreferencesManager.hasKey(this@SplashActivity,"Wearable")){
                            val intent = Intent(applicationContext, LandingActivity::class.java)
                            startActivity(intent)
                            finish()
                            /* }else{
                                 val intent = Intent(applicationContext, SyncDeviceActivity__::class.java)
                                 startActivity(intent)
                                 finish()
                             }
         */
                        } else if (!userObj.data.basicFlag) {
                            val intent = Intent(applicationContext, BasicInfoActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else if (!userObj.data.rewardFlag) {
                            val intent =
                                Intent(applicationContext, RewardsCategorySelectionActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }else{
                        val intent =
                            Intent(applicationContext, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            } else {
                Toast.makeText(
                    this@SplashActivity,
                    "Internet connection unavailable",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    public override fun onDestroy() {
        super.onDestroy()
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

    }


}