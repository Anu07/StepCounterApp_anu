package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
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

class MyProfileActivity : BaseActivity<ProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_my_profile

    override val viewModel: ProfileViewModel

        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { ProfileViewModel(application) })
            .get(ProfileViewModel::class.java)

    private var profileFragment: ProfileFragment? = null

    override
    val context: Context
        get() = this@MyProfileActivity

    lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate() {
        lastUpdtd.text =
            "Last updated: " + (SharedPreferencesManager.getString(this@MyProfileActivity, SYNCDATE)?.split("T")?.get(0)
                ?: "Not available")
        mViewModel!!.getProfileResponse().observe(this, Observer { mResponse ->
            showPopupProgressSpinner(false)
            if (mResponse.data != null) {
                SharedPreferencesManager.saveUpdatedUserObject(this@MyProfileActivity, mResponse.data)
                if (mResponse.data.image.isNotEmpty()) {
                    Picasso.get().load(RetrofitClient.IMG_URL + mResponse.data.image).placeholder(R.drawable.nouser)
                        .resize(200, 200)
                        .into(profileImg)
                }
                bmiVal.text = String.format("%.3f", mResponse.data.bmi)
            }
        })
        profileFragment = ProfileFragment.newInstance(this)
    }

    override fun onResume() {
        super.onResume()
        mViewModel!!.getProfileData()
    }

    override fun initListeners() {
        showPopupProgressSpinner(true)
        mViewModel!!.getProfileData()
        calculate.setOnClickListener {
            startActivity(Intent(this@MyProfileActivity, BmiCalculatorActivity::class.java).putExtra("inApp", "1"))
        }
        img_back.setOnClickListener {
            super.onBackPressed()
        }
        editView.setOnClickListener {
          openFragment(ProfileFragment.newInstance(this))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("Test", "Result")
        this.profileFragment?.onActivityResult(requestCode, resultCode, data)
    }
}