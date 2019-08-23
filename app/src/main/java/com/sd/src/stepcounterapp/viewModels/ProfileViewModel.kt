package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.DeviceResponse.DashboardResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.profile.ProfileResponse
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application){
    private var mProfileResponse: MutableLiveData<ProfileResponse>? = null
    val call = RetrofitClient.instance

    fun getProfileResponse(): MutableLiveData<ProfileResponse> {
        if (mProfileResponse == null) {
            mProfileResponse = MutableLiveData()
        }
        return mProfileResponse as MutableLiveData<ProfileResponse>
    }

    fun getProfileData() {
        call!!.getProfileData(BasicRequest(SharedPreferencesManager.getUserId(AppApplication.applicationContext()))).enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                mProfileResponse!!.value = response.body()
            }

        })
    }

}