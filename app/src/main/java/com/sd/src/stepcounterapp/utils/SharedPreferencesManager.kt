package com.sd.src.stepcounterapp.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.fitpolo.support.entity.DailyStep
import com.sd.src.stepcounterapp.activities.SignInActivity
import com.google.gson.Gson
import com.sd.src.stepcounterapp.model.login.LoginResponseJ


object SharedPreferencesManager {

    private val APP_SETTINGS = "APP_SETTINGS"


    // properties
    private val USERID = "userId"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getUserId(context: Context): String? {
        return getSharedPreferences(context).getString(USERID, null)
    }

    fun setUserId(context: Context, newValue: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(USERID, newValue)
        editor.commit()
    }

    fun setString(context: Context, newValue: String,Key: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Key, newValue)
        editor.commit()
    }
    fun getString(context: Context,Key: String): String? {
        return getSharedPreferences(context).getString(Key, null)
    }

    fun logout(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.commit()

        // After logout redirect user to Loing Activity
        val i = Intent(context, SignInActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        context.startActivity(i)
    }


    fun saveUserObject(context: Context, myObject: LoginResponseJ){
        val prefsEditor = getSharedPreferences(context).edit()
        val gson = Gson()
        val json = gson.toJson(myObject) // myObject - instance of MyObject
        prefsEditor.putString("User", json)
        prefsEditor.commit()
    }


    fun saveSyncObject(context: Context, myWearData: DailyStep){
        val prefsEditor = getSharedPreferences(context).edit()
        val gson = Gson()
        val json = gson.toJson(myWearData) // myObject - instance of MyObject
        prefsEditor.putString("Wearable", json)
        prefsEditor.commit()
    }

    fun getSyncObject(context: Context): DailyStep {
        val gson = Gson()
        val json = getSharedPreferences(context).getString("Wearable", "")
        val obj:DailyStep = gson.fromJson<DailyStep>(json, DailyStep::class.java)
        return obj
    }

    fun getUserObject(context: Context): LoginResponseJ {
        val gson = Gson()
        val json = getSharedPreferences(context).getString("User", "")
        val obj:LoginResponseJ = gson.fromJson<LoginResponseJ>(json, LoginResponseJ::class.java)
        return obj
    }

    fun hasKey(context: Context,key:String): Boolean{
      return getSharedPreferences(context).contains(key)
    }


}