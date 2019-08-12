package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.survey.Products

class SurveysQuestionsAdapter(
    mContext: Context,
    var mValues: ArrayList<Products>
) :
    RecyclerView.Adapter<SurveysQuestionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_ques_item, parent, false)
        return ViewHolder(v)    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = mValues[position].question
        holder.opt1.text =  mValues[position].option1
        holder.opt2.text =  mValues[position].option2
        holder.opt3.text =  mValues[position].option3
        holder.opt4.text =  mValues[position].option4
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView: TextView
        var opt1: RadioButton
        var opt2: RadioButton
        var opt3: RadioButton
        var opt4: RadioButton

        internal lateinit var item: com.sd.src.stepcounterapp.model.challenge.Data

        init {
            textView = v.findViewById<View>(R.id.quesText) as TextView
            opt1 = v.findViewById<View>(R.id.option_1) as RadioButton
            opt2 = v.findViewById<View>(R.id.option_2) as RadioButton
            opt3 = v.findViewById<View>(R.id.option_3) as RadioButton
            opt4 = v.findViewById<View>(R.id.option_4) as RadioButton
        }
    }


}
