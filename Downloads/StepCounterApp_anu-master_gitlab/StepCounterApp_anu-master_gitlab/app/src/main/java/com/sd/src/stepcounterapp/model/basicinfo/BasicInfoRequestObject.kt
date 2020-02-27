package com.sd.src.stepcounterapp.model

data class BasicInfoRequestObject(
    val userId: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val gender: String,
    val weight: Float,
    val weightType: String,
    val height: Float,
    val heightType: String,
    val bmi: Double,
    val basicFlag: Boolean
)
