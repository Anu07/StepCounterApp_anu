package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.adapter.NotificationsAdapter
import com.sd.src.stepcounterapp.model.notificatyionlist.Data
import com.sd.src.stepcounterapp.viewModels.NotificationsViewModel
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : BaseFragment() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: NotificationFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): NotificationFragment {
            instance = NotificationFragment()
            mContext = context
            return instance
        }
    }

    private lateinit var notificationAdapter: NotificationsAdapter
    private lateinit var mViewModel: NotificationsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(NotificationsViewModel::class.java)
        mViewModel.getNotifications()
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        mViewModel.getNotificationResponse().observe(this, Observer { mData ->
            if(mData.status == 200){
                setAdapter(mData.data as ArrayList<Data>)
            }else{
                Toast.makeText(SettingsFragment.mContext,"Some error occurred", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun setAdapter(mData: ArrayList<Data>) {
        //   showPopupProgressSpinner(false)
        notificationList.layoutManager = LinearLayoutManager(mContext)
        notificationAdapter = NotificationsAdapter(mContext, mData)
        notificationList.adapter = notificationAdapter
    }


    override fun onDetach() {
        super.onDetach()
        (HayatechFragment.mContext as LandingActivity).hideBottomLayout(false)
    }

}