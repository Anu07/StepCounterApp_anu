package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.model.wallet.walletDetailResponse.WalletModel
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.LoadingDialog
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private var mDelete: MutableLiveData<BasicInfoResponse>? = null
    private var mPurchaseResponse: MutableLiveData<BasicInfoResponse>?= null
    val call = RetrofitClient.instance

    private var mTokenModel: MutableLiveData<TokenModel>? = null
    private var mWalletModel: MutableLiveData<WalletModel>? = null

    fun getStepToken(): MutableLiveData<TokenModel> {
        if (mTokenModel == null) {
            mTokenModel = MutableLiveData()
        }
        return mTokenModel as MutableLiveData<TokenModel>
    }


    fun getDeleteResponse(): MutableLiveData<BasicInfoResponse> {
        if (mDelete == null) {
            mDelete = MutableLiveData()
        }
        return mDelete as MutableLiveData<BasicInfoResponse>
    }

    fun getPurchase(): MutableLiveData<BasicInfoResponse> {
        if (mPurchaseResponse == null) {
            mPurchaseResponse = MutableLiveData()
        }
        return mPurchaseResponse as MutableLiveData<BasicInfoResponse>
    }

    fun getWalletData(): MutableLiveData<WalletModel> {
        if (mWalletModel == null) {
            mWalletModel = MutableLiveData()
        }
        return mWalletModel as MutableLiveData<WalletModel>
    }

    fun setTokensFromSteps() {
        call!!.steps_to_token(BasicRequest(SharedPreferencesManager.getUserId(getApplication())!!)).enqueue(object : Callback<TokenModel> {
            override fun onFailure(call: Call<TokenModel>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
//                mTokenModel!!.value = null
            }

            override fun onResponse(call: Call<TokenModel>?, response: Response<TokenModel>?) {
                if (response!!.code() == 200) {
                    mTokenModel!!.value = response.body()!!
                } else {
                    var model = TokenModel()
                    model.message = "Insufficient steps"
                    mTokenModel!!.value = model
                }
            }
        })
    }
    fun hitWalletApi() {
        call!!.wallet(BasicRequest(SharedPreferencesManager.getUserId(getApplication())!!)).enqueue(object : Callback<WalletModel> {
            override fun onFailure(call: Call<WalletModel>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                mWalletModel!!.value = null
                LoadingDialog.getLoader().dismissLoader()
            }

            override fun onResponse(call: Call<WalletModel>?, response: Response<WalletModel>?) {
                if (response!!.code() == 200) {
                    LoadingDialog.getLoader().dismissLoader()
                    mWalletModel!!.value = response.body()!!
                } else {
                    var model = WalletModel()
                    model.message = "Server error"
                    mWalletModel!!.value = model
                    LoadingDialog.getLoader().dismissLoader()
                }
            }
        })
    }

    fun hitPurchaseApi(request: RedeemRequest) {
        call!!.redeemNow(request).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if (response!!.code() == 200) {
                    mPurchaseResponse!!.value = response.body()
                }else{
                    mPurchaseResponse!!.value = BasicInfoResponse()
                }
            }
        })
    }

    fun hitDeleteApi(redeemRequest: RedeemRequest) {
        call!!.deleteWishList(redeemRequest).enqueue(object : Callback<BasicInfoResponse> {

            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                mDelete!!.value = response!!.body()
            }
        })
    }

}