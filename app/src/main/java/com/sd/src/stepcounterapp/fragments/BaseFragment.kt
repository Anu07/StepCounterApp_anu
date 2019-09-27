package com.sd.src.stepcounterapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.Window
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.sd.src.stepcounterapp.R


open class BaseFragment : Fragment(){
    private lateinit var progressDialog: Dialog


    fun showPopupProgressSpinner(isShowing: Boolean?) {
        if (isShowing == true) {
            progressDialog = Dialog(activity)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setContentView(R.layout.popup_progressbar)
            progressDialog.setCancelable(true)
            progressDialog.window.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            var progressBar = progressDialog
                .findViewById(R.id.progressBar) as ProgressBar
            progressBar.indeterminateDrawable.setColorFilter(
                Color.parseColor("#8DC540"),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            progressDialog.show()

            dismissDialog()

        } else if (isShowing == false) {
            progressDialog.dismiss()
        }
    }

    private fun dismissDialog() {
        object : CountDownTimer(5000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

                progressDialog.dismiss()
            }
        }.start()
    }

}