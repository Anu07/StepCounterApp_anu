package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.view.Gravity
import androidx.annotation.RequiresApi
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
    var mListener: StartInterface)
    : BaseDialog(context, themeResId) {
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
        Picasso.get().load(RetrofitClient.IMG_URL+mData.image).into(challengeImg)
        txtName.text = mData.name
        txtDepartment.text = mData.department
        txtStartDate.text = mData.startDateTime
//        txtEndDate.text = mData.
//        txtParticipants.text = mData.
    }


    override fun clickIndex(pos: Int) {
        dismiss()
    }

    interface StartInterface{
        fun onStart(data: Data)
    }
}
