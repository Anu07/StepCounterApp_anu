package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.adapter.sectionAdapter.TransactionHistoryAdapter
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.transactionhistory.TransactionEntry
import com.sd.src.stepcounterapp.model.transactionhistory.TransactionHistoryModel
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.TransactionHistoryViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.backtitlebar.*
import java.util.*


class TransactionHistoryFragment : BaseFragment() {

    private lateinit var mViewModel: TransactionHistoryViewModel

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: TransactionHistoryFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): TransactionHistoryFragment {
            instance = TransactionHistoryFragment()
            mContext = context
            return instance
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    /**
     * sort list
     */

    private fun getReverseList(entries: List<TransactionEntry>): List<TransactionEntry> {
        Collections.reverse(entries)
        return entries
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transaction_history, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_title.setImageResource(R.drawable.history)
        img_back.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        mViewModel = ViewModelProviders.of(activity!!).get(TransactionHistoryViewModel::class.java)

        mViewModel.getTransactionHistoryObject().observe(this,
            Observer<TransactionHistoryModel> { mChallenge ->
                mChallenge?.data?.let { list ->
                    showPopupProgressSpinner(false)
                    when (!list.isNullOrEmpty()) {
                        true -> {
                            // Create an instance of SectionedRecyclerViewAdapter
                            val sectionAdapter = SectionedRecyclerViewAdapter()

                            for (i in 0 until list.size) {
                                sectionAdapter.addSection(
                                    TransactionHistoryAdapter(
                                        activity!!,
                                        list[i].id,
                                        getReverseList(list[i].entries)
                                    )
                                )
                            }

                            // Set up your RecyclerView with the SectionedRecyclerViewAdapter
                            val recyclerView = view?.findViewById(R.id.recyclerView) as RecyclerView
                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.adapter = sectionAdapter
                        }
                    }

                }
            })
        showPopupProgressSpinner(true)
        mViewModel.getTransactionHistory(BasicRequest(SharedPreferencesManager.getUserId(mContext), ""))

    }

    override fun onDetach() {
        super.onDetach()
        Log.i("test", "Detach")
        (HayatechFragment.mContext as LandingActivity).hideBottomLayout(false)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i("test", "onDestroy")
    }


}
