package com.jetec.nordic_googleplay;

import android.bluetooth.BluetoothGatt;

import com.jetec.nordic_googleplay.Service.BluetoothLeService;

public class SendLog extends Thread {

    BluetoothLeService mBluetoothLeService;
    private boolean over;

    public SendLog() {
        super();
    }

    public void set_Service(BluetoothLeService mBluetoothLeService) {
        this.mBluetoothLeService = mBluetoothLeService;
    }

    public void set_over(Boolean over) {
        this.over = over;
    }

    public void run(BluetoothLeService mBluetoothLeService, Boolean over) {
        //noinspection LoopConditionNotUpdatedInsideLoop
        for (; over; ) {
            //sleep(5);
            if (mBluetoothLeService.flag == 1) {
                if (mBluetoothLeService.mConnectionState == 0) {
                    this.over = false;
                    //mBluetoothLeService.mBluetoothGatt.close();
                } else {
                    mBluetoothLeService.mBluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                    //mBluetoothLeService.mBluetoothGatt.discoverServices();
                }
            }
        }
    }
}
