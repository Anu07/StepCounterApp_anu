package com.sd.src.stepcounterapp.model.challenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Tranding(
    @SerializedName("__v")
    @Expose
    val __v: Int,

    @SerializedName("_id")
    @Expose
    val _id: String,

    @SerializedName("adminId")
    @Expose
    val adminId: String,

    @SerializedName("bonusPoint1")
    @Expose
    val bonusPoint1: Int,

    @SerializedName("bonusPoint2")
    @Expose
    val bonusPoint2: Int,

    @SerializedName("bonusPoint3")
    @Expose
    val bonusPoint3: Int,

    @SerializedName("department")
    @Expose
    val department: String,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("duration")
    @Expose
    val duration: Int,

    @SerializedName("image")
    @Expose
    val image: String,

    @SerializedName("is_active")
    @Expose
    val is_active: Boolean,

    @SerializedName("is_deleted")
    @Expose
    val is_deleted: Boolean,

    @SerializedName("joinedIn")
    @Expose
    val joinedIn: Int,

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
    val steps: Int,

    @SerializedName("updatedAt")
    @Expose
    val updatedAt: String
)