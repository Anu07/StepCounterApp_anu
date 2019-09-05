package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sd.src.stepcounterapp.R
import kotlinx.android.synthetic.main.fragment_faq.*

class OpenFAQFragment : Fragment() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: OpenFAQFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): OpenFAQFragment {
            instance = OpenFAQFragment()
            mContext = context
            return instance
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        webView.loadUrl("https:www.google.com") //http://3.15.112.60:4200/faq

    }

}