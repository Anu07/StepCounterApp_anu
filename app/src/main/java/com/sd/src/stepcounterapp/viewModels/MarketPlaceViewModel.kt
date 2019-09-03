package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.marketplace.BasicSearchRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wishList.AddWishRequest
import com.sd.src.stepcounterapp.model.wishList.GetWishListRequest
import com.sd.src.stepcounterapp.model.wishList.WishListResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketPlaceViewModel(application: Application) : AndroidViewModel(application) {
    private var mPurchaseResponse: MutableLiveData<BasicInfoResponse>?= null
    val call = RetrofitClient.instance

    private var mProduct: MutableLiveData<MarketResponse>? = null
    private var mPopProduct: MutableLiveData<PopularProducts>? = null
    private var mWish: MutableLiveData<BasicInfoResponse>? = null

    fun getCategory(): MutableLiveData<MarketResponse> {
        if (mProduct == null) {
            mProduct = MutableLiveData()
        }
        return mProduct as MutableLiveData<MarketResponse>
    }
    fun getPurchase(): MutableLiveData<BasicInfoResponse> {
        if (mPurchaseResponse == null) {
            mPurchaseResponse = MutableLiveData()
        }
        return mPurchaseResponse as MutableLiveData<BasicInfoResponse>
    }

    fun getPopularity(): MutableLiveData<PopularProducts> {
        if (mPopProduct == null) {
            mPopProduct = MutableLiveData()
        }
        return mPopProduct as MutableLiveData<PopularProducts>
    }

    fun addWishList(): MutableLiveData<BasicInfoResponse> {
        if (mWish == null) {
            mWish = MutableLiveData()
        }
        return mWish as MutableLiveData<BasicInfoResponse>
    }

    fun getCategoryApi(request:BasicRequest) {
        call!!.getCategoryProducts(request).enqueue(object : Callback<MarketResponse> {

            override fun onFailure(call: Call<MarketResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MarketResponse>?, response: Response<MarketResponse>?) {
                mProduct!!.value = response!!.body()
            }
        })
    }

    fun getSearchCategoryApi(request:BasicSearchRequest) {
        call!!.searchCategoryProducts(request).enqueue(object : Callback<MarketResponse> {

            override fun onFailure(call: Call<MarketResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MarketResponse>?, response: Response<MarketResponse>?) {
                mProduct!!.value = response!!.body()
            }
        })
    }


    fun getProductApi(request:BasicRequest) {
        call!!.getPopularityProducts(request).enqueue(object : Callback<PopularProducts> {

            override fun onFailure(call: Call<PopularProducts>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<PopularProducts>?, response: Response<PopularProducts>?) {
                mPopProduct!!.value = response!!.body()
            }
        })
    }


    fun getSearchProductApi(request:BasicSearchRequest) {
        call!!.searchPopularProducts(request).enqueue(object : Callback<PopularProducts> {

            override fun onFailure(call: Call<PopularProducts>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<PopularProducts>?, response: Response<PopularProducts>?) {
                mPopProduct!!.value = response!!.body()
            }
        })
    }



    fun addWishList(body:AddWishRequest) {
        call!!.addWishList(body).enqueue(object : Callback<BasicInfoResponse> {

            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                mWish!!.value = response!!.body()
            }
        })
    }


    fun removeWishList(body:AddWishRequest) {
        call!!.removeWishList(body).enqueue(object : Callback<BasicInfoResponse> {

            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                mWish!!.value = response!!.body()
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
                }
            }
        })
    }
}