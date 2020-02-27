package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sd.src.stepcounterapp.BuildConfig.BASE_URL
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.utils.DisableLeftMenu
import kotlinx.android.synthetic.main.backtitlebar.*
import kotlinx.android.synthetic.main.fragment_faq.*

class OpenFAQFragment : BaseFragment() {

    companion object {
        private var mMenuListener: DisableLeftMenu? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: OpenFAQFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): OpenFAQFragment {
            instance = OpenFAQFragment()
            mContext = context
            if (context is LandingActivity) {
                mMenuListener = context
            }
            return instance
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mMenuListener?.disableLeftMenuSwipe()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        webView.loadUrl("https://hayatech.co/faq") //http://3.15.112.60:4200/faq
        webView.settings.javaScriptEnabled = true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_title.setImageResource(R.drawable.faqs)
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }


    override fun onDetach() {
        super.onDetach()
        Log.i("test", "Detach")
        (requireContext() as LandingActivity).hideBottomLayout(false)
        mMenuListener?.enableLeftMenuSwipe()

    }

}