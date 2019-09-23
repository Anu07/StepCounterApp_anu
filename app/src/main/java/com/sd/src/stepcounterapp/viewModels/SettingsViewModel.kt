package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.notification.NotificationResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private var mSettingsResponse: MutableLiveData<NotificationResponse>?= null
    val call = RetrofitClient.instance

    fun getSettingsResponse(): MutableLiveData<NotificationResponse> {
        if (mSettingsResponse == null) {
            mSettingsResponse = MutableLiveData()
        }
        return mSettingsResponse as MutableLiveData<NotificationResponse>
    }

    fun updateSettings() {
        call!!.notificationSettings(BasicRequest(SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()))).enqueue(object : Callback<NotificationResponse> {

            override fun onFailure(call: Call<NotificationResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<NotificationResponse>?, response: Response<NotificationResponse>?) {
                if(response!!.body()!!.status == 200 ){
                    mSettingsResponse!!.value = response!!.body()
                }else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                }else {
                    mSettingsResponse!!.value = NotificationResponse()
                }
            }
        })
    }



}
