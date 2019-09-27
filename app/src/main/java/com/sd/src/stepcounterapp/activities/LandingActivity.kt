package com.sd.src.stepcounterapp.activities

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.location.LocationManager
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.fitpolo.support.MokoConstants
import com.fitpolo.support.MokoSupport
import com.fitpolo.support.callback.MokoScanDeviceCallback
import com.fitpolo.support.entity.BleDevice
import com.fitpolo.support.entity.DailyStep
import com.fitpolo.support.entity.OrderEnum
import com.fitpolo.support.entity.OrderTaskResponse
import com.fitpolo.support.log.LogModule
import com.fitpolo.support.task.ZOpenStepListenerTask
import com.fitpolo.support.task.ZReadStepTask
import com.sd.src.stepcounterapp.AppConstants.*
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.LandingPagerAdapter
import com.sd.src.stepcounterapp.fragments.*
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.network.RetrofitClient.IMG_URL
import com.sd.src.stepcounterapp.service.DfuService
import com.sd.src.stepcounterapp.service.MokoService
import com.sd.src.stepcounterapp.utils.InterConsts
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.SYNCDATE
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_WEARABLE
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.WEARABLEID
import com.sd.src.stepcounterapp.utils.Utils
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.DeviceViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.bottom_toolbar.*
import kotlinx.android.synthetic.main.titlebar.*
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter
import no.nordicsemi.android.dfu.DfuServiceListenerHelper
import java.util.*
import kotlin.collections.ArrayList


/**This class handles
 * device connected or not check
 *  syncing on resuming activity
 *  steps count on step chnge listener
 *
 */


