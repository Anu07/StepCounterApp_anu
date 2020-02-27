package com.sd.src.stepcounterapp.model.loginrequest

data class ForgetRequestObject(
    val device_id: String,
    val device_type: String,
    val email: String,
    val password: String
)