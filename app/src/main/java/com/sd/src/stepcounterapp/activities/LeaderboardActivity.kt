package com.sd.src.stepcounterapp.activities

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.LeaderboardAdapter
import com.sd.src.stepcounterapp.fragments.SurveysFragment
import com.sd.src.stepcounterapp.model.leaderboard.Challenge
import com.sd.src.stepcounterapp.model.leaderboard.General
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.LeaderboardViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_leaderboard.*

class LeaderboardActivity : BaseActivity<LeaderboardViewModel>(), ItemClickGlobalListner {
    override fun onItemClick(pos: Int) {
    }
    private var mGeneralDataList: ArrayList<General> = ArrayList()
    private var mChallengeDataList: ArrayList<Challenge> = ArrayList()
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

        mViewModel!!.getLeaderboardResponse().observe(this, Observer {
            mData->
            showPopupProgressSpinner(false)
            if(mData.challenge.isNotEmpty()){
                Picasso.get().load(mData.challenge[0].image).error(R.drawable.facial_hair).into(firstImg)
                firstName.text = mData.challenge[0].name
                firstSteps.text =mData.challenge[0].steps.toString()

                secName.text =mData.challenge[1].name
                secSteps.text = mData.challenge[1].steps.toString()

                thirdName.text = mData.challenge[2].name
                thirdSteps.text = mData.challenge[2].steps.toString()

                mChallengeDataList= mData.challenge as ArrayList<Challenge>
                mLeaderAdapter.swap(mChallengeDataList)
            }
        })


    }

    override fun initListeners() {
        showPopupProgressSpinner(true)
        mViewModel!!.getLeaderboard()
    }



    private fun setLeaderboardAdapter() {
        rvleaderboard.layoutManager = LinearLayoutManager(SurveysFragment.mContext)
        mLeaderAdapter = LeaderboardAdapter(SurveysFragment.mContext, mChallengeDataList,this)
        rvleaderboard.adapter = mLeaderAdapter
    }

}