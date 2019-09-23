package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sd.src.stepcounterapp.AppConstants.INTENT_FROMAPP
import com.sd.src.stepcounterapp.HayaTechApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.RecyclerGridAdapter
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.rewards.AddRewardsRequestObject
import com.sd.src.stepcounterapp.model.rewards.Data
import com.sd.src.stepcounterapp.model.rewards.RewardsCategoriesResponse
import com.sd.src.stepcounterapp.model.rewards.selectedRewards.SelectedRewardCategories
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.SignInViewModel
import kotlinx.android.synthetic.main.activity_rewards_category_selection.*

class RewardsCategorySelectionActivity : BaseActivity<SignInViewModel>(),
    RecyclerGridAdapter.ItemListener {


    override fun onItemClick(item: Data, position: Int) {
        if (categoriesList.data[position].selectedItem && !selectedCategories!!.contains(item.name)) {
            selectedCategories!!.add(categoriesList.data[position]._id)
        }

    }

    private val mIdRewardList: ArrayList<String> = ArrayList()
    private var mRewardList: MutableList<Data> = ArrayList<Data>()
    private lateinit var adapter: RecyclerGridAdapter
    override val layoutId: Int
        get() = R.layout.activity_rewards_category_selection
    override val viewModel: SignInViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { SignInViewModel(application) }).get(SignInViewModel::class.java)
    override val context: Context
        get() = this@RewardsCategorySelectionActivity
    private lateinit var gridLayoutManager: GridLayoutManager
    var recyclerView: RecyclerView? = null
    var selectedCategories: MutableList<String>? = mutableListOf()
    lateinit var categoriesList: RewardsCategoriesResponse


    override fun onCreate() {
        recyclerView = findViewById(R.id.rewardsCategory)
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = gridLayoutManager

        if (intent.hasExtra(INTENT_FROMAPP)) {
            skipBttn.visibility = View.GONE
        } else {
            skipBttn.visibility = View.VISIBLE
        }

        mViewModel!!.getRewardsCategories().observe(this,
            Observer<RewardsCategoriesResponse> { mRewardsModel ->
                showPopupProgressSpinner(false)
                if (mRewardsModel.data != null) {
                    mRewardList = mRewardsModel.data
                    mRewardsModel.data.iterator().forEach {
                        mIdRewardList.add(it._id)
                    }
                    categoriesList = mRewardsModel
                    if (!intent.hasExtra(INTENT_FROMAPP)) {
                        adapter = RecyclerGridAdapter(this, mRewardsModel.data, this)
                        rewardsCategory.adapter = adapter
                    } else {
                        showPopupProgressSpinner(true)
                        mViewModel!!.getSelectedRewardCategories()
                    }
                }
            })
        mViewModel!!.getSelectedRewardsCategories().observe(this,
            Observer<SelectedRewardCategories> { mRewardsSelectedModel ->
                showPopupProgressSpinner(false)
                if (mRewardsSelectedModel.selected.isNotEmpty()) {
                    mRewardList.iterator().forEach {
                        if (mRewardsSelectedModel.selected.contains(it._id)) {
                            it.selectedItem = true
                            selectedCategories!!.add(it._id)
                        }
                    }
                    Log.e("refresh", "" + mRewardList.size)
                    adapter = RecyclerGridAdapter(this, mRewardList, this)
                    rewardsCategory.adapter = adapter
                }
            })

        mViewModel!!.getBasicResponse().observe(this, Observer { mBaseModel ->
            showPopupProgressSpinner(false)
            if (mBaseModel.status == 200) {
                if (intent.hasExtra(INTENT_FROMAPP)) {
                    Toast.makeText(
                        this@RewardsCategorySelectionActivity,
                        "Categories saved successfully",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    finish()
                } else {

                    var mUser = SharedPreferencesManager.getUserObject(this@RewardsCategorySelectionActivity)
                    var responseLogin = LoginResponseJ()
                    var loginData = com.sd.src.stepcounterapp.model.login.Data(
                        mUser.data.firstName,
                        mUser.data.lastName,
                        mUser.data.image,
                        mUser.data.dob,
                        mUser.data._id,
                        mUser.data.email,
                        mUser.data.username,
                        mUser.data.basicFlag,
                        true
                    )
                    responseLogin.data = loginData
                    SharedPreferencesManager.saveUserObject(this@RewardsCategorySelectionActivity, responseLogin)
                    goToSyncdevice()
                }
            }
        })


        svBttn.setOnClickListener {
            showPopupProgressSpinner(true)
            mViewModel!!.AddRewardsCategory(getRewardsSaveRequest())
        }

        skipBttn.setOnClickListener {

            mViewModel!!.AddRewardsCategory(getRewardsSaveRequest())

        }


    }

    private fun goToSyncdevice() {
        val intent = Intent(mContext, SyncDeviceActivity::class.java)
        //                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
        startActivity(intent)
        finish()
    }

    override fun initListeners() {
        if (HayaTechApplication.hasNetwork()) {
            showPopupProgressSpinner(true)
            mViewModel!!.getRewardCategory()

        } else {
            Toast.makeText(this@RewardsCategorySelectionActivity, "Internet connection unavailable", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun getRewardsSaveRequest(): AddRewardsRequestObject {
        Log.i("categoriess slected", "" + Gson().toJson(selectedCategories))
        return AddRewardsRequestObject(
            selectedCategories,
            true,
            SharedPreferencesManager.getUserId(this@RewardsCategorySelectionActivity)!!
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}