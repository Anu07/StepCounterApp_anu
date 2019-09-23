package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.image.ImageResponse
import com.sd.src.stepcounterapp.model.profile.UpdateProfileRequest
import com.sd.src.stepcounterapp.model.updateresponse.UpdateProfileResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpProfileViewModel(application: Application) : AndroidViewModel(application) {
    private var mUpdateDataResponse: MutableLiveData<UpdateProfileResponse>? = null
    private var mImageDataResponseModel: MutableLiveData<ImageResponse>? = null
    val call = RetrofitClient.instance


    fun getUpdateProfileResponse(): MutableLiveData<UpdateProfileResponse> {
        if (mUpdateDataResponse == null) {
            mUpdateDataResponse = MutableLiveData()
        }
        return mUpdateDataResponse as MutableLiveData<UpdateProfileResponse>
    }

    fun getImageResponse(): MutableLiveData<ImageResponse> {
        if (mImageDataResponseModel == null) {
            mImageDataResponseModel = MutableLiveData()
        }
        return mImageDataResponseModel as MutableLiveData<ImageResponse>
    }


    fun updateProfileData(request: UpdateProfileRequest) {
        call!!.update_profile(request).enqueue(object : Callback<UpdateProfileResponse> {
            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                } else if (response!!.code() == 200) {
                    mUpdateDataResponse!!.value = response.body()
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
                when {
                    response!!.code() == 405 -> {
                        Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                            .show()
                        HayaTechApplication.instance!!.logoutUser()
                    }
                    response!!.code() != 400 -> mImageDataResponseModel!!.value = response!!.body()!!
                    else -> Toast.makeText(HayaTechApplication.applicationContext(), response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

    }

}