package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardRequest
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {

    val call = RetrofitClient.instance

    private var mLeaderboard: MutableLiveData<LeaderBoardResponse>? = null

    fun getLeaderboardResponse(): MutableLiveData<LeaderBoardResponse> {
        if (mLeaderboard == null) {
            mLeaderboard = MutableLiveData()
        }
        return mLeaderboard as MutableLiveData<LeaderBoardResponse>
    }


    fun getLeaderboard(leaderBoardRequest: LeaderBoardRequest) {
        call!!.getLeaderboard(leaderBoardRequest).enqueue(object : Callback<LeaderBoardResponse> {

            override fun onFailure(call: Call<LeaderBoardResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(AppApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<LeaderBoardResponse>?, response: Response<LeaderBoardResponse>?) {
               if(response!!.body()!!.status == 200){
                    if(response!!.body()!!.data.isNotEmpty()){
                        mLeaderboard!!.value = response!!.body()
                    }else{
                        mLeaderboard!!.value = LeaderBoardResponse()
                    }
               }
            }
        })
    }
}


