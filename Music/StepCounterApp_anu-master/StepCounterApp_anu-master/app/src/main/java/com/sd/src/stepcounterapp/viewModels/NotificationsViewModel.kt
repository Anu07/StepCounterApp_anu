package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.notification.BasicRequest
import com.sd.src.stepcounterapp.model.notification.readnotification.NotificationReadRequest
import com.sd.src.stepcounterapp.model.notificatyionlist.NotificationListResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private var mNotificationResponse: MutableLiveData<NotificationListResponse>? = null
    val call = RetrofitClient.instance
    private var mReadNotificationResponse: MutableLiveData<BasicInfoResponse>? = null
    private var mReadAllNotificationResponse: MutableLiveData<BasicInfoResponse>? = null

    fun getAllNotificationResponse(): MutableLiveData<BasicInfoResponse> {
        if (mReadAllNotificationResponse == null) {
            mReadAllNotificationResponse = MutableLiveData()
        }
        return mReadAllNotificationResponse as MutableLiveData<BasicInfoResponse>
    }


    fun getNotificationResponse(): MutableLiveData<NotificationListResponse> {
        if (mNotificationResponse == null) {
            mNotificationResponse = MutableLiveData()
        }
        return mNotificationResponse as MutableLiveData<NotificationListResponse>
    }

    fun getReadNotificationResponse(): MutableLiveData<BasicInfoResponse> {
        if (mReadNotificationResponse == null) {
            mReadNotificationResponse = MutableLiveData()
        }
        return mReadNotificationResponse as MutableLiveData<BasicInfoResponse>
    }

    fun getNotifications() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.getNotificationList(
                BasicRequest(
                    SharedPreferencesManager.getUserId(
                        HayaTechApplication.applicationContext()
                    )
                )
            ).enqueue(object : Callback<NotificationListResponse> {

                override fun onFailure(call: Call<NotificationListResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "Server error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<NotificationListResponse>?,
                    response: Response<NotificationListResponse>?
                ) {
                    if (response!!.body()!!.status == 200) {
                        mNotificationResponse!!.value = response.body()
                    } else if (response.code() == 405) {
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "User doesn't exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else {
                        mNotificationResponse!!.value = NotificationListResponse()
                    }
                }
            })
        }
    }


    fun readAllNotifications() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            SharedPreferencesManager.getUserId(
                HayaTechApplication.applicationContext()
            )?.let {
                call!!.readAllNotifications(
                    it
                ).enqueue(object : Callback<BasicInfoResponse> {

                    override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            ""+t!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<BasicInfoResponse>?,
                        response: Response<BasicInfoResponse>?
                    ) {
                        when {
                            response!!.body()!!.status == 200 -> mReadAllNotificationResponse!!.value = response.body()
                            response.code() == 405 -> {
                                Toast.makeText(
                                    HayaTechApplication.applicationContext(),
                                    "User doesn't exist",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                HayaTechApplication.instance!!.logoutUser()
                            }
                            else -> mNotificationResponse!!.value = NotificationListResponse()
                        }
                    }
                })
            }
        }
    }


    fun readNotification(_id: String) {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.readNotification(NotificationReadRequest(_id))
                .enqueue(object : Callback<BasicInfoResponse> {

                    override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "Server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<BasicInfoResponse>?,
                        response: Response<BasicInfoResponse>?
                    ) {
                        if (response!!.body()!!.status == 200) {
                            mReadNotificationResponse!!.value = response.body()
                        } else if (response.code() == 405) {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        } else {
                            mReadNotificationResponse!!.value = BasicInfoResponse()
                        }
                    }
                })
        }


    }


}
