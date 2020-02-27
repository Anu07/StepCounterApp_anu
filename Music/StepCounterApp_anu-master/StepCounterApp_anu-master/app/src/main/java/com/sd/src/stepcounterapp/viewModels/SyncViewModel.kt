package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.refreshToken.RefreshTokenResponse
import com.sd.src.stepcounterapp.model.syncDevice.elcies.WidgetBody
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.Utils
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SyncViewModel(application: Application) : AndroidViewModel(application) {

    private var mDeviceList: MutableLiveData<JSONArray>? = null
    private var mToken: MutableLiveData<RefreshTokenResponse>? = null
    private var mWidget: MutableLiveData<BasicInfoResponse>? = null
    val call = RetrofitClient.baseClient


    fun getToken(): MutableLiveData<RefreshTokenResponse> {
        if (mToken == null) {
            mToken = MutableLiveData()
        }
        return mToken as MutableLiveData<RefreshTokenResponse>
    }


    fun getWidgetResponse(): MutableLiveData<BasicInfoResponse> {
        if (mWidget == null) {
            mWidget = MutableLiveData()
        }
        return mWidget as MutableLiveData<BasicInfoResponse>
    }

    fun getListOfDevices(): MutableLiveData<JSONArray> {
        if (mDeviceList == null) {
            mDeviceList = MutableLiveData()
        }
        return mDeviceList as MutableLiveData<JSONArray>
    }

    fun getConnectedDeviceList() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            val call = RetrofitClient.baseClient!!.getConnectedDevices(
                SharedPreferencesManager.getUserObject(HayaTechApplication.applicationContext()).data.elciesUserId
            )
            call.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

//                    {"fault":{"faultstring":"Invalid Access Token","detail":{"errorcode":"keymanagement.service.invalid_access_token"}}}
                    if (response.body() != null) {
                   /*     val jsonArr = JSONArray(response.body().toString())
                        val jsonObject: JSONObject = jsonArr.get(jsonArr.length()-1) as JSONObject
                        var name = jsonObject.get("fault")
                        var status = jsonObject.get("detail")
                        Log.i("success$name", "notes$status")*/
                        if (response.code() == 200) {
                            var msg = Gson().toJson(response.body())
                            when {
                                msg.equals("{\"error\":\"user not exist\"}") -> {
                                    mDeviceList!!.value = JSONArray()
                                }
                                msg.contains("error") -> {
                                    mDeviceList!!.value = JSONArray().put(JSONObject().put("status",false))
                                }
                                else -> {
                                    val jsonArr = JSONArray(response.body().toString())
                                    mDeviceList!!.value = jsonArr
                                }
                            }
//                            {"error":"user not exist"}
                        }
                    }else if (response.code() == 401) {
                        Log.i("failed", "notes" + response.errorBody())
                        mDeviceList!!.value = JSONArray().put(JSONObject().put("status",false))
//                        refreshToken()
//                        Toast.makeText(HayaTechApplication.applicationContext(),""+response.errorBody(),Toast.LENGTH_LONG ).show()
                    } else if (response.code() == 503) {
                        Log.i("failed", "notes" + response.errorBody())
                        mDeviceList!!.value = JSONArray().put(JSONObject().put("status",false))
//                        refreshToken()
//                        Toast.makeText(HayaTechApplication.applicationContext(),""+response.errorBody(),Toast.LENGTH_LONG ).show()
                    }

                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                }
            })
        }
    }


    fun refreshToken() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            RetrofitClient.instance!!.refreshToken().enqueue(object :
                Callback<RefreshTokenResponse> {
                override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                    Log.i("failure token", "notes")

                }

                override fun onResponse(
                    call: Call<RefreshTokenResponse>,
                    response: Response<RefreshTokenResponse>
                ) {
                    if(response.code() == 200){
                        Log.i("success token", "notes")
                        mToken!!.value = response.body()!!
                    }else{
                        var refreshResponse = RefreshTokenResponse()
                        refreshResponse.message = "Bad Request"
                        mToken!!.value = refreshResponse
                    }

                }
            })
        }
    }

    fun updateWidgetInfo(widgetRequest : WidgetBody){
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            RetrofitClient.instance!!.updateWidgetInfo(widgetRequest).enqueue(object :
                Callback<BasicInfoResponse> {
                override fun onFailure(call: Call<BasicInfoResponse>, t: Throwable) {
                    Log.i("failure token", "notes")
                    var bResponse = BasicInfoResponse()
                    bResponse.status = 400
                    mWidget!!.value = bResponse
                }

                override fun onResponse(
                    call: Call<BasicInfoResponse>,
                    response: Response<BasicInfoResponse>
                ) {
                    if(response.code()==200){
                        mWidget!!.value = response.body()
                    }else if(response.code() == 400){
                        var bResponse = BasicInfoResponse()
                        bResponse.status = 400
                        mWidget!!.value = bResponse
                    }
                    Log.i("success token", "notes")
                }
            })
        }
    }


}