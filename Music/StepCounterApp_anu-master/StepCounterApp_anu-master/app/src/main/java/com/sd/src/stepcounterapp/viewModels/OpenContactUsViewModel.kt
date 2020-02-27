package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.contactUs.ContactUsRequest
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenContactUsViewModel(application: Application) : AndroidViewModel(application){

    private var mContactUsResponse: MutableLiveData<BasicInfoResponse>? = null


    val call = RetrofitClient.instance

    fun postContactUsResponse(): MutableLiveData<BasicInfoResponse> {
        if (mContactUsResponse == null) {
            mContactUsResponse = MutableLiveData()
        }
        return mContactUsResponse as MutableLiveData<BasicInfoResponse>
    }




   fun postContactUs(contactUsRequest :ContactUsRequest) {
       if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

           call!!.postcontactus(contactUsRequest).enqueue(object : Callback<BasicInfoResponse> {
               override fun onFailure(call: Call<BasicInfoResponse>, t: Throwable) {
                   Log.v("retrofit", "call failed")
                   Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
               }

               override fun onResponse(call: Call<BasicInfoResponse>, response: Response<BasicInfoResponse>) {
                   when {
                       response.body()!!.status == 200 -> mContactUsResponse!!.value = response.body()
                       response.code() == 405 -> {
                           Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                               .show()
                           HayaTechApplication.instance!!.logoutUser()
                       }
                       else -> mContactUsResponse!!.value = BasicInfoResponse()
                   }

               }

           })
       }

    }
}
