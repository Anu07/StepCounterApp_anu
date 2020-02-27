package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.challenge.MyChallengeResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.generic.BasicUserRequest
import com.sd.src.stepcounterapp.model.profile.ProfileResponse
import com.sd.src.stepcounterapp.model.rewards.MyRedeemedResponse
import com.sd.src.stepcounterapp.model.survey.mysurveyresponse.MySurveyResponseModel
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private var mProfileResponse: MutableLiveData<ProfileResponse>? = null
    private var mMyChallengeResponse: MutableLiveData<MyChallengeResponse>? = null
    private var mMyRedeemedResponse: MutableLiveData<MyRedeemedResponse>? = null
    private var mySurveyReponse: MutableLiveData<MySurveyResponseModel>? = null


    val call = RetrofitClient.instance

    fun getProfileResponse(): MutableLiveData<ProfileResponse> {
        if (mProfileResponse == null) {
            mProfileResponse = MutableLiveData()
        }
        return mProfileResponse as MutableLiveData<ProfileResponse>
    }

    fun getSurveyResponse(): MutableLiveData<MySurveyResponseModel> {
        if (mySurveyReponse == null) {
            mySurveyReponse = MutableLiveData()
        }
        return mySurveyReponse as MutableLiveData<MySurveyResponseModel>
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
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.getProfileData(
                BasicUserRequest(
                    SharedPreferencesManager.getUserId(
                        HayaTechApplication.applicationContext()
                    )
                )
            )
                .enqueue(object : Callback<ProfileResponse> {
                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "Server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<ProfileResponse>,
                        response: Response<ProfileResponse>
                    ) {
                        if (response.code() == 405) {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        } else if (response.body()!!.status == 200) {
                            mProfileResponse!!.value = response.body()
                        }
                    }

                })
        }


    }


    fun getMyChallenge() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.getMyChallenges(
                BasicRequest(
                    SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                    0
                )
            )
                .enqueue(object :
                    Callback<MyChallengeResponse> {
                    override fun onFailure(call: Call<MyChallengeResponse>, t: Throwable) {
                        Log.v("retrofit", "call failed" + t)
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<MyChallengeResponse>,
                        response: Response<MyChallengeResponse>
                    ) {
                        if (response.code() == 405) {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        } else if (response.body()!!.status == 200) {
                            mMyChallengeResponse!!.value = response.body()
                        }
                    }

                })
        }


    }

    fun getRedeemRewards() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.getMyRedeemedRewards(
                BasicRequest(
                    SharedPreferencesManager.getUserId(
                        HayaTechApplication.applicationContext()
                    ), 0
                )
            )
                .enqueue(object :
                    Callback<MyRedeemedResponse> {
                    override fun onFailure(call: Call<MyRedeemedResponse>, t: Throwable) {
                        Log.v("retrofit", "call failed" + t)
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "Server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<MyRedeemedResponse>,
                        response: Response<MyRedeemedResponse>
                    ) {
                        if (response.code() == 405) {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        } else if (response.body()!!.status == 200) {
                            mMyRedeemedResponse!!.value = response.body()
                        }
                    }

                })
        }


    }


    fun getMySurveys() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.getMySurveys(
                BasicRequest(
                    SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                    0
                )
            )
                .enqueue(object :
                    Callback<MySurveyResponseModel> {
                    override fun onFailure(call: Call<MySurveyResponseModel>, t: Throwable) {
                        Log.v("retrofit", "call failed" + t)
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "Server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<MySurveyResponseModel>,
                        response: Response<MySurveyResponseModel>
                    ) {
                        if (response.code() == 405) {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        } else if (response.body()!!.status == 200) {
                            mySurveyReponse!!.value = response.body()

                        }
                    }

                })
        }


    }

}