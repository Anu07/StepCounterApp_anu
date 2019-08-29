package com.sd.src.stepcounterapp.model.rewards

data class MyRedeemedResponse(
    val `data`: List<RewardsData>,
    val message: String,
    val status: Int
)