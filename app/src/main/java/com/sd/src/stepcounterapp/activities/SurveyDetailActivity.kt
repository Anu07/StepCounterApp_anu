package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.SurveysQuestionsAdapter
import com.sd.src.stepcounterapp.fragments.SurveysFragment
import com.sd.src.stepcounterapp.model.survey.Data
import com.sd.src.stepcounterapp.model.survey.Products
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sdi.sdeiarchitecturemvvm.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_survey_detail.*
import kotlinx.android.synthetic.main.backtitlebar.*

class SurveyDetailActivity : BaseActivity<BaseViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_survey_detail
    override val viewModel: BaseViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { BaseViewModel(application) })
            .get(BaseViewModel::class.java)
    private var mData: ArrayList<Products> = ArrayList()
    private lateinit var mSurveyAdapter: SurveysQuestionsAdapter
    lateinit var mItemData: Data
    override val context: Context
        get() = this@SurveyDetailActivity

    override fun onCreate() {
        mItemData = intent.getParcelableExtra("Data")
        mData = mItemData.products as ArrayList<Products>
        surveyName.text = mItemData.name
        quesCount.text = mItemData.products.size.toString()
        expireDate.text = mItemData.expireOn.split("T")[0]
        setQuestionSurveyAdapter()
    }

    override fun initListeners() {
        img_back.setOnClickListener {
            startActivity(Intent(this@SurveyDetailActivity, LandingActivity::class.java))
        }

    }


    override fun onBackPressed() {
//        super.onBackPressed()
        startActivity(Intent(this@SurveyDetailActivity, LandingActivity::class.java).putExtra("surveyBack",""))
    }


    /**
     * Set questions adapter
     */
    private fun setQuestionSurveyAdapter() {
        quesList.layoutManager = LinearLayoutManager(SurveysFragment.mContext)
        mSurveyAdapter = SurveysQuestionsAdapter(SurveysFragment.mContext, mData)
        quesList.adapter = mSurveyAdapter
    }
}