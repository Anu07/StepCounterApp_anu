package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.MyChallengeAdapter
import com.sd.src.stepcounterapp.model.challenge.MyChallengeResponse
import com.sd.src.stepcounterapp.model.profile.Data
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.ProfileViewModel
import kotlinx.android.synthetic.main.list_challenges.*

class MyChallengeFragment : BaseFragment() {

    private lateinit var mProfileViewModel: ProfileViewModel
    private var userData: Data? = null

    private lateinit var myChallengeAdapter: MyChallengeAdapter

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MyChallengeFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): MyChallengeFragment {
            instance = MyChallengeFragment()
            mContext = context
            return instance
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.list_challenges, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProfileViewModel = ViewModelProviders.of(activity!!).get(ProfileViewModel::class.java)
        userData = SharedPreferencesManager.getUpdatedUserObject(MyChallengeFragment.mContext)

        //  Picasso.get().load(RetrofitClient.IMG_URL + userData?.image).placeholder(R.drawable.nouser).into(img_nav_header)
       // firstNameEd.setText(userData!!.firstName.toString())
      //  lastNameEd.setText(userData!!.lastName.toString())

        mProfileViewModel.getMyChallenge()

        mProfileViewModel.getMyChallengeResponse().observe(this, androidx.lifecycle.Observer { mData ->
        //  showPopupProgressSpinner(true)
            if (mData.status == 200) {
                Toast.makeText(activity, "Shown successfully", Toast.LENGTH_LONG).show()
                //fragmentManager!!.popBackStackImmediate()
                setAdapter(mData)

            }
        })

    }

    private fun setAdapter(mData: MyChallengeResponse) {
     //   showPopupProgressSpinner(false)
        list_mychallenges.layoutManager = LinearLayoutManager(mContext)
        myChallengeAdapter = MyChallengeAdapter(mContext, mData)
        list_mychallenges.adapter = myChallengeAdapter
    }

}
