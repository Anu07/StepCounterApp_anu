package com.sd.src.stepcounterapp.fragments

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
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

    fun changePassword(userId: String?, trim: String, trim1: String) {
        call!!.change_password(userId!!, trim, trim1).enqueue(object :
            Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>, t: Throwable) {
                Log.v("retrofit", "call failed" + t)
                Toast.makeText(
                    AppApplication.applicationContext(),
                    "Server error",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>, response: Response<BasicInfoResponse>) {
                changePasswordResponse?.value = response.body()
            }

        })
    }
}
