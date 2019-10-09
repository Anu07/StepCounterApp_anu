package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.sd.src.stepcounterapp.HayaTechApplication.Companion.TAG
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.fragment_faq.*

class OpenFAQFragment : BaseFragment() {

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
        webView.loadUrl("http://3.15.112.60:4200/faq") //http://3.15.112.60:4200/faq
        webView.settings.javaScriptEnabled = true

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }


    override fun onDetach() {
        super.onDetach()
        Log.i("test","Detach")
        (mContext as LandingActivity).hideBottomLayout(false)
    }

}