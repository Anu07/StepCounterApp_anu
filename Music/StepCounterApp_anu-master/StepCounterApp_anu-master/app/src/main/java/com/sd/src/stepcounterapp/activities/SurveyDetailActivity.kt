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
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.dateconvertToLocal
import com.sd.src.stepcounterapp.fragments.HayatechFragment
import com.sd.src.stepcounterapp.fragments.MarketPlaceFragment
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
        if(mAnsData.contains(value)){
            mAnsData.remove(value)
        }else{
            mAnsData.add(value)
        }
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

        expireDate.text =dateconvertToLocal("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", mItemData.expireOn!!,"dd MMM, yyyy")
        setQuestionSurveyAdapter()
        mViewModel!!.takesurvey().observe(this, Observer { mData ->
            showPopupProgressSpinner(false)
            if (mData.status == 200) {
                Toast.makeText(this@SurveyDetailActivity, "You have earned "+mItemData.earningToken+" tokens for completing your survey.", Toast.LENGTH_LONG).show()
                SurveysFragment.instance.notifyData()
//                finish()
                onBackPressed()

            }

        })
    }

    override fun initListeners() {
        img_back.setOnClickListener {
            SurveysFragment.instance.notifyData()
            onBackPressed()

//            (mContext as LandingActivity).onFragment(3)
//            ( this@SurveyDetailActivity as LandingActivity).onFragment(0)
        }
        finishbutton.setOnClickListener {
            if(mAnsData.isNotEmpty()){
                showPopupProgressSpinner(true)
                mViewModel!!.hitAttendSurveyApi(
                    SurveystartRequestModel(
                        mItemData._id,
                        getanswersData(),
                        mItemData,
                        SharedPreferencesManager.getUserId(this@SurveyDetailActivity)
                    )
                )
            }else{
                Toast.makeText(this@SurveyDetailActivity, "Please answer at least one question", Toast.LENGTH_LONG).show()
            }

        }
       /* img_back.setOnClickListener{
            onBackPressed()
        }*/

    }

    private fun getanswersData(): ArrayList<UserAnswer> {

        mItemData.questions.forEachIndexed { _, question ->
            mAnsData = ArrayList()
            question.options.forEachIndexed { _, option ->
                if(option.isSelected){
                   mAnsData.add(option._id)
                }
            }
            userAns = UserAnswer()
            userAns.answer = mAnsData
            userAns.id = question._id
            Log.e("Answer and Id", "" + userAns.answer + "!!1" + userAns.id)
            mUserAnswerData.add(userAns)
        }
        return mUserAnswerData
    }


    /**
     * Set questions adapter
     */
    private fun setQuestionSurveyAdapter() {
        quesList.layoutManager = LinearLayoutManager(mContext)
        mSurveyAdapter = SurveysQuestionsAdapter(mContext, this, mData)
        quesList.adapter = mSurveyAdapter
    }
}