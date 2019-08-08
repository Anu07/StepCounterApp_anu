package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    val call = RetrofitClient.instance

    private var mUserModel: MutableLiveData<TokenModel>? = null

    fun getStepToken(): MutableLiveData<TokenModel> {
        if (mUserModel == null) {
            mUserModel = MutableLiveData()
        }
        return mUserModel as MutableLiveData<TokenModel>
    }

    fun setTokensFromSteps() {
        call!!.steps_to_token(SharedPreferencesManager.getUserId(getApplication())!!).enqueue(object : Callback<TokenModel> {
            override fun onFailure(call: Call<TokenModel>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<TokenModel>?, response: Response<TokenModel>?) {
                if (response!!.code() == 200) {
                    mUserModel!!.value = response.body()!!
                } else {
                    var login = TokenModel()
                    login.message = "Invalid request"
                    mUserModel!!.value = login
                }
            }
        })
    }

}