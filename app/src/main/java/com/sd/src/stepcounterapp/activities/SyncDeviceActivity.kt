package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitpolo.support.MokoSupport
import com.sd.src.stepcounterapp.AppConstants
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.RecyclerSyncGridAdapter
import com.sd.src.stepcounterapp.model.syncDevice.SyncDeviceSelectionArray
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.WEARABLEID
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import kotlinx.android.synthetic.main.activity_select_syncsdk.*


class SyncDeviceActivity : BaseActivity<BaseViewModel>(),
    RecyclerSyncGridAdapter.ItemListener {
    override fun onItemClick(pos: Int, item: SyncDeviceSelectionArray?) {
        if(pos==0){
            populateData()
            adapter.swapData(updateItem(pos))
        }else{
            Toast.makeText(this@SyncDeviceActivity, "Please select Hayatech", Toast.LENGTH_LONG).show()
        }

    }

    private fun updateItem(position:Int): Array<SyncDeviceSelectionArray?> {
        dataArray.forEachIndexed { index, _ ->
            if(position== index){
                dataArray[position]?.selected = !dataArray[position]?.selected!!
            }
        }
        return dataArray
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
    val numbers: IntArray =
        intArrayOf(
            R.drawable.hayatech_fitness,
            R.drawable.fitbit_logo_transparent,
            R.drawable.garmin_logo,
            R.drawable.applewatch,
            R.drawable.polar,
            R.drawable.strava
        )
    private lateinit var gridLayoutManager: GridLayoutManager
    var recyclerView: RecyclerView? = null
    private var dataArray = arrayOfNulls<SyncDeviceSelectionArray>(numbers.size)

    internal var gps_enabled = false
    internal var network_enabled = false
    val REQUEST_CODE = 1001

    override fun onCreate() {
        recyclerView = findViewById(R.id.syncsCategory)
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = gridLayoutManager
        var user = SharedPreferencesManager.getUserObject(this@SyncDeviceActivity)
        headerTextwelcome.text = "Welcome " + user.data.firstName + "!"
        var lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var deviceSynced = SharedPreferencesManager.getString(this@SyncDeviceActivity, WEARABLEID)
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        adapter = RecyclerSyncGridAdapter(this, populateData(), this)
        recyclerView!!.adapter = adapter

        if(intent.hasExtra("disconnect")){
            skipBttn.visibility= View.GONE
            unlink.visibility = View.VISIBLE
            cntnBttn.visibility = View.GONE
            unlinkBttnsLay.visibility = View.VISIBLE
            headerTextwelcome.visibility = View.GONE
        }

        //when coming from home screen reconnect dialog
        if(intent.hasExtra(AppConstants.INTENT_RECONNECT)){
            skipBttn.visibility= View.GONE
            unlink.visibility = View.GONE
            cntnBttn.visibility = View.VISIBLE
            unlinkBttnsLay.visibility = View.GONE
            headerTextwelcome.visibility = View.GONE
        }

        unlink.setOnClickListener {

            if(!MokoSupport.getInstance().isConnDevice(this@SyncDeviceActivity,SharedPreferencesManager.getString(this@SyncDeviceActivity,WEARABLEID))){
                Log.e("disconnect","already")
//                Toast.makeText(this@SyncDeviceActivity, "Device disconnected successfully", Toast.LENGTH_LONG).show()
            }else{
                if(MokoSupport.getInstance().isConnDevice(this@SyncDeviceActivity,deviceSynced)){
                    MokoSupport.getInstance().disConnectBle()
                    SharedPreferencesManager.removeKey(this@SyncDeviceActivity, SharedPreferencesManager.VAR_WEARABLE)
                    SharedPreferencesManager.removeKey(this@SyncDeviceActivity, WEARABLEID)
                    Toast.makeText(this@SyncDeviceActivity, "Device disconnected successfully", Toast.LENGTH_LONG).show()
                }
            }
        }


        nBttn.setOnClickListener {
            val intent = Intent(mContext, LandingActivity::class.java)
            startActivity(intent)
            finish()
        }

        chgBttn.setOnClickListener {
            if(MokoSupport.getInstance().isConnDevice(this@SyncDeviceActivity,deviceSynced)){
                Toast.makeText(this@SyncDeviceActivity, "Please unlink wearable first.",Toast.LENGTH_SHORT).show()
            }else {
                if (!gps_enabled && !network_enabled) {
                    showGPSDisabledAlertToUser()
                } else if (gps_enabled && network_enabled) {
                    val intent = Intent(mContext, GuideActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                    startActivity(intent)
                }
            }
        }


        skipBttn.setOnClickListener {
            val intent = Intent(mContext, LandingActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
            startActivity(intent)
            finish()
        }

        cntnBttn.setOnClickListener {
            if (!gps_enabled && !network_enabled) {
                showGPSDisabledAlertToUser()
            } else if (gps_enabled && network_enabled) {
                val intent = Intent(mContext, GuideActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                startActivity(intent)
            }

        }

    }

    private fun populateData(): Array<SyncDeviceSelectionArray?> {
        for (i in numbers.indices) {
            dataArray[i] = SyncDeviceSelectionArray(mContext.getDrawable(numbers[i]), "", false)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == 0) {
            val provider = Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
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
                //Users did not switch on the GPS
            }
        }
    }

}