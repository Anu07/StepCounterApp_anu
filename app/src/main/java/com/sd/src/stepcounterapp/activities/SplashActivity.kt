package com.sd.src.stepcounterapp.activities

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.sd.src.stepcounterapp.AppConstants
import com.sd.src.stepcounterapp.AppConstants.INTENT_NOTIFICATION
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager

class SplashActivity : AppCompatActivity() {

    private lateinit var userObj: LoginResponseJ
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            if(!SharedPreferencesManager.hasKey(this@SplashActivity,"User")){
                val intent = Intent(applicationContext, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                userObj = SharedPreferencesManager.getUserObject(this@SplashActivity)
                if(userObj.data.basicFlag && userObj.data.rewardFlag){
                    if(SharedPreferencesManager.hasKey(this@SplashActivity,"Wearable")){
                        val intent = Intent(applicationContext, LandingActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val intent = Intent(applicationContext, SyncDeviceActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }else if(!userObj.data.basicFlag ){
                    val intent = Intent(applicationContext, BasicInfoActivity::class.java)
                    startActivity(intent)
                    finish()
                }else if(!userObj.data.rewardFlag){
                    val intent = Intent(applicationContext, RewardsCategorySelectionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        StatusBarUtil.setTransparent(this@SplashActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        Log.e("Test intent",""+HayaTechApplication.notificationTitle)
    }

}