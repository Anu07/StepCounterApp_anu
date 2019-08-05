package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataResponse
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceViewModel(application: Application) : AndroidViewModel(application) {
    private var mResponse: MutableLiveData<BasicInfoResponse>? = null
    private var mDashResponse: MutableLiveData<FetchDeviceDataResponse>? = null
    val call = RetrofitClient.instance

    fun getSyncResponse(): MutableLiveData<BasicInfoResponse> {
        if (mResponse == null) {
            mResponse = MutableLiveData()
        }
        return mResponse as MutableLiveData<BasicInfoResponse>
    }

    fun getDashResponse(): MutableLiveData<FetchDeviceDataResponse> {
        if (mDashResponse == null) {
            mDashResponse = MutableLiveData()
        }
        return mDashResponse as MutableLiveData<FetchDeviceDataResponse>
    }


    fun syncDevice(request: SyncRequest) {
        call!!.syncWeableData(request).enqueue(object : Callback<BasicInfoResponse> {

            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                mResponse!!.value = response!!.body()
            }
        })
    }


    fun fetchSyncData(request: FetchDeviceDataRequest) {
        call!!.getSyncData(request).enqueue(object : Callback<FetchDeviceDataResponse> {

            override fun onFailure(call: Call<FetchDeviceDataResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<FetchDeviceDataResponse>?, response: Response<FetchDeviceDataResponse>?) {
                if(response!!.body() != null) {
                    mDashResponse!!.value = response.body()
                }
            }
        })
    }

}

