package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.challenge.MyChallengeResponse
import com.sd.src.stepcounterapp.model.contactUs.ContactUsRequest
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.profile.ProfileResponse
import com.sd.src.stepcounterapp.model.profile.UpdateProfileRequest
import com.sd.src.stepcounterapp.model.rewards.MyRedeemedResponse
import com.sd.src.stepcounterapp.model.updateresponse.UpdateProfileResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
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




   /* fun postContactUs() {
        call!!.postcontactus(ContactUsRequest(SharedPreferencesManager.getUserId(AppApplication.applicationContext()).toString())).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>, t: Throwable) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>, response: Response<BasicInfoResponse>) {
                mContactUsResponse!!.value = response.body()
            }

        })
    }
*/

}
