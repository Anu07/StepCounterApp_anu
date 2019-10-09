package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.sd.src.stepcounterapp.*
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.activities.LeaderboardActivity
import com.sd.src.stepcounterapp.adapter.ChallengeAdapter
import com.sd.src.stepcounterapp.adapter.ChallengeTrendingAdapter
import com.sd.src.stepcounterapp.adapter.SlidingFeaturedChallengeImageAdapter
import com.sd.src.stepcounterapp.dialog.ChallengesDialog
import com.sd.src.stepcounterapp.dialog.StopChallengeDialog
import com.sd.src.stepcounterapp.model.challenge.Challenge
import com.sd.src.stepcounterapp.model.challenge.ChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.ChallengeTakenResponse.StartChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SpacesItemDecoration
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.viewModels.ChallengeViewModel
import kotlinx.android.synthetic.main.fragment_rewardschallenges.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChallengesFragment : BaseFragment(), ChallengeAdapter.ItemClickListener,
    SlidingFeaturedChallengeImageAdapter.ItemSlideListener,
    ChallengeTrendingAdapter.ItemTrendClickListener {
    override fun onImageSlider(position: Int, img: Data) {
        Challengedialog = ChallengesDialog(mContext,
            img,
            R.style.pullBottomfromTop,
            R.layout.dialog_challenges,
            false,
            mActiveList.isNotEmpty(),
            object : ChallengesDialog.StartInterface {
                override fun onStop(mData: Data) {
                    if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                        mViewModel.stopchallenges(mData._id)
                    }
                }

                override fun onStart(img: Data) {
                    if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                        showPopupProgressSpinner(true)
                        mViewModel.startChallenge(img)
                    }
                }
            })
        Challengedialog.show()
    }

    override fun onTrendItemClick(pos: Int, item: Data) {
        Challengedialog =
            ChallengesDialog(mContext,
                mChallengeCategory[pos],
                R.style.pullBottomfromTop,
                R.layout.dialog_challenges,
                false,
                mActiveList.isNotEmpty(),
                object : ChallengesDialog.StartInterface {
                    override fun onStop(mData: Data) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            mViewModel.stopchallenges(mData._id)
                        }
                    }

                    override fun onStart(data: Data) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            showPopupProgressSpinner(true)
                            mViewModel.startChallenge(data)
                        }
                    }
                })
        Challengedialog.show()

    }

    override fun onItemClick(pos: Int, item: Data) {

        Challengedialog =
            ChallengesDialog(mContext,
                mChallengeCategory[pos],
                R.style.pullBottomfromTop,
                R.layout.dialog_challenges,
                false,
                mActiveList.isNotEmpty(),
                object : ChallengesDialog.StartInterface {
                    override fun onStop(mData: Data) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            mViewModel.stopchallenges(mData._id)
                        }
                    }

                    override fun onStart(data: Data) {
                        if (checkChallengeValidity(data.startDateTime) >= 0) {
                            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                                showPopupProgressSpinner(true)
                                mViewModel.startChallenge(data)
                            }
                        } else {
                            Toast.makeText(
                                mContext,
                                "This challenge has not started yet.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                })
        Challengedialog.show()
    }

    private fun checkChallengeValidity(startDate: String): Int {

        var startDateformatted = changeDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd", startDate
        ) + " " + convertToLocal(startDate)
        Log.i("Date", "formatted$startDateformatted")
        var format = SimpleDateFormat("yyyy-MM-dd hh:mm a")
        return format.format(getCurrentDate()).compareTo(startDateformatted)
    }

    fun getCurrentDate(): Date {
        return Date(System.currentTimeMillis())
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
    private var mTrendChallengeCategory: MutableList<Data> = mutableListOf()
    var mActiveList: MutableList<Data> = mutableListOf()
    private var mTrendingChallengesAdapter: ChallengeTrendingAdapter? = null
    private var mChallengesAdapter: ChallengeAdapter? = null
    private var mChallengeCategory: MutableList<Data> = mutableListOf()
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
        private val IMAGES =
            arrayOf(R.drawable.slider_img, R.drawable.slider_img, R.drawable.slider_img)

        fun newInstance(context: Context): ChallengesFragment {
            instance = ChallengesFragment()
            mContext = context
            return instance
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            (activity as LandingActivity).showDisconnection(false)
            (activity as LandingActivity).disableSwipe(false)
        } catch (e: Exception) {
        }
    }

    private fun showOngoingChallenge(mActiveList: MutableList<Data>) {
        llStartChallenges.visibility = View.VISIBLE
        ongoingchallengeName.isSelected = true
        ongoingchallengeName.text = mActiveList[0].name.capitalize()
        ongoingChallengeDetail.text = "End Date: " + changeDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "dd MMM, yyyy",
            mActiveList[0].endDateTime
        ) + " | " + convertToLocal(mActiveList[0].endDateTime)

        var days = "${getDaysDifference(
            SimpleDateFormat("dd/MM/yyyy").format(getCurrentDate()),
            changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd/MM/yyyy", mActiveList[0].endDateTime)
        )}"
        if (days == "0") {
            daysLeft.text = "Today"
        } else {
            daysLeft.text = "$days Days"
        }

        if (mActiveList[0].is_completed) {
            progressTxt.text = "Completed"
        } else {
            progressTxt.text = "In Progress: "
        }

        /* stopchallengeBttn.setOnClickListener {
             mViewModel.stopchallenges(mActiveList[0]._id)
         }*/
    }

    private fun setFeaturedChallengeSliderAdapter(mFeaturedNewChallengeCategory: MutableList<Data>) {

        Log.i("new ", "list" + mFeaturedNewChallengeCategory.size)
        rewardsViewPager.adapter =
            SlidingFeaturedChallengeImageAdapter(mContext, mFeaturedNewChallengeCategory, this)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (mContext as LandingActivity).disableSwipe(true)
        mViewModel = ViewModelProviders.of(activity!!).get(ChallengeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_rewardschallenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rewardsViewPager = view.findViewById(R.id.rewardsViewPager)
        leaderBttn.setOnClickListener {
            startActivity(
                Intent(mContext, LeaderboardActivity::class.java).putExtra(
                    INTENT_PARAM,
                    1
                )
            )
        }

        stopchallengeBttn.setOnClickListener {
            stopChallengedialog = StopChallengeDialog(mContext,
                R.style.pullBottomfromTop,
                R.layout.dialog_stop_challenges,
                mActiveList[0],
                object : StopChallengeDialog.StopInterface {
                    override fun onStop(data: Data) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            mViewModel.stopchallenges(mActiveList[0]._id)
                        }
                    }
                })
            stopChallengedialog.show()
        }

        leaderboardView.setOnClickListener {
            startActivity(
                Intent(mContext, LeaderboardActivity::class.java).putExtra(
                    INTENT_PARAM,
                    1
                )
            )
        }


        ongoingchallengeName.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
