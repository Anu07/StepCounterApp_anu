package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.SignInViewModel
import com.sdi.sdeiarchitecturemvvm.activities.BaseActivity
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity<SignInViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_forgot_password

    override val viewModel: SignInViewModel
        get() = ViewModelProviders.of(this,
            BaseViewModelFactory { SignInViewModel(application) }).get(SignInViewModel::class.java)

    override val context: Context
        get() = this@ForgotPasswordActivity

    override fun onCreate() {
        mViewModel!!.getBasicResponse().observe(this,
            Observer<BasicInfoResponse> { mBase ->
                if(mBase.status==200){
                    Toast.makeText(this@ForgotPasswordActivity,mBase.message,Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@ForgotPasswordActivity,"No Email Record found",Toast.LENGTH_LONG).show()
                }
                showPopupProgressSpinner(false)
            })
    }

    override fun initListeners() {
        resetBttn.setOnClickListener {
            if (validate()) {
                showPopupProgressSpinner(true)
                mViewModel!!.forgetPassword(
                    LoginRequestObject(
                        "",
                        "",
                        emailTxt.text.toString(),
                        ""
                    )
                )
            }
        }
    }

    private fun validate(): Boolean {
        var emailStr: String = emailTxt.text.toString()
        if (!emailTxt.nonEmpty() || !emailStr.validEmail()) {
            Toast.makeText(this@ForgotPasswordActivity, resources.getString(R.string.email_error), Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }


}