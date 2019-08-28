package com.sd.src.stepcounterapp

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by shubham on 12/06/19.
 */

fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    text?.let { Toast.makeText(this, it, duration).show() }
}

fun Editable.toStringOrNull(): String? {
    val str = toString()
    return if (str.isEmpty()) null else str
}

fun Context.showProgess() {
//    LoadingDialog.getLoader().showLoader(this)
}

fun hideProgress() {
//    LoadingDialog.getLoader().dismissLoader()
}

fun ImageView.loadUserPhoto(photoUrl: String?) = ifNotDestroyed {
//    Glide.with(this).load(photoUrl).fallback(R.drawable.ic_launcher_foreground).into(this)
}

fun ImageView.loadImage(image: String?) = ifNotDestroyed {
//    Glide.with(this).load(image).centerCrop().into(this)
}

fun ImageView.loadImageOrHide(image: String?) =
    if (image != null) {
        visibility = View.VISIBLE
        loadImage(image)
    } else {
        visibility = View.GONE
    }

private fun View.ifNotDestroyed(block: () -> Unit) {
    if (!(context as Activity).isDestroyed) {
        block()
    }
}

fun changeDateFormat(currentFormat: String, requiredFormat: String, dateString: String?): String {
    var result = ""
    if (dateString.isNullOrEmpty()) {
        return result
    }
    val formatterOld = SimpleDateFormat(currentFormat, Locale.getDefault())
    val formatterNew = SimpleDateFormat(requiredFormat, Locale.getDefault())
    var date: Date? = null
    try {
        date = formatterOld.parse(dateString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    if (date != null) {
        result = formatterNew.format(date)
    }
    return result
}

fun setFirstCapWord(word: String?): String {
    return if (word != null && !word.isEmpty()) {
        word.substring(0, 1).toUpperCase() + word.substring(1)
    } else {
        ""
    }
}