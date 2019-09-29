package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.DeviceResponse.DashboardResponse
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceViewModel(application: Application) : AndroidViewModel(application) {
    private var mResponse: MutableLiveData<BasicInfoResponse>? = null
    private var mDashResponse: MutableLiveData<DashboardResponse>? = null
    val call = RetrofitClient.instance

    fun getSyncResponse(): MutableLiveData<BasicInfoResponse> {
        if (mResponse == null) {
            mResponse = MutableLiveData()
        }
        return mResponse as MutableLiveData<BasicInfoResponse>
    }

    fun getDashResponse(): MutableLiveData<DashboardResponse> {
        if (mDashResponse == null) {
            mDashResponse = MutableLiveData()
        }
        return mDashResponse as MutableLiveData<DashboardResponse>
    }


    fun syncDevice(request: SyncRequest) {
        if(!request.activity.isEmpty()){
            if(Utils.retryInternet(HayaTechApplication.applicationContext())){
                call!!.syncWeableData(request).enqueue(object : Callback<BasicInfoResponse> {

                    override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(HayaTechApplication.applicationContext(), "Server Error"+t!!.message, Toast.LENGTH_LONG).show()
//                        if(t.message!!.contains("timeout")){
                            mResponse!!.value = BasicInfoResponse()
//                        }
                    }

                    override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                        if (response!!.code() == 405) {
                            Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        } else if(response!!.code() == 200) {
                            mResponse!!.value = response!!.body()
                        }else {
                            Log.i("Empty","activity array")
                        }
                    }
                })
            }


        }

    }


    fun fetchSyncData(request: FetchDeviceDataRequest) {
        if(Utils.retryInternet(HayaTechApplication.applicationContext())){
            call!!.getSyncData(request).enqueue(object : Callback<DashboardResponse> {
                override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                    mResponse!!.value = BasicInfoResponse()

                }

                override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                    if (response!!.body()!!.status == 405) {
                        Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                            .show()
                        mDashResponse!!.value =DashboardResponse()
                    } else if (response!!.body()!!.status == 200) {
                        mDashResponse!!.value = response.body()
                    }
                }

            })
        }


    }

}

