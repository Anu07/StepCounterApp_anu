package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.transactionhistory.TransactionHistoryModel
import com.sd.src.stepcounterapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionHistoryViewModel(application: Application) : AndroidViewModel(application) {

    val call = RetrofitClient.instance
    private var mTransactioHistory: MutableLiveData<TransactionHistoryModel>? = null

    fun getTransactionHistoryObject(): MutableLiveData<TransactionHistoryModel> {
        if (mTransactioHistory == null) {
            mTransactioHistory = MutableLiveData()
        }
        return mTransactioHistory as MutableLiveData<TransactionHistoryModel>
    }


    fun getTransactionHistory(request: BasicRequest) {
        call!!.getTransactionHistory(request).enqueue(object : Callback<TransactionHistoryModel> {
            override fun onFailure(call: Call<TransactionHistoryModel>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
                mTransactioHistory!!.value = null
            }

            override fun onResponse(
                call: Call<TransactionHistoryModel>?,
                response: Response<TransactionHistoryModel>?
            ) {
                  if (response!!.code() == 405) {
                    Toast.makeText(HayaTechApplication.applicationContext(), "User doesn't exist", Toast.LENGTH_LONG)
                        .show()
                    HayaTechApplication.instance!!.logoutUser()
                }else if (response!!.body()!!.status == 200) {
                    mTransactioHistory!!.value = response!!.body()

                }
            }
        })
    }


}