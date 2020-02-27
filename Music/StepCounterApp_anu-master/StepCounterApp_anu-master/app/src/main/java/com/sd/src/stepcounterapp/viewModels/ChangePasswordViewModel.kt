package com.sd.src.stepcounterapp.fragments

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.challenge.ChallengeResponse
import com.sd.src.stepcounterapp.model.changepwd.ChangePasswordRequest
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel : ViewModel() {

    val call = RetrofitClient.instance
    private var changePasswordResponse: MutableLiveData<BasicInfoResponse>? = null

    fun getChangePasswordResponse(): MutableLiveData<BasicInfoResponse> {
        if (changePasswordResponse == null) {
            changePasswordResponse = MutableLiveData()
        }
        return changePasswordResponse as MutableLiveData<BasicInfoResponse>
    }

    fun changePassword(userId: String?, oldPwd: String, newPwd: String) {
        if(Utils.retryInternet(HayaTechApplication.applicationContext())){
            call!!.change_password(ChangePasswordRequest(userId!!, oldPwd, newPwd)).enqueue(object :
                Callback<BasicInfoResponse> {
                override fun onFailure(call: Call<BasicInfoResponse>, t: Throwable) {
                    Log.v("retrofit", "call failed$t")
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "Server error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<BasicInfoResponse>, response: Response<BasicInfoResponse>) {
                    if (response.code() == 405) {
                        Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    }else if (response.code() == 400) {
                        Toast.makeText(HayaTechApplication.applicationContext(),"Old password not matched" , Toast.LENGTH_LONG)
                            .show()
                        changePasswordResponse?.value = BasicInfoResponse()
                    }else if (response.body()!!.status == 200) {
                        changePasswordResponse?.value = response.body()
                    }
                }

            })
        }


    }
}
