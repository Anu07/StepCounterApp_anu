package com.sd.src.stepcounterapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button
import com.sd.src.stepcounterapp.R

class CustomButton : Button {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)
            val fontName = a.getString(R.styleable.CustomButton_appButtonfont)

            try {
                if (fontName != null) {
                    val myTypeface = Typeface.createFromAsset(context.assets, "fonts/$fontName")
                    typeface = myTypeface
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            a.recycle()
        }
    }
}