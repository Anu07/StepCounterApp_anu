package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.LandingPagerAdapter
import com.sd.src.stepcounterapp.fragments.*
import com.sd.src.stepcounterapp.network.RetrofitClient.IMG_URL
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sdi.sdeiarchitecturemvvm.activities.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.bottom_toolbar.*
import kotlinx.android.synthetic.main.titlebar.*


class LandingActivity : BaseActivity<BaseViewModel>(), View.OnClickListener {

    private val FRAG_HAYATECH: Int = 0
    private val FRAG_CHALLENGES: Int = 1
    private val FRAG_MARKET_PLACE: Int = 2
    private val FRAG_SURVEY: Int = 3
    private val FRAG_WALLET: Int = 4

    private var mSelected = -1

    override val layoutId: Int
        get() = R.layout.activity_landing

    override val viewModel: BaseViewModel
        get() = ViewModelProviders.of(this,
            BaseViewModelFactory { BaseViewModel(application) }).get(BaseViewModel::class.java)

    override val context: Context
        get() = this@LandingActivity

    lateinit var mAdapter: LandingPagerAdapter

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mActionBarDrawerToggle: ActionBarDrawerToggle
    private val users = arrayOf(
        "My Profile",
        "Transaction History",
        "FAQs",
        "Contact us",
        "Settings",
        "Notifications",
        "Logout"
    )

    override fun onCreate() {
        mDrawerLayout = findViewById(R.id.drawer_layout)

        var aAdapter = ArrayAdapter(this, R.layout.menu_textview, users)
        list_menu_item.adapter = aAdapter
        list_menu_item.onItemClickListener = AdapterView.OnItemClickListener { a, v, position, id ->
            if (position == 6) {
                logoutUser()
            }
        }

        mAdapter = LandingPagerAdapter(supportFragmentManager)
        mAdapter.addFragment(HayatechFragment.newInstance(this@LandingActivity))
        mAdapter.addFragment(ChallengesFragment.newInstance(this@LandingActivity))
        mAdapter.addFragment(MarketPlaceFragment.newInstance(this@LandingActivity))
        mAdapter.addFragment(SurveysFragment.newInstance(this@LandingActivity))
        mAdapter.addFragment(ProfileFragment.newInstance(this@LandingActivity))

        vpLanding.setPagingEnabled(false)
        vpLanding.adapter = mAdapter
        vpLanding.offscreenPageLimit = 4

        loadFragment(FRAG_HAYATECH)
    }

    override fun initListeners() {
        setNavHeader()
        llHayatech.setOnClickListener(this@LandingActivity)
        llChallenges.setOnClickListener(this@LandingActivity)
        llMarketPalace.setOnClickListener(this@LandingActivity)
        llSurvey.setOnClickListener(this@LandingActivity)
        llWallet.setOnClickListener(this@LandingActivity)
        img_nav.setOnClickListener(this@LandingActivity)
    }

    private fun setNavHeader() {
        if (SharedPreferencesManager.getUserObject(this@LandingActivity) != null) {
            var user = SharedPreferencesManager.getUserObject(this@LandingActivity)
            userNameNav.text = "Hello " + user.data.firstName + "!"
            Picasso.get().load(IMG_URL + user.data.image).resize(100, 100).into(img_nav_header)
            Picasso.get().load(IMG_URL + user.data.image).resize(80, 80).into(profileImgTitle)
        }
    }


    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loadFragment(selected: Int) {
        hideKeyboard()

        imgHayatech.setImageResource(R.drawable.dashboard)
        imgChallenges.setImageResource(R.drawable.challenge)
        imgMarketPalace.setImageResource(R.drawable.marketplace)
        imgSurvey.setImageResource(R.drawable.survey)
        imgWallet.setImageResource(R.drawable.wallet)

        when (selected) {
            FRAG_HAYATECH -> {
                imgHayatech.setImageResource(R.drawable.dashboard_s)
                txt_title.text = getString(R.string.hayatech)
            }
            FRAG_CHALLENGES -> {
                imgChallenges.setImageResource(R.drawable.challenge_s)
                txt_title.text = getString(R.string.challenges)
            }
            FRAG_MARKET_PLACE -> {
                imgMarketPalace.setImageResource(R.drawable.marketplace_s)
                txt_title.text = getString(R.string.marketplace)
            }
            FRAG_SURVEY -> {
                imgSurvey.setImageResource(R.drawable.survey_s)
                txt_title.text = getString(R.string.surveys)
            }
            FRAG_WALLET -> {
                imgWallet.setImageResource(R.drawable.wallet_s)
                txt_title.text = getString(R.string.wallet)
            }
        }
        mSelected = selected
        vpLanding.currentItem = mSelected
    }

    override fun onClick(view: View) {
        when (view.id) {
            // bottom bar
            R.id.llHayatech -> loadFragment(FRAG_HAYATECH)
            R.id.llChallenges -> loadFragment(FRAG_CHALLENGES)
            R.id.llMarketPalace -> loadFragment(FRAG_MARKET_PLACE)
            R.id.llSurvey -> loadFragment(FRAG_SURVEY)
            R.id.llWallet -> loadFragment(FRAG_WALLET)
            R.id.img_nav -> mDrawerLayout.openDrawer(Gravity.LEFT)

        }
    }


    /**
     * logout
     */

    fun logoutUser() {
        // Clearing all data from Shared Preferences
        val builder = AlertDialog.Builder(this@LandingActivity)
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setMessage("Are you sure you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id -> SharedPreferencesManager.logout(this@LandingActivity) }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()

    }


}
