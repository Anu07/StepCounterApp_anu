package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.activities.SurveyDetailActivity
import com.sd.src.stepcounterapp.adapter.SlidingImageAdapter
import com.sd.src.stepcounterapp.adapter.SurveysAdapter
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.survey.Datum
import com.sd.src.stepcounterapp.model.survey.SurveyResponse
import com.sd.src.stepcounterapp.utils.EndlessRecyclerOnScrollListener
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.viewModels.SurveyViewModel
import kotlinx.android.synthetic.main.fragment_surveys.*
import java.util.*
import kotlin.collections.ArrayList


class SurveysFragment : BaseFragment(), ItemClickGlobalListner {
    override fun onSlideItemClick(position: Int) {
        swapFragment(ImagesArray?.get(position)!!)
    }

    override fun onItemClick(pos: Int) {
        if (!surveyArray?.get(pos)!!.answered) {
            swapFragment(surveyArray?.get(pos))
        } else {
            Toast.makeText(mContext, "You have already taken this survey", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: SurveysFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): SurveysFragment {
            instance = SurveysFragment()
            mContext = context
            return instance
        }
    }

    private lateinit var mSlideAdapter: SlidingImageAdapter
    private var currentPage: Int = -1
    private var NUM_PAGES: Int = 0
    lateinit var mSurveyAdapter: SurveysAdapter
    private lateinit var mViewModel: SurveyViewModel
    private var ImagesArray: ArrayList<Datum>? = ArrayList()
    private var surveyArray: ArrayList<Datum>? = ArrayList()

    var rewardsViewPager: ViewPager? = null
    private var mData: ArrayList<Datum> = ArrayList()
    private var page = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        (mContext as LandingActivity).showDisconnection(false)
        (mContext as LandingActivity).disableSwipe(false)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_surveys, container, false)
        rewardsViewPager = v.findViewById(R.id.rewardsViewPager)
        return v

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(SurveyViewModel::class.java)
        mViewModel.getSurveyList().observe(this,
            Observer<SurveyResponse> { mData ->
                showPopupProgressSpinner(false)
                if (mData != null && (mData.data != null)) {
                    if (mData.data!!.size > 0) {
                        rvSurveys.visibility = View.VISIBLE
                        norec.visibility = View.GONE
                        surveyArray = mData.data as ArrayList<Datum>?
                        mSurveyAdapter.swap(mData.data as ArrayList<Datum>)
                        if (mData.featured.size > 0) {
                            pagerFrame.visibility = View.VISIBLE
                            ImagesArray = mData.featured as ArrayList<Datum>?
                            rewardsViewPager!!.adapter = SlidingImageAdapter(
                                ChallengesFragment.mContext,
                                (mData.featured as ArrayList<Datum>?)!!,
                                this
                            )
                            NUM_PAGES = mData.featured.size
                            spring_dots_indicator.setViewPager(rewardsViewPager)
                        }
                    } else {
                        pagerFrame.visibility = View.GONE
                    }
                } else {
                    rvSurveys.visibility = View.GONE
                    norec.visibility = View.VISIBLE
                }
            })
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            showPopupProgressSpinner(true)
            mViewModel.hitSurveyListApi(
                BasicRequest(
                    SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                    page
                )
            )
        }

        setSurveyAdapter()
        init()
//        (mContext as LandingActivity).showDisconnection(false)
    }


    private fun init() {
        spring_dots_indicator.setViewPager(rewardsViewPager)
        mSlideAdapter = SlidingImageAdapter(ChallengesFragment.mContext, ImagesArray!!, this)
        rewardsViewPager!!.adapter = mSlideAdapter

        // Auto start of viewpager
        if (ImagesArray!!.size > 0) {
            val handler = Handler()
            val Update = Runnable {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0
                }
                rewardsViewPager!!.setCurrentItem(currentPage++, true)
            }
            val swipeTimer = Timer()
            swipeTimer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(Update)
                }
            }, 3000, 5000)
        }

    }


    private fun setSurveyAdapter() {
        rvSurveys.layoutManager = LinearLayoutManager(mContext)
        mSurveyAdapter = SurveysAdapter(mContext, mData, this)
        rvSurveys.adapter = mSurveyAdapter
        rvSurveys.addOnScrollListener(object: EndlessRecyclerOnScrollListener(){
            override fun onLoadMore(currentPage: Int) {
                showPopupProgressSpinner(true)
                mViewModel.hitSurveyListApi(
                    BasicRequest(
                        SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                        currentPage
                    )
                )
            }

        })
    }


    private fun swapFragment(data: Datum?) {
        var intent = Intent(mContext, SurveyDetailActivity::class.java)
        intent.putExtra("Data", data)
        startActivity(intent)
    }

    fun notifyData() {
        if (mViewModel != null)
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
                mViewModel.hitSurveyListApi(  BasicRequest(
                    SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                    currentPage
                ))
            }
    }


}

