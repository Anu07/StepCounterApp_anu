package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.sd.src.stepcounterapp.R


class SlidingImageAdapter(private val context: Context, private val IMAGES: ArrayList<Int>) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.sliding_img_layout, view, false)!!
        val parentView = imageLayout!!.findViewById<CardView>(R.id.cardslideMain)
        val imageView = imageLayout!!
            .findViewById(R.id.image) as ImageView


        imageView.setImageResource(IMAGES[position])


        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


}


