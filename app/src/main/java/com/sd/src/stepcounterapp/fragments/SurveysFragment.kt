package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.SurveysAdapter
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
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
    private var mData: ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_surveys, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSurveyAdapter()
    }

    private fun setSurveyAdapter() {
        rvSurveys.layoutManager = LinearLayoutManager(mContext)
        mSurveyAdapter = SurveysAdapter(mContext, mData)
        rvSurveys.adapter = mSurveyAdapter
    }
}

