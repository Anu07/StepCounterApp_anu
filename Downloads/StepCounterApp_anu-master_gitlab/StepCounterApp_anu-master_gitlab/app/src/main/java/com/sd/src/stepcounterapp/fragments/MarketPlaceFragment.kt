package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.HayaTechApplication
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
import com.sd.src.stepcounterapp.model.marketplace.walletInfo.WalletModelResponse
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wallet.purchase.PurchaseResponse
import com.sd.src.stepcounterapp.model.wishList.AddWishRequest
import com.sd.src.stepcounterapp.utils.EndlessRecyclerOnScrollListener
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.EARNEDTOKENS
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.viewModels.MarketPlaceViewModel
import kotlinx.android.synthetic.main.fragment_market_place.*


class MarketPlaceFragment : BaseFragment(), FilterDialog.MarketFilterInterface,
    MarketPlaceClickInterface,
    MarketPlaceCategoryAdapter.twoItemListener,
    MarketPlaceCategoryAdapter.ClickMarketListener,
    MarketPlacePopularityAdapter.PopularInterface, MarketPlaceSeeAllAdapter.CategoryInterface {


    override fun onCategoryList(queryCat: String) {
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            mViewModel.getSearchCategoryApi(
                BasicSearchRequest(
                    SharedPreferencesManager.getUserId(mContext),
                    queryCat
                )
            )
        }
    }

    override fun onPopClick(position: Int, mItem: PopularProducts.Data) {
        marketPopDialog = MarketPopPlaceDialog(mContext,
            mItem,
            R.style.pullBottomfromTop,
            R.layout.dialog_marketplace,
            object : MarketPopPlaceDialog.PurchaseInterface {
                override fun onWishlist(data: PopularProducts.Data) {
                    if (!data.wishlist) {
                        showPopupProgressSpinner(true)
                        mViewModel.addWishList(
                            AddWishRequest(
                                SharedPreferencesManager.getUserId(
                                    context!!
                                ), data._id
                            )
                        )
                    } else {
                        showPopupProgressSpinner(true)
                        mViewModel.removeWishList(
                            AddWishRequest(
                                SharedPreferencesManager.getUserId(
                                    context!!
                                ), data._id
                            )
                        )
                    }
                }

                override fun onPurchase(data: PopularProducts.Data) {
                    if (mItem.quantity > 0) {
                        showPopupProgressSpinner(true)
                        mViewModel.hitPurchaseApi(
                            RedeemRequest(
                                mItem._id,
                                SharedPreferencesManager.getUserId(mContext)
                            )
                        )
                    } else {
                        Toast.makeText(
                            mContext,
                            "This product has been out of stock, please try another product.",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }
            })
        marketPopDialog.show()
    }

    override fun onClick(position: Int, mItem: MarketResponse.Products) {
        marketDialog = MarketPlaceDialog(mContext,
            mItem,
            R.style.pullBottomfromTop,
            R.layout.dialog_marketplace,
            object : MarketPlaceDialog.PurchaseInterface {
                override fun onWishlist(data: MarketResponse.Products) {
                    if (!data.wishlist) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            showPopupProgressSpinner(true)
                            mViewModel.addWishList(
                                AddWishRequest(
                                    SharedPreferencesManager.getUserId(
                                        context!!
                                    ), data._id
                                )
                            )
                        }
                    } else {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            showPopupProgressSpinner(true)
                            mViewModel.removeWishList(
                                AddWishRequest(
                                    SharedPreferencesManager.getUserId(
                                        context!!
                                    ), data._id
                                )
                            )
                        }
                    }

                }

                override fun onPurchase(data: MarketResponse.Products) {
                    if (mItem.quantity > 0) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            showPopupProgressSpinner(true)
                            mViewModel.hitPurchaseApi(
                                RedeemRequest(
                                    mItem._id,
                                    SharedPreferencesManager.getUserId(mContext)
                                )
                            )
                        }
                    } else {
                        Toast.makeText(
                            mContext,
                            "This product has been out of stock, please try another product.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
        marketDialog.show()
    }

    override fun onWish(position: Int, mItem: MarketResponse.Products) {
        if (!mItem.wishlist) {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                showPopupProgressSpinner(true)
                mViewModel.addWishList(
                    AddWishRequest(
                        SharedPreferencesManager.getUserId(context!!),
                        mItem._id
                    )
                )
            }
        } else {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                showPopupProgressSpinner(true)
                mViewModel.removeWishList(
                    AddWishRequest(
                        SharedPreferencesManager.getUserId(context!!),
                        mItem._id
                    )
                )
            }
        }
    }


    override fun onItemwishlisted(position: Int, mItem: MarketResponse.Products) {
        if (!mItem.wishlist) {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                showPopupProgressSpinner(true)
                mViewModel.addWishList(
                    AddWishRequest(
                        SharedPreferencesManager.getUserId(context!!),
                        mItem._id
                    )
                )
            }
        } else {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                showPopupProgressSpinner(true)
                mViewModel.removeWishList(
                    AddWishRequest(
                        SharedPreferencesManager.getUserId(context!!),
                        mItem._id
                    )
                )
            }
        }
    }

    override fun onPopularItemwishlisted(
        position: Int,
        mItem: PopularProducts.Data
    ) {
        if (!mItem.wishlist) {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                showPopupProgressSpinner(true)
                mViewModel.addWishList(
                    AddWishRequest(
                        SharedPreferencesManager.getUserId(context!!),
                        mItem._id
                    )
                )
            }
        } else {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                showPopupProgressSpinner(true)
                mViewModel.removeWishList(
                    AddWishRequest(
                        SharedPreferencesManager.getUserId(context!!),
                        mItem._id
                    )
                )
            }
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
                mSeeAllAdapter =
                    MarketPlaceSeeAllAdapter(mDataCategory[position].products, this, this)
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

    var loading = true
    private var page: Int = 0
    lateinit var filterdial: FilterDialog
    private val mCategoryNameList: ArrayList<String> = ArrayList()
    var count: Int? = 0
    private lateinit var marketDialog: MarketPlaceDialog
    private lateinit var marketPopDialog: MarketPopPlaceDialog
    private var tabtype: Boolean = true
    private lateinit var mViewModel: MarketPlaceViewModel
    lateinit var mCategoryAdapter: MarketPlaceCategoryAdapter
    lateinit var mPopularityAdapter: MarketPlacePopularityAdapter
    private var mDataCategory: ArrayList<MarketResponse.Data> = ArrayList()
    private var mDataPopularity: ArrayList<PopularProducts.Data> = ArrayList()
    lateinit var mSeeAllAdapter: MarketPlaceSeeAllAdapter
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(MarketPlaceViewModel::class.java)
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            mViewModel.getCategoryApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), 0))
            mViewModel.getProductApi(BasicRequest(SharedPreferencesManager.getUserId(mContext), 0))
        }
        mViewModel.getPurchase().observe(this,
            Observer<PurchaseResponse> { mData ->
                showPopupProgressSpinner(false)
                try {
                    if (marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                    if (marketPopDialog.isShowing) {
                        marketPopDialog.dismiss()
                    }
                } catch (e: Exception) {
                }
                if (mData.status == 200) {
                    Toast.makeText(
                        WalletFragment.mContext,
                        "Product purchased successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                        mViewModel.getCategoryApi(
                            BasicRequest(
                                SharedPreferencesManager.getUserId(
                                    mContext
                                ), 0
                            )
                        )
                    }
                } else if (mData.status == 400) {
                    Toast.makeText(
                        WalletFragment.mContext,
                        "Insufficient tokens",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })


        mViewModel.getWalletData().observe(this,
            Observer<WalletModelResponse> { mData ->
                if (mData!=null && mData.data != null) {
                    SharedPreferencesManager.setInt(
                        WalletFragment.mContext,
                        SharedPreferencesManager.WISHCOUNT, mData.data?.wishlist!!.size
                    )
                    count = SharedPreferencesManager.getInt(
                        mContext,
                        SharedPreferencesManager.WISHCOUNT
                    )
                    SharedPreferencesManager.setString(
                        mContext,
                        mData.data.totalEarnings.toString(),
                        EARNEDTOKENS
                    )
                    setupBadge()
                    setUpTokens()
                }

            })

        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

            mViewModel.hitWalletApi()
        }
        mViewModel.getCategory().observe(this,
            Observer<MarketResponse> { mProduct ->
                try {
                    showPopupProgressSpinner(false)

                    if (filterdial.isShowing) {
                        filterdial.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("Dialog", "Filter" + e.message)
                }
                if (mProduct != null) {
                    if (mProduct.data != null && mProduct.data.size > 0) {
                        mDataCategory = ArrayList()
                        rvProduct.visibility = View.VISIBLE
                        noRec.visibility = View.GONE
                        mDataCategory = mProduct.data
                        getCategoryNameList(mDataCategory)
//                        mCategoryAdapter.swap(mDataCategory)
                        mCategoryAdapter =
                            MarketPlaceCategoryAdapter(mDataCategory, mContext, this, this, this)
                        rvProduct.adapter = mCategoryAdapter
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
                showPopupProgressSpinner(false)
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
                try {
                    showPopupProgressSpinner(false)
                } catch (e: Exception) {
                }
                try {
                    if (marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                    if (marketPopDialog.isShowing) {
                        marketPopDialog.dismiss()
                    }
                } catch (e: Exception) {
                }
                if (it != null) {
                    if (it.status == 200) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            mViewModel.getCategoryApi(
                                BasicRequest(
                                    SharedPreferencesManager.getUserId(
                                        mContext
                                    ), 0
                                )
                            )
                            mViewModel.getProductApi(
                                BasicRequest(
                                    SharedPreferencesManager.getUserId(
                                        mContext
                                    ), page
                                )
                            )
                            mViewModel.hitWalletApi()
                        }
                    }
                }
            })

        mViewModel.removeWishList().observe(this,
            Observer<BasicInfoResponse> {
                showPopupProgressSpinner(false)
                try {
                    if (marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                    if (marketPopDialog.isShowing) {
                        marketPopDialog.dismiss()
                    }
                } catch (e: Exception) {
                }
                if (it != null) {
                    if (it.status == 200) {
                        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                            mViewModel.hitWalletApi()
                            mViewModel.getCategoryApi(
                                BasicRequest(
                                    SharedPreferencesManager.getUserId(
                                        mContext
                                    ), 0
                                )
                            )
                            mViewModel.getProductApi(
                                BasicRequest(
                                    SharedPreferencesManager.getUserId(
                                        mContext
                                    ), page
                                )
                            )
                        }
                    }
                }
            })


    }

    private fun getCategoryNameList(mDataCategory: java.util.ArrayList<MarketResponse.Data>) {

        mDataCategory.forEach {
            if (!mCategoryNameList.contains(it.name)) {
                mCategoryNameList.add(it.name)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_market_place, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            (mContext as LandingActivity).showDisconnection(false)
            (mContext as LandingActivity).disableSwipe(false)
        } catch (e: Exception) {
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCategoryAdapter()
        setPopularityAdapter()
        setSeeAllAdapter()
        setUpTokens()
        setupBadge()
//        (mContext as LandingActivity).showDisconnection(false)
        txtSeeAll.setOnClickListener {
            rvSeeAll.visibility = View.GONE
            llSeeAll.visibility = View.GONE
            rvProduct.visibility = View.VISIBLE
        }

        txtCategory.setOnClickListener {
            tabtype = true
            txtCategory.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
            txtPopularity.setTextColor(ContextCompat.getColor(mContext, R.color.gray_text))
            llCategory.visibility = View.VISIBLE
            llPopularity.visibility = View.GONE
        }

        txtPopularity.setOnClickListener {
            tabtype = false
            txtPopularity.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
            txtCategory.setTextColor(ContextCompat.getColor(mContext, R.color.gray_text))
            llCategory.visibility = View.GONE
            llPopularity.visibility = View.VISIBLE
        }

        filter.setOnClickListener {
            filterdial = FilterDialog(
                mContext, R.style.pullBottomfromTop,
                R.layout.dialog_filter, this,
                mCategoryNameList
            )
            filterdial.show()
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
                if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                    mViewModel.getCategoryApi(
                        BasicRequest(
                            SharedPreferencesManager.getUserId(
                                mContext
                            ), 0
                        )
                    )
                    mViewModel.getProductApi(
                        BasicRequest(
                            SharedPreferencesManager.getUserId(
                                mContext
                            ), page
                        )
                    )
                }
            }

        }


        searchView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (s.isEmpty()) {
                    if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                        mViewModel.getCategoryApi(
                            BasicRequest(
                                SharedPreferencesManager.getUserId(
                                    mContext
                                ), 0
                            )
                        )
                        mViewModel.getProductApi(
                            BasicRequest(
                                SharedPreferencesManager.getUserId(
                                    mContext
                                ), page
                            )
                        )
                    }
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
            (mContext as LandingActivity).onFragment(4)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvProduct.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            }
        }

        topLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, ev: MotionEvent?): Boolean {
                if (ev!!.action == MotionEvent.ACTION_DOWN) {
                    (mContext as LandingActivity).disableSwipe(false)
                } else if (ev.action == MotionEvent.ACTION_UP) {
                    //no need
                }

                return true
            }
        })
        (mContext as LandingActivity).disableSwipe(true)

    }

    /**
     * set earned tokens
     */

    private fun setUpTokens() {
        if (SharedPreferencesManager.hasKey(mContext, EARNEDTOKENS)) {
            tokenVal.text = SharedPreferencesManager.getString(mContext, EARNEDTOKENS)
        } else {
            tokenVal.text = ""
        }
    }

    private fun performSearch() {
        if (tabtype) {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                showPopupProgressSpinner(true)
                mViewModel.getSearchCategoryApi(
                    BasicSearchRequest(
                        SharedPreferencesManager.getUserId(mContext),
                        searchView.text.toString()
                    )
                )
            }
        } else {
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                showPopupProgressSpinner(true)
                mViewModel.getSearchProductApi(
                    BasicSearchRequest(
                        SharedPreferencesManager.getUserId(mContext),
                        searchView.text.toString()
                    )
                )
            }
        }
    }

    private fun setCategoryAdapter() {
        rvProduct.layoutManager = LinearLayoutManager(mContext)
        mCategoryAdapter = MarketPlaceCategoryAdapter(mDataCategory, mContext, this, this, this)
        rvProduct.adapter = mCategoryAdapter
    }

    private fun setPopularityAdapter() {
        var mlayoutManager = LinearLayoutManager(mContext)
        rvPopularity.isNestedScrollingEnabled = false
        rvPopularity.layoutManager = mlayoutManager
        mPopularityAdapter = MarketPlacePopularityAdapter(mDataPopularity, mContext, this)
        rvPopularity.adapter = mPopularityAdapter
        rvPopularity.addOnScrollListener(object:EndlessRecyclerOnScrollListener(){
            override fun onLoadMore(currentPage: Int) {
                showPopupProgressSpinner(true)
                mViewModel.getProductApi( BasicRequest(
                    SharedPreferencesManager.getUserId(
                        mContext
                    ), currentPage
                ))
            }

        })
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

