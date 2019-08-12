package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.SurveysAdapter
import com.sd.src.stepcounterapp.model.survey.Data
import com.sd.src.stepcounterapp.model.survey.Products
import com.sd.src.stepcounterapp.model.survey.SurveyListResponse
import com.sd.src.stepcounterapp.viewModels.SurveyViewModel
import kotlinx.android.synthetic.main.fragment_market_place.*
import kotlinx.android.synthetic.main.fragment_surveys.*

class SurveysFragment : Fragment() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: SurveysFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): SurveysFragment {
            instance = SurveysFragment()
            mContext = context
            return instance
        }
    }

    lateinit var mSurveyAdapter: SurveysAdapter
    private lateinit var mViewModel: SurveyViewModel

    private var mData: ArrayList<Data> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_surveys, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(SurveyViewModel::class.java)
        mViewModel.getSurveyList().observe(this,
            Observer<SurveyListResponse> { mData ->
                if (mData != null) {
                    if (mData.data!!.size > 0) {
                        this.mData.addAll(mData.data)
                        rvSurveys.visibility = View.VISIBLE
                        norec.visibility = View.GONE
                        mSurveyAdapter.notifyDataSetChanged()
                    }
                } else {
                    rvSurveys.visibility = View.GONE
                    norec.visibility = View.VISIBLE
                }
            })

        mViewModel.hitSurveyListApi()

        setSurveyAdapter()
    }

    private fun setSurveyAdapter() {
        rvSurveys.layoutManager = LinearLayoutManager(mContext)
        mSurveyAdapter = SurveysAdapter(mContext, mData)
        rvSurveys.adapter = mSurveyAdapter
    }
}

