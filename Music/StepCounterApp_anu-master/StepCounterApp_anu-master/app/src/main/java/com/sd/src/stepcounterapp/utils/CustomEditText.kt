package com.sd.src.stepcounterapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.sd.src.stepcounterapp.R


class CustomEditText : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        parseAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        parseAttributes(context, attrs)
    }

    private fun parseAttributes(context: Context, attrs: AttributeSet) {
        val values = context.obtainStyledAttributes(attrs, R.styleable.Simplified)

        val typefaceValue = values.getString(R.styleable.Simplified_appEditfont)

        typeface =  Typeface.createFromAsset(context.assets, "fonts/$typefaceValue")
        values.recycle()
    }

}