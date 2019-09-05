package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.DeviceResponse.DashboardResponse
import com.sd.src.stepcounterapp.model.challenge.MyChallengeResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.profile.ProfileResponse
import com.sd.src.stepcounterapp.model.profile.UpdateProfileRequest
import com.sd.src.stepcounterapp.model.rewards.MyRedeemedResponse
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.updateresponse.UpdateProfileResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application){
    private var mProfileResponse: MutableLiveData<ProfileResponse>? = null
    private var mUpdateResponse: MutableLiveData<UpdateProfileResponse>? = null
    private var mMyChallengeResponse: MutableLiveData<MyChallengeResponse>? = null
    private var mMyRedeemedResponse: MutableLiveData<MyRedeemedResponse>? = null


    val call = RetrofitClient.instance

    fun getProfileResponse(): MutableLiveData<ProfileResponse> {
        if (mProfileResponse == null) {
            mProfileResponse = MutableLiveData()
        }
        return mProfileResponse as MutableLiveData<ProfileResponse>
    }


    fun getUpdateProfileResponse(): MutableLiveData<UpdateProfileResponse> {
        if (mUpdateResponse == null) {
            mUpdateResponse = MutableLiveData()
        }
        return mUpdateResponse as MutableLiveData<UpdateProfileResponse>
    }

    fun getMyChallengeResponse(): MutableLiveData<MyChallengeResponse> {
        if (mMyChallengeResponse == null) {
            mMyChallengeResponse = MutableLiveData()
        }
        return mMyChallengeResponse as MutableLiveData<MyChallengeResponse>
    }

    fun getMyRedeemedResponse(): MutableLiveData<MyRedeemedResponse> {
        if (mMyRedeemedResponse == null) {
            mMyRedeemedResponse = MutableLiveData()
        }
        return mMyRedeemedResponse as MutableLiveData<MyRedeemedResponse>
    }


    fun getProfileData() {
        call!!.getProfileData(BasicRequest(SharedPreferencesManager.getUserId(AppApplication.applicationContext()))).enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                mProfileResponse!!.value = response.body()
            }

        })
    }

    fun updateProfileData(request:UpdateProfileRequest) {
        call!!.update_profile(request).enqueue(object : Callback<UpdateProfileResponse> {
            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                mUpdateResponse!!.value = response.body()
            }

        })
    }


    fun getMyChallenge() {
        call!!.getMyChallenges(BasicRequest(SharedPreferencesManager.getUserId(AppApplication.applicationContext()))).enqueue(object :
            Callback<MyChallengeResponse> {
            override fun onFailure(call: Call<MyChallengeResponse>, t: Throwable) {
                Log.v("retrofit", "call failed" + t)
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MyChallengeResponse>, response: Response<MyChallengeResponse>) {
                mMyChallengeResponse!!.value = response.body()
            }

        })
    }


    fun getRedeemRewards() {
        call!!.getMyRedeemedRewards(BasicRequest(SharedPreferencesManager.getUserId(AppApplication.applicationContext()))).enqueue(object :
            Callback<MyRedeemedResponse> {
            override fun onFailure(call: Call<MyRedeemedResponse>, t: Throwable) {
                Log.v("retrofit", "call failed" + t)
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MyRedeemedResponse>, response: Response<MyRedeemedResponse>) {
                mMyRedeemedResponse!!.value = response.body()
            }

        })
    }


}