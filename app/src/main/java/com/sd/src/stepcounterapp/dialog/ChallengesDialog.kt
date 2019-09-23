package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.Gravity
import android.view.View
import androidx.annotation.RequiresApi
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.convertToLocal
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_challenges.*

class ChallengesDialog(
    context: Context,
    data: Data,
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
    }

    override fun getContentView(): Int {
        return LayoutId
    }

    fun setData() {
        if (stopVisibility) {         //to show in progress if challenge is taken
            txtProgress.visibility = View.VISIBLE
            btnStop.visibility = View.VISIBLE
            btnStart.visibility = View.GONE
        } else {
            txtProgress.visibility = View.GONE
            btnStop.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
        }

        Picasso.get().load(RetrofitClient.IMG_URL + mData.image).into(challengeImg)
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
        rewardTokens.text = mData.points.toString()
        txtSteps.text = mData.steps.toString()
        txtDetail.text = Html.fromHtml(mData.description.capitalize())
        txtDetail.isSelected = true
    }


    override fun clickIndex(pos: Int) {
        dismiss()
    }


    fun disableStartButton(status: Boolean) {
        btnStart.isClickable = status
    }


    interface StartInterface {
        fun onStart(data: Data)
        fun onStop(mData: Data)
    }


}
