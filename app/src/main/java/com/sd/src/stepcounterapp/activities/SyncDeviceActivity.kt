package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.RecyclerSyncGridAdapter
import com.sd.src.stepcounterapp.model.rewards.Data
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sdi.sdeiarchitecturemvvm.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_select_syncsdk.*



class SyncDeviceActivity : BaseActivity<BaseViewModel>(),
    RecyclerSyncGridAdapter.ItemListener {
    override fun onItemClick(item: Data) {

    }

    private lateinit var adapter: RecyclerSyncGridAdapter
    override val layoutId: Int
        get() = R.layout.activity_select_syncsdk
    override val viewModel: BaseViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { BaseViewModel(application) }).get(BaseViewModel::class.java)
    override val context: Context
        get() = this@SyncDeviceActivity
    val numbers: IntArray = intArrayOf(R.drawable.hayatech_fitness,R.drawable.applewatch, R.drawable.polar,  R.drawable.strava)
    private lateinit var gridLayoutManager: GridLayoutManager
    var recyclerView: RecyclerView? = null


    override fun onCreate() {
        recyclerView = findViewById(R.id.syncsCategory)
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = gridLayoutManager

        adapter = RecyclerSyncGridAdapter(this,numbers, this)
        recyclerView!!.adapter = adapter

        skipBttn.setOnClickListener {
            val intent = Intent(mContext, LandingActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
            startActivity(intent)
        }

        cntnBttn.setOnClickListener{
            val intent = Intent(mContext, GuideActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
            startActivity(intent)
        }
    }

    override fun initListeners() {
    }

}