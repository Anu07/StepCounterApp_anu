package com.sd.src.stepcounterapp.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout


class FrameLayoutForSwipeRefresh : FrameLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun canScrollVertically(direction: Int): Boolean {
        if (super.canScrollVertically(direction)) {
            return true
        }

        val cc = childCount
        for (i in 0 until cc) {
            if (getChildAt(i).canScrollVertically(direction)) {
                return true
            }
        }

        return false
    }
}