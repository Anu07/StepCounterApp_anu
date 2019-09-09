package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.survey.Datum
import com.sd.src.stepcounterapp.model.survey.Question
import android.graphics.Typeface


class SurveysQuestionsAdapter(
    var mContext: Context,
    var mListener: AnswerListener,
    var mValues: ArrayList<Question>
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
        mValues[position].options.forEachIndexed { index, option ->
            var chck = CheckBox(holder.itemView.context)
            chck.text = mValues[position].options[index].label
            val font = Typeface.createFromAsset(mContext.assets, "fonts/montserrat_regular.ttf")
            chck.typeface = font
            holder.optionsLayout.addView(chck)
            chck.setOnCheckedChangeListener { _, isChecked ->
                    mListener.onAnswer(position,option._id)
            }
        }

    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView: TextView = v.findViewById<View>(R.id.quesText) as TextView
        var optionsLayout: LinearLayout = v.findViewById(R.id.optionsview) as LinearLayout


        internal lateinit var item: Datum

    }


    interface AnswerListener{
        fun onAnswer(pos: Int, value : String)
    }

}
