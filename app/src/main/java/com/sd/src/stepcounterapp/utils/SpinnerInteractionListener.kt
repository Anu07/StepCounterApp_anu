package com.sd.src.stepcounterapp.utils

import android.util.Log
import android.widget.AdapterView
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.sd.src.stepcounterapp.activities.LeaderboardActivity


class SpinnerInteractionListener(var mListener: ClickSpinnerItem) : AdapterView.OnItemSelectedListener, OnTouchListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    internal var userSelect = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        userSelect = true
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        if (userSelect) {
            Log.e("tesjk","error")
            mListener.onSpinSelected(pos)
            userSelect = false
        }
    }

    interface ClickSpinnerItem{
        fun onSpinSelected(pos: Int)
    }


}