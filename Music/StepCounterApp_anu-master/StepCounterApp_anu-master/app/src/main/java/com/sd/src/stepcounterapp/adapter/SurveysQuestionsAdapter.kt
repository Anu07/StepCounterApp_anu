package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.survey.Datum
import com.sd.src.stepcounterapp.model.survey.Question

class SurveysQuestionsAdapter(
    var mContext: Context,
    var mListener: AnswerListener,
    var mValues: ArrayList<Question>
) :
    RecyclerView.Adapter<SurveysQuestionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_ques_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.text = mValues[position].question
        holder.optionsLayout.removeAllViews()
        if (mValues[position].questionType == "multipleChoice") {
            mValues[position].options.forEachIndexed { index, option ->
                var chck = multipleChoiceType(holder, position, index)
                chck.isChecked = option.isSelected
                holder.optionsLayout.addView(chck)
                chck.setOnCheckedChangeListener { _, _ ->
                    mListener.onAnswer(position, option._id)
                    option.isSelected = true
                }
            }
        } else {
            var radioGrp = RadioGroup(mContext)
            mValues[position].options.forEachIndexed { index, option ->
                var chckd = singleChoiceType(position, index)
                chckd.isChecked = option.isSelected
                radioGrp.addView(chckd)
            }
            radioGrp.setOnCheckedChangeListener { group, _ ->
                if (radioGrp.checkedRadioButtonId > -1) {
                    val selectedId = group.checkedRadioButtonId
                    // find the radiobutton by returned id
                    mValues[position].options.iterator().forEach {
                        it.isSelected = false
                    }
                    mValues[position].options[selectedId].isSelected = true
                    mListener.onAnswer(position, mValues[position].options[selectedId]._id)
                }
            }
            holder.optionsLayout.addView(radioGrp)
        }


    }

    private fun singleChoiceType(
        position: Int,
        index: Int
    ): RadioButton {
        var radioBttn = RadioButton(mContext)
        radioBttn.id = index
        radioBttn.text = mValues[position].options[index].label
        val font = Typeface.createFromAsset(mContext.assets, "fonts/montserrat_regular.ttf")
        radioBttn.typeface = font
        return radioBttn
    }

    private fun multipleChoiceType(
        holder: ViewHolder,
        position: Int,
        index: Int
    ): CheckBox {
        var chck = CheckBox(holder.itemView.context)
        chck.text = mValues[position].options[index].label
        val font = Typeface.createFromAsset(mContext.assets, "fonts/montserrat_regular.ttf")
        chck.typeface = font
        return chck
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView: TextView = v.findViewById<View>(R.id.quesText) as TextView
        var optionsLayout: LinearLayout = v.findViewById(R.id.optionsview) as LinearLayout


        internal lateinit var item: Datum

    }


    interface AnswerListener {
        fun onAnswer(pos: Int, value: String)
    }

}
