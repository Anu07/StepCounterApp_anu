package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.GridLayout.VERTICAL
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitpolo.support.MokoSupport
import com.fitpolo.support.callback.MokoConnStateCallback
import com.sd.src.stepcounterapp.*
import com.sd.src.stepcounterapp.adapter.RecyclerSyncGridAdapter
import com.sd.src.stepcounterapp.model.refreshToken.RefreshTokenResponse
import com.sd.src.stepcounterapp.model.syncDevice.SyncDeviceSelectionArray
import com.sd.src.stepcounterapp.model.syncDevice.elcies.WidgetBody
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_ELCIES_CONNCTED
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_ELCTOKEN
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.VAR_WEARABLE_CONNECTED
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.WEARABLEID
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.SyncViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.contains
import kotlinx.android.synthetic.main.activity_select_syncsdk.*
import kotlinx.android.synthetic.main.recycler_sync_view_item.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.logging.Handler


class SyncDeviceActivity : BaseActivity<SyncViewModel>(),
    RecyclerSyncGridAdapter.ItemListener {


    override fun onItemClick(pos: Int, item: SyncDeviceSelectionArray?) {
        if (selectedPosition == -1 || (deviceSynced.isEmpty() && !SharedPreferencesManager.hasKey(
                this@SyncDeviceActivity,
                VAR_ELCIES_CONNCTED
            ))
        ) {
            selectedPosition = pos
            adapter.swapData(updateItem(pos))
        } else if (selectedPosition != pos) {
            changeWearable()
        }
    }


    private fun updateItem(position: Int): Array<SyncDeviceSelectionArray?> {
        dataArray.forEachIndexed { index, _ ->
            if (position == index) {
                when (position) {
                    0 -> {
                        dataArray[0]?.selected = true
//                        dataArray[1]?.selected = false
                    }
                    1 -> {
                        dataArray[1]?.selected = true
                        dataArray[0]?.selected = false
                    }
                }
            } else if (position == -1) {
//                dataArray[1]?.selected = false
                dataArray[0]?.selected = false
            }
        }
        return dataArray
    }

    private var elciesEnabled: Boolean = false
    private lateinit var adapter: RecyclerSyncGridAdapter
    override val layoutId: Int
        get() = R.layout.activity_select_syncsdk
    override val viewModel: SyncViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { SyncViewModel(application) }).get(SyncViewModel::class.java)
    override val context: Context
        get() = this@SyncDeviceActivity
    val numbers: IntArray =
        intArrayOf(
            R.drawable.one
        )
    //            R.drawable.two
    var widgetName = ""
    private lateinit var gridLayoutManager: GridLayoutManager
    var recyclerView: RecyclerView? = null
    private var dataArray = arrayOfNulls<SyncDeviceSelectionArray>(numbers.size)
    var deviceSynced = ""
    var selectedPosition = -1
    internal var gps_enabled = false
    internal var network_enabled = false
    val REQUEST_CODE = 1001

    override fun onCreate() {
        recyclerView = findViewById(R.id.syncsCategory)
        gridLayoutManager = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = gridLayoutManager
        var user = SharedPreferencesManager.getUserObject(this@SyncDeviceActivity)
        headerTextwelcome.text = "Welcome " + user.data.firstName + "!"
        var lm =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        adapter = RecyclerSyncGridAdapter(this, populateData(), this)
        recyclerView!!.adapter = adapter

        if (intent.hasExtra("disconnect")) {
            skipBttn.visibility = View.GONE
            if (deviceSynced.isNotEmpty() || SharedPreferencesManager.getBoolean(
                    this@SyncDeviceActivity,
                    VAR_ELCIES_CONNCTED
                )
            ) {
                unlink.visibility = View.VISIBLE
            }
            cntnBttn.visibility = View.GONE
            unlinkBttnsLay.visibility = View.VISIBLE
            headerTextwelcome.visibility = View.GONE
            if (intent.hasExtra("elcies")) {
                elciesEnabled = true
                if (elciesEnabled) {
                    showPopupProgressSpinner(true)
                    viewModel.getConnectedDeviceList()
                }
            }
        }

        //when coming from home screen reconnect dialog

        //when coming from home screen reconnect dialog
        onReconnect()

        unlink.setOnClickListener {
            showUnlinkDialog()
        }


        nBttn.setOnClickListener {
            onBackPressed()


        }

        chgBttn.setOnClickListener {
            changeWearable()

        }


        skipBttn.setOnClickListener {
            val intent = Intent(mContext, LandingActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
            startActivity(intent)
            finish()
        }

        cntnBttn.setOnClickListener {
            changeWearable()
            /*if (!gps_enabled && !network_enabled) {
                showGPSDisabledAlertToUser()
            } else if (gps_enabled && network_enabled) {
                val intent = Intent(mContext, GuideActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                startActivity(intent)
            }*/

        }


        mViewModel!!.getListOfDevices().observe(this@SyncDeviceActivity,
            Observer<JSONArray> { mData ->
                showPopupProgressSpinner(false)
                if (mData == null) {
                    viewModel.refreshToken()
                } else {
                    if (mData.length() > 0) {
                        val jsonObject: JSONObject = mData.get(mData.length() - 1) as JSONObject
                        var status: Boolean = jsonObject.getBoolean("status")
                        if (!status) {
                            viewModel.refreshToken()
                        } else {
                            elciesEnabled = false
                            widgetName = jsonObject.getString("name")
                            viewModel.updateWidgetInfo(
                                WidgetBody(
                                    widgetName,
                                    status,
                                    SharedPreferencesManager.getUserId(this@SyncDeviceActivity)
                                )
                            )
                        }
                    } else {
                        showPopupProgressSpinner(false)
                    }
                }
            })

        mViewModel!!.getToken().observe(this@SyncDeviceActivity,
            Observer<RefreshTokenResponse> { mData ->
                if (mData != null && mData.status == 200) {
                    Log.e("Token", "generated" + mData.data.accessToken)
                    SharedPreferencesManager.setString(
                        this@SyncDeviceActivity,
                        mData.data.accessToken,
                        VAR_ELCTOKEN
                    )
                    viewModel.getConnectedDeviceList()
                } else {
                    showPopupProgressSpinner(false)
                    Toast.makeText(
                        HayaTechApplication.applicationContext(),
                        mData.message + " Please try again!!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        mViewModel!!.getWidgetResponse().observe(this@SyncDeviceActivity, Observer { mData ->
            showPopupProgressSpinner(false)
            if (mData != null && mData.status == 200) {
                if (widgetName.isEmpty()
                ) {
                    SharedPreferencesManager.removeKey(
                        this@SyncDeviceActivity,
                        VAR_ELCIES_CONNCTED
                    )
                    selectedPosition = -1       //to rest to no selection
                    adapter.swapData(updateItem(selectedPosition))
                    unlink.visibility = View.GONE
                } else {
                    SharedPreferencesManager.setBoolean(
                        this@SyncDeviceActivity, true,
                        VAR_ELCIES_CONNCTED
                    )
                    selectedPosition = 1
                    adapter.swapData(updateItem(selectedPosition))
                    unlink.visibility = View.VISIBLE
                    Toast.makeText(
                        this@SyncDeviceActivity,
                        "Your device has been connected successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            } else if (mData.status == 400 && mData.message.contains("wearableDeviceChanged")) {
                SharedPreferencesManager.setBoolean(
                    this@SyncDeviceActivity, true,
                    VAR_ELCIES_CONNCTED
                )
                Toast.makeText(
                    this@SyncDeviceActivity,
                    "Wearable Device Changed",
                    Toast.LENGTH_LONG
                ).show()
                selectedPosition = 1
                adapter.swapData(updateItem(selectedPosition))
                unlink.visibility = View.VISIBLE
            } else {
                unlink.visibility = View.GONE
            }
        })

    }

    private fun onReconnect() {
        if (intent.hasExtra(AppConstants.INTENT_RECONNECT)) {
            skipBttn.visibility = View.GONE
            unlink.visibility = View.GONE
            cntnBttn.visibility = View.VISIBLE
            unlinkBttnsLay.visibility = View.GONE
            headerTextwelcome.visibility = View.GONE
            adapter.swapData(populateData())
        }
    }

    private fun changeWearable() {
        if (selectedPosition == 1) {
            if (deviceSynced.isNotEmpty()) {
                //device connected
                if (deviceSynced.isNotEmpty() && MokoSupport.getInstance().isConnDevice(
                        this@SyncDeviceActivity,
                        deviceSynced
                    )
                ) {
                    Toast.makeText(
                        this@SyncDeviceActivity,
                        "You have to unlink Fitpolo, then you can connect with other trackers.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (deviceSynced.isNotEmpty() && !MokoSupport.getInstance().isConnDevice(
                        this@SyncDeviceActivity,
                        deviceSynced
                    )
                ) {
                    //                        MokoSupport.getInstance().connDevice(this@SyncDeviceActivity,deviceSynced,this)
                    //device not connected but linked
                    Toast.makeText(
                        this@SyncDeviceActivity,
                        "Fitpolo connection disconnected. Please reconnect and unlink to change.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (SharedPreferencesManager.getBoolean(
                    this@SyncDeviceActivity,
                    VAR_ELCIES_CONNCTED
                )
            ) {
                Toast.makeText(
                    this@SyncDeviceActivity,
                    "You have to unlink other trackers to change.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val browseIntent = Intent(this, ElciesWebActivity::class.java)
                startActivity(browseIntent)
            }
        } else if (selectedPosition == 0) {
            if (SharedPreferencesManager.getBoolean(
                    this@SyncDeviceActivity,
                    VAR_ELCIES_CONNCTED
                )
            ) {
                Toast.makeText(
                    this@SyncDeviceActivity,
                    "You have to unlink other trackers, then you can connect with Fitpolo.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!SharedPreferencesManager.getBoolean(
                    this@SyncDeviceActivity,
                    VAR_ELCIES_CONNCTED
                ) && (deviceSynced.isNotEmpty() && MokoSupport.getInstance().isConnDevice(this@SyncDeviceActivity,deviceSynced))
            ) {
                Toast.makeText(
                    this@SyncDeviceActivity,
                    "Please unlink the selected connected device before clicking on change button",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                unlinkDevice() //to disconnecrt if any wearable gets connected automatically
                if (!gps_enabled && !network_enabled) {
                    showGPSDisabledAlertToUser()
                } else {
                    val intent = Intent(this@SyncDeviceActivity, GuideActivity::class.java)
                    //                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                    startActivity(intent)
                }
            }
        } else {
            Toast.makeText(
                this@SyncDeviceActivity,
                "Please select any Fitness device?",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun populateData(): Array<SyncDeviceSelectionArray?> {
        for (i in numbers.indices) {
            dataArray[i] =
                SyncDeviceSelectionArray(mContext.getDrawable(numbers[i]), "", false)
        }
        return dataArray
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


    override fun initListeners() {
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_CODE && resultCode == 0) {
            val provider = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            var lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (provider != null) {
                //Start searching for location and update the location text when update available.
                // Do whatever you want
                if (!gps_enabled && !network_enabled) {
                    showGPSDisabledAlertToUser()
                } else if (gps_enabled && network_enabled) {
                    val intent = Intent(mContext, GuideActivity::class.java)
                    startActivity(intent)
                }
            } else {
                //permission not granted
            }
        } else if (requestCode == 2) {
            elciesEnabled = true
        }
    }


    private fun showUnlinkDialog() {
        val builder = AlertDialog.Builder(this@SyncDeviceActivity)
        builder.setMessage("Do you want to unlink this device?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                unlinkDevice()
            }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }


    /**
     * method to unlink wearable
     */

    private fun unlinkDevice() {
        if (SharedPreferencesManager.hasKey(
                this@SyncDeviceActivity,
                WEARABLEID
            ) && deviceSynced.isNotEmpty()
        ) {
            dataArray[0]?.selected = false
            deviceSynced = ""
            MokoSupport.getInstance().disConnectBle()
            unlink.visibility = View.GONE
            SharedPreferencesManager.removeKey(
                this@SyncDeviceActivity,
                SharedPreferencesManager.VAR_WEARABLE
            )

            SharedPreferencesManager.removeKey(this@SyncDeviceActivity, VAR_WEARABLE_CONNECTED)
//            SharedPreferencesManager.removeKey(this@SyncDeviceActivity, WEARABLEID)
           /* Toast.makeText(
                this@SyncDeviceActivity,
                "Device disconnected successfully",
                Toast.LENGTH_LONG
            )
                .show()*/
            unlink.visibility = View.GONE
        } else if (SharedPreferencesManager.getBoolean(
                this@SyncDeviceActivity,
                VAR_ELCIES_CONNCTED
            )
        ) {
            dataArray[1]?.selected = false
            widgetName = ""
            SharedPreferencesManager.removeKey(
                this@SyncDeviceActivity,
                VAR_ELCIES_CONNCTED
            )
            showPopupProgressSpinner(true)
            viewModel.updateWidgetInfo(
                WidgetBody(
                    widgetName,
                    false,
                    SharedPreferencesManager.getUserId(this@SyncDeviceActivity)
                )
            )
//            unlink.visibility = View.GONE

        } else {
            unlink.visibility = View.GONE
        }
        selectedPosition = -1  //to reset to no selection
        adapter.swapData(updateItem(selectedPosition))
    }


    override fun onResume() {
        super.onResume()
        populateData()
        when {
            intent.hasExtra(AppConstants.INTENT_RECONNECT) -> {
                onReconnect()
            }
            SharedPreferencesManager.hasKey(this@SyncDeviceActivity, WEARABLEID) -> {
                deviceSynced =
                    SharedPreferencesManager.getString(this@SyncDeviceActivity, WEARABLEID)
                if(MokoSupport.getInstance().isConnDevice(
                        this@SyncDeviceActivity, deviceSynced
                    )){
                    selectedPosition = 0
                    adapter.swapData(updateItem(selectedPosition))
                    unlink.visibility = View.VISIBLE
                }
            }
            (SharedPreferencesManager.hasKey(this@SyncDeviceActivity, VAR_ELCIES_CONNCTED) && SharedPreferencesManager.getBoolean(this@SyncDeviceActivity, VAR_ELCIES_CONNCTED)) -> {
                selectedPosition = 1
                adapter.swapData(updateItem(selectedPosition))
                unlink.visibility = View.VISIBLE
            }
            else -> {
                selectedPosition = -1
                adapter.swapData(updateItem(selectedPosition))
            }
        }
    }
}

