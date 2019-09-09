package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.change_password_fragment.*

class ChangePasswordFragment : BaseFragment() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: ChangePasswordFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): ChangePasswordFragment {
            instance = ChangePasswordFragment()
            mContext = context
            return instance
        }
    }

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.change_password_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)

        changePassword.setOnClickListener {
            if (old_password.text.toString().trim().isNotEmpty() && newPwdTxt.text.toString().trim().isNotEmpty() && conNewPwdText.text.toString().trim().length != 0) {
                if (newPwdTxt.text.toString().trim().equals(conNewPwdText.text.toString().trim())) {
                    viewModel.changePassword(
                        SharedPreferencesManager.getUserId(AppApplication.applicationContext()),
                        old_password.text.toString().trim(),
                        newPwdTxt.text.toString().trim()
                    )
                } else {
                    Toast.makeText(
                        activity,
                        "Confirm password dosn't match with new password",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(activity, "Please fill all information", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getChangePasswordResponse().observe(this, androidx.lifecycle.Observer { mData ->
            if (mData.status == 200) {
                Toast.makeText(activity, "Password Changed", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    activity,
                    mData.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

}
