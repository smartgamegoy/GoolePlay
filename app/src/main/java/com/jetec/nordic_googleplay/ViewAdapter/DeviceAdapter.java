package com.jetec.nordic_googleplay.ViewAdapter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jetec.nordic_googleplay.R;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {
    private List<BluetoothDevice> devices;
    private LayoutInflater inflater;

    public DeviceAdapter(Context context, List<BluetoothDevice> devices) {
        inflater = LayoutInflater.from(context);
        this.devices = devices;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewGroup view;

        if (convertView != null) {
            view = (ViewGroup) convertView;
        } else {
            view = (ViewGroup) inflater.inflate(R.layout.search_device, null);
        }

        BluetoothDevice device = devices.get(position);
        final TextView device_name = view.findViewById(R.id.search_device);
        final TextView device_address = view.findViewById(R.id.device_address);
        device_name.setVisibility(View.VISIBLE);
        device_address.setVisibility(View.VISIBLE);
        if(device.getName() == null) {
            device_name.setText("N/A");
            device_address.setText(device.getAddress());
        }
        else {
            device_name.setText(device.getName());
            device_address.setText(device.getAddress());
        }
        return view;
    }
}