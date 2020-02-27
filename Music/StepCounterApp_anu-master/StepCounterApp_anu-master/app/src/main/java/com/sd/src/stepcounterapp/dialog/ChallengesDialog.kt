package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.convertToLocal
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.challenge.Ongoing
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.TARSTEPS
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_challenges.*
import kotlinx.android.synthetic.main.fragment_rewardschallenges.*

class ChallengesDialog(
    context: Context,
    data: Data,
    var mOngoingData: Ongoing,
    themeResId: Int,
    private val LayoutId: Int,
    var stopVisibility: Boolean,
    var disableStart: Boolean,
    var mListener: StartInterface
) : BaseDialog(context, themeResId) {
    var mData: Data = data

    init {
        val wmlp = this.window!!.attributes
        wmlp.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        window!!.attributes = wmlp
    }

    override fun getInterfaceInstance(): InterfacesCall.IndexClick {
        return this
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateStuff() {
        setData()
        txtDetail.movementMethod = ScrollingMovementMethod()
        if (disableStart) {
            btnStart.isClickable = false
            btnStart.alpha = 0.4f
            btnStart.isEnabled = false
        } else {
            btnStart.isClickable = true
            btnStart.alpha = 1f
            btnStart.isEnabled = true
        }
        btnStart.setOnClickListener {
            mListener.onStart(mData)
        }
        btnStop.setOnClickListener {
            mListener.onStop(mData)
        }

        if(mData.challengeType.equals("weekly-boostup",false)){
            stepsTitle.text = "Average steps: "
            txtSteps.text = mData.averageDailySteps.toString()
            tarStepsLayout.visibility = View.VISIBLE
            txtTarSteps.text = calculateTargetSteps(mData).toString()
        }else{
            tarStepsLayout.visibility = View.GONE
            stepsTitle.text = "Target steps: "
            txtSteps.text = mData.steps.toString()
        }

        if(mOngoingData._id == "" && mOngoingData.completed){
            txtProgress.text = "Completed"
        }else{
            txtProgress.text = "In Progress"
        }
    }

    override fun getContentView(): Int {
        return LayoutId
    }

    fun setData() {
        if (stopVisibility) {         //to show in progress if challenge is taken
            txtProgress.visibility = View.VISIBLE
            btnStop.visibility = View.GONE
            btnStart.visibility = View.GONE
        } else {
            txtProgress.visibility = View.GONE
            btnStop.visibility = View.GONE
            buttonsLayout.visibility = View.VISIBLE
            btnStart.visibility = View.VISIBLE
        }

        Picasso.get().load(IMG_URL + mData.image).into(challengeImg)
        txtName.text = mData.name.capitalize()
        txtDepartment.text = mData.shortDesc.capitalize()
        txtChallengeStartDate.text = changeDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "dd MMM, yyyy",
            mData.startDateTime
        ) + " | " + convertToLocal(mData.startDateTime)
        txtEndDate.text = changeDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "dd MMM, yyyy",
            mData.endDateTime
        ) + " | " + convertToLocal(mData.endDateTime)
//        txtParticipants.text = mData.
        rewardTokens.text = mData.points.toString()+" TKNS"
        txtSteps.text = mData.steps.toString()
        txtDetail.text = HtmlCompat.fromHtml(mData.description.capitalize(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        txtDetail.isSelected = true
        txtParticipants.text = mData.count.toString()
    }


    override fun clickIndex(pos: Int) {
        dismiss()
    }


    interface StartInterface {
        fun onStart(dataObject: Data)
        fun onStop(mData: Data)
    }


    private fun calculateTargetSteps(data: Data): Int {
        var avgSteps = SharedPreferencesManager.getString(
            HayaTechApplication.applicationContext(),
            SharedPreferencesManager.AVGSTEPS
        ).toInt()
        Log.i("Target", "steps$avgSteps")
        return  avgSteps+(avgSteps*data.increaseSteps)/100
    }

}
