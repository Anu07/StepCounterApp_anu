package com.sd.src.stepcounterapp.activities


import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fitpolo.support.MokoConstants
import com.fitpolo.support.MokoSupport
import com.fitpolo.support.callback.MokoScanDeviceCallback
import com.fitpolo.support.entity.BleDevice
import com.fitpolo.support.entity.DailyStep
import com.fitpolo.support.entity.OrderTaskResponse
import com.fitpolo.support.log.LogModule
import com.fitpolo.support.task.ZReadStepTask
import com.fitpolo.support.task.ZWriteSystemTimeTask
import com.sd.src.stepcounterapp.AppConstants
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.DeviceAdapter
import com.sd.src.stepcounterapp.service.DfuService
import com.sd.src.stepcounterapp.service.MokoService
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager.WEARABLEID
import com.sd.src.stepcounterapp.utils.Utils
import kotlinx.android.synthetic.main.scanbar.*
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter
import no.nordicsemi.android.dfu.DfuServiceListenerHelper
import java.text.SimpleDateFormat
import java.util.*


/**
 * @Date 2017/5/11
 * @Author wenzheng.liu
 * @Description
 */

class DeviceListActivity : Basefit(), AdapterView.OnItemClickListener, MokoScanDeviceCallback {
    private var deviceWearableId: String? = null
    internal var lvDevice: GridView? = null
    private var mDeviceConnectCount: Int = 0
    private var mDatas: ArrayList<BleDevice>? = null
    private var mAdapter: DeviceAdapter? = null
    private var mDialog: ProgressDialog? = null
    private var mService: MokoService? = null
    private var mDevice: BleDevice? = null
    private var deviceMap: HashMap<String, BleDevice>? = null
    var lastestSteps: ArrayList<DailyStep>? = null
    private val mDfuProgressListener = object : DfuProgressListenerAdapter() {
        override fun onDeviceConnecting(deviceAddress: String?) {
            LogModule.w("onDeviceConnecting...")
            mDeviceConnectCount++
            if (mDeviceConnectCount > 3) {
                Toast.makeText(this@DeviceListActivity, "Error:DFU Failed", Toast.LENGTH_SHORT).show()
                dismissDFUProgressDialog()
                val manager = LocalBroadcastManager.getInstance(this@DeviceListActivity)
                val abortAction = Intent(DfuService.BROADCAST_ACTION)
                abortAction.putExtra(DfuService.EXTRA_ACTION, DfuService.ACTION_ABORT)
                manager.sendBroadcast(abortAction)
            }
        }

        override fun onDeviceDisconnecting(deviceAddress: String?) {
            LogModule.w("onDeviceDisconnecting...")
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
            Toast.makeText(this@DeviceListActivity, "DfuCompleted!", Toast.LENGTH_SHORT).show()
            dismissDFUProgressDialog()
            this@DeviceListActivity.finish()
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
            Toast.makeText(this@DeviceListActivity, "Error:" + message!!, Toast.LENGTH_SHORT).show()
            LogModule.i("Error:$message")
            dismissDFUProgressDialog()
        }
    }


