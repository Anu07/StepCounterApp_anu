package com.sd.src.stepcounterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sd.src.stepcounterapp.R
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnBoardingFragment : Fragment(){


    companion object {
        val INDEX = "index"
        /**
         * @param name a string to be displayed on the fragment
         * @param listener click listener to pass click events to the activity
         */
        fun newInstance(name : Int) : Fragment {
            val fragment = OnBoardingFragment()
            val bundle = Bundle()
            bundle.putInt(INDEX, name)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments!!.get(INDEX)==0){
            tutorialImg.setImageResource(R.drawable.slide_1)
        }else if(arguments!!.get(INDEX)==1){
            tutorialImg.setImageResource(R.drawable.slide_2)
        }else{
            tutorialImg.setImageResource(R.drawable.slide_3)
        }
    }

}