package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.black_crosstitlebar.*
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_title.setImageResource(R.drawable.changepwdheader)
        img_back.setOnClickListener {
            (HayatechFragment.mContext as LandingActivity).hideBottomLayout(true)
            fragmentManager!!.popBackStackImmediate()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)

        changePassword.setOnClickListener {
            if (old_password.text.toString().trim().isNotEmpty() && newPwdTxt.text.toString().trim().isNotEmpty() && conNewPwdText.text.toString().trim().length != 0) {
                if (newPwdTxt.text.toString().trim() == conNewPwdText.text.toString().trim()) {
                    showPopupProgressSpinner(true)
                    viewModel.changePassword(
                        SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                        old_password.text.toString().trim(),
                        newPwdTxt.text.toString().trim()
                    )
                } else {
                    Toast.makeText(
                        activity,
                        "Confirm password doesn't match with the new password",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(activity, "Please fill all the information", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getChangePasswordResponse().observe(this, androidx.lifecycle.Observer { mData ->
            showPopupProgressSpinner(false)
            if (mData.status == 200) {
                Toast.makeText(activity, "Password has been changed successfully.", Toast.LENGTH_LONG).show()
                fragmentManager!!.popBackStackImmediate()
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
