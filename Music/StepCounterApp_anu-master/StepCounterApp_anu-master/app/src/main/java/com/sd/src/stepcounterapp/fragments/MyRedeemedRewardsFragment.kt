package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.MyRedeemedRewardsAdapter
import com.sd.src.stepcounterapp.model.profile.Data
import com.sd.src.stepcounterapp.model.rewards.MyRedeemedResponse
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.ProfileViewModel
import kotlinx.android.synthetic.main.black_crosstitlebar.*
import kotlinx.android.synthetic.main.list_challenges.*

class MyRedeemedRewardsFragment : BaseFragment() {

    private lateinit var mProfileViewModel: ProfileViewModel
    private var userData: Data? = null

    private lateinit var myRedeemedRewardsAdapter: MyRedeemedRewardsAdapter

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MyRedeemedRewardsFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): MyRedeemedRewardsFragment {
            instance = MyRedeemedRewardsFragment()
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
        userData = SharedPreferencesManager.getUpdatedUserObject(mContext)
        //  Picasso.get().load(RetrofitClient.IMG_URL + userData?.image).placeholder(R.drawable.nouser).into(img_nav_header)
       // firstNameEd.setText(userData!!.firstName.toString())
      //  lastNameEd.setText(userData!!.lastName.toString())
        txt_title.setImageResource(R.drawable.redeemed_header)
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        mProfileViewModel.getRedeemRewards()

        mProfileViewModel.getMyRedeemedResponse().observe(this, androidx.lifecycle.Observer { mData ->
        //  showPopupProgressSpinner(true)
            if (mData.status == 200) {
                if(mData.data.isEmpty()){
                    noDataTxt.visibility = View.VISIBLE
                }else{
                    noDataTxt.visibility = View.GONE
                    setAdapter(mData)
                }

            }
        })

    }

    private fun setAdapter(mData: MyRedeemedResponse) {
     //   showPopupProgressSpinner(false)
        list_mychallenges.layoutManager = LinearLayoutManager(mContext)
        myRedeemedRewardsAdapter = MyRedeemedRewardsAdapter(mContext, mData)
        list_mychallenges.adapter = myRedeemedRewardsAdapter
    }

}
