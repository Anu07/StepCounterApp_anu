package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.SurveysQuestionsAdapter
import com.sd.src.stepcounterapp.fragments.SurveysFragment
import com.sd.src.stepcounterapp.model.survey.Datum
import com.sd.src.stepcounterapp.model.survey.Question
import com.sd.src.stepcounterapp.model.survey.surveyrequest.SurveystartRequestModel
import com.sd.src.stepcounterapp.model.survey.surveyrequest.UserAnswer
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.SurveyViewModel
import kotlinx.android.synthetic.main.activity_survey_detail.*
import kotlinx.android.synthetic.main.backtitlebar.*

class SurveyDetailActivity : BaseActivity<SurveyViewModel>(), SurveysQuestionsAdapter.AnswerListener {
    override fun onAnswer(pos: Int, value: String) {
        mAnsData.add(pos, value)
    }

    override val layoutId: Int
        get() = R.layout.activity_survey_detail
    override val viewModel: SurveyViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { SurveyViewModel(application) })
            .get(SurveyViewModel::class.java)
    private var mData: ArrayList<Question> = ArrayList()
    private var mAnsData: ArrayList<String> = ArrayList()
    private lateinit var mSurveyAdapter: SurveysQuestionsAdapter
    lateinit var mItemData: Datum
    override val context: Context
        get() = this@SurveyDetailActivity
    lateinit var userAns: UserAnswer
    private var mUserAnswerData: ArrayList<UserAnswer> = ArrayList()

    override fun onCreate() {
        mItemData = intent.getParcelableExtra("Data")
        if(mItemData.answered == true){
            Toast.makeText(mContext,"You have already taken this survey", Toast.LENGTH_LONG).show()
            finish()
        }
        mData = mItemData.questions as ArrayList<Question>
        surveyName.text = mItemData.name.capitalize()
        quesCount.text = mItemData.questions.size.toString().capitalize()
        expireDate.text = mItemData.expireOn.split("T")[0]
        setQuestionSurveyAdapter()
        mViewModel!!.takesurvey().observe(this, Observer { mData ->
            if (mData.status == 200) {
                Toast.makeText(this@SurveyDetailActivity, "Survey submitted successfully", Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }

    override fun initListeners() {
        img_back.setOnClickListener {
            startActivity(Intent(this@SurveyDetailActivity, LandingActivity::class.java))
            finish()
        }
        finishbutton.setOnClickListener {
            showPopupProgressSpinner(true)
            mViewModel!!.hitAttendSurveyApi(
                SurveystartRequestModel(
                    mItemData._id,
                    getanswersData(),
                    mItemData,
                    SharedPreferencesManager.getUserId(this@SurveyDetailActivity)
                )
            )
        }
        img_back.setOnClickListener{
            onBackPressed()
        }

    }

    private fun getanswersData(): ArrayList<UserAnswer> {
        mItemData!!.questions.forEachIndexed { index, element ->
            if (mAnsData[index].isNotEmpty()) {           //if mAns list has answer stored on that position
                userAns = UserAnswer()
                userAns.answer = mAnsData
                userAns.id = mItemData.questions.get(index)._id
                Log.e("Answer and Id", "" + userAns.answer + "!!1" + userAns.id)
                mUserAnswerData.add(userAns)
            }
        }
        return mUserAnswerData
    }


    override fun onBackPressed() {
        super.onBackPressed()
//        startActivity(Intent(this@SurveyDetailActivity, LandingActivity::class.java).putExtra("surveyBack", ""))
    }


    /**
     * Set questions adapter
     */
    private fun setQuestionSurveyAdapter() {
        quesList.layoutManager = LinearLayoutManager(SurveysFragment.mContext)
        mSurveyAdapter = SurveysQuestionsAdapter(SurveysFragment.mContext, this, mData)
        quesList.adapter = mSurveyAdapter
    }
}