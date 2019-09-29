package com.sd.src.stepcounterapp.model.challenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyChallengeData(

    @SerializedName("_id")
    @Expose
    val _id: String,

    @SerializedName("userId")
    @Expose
    val userId: String,

    @SerializedName("challengeId")
    @Expose
    val challengeId: String,

    @SerializedName("joinedAt")
    @Expose
    val joinedAt: String,

    @SerializedName("__v")
    @Expose
    val __v: Int,

    @SerializedName("stopAt")
    @Expose
    val stopAt: String,

    @SerializedName("is_stop")
    @Expose
    val is_stop: Boolean,

    @SerializedName("completedAt")
    @Expose
    val completedAt: String,

    @SerializedName("is_completed")
    @Expose
    val is_completed: String,

    @SerializedName("invitationId")
    @Expose
    val invitationId: Int,


    @SerializedName("challenge")
    @Expose
    val challenge:Tranding
)