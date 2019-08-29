package com.sd.src.stepcounterapp.dialog

import android.content.Context
import android.os.Build
import android.view.Gravity
import androidx.annotation.RequiresApi
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import kotlinx.android.synthetic.main.dialog_stop_challenges.*

class StopChallengeDialog(context: Context, themeResId: Int, private val LayoutId: Int)
    : BaseDialog(context, themeResId) {
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
        return LayoutId
    }

    fun setData() {
        btnNo.setOnClickListener {
            dismiss()
        }
    }

    fun dismissDialog(){
        dismiss()
    }


    override fun clickIndex(pos: Int) {
        dismiss()
    }
}