package com.sd.src.stepcounterapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sd.src.stepcounterapp.network.RetrofitClient

class SurveyViewModel(application: Application) : AndroidViewModel(application) {
    val call = RetrofitClient.instance




}