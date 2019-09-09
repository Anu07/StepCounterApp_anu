package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.activities.LandingActivity
import com.sd.src.stepcounterapp.adapter.MarketPlaceCategoryAdapter
import com.sd.src.stepcounterapp.adapter.MarketPlacePopularityAdapter
import com.sd.src.stepcounterapp.adapter.MarketPlaceSeeAllAdapter
import com.sd.src.stepcounterapp.dialog.FilterDialog
import com.sd.src.stepcounterapp.dialog.MarketPlaceDialog
import com.sd.src.stepcounterapp.dialog.MarketPopPlaceDialog
import com.sd.src.stepcounterapp.interfaces.MarketPlaceClickInterface
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.marketplace.BasicSearchRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wishList.AddWishRequest
import com.sd.src.stepcounterapp.model.wishList.Data
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.MarketPlaceViewModel
import kotlinx.android.synthetic.main.fragment_market_place.*


class MarketPlaceFragment : BaseFragment(), MarketPlaceClickInterface, MarketPlaceCategoryAdapter.twoItemListener,
    MarketPlaceCategoryAdapter.ClickMarketListener,
    MarketPlacePopularityAdapter.PopularInterface, MarketPlaceSeeAllAdapter.CategoryInterface {
    override fun onPopClick(position: Int, mItem: PopularProducts.Data) {
        marketPopDialog = MarketPopPlaceDialog(mContext, mItem, R.style.pullBottomfromTop, R.layout.dialog_marketplace,
            object : MarketPopPlaceDialog.PurchaseInterface {
                override fun onWishlist(data: PopularProducts.Data) {
                    showPopupProgressSpinner(true)
                    mViewModel.addWishList(AddWishRequest(SharedPreferencesManager.getUserId(context!!), mItem._id))
                }

                override fun onPurchase(data: PopularProducts.Data) {
                    showPopupProgressSpinner(true)
                    mViewModel.hitPurchaseApi(RedeemRequest(mItem._id, SharedPreferencesManager.getUserId(mContext)))

                }
            })
        marketPopDialog!!.show()
    }

    override fun onClick(position: Int, mItem: MarketResponse.Products) {
        marketDialog = MarketPlaceDialog(mContext, mItem, R.style.pullBottomfromTop, R.layout.dialog_marketplace,
            object : MarketPlaceDialog.PurchaseInterface {
                override fun onWishlist(data: MarketResponse.Products) {
                    showPopupProgressSpinner(true)
                    mViewModel.addWishList(AddWishRequest(SharedPreferencesManager.getUserId(context!!), mItem._id))
                }

                override fun onPurchase(data: MarketResponse.Products) {
                    showPopupProgressSpinner(true)
                    mViewModel.hitPurchaseApi(RedeemRequest(mItem._id, SharedPreferencesManager.getUserId(mContext)))

                }
            })
        marketDialog!!.show()
    }

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
                mSeeAllAdapter = MarketPlaceSeeAllAdapter(mDataCategory[position].products, this, this)
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

    var count: Int? = 0
    private lateinit var marketDialog: MarketPlaceDialog
    private lateinit var marketPopDialog: MarketPopPlaceDialog
    private val mCartItemCount: Int = 0
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

        mViewModel.getPurchase().observe(this,
            Observer<BasicInfoResponse> { mData ->
                showPopupProgressSpinner(false)
                try {
                    if (marketDialog != null && marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                } catch (e: Exception) {
                }
                if (mData.status == 200) {
                    Toast.makeText(WalletFragment.mContext, "Product purchased successfully", Toast.LENGTH_LONG).show()
                    mViewModel.getCategoryApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
                } else if (mData.status == 400) {
                    Toast.makeText(WalletFragment.mContext, "Insufficient tokens", Toast.LENGTH_LONG).show()
                }
            })



        mViewModel.getCategory().observe(this,
            Observer<MarketResponse> { mProduct ->
                if (mProduct != null) {
                    if (mProduct.data.size > 0) {
                        rvProduct.visibility = View.VISIBLE
                        noRec.visibility = View.GONE
                        mDataCategory = mProduct.data
                        mCategoryAdapter.swap(mDataCategory)
                        if (mDataCategory.size > 0)
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
                        mPopularityAdapter.swap(mDataPopularity)
                    }
                } else {
                    rvPopularity.visibility = View.GONE
                    noRecP.visibility = View.VISIBLE
                }
            })
        mViewModel.addWishList().observe(this,
            Observer<BasicInfoResponse> {
                showPopupProgressSpinner(false)
                try {
                    if (marketDialog != null && marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                } catch (e: Exception) {
                }
                if (it != null) {
                    if (it.status == 200) {


                        count = count!! + 1

                        SharedPreferencesManager.setInt(
                            WalletFragment.mContext,
                            SharedPreferencesManager.WISHCOUNT, count!!
                        )



                        cart_badge.text = count.toString()
                        mViewModel.getCategoryApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
                        mViewModel.getProductApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), "1"))
                    }
                }
            })

        mViewModel.removeWishList().observe(this,
            Observer<BasicInfoResponse> {
                showPopupProgressSpinner(false)
                try {
                    if (marketDialog != null && marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                } catch (e: Exception) {
                }
                if (it != null) {
                    if (it.status == 200) {

                        count = count!! - 1
                        SharedPreferencesManager.setInt(
                            WalletFragment.mContext,
                            SharedPreferencesManager.WISHCOUNT, count!!
                        )
                        cart_badge.text = count.toString()
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
            FilterDialog(
                mContext, R.style.pullBottomfromTop,
                R.layout.dialog_filter
            ).show()
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
            //            callback.onFragmentClick(2)
            (mContext as LandingActivity).onFragmnet(4)
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
        mCategoryAdapter = MarketPlaceCategoryAdapter(mDataCategory, mContext, this, this, this)
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


//    interface FragmentClick {
//        fun onFragmentClick(pos:Int)
//    }

    /**
     * to setup wishlist badge count
     */

    fun setupBadge() {
        count = SharedPreferencesManager.getInt(
            WalletFragment.mContext,
            SharedPreferencesManager.WISHCOUNT
        )
        if (count == -1 || count == 0) {
//                if (cart_badge.visibility != View.GONE) {
            cart_badge.visibility = View.GONE
//                }
        } else {
//                if (cart_badge.visibility != View.VISIBLE) {
            cart_badge.visibility = View.VISIBLE
            cart_badge.text = count.toString()

//                }
        }
    }
}

