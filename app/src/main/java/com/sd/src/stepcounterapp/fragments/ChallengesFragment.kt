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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LeaderboardActivity
import com.sd.src.stepcounterapp.adapter.ChallengeAdapter
import com.sd.src.stepcounterapp.adapter.ChallengeTrendingAdapter
import com.sd.src.stepcounterapp.adapter.SlidingFeaturedChallengeImageAdapter
import com.sd.src.stepcounterapp.dialog.ChallengesDialog
import com.sd.src.stepcounterapp.dialog.StopChallengeDialog
import com.sd.src.stepcounterapp.model.challenge.*
import com.sd.src.stepcounterapp.model.challenge.ChallengeTakenResponse.StartChallengeResponse
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.ChallengeViewModel
import kotlinx.android.synthetic.main.fragment_rewardschallenges.*
import java.util.*
import kotlin.collections.ArrayList


class ChallengesFragment : Fragment(), ChallengeAdapter.ItemClickListener ,SlidingFeaturedChallengeImageAdapter.ItemSlideListener,
    ChallengeTrendingAdapter.ItemTrendClickListener{
    override fun onImageSlider(position: Int, img: Data) {
        Challengedialog = ChallengesDialog(mContext, img, R.style.pullBottomfromTop, R.layout.dialog_challenges,
            object : ChallengesDialog.StartInterface {
                override fun onStart(img: Data) {
                    mViewModel.startChallenge(img)
                }
            })
        Challengedialog!!.show()
    }

    override fun onTrendItemClick(pos: Int, item: Data) {
        Challengedialog = ChallengesDialog(mContext, mChallengeCategory[pos], R.style.pullBottomfromTop, R.layout.dialog_challenges,
            object : ChallengesDialog.StartInterface {
                override fun onStart(data: Data) {
                    mViewModel.startChallenge(data)
                }
            })
        Challengedialog!!.show()

    }

    override fun onItemClick(pos: Int, item: Data) {

        Challengedialog = ChallengesDialog(mContext, mChallengeCategory[pos], R.style.pullBottomfromTop, R.layout.dialog_challenges,
            object : ChallengesDialog.StartInterface {
                override fun onStart(data: Data) {
                    mViewModel.startChallenge(data)
                }
            })
        Challengedialog!!.show()
    }

    private fun sendRequestObject(data: Data): Challenge {
        return Challenge(
            data.image,
            data.is_active,
            data.description,
            data.steps,
            data.points,
            data.duration,
            data.bonusPoint3,
            data.bonusPoint2,
            data.bonusPoint1,
            data.startDateTime,
            data.is_deleted,
            data.__v,
            data.adminId,
            data.name,
            data.shortDesc,
            data._id,
            data.department,
            data.updatedAt
        )
    }

    private var mFeaturedChallengeCategory: MutableList<Data> = mutableListOf()
    private lateinit var mTrendChallengeCategory: MutableList<Data>
    var mActiveList: MutableList<Data> = mutableListOf()
    private lateinit var mTrendingChallengesAdapter: ChallengeTrendingAdapter
    private lateinit var mChallengesAdapter: ChallengeAdapter
    private lateinit var mChallengeCategory: MutableList<Data>
    private var currentPage: Int = -1
    private var NUM_PAGES: Int = 0
    private val ImagesArray: ArrayList<Int>? = ArrayList()
    lateinit var rewardsViewPager: ViewPager
    private lateinit var mViewModel: ChallengeViewModel
    var dialog: ChallengesDialog? = null
    lateinit var Challengedialog: ChallengesDialog
    lateinit var stopChallengedialog: StopChallengeDialog

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: ChallengesFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
        val INTENT_PARAM = "Challenge"
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

                    if (mChallenge.data != null && mChallenge.trending.size > 0) {
                        mChallengeCategory = mChallenge.data
                        setChallengeAdapter()
                        mActiveList = mChallenge.ongoing
                        if (mActiveList.isNotEmpty()) {
                            showOngoingChallenge(mActiveList)
                        } else {
                            llStartChallenges.visibility = View.GONE
                        }
                    } else {
                        setChallengesView()
                    }

                    if (mChallenge.featured != null && mChallenge.featured.size > 0) {
                        mFeaturedChallengeCategory = mChallenge.featured
                        setFeaturedChallengeSliderAdapter(mFeaturedChallengeCategory)
                    }

                    if (mChallenge.trending != null && mChallenge.trending.size > 0) {
                        mTrendChallengeCategory = mChallenge.trending
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

        mViewModel.getStartChallengeObject().observe(this, Observer<StartChallengeResponse> { mData ->
            if(Challengedialog!=null && Challengedialog!!.isShowing){
                Challengedialog!!.dismiss()
            }
            if (mData!=null && mData.status == 200) {
                mViewModel.getchallenges(BasicRequest(SharedPreferencesManager.getUserId(mContext), ""))
            }else if(mData==null){

                Toast.makeText(mContext,"There is another challenge already started.",Toast.LENGTH_LONG).show()
            }
        })

        mViewModel.getStopChallengeObject().observe(this, Observer<BasicInfoResponse> { mData ->
            if(stopChallengedialog!=null && stopChallengedialog!!.isShowing){
                stopChallengedialog!!.dismiss()
            }
            if (mData!=null && mData.status == 200) {
                Toast.makeText(mContext,"This challenge has been stopped.",Toast.LENGTH_LONG).show()
                mViewModel.getchallenges(BasicRequest(SharedPreferencesManager.getUserId(mContext), ""))
            }
        })
        llStartChallenges.setOnClickListener {
            stopChallengedialog= StopChallengeDialog(mContext, R.style.pullBottomfromTop, R.layout.dialog_stop_challenges,mActiveList[0],
                object : StopChallengeDialog.StopInterface {
                    override fun onStop(data: Data) {
                        mViewModel.stopchallenges(mActiveList[0]._id)
                    }
            })
            stopChallengedialog.show()
        }

        leaderboardView.setOnClickListener {
            startActivity(Intent(mContext, LeaderboardActivity::class.java).putExtra(INTENT_PARAM,1))
        }

    }

    private fun showOngoingChallenge(mActiveList: MutableList<Data>) {
        llStartChallenges.visibility = View.VISIBLE
        ongoingchallengeName.text = mActiveList[0].name
        ongoingChallengeDetail.text = "Duration: " + mActiveList[0].endDateTime.split("T")[0]
        /*stopchallengeBttn.setOnClickListener {
            mViewModel.stopchallenges(mActiveList[0]._id)
        }*/
    }

    private fun setFeaturedChallengeSliderAdapter(mFeaturedNewChallengeCategory: MutableList<Data>) {

        Log.i("new ", "list" + mFeaturedNewChallengeCategory.size)
        rewardsViewPager.adapter = SlidingFeaturedChallengeImageAdapter(mContext, mFeaturedNewChallengeCategory,this)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rewardschallenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rewardsViewPager = view.findViewById(R.id.rewardsViewPager)
        leaderBttn.setOnClickListener {
            startActivity(Intent(mContext, LeaderboardActivity::class.java))
        }

        init()
    }


    private fun init() {
        spring_dots_indicator.setViewPager(rewardsViewPager)

        for (i in 0 until IMAGES.size)
            ImagesArray!!.add(IMAGES[i])

        rewardsViewPager.adapter = SlidingFeaturedChallengeImageAdapter(mContext, mFeaturedChallengeCategory,this)


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
        mTrendingChallengesAdapter = ChallengeTrendingAdapter(mContext, mTrendChallengeCategory,this)
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