     val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            mDialog!!.dismiss()
            if (intent != null) {
                val action = intent.action
                if (MokoConstants.ACTION_DISCOVER_SUCCESS == intent.action) {
                    abortBroadcast()
                    if (!this@DeviceListActivity.isFinishing() && mDialog!!.isShowing) {
                        mDialog!!.dismiss()
                    }
                    Toast.makeText(this@DeviceListActivity, "Connect success", Toast.LENGTH_SHORT).show()
                    getLastestSteps()
                    updateDeviceTime()
                }
                if (MokoConstants.ACTION_CONN_STATUS_DISCONNECTED == intent.action) {
                    abortBroadcast()
                    if (MokoSupport.getInstance().isBluetoothOpen && MokoSupport.getInstance().reconnectCount > 0) {
                        Toast.makeText(this@DeviceListActivity, "Device disconnected", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (!this@DeviceListActivity.isFinishing() && mDialog!!.isShowing) {
                        mDialog!!.dismiss()
                    }
                    Toast.makeText(this@DeviceListActivity, "Device disconnected", Toast.LENGTH_SHORT).show()
                }
                if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                    val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                    when (blueState) {
                        BluetoothAdapter.STATE_TURNING_OFF, BluetoothAdapter.STATE_OFF -> this@DeviceListActivity.finish()
                    }
                }
                if (MokoConstants.ACTION_CONN_STATUS_DISCONNECTED == action) {
                    abortBroadcast()
                    Toast.makeText(this@DeviceListActivity, "Device disconnected", Toast.LENGTH_SHORT).show()
                    this@DeviceListActivity.finish()
                }
                if (MokoConstants.ACTION_ORDER_RESULT == action) {
                    val response =
                        intent.getSerializableExtra(MokoConstants.EXTRA_KEY_RESPONSE_ORDER_TASK) as OrderTaskResponse
                    val orderEnum = response.order
                    lastestSteps = MokoSupport.getInstance().dailySteps
                    if (lastestSteps == null || lastestSteps!!.isEmpty()) {
                        return
                    }
                 /*   for (step in lastestSteps!!) {

                        Toast.makeText(
                            this@DeviceListActivity,
                            "Total steps taken till now: " + lastestSteps!![lastestSteps!!.size - 1].count,
                            Toast.LENGTH_LONG
                        ).show()
                    }*/
                    SharedPreferencesManager.saveSyncObject(this@DeviceListActivity, lastestSteps)
                }
                if (MokoConstants.ACTION_ORDER_TIMEOUT == action) {
                    Toast.makeText(this@DeviceListActivity, "Timeout", Toast.LENGTH_SHORT).show()
                }
                if (MokoConstants.ACTION_ORDER_FINISH == action) {
//                    Toast.makeText(this@DeviceListActivity, "Success", Toast.LENGTH_SHORT).show()
                    Log.i("steps",""+lastestSteps)
                    gotoDeviceconnctd()
                }
            }
        }
    }

    private fun updateDeviceTime() {
        setSystemTime()
    }

    private fun gotoDeviceconnctd() {
        val intent = Intent(this@DeviceListActivity, DeviceConnctdActivity::class.java)
        //                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
        intent.putExtra("Steps", lastestSteps?.get(lastestSteps!!.size - 1)!!.count)
        startActivity(intent)
        finish()
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
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_devices)
//        mDevice = intent.getSerializableExtra("device") as BleDevice
        bindService(Intent(this, MokoService::class.java), mServiceConnection, BIND_AUTO_CREATE)
        initContentView()
    }

    private fun initContentView() {
        lvDevice = findViewById(R.id.lv_device)
        mDialog = ProgressDialog(this)
        mDatas = ArrayList()
        deviceMap = HashMap()
        mAdapter = DeviceAdapter(this)
        mAdapter!!.items = mDatas
        lvDevice!!.adapter = mAdapter
        lvDevice!!.onItemClickListener = this
        img_scan.setOnClickListener {
            searchDevices()
        }
        img_close.setOnClickListener {
            finish()
        }
        searchDevices()
    }

    fun searchDevices() {
        MokoSupport.getInstance().startScanDevice(this)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        mDialog!!.setMessage("Connect...")
        mDialog!!.show()
        val device = parent.getItemAtPosition(position) as BleDevice
        deviceWearableId = device.address
        SharedPreferencesManager.setString(this@DeviceListActivity, device.address, WEARABLEID)
        mService!!.connectBluetoothDevice(device.address)
        mDevice = device
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
        unbindService(mServiceConnection)
        stopService(Intent(this, MokoService::class.java))
    }

    override fun onStartScan() {
        deviceMap!!.clear()
        mDialog!!.setMessage("Scanning...")
         mDialog!!.show()
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
        mAdapter!!.notifyDataSetChanged()
//        }
    }

    override fun onStopScan() {
        if (!this@DeviceListActivity.isFinishing() && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }

//        if (deviceMap!!.values.isEmpty()) {
//            noAvlbl.visibility = View.VISIBLE
//            lvDevice!!.visibility = View.GONE
//        } else {
//            noAvlbl.visibility = View.GONE
//            lvDevice!!.visibility = View.VISIBLE
        mDatas!!.clear()
        mDatas!!.addAll(deviceMap!!.values)
        mAdapter!!.notifyDataSetChanged()
//        }
    }

    companion object {
        private val TAG = "MainActivity"
    }

    override fun onPause() {
        super.onPause()
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener)

    }


    override fun onResume() {
        super.onResume()
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener)

    }


    fun getLastestSteps() {
        var calendar: Calendar? = null
        calendar = Utils.strDate2Calendar(
             "2018-06-01 00:00",
            AppConstants.PATTERN_YYYY_MM_DD_HH_MM
        )
        MokoSupport.getInstance().sendOrder(ZReadStepTask(mService, calendar!!))
    }


    private var mDFUDialog: ProgressDialog? = null

    private fun showDFUProgressDialog(tips: String) {
        mDFUDialog = ProgressDialog(this@DeviceListActivity)
        mDFUDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDFUDialog!!.setCanceledOnTouchOutside(false)
        mDFUDialog!!.setCancelable(false)
        mDFUDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mDFUDialog!!.setMessage(tips)
        if (!isFinishing && mDFUDialog != null && !mDFUDialog!!.isShowing) {
            mDFUDialog!!.show()
        }
    }

    private fun dismissDFUProgressDialog() {
        mDeviceConnectCount = 0
        if (!isFinishing && mDFUDialog != null && mDFUDialog!!.isShowing) {
            mDFUDialog!!.dismiss()
        }
    }


    private fun getToday(): String? {
        //2019-08-07
        var c = Calendar.getInstance().getTime()
        var df: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return df.format(c)
    }

    /**
     * Set system time from device
     */


    fun setSystemTime() {
        MokoSupport.getInstance().sendOrder(ZWriteSystemTimeTask(mService))
    }


}
