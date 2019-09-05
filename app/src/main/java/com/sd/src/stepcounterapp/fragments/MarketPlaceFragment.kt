package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.MarketPlaceCategoryAdapter
import com.sd.src.stepcounterapp.adapter.MarketPlacePopularityAdapter
import com.sd.src.stepcounterapp.adapter.MarketPlaceSeeAllAdapter
import com.sd.src.stepcounterapp.dialog.FilterDialog
import com.sd.src.stepcounterapp.interfaces.MarketPlaceClickInterface
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.marketplace.BasicSearchRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.model.wishList.AddWishRequest
import com.sd.src.stepcounterapp.model.wishList.Data
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.MarketPlaceViewModel
import kotlinx.android.synthetic.main.fragment_market_place.*


class MarketPlaceFragment : BaseFragment(), MarketPlaceClickInterface, MarketPlaceCategoryAdapter.twoItemListener,
    MarketPlacePopularityAdapter.PopularInterface, MarketPlaceSeeAllAdapter.CategoryInterface {
    override fun onWish(position: Int, mItem: MarketResponse.Products) {
        if (!mItem.wishlist) {
            showPopupProgressSpinner(true)
            mViewModel.addWishList(AddWishRequest(SharedPreferencesManager.getUserId(context!!), mItem._id))
        } else {
            showPopupProgressSpinner(true)
            mViewModel.removeWishList(AddWishRequest(SharedPreferencesManager.getUserId(context!!), mItem._id))
        }
    }


    override fun onItemwishlisted(position: Int, mItem: MarketResponse.Products) {
        if (!mItem.wishlist) {
            showPopupProgressSpinner(true)
            mViewModel.addWishList(AddWishRequest(SharedPreferencesManager.getUserId(context!!), mItem._id))
        } else {
            showPopupProgressSpinner(true)

            mViewModel.removeWishList(AddWishRequest(SharedPreferencesManager.getUserId(context!!), mItem._id))
        }
    }

    override fun onPopularItemwishlisted(
        position: Int,
        mItem: PopularProducts.Data
    ) {
        if (!mItem.wishlist) {
            showPopupProgressSpinner(true)
            mViewModel.addWishList(AddWishRequest(SharedPreferencesManager.getUserId(context!!), mItem._id))
        } else {
            showPopupProgressSpinner(true)
            mViewModel.removeWishList(AddWishRequest(SharedPreferencesManager.getUserId(context!!), mItem._id))
        }
    }

    override fun onItemClick(position: Int) {
        txtCategoryName.text = mDataCategory[position].name.toString()
    }

    override fun onSeeAllClick(position: Int) {
        txtCategoryName.text = mDataCategory[position].name

        if (rvProduct.isVisible) {
            rvSeeAll.visibility = View.VISIBLE
            llSeeAll.visibility = View.VISIBLE
            rvProduct.visibility = View.GONE
            if (mDataCategory[position].products.size > 0) {
                mSeeAllAdapter = MarketPlaceSeeAllAdapter(mDataCategory[position].products, this)
                rvSeeAll.adapter = mSeeAllAdapter
            }
        } else {
            rvProduct.visibility = View.VISIBLE
            rvSeeAll.visibility = View.GONE
            llSeeAll.visibility = View.GONE
        }

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MarketPlaceFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): MarketPlaceFragment {
            instance = MarketPlaceFragment()
            mContext = context
            return instance
        }
    }

    private val mCartItemCount: Int = 0
    internal lateinit var callback: FragmentClick

    fun FragmentClickListener(callback: FragmentClick) {
        this.callback = callback
    }

    private var tabtype: Boolean = true
    private var mDataWish: ArrayList<Data> = ArrayList()
    private lateinit var mViewModel: MarketPlaceViewModel
    lateinit var mCategoryAdapter: MarketPlaceCategoryAdapter
    lateinit var mPopularityAdapter: MarketPlacePopularityAdapter
    private var mDataCategory: ArrayList<MarketResponse.Data> = ArrayList()
    private var mDataPopularity: ArrayList<PopularProducts.Data> = ArrayList()
    lateinit var mSeeAllAdapter: MarketPlaceSeeAllAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(MarketPlaceViewModel::class.java)
        mViewModel.getCategoryApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
        mViewModel.getProductApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))

        mViewModel.getCategory().observe(this,
            Observer<MarketResponse> { mProduct ->
                if (mProduct != null) {
                    if (mProduct.data.size > 0) {
                        rvProduct.visibility = View.VISIBLE
                        noRec.visibility = View.GONE
                        mDataCategory = mProduct.data
                        mCategoryAdapter = MarketPlaceCategoryAdapter(mDataCategory, mContext, this, this)
                        rvProduct.adapter = mCategoryAdapter
                        txtCategoryName.text = mDataCategory[0].name
                    }
                } else {
                    rvProduct.visibility = View.GONE
                    noRec.visibility = View.VISIBLE
                }
            })

        mViewModel.getPopularity().observe(this,
            Observer<PopularProducts> { mPopProduct ->
                if (mPopProduct != null) {
                    if (mPopProduct.data.size > 0) {
                        rvPopularity.visibility = View.VISIBLE
                        noRecP.visibility = View.GONE
                        mDataPopularity = mPopProduct.data
                        mPopularityAdapter = MarketPlacePopularityAdapter(mDataPopularity, mContext, this)
                        rvPopularity.adapter = mPopularityAdapter
                    }
                } else {
                    rvPopularity.visibility = View.GONE
                    noRecP.visibility = View.VISIBLE
                }
            })
        mViewModel.addWishList().observe(this,
            Observer<BasicInfoResponse> {
                showPopupProgressSpinner(false)
                if (it != null) {
                    if (it.status == 200) {
                        mViewModel.getCategoryApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
                        mViewModel.getProductApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
                    }
                }
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_market_place, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCategoryAdapter()
        setPopularityAdapter()
        setSeeAllAdapter()
        setupBadge()

        txtSeeAll.setOnClickListener {
            rvSeeAll.visibility = View.GONE
            llSeeAll.visibility = View.GONE
            rvProduct.visibility = View.VISIBLE
        }

        txtCategory.setOnClickListener {
            tabtype = true
            txtCategory.setTextColor(resources.getColor(R.color.colorBlack))
            txtPopularity.setTextColor(resources.getColor(R.color.gray_text))
            llCategory.visibility = View.VISIBLE
            llPopularity.visibility = View.GONE
        }

        txtPopularity.setOnClickListener {
            tabtype = false
            txtPopularity.setTextColor(resources.getColor(R.color.colorBlack))
            txtCategory.setTextColor(resources.getColor(R.color.gray_text))
            llCategory.visibility = View.GONE
            llPopularity.visibility = View.VISIBLE
        }

        filter.setOnClickListener {
            FilterDialog(mContext, R.style.pullBottomfromTop,
                R.layout.dialog_filter).show()
        }


        searchView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                if (searchView.text.isNotEmpty())
                    performSearch()
                true
            } else {
                false
            }
        }

        searchView.onRightDrawableClicked {
            if (searchView.text.isNotEmpty()) {
                it.text.clear()
                mViewModel.getCategoryApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
                mViewModel.getProductApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
            }

        }


        searchView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (s.isEmpty()) {
                    mViewModel.getCategoryApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
                    mViewModel.getProductApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
                }

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {


            }
        })

        wishlist.setOnClickListener {
            callback.onFragmentClick(2)
        }
    }

    private fun performSearch() {
        if (tabtype) {
            mViewModel.getSearchCategoryApi(
                BasicSearchRequest(
                    SharedPreferencesManager.getUserId(mContext),
                    searchView.text.toString()
                )
            )
        } else {
            mViewModel.getSearchProductApi(
                BasicSearchRequest(
                    SharedPreferencesManager.getUserId(mContext),
                    searchView.text.toString()
                )
            )
        }
    }

    private fun setCategoryAdapter() {
        rvProduct.layoutManager = LinearLayoutManager(mContext)
        mCategoryAdapter = MarketPlaceCategoryAdapter(mDataCategory, mContext, this, this)
        rvProduct.adapter = mCategoryAdapter
    }

    private fun setPopularityAdapter() {
        rvPopularity.layoutManager = LinearLayoutManager(mContext)
        mPopularityAdapter = MarketPlacePopularityAdapter(mDataPopularity, mContext, this)
        rvPopularity.adapter = mPopularityAdapter
    }

    private fun setSeeAllAdapter() {
        rvSeeAll.layoutManager = LinearLayoutManager(mContext)
    }


    fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }


    fun hideKeyboard() {
        val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity!!.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }




    interface FragmentClick {
        fun onFragmentClick(pos:Int)
    }

    /**
     * to setup wishlist badge count
     */

    fun setupBadge() {
        if (cart_badge != null) {
            if (mCartItemCount == 0) {
                if (cart_badge.visibility != View.GONE) {
                    cart_badge.visibility = View.GONE;
                }
            } else {
                cart_badge.text = "1"
                if (cart_badge.visibility != View.VISIBLE) {
                    cart_badge.visibility = View.VISIBLE;
                }
            }
        }
    }

}
