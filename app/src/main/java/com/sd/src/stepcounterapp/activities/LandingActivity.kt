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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
import com.sd.src.stepcounterapp.AppConstants
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.LandingPagerAdapter
import com.sd.src.stepcounterapp.fragments.*
import com.sd.src.stepcounterapp.model.DeviceResponse.DashboardResponse
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.network.RetrofitClient.IMG_URL
import com.sd.src.stepcounterapp.service.DfuService
import com.sd.src.stepcounterapp.service.MokoService
import com.sd.src.stepcounterapp.utils.InterConsts
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
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


/**This class handles
 * device connected or not check
 *  syncing on resuming activity
 *  steps count on step chnge listener
 *
 */


class LandingActivity : BaseActivity<DeviceViewModel>(), MokoScanDeviceCallback, View.OnClickListener{
//
//    override fun onFragmentClick(position: Int) {
//        if (position == 2) {
//            vpLanding.currentItem = 4
//        } else if (position == 0) {
//            vpLanding.currentItem = 2
//        }
//    }

    fun onFragmnet(position: Int){
        if (position == 2) {
            vpLanding.currentItem = 4
        } else if (position == 0) {
            vpLanding.currentItem = 2
        }
    }


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
        "Contact us",
        "Settings",
        "Notifications",
        "Logout"
    )

    override fun onCreate() {
        mDrawerLayout = findViewById(R.id.drawer_layout)
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mDialog = ProgressDialog(this)
        var aAdapter = ArrayAdapter(this, R.layout.menu_textview, users)
        list_menu_item.adapter = aAdapter
        list_menu_item.onItemClickListener = AdapterView.OnItemClickListener { a, v, position, id ->
            when (position) {
                0 -> openEditActivity()
                1 -> openTransactionFragment()
                4 -> openFragment()
            }

            if (position == 6) {
                logoutUser()
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
//        vpLanding.offscreenPageLimit = 4

        if (intent.hasExtra("surveyBack")) {
            vpLanding.currentItem = 3
        }
//        loadFragment(FRAG_HAYATECH)
        bindService(Intent(this, MokoService::class.java), mServiceConnection, Activity.BIND_AUTO_CREATE)
        mViewModel?.getSyncResponse()?.observe(this,
            androidx.lifecycle.Observer<BasicInfoResponse> {
                showPopupProgressSpinner(false)
//                HayatechFragment.instance.callDashboard()
                loadFragment(FRAG_HAYATECH)     //loading fragment here to maintain the step count

            })

    }

    private fun openEditActivity() {
        startActivity(Intent(this@LandingActivity, MyProfileActivity::class.java))
    }

    private fun openFragment() {
        mDrawerLayout.closeDrawer(GravityCompat.START)
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, SettingsFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }

    override fun initListeners() {
        profileImgTitle.setOnClickListener {
            startActivity(Intent(this@LandingActivity, MyProfileActivity::class.java))
        }
        setNavHeader()
        checkBluetoothGPSPermissions()
        llHayatech.setOnClickListener(this@LandingActivity)
        llChallenges.setOnClickListener(this@LandingActivity)
        llMarketPalace.setOnClickListener(this@LandingActivity)
        llSurvey.setOnClickListener(this@LandingActivity)
        llWallet.setOnClickListener(this@LandingActivity)
        img_nav.setOnClickListener(this@LandingActivity)
        swipeLayout.setOnRefreshListener {
            checkBluetoothGPSPermissions()
            vpLanding.adapter = mAdapter
        }
    }

    /**
     * to check if bluetooth & GPS enabled or not
     */
    private fun checkBluetoothGPSPermissions() {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            searchDevices()
        } else {

            if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
                showGPSDisabledAlertToUser()
            }else{
                startActivity(Intent(this@LandingActivity, GuideActivity::class.java))
            }

        }

    }


    fun setNavHeader() {
//         UpdatedUser

        if (SharedPreferencesManager.hasKey(this@LandingActivity, "UpdatedUser")) {
            var user = SharedPreferencesManager.getUpdatedUserObject(this@LandingActivity)
            deviceSynced = SharedPreferencesManager.getString(this@LandingActivity, WEARABLEID)
            userNameNav.text = "Hello " + user.firstName + "!"
            Picasso.get().load(IMG_URL + user.image).resize(100, 100).placeholder(R.drawable.nouser)
                .into(img_nav_header)
            Picasso.get().load(IMG_URL + user.image).resize(80, 80).placeholder(R.drawable.nouser).into(profileImgTitle)

        } else {
            var user = SharedPreferencesManager.getUserObject(this@LandingActivity)
            deviceSynced = SharedPreferencesManager.getString(this@LandingActivity, WEARABLEID)
            userNameNav.text = "Hello " + user.data.firstName + "!"
            Picasso.get().load(IMG_URL + user.data.image).resize(100, 100).placeholder(R.drawable.nouser)
                .into(img_nav_header)
            Picasso.get().load(IMG_URL + user.data.image).resize(80, 80).placeholder(R.drawable.nouser)
                .into(profileImgTitle)

        }
        profileImgTitle.setOnClickListener {
            mDrawerLayout.closeDrawer(GravityCompat.START)
            openEditActivity()
        }
    }


    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loadFragment(selected: Int) {
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
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setMessage("Are you sure you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id -> SharedPreferencesManager.logout(this@LandingActivity) }
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
//            filter.priority = 100
            registerReceiver(mReceiver, filter)
//            MokoSupport.getInstance().sendOrder(ZReadVersionTask(mService))

        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }


    val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            mDialog!!.dismiss()
            if (intent != null) {
                val action = intent.action
                if (MokoConstants.ACTION_DISCOVER_SUCCESS == intent.action) {
                    abortBroadcast()
                    if (!this@LandingActivity.isFinishing() && mDialog!!.isShowing) {
                        mDialog!!.dismiss()
                    }
//                    Toast.makeText(this@LandingActivity, "Connect success", Toast.LENGTH_SHORT).show()
                    notconnectedTxt.visibility = View.GONE
                    swipeLayout.isEnabled = false

                    getLastestSteps()
                    openStepChangeListener()

                }
                if (MokoConstants.ACTION_CONN_STATUS_DISCONNECTED == intent.action) {
                    abortBroadcast()
                    if (MokoSupport.getInstance().isBluetoothOpen && MokoSupport.getInstance().reconnectCount > 0) {
//                        Toast.makeText(this@LandingActivity, "Connect failed", Toast.LENGTH_SHORT).show()
                        notconnectedTxt.visibility = View.VISIBLE
                        return
                    }
                    if (!this@LandingActivity.isFinishing() && mDialog!!.isShowing) {
                        mDialog!!.dismiss()
                    }
                    notconnectedTxt.visibility = View.VISIBLE
                    swipeLayout.isEnabled = false

                }
                if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                    val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                    when (blueState) {
                        BluetoothAdapter.STATE_TURNING_OFF, BluetoothAdapter.STATE_OFF -> this@LandingActivity.finish()
                    }
                }
                if (MokoConstants.ACTION_CONN_STATUS_DISCONNECTED == action) {
                    abortBroadcast()
//                    Toast.makeText(this@LandingActivity, "Connect failed", Toast.LENGTH_SHORT).show()
                    notconnectedTxt.visibility = View.VISIBLE
                    swipeLayout.isEnabled = false
                    this@LandingActivity.finish()
                }
                if (MokoConstants.ACTION_ORDER_RESULT == action) {
                    val response =
                        intent.getSerializableExtra(MokoConstants.EXTRA_KEY_RESPONSE_ORDER_TASK) as OrderTaskResponse
                    val orderEnum = response.order
                    lastestSteps = MokoSupport.getInstance().dailySteps
                    if (lastestSteps == null || lastestSteps!!.isEmpty()) {
                        return
                    }
                     /*for (step in lastestSteps!!) {
                         Toast.makeText(
                             this@LandingActivity,
                             "Steps count " + lastestSteps!![lastestSteps!!.size - 1].count,
                             Toast.LENGTH_LONG
                         ).show()
                     }*/
                    SharedPreferencesManager.saveSyncObject(this@LandingActivity, lastestSteps)
                    updateDeviceData()
                }
                if (MokoConstants.ACTION_ORDER_TIMEOUT == action) {
                    Toast.makeText(this@LandingActivity, "Timeout", Toast.LENGTH_SHORT).show()
                }
                if (MokoConstants.ACTION_ORDER_FINISH == action) {
//                    Toast.makeText(this@LandingActivity, "Success", Toast.LENGTH_SHORT).show()

                }
                if (MokoConstants.ACTION_CURRENT_DATA == action) {
                    val orderEnum: OrderEnum =
                        intent.getSerializableExtra(MokoConstants.EXTRA_KEY_CURRENT_DATA_TYPE) as OrderEnum
                    when (orderEnum) {
                        OrderEnum.Z_STEPS_CHANGES_LISTENER -> {
                            val dailyStep = MokoSupport.getInstance().dailyStep
                            LogModule.i(dailyStep.toString())
                            HayatechFragment.instance.setCurrentSteps(dailyStep)
                        }
                    }
                }
            }

        }
    }

    private fun updateDeviceData() {
        if (SharedPreferencesManager.hasKey(HayatechFragment.mContext, "Wearable")) {
            var android_id = Settings.Secure.getString(
                HayatechFragment.mContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            showPopupProgressSpinner(true)
            mViewModel!!.syncDevice(
                SyncRequest(
                    getActivityData(),
                    SharedPreferencesManager.getUserId(HayatechFragment.mContext),
                    android_id
                )
            )
        }
    }


    private fun getActivityData(): ArrayList<com.sd.src.stepcounterapp.model.syncDevice.Activity>? {
        var list: ArrayList<DailyStep>? = SharedPreferencesManager.getSyncObject(HayatechFragment.mContext)
        var activityList: ArrayList<com.sd.src.stepcounterapp.model.syncDevice.Activity>? = ArrayList()
        list!!.iterator().forEach {
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
            AppConstants.PATTERN_YYYY_MM_DD_HH_MM
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
//        if (deviceMap!!.values.isEmpty()) {
//            noAvlbl.visibility = View.VISIBLE
//            lvDevice!!.visibility = View.GONE
//
//        } else {
//            noAvlbl.visibility = View.GONE
//            lvDevice!!.visibility = View.VISIBLE
        deviceMap!![device.address] = device
        mDatas!!.clear()
        mDatas!!.addAll(deviceMap!!.values)


    }

    override fun onStopScan() {
        if (!this@LandingActivity.isFinishing && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
        mDatas!!.clear()
        mDatas!!.addAll(deviceMap!!.values)

        /*if(mDatas!!.size==0){
            notconnectedTxt.visibility = View.VISIBLE
        }*/

        mDatas!!.iterator().forEach {
            if (it.address == deviceSynced) {
                if (MokoSupport.getInstance().isConnDevice(this@LandingActivity, it.address)) {
                    getLastestSteps()
                } else {
                    mService!!.connectBluetoothDevice(it.address)
                }
            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
        unbindService(mServiceConnection)
        stopService(Intent(this, MokoService::class.java))
    }


    override fun onPause() {
        super.onPause()
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener)

    }


    override fun onResume() {
        super.onResume()
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener)
        checkBluetoothGPSPermissions()
        setNavHeader()
    }


    private val mDfuProgressListener = object : DfuProgressListenerAdapter() {
        override fun onDeviceConnecting(deviceAddress: String?) {
            mDeviceConnectCount++
            if (mDeviceConnectCount > 3) {
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
        mDrawerLayout.closeDrawer(GravityCompat.START)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, TransactionHistoryFragment.newInstance(this@LandingActivity))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }



}
