package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.HayaTechApplication.Companion.applicationContext
import com.sd.src.stepcounterapp.model.challenge.ChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.ChallengeStartRequestModel
import com.sd.src.stepcounterapp.model.challenge.Challengetakenresponse.StartChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.challenge.WeekendChallengeStartRequestModel
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.AVGSTEPS
import com.sd.src.stepcounterapp.utils.Utils
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
        if(Utils.retryInternet(applicationContext())){
            call!!.getChallenges(request).enqueue(object : Callback<ChallengeResponse> {
                override fun onFailure(call: Call<ChallengeResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                    mChallengeProduct!!.value = ChallengeResponse()
                }

                override fun onResponse(call: Call<ChallengeResponse>?, response: Response<ChallengeResponse>?) {
                    if (response!!.code() == 405) {
                        Toast.makeText(applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else if (response.body()!!.status == 200) {
                        mChallengeProduct!!.value = response.body()
                    }else  if (response.body()!!.status ==502){
                        Toast.makeText(applicationContext(), "502 Bad gateway", Toast.LENGTH_LONG)
                            .show()
                    }else{
                        Toast.makeText(applicationContext(), "Some error occurred", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
        }
    }

    fun stopchallenges(challengeId: String) {
        if(Utils.retryInternet(applicationContext())){
            call!!.stopChallenges(SharedPreferencesManager.getUserId(getApplication())!!, challengeId)
                .enqueue(object : Callback<BasicInfoResponse> {
                    override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(applicationContext(), "Some error occurred", Toast.LENGTH_LONG).show()
                        mStopChallenge!!.value = BasicInfoResponse()

                    }

                    override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                        when {
                            response!!.body()!!.status == 200 -> mStopChallenge!!.value = response.body()
                            response.code() ==404 -> mStopChallenge!!.value = BasicInfoResponse()
                            response.body()!!.status == 400 -> mStopChallenge!!.value = BasicInfoResponse()
                            response.code() == 405 -> {
                                Toast.makeText(applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                                    .show()
                                HayaTechApplication.instance!!.logoutUser()
                            }

                        }

                    }
                })
        }
    }

    fun startWeekendChallenge(data: Data) {
        if(Utils.retryInternet(applicationContext())){
            call!!.startWeekendChallenge(
                WeekendChallengeStartRequestModel(
                    data._id,
                    data,
                    SharedPreferencesManager.getUserId(applicationContext()),
                    calculateTargetSteps(data),
                    SharedPreferencesManager.getString(applicationContext(),AVGSTEPS)
                )
            )
                .enqueue(object : Callback<StartChallengeResponse> {
                    override fun onFailure(call: Call<StartChallengeResponse>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(applicationContext(), "Some error occurred", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<StartChallengeResponse>?,
                        response: Response<StartChallengeResponse>?
                    ) {
                        when {
                            response!!.code() == 200 -> {
                                mStartChallenge!!.value = response.body()
                            }
                            response.code() == 401 -> {
                                Toast.makeText(
                                    applicationContext(),
                                    "There is an ongoing challenge already.",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                mStartChallenge!!.value = StartChallengeResponse()
                            }
                            response.code() == 405 -> {
                                Toast.makeText(applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
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

    fun startChallenge(data: Data) {
        if(Utils.retryInternet(applicationContext())){
            call!!.startChallenge(
                ChallengeStartRequestModel(
                    data._id,
                    data,
                    SharedPreferencesManager.getUserId(applicationContext())
                )
            )
                .enqueue(object : Callback<StartChallengeResponse> {
                    override fun onFailure(call: Call<StartChallengeResponse>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(applicationContext(), "Some error occurred", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<StartChallengeResponse>?,
                        response: Response<StartChallengeResponse>?
                    ) {
                        when {
                            response!!.code() == 200 -> {
                                mStartChallenge!!.value = response.body()
                            }
                            response.code() == 401 -> {
                                Toast.makeText(
                                    applicationContext(),
                                    "There is an ongoing challenge already.",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                mStartChallenge!!.value = StartChallengeResponse()
                            }
                            response.code() == 405 -> {
                                Toast.makeText(applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
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



    private fun calculateTargetSteps(data: Data): Int {
       var avgSteps = SharedPreferencesManager.getString(applicationContext(),AVGSTEPS).toInt()
        Log.i("Target", "steps$avgSteps")
        return  avgSteps+(avgSteps*data.increaseSteps)/100
    }
}