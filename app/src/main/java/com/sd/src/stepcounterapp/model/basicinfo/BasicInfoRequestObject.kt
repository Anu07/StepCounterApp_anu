package com.sd.src.stepcounterapp.model

data class BasicInfoRequestObject(
    val userId: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val gender: String,
    val weight: Int,
    val weightType: String,
    val height: Int,
    val heightType: String,
    val bmi: Float,
    val basicFlag: Boolean
)
