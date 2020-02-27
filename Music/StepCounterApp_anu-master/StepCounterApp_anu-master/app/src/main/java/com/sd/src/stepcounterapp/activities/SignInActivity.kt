package com.sd.src.stepcounterapp.activities

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.FIREBASETOKEN
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_ELCTOKEN
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_USEREXISTING
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.SignInViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_signin.*
import java.util.*


class SignInActivity : BaseActivity<SignInViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_signin
    override val viewModel: SignInViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { SignInViewModel(application) }).get(SignInViewModel::class.java)
    override val context: Context
        get() = this@SignInActivity

    override fun onCreate() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(HayaTechApplication.TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                Log.e("Token",""+token)
                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(HayaTechApplication.TAG, token)
                if (token != null) {
                    SharedPreferencesManager.setString(HayaTechApplication.applicationContext(),token, FIREBASETOKEN)
                }
//                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
        mViewModel!!.getUser().observe(this,
            Observer<LoginResponseJ> { loginUser ->
                showPopupProgressSpinner(false)
                if (loginUser.data != null) {
                    SharedPreferencesManager.setBoolean(this@SignInActivity, true,VAR_USEREXISTING)
                    SharedPreferencesManager.setUserId(this@SignInActivity, loginUser.data!!._id)
                    SharedPreferencesManager.saveUserObject(this@SignInActivity, loginUser)
                    SharedPreferencesManager.setString(this@SignInActivity, loginUser.token,VAR_ELCTOKEN)
                    if (loginUser.data.username.isNotEmpty()) {
                        if (loginUser.data.basicFlag && loginUser.data.rewardFlag) {
                            when {
                                SharedPreferencesManager.hasKey(this@SignInActivity,
                                    SharedPreferencesManager.VAR_WEARABLE
                                ) -> {
                                    val intent = Intent(applicationContext, LandingActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                SharedPreferencesManager.hasKey(this@SignInActivity,
                                    VAR_USEREXISTING
                                ) -> {
                                    val intent = Intent(applicationContext, LandingActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                                else -> {
                                    val intent = Intent(applicationContext, SyncDeviceActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else if (!loginUser.data.basicFlag) {
                            val intent = Intent(applicationContext, BasicInfoActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else if (!loginUser.data.rewardFlag) {
                            val intent = Intent(applicationContext, RewardsCategorySelectionActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    } else {
                        val intent = Intent(mContext, BasicInfoActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this@SignInActivity, loginUser.message, Toast.LENGTH_LONG).show()

                }
            })
    }

    override fun initListeners() {
        forgotTextView.setOnClickListener {
            val intent = Intent(mContext, ForgotPasswordActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())
        }

        signBttn.setOnClickListener {
            if (validate()) {
                if (HayaTechApplication.hasNetwork()) {
                    showPopupProgressSpinner(true)
                    var uVal:Int = parseTimeOffset().toInt()
                    mViewModel!!.setLoginData(
                        LoginRequestObject(
                            SharedPreferencesManager.getString(this@SignInActivity, FIREBASETOKEN),
                            "Android",
                            emailTxt.text.toString().toLowerCase(),
                            pwdTxt.text.toString(),
                            uVal
                        )
                    )
                } else {
                    Toast.makeText(this@SignInActivity, "Internet connection unavailable", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    fun parseTimeOffset(): String {

        val tz = TimeZone.getDefault()
        val cal = Calendar.getInstance(tz)
        val offsetInMillis = tz.getOffset(cal.timeInMillis)

        var offset =
            String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs(offsetInMillis / 60000 % 60))
        var minutesOffset = ((offset.split(":")[0].toInt()*60) + offset.split(":")[1].toInt())
        offset = (if (offsetInMillis >= 0) "+" else "-") + minutesOffset
        return offset
    }



    private fun validate(): Boolean {
        if (!emailTxt.nonEmpty() ) {
            Toast.makeText(this@SignInActivity, resources.getString(R.string.email_empty), Toast.LENGTH_LONG)
                .show()
            return false
        }else if(!emailTxt.text.toString().validEmail()){
            Toast.makeText(this@SignInActivity, resources.getString(R.string.email_error), Toast.LENGTH_LONG)
                .show()
            return false
        } else if (!pwdTxt.nonEmpty()) {
            Toast.makeText(this@SignInActivity, resources.getString(R.string.pwd_error), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

}