package com.sd.src.stepcounterapp.model.challenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Tranding(

@SerializedName("department")
@Expose
val department: String,

@SerializedName("description")
@Expose
val description: String,

@SerializedName("updatedAt")
@Expose
val updatedAt: String,

@SerializedName("joinedIn")
@Expose
val joinedIn: Int,

@SerializedName("shortDesc")
@Expose
val shortDesc: String,

@SerializedName("is_deleted")
@Expose
val is_deleted: Boolean,

@SerializedName("startDateTime")
@Expose
val startDateTime: String,

@SerializedName("bonusPoint2")
@Expose
val bonusPoint2: Int,

@SerializedName("bonusPoint1")
@Expose
val bonusPoint1: Int,

@SerializedName("__v")
@Expose
val __v: Int,

@SerializedName("bonusPoint3")
@Expose
val bonusPoint3: Int,

@SerializedName("name")
@Expose
val name: String,

@SerializedName("image")
@Expose
val image: String,

@SerializedName("adminId")
@Expose
val adminId: String,

@SerializedName("_id")
@Expose
val _id: String,

@SerializedName("duration")
@Expose
val duration: Int,

@SerializedName("points")
@Expose
val points: Int,

@SerializedName("steps")
@Expose
val steps: Int,

@SerializedName("is_active")
@Expose
val is_active: Boolean
)