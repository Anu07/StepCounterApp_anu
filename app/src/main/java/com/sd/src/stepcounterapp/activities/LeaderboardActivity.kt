package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.LeaderboardAdapter
import com.sd.src.stepcounterapp.fragments.ChallengesFragment.Companion.INTENT_PARAM
import com.sd.src.stepcounterapp.fragments.SurveysFragment
import com.sd.src.stepcounterapp.model.leaderboard.Data
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SpinnerInteractionListener
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.LeaderboardViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_leaderboard.*
import kotlinx.android.synthetic.main.crosstitlebar.*


class LeaderboardActivity : BaseActivity<LeaderboardViewModel>(), ItemClickGlobalListner,
    SpinnerInteractionListener.ClickSpinnerItem {
    override fun onSpinSelected(pos: Int) {
        mViewModel!!.getLeaderboard(
            LeaderBoardRequest(
                pos.toString(),
                SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
            )
        )
    }

    override fun onSlideItemClick(position: Int) {

    }

    override fun onItemClick(pos: Int) {

    }

    private var mainList: ArrayList<Data>? = ArrayList()
    private var mChallengeDataList: ArrayList<Data> = ArrayList()
    private lateinit var mLeaderAdapter: LeaderboardAdapter
    override val layoutId: Int
        get() = R.layout.activity_leaderboard

    override val viewModel: LeaderboardViewModel
        get() = ViewModelProviders.of(this,
            BaseViewModelFactory { LeaderboardViewModel(application) }).get(LeaderboardViewModel::class.java)

    override val context: Context
        get() = this@LeaderboardActivity
    private var userIsInteracting: Boolean = false
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate() {
        setLeaderboardAdapter()
        var leaderselection: Spinner = findViewById(R.id.leaderselection)
        adapter = ArrayAdapter(this, R.layout.spinner_item, addEntriesToDropDown())
        leaderselection.adapter = adapter
        if (intent.hasExtra(INTENT_PARAM)) {
            ag_steps_msg.visibility = View.GONE
            leaderselection.setSelection(1)
            showPopupProgressSpinner(true)
            mViewModel!!.getLeaderboard(
                LeaderBoardRequest(
                    "1",                // 0 and 1 are also supported as general and challeneg
                    SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
                )
            )
        } else {
            ag_steps_msg.visibility = View.VISIBLE
            leaderselection.setSelection(0)
            showPopupProgressSpinner(true)
            mViewModel!!.getLeaderboard(
                LeaderBoardRequest(
                    "0",
                    SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
                )
            )
        }

        mViewModel!!.getLeaderboardResponse().observe(this, Observer { mData ->
            showPopupProgressSpinner(false)
            if (mData != null) {
                if(leaderselection.selectedItemPosition == 1){
                    ag_steps_msg.visibility = View.GONE
                }else{
                    ag_steps_msg.visibility = View.VISIBLE
                }
                if (mData.data != null && mData.data[0].name != null) {
                    if(mData.data.size>3){
                        rvleaderboard.visibility = View.VISIBLE
                    }else{
                        rvleaderboard.visibility = View.INVISIBLE
                    }
                    mainList = mData.data as ArrayList<Data>?
                    firstLayout.visibility = View.VISIBLE
                    Picasso.get().load(RetrofitClient.IMG_URL + mData.data[0].image).error(R.drawable.nouser).placeholder(R.drawable.nouser)
                        .into(firstImg)
                    firstName.text = mData.data[0].name
                    firstSteps.text = mData.data[0].steps.toString()+" Steps"
                    if (mData.data.size >= 2) {
                        if (mData.data.size >= 3) {
                            ThirdPosition.visibility = View.VISIBLE
                            thirdName.text = mData.data[2].name
                            thirdSteps.text = mData.data[2].steps.toString()+" Steps"
                        } else {
                            ThirdPosition.visibility = View.VISIBLE
                            thirdName.text = "NA"
                            thirdSteps.text = "0 steps"
                        }


                        secondPosition.visibility = View.VISIBLE
                        secName.text = mData.data[1].name
                        secSteps.text = mData.data[1].steps.toString()+" Steps"
                    }

                    if (mainList!!.size > 2) {
                        if(mainList!!.size>4){
                            mChallengeDataList = ArrayList(mainList?.subList(3, mainList!!.size))
                        }else if(mainList!!.size>3){
                            mChallengeDataList.add(mainList!![3])
                        }
                        mLeaderAdapter.swap(mChallengeDataList)
                    }

                    if(mainList!!.size ==1){
                        secondPosition.visibility = View.VISIBLE
                        secName.text = "NA"
                        secSteps.text = "0 steps"
                        ThirdPosition.visibility = View.VISIBLE
                        thirdName.text = "NA"
                        thirdSteps.text = "0 steps"
                    }

                } else {
                    Toast.makeText(this@LeaderboardActivity, "You have not taken any challenge.", Toast.LENGTH_LONG).show()
                    mLeaderAdapter.swap(ArrayList())
                    firstImg.setImageResource(R.drawable.nouser)
                    firstName.text ="NA"
                    firstSteps.text ="0 steps"
                    secondPosition.visibility = View.INVISIBLE
                    ThirdPosition.visibility = View.INVISIBLE
                }

            }


        })


    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        userIsInteracting = true
    }


    private fun addEntriesToDropDown(): Array<String>? {
        return resources.getStringArray(R.array.leaderArray)
    }


    override fun initListeners() {

        titleTxt.visibility = View.VISIBLE
        txt_title.visibility = View.GONE
        val listener = SpinnerInteractionListener(this)
        leaderselection.setOnTouchListener(listener)
        leaderselection.onItemSelectedListener = listener

        img_back.setOnClickListener {
            onBackPressed()
        }


    }


    private fun setLeaderboardAdapter() {
        rvleaderboard.layoutManager = LinearLayoutManager(SurveysFragment.mContext)
        mLeaderAdapter = LeaderboardAdapter(SurveysFragment.mContext, mChallengeDataList, this)
        rvleaderboard.adapter = mLeaderAdapter

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}