//                if(motionEvent.x >= (ongoingchallengeName.right - ongoingchallengeName.compoundDrawables[2].bounds.width())) {
                Challengedialog = ChallengesDialog(mContext,
                    mActiveList[0],
                    R.style.pullBottomfromTop,
                    R.layout.dialog_challenges,
                    true,
                    false, object : ChallengesDialog.StartInterface {
                        override fun onStop(mData: Data) {
                            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                                mViewModel.stopchallenges(mData._id)
                            }
                        }

                        override fun onStart(img: Data) {
//                                mViewModel.startChallenge(img)
                        }
                    })
                Challengedialog.show()
//                }
            }

            false

        }
        init()
        viewModelSetUp()

//        (mContext as LandingActivity).showDisconnection(false)
    }

    private fun viewModelSetUp() {


        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            showPopupProgressSpinner(true)
            mViewModel.getchallenges(BasicRequest(SharedPreferencesManager.getUserId(mContext), 0))
        }
        (mContext as LandingActivity).disableSwipe(true)

        mViewModel.getChallengeObject().observe(this,
            Observer<ChallengeResponse> { mChallenge ->
                if (mChallenge != null) {
                    showPopupProgressSpinner(false)
                    if (mChallenge.data != null) {
                        mChallengeCategory = mChallenge.data
//                        setChallengeAdapter()
                        if (mChallengesAdapter != null) {
                            setChallengeAdapter()
                        } else {
                            mChallengesAdapter!!.swap(mChallengeCategory as ArrayList<Data>)
                        }
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
                        pagerParent.visibility = View.VISIBLE
                        mFeaturedChallengeCategory = mChallenge.featured
                        setFeaturedChallengeSliderAdapter(mFeaturedChallengeCategory)
                    } else {
                        pagerParent.visibility = View.GONE
                    }

                    if (mChallenge.trending != null && mChallenge.trending.size > 0) {
                        mTrendChallengeCategory = mChallenge.trending
//                        setTrendingChallengeAdapter()
                        if (mTrendingChallengesAdapter != null) {
                            setTrendingChallengeAdapter()
                        } else {
                            mTrendingChallengesAdapter!!.swap(mChallengeCategory as ArrayList<Data>)
                            NUM_PAGES = mChallengeCategory.size
                        }
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

        mViewModel.getStartChallengeObject()
            .observe(this, Observer<StartChallengeResponse> { mData ->
                try {
                    if (Challengedialog.isShowing) {
                        Challengedialog.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("trace", e.message)
                }

                showPopupProgressSpinner(false)
                if (mData != null && mData.status == 200) {
                    if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                        mViewModel.getchallenges(
                            BasicRequest(
                                SharedPreferencesManager.getUserId(
                                    mContext
                                ), 0
                            )
                        )
                    }
                } else if (mData.status == 400) {
                    Toast.makeText(
                        mContext,
                        "There is another challenge already started.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        mViewModel.getStopChallengeObject().observe(this, Observer<BasicInfoResponse> { mData ->
            try {
                if (stopChallengedialog.isShowing) {
                    stopChallengedialog.dismiss()
                }
            } catch (e: Exception) {
                Log.e("trace", e.message)
            }
            try {
                if (Challengedialog.isShowing) {
                    Challengedialog.dismiss()
                }
            } catch (e: Exception) {
                Log.e("trace", e.message)
            }


            if (mData != null && mData.status == 200) {
                Toast.makeText(mContext, "This challenge has been stopped.", Toast.LENGTH_LONG)
                    .show()
                if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                    mViewModel.getchallenges(
                        BasicRequest(
                            SharedPreferencesManager.getUserId(mContext),
                            0
                        )
                    )
                }
            }
        })


    }


    private fun init() {
        setChallengeAdapter()
        setTrendingChallengeAdapter()
        spring_dots_indicator.setViewPager(rewardsViewPager)

        for (i in 0 until IMAGES.size)
            ImagesArray!!.add(IMAGES[i])

        rewardsViewPager.adapter =
            SlidingFeaturedChallengeImageAdapter(mContext, mFeaturedChallengeCategory, this)


        NUM_PAGES = IMAGES.size

    }


    private fun autoStartViewPager() {
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
        }, 3000, 5000)
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
        mTrendingChallengesAdapter =
            ChallengeTrendingAdapter(mContext, mTrendChallengeCategory, this)
//        var spacingInPixels = resources.getDimensionPixelSize(R.dimen._10sdp)
//        trendchallengesList.addItemDecoration(SpacesItemDecoration(spacingInPixels))
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            autoStartViewPager()
            (mContext as LandingActivity).showDisconnection(false)
            (mContext as LandingActivity).disableSwipe(false)

        }
    }


}
