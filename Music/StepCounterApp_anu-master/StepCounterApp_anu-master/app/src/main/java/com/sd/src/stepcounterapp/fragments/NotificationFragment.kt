package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.AppConstants
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.activities.LeaderboardActivity
import com.sd.src.stepcounterapp.adapter.NotificationsAdapter
import com.sd.src.stepcounterapp.model.notificatyionlist.NotificationData
import com.sd.src.stepcounterapp.utils.DisableLeftMenu
import com.sd.src.stepcounterapp.viewModels.NotificationsViewModel
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.backtitlebar.img_back
import kotlinx.android.synthetic.main.backtitlebar.txt_title
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.notificationbacktitlebar.*

class NotificationFragment : BaseFragment(), NotificationsAdapter.NotifyItemClickListener {
    override fun onItemClick(pos: Int) {
        mViewModel.readNotification(mNotificationList[pos]._id)
//        openHomeActivity(mNotificationList[pos].subject)
    }

    private fun openHomeActivity(subject: String) {
        val intent = Intent(requireContext(), LandingActivity::class.java).apply {
            putExtra(AppConstants.INTENT_NOTIFICATION,subject)
        }
        startActivity(intent)
    }

    companion object {
        private var mMenuListener: DisableLeftMenu? = null


        @SuppressLint("StaticFieldLeak")
        lateinit var instance: NotificationFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): NotificationFragment {
            instance = NotificationFragment()
            mContext = context
            if (context is LandingActivity) {
                mMenuListener = context
            }
            return instance
        }
    }

    private var mNotificationList: MutableList<NotificationData> =ArrayList()
    private lateinit var notificationAdapter: NotificationsAdapter
    private lateinit var mViewModel: NotificationsViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mMenuListener?.disableLeftMenuSwipe()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(NotificationsViewModel::class.java)
        mViewModel.getNotifications()
        txt_title.setImageResource(R.drawable.notificationheader)
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        readAll.setOnClickListener {
            //API to be added
            mViewModel.readAllNotifications()
        }

        mViewModel.getNotificationResponse().observe(this, Observer { mData ->
            if(mData.status == 200){
                mNotificationList=mData.data
                setAdapter(mData.data as ArrayList<NotificationData>)
            }else{
                Toast.makeText(requireContext(),"Some error occurred", Toast.LENGTH_LONG).show()
            }
        })
        mViewModel.getAllNotificationResponse().observe(this, Observer { mData ->
            if(mData.status == 200){
                mViewModel.getNotifications()
            }else{
                Toast.makeText(requireContext(),"Some error occurred", Toast.LENGTH_LONG).show()
            }
        })
        mViewModel.getReadNotificationResponse().observe(this, Observer { mData ->
            if(mData.status == 200){
                mViewModel.getNotifications()
            }else{
                Toast.makeText(requireContext(),"Some error occurred", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun setAdapter(mData: ArrayList<NotificationData>) {
        //   showPopupProgressSpinner(false)
        notificationList.layoutManager = LinearLayoutManager(requireContext())
        notificationAdapter = NotificationsAdapter(requireContext(), mData,this)
        notificationList.adapter = notificationAdapter
    }


    override fun onDetach() {
        super.onDetach()
        (requireContext() as LandingActivity).hideBottomLayout(false)
        mMenuListener?.enableLeftMenuSwipe()
    }

}