package com.sd.src.stepcounterapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fitpolo.support.entity.BleDevice;
import com.sd.src.stepcounterapp.R;

/**
 * @Date 2017/5/11
 * @Author wenzheng.liu
 * @Description
 */

public class DeviceAdapter extends FitpoloBaseAdapter<BleDevice> {


    public DeviceAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindViewHolder(int position, ViewHolder viewHolder, View convertView, ViewGroup parent) {
        final DeviceViewHolder holder = (DeviceViewHolder) viewHolder;
        final BleDevice device = getItem(position);
        holder.deviceName.setText(device.name);
        holder.deviceAddress.setText(device.address);
        holder.deviceRssi.setText(device.rssi + "");
    }

    @Override
    protected ViewHolder createViewHolder(int position, LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.device_list_item, parent, false);
        return new DeviceViewHolder(view);
    }

    class DeviceViewHolder extends ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRssi;

        public DeviceViewHolder(View convertView) {
            super(convertView);
            deviceName = convertView.findViewById(R.id.device_name);
            deviceAddress = convertView.findViewById(R.id.device_address);
            deviceRssi = convertView.findViewById(R.id.device_rssi);
        }
    }
}
