package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.MySurveyAdapter
import com.sd.src.stepcounterapp.model.profile.Data
import com.sd.src.stepcounterapp.model.survey.mysurvey.MySurveyResponse
import com.sd.src.stepcounterapp.model.survey.mysurveyresponse.MySurveyResponseModel
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.ProfileViewModel
import kotlinx.android.synthetic.main.black_crosstitlebar.*
import kotlinx.android.synthetic.main.list_challenges.*
import kotlinx.android.synthetic.main.list_surveys.*

class MySurveysFragment : BaseFragment() {

    private lateinit var mProfileViewModel: ProfileViewModel
    private var userData: Data? = null

    private lateinit var mySurveyAdapter: MySurveyAdapter

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MySurveysFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): MySurveysFragment {
            instance = MySurveysFragment()
            mContext = context
            return instance
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.list_surveys, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProfileViewModel = ViewModelProviders.of(activity!!).get(ProfileViewModel::class.java)
        userData = SharedPreferencesManager.getUpdatedUserObject(mContext)

        //  Picasso.get().load(RetrofitClient.IMG_URL + userData?.image).placeholder(R.drawable.nouser).into(img_nav_header)
       // firstNameEd.setText(userData!!.firstName.toString())
      //  lastNameEd.setText(userData!!.lastName.toString())

        showPopupProgressSpinner(true)
        txt_title.setImageResource(R.drawable.mysurveys_header)
//        txt_title.setImageResource()
        mProfileViewModel.getSurveyResponse().observe(this, androidx.lifecycle.Observer { mData ->
          showPopupProgressSpinner(false)
            if(mData.data.isEmpty()){
                noSurveyDataTxt.visibility = View.VISIBLE
            }else{
                noSurveyDataTxt.visibility = View.GONE
                setAdapter(mData)
            }
        })
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        mProfileViewModel.getMySurveys()

    }

    private fun setAdapter(mData: MySurveyResponseModel) {
     //   showPopupProgressSpinner(false)
        list_mysurveys.layoutManager = LinearLayoutManager(mContext)
        mySurveyAdapter = MySurveyAdapter(mContext, mData)
        list_mysurveys.adapter = mySurveyAdapter
    }

}
