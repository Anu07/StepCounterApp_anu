package com.sd.src.stepcounterapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.marketplace.BasicSearchRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.model.marketplace.walletInfo.WalletModelResponse
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.wallet.purchase.PurchaseResponse
import com.sd.src.stepcounterapp.model.wishlist.AddWishRequest
import com.sd.src.stepcounterapp.model.wishlist.basicwishlist.BasicWishListResponse
import com.sd.src.stepcounterapp.utils.EndlessRecyclerViewScrollListener
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
    override fun onTokenList(tokenStr: String) {
        sortListByToken(tokenStr)
    }


    /**
     * sort List by tokens range
     */

    private fun sortListByToken(tokenStr: String) {
        mFilterPopularity = ArrayList()
        mFilterCategory = ArrayList()
        selectedToken = tokenStr
        var min = 0
        var max = 0
        if (tokenStr.contains(" - ")) {
            min = tokenStr.split(" - ")[0].toInt()
            max = tokenStr.split(" - ")[1].toInt()
        }
        if (tabtype) {            //tabType will be true in case of category view selected and false when popularity selected
            val iterator = mDataCategory.iterator()

            while (iterator.hasNext()) {
                var element: MarketResponse.Data = iterator.next()
                var itrtr: Iterator<MarketResponse.Products> = element.products.iterator()
                while (itrtr.hasNext()) {
                    if (mFilterDataCategory.size > 0) {
                        if (element.name == mFilterDataCategory[0].name) {
                            if (itrtr.next().token in min..max && !mFilterCategory.contains(element)) {
                                var Object: MarketResponse.Data = element
                                Object.products.add(itrtr.next())
                                mFilterCategory.add(Object)
                            }
                        }
                        break
                    } else {
                        if (!mFilterCategory.contains(element)) {
                            var elementNew: MarketResponse.Data = element
                            Log.e("Test","itrt"+element.products.size)
                            elementNew.setProducts(ArrayList())
                            var filteredProducts : ArrayList<MarketResponse.Products> = ArrayList()
                            itrtr.forEach {
                               if(it.token  in min..max){
                                   filteredProducts.add(it)
                               }
                            }
                            elementNew.setProducts(filteredProducts)
                            mFilterCategory.add(elementNew)
                        }
                    }
                }
            }


            if (mFilterCategory.size > 0) {
                noRec.visibility = View.GONE
                rvProduct.visibility = View.VISIBLE
                mCategoryAdapter.swap(mFilterCategory)
            } else {
                noRec.visibility = View.VISIBLE
                rvProduct.visibility = View.GONE
            }
        } else {
            mDataPopularity.iterator().forEach {
                if (it.token.toInt() in min..max) {
                    mFilterPopularity.add(it)
                }
            }
            mFilterPopularity.sortBy { it.token }
            mPopularityAdapter.swap(mFilterPopularity)
        }
    }

    override fun onCategoryList(queryCat: String) {
        if (queryCat == "All") {
            filterAll = true
            selectedCategory = ""
            if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                mViewModel.getCategoryApi(
                    BasicRequest(
                        SharedPreferencesManager.getUserId(
                            mContext
                        ), mCatpage
                    )
                )
            }
        } else {
            filterAll = false
            sortByCategory(queryCat)
        }

    }


    /**
     * Sorting by category
     */

    private fun sortByCategory(queryCat: String) {
//        mFilterDataPopularity = ArrayList()
        mFilterDataCategory = ArrayList()
        mFilterCategory = ArrayList()

        if (tabtype) {            //tabType will be true in case of category view selected and false when popularity selected
            val iterator = mDataCategory.iterator()

            while (iterator.hasNext()) {
                var element: MarketResponse.Data = iterator.next()
                if (element.name.equals(queryCat, true) && !mFilterDataCategory.contains(
                        element
                    )
                ) {
                    mFilterDataCategory.add(element)
                }
            }

            if (mFilterDataCategory.size > 0) {
                selectedCategory = queryCat
                noRec.visibility = View.GONE
                rvProduct.visibility = View.VISIBLE
                mCategoryAdapter.swap(mFilterDataCategory)
            } else {
                noRec.visibility = View.VISIBLE
                rvProduct.visibility = View.GONE
            }
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
                        var alertbuilder = Utils.purchaseNowdialog(requireContext())
                        alertbuilder.setPositiveButton("Yes") { _, _ ->
                            showPopupProgressSpinner(true)
                            mViewModel.hitPurchaseApi(
                                RedeemRequest(
                                    mItem._id,
                                    SharedPreferencesManager.getUserId(mContext)
                                )
                            )
                        }
                        alertbuilder.create().show()
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
                            var alertbuilder = Utils.purchaseNowdialog(requireContext())
                            alertbuilder.setPositiveButton("Yes") { _, _ ->
                                showPopupProgressSpinner(true)
                                mViewModel.hitPurchaseApi(
                                    RedeemRequest(
                                        mItem._id,
                                        SharedPreferencesManager.getUserId(mContext)
                                    )
                                )
                            }
                            alertbuilder.create().show()

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
        if (mItem.wishlisted) {
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
        if (mItem.wishlisted) {
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
//        if (!mItem.wishListed) {
        if (mItem.wishListed) {
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
        clickedParent = position
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
            //when see less is clicked
            clickedParent = -1
            mViewModel.getCategoryApi(
                BasicRequest(
                    SharedPreferencesManager.getUserId(
                        mContext
                    ), 0
                )
            )
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

    private var mWishcountUpdated: Boolean = false
    private var totalCatPages: Int = 0
    private var totalPopPages: Int = 0
    private var selectedCategory: String = ""
    private var selectedToken: String = ""
    private var mFilterDataCategory: ArrayList<MarketResponse.Data> = ArrayList()
    private var filterAll: Boolean = false
    private lateinit var filterScrollListener: EndlessRecyclerViewScrollListener
    private var clickedParent: Int = -1
    private var mCatpage: Int = 1
    private var mPopPage: Int = 1
    lateinit var filterdial: FilterDialog
    private val mCategoryNameList: ArrayList<String> = ArrayList()
    var count: Int? = 0
    private lateinit var marketDialog: MarketPlaceDialog
    private lateinit var marketPopDialog: MarketPopPlaceDialog
    private var tabtype: Boolean = false
    private lateinit var mViewModel: MarketPlaceViewModel
    lateinit var mCategoryAdapter: MarketPlaceCategoryAdapter
    lateinit var mPopularityAdapter: MarketPlacePopularityAdapter
    private var mDataCategory: ArrayList<MarketResponse.Data> = ArrayList()
    private var mFilterCategory: ArrayList<MarketResponse.Data> = ArrayList()
    private var mDataPopularity: ArrayList<PopularProducts.Data> = ArrayList()
    private var mFilterPopularity: ArrayList<PopularProducts.Data> = ArrayList()
    lateinit var mSeeAllAdapter: MarketPlaceSeeAllAdapter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(MarketPlaceViewModel::class.java)

        mViewModel.getPurchase().observe(this,
            Observer<PurchaseResponse> { mData ->
                showPopupProgressSpinner(false)
                try {
                    if (marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }

                } catch (e: Exception) {
                }


                try {
                    if (marketPopDialog.isShowing) {
                        marketPopDialog.dismiss()
                        mCategoryAdapter.swap(mDataCategory)
                        mPopularityAdapter.swap(mDataPopularity)

                    }
                } catch (e: Exception) {
                }
                if (mData.status == 200) {
                    Toast.makeText(
                        mContext,
                        "Product purchased successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    if (Utils.retryInternet(HayaTechApplication.applicationContext())) {

                        mCategoryAdapter.swap(ArrayList())
                        mPopularityAdapter.swap(ArrayList())
                        mViewModel.getCategoryApi(
                            BasicRequest(
                                SharedPreferencesManager.getUserId(
                                    mContext
                                ), mCatpage
                            )
                        )

                        mViewModel.getProductApi(
                            BasicRequest(
                                SharedPreferencesManager.getUserId(
                                    mContext
                                ), mCatpage
                            )
                        )
                    }
                } else if (mData.status == 400) {
                    Toast.makeText(
                        mContext,
                        "Insufficient tokens",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })


        mViewModel.getWalletData().observe(this,
            Observer<WalletModelResponse> { mData ->
                if (mData != null && mData.data != null) {
                    SharedPreferencesManager.setInt(
                        mContext,
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

//        {"status":200,"message":"Success","data":[],"pages":0}
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
                    totalCatPages = mProduct.pages
                    if (mProduct.data.size == 0 && (mProduct.pages == 1 || mProduct.pages == 0)) {
                        rvProduct.visibility = View.GONE
                        noRec.visibility = View.VISIBLE
                    } else if (mProduct.data != null && mProduct.data.size > 0) {
                        rvProduct.visibility = View.VISIBLE
                        noRec.visibility = View.GONE
                        mFilterCategory = ArrayList()

                        mDataCategory = ArrayList()
                        rvProduct.visibility = View.VISIBLE
                        noRec.visibility = View.GONE
                        /*if (mCategoryAdapter.itemCount > 0 && !filterAll) {
                            mDataCategory.addAll(mProduct.data)
                            mCategoryAdapter.notifyItemRangeInserted(
                                mCategoryAdapter.itemCount,
                                mDataCategory.size
                            )
                        } else {*/
                        mDataCategory.addAll(mProduct.data)
                        mCategoryAdapter.swap(mProduct.data)
                        getCategoryNameList(mDataCategory)
//                        }
                        assignCategoryWishList(mDataCategory)
//                        setCategoryAdapter()
                        Log.e("expanded", "parent$clickedParent")
                        if (clickedParent > -1) {
                            onSeeAllClick(clickedParent)
                        }
                        if (mDataCategory.size > 0)
                            txtCategoryName.text = mDataCategory[0].name


                        mSeeAllAdapter =
                            MarketPlaceSeeAllAdapter(ArrayList(), this, this)
                    }/*else {
                        rvProduct.visibility = View.GONE
                        noRec.visibility = View.VISIBLE
                    }*/

                } else {
                    rvProduct.visibility = View.GONE
                    noRec.visibility = View.VISIBLE
                }
            })

        mViewModel.getPopularity().observe(this,
            Observer<PopularProducts> { mPopProduct ->
                showPopupProgressSpinner(false)
                if (mPopProduct != null) {
                    totalPopPages = mPopProduct.pages
                    if (mPopProduct.data.size == 0 && mPopProduct.pages == 1 && mDataPopularity.isEmpty()) {
                        rvPopularity.visibility = View.GONE
                        noRecP.visibility = View.VISIBLE
                    } else if (mPopProduct.data.size > 0) {
                        mFilterPopularity = ArrayList()

                        rvPopularity.visibility = View.VISIBLE
                        noRecP.visibility = View.GONE
                        if (mPopPage == 1) {
                            mDataPopularity.clear()
                        }
                        mDataPopularity.addAll(mDataPopularity.size, mPopProduct.data)
                        mPopularityAdapter.swap(mDataPopularity)
                        /* if (mPopularityAdapter.itemCount > 0 && !filterAll) {
                             mDataPopularity.addAll(mPopProduct.data)
                             mPopularityAdapter.notifyItemRangeInserted(
                                 mPopularityAdapter.itemCount,
                                 mDataPopularity.size
                             )
                         } else {*/
                        /* mDataPopularity.addAll(mPopProduct.data)
                         mPopularityAdapter.swap(mPopProduct.data)*/
//                        }
                        assignPopularityWishList(mDataPopularity)
                    }/* else {
                        rvPopularity.visibility = View.GONE
                        noRecP.visibility = View.VISIBLE
                    }*/
                } else {
                    rvPopularity.visibility = View.GONE
                    noRecP.visibility = View.VISIBLE
                }
            })
        mViewModel.addWishList().observe(this,
            Observer<BasicWishListResponse> {
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
                    Log.i("dialog", "null")
                }

                if (it != null) {
                    if (it.status == 200) {
                        if (tabtype) {
                            mDataCategory.forEachIndexed { _, data ->
                                data.products.forEachIndexed { _, products ->
                                    if (products._id == it.productId) {
                                        products.wishlist = !products.wishlist
                                        products.wishlisted = products.wishlist
                                    }
                                }
                            }
                            mCategoryAdapter.swap(mDataCategory)
                        } else {
                            mDataPopularity.forEachIndexed { _, data ->
                                if (data._id == it.productId) {
                                    data.wishlist = !data.wishlist
                                    data.wishListed = data.wishlist
                                }
                            }
                            mPopularityAdapter.swap(mDataPopularity)
                        }
                        mWishcountUpdated = true
                        mViewModel.hitWalletApi()
                    }
                }
            })

        mViewModel.removeWishList().observe(this,
            Observer<BasicWishListResponse> {
                showPopupProgressSpinner(false)
                try {
                    if (marketDialog != null && marketDialog.isShowing) {
                        marketDialog.dismiss()
                    }
                } catch (e: Exception) {
                    Log.i("Dialog", "Empty")
                }

                try {
                    if (marketPopDialog != null && marketPopDialog.isShowing) {
                        marketPopDialog.dismiss()

                    }
                } catch (e: Exception) {
                    Log.i("pop Dialog", "Empty")

                }
                mWishcountUpdated = true
                if (it != null) {
                    if (it.status == 200) {
                        Log.i("Refresh", "List")
                        if (tabtype) {
                            mDataCategory.forEachIndexed { _, data ->
                                data.products.forEachIndexed { _, products ->
                                    if (products._id == it.productId) {
                                        products.wishlist = !products.wishlist
                                        products.wishlisted = !products.wishlist
                                    }
//                                    mSeeAllAdapter.swap(data.products)
                                }
                            }
                            mCategoryAdapter.swap(mDataCategory)

                        } else if (!tabtype) {
                            mDataPopularity.forEachIndexed { _, data ->
                                if (data._id == it.productId) {
                                    data.wishlist = !data.wishlist
                                    data.wishListed = !data.wishlist
                                }
                            }
                            mPopularityAdapter.swap(mDataPopularity)

                        }
                        mViewModel.hitWalletApi()
                    }

                }
            })


    }


    /**
     * To assign wishlisted same value as wishlist
     */

    private fun assignCategoryWishList(mDataCategory: java.util.ArrayList<MarketResponse.Data>) {
        mDataCategory.forEach {
            it.products.forEachIndexed { _, products ->
                products.wishlisted = products.wishlist
            }
        }
    }


    /**
     * To assign wishlisted same value as wishlist
     */

    private fun assignPopularityWishList(mDataPopularity: ArrayList<PopularProducts.Data>) {
        mDataPopularity.forEachIndexed { index, data ->
            data.wishListed = data.wishlist
        }
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
            (activity as LandingActivity).showDisconnection(false)
        } catch (e: Exception) {
            Log.e("catching", "exception on Attach")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCategoryAdapter()
        setPopularityAdapter()
        setSeeAllAdapter()
        setUpTokens()
        setupBadge()
        txtSeeAll.setOnClickListener {
            clickedParent = -1
            mViewModel.getCategoryApi(
                BasicRequest(
                    SharedPreferencesManager.getUserId(
                        mContext
                    ), mCatpage
                )
            )
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
            if (mWishcountUpdated) {
                if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
                    mPopPage = 1
                    showPopupProgressSpinner(true)
                    mViewModel.getProductApi(
                        BasicRequest(
                            SharedPreferencesManager.getUserId(
                                mContext
                            ), mPopPage
                        )
                    )
                }
            }
        }

        filter.setOnClickListener {
            filterdial = FilterDialog(
                mContext, R.style.pullBottomfromTop,
                R.layout.dialog_filter, this,
                mCategoryNameList, tabtype, selectedToken, selectedCategory
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
                            ), mCatpage
                        )
                    )
                    mViewModel.getProductApi(
                        BasicRequest(
                            SharedPreferencesManager.getUserId(
                                mContext
                            ), mPopPage
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
                                ), mCatpage
                            )
                        )
                        mViewModel.getProductApi(
                            BasicRequest(
                                SharedPreferencesManager.getUserId(
                                    mContext
                                ), mPopPage
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

    }

    /**
     * set earned tokens
     */

    private fun setUpTokens() {
        if (SharedPreferencesManager.hasKey(mContext, EARNEDTOKENS)) {
            tokenVal.text = SharedPreferencesManager.getString(mContext, EARNEDTOKENS) + " TKNS"
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
        var mlayoutManager = LinearLayoutManager(mContext)
        rvProduct.layoutManager = mlayoutManager
        mCategoryAdapter =
            MarketPlaceCategoryAdapter(mDataCategory, mContext, this, this, this)
        rvProduct.adapter = mCategoryAdapter
        rvProduct.isNestedScrollingEnabled = false
        var filterScrollListener = object : EndlessRecyclerViewScrollListener(mlayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (mCatpage < totalCatPages) {
                    mCatpage = page + 1
//                showPopupProgressSpinner(true)
                    mViewModel.getCategoryApi(
                        BasicRequest(
                            SharedPreferencesManager.getUserId(
                                mContext
                            ), mCatpage
                        )
                    )
                }

            }
        }

        rvProduct.addOnScrollListener(filterScrollListener)
    }


    private fun setPopularityAdapter() {
        var mlayoutManager = LinearLayoutManager(mContext)
        rvPopularity.layoutManager = mlayoutManager
        mPopularityAdapter = MarketPlacePopularityAdapter(mDataPopularity, requireContext(), this)
        rvPopularity.adapter = mPopularityAdapter
        rvPopularity.isNestedScrollingEnabled = false
        filterScrollListener = object : EndlessRecyclerViewScrollListener(mlayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (mPopPage <= totalPopPages) {

                    mPopPage = page + 1
//                showPopupProgressSpinner(true)
                    mViewModel.getProductApi(
                        BasicRequest(
                            SharedPreferencesManager.getUserId(
                                mContext
                            ), mPopPage
                        )
                    )
                }
            }
        }
        rvPopularity.addOnScrollListener(filterScrollListener)

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

    private fun setupBadge() {

        if (count == -1 || count == 0) {
            cart_badge.visibility = View.GONE
        } else {
            cart_badge.visibility = View.VISIBLE
            cart_badge.text = count.toString()
        }
    }


    override fun onResume() {
        super.onResume()
        mCatpage = 1
        mPopPage = 1
        if (Utils.retryInternet(HayaTechApplication.applicationContext())) {
            mViewModel.getProductApi(
                BasicRequest(
                    SharedPreferencesManager.getUserId(requireContext()),
                    mPopPage
                )
            )
            mViewModel.getCategoryApi(
                BasicRequest(
                    SharedPreferencesManager.getUserId(requireContext()),
                    mCatpage
                )
            )
            mViewModel.hitWalletApi()
        }
        try {
            (activity as LandingActivity).showDisconnection(false)
        } catch (e: Exception) {
            Log.e("catching", "exception on Attach")
        }
    }
}