class LandingActivity : BaseActivity<DeviceViewModel>(),
    MokoScanDeviceCallback, View.OnClickListener,HayatechFragment.SwipeVisibiltyListener {
    override fun showSwipe() {
        swipeLayout.isEnabled = true
    }

    fun hideBottomLayout(state: Boolean) {
        if (state) {
            bottom_bar.visibility = GONE
        } else {
            bottom_bar.visibility = VISIBLE
        }
    }


    fun onFragment(position: Int) {
        when (position) {
            2 -> loadFragment(position)
            0 -> //to open marketplace
                loadFragment(position)
            1 -> //to open marketplace
                loadFragment(position)
            5 -> startActivity(Intent(this@LandingActivity, LeaderboardActivity::class.java))
            6 -> openChangeFragment()
            4 -> loadFragment(position)
            10 -> openBlankFragment()
            11 -> openTnCFragment()
        }
    }

    private fun openBlankFragment() {
        swipeLayout.isEnabled = false
        hideBottomLayout(true)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, BlankFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun openTnCFragment() {
        swipeLayout.isEnabled = false
        hideBottomLayout(true)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, TnCFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private lateinit var mDevice: BleDevice
    private var current: Int = 0
    private var deviceSynced: String? = ""
    private val FRAG_HAYATECH: Int = 0
    private val FRAG_CHALLENGES: Int = 1
    private val FRAG_MARKET_PLACE: Int = 2
    private val FRAG_SURVEY: Int = 3
    private val FRAG_WALLET: Int = 4
    lateinit var manager: LocationManager
    private var mDialog: ProgressDialog? = null
    private var mService: MokoService? = null
    private var deviceMap: HashMap<String, BleDevice>? = null
    private var mDatas: ArrayList<BleDevice>? = null
    private var mDeviceConnectCount: Int = 0
    private var mDFUDialog: ProgressDialog? = null
    val REQUEST_CODE = 1001
    private var mSelected = -1
    internal var gps_enabled = false
    internal var network_enabled = false
    override val layoutId: Int
        get() = R.layout.activity_landing
    var lastestSteps: ArrayList<DailyStep>? = null

    override val viewModel: DeviceViewModel
        get() = ViewModelProviders.of(this,
            BaseViewModelFactory { DeviceViewModel(application) }).get(DeviceViewModel::class.java)

    override val context: Context
        get() = this@LandingActivity

    lateinit var mAdapter: LandingPagerAdapter

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mActionBarDrawerToggle: ActionBarDrawerToggle
    private val users = arrayOf(
        "My Profile",
        "Transaction History",
        "FAQs",
        "Contact Us",
        "Settings",
        "Notifications",
        "Reward categories"
    )

    override fun onCreate() {
        if (intent.hasExtra("device")) {
            mDevice = intent.getSerializableExtra("device") as BleDevice
        }

        bindService(Intent(this, MokoService::class.java), mServiceConnection, Activity.BIND_AUTO_CREATE)

        mDrawerLayout = findViewById(R.id.drawer_layout)
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mDialog = ProgressDialog(this)
        var aAdapter = ArrayAdapter(this, R.layout.menu_textview, users)
        list_menu_item.adapter = aAdapter
        list_menu_item.onItemClickListener = AdapterView.OnItemClickListener { a, v, position, id ->
            when (position) {
                0 -> openEditActivity()
                1 -> openTransactionFragment()
                2 -> openFAQFragment()
                3 -> openContactUsFragment()
                4 -> openFragment()
                5 -> openNotificationFragment()
                6 -> openRewardActivity()
            }

        }
        mAdapter = LandingPagerAdapter(supportFragmentManager)
        mAdapter.addFragment(HayatechFragment.newInstance(this@LandingActivity))
        mAdapter.addFragment(ChallengesFragment.newInstance(this@LandingActivity))
        mAdapter.addFragment(MarketPlaceFragment.newInstance(this@LandingActivity))
        mAdapter.addFragment(SurveysFragment.newInstance(this@LandingActivity))
        mAdapter.addFragment(WalletFragment.newInstance(this@LandingActivity))

        vpLanding.setPagingEnabled(false)
        vpLanding.adapter = mAdapter
        vpLanding.offscreenPageLimit = 0
        vpLanding.currentItem = 0
        setNavHeader()
        initListeners()

        mViewModel?.getDashResponse()?.observe(this, androidx.lifecycle.Observer { mDashResponse ->
            if (mDashResponse.data.lastUpdated != null) {
                SharedPreferencesManager.setString(
                    HayatechFragment.mContext, mDashResponse.data.lastUpdated,
                    SYNCDATE
                )
            }
        })

        if (intent.hasExtra("surveyBack")) {
            vpLanding.currentItem = 3
        }
        mViewModel?.getSyncResponse()?.observe(this,
            androidx.lifecycle.Observer<BasicInfoResponse> {
//                dismissSwipe()
            })



        vpLanding?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                current = position
                swipeLayout.isEnabled = position == 0
                loadFragment(position)
                /* if (current == 0) {
                     if (MokoSupport.getInstance().isConnDevice(
                             this@LandingActivity,
                             SharedPreferencesManager.getString(this@LandingActivity, WEARABLEID)
                         )
                     ) {
                         showDisconnection(false)
                     } else {
                         showDisconnection(true)
                     }
                 }*/
            }

        })

        mViewModel?.fetchSyncData(
            FetchDeviceDataRequest(
                InterConsts.WEEKLY, SharedPreferencesManager.getUserId(
                    HayatechFragment.mContext
                )
            )
        )


    }

    private fun openNotificationFragment() {
        hideBottomLayout(true)
        mDrawerLayout.closeDrawer(GravityCompat.START)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, NotificationFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun openEditActivity() {
        startActivity(Intent(this@LandingActivity, MyProfileActivity::class.java))
    }

    private fun openRewardActivity() {
        startActivity(
            Intent(
                this@LandingActivity,
                RewardsCategorySelectionActivity::class.java
            ).putExtra(INTENT_FROMAPP, true)
        )
    }


    override fun initListeners() {
        profileImgTitle.setOnClickListener {
            startActivity(Intent(this@LandingActivity, MyProfileActivity::class.java))
        }
        setNavHeader()
        loadFragment(0)
        llHayatech.setOnClickListener(this@LandingActivity)
        llChallenges.setOnClickListener(this@LandingActivity)
        llMarketPalace.setOnClickListener(this@LandingActivity)
        llSurvey.setOnClickListener(this@LandingActivity)
        llWallet.setOnClickListener(this@LandingActivity)
        img_nav.setOnClickListener(this@LandingActivity)
        swipeLayout.setOnRefreshListener {
            //            swipeLayout.isRefreshing = false
            if (!SharedPreferencesManager.hasKey(this@LandingActivity, WEARABLEID) || !SharedPreferencesManager.hasKey(
                    this@LandingActivity,
                    VAR_WEARABLE
                )
            ) {
                swipeLayout.isRefreshing = false
                showReconnection()
            } else {
                swipeLayout.isRefreshing = false
//                showDisconnection(true)
                checkBluetoothGPSPermissions()
            }
        }

    }

    private fun showReconnection() {
        val builder = AlertDialog.Builder(this@LandingActivity)
        builder.setMessage("You are not connected with wearable device, press OK for connection")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, id ->
                swipeLayout.isRefreshing = false
                startActivity(
                    Intent(this@LandingActivity, SyncDeviceActivity::class.java).putExtra(
                        INTENT_RECONNECT,
                        true
                    )
                )
            }
            .setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    /**
     * to check if bluetooth & GPS enabled or not
     */
    private fun checkBluetoothGPSPermissions() {
        if (!SharedPreferencesManager.hasKey(
                this@LandingActivity,
                WEARABLEID
            ) || !SharedPreferencesManager.hasKey(this@LandingActivity, VAR_WEARABLE)
        ) {
            showDisconnection(true)
        } else {
//            notconnectedTxt.text = "Reconnecting..."
            if (BluetoothAdapter.getDefaultAdapter().isEnabled && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                searchDevices()
            } else {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showGPSDisabledAlertToUser()
                } else {
                    searchDevices()
                }
            }
        }
    }


    fun setNavHeader() {
//         UpdatedUser

        if (SharedPreferencesManager.hasKey(this@LandingActivity, "UpdatedUser")) {
            var user = SharedPreferencesManager.getUpdatedUserObject(this@LandingActivity)
            deviceSynced = SharedPreferencesManager.getString(this@LandingActivity, WEARABLEID)
            userNameNav.text = "${user.firstName} ${user.lastName}"
            Picasso.get().load(IMG_URL + user.image).resize(100, 100).placeholder(R.drawable.nouser)
                .into(img_nav_header)
            Picasso.get().load(IMG_URL + user.image).resize(80, 80).placeholder(R.drawable.nouser).into(profileImgTitle)

        } else {
            var user = SharedPreferencesManager.getUserObject(this@LandingActivity)
            deviceSynced = SharedPreferencesManager.getString(this@LandingActivity, WEARABLEID)
            userNameNav.text = "${user.data.firstName} ${user.data.lastName}!"
            Picasso.get().load(IMG_URL + user.data.image).resize(100, 100).placeholder(R.drawable.nouser)
                .into(img_nav_header)
            Picasso.get().load(IMG_URL + user.data.image).resize(80, 80).placeholder(R.drawable.nouser)
                .into(profileImgTitle)

        }
        userProfileImg.setOnClickListener {
            mDrawerLayout.closeDrawer(GravityCompat.START)
            openEditActivity()
        }
        profileImgTitle.setOnClickListener {
            mDrawerLayout.closeDrawer(GravityCompat.START)
            openEditActivity()
        }
        signout.setOnClickListener {
            logoutUser()
        }
    }


    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            hideBottomLayout(false)
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loadFragment(selected: Int) {
        hideBottomLayout(false)
        hideKeyboard()
        imgHayatech.setImageResource(R.drawable.dashboard)
        imgChallenges.setImageResource(R.drawable.challenge)
        imgMarketPalace.setImageResource(R.drawable.marketplace_un)
        imgSurvey.setImageResource(R.drawable.survey)
        imgWallet.setImageResource(R.drawable.wallet_un)

        when (selected) {
            FRAG_HAYATECH -> {
                imgHayatech.setImageResource(R.drawable.dashboard_s)
                txt_title.setImageResource(R.drawable.headerlogo)
            }
            FRAG_CHALLENGES -> {
                imgChallenges.setImageResource(R.drawable.challenge_s)
                txt_title.setImageResource(R.drawable.challenges_header)
            }
            FRAG_MARKET_PLACE -> {
                imgMarketPalace.setImageResource(R.drawable.marketplace_s)
                txt_title.setImageResource(R.drawable.marketplace_header)
            }
            FRAG_SURVEY -> {
                imgSurvey.setImageResource(R.drawable.survey_s)
                txt_title.setImageResource(R.drawable.surveys_header)
            }
            FRAG_WALLET -> {

                imgWallet.setImageResource(R.drawable.wallet_s)
                txt_title.setImageResource(R.drawable.wallet_header)
            }
        }
        mSelected = selected
        vpLanding.currentItem = mSelected
    }

    override fun onClick(view: View) {
        when (view.id) {
            // bottom bar
            R.id.llHayatech -> loadFragment(FRAG_HAYATECH)
            R.id.llChallenges -> loadFragment(FRAG_CHALLENGES)
            R.id.llMarketPalace -> loadFragment(FRAG_MARKET_PLACE)
            R.id.llSurvey -> loadFragment(FRAG_SURVEY)
            R.id.llWallet -> loadFragment(FRAG_WALLET)
            R.id.img_nav -> mDrawerLayout.openDrawer(Gravity.LEFT)

        }
    }


    /**
     * logout
     */

    fun logoutUser() {
        // Clearing all data from Shared Preferences
        val builder = AlertDialog.Builder(this@LandingActivity)
        builder.setMessage("Are you sure you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                //                disconnectBLE()
                SharedPreferencesManager.logout(this@LandingActivity)
                MokoSupport.getInstance().disConnectBle()
                unregisterReceiver(mReceiver)
                unbindService(mServiceConnection)
                stopService(Intent(this, MokoService::class.java))
            }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()

    }

    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = (service as MokoService.LocalBinder).service
            val filter = IntentFilter()
            filter.addAction(MokoConstants.ACTION_CONN_STATUS_DISCONNECTED)
            filter.addAction(MokoConstants.ACTION_DISCOVER_SUCCESS)
            filter.addAction(MokoConstants.ACTION_CONN_STATUS_DISCONNECTED)
            filter.addAction(MokoConstants.ACTION_DISCOVER_TIMEOUT)
            filter.addAction(MokoConstants.ACTION_ORDER_RESULT)
            filter.addAction(MokoConstants.ACTION_ORDER_TIMEOUT)
            filter.addAction(MokoConstants.ACTION_ORDER_FINISH)
            filter.addAction(MokoConstants.ACTION_CURRENT_DATA)
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            filter.priority = 200
            dismissSwipe()
            registerReceiver(mReceiver, filter)

        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e("Got", "disconnected" + name.flattenToShortString())
        }
    }


    val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            mDialog!!.dismiss()
            if (intent != null) {
                val action = intent.action
                if (MokoConstants.ACTION_DISCOVER_SUCCESS == intent.action) {
                    showDisconnection(false)
                    HayatechFragment.instance.syncingWearableMsg(false)
                    abortBroadcast()
                    dismissSwipe()
                    if (!this@LandingActivity.isFinishing && mDialog!!.isShowing) {
                        mDialog!!.dismiss()

                    }
                    getLastestSteps()
                    openStepChangeListener()
                }
                if (MokoConstants.ACTION_CONN_STATUS_DISCONNECTED == intent.action) {
                    abortBroadcast()
                    showDisconnection(true)
                    dismissSwipe()
                    HayatechFragment.instance.syncingWearableMsg(false)
                    if (!this@LandingActivity.isFinishing() && mDialog!!.isShowing) {
                        mDialog!!.dismiss()
                    }

                }
                if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                    val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                    checkBluetoothGPSPermissions()
                    when (blueState) {
                        BluetoothAdapter.STATE_TURNING_OFF, BluetoothAdapter.STATE_OFF -> turnOnBluetooth()
                    }
                    dismissSwipe()
                }
                if (MokoConstants.ACTION_CONN_STATUS_DISCONNECTED == action) {
                    abortBroadcast()
                    showDisconnection(true)
                    dismissSwipe()
                    HayatechFragment.instance.syncingWearableMsg(false)
                    if (!this@LandingActivity.isFinishing() && mDialog!!.isShowing) {
                        mDialog!!.dismiss()
                    }
//                    this@LandingActivity.finish()
                }
                if (MokoConstants.ACTION_ORDER_RESULT == action) {
                    val response =
                        intent.getSerializableExtra(MokoConstants.EXTRA_KEY_RESPONSE_ORDER_TASK) as OrderTaskResponse
                    val orderEnum = response.order
                    lastestSteps = MokoSupport.getInstance().dailySteps
                    dismissSwipe()
                    if (lastestSteps == null || lastestSteps!!.isEmpty()) {
                        return
                    }
                    showDisconnection(false)
//                    HayatechFragment.instance.syncingWearableMsg(false)
                    SharedPreferencesManager.saveSyncObject(this@LandingActivity, lastestSteps)
                    HayatechFragment.instance.setCurrentSteps(lastestSteps!![lastestSteps!!.lastIndex])
                    updateDeviceData()
                }
                if (MokoConstants.ACTION_ORDER_TIMEOUT == action) {
//                    showDisconnection(true)
//                    notconnectedTxt.text = "Request Timed out.Trying to reconnect..."
//                    Toast.makeText(this@LandingActivity, "Timeout", Toast.LENGTH_SHORT).show()
                    dismissSwipe()
                    checkBluetoothGPSPermissions()
                }
                if (MokoConstants.ACTION_ORDER_FINISH == action) {
//                    Toast.makeText(this@LandingActivity, "Success", Toast.LENGTH_SHORT).show()
                    dismissSwipe()

                }
                if (MokoConstants.ACTION_CURRENT_DATA == action) {
                    dismissSwipe()
                    val orderEnum: OrderEnum =
                        intent.getSerializableExtra(MokoConstants.EXTRA_KEY_CURRENT_DATA_TYPE) as OrderEnum
                    when (orderEnum) {
                        OrderEnum.Z_STEPS_CHANGES_LISTENER -> {
                            val dailyStep = MokoSupport.getInstance().dailyStep
                            HayatechFragment.instance.syncingWearableMsg(false)
                            HayatechFragment.instance.setCurrentSteps(dailyStep)
                            mViewModel!!.syncDevice(
                                SyncRequest(
                                    getLatestActivityData(dailyStep),
                                    SharedPreferencesManager.getUserId(HayatechFragment.mContext),
                                    SharedPreferencesManager.getString(this@LandingActivity, WEARABLEID),
                                    SharedPreferencesManager.getString(
                                        HayatechFragment.mContext,
                                        SharedPreferencesManager.FIREBASETOKEN
                                    )
                                )
                            )
                        }
                    }
                }
            }

        }
    }

    private fun turnOnBluetooth() {
        val builder = AlertDialog.Builder(this@LandingActivity)
        builder.setMessage("Bluetooth permissions are required for this application to function properly.")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, id ->
                var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                mBluetoothAdapter.enable()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                Toast.makeText(
                    this@LandingActivity,
                    "Please enable bluetooth for proper functioning of this application.",
                    Toast.LENGTH_LONG
                ).show()
                dialog.cancel()
            }

        val alert = builder.create()
        alert.show()


    }

    /**
     * To dismiss swipe refresh
     */


    private fun dismissSwipe() {
        if (swipeLayout.isRefreshing) {
            swipeLayout.isRefreshing = false
        }

    }

    private fun updateDeviceData() {
        if (SharedPreferencesManager.hasKey(HayatechFragment.mContext, VAR_WEARABLE)) {
            mViewModel!!.syncDevice(
                SyncRequest(
                    getActivityData(),
                    SharedPreferencesManager.getUserId(HayatechFragment.mContext),
                    SharedPreferencesManager.getString(this@LandingActivity, WEARABLEID),SharedPreferencesManager.getString(
                        HayatechFragment.mContext,
                        SharedPreferencesManager.FIREBASETOKEN
                    )
                )
            )
        }
    }


    private fun getActivityData(): ArrayList<com.sd.src.stepcounterapp.model.syncDevice.Activity>? {
        var lastSyncDate: String = ""

        var list: ArrayList<DailyStep>? = SharedPreferencesManager.getSyncObject(HayatechFragment.mContext)
        var newList: ArrayList<DailyStep>? = ArrayList()
        var activityList: ArrayList<com.sd.src.stepcounterapp.model.syncDevice.Activity>? = ArrayList()
        if (SharedPreferencesManager.getString(this@LandingActivity, SYNCDATE) != null) {
            lastSyncDate = SharedPreferencesManager.getString(this@LandingActivity, SYNCDATE)!!.split("T")[0]
        }
        list?.forEachIndexed { index, dailyStep ->
            if (list[index].date == lastSyncDate) {
                newList?.addAll(list.subList(index, list.lastIndex))
            }
        }
        newList!!.iterator().forEach {
            activityList!!.add(
                com.sd.src.stepcounterapp.model.syncDevice.Activity(
                    it.date,
                    it.distance.toDouble(),
                    it.duration.toInt(),
                    it.count.toInt(),
                    it.calories.toInt()
                )
            )
        }
        Log.i("Size", "list" + activityList!!.size)
        return activityList
    }


    /**
     * to get latest data
     */

    private fun getLastestSteps() {
        var calendar: Calendar? = null
        calendar = Utils.strDate2Calendar(
            "2018-06-01 00:00",
            PATTERN_YYYY_MM_DD_HH_MM
        )
        MokoSupport.getInstance().sendOrder(ZReadStepTask(mService, calendar!!))
    }

    /**
     * to search nearby devices
     */

    fun searchDevices() {
        mDialog = ProgressDialog(this)
        mDatas = ArrayList()
        deviceMap = HashMap()
        MokoSupport.getInstance().startScanDevice(this)
    }


    override fun onStartScan() {
        deviceMap!!.clear()
    }

    override fun onScanDevice(device: BleDevice) {

        deviceMap!![device.address] = device
        mDatas!!.clear()
        mDatas!!.addAll(deviceMap!!.values)
        mAdapter!!.notifyDataSetChanged()

    }

    override fun onStopScan() {
        if (!this@LandingActivity.isFinishing && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }

        dismissSwipe()
        mDatas!!.clear()
        mDatas!!.addAll(deviceMap!!.values)

        mDatas!!.iterator().forEach {
            if (it.address == deviceSynced) {
                if (MokoSupport.getInstance().isConnDevice(this@LandingActivity, it.address)) {
                    showDisconnection(false)
                    HayatechFragment.instance.syncingWearableMsg(true)
                    bindService(Intent(this, MokoService::class.java), mServiceConnection, Activity.BIND_AUTO_CREATE)
                    getLastestSteps()
                    openStepChangeListener()

                } else {
                    if (SharedPreferencesManager.hasKey(this@LandingActivity, VAR_WEARABLE)) {
                        mService!!.connectBluetoothDevice(it.address)
                        bindService(
                            Intent(this, MokoService::class.java),
                            mServiceConnection,
                            Activity.BIND_AUTO_CREATE
                        )
                        getLastestSteps()
                        openStepChangeListener()
                    }
                }
            } else {
                dismissSwipe()
//                Toast.makeText(this@LandingActivity,"Please retry",Toast.LENGTH_SHORT).show()
                Log.e("Device", "not in list." + it.address)
            }

        }

        if(MokoSupport.getInstance().isConnDevice(this@LandingActivity,SharedPreferencesManager.getString(this@LandingActivity,
                WEARABLEID))){
            getLastestSteps()
            openStepChangeListener()
            Log.e("getLastestSteps","openStepChangeListener"+MokoSupport.getInstance().userInfo)
        }

        Log.e("array","device" + mDatas!!.size)
    }

    override fun onDestroy() {
        super.onDestroy()
      /*  unregisterReceiver(mReceiver)
        unbindService(mServiceConnection)
        stopService(Intent(this, MokoService::class.java))*/
    }


    override fun onResume() {
        super.onResume()
        swipeLayout.isEnabled = true
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener)
        checkDeviceConnection()
        setNavHeader()
        checkBluetoothGPSPermissions()
        onNotification()
    }

     fun checkDeviceConnection() {
        if (SharedPreferencesManager.hasKey(this@LandingActivity, VAR_WEARABLE) ) {
            showDisconnection(false)
        }else if(SharedPreferencesManager.hasKey(this@LandingActivity, WEARABLEID) && !MokoSupport.getInstance().isConnDevice(this@LandingActivity,SharedPreferencesManager.getString(this@LandingActivity,
                WEARABLEID))){
            showDisconnection(true)
            swipeLayout.isEnabled = true
        } else {
            showDisconnection(true)
            swipeLayout.isEnabled = true
        }
    }

    private fun onNotification() {
        if (intent.hasExtra(INTENT_NOTIFICATION)) {
            when (intent.getStringExtra(INTENT_NOTIFICATION)) {
                "Challenge Complete" -> loadFragment(1)     //challenges
                "Redeem Wishlist" ->
                    loadFragment(4) //wallet
                "Product Deleted" ->
                    loadFragment(2)     //marketplace
                "Daily Top Score" -> startActivity(Intent(this@LandingActivity, LeaderboardActivity::class.java))
                "Weekly Top Score" -> startActivity(Intent(this@LandingActivity, LeaderboardActivity::class.java))
                "Challenge Invitiation" -> loadFragment(1)  //challenges
                "New Product" -> loadFragment(2)  //marketplace
            }
        }
    }


    private fun getLatestActivityData(dailyStep: DailyStep): ArrayList<com.sd.src.stepcounterapp.model.syncDevice.Activity>? {
        var activityList: ArrayList<com.sd.src.stepcounterapp.model.syncDevice.Activity>? = ArrayList()
        activityList!!.add(
            com.sd.src.stepcounterapp.model.syncDevice.Activity(
                dailyStep.date,
                dailyStep.distance.toDouble(),
                dailyStep.duration.toInt(),
                dailyStep.count.toInt(),
                dailyStep.calories.toInt()
            )
        )
        return activityList
    }

    private val mDfuProgressListener = object : DfuProgressListenerAdapter() {
        override fun onDeviceConnecting(deviceAddress: String?) {
            mDeviceConnectCount++
            if (mDeviceConnectCount > 1) {
                Toast.makeText(this@LandingActivity, "Error:DFU Failed", Toast.LENGTH_SHORT).show()
                dismissDFUProgressDialog()
                val manager = LocalBroadcastManager.getInstance(this@LandingActivity)
                val abortAction = Intent(DfuService.BROADCAST_ACTION)
                abortAction.putExtra(DfuService.EXTRA_ACTION, DfuService.ACTION_ABORT)
                manager.sendBroadcast(abortAction)
            }
        }

        override fun onDeviceDisconnecting(deviceAddress: String?) {
            Log.i("", "onDeviceDisconnecting...")
        }

        override fun onDfuProcessStarting(deviceAddress: String?) {
            mDFUDialog!!.setMessage("DfuProcessStarting...")
        }


        override fun onEnablingDfuMode(deviceAddress: String?) {
            mDFUDialog!!.setMessage("EnablingDfuMode...")
        }

        override fun onFirmwareValidating(deviceAddress: String?) {
            mDFUDialog!!.setMessage("FirmwareValidating...")
        }

        override fun onDfuCompleted(deviceAddress: String?) {
            Toast.makeText(this@LandingActivity, "DfuCompleted!", Toast.LENGTH_SHORT).show()
            dismissDFUProgressDialog()
            this@LandingActivity.finish()
        }

        override fun onDfuAborted(deviceAddress: String?) {
            mDFUDialog!!.setMessage("DfuAborted...")
        }

        override fun onProgressChanged(
            deviceAddress: String?,
            percent: Int,
            speed: Float,
            avgSpeed: Float,
            currentPart: Int,
            partsTotal: Int
        ) {
            mDFUDialog!!.setMessage("Progress:$percent%")
        }

        override fun onError(deviceAddress: String?, error: Int, errorType: Int, message: String?) {
            Toast.makeText(this@LandingActivity, "Error:" + message!!, Toast.LENGTH_SHORT).show()
            LogModule.i("Error:$message")
            dismissDFUProgressDialog()
        }
    }


    private fun dismissDFUProgressDialog() {
        mDeviceConnectCount = 0
        if (!isFinishing && mDFUDialog != null && mDFUDialog!!.isShowing) {
            mDFUDialog!!.dismiss()
        }
    }


    /*override fun onPause() {
        super.onPause()
        MokoSupport.getInstance().disConnectBle()
    }*/

    fun openStepChangeListener() {
        MokoSupport.getInstance().sendOrder(ZOpenStepListenerTask(mService))
    }


    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Goto Settings Page To Enable GPS"
            ) { dialog, id ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, REQUEST_CODE)
            }
        alertDialogBuilder.setNegativeButton(
            "Cancel"
        ) { dialog, id ->
            dialog.cancel()
        }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == 0) {
            val provider = Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            var lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (provider != null) {
                //Start searching for location and update the location text when update available.
                // Do whatever you want
                checkBluetoothGPSPermissions()
            } else {
                Toast.makeText(
                    this@LandingActivity,
                    "These permissions are required for app to work function properly.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun openTransactionFragment() {
        swipeLayout.isEnabled = false
        hideBottomLayout(true)
        mDrawerLayout.closeDrawer(GravityCompat.START)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, TransactionHistoryFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }

    private fun openFAQFragment() {
        swipeLayout.isEnabled = false
        hideBottomLayout(true)
        mDrawerLayout.closeDrawer(GravityCompat.START)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, OpenFAQFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }

    private fun openFragment() {
        swipeLayout.isEnabled = false

        hideBottomLayout(true)
        mDrawerLayout.closeDrawer(GravityCompat.START)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, SettingsFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun openContactUsFragment() {
        swipeLayout.isEnabled = false
        hideBottomLayout(true)
        mDrawerLayout.closeDrawer(GravityCompat.START)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, OpenContactUsFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }

    private fun openChangeFragment() {
        swipeLayout.isEnabled = false
        hideBottomLayout(true)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, ChangePasswordFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    /**
     * Set system time from device
     */


    fun disableSwipe(b: Boolean) {
        if (swipeLayout != null) {
            swipeLayout.isEnabled = !b
        }
    }

    fun showDisconnection(b: Boolean): FrameLayout.LayoutParams {
        var params = swipeLayout.layoutParams as FrameLayout.LayoutParams
        if (b) {
            notconnectedTxt.visibility = VISIBLE
            swipeLayout.isEnabled = true
            params.setMargins(10, 230, 10, 10)
        } else {
            notconnectedTxt.visibility = GONE
            params.setMargins(0, 140, 0, 0)
        }
        swipeLayout.layoutParams = params
        return params
    }

}

