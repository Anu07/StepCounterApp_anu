package com.sd.src.stepcounterapp.model.challenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyChallengeResponse(

    @SerializedName("data")
    @Expose
    val `data`: List<MyChallengeData>,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("status")
    @Expose
    val status: Int
)