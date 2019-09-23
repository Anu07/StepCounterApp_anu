package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.survey.SurveyResponse
import com.sd.src.stepcounterapp.model.survey.surveyrequest.SurveystartRequestModel
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyViewModel(application: Application) : AndroidViewModel(application) {

    val call = RetrofitClient.instance

    private var mSurveyList: MutableLiveData<SurveyResponse>? = null
    private var mSurveyAttend: MutableLiveData<BasicInfoResponse>? = null

    fun getSurveyList(): MutableLiveData<SurveyResponse> {
        if (mSurveyList == null) {
            mSurveyList = MutableLiveData()
        }
        return mSurveyList as MutableLiveData<SurveyResponse>
    }

    fun takesurvey(): MutableLiveData<BasicInfoResponse> {
        if (mSurveyAttend == null) {
            mSurveyAttend = MutableLiveData()
        }
        return mSurveyAttend as MutableLiveData<BasicInfoResponse>
    }

    fun hitSurveyListApi() {
        call!!.getsurvey(BasicRequest(SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())))
            .enqueue(object :
                Callback<SurveyResponse> {
                override fun onFailure(call: Call<SurveyResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<SurveyResponse>?, response: Response<SurveyResponse>?) {
                    if (response!!.code() == 200) {
                        mSurveyList!!.value = response?.body()!!
                    } else  if (response!!.code() == 405) {
                        Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else {
                        var model = SurveyResponse()
                        model.message = "Invalid request"       //TODO
                        mSurveyList!!.value = model
                    }
                }
            })
    }


    fun hitAttendSurveyApi(request: SurveystartRequestModel) {
        call!!.takesurvey(request).enqueue(object :
            Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if(response!!.body()!!.status == 200){
                    mSurveyAttend!!.value = response!!.body()
                }else  if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                }else{
                    mSurveyAttend!!.value = BasicInfoResponse()
                }

            }
        })
    }


}