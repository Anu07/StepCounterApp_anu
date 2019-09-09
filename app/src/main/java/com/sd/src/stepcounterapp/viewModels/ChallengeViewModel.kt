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
import com.sd.src.stepcounterapp.model.challenge.ChallengeStartRequestModel
import com.sd.src.stepcounterapp.model.challenge.ChallengeTakenResponse.StartChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    val call = RetrofitClient.instance
    private var mChallengeProduct: MutableLiveData<ChallengeResponse>? = null
    private var mStartChallenge: MutableLiveData<StartChallengeResponse>? = null
    private var mStopChallenge: MutableLiveData<BasicInfoResponse>? = null

    fun getChallengeObject(): MutableLiveData<ChallengeResponse> {
        if (mChallengeProduct == null) {
            mChallengeProduct = MutableLiveData()
        }
        return mChallengeProduct as MutableLiveData<ChallengeResponse>
    }


    fun getStartChallengeObject(): MutableLiveData<StartChallengeResponse> {
        if (mStartChallenge == null) {
            mStartChallenge = MutableLiveData()
        }
        return mStartChallenge as MutableLiveData<StartChallengeResponse>
    }



    fun getStopChallengeObject(): MutableLiveData<BasicInfoResponse> {
        if (mStopChallenge == null) {
            mStopChallenge = MutableLiveData()
        }
        return mStopChallenge as MutableLiveData<BasicInfoResponse>
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
        call!!.stopChallenges(SharedPreferencesManager.getUserId(getApplication())!!, challengeId)
            .enqueue(object : Callback<BasicInfoResponse> {
                override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                    mStopChallenge!!.value = response!!.body()
                    getchallenges(BasicRequest(SharedPreferencesManager.getUserId(ChallengesFragment.mContext), ""))
                }
            })
    }

    fun startChallenge(data: Data) {
        call!!.startChallenge(ChallengeStartRequestModel(data._id,data,SharedPreferencesManager.getUserId(AppApplication.applicationContext())))
            .enqueue(object : Callback<StartChallengeResponse> {
                override fun onFailure(call: Call<StartChallengeResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<StartChallengeResponse>?, response: Response<StartChallengeResponse>?) {
                    Toast.makeText(AppApplication.applicationContext(), response!!.message(), Toast.LENGTH_LONG)
                        .show()
                    mStartChallenge!!.value = response!!.body()
                }
            })
    }
}