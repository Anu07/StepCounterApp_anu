package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.ChallengeAdapter
import com.sd.src.stepcounterapp.adapter.ChallengeTrendingAdapter
import com.sd.src.stepcounterapp.adapter.SlidingImageAdapter
import com.sd.src.stepcounterapp.dialog.ChallengesDialog
import com.sd.src.stepcounterapp.dialog.StopChallengeDialog
import com.sd.src.stepcounterapp.model.challenge.ChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.challenge.Tranding
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.ChallengeViewModel
import kotlinx.android.synthetic.main.fragment_rewardschallenges.*
import java.util.*
import kotlin.collections.ArrayList


class ChallengesFragment : Fragment(), ChallengeAdapter.ItemClickListener {

    override fun onItemClick(pos: Int, item: Data) {
        val dialog =
            ChallengesDialog(mContext, item, R.style.pullBottomfromTop, R.layout.dialog_challenges,
                object : ChallengesDialog.StartInterface {
                    override fun onStart(data: Data) {
                        mViewModel.startChallenge(data)
                    }
                })
        dialog.show()
    }

    private lateinit var mTrendChallengeCategory: MutableList<Tranding>
    private lateinit var mTrendingChallengesAdapter: ChallengeTrendingAdapter
    private lateinit var mChallengesAdapter: ChallengeAdapter
    private lateinit var mChallengeCategory: MutableList<Data>
    private var currentPage: Int = -1
    private var NUM_PAGES: Int = 0
    private val ImagesArray: ArrayList<Int>? = ArrayList()
    lateinit var rewardsViewPager: ViewPager
    private lateinit var mViewModel: ChallengeViewModel

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: ChallengesFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        private val IMAGES = arrayOf(R.drawable.slider_img, R.drawable.slider_img, R.drawable.slider_img)

        fun newInstance(context: Context): ChallengesFragment {
            instance = ChallengesFragment()
            mContext = context
            return instance
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(ChallengeViewModel::class.java)
        mViewModel.getchallenges(BasicRequest(SharedPreferencesManager.getUserId(mContext), ""))

        mViewModel.getChallengeObject().observe(this,
            Observer<ChallengeResponse> { mChallenge ->
                if (mChallenge != null) {
                    if (mChallenge.data != null && mChallenge.tranding.size > 0) {
                        mChallengeCategory = mChallenge.data
                        setChallengeAdapter()
                    } else {
                        setChallengesView()
                    }

                    if (mChallenge.tranding != null && mChallenge.tranding.size > 0) {
                        mTrendChallengeCategory = mChallenge.tranding
                        setTrendingChallengeAdapter()
                    } else {
                        setTrendingView()
                    }
                } else {
                    txtNoChallenges.visibility = View.VISIBLE
                    challengesList.visibility = View.GONE

                    trendchallengesList.visibility = View.GONE
                    txtNoTrending.visibility = View.VISIBLE
                }
            })
        llStartChallenges.setOnClickListener {
            StopChallengeDialog(mContext, R.style.pullBottomfromTop, R.layout.dialog_stop_challenges).show()
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rewardschallenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rewardsViewPager = view.findViewById(R.id.rewardsViewPager)
        init()
    }


    private fun init() {
        spring_dots_indicator.setViewPager(rewardsViewPager)

        for (i in 0 until IMAGES.size)
            ImagesArray!!.add(IMAGES[i])

        rewardsViewPager.adapter = SlidingImageAdapter(mContext, ImagesArray!!)

        val density = resources.displayMetrics.density

        NUM_PAGES = IMAGES.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            rewardsViewPager.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)
    }


    private fun setChallengeAdapter() {
        var gridLayoutManager = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
        challengesList.layoutManager = gridLayoutManager
        mChallengesAdapter = ChallengeAdapter(mContext, mChallengeCategory, this)
        challengesList.adapter = mChallengesAdapter
        setChallengesView()
    }

    private fun setChallengesView() {
        if (mChallengeCategory.size > 0) {
            challengesList.visibility = View.VISIBLE
            txtNoChallenges.visibility = View.GONE
        } else {
            txtNoChallenges.visibility = View.VISIBLE
            challengesList.visibility = View.GONE
        }
    }

    private fun setTrendingChallengeAdapter() {
        var gridLayoutManager = GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false)
        trendchallengesList.layoutManager = gridLayoutManager
        mTrendingChallengesAdapter = ChallengeTrendingAdapter(mContext, mTrendChallengeCategory)
        trendchallengesList.adapter = mTrendingChallengesAdapter
        setTrendingView()
    }

    private fun setTrendingView() {
        if (mTrendChallengeCategory.size > 0) {
            trendchallengesList.visibility = View.VISIBLE
            txtNoTrending.visibility = View.GONE
        } else {
            trendchallengesList.visibility = View.GONE
            txtNoTrending.visibility = View.VISIBLE
        }
    }

}
