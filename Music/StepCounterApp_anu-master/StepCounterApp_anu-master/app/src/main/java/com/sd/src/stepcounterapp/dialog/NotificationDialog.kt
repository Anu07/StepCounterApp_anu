package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.view.Gravity
import androidx.annotation.RequiresApi
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.fragments.SettingsFragment
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.GA_NOTIFY
import kotlinx.android.synthetic.main.dialog_notification_settings.*


class NotificationDialog(
    context: Context,
    themeResId: Int,
    var mListener: NotificationInterface
) : BaseDialog(context, themeResId) {

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
    }

    override fun getContentView(): Int {
        return R.layout.dialog_notification_settings
    }

    fun setData() {
        if (SharedPreferencesManager.hasKey(context, GA_NOTIFY)) {
            goalachievedbttn.isChecked = SharedPreferencesManager.getBoolean(
                context,
                GA_NOTIFY
            )
            newchallnegebttn.isChecked = SharedPreferencesManager.getBoolean(
                context,
                SharedPreferencesManager.NC_NOTIFY
            )
            newsurveybttn.isChecked = SharedPreferencesManager.getBoolean(
                context,
                SharedPreferencesManager.NSA_NOTIFY
            )
        }
        nobttn.setOnClickListener {
            dismiss()
        }
        changebttn.setOnClickListener {
            mListener.onUpdateSettings(goalachievedbttn.isChecked, newchallnegebttn.isChecked ,newsurveybttn.isChecked )
        }
    }

    override fun clickIndex(pos: Int) {
        dismiss()
    }


    interface NotificationInterface {
        fun onUpdateSettings(goal: Boolean,challenge:Boolean,survey:Boolean)

    }
}