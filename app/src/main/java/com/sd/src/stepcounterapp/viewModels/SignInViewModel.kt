package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.BasicInfoRequestObject
import com.sd.src.stepcounterapp.model.bmi.BMIinfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.image.ImageResponse
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject
import com.sd.src.stepcounterapp.model.rewards.AddRewardsRequestObject
import com.sd.src.stepcounterapp.model.rewards.RewardsCategoriesResponse
import com.sd.src.stepcounterapp.model.rewards.selectedRewards.SelectedRewardCategories
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.repositories.UserRepository
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
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
    private var mBmiResponseModel: MutableLiveData<BMIinfoResponse>? = null
    private var mImageResponseModel: MutableLiveData<ImageResponse>? = null
    private var mRewardsCategoriesResponseModel: MutableLiveData<RewardsCategoriesResponse>? = null
    private var mSelectedRewardsCategoriesResponseModel: MutableLiveData<SelectedRewardCategories>? = null

    val call = RetrofitClient.instance

    fun getUser(): MutableLiveData<LoginResponseJ> {
            mUserModel = MutableLiveData()
        return mUserModel as MutableLiveData<LoginResponseJ>
    }

    fun getRewardsCategories(): MutableLiveData<RewardsCategoriesResponse> {
            mRewardsCategoriesResponseModel = MutableLiveData()
        return mRewardsCategoriesResponseModel as MutableLiveData<RewardsCategoriesResponse>
    }

    fun getSelectedRewardsCategories(): MutableLiveData<SelectedRewardCategories> {
            mSelectedRewardsCategoriesResponseModel = MutableLiveData()
        return mSelectedRewardsCategoriesResponseModel as MutableLiveData<SelectedRewardCategories>
    }

    fun getResponse(): MutableLiveData<LoginResponseJ> {
            mResponseModel = MutableLiveData()
        return mResponseModel as MutableLiveData<LoginResponseJ>
    }

    fun getBasicResponse(): MutableLiveData<BasicInfoResponse> {
            mBasicResponseModel = MutableLiveData()
        return mBasicResponseModel as MutableLiveData<BasicInfoResponse>
    }


    fun getBmiResponse(): MutableLiveData<BMIinfoResponse> {
            mBmiResponseModel = MutableLiveData()
        return mBmiResponseModel as MutableLiveData<BMIinfoResponse>
    }

    fun getImageResponse(): MutableLiveData<ImageResponse> {
            mImageResponseModel = MutableLiveData()
        return mImageResponseModel as MutableLiveData<ImageResponse>
    }

    fun setLoginData(login: LoginRequestObject) {
        call!!.authenticate_user(login).enqueue(object : Callback<LoginResponseJ> {
            override fun onFailure(call: Call<LoginResponseJ>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<LoginResponseJ>?, response: Response<LoginResponseJ>?) {
                if (response!!.code() == 200) {
                    mUserModel!!.value = response!!.body()!!
                } else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else {
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
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if (response!!.code() == 200) {
                    mBasicResponseModel!!.value = response!!.body()!!
                } else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else {
                    mBasicResponseModel!!.value = BasicInfoResponse()
                    mBasicResponseModel!!.value!!.message = "No email record"
                }
            }
        })

    }

    fun addBasicInfo(login: BasicInfoRequestObject) {
        call!!.add_basic_info(login).enqueue(object : Callback<BMIinfoResponse> {
            override fun onFailure(call: Call<BMIinfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed" + t!!.message)
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BMIinfoResponse>?, response: Response<BMIinfoResponse>?) {
                if (response!!.body()!!.status == 200) {
                    mBmiResponseModel!!.value = response!!.body()!!
                } else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else {
                    Toast.makeText(HayaTechApplication.applicationContext(), response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

    }


    fun uploadImage(userPic: RequestBody, image: MultipartBody.Part) {
        call!!.uploadImage(userPic, image).enqueue(object : Callback<ImageResponse> {
            override fun onFailure(call: Call<ImageResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ImageResponse>?, response: Response<ImageResponse>?) {
                if (response!!.code() != 400) {
                    mImageResponseModel!!.value = response!!.body()!!
                } else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else {
                    Toast.makeText(HayaTechApplication.applicationContext(), response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

    }

    fun checkAvailability(userName: String) {
        call!!.check_username(userName).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if (response!!.code() != 400) {
                    mBasicResponseModel!!.value = response!!.body()!!
                } else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else {
                    mBasicResponseModel!!.value = BasicInfoResponse()
                    mBasicResponseModel!!.value!!.message = "Username Exists"
                }
            }
        })

    }

    /**
     * To fetch rewards categories
     */
    fun getRewardCategory() {
        call!!.getRewardsCategories().enqueue(object : Callback<RewardsCategoriesResponse> {
            override fun onFailure(call: Call<RewardsCategoriesResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<RewardsCategoriesResponse>?,
                response: Response<RewardsCategoriesResponse>?
            ) {
                if (response!!.code() != 400) {
                    mRewardsCategoriesResponseModel!!.value = response!!.body()!!
                } else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else {
                    mRewardsCategoriesResponseModel!!.value = RewardsCategoriesResponse()
                }
            }
        })

    }


    /**
     * To get selected reqwards categories
     */
    fun getSelectedRewardCategories() {
        call!!.getSelectedRewardsCategories(BasicRequest(SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())))
            .enqueue(object : Callback<SelectedRewardCategories> {
                override fun onFailure(call: Call<SelectedRewardCategories>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<SelectedRewardCategories>?,
                    response: Response<SelectedRewardCategories>?
                ) {
                    if (response!!.code() != 400) {
                        mSelectedRewardsCategoriesResponseModel!!.value = response!!.body()!!
                    } else if (response!!.code() == 405) {
                        Toast.makeText(
                            HayaTechApplication.applicationContext(),
                            "User doesn't exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    } else {
                        mSelectedRewardsCategoriesResponseModel!!.value = SelectedRewardCategories()
                    }
                }
            })

    }


    /**
     * Add rewards categories
     */


    fun AddRewardsCategory(addRequest: AddRewardsRequestObject) {
        call!!.AddRewards(addRequest).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if (response!!.code() != 400) {
                    mBasicResponseModel!!.value = response!!.body()!!
                } else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else {
                    mBasicResponseModel!!.value = BasicInfoResponse()
                }
            }
        })

    }


    /**
     * Add rewards categories
     */


    fun getRewardsCategory(addRequest: AddRewardsRequestObject) {
        call!!.AddRewards(addRequest).enqueue(object : Callback<BasicInfoResponse> {
            override fun onFailure(call: Call<BasicInfoResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BasicInfoResponse>?, response: Response<BasicInfoResponse>?) {
                if (response!!.code() != 400) {
                    mBasicResponseModel!!.value = response!!.body()!!
                } else if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else {
                    mBasicResponseModel!!.value = BasicInfoResponse()
                }
            }
        })

    }

}