package com.sd.src.stepcounterapp.model.loginrequest

data class LoginRequestObject(
    val device_id: String,
    val device_type: String,
    val email: String,
    val password: String,
    val offset: Int

)