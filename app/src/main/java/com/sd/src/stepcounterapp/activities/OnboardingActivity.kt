package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.jaeger.library.StatusBarUtil
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.OnBoardingViewPagerAdapter
import com.sd.src.stepcounterapp.fragments.OnBoardingFragment
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardingActivity : BaseActivity<BaseViewModel>() {
    override fun initListeners() {
        tv_next.setOnClickListener {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    val message = arrayOf("Get Moving and Track your Progress!","Earn Tokens and Redeem Rewards in the Wellness Marketplace!","Join the Movement and Participate in Exciting Movement challenges!")


    private var currentPage = 0
    private val fragments = ArrayList<Fragment>()

    override val layoutId: Int
        get() = R.layout.activity_onboarding
    override val viewModel: BaseViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { BaseViewModel(application) }).get(BaseViewModel::class.java)
    override val context: Context
        get() = this@OnboardingActivity


    override fun onCreate() {
        StatusBarUtil.setTransparent(this@OnboardingActivity)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        val adapter = OnBoardingViewPagerAdapter(supportFragmentManager)
        for (i in 0..2) {
            fragments.add(OnBoardingFragment.newInstance(i))
        }
        adapter.addFragments(fragments)
        view_pager.adapter = adapter
        spring_dots_indicator.setViewPager(view_pager)
        // keep track of the current screen
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                tv_name.text = message[position]
            }
        })
    }


}