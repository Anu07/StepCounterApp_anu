package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.model.privacy.PrivacyResponse
import com.sd.src.stepcounterapp.network.RetrofitClient
import kotlinx.android.synthetic.main.fragment_web.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.text.method.ScrollingMovementMethod



class TnCFragment : BaseFragment() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: TnCFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): TnCFragment {
            instance = TnCFragment()
            mContext = context
            return instance
        }
    }

    val call = RetrofitClient.instance


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        call!!.gettnc().enqueue(object :
            Callback<PrivacyResponse> {
            override fun onFailure(call: Call<PrivacyResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
                Toast.makeText(HayaTechApplication.applicationContext(), "Server error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<PrivacyResponse>?, response: Response<PrivacyResponse>?) {
                if (response!!.body()!!.status == 200) {
                    largeTxt.text = Html.fromHtml(response.body()!!.data[0].description)
                }
            }
        })

        largeTxt.movementMethod = ScrollingMovementMethod()

    }

    override fun onDetach() {
        super.onDetach()
        (mContext as LandingActivity).hideBottomLayout(true)
    }


}