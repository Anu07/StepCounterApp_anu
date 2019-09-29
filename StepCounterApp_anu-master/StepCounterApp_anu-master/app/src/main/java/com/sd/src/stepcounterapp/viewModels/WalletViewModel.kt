package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicUserRequest
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.model.wallet.purchase.PurchaseResponse
import com.sd.src.stepcounterapp.model.wallet.walletDetailResponse.WalletModel
import com.sd.src.stepcounterapp.model.wishList.AddWishRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.LoadingDialog
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private var mDelete: MutableLiveData<BasicInfoResponse>? = null
    private var mPurchaseResponse: MutableLiveData<PurchaseResponse>? = null
    val call = RetrofitClient.instance
    private var mRemoveWish: MutableLiveData<BasicInfoResponse>? = null
    private var mTokenModel: MutableLiveData<TokenModel>? = null
    private var mWalletModel: MutableLiveData<WalletModel>? = null

    fun getStepToken(): MutableLiveData<TokenModel> {
        mTokenModel = MutableLiveData()
        return mTokenModel as MutableLiveData<TokenModel>
    }

    fun removeWishList(): MutableLiveData<BasicInfoResponse> {
        mRemoveWish = MutableLiveData()
        return mRemoveWish as MutableLiveData<BasicInfoResponse>
    }

    fun getDeleteResponse(): MutableLiveData<BasicInfoResponse> {
        mDelete = MutableLiveData()
        return mDelete as MutableLiveData<BasicInfoResponse>
    }

    fun getPurchase(): MutableLiveData<PurchaseResponse> {
        mPurchaseResponse = MutableLiveData()
        return mPurchaseResponse as MutableLiveData<PurchaseResponse>
    }

    fun getWalletData(): MutableLiveData<WalletModel> {
        mWalletModel = MutableLiveData()
        return mWalletModel as MutableLiveData<WalletModel>
    }

    fun setTokensFromSteps() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.steps_to_token(BasicUserRequest(SharedPreferencesManager.getUserId(getApplication())!!))
                .enqueue(object : Callback<TokenModel> {
                    override fun onFailure(call: Call<TokenModel>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "Server error",
                            Toast.LENGTH_LONG
                        ).show()
//                mTokenModel!!.value = null
                    }

                    override fun onResponse(
                        call: Call<TokenModel>?,
                        response: Response<TokenModel>?
                    ) {
                        if (response!!.code() == 200) {
                            mTokenModel!!.value = response.body()!!
                        } else if (response.code() == 405) {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        } else {
                            var model = TokenModel()
                            model.message = "You do not have enough steps to convert token."
                            mTokenModel!!.value = model
                        }
                    }
                })
        }


    }

    fun hitWalletApi() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            call!!.wallet(BasicUserRequest(SharedPreferencesManager.getUserId(getApplication())!!))
                .enqueue(object : Callback<WalletModel> {
                    override fun onFailure(call: Call<WalletModel>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "Server error",
                            Toast.LENGTH_LONG
                        ).show()
                        mWalletModel!!.value = null
                        LoadingDialog.getLoader().dismissLoader()
                    }

                    override fun onResponse(
                        call: Call<WalletModel>?,
                        response: Response<WalletModel>?
                    ) {
                        if (response!!.code() == 200) {
                            LoadingDialog.getLoader().dismissLoader()
                            mWalletModel!!.value = response.body()!!
                        } else if (response!!.code() == 405) {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        } else {
                            var model = WalletModel()
                            model.message = "Server error"
                            mWalletModel!!.value = model
                            LoadingDialog.getLoader().dismissLoader()
                        }
                    }
                })
        }
    }

    fun hitPurchaseApi(request: RedeemRequest) {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.redeemNow(request).enqueue(object : Callback<PurchaseResponse> {
                override fun onFailure(call: Call<PurchaseResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "Server error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<PurchaseResponse>?,
                    response: Response<PurchaseResponse>?
                ) {
                    if (response!!.code() == 200) {
                        mPurchaseResponse!!.value = response.body()
                    } else if (response!!.code() == 405) {
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "User doesn't exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else if (response!!.code() == 400) {
                        var basic = PurchaseResponse()
                        basic.status = 400
                        basic.message =
                            "This product has been out of stock, please try another product."
                        mPurchaseResponse!!.value = basic
                    } else {
                        mPurchaseResponse!!.value = PurchaseResponse()
                    }
                }
            })
        }


    }

    fun hitDeleteApi(redeemRequest: RedeemRequest) {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            call!!.deleteWishList(redeemRequest).enqueue(object : Callback<BasicInfoResponse> {

                override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "Server error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<BasicInfoResponse>?,
                    response: Response<BasicInfoResponse>?
                ) {
                    if (response!!.code() == 405) {
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "User doesn't exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else if (response!!.code() == 200) {
                        mDelete!!.value = response!!.body()

                    }
                }
            })
        }

    }


    fun removeWishList(body: AddWishRequest) {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            call!!.removeWishList(body).enqueue(object : Callback<BasicInfoResponse> {

                override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "Server error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<BasicInfoResponse>?,
                    response: Response<BasicInfoResponse>?
                ) {
                    if (response!!.code() == 405) {
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "User doesn't exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else if (response!!.code() == 200) {
                        mRemoveWish!!.value = response!!.body()
                    }
                }
            })
        }
    }

}