package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.AppConstants.INTENT_CHALLENGETKN
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LeaderboardActivity
import com.sd.src.stepcounterapp.adapter.MyChallengeAdapter
import com.sd.src.stepcounterapp.model.challenge.Datum
import com.sd.src.stepcounterapp.model.challenge.MyChallengeResponse
import com.sd.src.stepcounterapp.model.profile.Data
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.ProfileViewModel
import kotlinx.android.synthetic.main.black_crosstitlebar.*
import kotlinx.android.synthetic.main.list_challenges.*

class MyChallengeFragment : BaseFragment(), MyChallengeAdapter.myChallengeItemClickListener {
    override fun onItemClick(pos: Int) {
        startActivity(Intent(activity,LeaderboardActivity::class.java).putExtra(INTENT_CHALLENGETKN,challengesTkn[pos].challengeId))
    }

    private var challengesTkn: List<Datum> = arrayListOf()
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProfileViewModel = ViewModelProviders.of(activity!!).get(ProfileViewModel::class.java)
        userData = SharedPreferencesManager.getUpdatedUserObject(mContext)

        //  Picasso.get().load(RetrofitClient.IMG_URL + userData?.image).placeholder(R.drawable.nouser).into(img_nav_header)
       // firstNameEd.setText(userData!!.firstName.toString())
      //  lastNameEd.setText(userData!!.lastName.toString())

        mProfileViewModel.getMyChallenge()
        txt_title.setImageResource(R.drawable.mychallenge_header)
        mProfileViewModel.getMyChallengeResponse().observe(this, androidx.lifecycle.Observer { mData ->
        //  showPopupProgressSpinner(true)
            if (mData.status == 200) {
                if(mData.data.isEmpty()){
                    noDataTxt.visibility = View.VISIBLE
                }else{
                    challengesTkn= mData.data
                    noDataTxt.visibility = View.GONE
                    setAdapter(mData)
                }

            }
        })
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }

    private fun setAdapter(mData: MyChallengeResponse) {
     //   showPopupProgressSpinner(false)
        list_mychallenges.layoutManager = LinearLayoutManager(mContext)
        myChallengeAdapter = MyChallengeAdapter(mContext, mData,this)
        list_mychallenges.adapter = myChallengeAdapter
    }
}
