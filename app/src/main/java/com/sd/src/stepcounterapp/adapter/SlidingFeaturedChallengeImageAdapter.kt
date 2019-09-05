package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.challenge.Featured
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso


class SlidingFeaturedChallengeImageAdapter(var context: Context, private val IMAGES: MutableList<Featured>) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.sliding_img_layout, view, false)!!

        val imageView = imageLayout!!
            .findViewById(R.id.image) as ImageView
        val mainTitleView = imageLayout!!
            .findViewById(R.id.challengesTitle) as TextView
        val mainSubTitleView = imageLayout!!
            .findViewById(R.id.challengesSubTitle) as TextView

        Picasso.get().load(RetrofitClient.IMG_URL+IMAGES[position].image).into(imageView)
        mainTitleView.text = IMAGES[position].name
        mainSubTitleView.text = IMAGES[position].shortDesc
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.foreground = context.getDrawable(R.drawable.img_ovrlay_slider)
        }
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