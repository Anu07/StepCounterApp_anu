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
import com.sd.src.stepcounterapp.model.notificatyionlist.NotificationListResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private var mNotificationResponse: MutableLiveData<NotificationListResponse>?= null
    val call = RetrofitClient.instance

    fun getNotificationResponse(): MutableLiveData<NotificationListResponse> {
        if (mNotificationResponse == null) {
            mNotificationResponse = MutableLiveData()
        }
        return mNotificationResponse as MutableLiveData<NotificationListResponse>
    }

    fun getNotifications() {
        call!!.getNotificationList(BasicRequest(SharedPreferencesManager.getUserId(AppApplication.applicationContext()))).enqueue(object : Callback<NotificationListResponse> {

            override fun onFailure(call: Call<NotificationListResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<NotificationListResponse>?, response: Response<NotificationListResponse>?) {
                if(response!!.body()!!.status == 200 ){
                    mNotificationResponse!!.value = response!!.body()
                }else {
                    mNotificationResponse!!.value = NotificationListResponse()
                }
            }
        })
    }



}
