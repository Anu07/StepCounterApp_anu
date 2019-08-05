package com.sd.src.stepcounterapp.activities

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.provider.Settings.Secure
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.SignInViewModel
import com.sdi.sdeiarchitecturemvvm.activities.BaseActivity
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_signin.*


class SignInActivity : BaseActivity<SignInViewModel>() {
    private var android_id : String= ""
    override val layoutId: Int
        get() = R.layout.activity_signin
    override val viewModel: SignInViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { SignInViewModel(application) }).get(SignInViewModel::class.java)
    override val context: Context
        get() = this@SignInActivity

    override fun onCreate() {

        mViewModel!!.getUser().observe(this,
            Observer<LoginResponseJ> { loginUser ->
                showPopupProgressSpinner(false)
                if(loginUser.data!=null){
                    SharedPreferencesManager.setUserId(this@SignInActivity,loginUser.data!!._id)
                   if(loginUser.data.username.isNotEmpty()){
                       SharedPreferencesManager.saveUserObject(this@SignInActivity,loginUser)
                       if(loginUser.data.basicFlag && loginUser.data.rewardFlag){
                           val intent = Intent(mContext, LandingActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                           startActivity(intent)
                           finish()
                       }else if(!loginUser.data.basicFlag){
                           val intent = Intent(applicationContext, BasicInfoActivity::class.java)
                           startActivity(intent)
                           finish()
                       }else if(!loginUser.data.rewardFlag){
                           val intent = Intent(applicationContext, RewardsCategorySelectionActivity::class.java)
                           startActivity(intent)
                           finish()
                       }

                   }else{
                       val intent = Intent(mContext, BasicInfoActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                       startActivity(intent)
                       finish()
                   }
                }else{
                    Toast.makeText(this@SignInActivity,loginUser.message,Toast.LENGTH_LONG).show()

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
            android_id= Secure.getString(
                this@SignInActivity.contentResolver,
                Secure.ANDROID_ID
            )
            Log.i("test Id",android_id)

            if (validate()) {
                if (AppApplication.hasNetwork()) {
                    showPopupProgressSpinner(true)
                    mViewModel!!.setLoginData(
                        LoginRequestObject(
                            android_id,
                            "Android",
                            emailTxt.text.toString(),
                            pwdTxt.text.toString()
                        )
                    )
                } else {
                    Toast.makeText(this@SignInActivity, "Internet connection unavailable", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (!emailTxt.nonEmpty() || !emailTxt.text.toString().validEmail()) {
            Toast.makeText(this@SignInActivity, resources.getString(R.string.email_error), Toast.LENGTH_LONG).show()
            return false
        } else if (!pwdTxt.nonEmpty()) {
            Toast.makeText(this@SignInActivity, resources.getString(R.string.pwd_error), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

}