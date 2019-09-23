package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.notification.NotificationResponse
import com.sd.src.stepcounterapp.model.vendor.PurchaseVendorResponse
import com.sd.src.stepcounterapp.model.vendor.VendorRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PurchasedVendorModel(application: Application) : AndroidViewModel(application){
    private var mPurchaseVendorResponse: MutableLiveData<PurchaseVendorResponse>?= null
    val call = RetrofitClient.instance

    fun getVendorResponse(): MutableLiveData<PurchaseVendorResponse> {
        if (mPurchaseVendorResponse == null) {
            mPurchaseVendorResponse = MutableLiveData()
        }
        return mPurchaseVendorResponse as MutableLiveData<PurchaseVendorResponse>
    }

    fun getVendorDetails(request:VendorRequest) {
        call!!.getVendorDetails(request).enqueue(object :
            Callback<PurchaseVendorResponse> {

            override fun onFailure(call: Call<PurchaseVendorResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<PurchaseVendorResponse>?, response: Response<PurchaseVendorResponse>?) {
                if(response!!.body()!!.status == 200 ){
                    mPurchaseVendorResponse!!.value = response!!.body()
                }else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                }else {
                    mPurchaseVendorResponse!!.value = PurchaseVendorResponse()
                }
            }
        })
    }


}