package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.fragments.MyChallengeFragment
import com.sd.src.stepcounterapp.fragments.MyRedeemedRewardsFragment
import com.sd.src.stepcounterapp.fragments.MySurveysFragment
import com.sd.src.stepcounterapp.fragments.ProfileFragment
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.black_crosstitlebar.*
import android.net.Uri
import android.util.Log
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.utils.DisableLeftMenu


class MyProfileActivity : BaseActivity<ProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_my_profile

    override val viewModel: ProfileViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { ProfileViewModel(application) })
            .get(ProfileViewModel::class.java)

    private var profileFragment: ProfileFragment? = null
    private var mCurrentPhotoPath: String? = null
    private var mCapturedImageURI: Uri? = null
    override
    val context: Context
        get() = this@MyProfileActivity
    lateinit var  mMenuListener:DisableLeftMenu

    override fun onCreate() {
        if(SharedPreferencesManager.hasKey(this@MyProfileActivity, SYNCDATE)){
            lastUpdtd.text =
                "Last updated: " + changeDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS",
                    "dd MMM, yyyy", SharedPreferencesManager.getString(this@MyProfileActivity, SYNCDATE)
                )
        }
        mViewModel!!.getProfileResponse().observe(this, Observer { mResponse ->
            showPopupProgressSpinner(false)
            if (mResponse.data != null) {
                SharedPreferencesManager.saveUpdatedUserObject(this@MyProfileActivity, mResponse.data)
                if (mResponse.data.image.isNotEmpty()) {
                    SharedPreferencesManager.setString(
                        this@MyProfileActivity,
                        IMG_URL + mResponse.data.image,
                        "userImage"
                    )
                    Log.i("IMG_URL",""+IMG_URL + mResponse.data.image)
//                    LandingActivity.updateUserImage(IMG_URL + mResponse.data.image)
                    Picasso.get().load(IMG_URL + mResponse.data.image).placeholder(R.drawable.nouser)
                        .resize(200, 200)
                        .into(profileImg)
                }
                bmiVal.text = String.format("%.2f", mResponse.data.bmi)
            }
        })
        profileFragment = ProfileFragment.newInstance(this)
//        mMenuListener = LandingActivity.mInstance
//        mMenuListener.disableLeftMenuSwipe()

    }

    override fun onResume() {
        super.onResume()
        updateData()
    }

    fun updateData() {
        mViewModel!!.getProfileData()
    }

    override fun initListeners() {
        showPopupProgressSpinner(true)
        mViewModel!!.getProfileData()
        calculate.setOnClickListener {
            startActivity(Intent(this@MyProfileActivity, BmiCalculatorActivity::class.java).putExtra("inApp", "1"))
        }
        txt_title.setImageResource(R.drawable.myprofile_header)
        img_back.setOnClickListener {
            onBackPressed()
        }
        editView.setOnClickListener {
            openFragment(ProfileFragment())
        }

        lin_my_challenges.setOnClickListener {
            openFragment(MyChallengeFragment.newInstance(this))
        }

        lin_my_survey.setOnClickListener {
            openFragment(MySurveysFragment.newInstance(this))
        }

        lin_redeemed_rewards.setOnClickListener {
            openFragment(MyRedeemedRewardsFragment.newInstance(this))
        }

        profileImg.setOnClickListener {
            openFragment(ProfileFragment())
        }
    }

    /**
     * To open fragment
     */
    private fun openFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.editcontainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        mMenuListener.enableLeftMenuSwipe()

//        finish()
    }




}