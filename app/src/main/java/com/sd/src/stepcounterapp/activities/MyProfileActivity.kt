package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
<<<<<<< HEAD
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.fragments.ProfileFragment
import com.sd.src.stepcounterapp.fragments.SettingsFragment
=======
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
>>>>>>> origin/master
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.LeaderboardViewModel
import com.sd.src.stepcounterapp.viewModels.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_leaderboard.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.black_crosstitlebar.*

class MyProfileActivity : BaseActivity<ProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_my_profile
    override val viewModel: ProfileViewModel
        get() =ViewModelProviders.of(
            this,
            BaseViewModelFactory { ProfileViewModel(application) }).get(ProfileViewModel::class.java)

    override
    val context: Context
        get() = this@MyProfileActivity

    override fun onCreate() {
        lastUpdtd.text = "Last updated: "+ (SharedPreferencesManager.getString(this@MyProfileActivity,SYNCDATE)?.split("T")?.get(0) ?: "Not available" )

        mViewModel!!.getProfileResponse().observe(this, Observer { mResponse->
          showPopupProgressSpinner(false)
            if(mResponse.data!= null){
              SharedPreferencesManager.saveUpdatedUserObject(this@MyProfileActivity, mResponse.data)
              if(mResponse.data.image.isNotEmpty()){
                  Picasso.get().load(mResponse.data.image).placeholder(R.drawable.nouser).into(profileImg)
              }
              bmiVal.text = mResponse.data.bmi.toString()
          }
        })
    }

    override fun onResume() {
        super.onResume()
        mViewModel!!.getProfileData()
    }


    override fun initListeners() {
        showPopupProgressSpinner(true)
        mViewModel!!.getProfileData()
        calculate.setOnClickListener {
            startActivity(Intent(this@MyProfileActivity, BmiCalculatorActivity::class.java).putExtra("inApp","1"))
        }
        img_back.setOnClickListener {
            finish()
        }
<<<<<<< HEAD
        editClick.setOnClickListener {
            openFragment()
        }

    }

    /**
     * To open fragment
     */

    private fun openFragment() {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.editcontainer, ProfileFragment.newInstance(this))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
=======
>>>>>>> origin/master
    }

}