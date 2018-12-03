package com.jetec.nordic_googleplay;

import android.util.Log;

import com.jetec.nordic_googleplay.Service.BluetoothLeService;

import java.io.UnsupportedEncodingException;

public class SendValue {

    private BluetoothLeService bluetoothLeService;
    private String TAG = "SendValue";

    public SendValue(BluetoothLeService bluetoothLeService){
        this.bluetoothLeService = bluetoothLeService;
    }

    public void send(String str){
        byte[] sends;
        try {
            sends = str.getBytes("UTF-8");
            Log.e(TAG, "sends = " + str);
            bluetoothLeService.writeRXCharacteristic(sends);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
