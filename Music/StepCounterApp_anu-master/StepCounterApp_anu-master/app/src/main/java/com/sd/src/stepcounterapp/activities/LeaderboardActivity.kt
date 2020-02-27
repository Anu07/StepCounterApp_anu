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
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.AppConstants.INTENT_CHALLENGETKN
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.LeaderboardAdapter
import com.sd.src.stepcounterapp.adapter.LeaderboardDepartmentAdapter
import com.sd.src.stepcounterapp.fragments.ChallengesFragment.Companion.INTENT_MULTI
import com.sd.src.stepcounterapp.fragments.ChallengesFragment.Companion.INTENT_PARAM
import com.sd.src.stepcounterapp.fragments.HayatechFragment.Companion.INTENT_HOME
import com.sd.src.stepcounterapp.model.challenge.Ongoing
import com.sd.src.stepcounterapp.model.challenge.departmentchallengeresponse.Datum
import com.sd.src.stepcounterapp.model.challenge.departmentchallengeresponse.DepartmentLeaderboardResponse
import com.sd.src.stepcounterapp.model.leaderboard.Data
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardRequest
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardTknRequest
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SpinnerInteractionListener
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.LeaderboardViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_leaderboard.*
import kotlinx.android.synthetic.main.crosstitlebar.*
import java.util.*
import kotlin.collections.ArrayList




