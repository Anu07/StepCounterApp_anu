package com.sd.src.stepcounterapp.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.annotation.NonNull
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator


class PatternProgressTextAdapter : CircularProgressIndicator.ProgressTextAdapter {

    @NonNull
    override fun formatText(currentProgress: Double): String {


        return String.format(""+currentProgress)
    }
}