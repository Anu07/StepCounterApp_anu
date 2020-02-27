package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.sd.src.stepcounterapp.utils.EndlessRecyclerViewScrollListener
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.viewModels.SurveyViewModel
import kotlinx.android.synthetic.main.fragment_market_place.*
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
    var currPage: Int = 1
    private lateinit var mSlideAdapter: SlidingImageAdapter
    private var currentPage: Int = 0
    private var NUM_PAGES: Int = 0
    lateinit var mSurveyAdapter: SurveysAdapter
    private lateinit var mViewModel: SurveyViewModel
    private var ImagesArray: ArrayList<Datum>? = ArrayList()
    private var surveyArray: ArrayList<Datum>? = ArrayList()
    var handler: Handler? = null
    var Update: Runnable? = null

    var rewardsViewPager: ViewPager? = null
    private var mData: ArrayList<Datum> = ArrayList()
    private var swipeTimer: Timer? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            (activity as LandingActivity).showDisconnection(false)
        } catch (e: Exception) {
            Log.e("catching", "exception on Attach survey")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_surveys, container, false)
        rewardsViewPager = v.findViewById(R.id.rewardsViewPager)
        mViewModel = ViewModelProviders.of(activity!!).get(SurveyViewModel::class.java)
        return v

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getSurveyList().observe(this,
            Observer<SurveyResponse> { mData ->
                showPopupProgressSpinner(false)
                if (mData != null && (mData.data != null)) {
                    surveyArray = ArrayList()
                    rvSurveys.visibility = View.VISIBLE
                    norec.visibility = View.GONE
                    if (mSurveyAdapter.itemCount > 0) {
                        surveyArray?.addAll(mData.data)
                        surveyArray?.size?.let {
                            mSurveyAdapter.notifyItemRangeInserted(
                                mSurveyAdapter.itemCount,
                                it
                            )
                        }
                    } else {
                        surveyArray?.addAll(mData.data)
                        surveyArray?.let { mSurveyAdapter.swap(it) }
                    }

                    if (mData.featured.size > 0) {
                        pagerFrame.visibility = View.VISIBLE
                        ImagesArray = mData.featured as ArrayList<Datum>?
                        rewardsViewPager!!.adapter = SlidingImageAdapter(
                            mContext,
                            (mData.featured as ArrayList<Datum>?)!!,
                            this
                        )
                        NUM_PAGES = mData.featured.size
                        spring_dots_indicator.setViewPager(rewardsViewPager)
                    } else {
                        pagerFrame.visibility = View.GONE
                    }
                } else {
                    rvSurveys.visibility = View.GONE
                    norec.visibility = View.VISIBLE
                }
            })
        setSurveyAdapter()
        init()

//        (mContext as LandingActivity).showDisconnection(false)
    }


    private fun init() {
        swipeTimer = Timer()
        spring_dots_indicator.setViewPager(rewardsViewPager)
        mSlideAdapter = SlidingImageAdapter(requireContext(), ImagesArray!!, this)
        rewardsViewPager!!.adapter = mSlideAdapter

        // Auto start of viewpager
//        if (ImagesArray!!.size > 0) {
        //Timer disabled
         /*   handler = Handler()
            Update = Runnable {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0
                }
                rewardsViewPager!!.setCurrentItem(currentPage++, true)
            }
            swipeTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    handler!!.post(Update)
                }
            }, 3, 5000)*/
//        }

    }

    private fun setSurveyAdapter() {
        var mLayoutManager = LinearLayoutManager(mContext)
        rvSurveys.layoutManager = mLayoutManager
        mSurveyAdapter = SurveysAdapter(mContext, mData, this)
        rvSurveys.adapter = mSurveyAdapter
        var filterScrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                currPage = page
                if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
                    showPopupProgressSpinner(true)
                    mViewModel.hitSurveyListApi(
                        BasicRequest(
                            SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                            currPage
                        )
                    )
                }

            }
        }

        rvSurveys.addOnScrollListener(filterScrollListener)
    }


    private fun swapFragment(data: Datum?) {
        var intent = Intent(mContext, SurveyDetailActivity::class.java)
        intent.putExtra("Data", data)
        startActivity(intent)
    }

    fun notifyData() {
        if (mViewModel != null)
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
                mViewModel.hitSurveyListApi(
                    BasicRequest(
                        SharedPreferencesManager.getUserId(HayaTechApplication.applicationContext()),
                        currPage
                    )
                )
            }
    }


    override fun onResume() {
        super.onResume()
        try {
            notifyData()
            (activity as LandingActivity).showDisconnection(false)
        } catch (e: Exception) {
            Log.e("catching resume", "exception on Attach survey")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler?.removeCallbacks(Update)
        handler = null
        Update = null
        swipeTimer?.cancel()
    }

}

