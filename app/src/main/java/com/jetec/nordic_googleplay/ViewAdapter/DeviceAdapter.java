package com.jetec.nordic_googleplay.ViewAdapter;

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
    private Context context;
    private List<BluetoothDevice> devices;
    private LayoutInflater inflater;
    private double all_Width;

    public DeviceAdapter(Context context, List<BluetoothDevice> devices, double all_Width) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.devices = devices;
        this.all_Width = all_Width;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewGroup view;

        if (convertView != null) {
            view = (ViewGroup) convertView;
        } else {
            view = (ViewGroup) inflater.inflate(R.layout.search_device, null);
        }

        BluetoothDevice device = devices.get(position);
        final TextView device_name = ((TextView) view.findViewById(R.id.search_device));
        final TextView device_address = ((TextView) view.findViewById(R.id.device_address));
        device_name.setPadding((int)(all_Width/6),0,0,0);
        device_address.setPadding((int)(all_Width/6),0,0,0);
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