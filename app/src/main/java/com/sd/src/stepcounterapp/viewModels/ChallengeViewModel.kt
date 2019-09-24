package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.fragments.ChallengesFragment
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
            mChallengeProduct = MutableLiveData()
        return mChallengeProduct as MutableLiveData<ChallengeResponse>
    }


    fun getStartChallengeObject(): MutableLiveData<StartChallengeResponse> {
            mStartChallenge = MutableLiveData()
        return mStartChallenge as MutableLiveData<StartChallengeResponse>
    }


    fun getStopChallengeObject(): MutableLiveData<BasicInfoResponse> {
            mStopChallenge = MutableLiveData()
        return mStopChallenge as MutableLiveData<BasicInfoResponse>
    }


    fun getchallenges(request: BasicRequest) {
        call!!.getChallenges(request).enqueue(object : Callback<ChallengeResponse> {
            override fun onFailure(call: Call<ChallengeResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                mChallengeProduct!!.value = null
            }

            override fun onResponse(call: Call<ChallengeResponse>?, response: Response<ChallengeResponse>?) {
                if (response!!.body()!!.status == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    mChallengeProduct!!.value = ChallengeResponse()
                } else if (response!!.body()!!.status == 200) {
                    mChallengeProduct!!.value = response!!.body()
                }
            }
        })
    }

    fun stopchallenges(challengeId: String) {
        call!!.stopChallenges(SharedPreferencesManager.getUserId(getApplication())!!, challengeId)
            .enqueue(object : Callback<BasicInfoResponse> {
                override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(HayaTechApplication.applicationContext(), "Some error occurred", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                    if (response!!.code() == 405) {
                        Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else if (response!!.body()!!.status == 400) {
                        mStopChallenge!!.value = BasicInfoResponse()
                    } else if (response!!.body()!!.status == 200) {
                        mStopChallenge!!.value = response!!.body()
                        getchallenges(BasicRequest(SharedPreferencesManager.getUserId(ChallengesFragment.mContext), ""))
                    }

                }
            })
    }

    fun startChallenge(data: Data) {
        call!!.startChallenge(
            ChallengeStartRequestModel(
                data._id,
                data,
                SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
            )
        )
            .enqueue(object : Callback<StartChallengeResponse> {
                override fun onFailure(call: Call<StartChallengeResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(HayaTechApplication.applicationContext(), "Some error occurred", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<StartChallengeResponse>?,
                    response: Response<StartChallengeResponse>?
                ) {
                    when {
                        response!!.code() == 200 -> {
                            mStartChallenge!!.value = response!!.body()
                        }
                        response!!.code() == 401 -> {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "There is an ongoing challenge already.",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            mStartChallenge!!.value = StartChallengeResponse()
                        }
                        response!!.code() == 405 -> {
                                Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                                    .show()
                                HayaTechApplication.instance!!.logoutUser()
                        }
                        else -> {
                            var startRespo = StartChallengeResponse()
                            startRespo.status = 400
                            startRespo.message = "You have already joined this challenge."
                            mStartChallenge!!.value = startRespo
                        }
                    }


                }
            })
    }
}