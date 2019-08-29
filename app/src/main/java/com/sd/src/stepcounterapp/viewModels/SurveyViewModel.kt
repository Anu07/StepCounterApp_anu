package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.survey.SurveyListResponse
import com.sd.src.stepcounterapp.model.survey.surveyrequest.SurveystartRequestModel
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyViewModel(application: Application) : AndroidViewModel(application) {

    val call = RetrofitClient.instance

    private var mSurveyList: MutableLiveData<SurveyListResponse>? = null
    private var mSurveyAttend: MutableLiveData<BasicInfoResponse>? = null

    fun getSurveyList(): MutableLiveData<SurveyListResponse> {
        if (mSurveyList == null) {
            mSurveyList = MutableLiveData()
        }
        return mSurveyList as MutableLiveData<SurveyListResponse>
    }

    fun takesurvey(): MutableLiveData<BasicInfoResponse> {
        if (mSurveyAttend == null) {
            mSurveyAttend = MutableLiveData()
        }
        return mSurveyAttend as MutableLiveData<BasicInfoResponse>
    }

    fun hitSurveyListApi() {
        call!!.getsurvey(BasicRequest(SharedPreferencesManager.getUserId(AppApplication.applicationContext())))
            .enqueue(object :
                Callback<SurveyListResponse> {
                override fun onFailure(call: Call<SurveyListResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<SurveyListResponse>?, response: Response<SurveyListResponse>?) {
                    if (response!!.code() == 200) {
                        mSurveyList!!.value = response!!.body()!!
                    } else {
                        var model = SurveyListResponse()
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
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                mSurveyAttend!!.value = response!!.body()

            }
        })
    }


}