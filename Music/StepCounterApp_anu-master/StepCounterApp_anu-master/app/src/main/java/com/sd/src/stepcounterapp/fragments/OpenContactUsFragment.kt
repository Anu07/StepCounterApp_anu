package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.model.contactUs.ContactUsRequest
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.utils.DisableLeftMenu
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.OpenContactUsViewModel
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.open_contact_us_fragment.*

class OpenContactUsFragment : BaseFragment() {

    private lateinit var viewModel: OpenContactUsViewModel
    private var userData: LoginResponseJ? = null
    companion object {
        private var mMenuListener: DisableLeftMenu? = null
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: OpenContactUsFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context, lock: Boolean): OpenContactUsFragment {
            var bundle = Bundle()
            bundle.putBoolean("lock",lock)
            instance = OpenContactUsFragment()
            instance.arguments = bundle
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.open_contact_us_fragment, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(OpenContactUsViewModel::class.java)
        txt_title.setImageResource(R.drawable.contactus)
        userData = SharedPreferencesManager.getUserObject(mContext)
        img_back.setOnClickListener {
            fragmentManager!!.popBackStackImmediate()
        }
        send_contact_us.setOnClickListener {
            if(messageTxt.text.toString().isNotEmpty()){
                if(arguments?.isEmpty!!){
                    showPopupProgressSpinner(true)
                    viewModel.postContactUs(ContactUsRequest(userData!!.data._id,false,messageTxt.text.toString().trim()))
                }else{
                    viewModel.postContactUs(ContactUsRequest(userData!!.data._id,true,messageTxt.text.toString().trim()))
                }
            }else{
                Toast.makeText(activity,"Message can't be empty.", Toast.LENGTH_LONG).show()
            }
        }


        viewModel.postContactUsResponse().observe(this, androidx.lifecycle.Observer { mData ->
            //  showPopupProgressSpinner(true)
            showPopupProgressSpinner(false)
            if (mData.status == 200) {
                Toast.makeText(activity, "Mail sent successfully.", Toast.LENGTH_LONG).show()
                messageTxt.text?.clear()
                //fragmentManager!!.popBackStackImmediate()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("test","Detach")
        (requireContext() as LandingActivity).hideBottomLayout(false)
        mMenuListener?.enableLeftMenuSwipe()
    }

}
