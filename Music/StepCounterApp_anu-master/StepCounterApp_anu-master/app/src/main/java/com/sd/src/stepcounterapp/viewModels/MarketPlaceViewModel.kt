package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.marketplace.BasicSearchRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.model.marketplace.walletInfo.WalletModelResponse
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wallet.purchase.PurchaseResponse
import com.sd.src.stepcounterapp.model.wishlist.AddWishRequest
import com.sd.src.stepcounterapp.model.wishlist.basicwishlist.BasicWishListResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.LoadingDialog
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketPlaceViewModel(application: Application) : AndroidViewModel(application) {
    private var mPurchaseResponse: MutableLiveData<PurchaseResponse>? = null
    val call = RetrofitClient.instance
    private var mWalletModel: MutableLiveData<WalletModelResponse>? = null

    private var mProduct: MutableLiveData<MarketResponse>? = null
    private var mPopProduct: MutableLiveData<PopularProducts>? = null
    private var mWish: MutableLiveData<BasicWishListResponse>? = null

    private var mRemoveWish: MutableLiveData<BasicWishListResponse>? = null
    fun getCategory(): MutableLiveData<MarketResponse> {
        mProduct = MutableLiveData()
        return mProduct as MutableLiveData<MarketResponse>
    }

    fun getPurchase(): MutableLiveData<PurchaseResponse> {
        mPurchaseResponse = MutableLiveData()
        return mPurchaseResponse as MutableLiveData<PurchaseResponse>
    }

    fun getPopularity(): MutableLiveData<PopularProducts> {
        mPopProduct = MutableLiveData()
        return mPopProduct as MutableLiveData<PopularProducts>
    }

    fun addWishList(): MutableLiveData<BasicWishListResponse> {
        mWish = MutableLiveData()
        return mWish as MutableLiveData<BasicWishListResponse>
    }


    fun removeWishList(): MutableLiveData<BasicWishListResponse> {
        mRemoveWish = MutableLiveData()
        return mRemoveWish as MutableLiveData<BasicWishListResponse>
    }

    fun getCategoryApi(request: BasicRequest) {
        call!!.getCategoryProducts(request).enqueue(object : Callback<MarketResponse> {

            override fun onFailure(call: Call<MarketResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(
                    HayaTechApplication.applicationContext(),
                    t!!.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<MarketResponse>?,
                response: Response<MarketResponse>?
            ) {
                if (response!!.code() == 405) {
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "User doesn't exist",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else if (response.body()!!.status == 200) {
                    mProduct!!.value = response.body()
                }
            }
        })
    }

    fun getSearchCategoryApi(request: BasicSearchRequest) {
        call!!.searchCategoryProducts(request).enqueue(object : Callback<MarketResponse> {

            override fun onFailure(call: Call<MarketResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(
                    HayaTechApplication.applicationContext(),
                    "Server error",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<MarketResponse>?,
                response: Response<MarketResponse>?
            ) {
                when {
                    response!!.code() == 405 -> {
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "User doesn't exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    }
                    response.code() == 400 -> Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "No record found.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    response.code() == 200 -> mProduct!!.value = response.body()
                    else -> mProduct!!.value = MarketResponse()
                }
            }
        })
    }


    fun getProductApi(request: BasicRequest) {
        call!!.getPopularityProducts(request).enqueue(object : Callback<PopularProducts> {

            override fun onFailure(call: Call<PopularProducts>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(
                    HayaTechApplication.applicationContext(),
                    "Server error "+t!!.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<PopularProducts>?,
                response: Response<PopularProducts>?
            ) {
                if (response!!.code() == 405) {
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "User doesn't exist",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else if (response.body()!!.status == 200) {
                    mPopProduct!!.value = response.body()
                }
            }
        })
    }


    fun getSearchProductApi(request: BasicSearchRequest) {
        call!!.searchPopularProducts(request).enqueue(object : Callback<PopularProducts> {

            override fun onFailure(call: Call<PopularProducts>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(
                    HayaTechApplication.applicationContext(),
                    "Server error",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<PopularProducts>?,
                response: Response<PopularProducts>?
            ) {
                if (response!!.code() == 405) {
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "User doesn't exist",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else if (response.body()!!.status == 200) {
                    mPopProduct!!.value = response.body()
                }
            }
        })
    }

    fun getWalletData(): MutableLiveData<WalletModelResponse> {
        if (mWalletModel == null) {
            mWalletModel = MutableLiveData()
        }
        return mWalletModel as MutableLiveData<WalletModelResponse>
    }


    fun hitWalletApi() {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.walletData(
                BasicRequest(
                    SharedPreferencesManager.getUserId(getApplication())!!,
                    0
                )
            )
                .enqueue(object : Callback<WalletModelResponse> {
                    override fun onFailure(call: Call<WalletModelResponse>?, t: Throwable?) {
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
                        call: Call<WalletModelResponse>?,
                        response: Response<WalletModelResponse>?
                    ) {
                        when {
                            response!!.code() == 200 -> {
                                LoadingDialog.getLoader().dismissLoader()
                                mWalletModel!!.value = response.body()!!
                            }
                            response.code() ==400 -> {
                            LoadingDialog.getLoader().dismissLoader()
                                mWalletModel!!.value = WalletModelResponse()
                            }
                            response.code() == 405 -> {
                                Toast.makeText(
                                    HayaTechApplication.applicationContext(),
                                    "User doesn't exist",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                HayaTechApplication.instance!!.logoutUser()
                            }
                            else -> {
                                var model = WalletModelResponse()
                                model.message = "Server error"
                                mWalletModel!!.value = model
                                LoadingDialog.getLoader().dismissLoader()
                            }
                        }
                    }
                })
        }


    }


    fun addWishList(body: AddWishRequest) {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.addWishList(body).enqueue(object : Callback<BasicWishListResponse> {

                override fun onFailure(call: Call<BasicWishListResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "Server error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<BasicWishListResponse>?,
                    response: Response<BasicWishListResponse>?
                ) {
                    if (response!!.code() == 405) {
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "User doesn't exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else if (response.body()!!.status == 200) {
                        var basResponse = BasicWishListResponse()
                        basResponse.productId =  body.productId
                        basResponse.status = response.body()!!.status
                        basResponse.message = response.body()!!.message
                        mWish!!.value = basResponse
                    }

                }
            })
        }


    }


    fun removeWishList(body: AddWishRequest) {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            call!!.removeWishList(body).enqueue(object : Callback<BasicWishListResponse> {

                override fun onFailure(call: Call<BasicWishListResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        "Server error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<BasicWishListResponse>?,
                    response: Response<BasicWishListResponse>?
                ) {
                    if (response!!.code() == 405) {
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "User doesn't exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else if (response.code() == 200) {
                        var basResponse = BasicWishListResponse()
                        basResponse.productId =  body.productId
                        basResponse.status = response.body()!!.status
                        basResponse.message = response.body()!!.message
                        mWish!!.value = basResponse
                        mRemoveWish!!.value = basResponse
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
                    when {
                        response!!.code() == 405 -> {
                            Toast.makeText(
                                HayaTechApplication.applicationContext(),
                                "User doesn't exist",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            HayaTechApplication.instance!!.logoutUser()
                        }
                        response.code() == 200 -> mPurchaseResponse!!.value = response.body()
                        else -> {
                            var binfo = PurchaseResponse()
                            binfo.status = 400
                            binfo.message = "Unavailable"
                            mPurchaseResponse!!.value = binfo
                        }
                    }
                }
            })
        }
    }
}