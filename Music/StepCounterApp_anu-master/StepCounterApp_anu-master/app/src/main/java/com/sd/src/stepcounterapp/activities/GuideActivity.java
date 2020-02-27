package com.sd.src.stepcounterapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.fitpolo.support.MokoSupport;
import com.sd.src.stepcounterapp.AppConstants;
import com.sd.src.stepcounterapp.R;
import com.sd.src.stepcounterapp.service.MokoService;
import com.sd.src.stepcounterapp.utils.Utils;

public class GuideActivity extends Basefit {

    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isWriteStoragePermissionOpen()) {
                showRequestPermissionDialog();
                return;
            }
        }
        if (isLocationPermissionOpen()) {
            delayGotoMain();
        }
    }

        private void delayGotoMain () {
            if (!MokoSupport.getInstance().isBluetoothOpen()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, AppConstants.REQUEST_CODE_ENABLE_BT);
                return;
            }
//        if (!Utils.INSTANCE.isLocServiceEnable(this)) {
            if (getLocationStatus()) {
                showOpenLocationDialog();
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!isLocationPermissionOpen()) {
                    showRequestPermissionDialog2();
                    return;
                } else {
                    AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                    int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_COARSE_LOCATION, Process.myUid(), getPackageName());
                    if (checkOp != AppOpsManager.MODE_ALLOWED) {
                        showOpenSettingsDialog2();
                        return;
                    }
                }
            }
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startService(new Intent(GuideActivity.this, MokoService.class));
                }
            }.start();
            startActivity(new Intent(this, DeviceSynchronizingActivity.class));
        }

        private boolean getLocationStatus () {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc.equals(null)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            return false;
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, String[] permissions,
        int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case AppConstants.PERMISSION_REQUEST_CODE: {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            boolean shouldShowRequest = shouldShowRequestPermissionRationale(permissions[0]);
                            if (shouldShowRequest) {
                                if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                    showRequestPermissionDialog2();
                                } else {
                                    showRequestPermissionDialog();
                                }
                            } else {
                                if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                    showOpenSettingsDialog2();
                                } else {
                                    showOpenSettingsDialog();
                                }
                            }
                        } else {
                            delayGotoMain();
                        }
                    }
                }
            }
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == AppConstants.REQUEST_CODE_ENABLE_BT) {
                delayGotoMain();
            }
            if (requestCode == 1) {
                delayGotoMain();
            }
            if (requestCode == AppConstants.REQUEST_CODE_PERMISSION) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!isWriteStoragePermissionOpen()) {
                        showOpenSettingsDialog();
                    } else {
                        delayGotoMain();
                    }
                }
            }
            if (requestCode == AppConstants.REQUEST_CODE_PERMISSION_2) {
                delayGotoMain();
            }
            if (requestCode == AppConstants.REQUEST_CODE_LOCATION_SETTINGS) {
                if (Utils.INSTANCE.isLocServiceEnable(this)) {
                    showOpenLocationDialog();
                } else {
                    delayGotoMain();
                }
            }
        }

        private void showOpenSettingsDialog () {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.permission_storage_close_title)
                    .setMessage(R.string.permission_storage_close_content)
                    .setPositiveButton(getString(R.string.permission_open), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            // 根据包名打开对应的设置界面
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, AppConstants.REQUEST_CODE_PERMISSION);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            return;
                        }
                    }).create();
            dialog.show();
        }

        private void showRequestPermissionDialog () {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.permission_storage_need_title)
                    .setMessage(R.string.permission_storage_need_content)
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(GuideActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    AppConstants.PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            return;
                        }
                    }).create();
            dialog.show();
        }

        private void showOpenLocationDialog () {
            //Start searching for location and update the location text when update available.
            // Do whatever you want
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.location_need_title)
                    .setMessage(R.string.location_need_content)
                    .setPositiveButton(getString(R.string.permission_open), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, AppConstants.REQUEST_CODE_LOCATION_SETTINGS);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            return;
                        }
                    }).create();
            dialog.show();
        }

        private void showOpenSettingsDialog2 () {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.permission_location_close_title)
                    .setMessage(R.string.permission_location_close_content)
                    .setPositiveButton(getString(R.string.permission_open), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            // 根据包名打开对应的设置界面
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, AppConstants.REQUEST_CODE_PERMISSION_2);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        finish();
                            return;
                        }
                    }).create();
            dialog.show();
        }

        private void showRequestPermissionDialog2 () {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.permission_location_need_title)
                    .setMessage(R.string.permission_location_need_content)
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(GuideActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    AppConstants.PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            return;
                        }
                    }).create();
            dialog.show();
        }


        @Override
        protected void onResume () {
            super.onResume();

        }
    }