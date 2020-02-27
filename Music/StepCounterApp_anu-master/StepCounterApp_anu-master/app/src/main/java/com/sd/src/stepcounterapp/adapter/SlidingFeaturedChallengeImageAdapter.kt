package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.os.Parcelable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.PagerAdapter
import com.sd.src.stepcounterapp.AppConstants.IMG_URL
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.challenge.Data
import com.sd.src.stepcounterapp.model.challenge.Featured
import com.sd.src.stepcounterapp.network.RetrofitClient
import com.squareup.picasso.Picasso


class SlidingFeaturedChallengeImageAdapter(var context: Context, val images: MutableList<Data>, val listener: ItemSlideListener) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.sliding_img_layout, view, false)!!
        val parentLay = imageLayout.findViewById<CardView>(R.id.cardslideMain)
        val imageView = imageLayout
            .findViewById(R.id.image) as ImageView
        val mainTitleView = imageLayout
            .findViewById(R.id.challengesTitle) as TextView
        val mainSubTitleView = imageLayout
            .findViewById(R.id.challengesSubTitle) as TextView

        Picasso.get().load(IMG_URL+images[position].image).into(imageView)
        mainTitleView.text = images[position].name.capitalize()
        mainSubTitleView.text = HtmlCompat.fromHtml(images[position].shortDesc.capitalize(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.foreground = context.getDrawable(R.drawable.img_ovrlay_slider)
        }*/
        view.addView(imageLayout, 0)

        parentLay.setOnClickListener {
            listener.onImageSlider(position,images[position] )
        }

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


    interface ItemSlideListener {
        fun onImageSlider(position: Int, img: Data)
    }

}