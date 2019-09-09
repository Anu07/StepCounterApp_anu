package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.contactUs.ContactUsRequest
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.profile.Data
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.OpenContactUsViewModel
import kotlinx.android.synthetic.main.open_contact_us_fragment.*

class OpenContactUsFragment : Fragment() {

    private lateinit var viewModel: OpenContactUsViewModel
    private var userData: LoginResponseJ? = null
    private var contactUsRequest: ContactUsRequest? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: OpenContactUsFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): OpenContactUsFragment {
            instance = OpenContactUsFragment()
            mContext = context
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.open_contact_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OpenContactUsViewModel::class.java)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(OpenContactUsViewModel::class.java)

        userData = SharedPreferencesManager.getUserObject(mContext)

        send_contact_us.setOnClickListener {
            if(message.toString().isNotEmpty()){

                viewModel.postContactUs(ContactUsRequest(userData!!.data._id,message.toString().trim()))
            }else{
                Toast.makeText(activity,"Message can't be empty.", Toast.LENGTH_LONG).show()
            }
        }


        viewModel.postContactUsResponse().observe(this, androidx.lifecycle.Observer { mData ->
            //  showPopupProgressSpinner(true)
            if (mData.status == 200) {
                Toast.makeText(activity, mData.message, Toast.LENGTH_LONG).show()
                //fragmentManager!!.popBackStackImmediate()
            }
        })
    }
}
