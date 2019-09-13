package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
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
        call!!.notificationSettings(BasicRequest(SharedPreferencesManager.getUserId(AppApplication.applicationContext()))).enqueue(object : Callback<NotificationResponse> {

            override fun onFailure(call: Call<NotificationResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<NotificationResponse>?, response: Response<NotificationResponse>?) {
                if(response!!.body()!!.status == 200 ){
                    mSettingsResponse!!.value = response!!.body()
                }else {
                    mSettingsResponse!!.value = NotificationResponse()
                }
            }
        })
    }



}