class LeaderboardActivity : BaseActivity<LeaderboardViewModel>(), ItemClickGlobalListner,
    SpinnerInteractionListener.ClickSpinnerItem {
    override fun onSpinSelected(pos: Int) {
        if (pos == 1) {
            when {
                intent.hasExtra(INTENT_PARAM) -> {
                    mViewModel!!.getLeaderboard(
                        LeaderBoardRequest(
                            pos.toString(),
                            SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
                        )
                    )
                }
                intent.hasExtra(INTENT_MULTI) -> {
                    var multiDept: Ongoing = intent.getParcelableExtra(INTENT_MULTI)
                    mViewModel!!.getMultiLeaderboard(multiDept._id)
                }
                else -> {
                    noChallengeView()
                }
            }

        } else {
            mViewModel!!.getLeaderboard(
                LeaderBoardRequest(
                    pos.toString(),
                    SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
                )
            )

        }
    }

    private fun noChallengeView() {
        Toast.makeText(
            this@LeaderboardActivity,
            "You have not taken any challenge.",
            Toast.LENGTH_LONG
        )
            .show()
        mLeaderAdapter.swap(ArrayList())
        firstImg.setImageResource(R.drawable.nouser)
        firstName.text = "NA"
        firstSteps.text = "0 steps"
        secondPosition.visibility = View.INVISIBLE
        ThirdPosition.visibility = View.INVISIBLE
    }

    override fun onSlideItemClick(position: Int) {

    }

    override fun onItemClick(pos: Int) {

    }

    private var mainList: ArrayList<Data>? = ArrayList()
    private var mChallengeDataList: ArrayList<Data> = ArrayList()
    private var mDeptChallengeDataList: ArrayList<Datum> = ArrayList()
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
        when {
            intent.hasExtra(INTENT_HOME) -> {
                leaderFrame.visibility = View.VISIBLE
                ag_steps_msg.visibility = View.VISIBLE
                adapter = ArrayAdapter(this, R.layout.spinner_item, addEntryToDropDown())
                leaderselection.adapter = adapter
                leaderselection.setSelection(0)
                mViewModel!!.getLeaderboard(
                    LeaderBoardRequest(
                        "0",                // 0 and 1 are also supported as general and challeneg
                        SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
                    )
                )
                /*leaderselection.setOnTouchListener(null)
                leaderselection.onItemSelectedListener = null*/
            }
            intent.hasExtra(INTENT_MULTI)->{
                leaderFrame.visibility = View.VISIBLE
                ag_steps_msg.visibility = View.GONE
                leaderselection.setSelection(1)
                showPopupProgressSpinner(true)
                var challengeObj: Ongoing = intent.getParcelableExtra(INTENT_MULTI)
                if (challengeObj.invitationType == "multiDepartments") {
                    mViewModel!!.getMultiLeaderboard(challengeObj._id)
                } else {
                    mViewModel!!.getLeaderboard(
                        LeaderBoardRequest(
                            "1",                // 0 and 1 are also supported as general and challeneg
                            SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
                        )
                    )
                }
            }
            intent.hasExtra(INTENT_PARAM) -> {
                leaderFrame.visibility = View.VISIBLE
                ag_steps_msg.visibility = View.GONE
                leaderselection.setSelection(1)
                showPopupProgressSpinner(true)
                var challengeObj: Ongoing = intent.getParcelableExtra(INTENT_PARAM)
                if (challengeObj.invitationType == "multiDepartments") {
                    mViewModel!!.getMultiLeaderboard(challengeObj._id)
                } else {
                    mViewModel!!.getLeaderboard(
                        LeaderBoardRequest(
                            "1",                // 0 and 1 are also supported as general and challeneg
                            SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext())
                        )
                    )
                }
            }
            intent.hasExtra(INTENT_CHALLENGETKN) -> {
                leaderFrame.visibility = View.GONE
                ag_steps_msg.visibility = View.GONE
                leaderselection.setSelection(1)
                showPopupProgressSpinner(true)
                mViewModel!!.getLeaderboard(
                    LeaderBoardTknRequest(
                        SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                        intent.getStringExtra(INTENT_CHALLENGETKN)
                    )
                )
            }
            else -> {
                leaderFrame.visibility = View.VISIBLE
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
        }

        mViewModel!!.getDeptLeaderboardResponse().observe(this, Observer { mData ->
            showPopupProgressSpinner(false)
            if (mData != null && mData.data!=null) {
                setAvgStepsTextVisibility(leaderselection)
                var mNewData = calculateRankforDepartments(mData)
                Collections.reverse(mNewData)
                if (mNewData != null && mNewData[0].departmentName != null) {
                    if (mNewData.size > 3) {
                        rvleaderboard.visibility = View.VISIBLE
                    } else {
                        rvleaderboard.visibility = View.INVISIBLE
                    }
                    topRankersLayout.visibility = View.VISIBLE
                    firstLayout.visibility = View.VISIBLE
                    firstName.text = mNewData[0].departmentName
                    firstSteps.text = mNewData[0].steps.toString() + " Steps"
                    firstImg.setImageResource(R.drawable.circle_bg_trans)
                    firstImg.visibility = View.GONE
                    firstChallengeImg.visibility = View.VISIBLE
                    if (mNewData.size >= 2) {
                        if (mNewData.size >= 3) {
                            ThirdPosition.visibility = View.VISIBLE
                            thirdName.text = mNewData[2].departmentName
                            thirdSteps.text = mNewData[2].steps.toString() + " Steps"
                        } else {
                            ThirdPosition.visibility = View.VISIBLE
                            thirdName.text = "NA"
                            thirdSteps.text = "0 steps"
                        }
                        secondPosition.visibility = View.VISIBLE
                        secName.text = mNewData[1].departmentName
                        secSteps.text = mNewData[1].steps.toString() + " Steps"
                    }
                    if (mNewData!!.size > 2) {
                        if (mNewData!!.size == 3) {
                            mDeptChallengeDataList.add(mNewData!![2])
                        } else if (mNewData!!.size >= 4) {
                            mDeptChallengeDataList = ArrayList(mNewData?.subList(3, mNewData!!.size))
                        }
                        var mLeaderDeptAdapter = LeaderboardDepartmentAdapter(mContext, mDeptChallengeDataList, this)
                        rvleaderboard.adapter = mLeaderDeptAdapter
                    }

                    if (mNewData!!.size == 1) {
                        secondPosition.visibility = View.VISIBLE
                        secName.text = "NA"
                        secSteps.text = "0 steps"
                        ThirdPosition.visibility = View.VISIBLE
                        thirdName.text = "NA"
                        thirdSteps.text = "0 steps"
                    }

                } else {
                    if (mData.message == "weekendChallengeLeaderboard") {
                        topRankersLayout.visibility = View.INVISIBLE
                        Toast.makeText(
                            this@LeaderboardActivity,
                            "There is no leaderboard for a weekend challenge.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else {
                        if (leaderselection.selectedItemPosition == 1) {

                            Toast.makeText(
                                this@LeaderboardActivity,
                                "You have not taken any challenge.",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        mLeaderAdapter.swap(ArrayList())
                        firstImg.setImageResource(R.drawable.nouser)
                        firstName.text = "NA"
                        firstSteps.text = "0 steps"
                        secondPosition.visibility = View.INVISIBLE
                        ThirdPosition.visibility = View.INVISIBLE
                    }

                }

            }
        })



        mViewModel!!.getLeaderboardResponse().observe(this, Observer { mData ->
            showPopupProgressSpinner(false)
            if (mData != null && mData.data!=null) {
                setAvgStepsTextVisibility(leaderselection)
                if (mData.data != null && mData.data[0].name != null) {
                    if (mData.data.size > 3) {
                        rvleaderboard.visibility = View.VISIBLE
                    } else {
                        rvleaderboard.visibility = View.INVISIBLE
                    }
                    topRankersLayout.visibility = View.VISIBLE
                    mainList = mData.data as ArrayList<Data>?
                    firstImg.visibility = View.VISIBLE
                    firstChallengeImg.visibility = View.GONE
                    firstLayout.visibility = View.VISIBLE
                    Picasso.get().load(IMG_URL + mData.data[0].image)
                        .error(R.drawable.nouser)
                        .placeholder(R.drawable.nouser)
                        .into(firstImg)
                    firstName.text = mData.data[0].name
                    firstSteps.text = mData.data[0].steps.toString() + " Steps"
                    if (mData.data.size >= 2) {
                        if (mData.data.size >= 3) {
                            ThirdPosition.visibility = View.VISIBLE
                            thirdName.text = mData.data[2].name
                            thirdSteps.text = mData.data[2].steps.toString() + " Steps"
                        } else {
                            ThirdPosition.visibility = View.VISIBLE
                            thirdName.text = "NA"
                            thirdSteps.text = "0 steps"
                        }


                        secondPosition.visibility = View.VISIBLE
                        secName.text = mData.data[1].name
                        secSteps.text = mData.data[1].steps.toString() + " Steps"
                    }

                    if (mainList!!.size > 2) {
                        if (mainList!!.size == 3) {
                            mChallengeDataList.add(mainList!![2])
                        } else if (mainList!!.size >= 4) {
                            mChallengeDataList = ArrayList(mainList?.subList(3, mainList!!.size))
                        }
                        mLeaderAdapter.swap(mChallengeDataList)
                        rvleaderboard.adapter = mLeaderAdapter
                    }

                    if (mainList!!.size == 1) {
                        secondPosition.visibility = View.VISIBLE
                        secName.text = "NA"
                        secSteps.text = "0 steps"
                        ThirdPosition.visibility = View.VISIBLE
                        thirdName.text = "NA"
                        thirdSteps.text = "0 steps"
                    }

                } else {

                    if (leaderselection.selectedItemPosition == 1) {

                        noChallengeView()
                    }
                }


            }


        })


    }

    /**
     * sorting ranks for departments
     * when all dept rank is 0, then sort by steps
     * if a dept rank is 1, then sort others by steps except dept rank 1
     * else if dept rank has different values then sort it accordignly.
     */

    private fun calculateRankforDepartments(mData: DepartmentLeaderboardResponse?): List<Datum> {
        return when {
            checkIfRankAllZero(mData) -> {
                mData!!.data.sortedWith(compareBy { it.steps})
            }
            /*checkIfRankAllOneZero(mData) ==1 -> {
                    //TODO
                }*/
            else -> mData!!.data.sortedWith(compareBy { it.departmentRank})
        }
    }


    /**
     * check if all the deptt rank is non-zero
     */
    private fun checkIfRankAllOneZero(mData: DepartmentLeaderboardResponse?): Int {

        if (mData?.data != null) {
            Log.e("frequency", "of 1" + Collections.frequency(mData.data, 1))
        }
        return 1
    }


    /**
     * check if all the deptt rank is zero
     */

    private fun checkIfRankAllZero(mData: DepartmentLeaderboardResponse?): Boolean {
        mData?.data?.forEach {
            return it.departmentRank == 0
        }
        return false
    }

    private fun setAvgStepsTextVisibility(leaderselection: Spinner) {
        if (leaderselection.selectedItemPosition == 1) {
            ag_steps_msg.visibility = View.GONE
        } else {
            ag_steps_msg.visibility = View.VISIBLE
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        userIsInteracting = true
    }


    private fun addEntriesToDropDown(): Array<String>? {
        return resources.getStringArray(R.array.leaderArray)
    }

    private fun addEntryToDropDown(): Array<String>? {
        return resources.getStringArray(R.array.leaderSingleArray)
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
        rvleaderboard.layoutManager = LinearLayoutManager(mContext)
        mLeaderAdapter = LeaderboardAdapter(mContext, mChallengeDataList, this)
        rvleaderboard.adapter = mLeaderAdapter

    }

}