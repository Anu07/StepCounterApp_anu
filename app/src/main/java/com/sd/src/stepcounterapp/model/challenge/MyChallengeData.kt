package com.sd.src.stepcounterapp.model.challenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyChallengeData(
    @SerializedName("_id")
    @Expose
    val _id: String,

    @SerializedName("bonusPoint1")
    @Expose
    val bonusPoint1: Int,

    @SerializedName("bonusPoint2")
    @Expose
    val bonusPoint2: Int,

    @SerializedName("bonusPoint3")
    @Expose
    val bonusPoint3: Int,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("duration")
    @Expose
    val duration: Int,

    @SerializedName("image")
    @Expose
    val image: String,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("points")
    @Expose
    val points: Int,

    @SerializedName("shortDesc")
    @Expose
    val shortDesc: String,

    @SerializedName("startDateTime")
    @Expose
    val startDateTime: String,

    @SerializedName("steps")
    @Expose
    val steps: Int
)