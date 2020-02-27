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
import com.sd.src.stepcounterapp.*
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.activities.SyncDeviceActivity
import com.sd.src.stepcounterapp.dialog.NotificationDialog
import com.sd.src.stepcounterapp.model.notification.BasicNotificationSettingsRequest
import com.sd.src.stepcounterapp.utils.DisableLeftMenu
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.GA_NOTIFY
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.NC_NOTIFY
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.NSA_NOTIFY
import com.sd.src.stepcounterapp.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.backtitlebar.img_back
import kotlinx.android.synthetic.main.backtitlebar.txt_title
import kotlinx.android.synthetic.main.black_crosstitlebar.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    private lateinit var notificationDialog: NotificationDialog
    private lateinit var mViewModel: SettingsViewModel

    companion object {
        private var mMenuListener: DisableLeftMenu? = null


        @SuppressLint("StaticFieldLeak")
        lateinit var instance: SettingsFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): SettingsFragment {
            instance = SettingsFragment()
            mContext = context
            if (context is LandingActivity) {
                mMenuListener = context
            }
            return instance
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mMenuListener?.disableLeftMenuSwipe()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_settings, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(SettingsViewModel::class.java)

        btnChangeTracker.setOnClickListener {
            btnChangeTracker.isEnabled = false
            startActivity(
                Intent(requireContext(), SyncDeviceActivity::class.java).putExtra(
                    "disconnect",
                    true
                )
            )
        }
        ll_change_password.setOnClickListener {
            (requireContext() as LandingActivity).onFragment(6)
        }
        txt_title.setImageResource(R.drawable.settings)
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        privacy_policy.setOnClickListener {
            (requireContext() as LandingActivity).onFragment(10)
        }

        terms_conditions.setOnClickListener {
            (requireContext() as LandingActivity).onFragment(11)

        }

        stepscreenbttn.setOnCheckedChangeListener { _, isChecked ->
                try {
                    if((mContext as LandingActivity).checkDeviceConnectionStatus()){
                        (mContext as LandingActivity).lockStepsScreen(isChecked)
                    }else{
                        stepscreenbttn.isChecked = false
                        Toast.makeText(requireContext(),"Fitpolo wearable is not connected", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                   Log.e("error",""+e.message)
                }
        }

       /* var syncDate = changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM, yyyy", SharedPreferencesManager.getString(
            HayatechFragment.mContext, SYNCDATE)!!)+" | "+ convertToLocal(SharedPreferencesManager.getString(
            HayatechFragment.mContext, SYNCDATE)!!)*/

//        var indate= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(syncDate)
        if(SharedPreferencesManager.hasKey(requireContext(),SharedPreferencesManager.SYNCTIME)){
            tv_sync_date_time.text = dateconvertToLocal("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", SharedPreferencesManager.getString(HayatechFragment.mContext,
                SharedPreferencesManager.SYNCTIME
            ), "EEEE, MMM d, yyyy, hh:mm a")
        }


        mViewModel.getSettingsResponse().observe(this, Observer { mData ->
            try {
                showPopupProgressSpinner(false)
                notificationDialog.dismiss()
            } catch (e: Throwable) {
                Log.e("error", "popup")
            }
            if (mData.status == 200) {
                SharedPreferencesManager.setBoolean(
                    requireContext(),
                    mData.data.goalAchivedNotification,
                    GA_NOTIFY
                )
                SharedPreferencesManager.setBoolean(
                    requireContext(),
                    mData.data.newSurveyNotification,
                    NSA_NOTIFY
                )
                SharedPreferencesManager.setBoolean(
                    requireContext(), mData.data.newChallengeNotification,
                    NC_NOTIFY
                )
//                Toast.makeText(requireContext(), "Settings updated successfully", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(requireContext(), "Some error occurred", Toast.LENGTH_LONG).show()
            }
        })



        notificationLayout.setOnClickListener {
            notificationDialog = NotificationDialog(
                requireContext(), R.style.pullBottomfromTop,
                object : NotificationDialog.NotificationInterface {
                    override fun onUpdateSettings(goal: Boolean,challenge:Boolean,survey:Boolean) {
                        showPopupProgressSpinner(true)
                        mViewModel.updateSettings(BasicNotificationSettingsRequest(SharedPreferencesManager.getUserId(
                            HayaTechApplication.applicationContext()
                        ),goal,challenge,survey))
                    }

                })
            notificationDialog.show()
        }

    }

    override fun onDetach() {
        super.onDetach()
        try {
            (requireContext() as LandingActivity).hideBottomLayout(false)
            mMenuListener?.enableLeftMenuSwipe()

        } catch (e: Exception) {
            Log.e("Test", "Settings")
        }
    }

    override fun onResume() {
        super.onResume()
        btnChangeTracker.isEnabled = true
    }

}
