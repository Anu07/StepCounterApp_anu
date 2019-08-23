package com.sd.src.stepcounterapp.adapter.sectionAdapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.changeDateFormat
import com.sd.src.stepcounterapp.model.transactionhistory.TransactionEntry
import com.sd.src.stepcounterapp.setFirstCapWord
import com.wajahatkarim3.easyvalidation.core.view_ktx.contains
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection

// Created by Pardeep on 23/8/19.
// Copyright (c) 2019 (c) Pardeep - All Rights Reserved.

class TransactionHistoryAdapter(val context: Context, val date: String, val list: List<TransactionEntry>) :
    StatelessSection(
        SectionParameters.builder()
            .itemResourceId(R.layout.child_view)
            .headerResourceId(R.layout.header_view)
            .build()
    ) {

    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as ItemViewHolder

        val data = list[position]

        data.transactionName?.let {
            itemHolder.titleTxt.text = setFirstCapWord(it)
        } ?: run {
            itemHolder.titleTxt.text = ""
        }

        data.closingTokens?.let {
            itemHolder.closingToken.text = context.getString(R.string.closing_tokens) + " " + it
        } ?: run {
            itemHolder.closingToken.text = ""
        }

        data.currentTransaction?.let {
            itemHolder.currentTransaction.text = it
            when (it.contains("+")) {
                true -> itemHolder.currentTransaction.setTextColor(Color.parseColor("#8DC540"))
                false -> itemHolder.currentTransaction.setTextColor(Color.parseColor("#FF5454"))
            }
        } ?: run {
            itemHolder.currentTransaction.text = ""
        }

        data.date?.let {
            itemHolder.time.text = changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "HH:mm", it)
        } ?: run {
            itemHolder.time.text = ""
        }

        data.id?.let {
            itemHolder.transactionID.text = context.getString(R.string.transaction_id) + " " + it
        } ?: run {
            itemHolder.transactionID.text = ""
        }

    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val headerHolder = holder as HeaderViewHolder?

        headerHolder?.dateTxt?.text = changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM yyyy", date)
    }

    internal inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val dateTxt: TextView

        init {
            dateTxt = view.findViewById(R.id.dateTxt)
        }
    }

    internal inner class ItemViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        val titleTxt: TextView
        val closingToken: TextView
        val time: TextView
        val transactionID: TextView
        val currentTransaction: TextView

        init {
            closingToken = rootView.findViewById(R.id.closingToken)
            titleTxt = rootView.findViewById(R.id.titleTxt)
            time = rootView.findViewById(R.id.time)
            transactionID = rootView.findViewById(R.id.transactionID)
            currentTransaction = rootView.findViewById(R.id.currentTransaction)
        }
    }
}


