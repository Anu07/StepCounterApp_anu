package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.viewModels.OpenContactUsViewModel


class OpenContactUsFragment : Fragment() {


    private lateinit var viewModel: OpenContactUsViewModel
    private lateinit var mViewModel: OpenContactUsViewModel

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: OpenContactUsFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): OpenContactUsFragment {
            instance = OpenContactUsFragment()
            mContext = context
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.open_contact_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OpenContactUsViewModel::class.java)
    }


}
