package com.sd.src.stepcounterapp.repositories

import android.app.Application
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject


class UserRepository constructor(private val application: Application) {

    //    var data = LoginResponseJ

    fun login(login: LoginRequestObject): LoginResponseJ? {
        var data: LoginResponseJ? = null


        return data!!
    }
}
