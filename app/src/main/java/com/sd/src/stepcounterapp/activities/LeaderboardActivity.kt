package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.LeaderboardAdapter
import com.sd.src.stepcounterapp.fragments.SurveysFragment
import com.sd.src.stepcounterapp.model.leaderboard.Data
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.LeaderboardViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_leaderboard.*
import kotlinx.android.synthetic.main.crosstitlebar.*


class LeaderboardActivity : BaseActivity<LeaderboardViewModel>(), ItemClickGlobalListner {
    override fun onItemClick(pos: Int) {

    }

    private var mChallengeDataList: ArrayList<Data> = ArrayList()
    private lateinit var mLeaderAdapter: LeaderboardAdapter
    override val layoutId: Int
        get() = R.layout.activity_leaderboard
    override val viewModel: LeaderboardViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { LeaderboardViewModel(application) }).get(LeaderboardViewModel::class.java)
    override val context: Context
        get() = this@LeaderboardActivity


    override fun onCreate() {
        setLeaderboardAdapter()

        mViewModel!!.getLeaderboardResponse().observe(this, Observer { mData ->
            showPopupProgressSpinner(false)
            if (mData != null) {
                if(mData.data[0].name!=null){
                    firstLayout.visibility= View.VISIBLE
                    Picasso.get().load(RetrofitClient.IMG_URL+mData.data[0].image).error(R.drawable.nouser).into(firstImg)
                    firstName.text = mData.data[0].name
                    firstSteps.text = mData.data[0].steps.toString()

                    if (mData.data.size > 2) {
                        secondPosition.visibility = View.VISIBLE
                        secName.text = mData.data[1].name
                        secSteps.text = mData.data[1].steps.toString()
                    }

                    if (mData.data.size > 3) {
                        ThirdPosition.visibility = View.VISIBLE
                        thirdName.text = mData.data[2].name
                        thirdSteps.text = mData.data[2].steps.toString()
                    }

                    mChallengeDataList = mData.data as ArrayList<Data>
                    setLeaderboardAdapter()
                }else{
                    Toast.makeText(this@LeaderboardActivity,"No records found", Toast.LENGTH_LONG).show()
                }

            }


            var leaderselection = findViewById<Spinner>(R.id.leaderselection)
            val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, addEntriesToDropDown())
            leaderselection.adapter = adapter

        })


    }


    private fun addEntriesToDropDown(): Array<String>? {
        return resources.getStringArray(R.array.leaderArray)
    }


    override fun initListeners() {
        showPopupProgressSpinner(true)
        mViewModel!!.getLeaderboard(
            LeaderBoardRequest(
                getSelectedItem(),
                SharedPreferencesManager.getUserId(AppApplication.applicationContext())
            )
        )
        titleTxt.visibility = View.VISIBLE
        txt_title.visibility = View.GONE
       /* drop_downImg.setOnClickListener {
            leaderselection.performClick()
        }*/

        img_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getSelectedItem(): String? {
        return if(leaderselection.selectedItemPosition == 0) "general" else "challenge"
    }


    private fun setLeaderboardAdapter() {
        rvleaderboard.layoutManager = LinearLayoutManager(SurveysFragment.mContext)
        mLeaderAdapter = LeaderboardAdapter(SurveysFragment.mContext, mChallengeDataList, this)
        rvleaderboard.adapter = mLeaderAdapter
    }

}