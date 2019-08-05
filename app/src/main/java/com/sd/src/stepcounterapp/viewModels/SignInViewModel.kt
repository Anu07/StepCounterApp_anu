package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.BasicInfoRequestObject
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.image.ImageResponse
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject
import com.sd.src.stepcounterapp.model.rewards.AddRewardsRequestObject
import com.sd.src.stepcounterapp.model.rewards.RewardsCategoriesResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.repositories.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: UserRepository = UserRepository(application)

    private var mUserModel: MutableLiveData<LoginResponseJ>? = null
    private var mResponseModel: MutableLiveData<LoginResponseJ>? = null
    private var mBasicResponseModel: MutableLiveData<BasicInfoResponse>? = null
    private var mImageResponseModel: MutableLiveData<ImageResponse>? = null
    private var mRewardsCategoriesResponseModel: MutableLiveData<RewardsCategoriesResponse>? = null

    val call = RetrofitClient.instance

    fun getUser(): MutableLiveData<LoginResponseJ> {
        if (mUserModel == null) {
            mUserModel = MutableLiveData()
        }
        return mUserModel as MutableLiveData<LoginResponseJ>
    }

    fun getRewardsCategories(): MutableLiveData<RewardsCategoriesResponse> {
        if (mRewardsCategoriesResponseModel == null) {
            mRewardsCategoriesResponseModel = MutableLiveData()
        }
        return mRewardsCategoriesResponseModel as MutableLiveData<RewardsCategoriesResponse>
    }

    fun getResponse(): MutableLiveData<LoginResponseJ> {
        if (mResponseModel == null) {
            mResponseModel = MutableLiveData()
        }
        return mResponseModel as MutableLiveData<LoginResponseJ>
    }

    fun getBasicResponse(): MutableLiveData<BasicInfoResponse> {
        if (mBasicResponseModel == null) {
            mBasicResponseModel = MutableLiveData()
        }
        return mBasicResponseModel as MutableLiveData<BasicInfoResponse>
    }

    fun getImageResponse(): MutableLiveData<ImageResponse> {
        if (mImageResponseModel == null) {
            mImageResponseModel = MutableLiveData()
        }
        return mImageResponseModel as MutableLiveData<ImageResponse>
    }

    fun setLoginData(login: LoginRequestObject) {
        call!!.authenticate_user(login).enqueue(object : Callback<LoginResponseJ> {
            override fun onFailure(call: Call<LoginResponseJ>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<LoginResponseJ>?, response: Response<LoginResponseJ>?) {
                if(response!!.code()==200){
                    mUserModel!!.value = response!!.body()!!
                }else{
                    var login = LoginResponseJ()
                    login.message = "Invalid request"       //TODO
                    mUserModel!!.value = login

                }
            }
        })
    }

    fun forgetPassword(login: LoginRequestObject) {
        call!!.forget_password(login).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if(response!!.code()==200){
                    mBasicResponseModel!!.value = response!!.body()!!
                }else{
                    mBasicResponseModel!!.value = BasicInfoResponse()
                    mBasicResponseModel!!.value!!.message = "No email record"
                }
            }
        })

    }

    fun addBasicInfo(login: BasicInfoRequestObject) {
        call!!.add_basic_info(login).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
               if(response!!.code()!=400){
                   mBasicResponseModel!!.value = response!!.body()!!
               }else{
                   Toast.makeText(AppApplication.applicationContext(), response.message(), Toast.LENGTH_LONG).show()
               }
            }
        })

    }


    fun uploadImage(userPic: RequestBody, image: MultipartBody.Part) {
        call!!.uploadImage(userPic,image).enqueue(object : Callback<ImageResponse> {
            override fun onFailure(call: Call<ImageResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ImageResponse>?, response: Response<ImageResponse>?) {
                if(response!!.code()!=400){
                    mImageResponseModel!!.value = response!!.body()!!
                }else{
                    Toast.makeText(AppApplication.applicationContext(), response.message(), Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    fun checkAvailability(userName: String) {
        call!!.check_username(userName).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if(response!!.code()!=400){
                    mBasicResponseModel!!.value = response!!.body()!!
                }else{
                    mBasicResponseModel!!.value = BasicInfoResponse()
                    mBasicResponseModel!!.value!!.message = "Username Exists"
                }
            }
        })

    }

    /**
     * To fetch reqwards categories
     */


    fun getRewardCategory() {
        call!!.getRewardsCategories().enqueue(object : Callback<RewardsCategoriesResponse> {
            override fun onFailure(call: Call<RewardsCategoriesResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<RewardsCategoriesResponse>?, response: Response<RewardsCategoriesResponse>?) {
                if(response!!.code()!=400){
                    mRewardsCategoriesResponseModel!!.value = response!!.body()!!
                }else{
                    mRewardsCategoriesResponseModel!!.value = RewardsCategoriesResponse()
                }
            }
        })

    }

    /**
     * Add rewards categories
     */


    fun AddRewardsCategory( addRequest:AddRewardsRequestObject) {
        call!!.AddRewards(addRequest).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if(response!!.code()!=400){
                    mBasicResponseModel!!.value = response!!.body()!!
                }else{
                    mBasicResponseModel!!.value = BasicInfoResponse()
                }
            }
        })

    }

}