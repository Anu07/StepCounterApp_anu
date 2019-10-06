package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.vendor.VendorDetailResponse
import com.sd.src.stepcounterapp.model.vendor.VendorRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PurchasedVendorModel(application: Application) : AndroidViewModel(application) {
    private var mPurchaseVendorResponse: MutableLiveData<VendorDetailResponse>? = null
    val call = RetrofitClient.instance

    fun getVendorResponse(): MutableLiveData<VendorDetailResponse> {
        if (mPurchaseVendorResponse == null) {
            mPurchaseVendorResponse = MutableLiveData()
        }
        return mPurchaseVendorResponse as MutableLiveData<VendorDetailResponse>
    }

    fun getVendorDetails(request: VendorRequest) {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            call!!.getVendorDetails(request).enqueue(object :
                Callback<VendorDetailResponse> {

                override fun onFailure(call: Call<VendorDetailResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "Server error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<VendorDetailResponse>?,
                    response: Response<VendorDetailResponse>?
                ) {
                    when {
                        response!!.body()!!.getmStatus() == 200 -> mPurchaseVendorResponse!!.value =
                            response!!.body()
                        response!!.code() == 405 -> {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        }
                        response!!.code() == 400 -> mPurchaseVendorResponse!!.value =
                            VendorDetailResponse()
                        else -> mPurchaseVendorResponse!!.value = VendorDetailResponse()
                    }
                }

            })
        }
    }


}