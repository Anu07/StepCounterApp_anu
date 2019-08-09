package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.fragments.ChallengesFragment
import com.sd.src.stepcounterapp.model.BaseModel
import com.sd.src.stepcounterapp.model.challenge.ChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    val call = RetrofitClient.instance
    private var mChallengeProduct: MutableLiveData<ChallengeResponse>? = null

    fun getChallengeObject(): MutableLiveData<ChallengeResponse> {
        if (mChallengeProduct == null) {
            mChallengeProduct = MutableLiveData()
        }
        return mChallengeProduct as MutableLiveData<ChallengeResponse>
    }


    fun getchallenges(request: BasicRequest) {
        call!!.getChallenges(request).enqueue(object : Callback<ChallengeResponse> {
            override fun onFailure(call: Call<ChallengeResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                mChallengeProduct!!.value = null
            }

            override fun onResponse(call: Call<ChallengeResponse>?, response: Response<ChallengeResponse>?) {
                mChallengeProduct!!.value = response!!.body()
            }
        })
    }

    fun stopchallenges(challengeId: String) {
        call!!.stopChallenges(SharedPreferencesManager.getUserId(getApplication())!!, "")
            .enqueue(object : Callback<BaseModel> {
                override fun onFailure(call: Call<BaseModel>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<BaseModel>?, response: Response<BaseModel>?) {
                    Toast.makeText(AppApplication.applicationContext(), response!!.body()!!.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    fun startChallenge(data: Data) {
        call!!.startChallenge(data)
            .enqueue(object : Callback<BaseModel> {
                override fun onFailure(call: Call<BaseModel>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<BaseModel>?, response: Response<BaseModel>?) {
                    Toast.makeText(AppApplication.applicationContext(), response!!.body()!!.message, Toast.LENGTH_LONG)
                        .show()
                    getchallenges(BasicRequest(SharedPreferencesManager.getUserId(ChallengesFragment.mContext), ""))
                }
            })
    }
}