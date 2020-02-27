package com.sd.src.stepcounterapp.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.fitpolo.support.entity.DailyStep
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sd.src.stepcounterapp.activities.SignInActivity
import com.sd.src.stepcounterapp.model.bmi.BMIObject
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.profile.Data
import java.util.*


object SharedPreferencesManager {

    private val APP_SETTINGS = "APP_SETTINGS"
    val FIREBASETOKEN = "FirebaseToken"
    val GA_NOTIFY = "goal_achieved"
    val NSA_NOTIFY = "new_survey_added"
    val NC_NOTIFY = "new_challenge"
    val VAR_WEARABLE = "Wearable"
    val VAR_WEARABLE_CONNECTED = "wearable_connected"
    val VAR_USEROBJECT = "User"
    val VAR_USEREXISTING = "ExistingUser"
    val VAR_ELCTOKEN = "ElciesToken"
    val VAR_ELCIES_CONNCTED = "elciesconnected"
    val TARSTEPS = "TargetSteps"
    // properties
    private val USERID = "userId"
    val SYNCDATE = "syncDate"
    val SYNCTIME = "syncTime"
    val WEARABLEID = "wearableId"
    val NEWWEARABLEID = "newwearableId"
    val WISHCOUNT = "wishbadge"
    val EARNEDTOKENS = "earnedTokens"
    val AVGSTEPS = "avgSteps"
    val PREF_CURR_WEARABLE = "current_Wearable_address"
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getUserId(context: Context): String? {
        return getSharedPreferences(context).getString(USERID, null)
    }

    fun setUserId(context: Context, newValue: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(USERID, newValue)
        editor.apply()
    }

    fun setString(context: Context, newValue: String?, Key: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Key, newValue)
        editor.apply()
    }

    fun setBoolean(context: Context, newValue: Boolean, Key: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Key, newValue)
        editor.apply()
    }

    fun getString(context: Context, Key: String): String {
        return if (hasKey(context, Key)) {
            getSharedPreferences(context).getString(Key, null)
        } else {
            ""
        }
    }

    fun getBoolean(context: Context, Key: String): Boolean {
        return if (hasKey(context, Key)) {
            return getSharedPreferences(context).getBoolean(Key, false)
        } else {
            false
        }
    }


    fun setInt(context: Context, Key: String, newValue: Int) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(Key, newValue)
        editor.apply()
    }

    fun getInt(context: Context, Key: String): Int? {
        return if (hasKey(context, Key)) {
            return getSharedPreferences(context).getInt(Key, -1)
        } else {
            -1
        }
    }

    fun logout(context: Context) {
//        var wearAddress = getString(context, WEARABLEID)
//        var wearConnected = getString(context, VAR_WEARABLE)
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
        setBoolean(context, true, VAR_USEREXISTING)
        /* setString(context,wearAddress
             ,WEARABLEID)
         setString(context,wearConnected
             ,VAR_WEARABLE)*/
        // After logout redirect user to Login Activity
        val i = Intent(context, SignInActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        // Staring Login Activity
        context.startActivity(i)
    }


    fun saveUserObject(context: Context, myObject: LoginResponseJ) {
        val prefsEditor = getSharedPreferences(context).edit()
        val gson = Gson()
        val json = gson.toJson(myObject) // myObject - instance of MyObject
        prefsEditor.putString(VAR_USEROBJECT, json)
        prefsEditor.apply()
    }

    fun saveBmiObject(context: Context, myObject: BMIObject) {
        val prefsEditor = getSharedPreferences(context).edit()
        val gson = Gson()
        val json = gson.toJson(myObject) // myObject - instance of MyObject
        prefsEditor.putString("BMI", json)
        prefsEditor.apply()
    }


    fun getBmiObject(context: Context): BMIObject {
        val gson = Gson()
        val json = getSharedPreferences(context).getString("BMI", "")
        val bmi: BMIObject = gson.fromJson(json, BMIObject::class.java)
        return bmi
    }


    fun saveUpdatedUserObject(context: Context, myObject: Data) {
        val prefsEditor = getSharedPreferences(context).edit()
        val gson = Gson()
        val json = gson.toJson(myObject) // myObject - instance of MyObject
        prefsEditor.putString("UpdatedUser", json)
        prefsEditor.apply()
    }


    fun saveSyncObject(context: Context, myWearData: ArrayList<DailyStep>?) {
        val prefsEditor = getSharedPreferences(context).edit()
        val gson = Gson()
        val json = gson.toJson(myWearData) // myObject - instance of MyObject
        prefsEditor.putString(VAR_WEARABLE, json)
        prefsEditor.apply()
    }

    fun getSyncObject(context: Context): ArrayList<DailyStep>? {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(VAR_WEARABLE, "")
        val type = object : TypeToken<List<DailyStep>>() {}.type
        val array: ArrayList<DailyStep> = gson.fromJson(json, type)
        return array
    }

    fun getUserObject(context: Context): LoginResponseJ {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(VAR_USEROBJECT, "")
        val obj: LoginResponseJ = gson.fromJson<LoginResponseJ>(json, LoginResponseJ::class.java)
        return obj
    }

    fun getUpdatedUserObject(context: Context): Data {
        val gson = Gson()
        val json = getSharedPreferences(context).getString("UpdatedUser", "")
        val obj: Data = gson.fromJson(json, Data::class.java)
        return obj
    }

    fun hasKey(context: Context, key: String): Boolean {
        return getSharedPreferences(context).contains(key)
    }


    fun removeKey(context: Context, key: String) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(key)
        editor.apply()
    }

}