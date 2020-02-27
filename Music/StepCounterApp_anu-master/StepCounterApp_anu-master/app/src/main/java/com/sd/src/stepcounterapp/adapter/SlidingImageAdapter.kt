package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.survey.Datum
import com.sd.src.stepcounterapp.utils.ItemClickGlobalListner


class SlidingImageAdapter(
    context: Context, private var IMAGES: ArrayList<Datum>,
    var mListener: ItemClickGlobalListner) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.sliding_survey_img_layout, view, false)!!
        var surveyTitle = imageLayout.findViewById<TextView>(R.id.challengesTitle)
        surveyTitle.text = IMAGES[position].name.capitalize()

        val imageView = imageLayout
            .findViewById(R.id.image) as ImageView
        imageView.setImageResource(R.drawable.survey_green)
//        imageView.scaleType = ImageView.ScaleType.FIT_XY
//        imageView.adjustViewBounds = true
        imageView.requestLayout()
        imageView.layoutParams.height = 1500
        imageView.layoutParams.width = MATCH_PARENT

        view.addView(imageLayout, 0)
        imageLayout.setOnClickListener {
            mListener.onSlideItemClick(position)
        }
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


    fun swap(imagesNew: ArrayList<Datum>) {
        if (IMAGES.size > 0)
            IMAGES.clear()
        IMAGES = imagesNew
        notifyDataSetChanged()
    }
}