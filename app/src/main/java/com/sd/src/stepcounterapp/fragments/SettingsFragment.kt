package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.activities.SyncDeviceActivity
import com.sd.src.stepcounterapp.dialog.NotificationDialog
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.GA_NOTIFY
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.NC_NOTIFY
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.NSA_NOTIFY
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    private lateinit var notificationDialog: NotificationDialog
    private lateinit var mViewModel: SettingsViewModel

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_settings, container, false)
        mViewModel = ViewModelProviders.of(activity!!).get(SettingsViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_sync_date_time.text = "Last sync: " + SharedPreferencesManager.getString(mContext, SYNCDATE)!!.split("T")[0]
        btnChangeTracker.setOnClickListener {
            startActivity(Intent(mContext, SyncDeviceActivity::class.java).putExtra("disconnect", true))
        }
        ll_change_password.setOnClickListener {
            (mContext as LandingActivity).onFragment(6)
        }
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        privacy_policy.setOnClickListener {
            (mContext as LandingActivity).onFragment(10)
        }

        terms_conditions.setOnClickListener {
            (mContext as LandingActivity).onFragment(11)

        }

        mViewModel.getSettingsResponse().observe(this, Observer { mData ->
            try {
                notificationDialog.dismiss()
            } catch (e: Exception) {
                Log.e("error","popup")
            }
            if(mData.status == 200){
                     SharedPreferencesManager.setBoolean(mContext,mData.data.goalAchivedNotification,GA_NOTIFY)
                    SharedPreferencesManager.setBoolean(mContext, mData.data.newSurveyNotification,NSA_NOTIFY)
                    SharedPreferencesManager.setBoolean(mContext, mData.data.newChallengeNotification,
                        NC_NOTIFY)
                Toast.makeText(mContext,"Settings updated successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(mContext,"Some error occurred", Toast.LENGTH_LONG).show()
            }
        })



        notificationLayout.setOnClickListener {
            notificationDialog = NotificationDialog(
                mContext, R.style.pullBottomfromTop,
                object : NotificationDialog.NotificationInterface {
                    override fun onUpdateSettings(queryCat: String) {
                        mViewModel.updateSettings()
                    }

                })
            notificationDialog!!.show()
        }

    }

    override fun onDetach() {
        super.onDetach()
        Log.i("test", "Detach")
        (HayatechFragment.mContext as LandingActivity).hideBottomLayout(false)
    }

}