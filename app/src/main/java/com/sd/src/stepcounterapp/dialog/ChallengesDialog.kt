package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Gravity
import androidx.annotation.RequiresApi
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.sd.src.stepcounterapp.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_challenges.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChallengesDialog(
    context: Context,
    data: Data,
    themeResId: Int,
    private val LayoutId: Int,
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
        btnStart.setOnClickListener {
            mListener.onStart(mData)
        }
    }

    override fun getContentView(): Int {
        return LayoutId
    }

    fun setData() {
        Picasso.get().load(RetrofitClient.IMG_URL + mData.image).into(challengeImg)
        txtName.text = mData.name.capitalize()
        txtDepartment.text = mData.department.capitalize()
        txtStartDate.text = changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM, yyyy", mData.startDateTime) + " | " + Utils.getTimefromISOTime(mData.startDateTime)
        txtEndDate.text = changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM, yyyy", mData.endDateTime) + " | " + Utils.getTimefromISOTime(mData.endDateTime)
//        txtParticipants.text = mData.
        rewardTokens.text = mData.points.toString()
        txtSteps.text = mData.steps.toString()
    }


    override fun clickIndex(pos: Int) {
        dismiss()
    }

    interface StartInterface {
        fun onStart(data: Data)
    }


}
