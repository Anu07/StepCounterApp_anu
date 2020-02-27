package com.sd.src.stepcounterapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.fragments.TnCFragment
import com.sd.src.stepcounterapp.fragments.WebFragment

class DisclaimerActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disclaimer)
        if(intent.getIntExtra("tnc",-1)==0){
            openTnCFragment()
        }else{
            openPrivacyFragment()
        }

    }


    private fun openTnCFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(
            R.id.disclaimerContainer,
            TnCFragment.newInstance(this@DisclaimerActivity
        ))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun openPrivacyFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(
            R.id.disclaimerContainer,
            WebFragment.newInstance(this@DisclaimerActivity
            ))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}