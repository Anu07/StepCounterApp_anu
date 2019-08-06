package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.survey.SurveyModel
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyViewModel(application: Application) : AndroidViewModel(application) {

    val call = RetrofitClient.instance

    private var mSurveyList: MutableLiveData<SurveyModel>? = null

    fun getSurveyList(): MutableLiveData<SurveyModel> {
        if (mSurveyList == null) {
            mSurveyList = MutableLiveData()
        }
        return mSurveyList as MutableLiveData<SurveyModel>
    }

    fun hitSurveyListApi() {
        call!!.getsurvey().enqueue(object :
            Callback<SurveyModel> {
            override fun onFailure(call: Call<SurveyModel>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<SurveyModel>?, response: Response<SurveyModel>?) {
                if(response!!.code()==200){
                    mSurveyList!!.value = response!!.body()!!
                }else{
                    var model = SurveyModel()
                    model.message = "Invalid request"       //TODO
                    mSurveyList!!.value = model
                }
            }
        })
    }


}