package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.SurveyDetailActivity
import com.sd.src.stepcounterapp.adapter.SlidingImageAdapter
import com.sd.src.stepcounterapp.adapter.SurveysAdapter
import com.sd.src.stepcounterapp.model.survey.Data
import com.sd.src.stepcounterapp.model.survey.SurveyListResponse
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner
import com.sd.src.stepcounterapp.viewModels.SurveyViewModel
import kotlinx.android.synthetic.main.fragment_rewardschallenges.*
import kotlinx.android.synthetic.main.fragment_surveys.*
import kotlinx.android.synthetic.main.fragment_surveys.rewardsViewPager
import kotlinx.android.synthetic.main.fragment_surveys.spring_dots_indicator
import java.util.*
import kotlin.collections.ArrayList


class SurveysFragment : Fragment(),ItemClickGlobalListner {
    override fun onItemClick(pos: Int) {
        swapFragment(mData[pos])
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

    private var currentPage: Int = -1
    private var NUM_PAGES: Int = 3
    lateinit var mSurveyAdapter: SurveysAdapter
    private lateinit var mViewModel: SurveyViewModel
    private val ImagesArray: ArrayList<Int>? = ArrayList()
    var rewardsViewPager: ViewPager? = null
    private var mData: ArrayList<Data> = ArrayList()
    private val IMAGES = arrayOf(R.drawable.slider_img, R.drawable.slider_img, R.drawable.slider_img)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =inflater.inflate(R.layout.fragment_surveys, container, false)
        rewardsViewPager = v.findViewById<ViewPager>(R.id.rewardsViewPager)
        return v

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(SurveyViewModel::class.java)
        mViewModel.getSurveyList().observe(this,
            Observer<SurveyListResponse> { mData ->
                if (mData != null) {
                    if (mData.data!!.size > 0) {
                        this.mData.addAll(mData.data)
                        rvSurveys.visibility = View.VISIBLE
                        norec.visibility = View.GONE
                        mSurveyAdapter.swap(mData.data)
                    }
                } else {
                    rvSurveys.visibility = View.GONE
                    norec.visibility = View.VISIBLE
                }
            })

        mViewModel.hitSurveyListApi()

        setSurveyAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }


    private fun init() {
        spring_dots_indicator.setViewPager(rewardsViewPager)

        for (i in 0 until IMAGES.size)
            ImagesArray!!.add(IMAGES[i])

        rewardsViewPager!!.adapter = SlidingImageAdapter(ChallengesFragment.mContext, ImagesArray!!)

        val density = resources.displayMetrics.density


        // Auto start of viewpager
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
        }, 3000, 3000)
    }


    private fun setSurveyAdapter() {
        rvSurveys.layoutManager = LinearLayoutManager(mContext)
        mSurveyAdapter = SurveysAdapter(mContext, mData,this)
        rvSurveys.adapter = mSurveyAdapter
    }


    private fun swapFragment(data: Data) {
     var intent = Intent(mContext, SurveyDetailActivity::class.java)
        intent.putExtra("Data",data)
        startActivity(intent)
    }

}

