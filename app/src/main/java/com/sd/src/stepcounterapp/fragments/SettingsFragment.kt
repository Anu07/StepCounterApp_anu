package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.activities.SyncDeviceActivity
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.viewModels.MarketPlaceViewModel
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment: Fragment(){

    private lateinit var mViewModel: MarketPlaceViewModel
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: SettingsFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): SettingsFragment {
            instance = SettingsFragment()
            mContext = context
            return instance
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(MarketPlaceViewModel::class.java)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_settings,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_sync_date_time.text = "Last sync: "+SharedPreferencesManager.getString(mContext,SYNCDATE)!!.split("T")[0]
        btnChangeTracker.setOnClickListener {
            startActivity(Intent(mContext,SyncDeviceActivity::class.java).putExtra("disconnect",true))
        }
        ll_change_password.setOnClickListener {
            (mContext as LandingActivity).onFragmnet(6)
        }
    }

}