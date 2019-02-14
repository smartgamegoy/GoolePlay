package com.jetec.nordic_googleplay.Service;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jetec.nordic_googleplay.Value;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothLeService extends Service {
    private final static String TAG = "bluetoothleservice";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    public BluetoothGatt mBluetoothGatt;
    public int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    public static final int CONNECTION_PRIORITY_HIGH = 1;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 4;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String DEVICE_DOES_NOT_SUPPORT_UART =
            "com.example.bluetooth.le.DEVICE_DOES_NOT_SUPPORT_UART";

    public static String Service_uuid = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    public static String Characteristic_uuid_TX = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    public static String Characteristic_uuid_RX = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    public int flag = 0, count = 0;
    private Boolean oppo = false;
    private ArrayList<String> loglist = new ArrayList<>();

    public void enableTXNotification() {
        //Log.e(TAG,"高速藍牙");
        BluetoothGattService RxService = mBluetoothGatt.getService(UUID.fromString(Service_uuid));
        if (RxService == null) {
            showMessage("Rx service not found on enable!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(UUID.fromString(Characteristic_uuid_TX));
        Log.e(TAG, "TxChar = " + TxChar.getUuid());
        if (TxChar == null) {
            showMessage("Tx charateristic not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(TxChar, true);
        //if(Value.phonename.matches("xiaomi"))
        //mBluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
        //else if(Value.phonename.matches("asus"))
        //mBluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
        /*if (Value.connect_flag == 1) {
            mBluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
        }*/
        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE); //ENABLE_NOTIFICATION_VALUE
        mBluetoothGatt.writeDescriptor(descriptor);
        Log.e(TAG, "mBluetoothGatt = 發生變化");
    }

    public void writeRXCharacteristic(byte[] value) {
        BluetoothGattService RxService = mBluetoothGatt.getService(UUID.fromString(Service_uuid));
        if (RxService == null) {
            showMessage("Rx service not found on RxService!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(UUID.fromString(Characteristic_uuid_RX));
        if (RxChar == null) {
            showMessage("Rx charateristic not found onRxchar!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        RxChar.setValue(value);
        boolean status = mBluetoothGatt.writeCharacteristic(RxChar);
        Log.e(TAG, "writeRXCharacteristic - status = " + status);
    }

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        int getstatus;

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                getstatus = status;
                gatt.discoverServices();
                //onConnectionUpdated(gatt, 6, 0,3200, status);
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
                Log.e(TAG, "已連線");

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                getstatus = status;
                Log.e(TAG, "斷線囉");
                /*gatt.connect();
                Log.e(TAG,"gatt.connect() = " + gatt.connect());*/
                //gatt.connect();
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //if(Value.phonename.matches("asus")) {
            //gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
            /*}
            else if(Value.phonename.matches("xiaomi")){
                gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
            }*/
            if (Value.connect_flag == 1) {
                gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
            }
            if(oppo){
                gatt.disconnect();
                gatt.close();
            }
            readCharacteristic(characteristic);
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        public void onMtuChanged(android.bluetooth.BluetoothGatt gatt, int mtu, int status) {
            Log.e("BLE", "onMtuChanged mtu=" + mtu + ",status=" + status);
        }

        /*private void test(BluetoothGattCharacteristic characteristic){
            try {
                String text = new String(characteristic.getValue(), "UTF-8");
                if(text.startsWith("-") || text.startsWith("+")){
                    flag = 1;
                    count++;
                    loglist.add(text);
                    Log.e(TAG,"count = " + count);
                }
                else if(text.startsWith("INTER")){
                    loglist.clear();
                    count = 0;
                }
                if(loglist != null && loglist.size() != 0 && text.matches("OVER")){
                    Log.e(TAG,"count_totle = " + count);
                    flag = 0;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/

        public void onConnectionUpdated(BluetoothGatt gatt, int interval, int latency, int timeout,
                                        int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "高速藍牙 => 最短間隔(ms) = " + interval + ", 延遲時間 = " + latency + ", 時間限制 = " + timeout);
                /*if (Value.connect_flag == 1) {
                    if(!gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH))
                        gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                }*/
            } else if (status == 0x3b) { // HCI_ERR_UNACCEPT_CONN_INTERVAL
                Log.e(TAG, "onConnectionUpdated received status: Unacceptable connection interval, interval: " + interval + ", latency: " + latency + ", timeout: " + timeout);
            } else {
                Log.e(TAG, "onConnectionUpdated received status: " + status + ", interval: " + interval + ", latency: " + latency + ", timeout: " + timeout);
                //mCallbacks.onError(gatt.getDevice(), ERROR_CONNECTION_PRIORITY_REQUEST, status);
            }
        }
    };

    public void requestConnectionPriority(int priority) {
        String intentAction;
        if (mBluetoothGatt != null) {
            boolean requestConnectionPriority = mBluetoothGatt.requestConnectionPriority(priority);
            /*intentAction = ACTION_GATT_CONNECTED;
            mConnectionState = STATE_CONNECTED;
            broadcastUpdate(intentAction);*/
            //Log.e(TAG, " requestConnectionPriority : " + requestConnectionPriority);
        }
    }


    private void broadcastUpdate(final String action) {
        Intent intent = new Intent(action);
        this.sendBroadcast(intent);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        //Log.e(TAG, "uuid = " + characteristic.getUuid());
        if (UUID.fromString(Characteristic_uuid_TX).equals(characteristic.getUuid())) {
            Intent intent = new Intent(action);
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
            //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            this.sendBroadcast(intent);
            //flag = 0;
        } else {
        }
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.e(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                Log.e(TAG, "mBluetoothGatt connect.");
                return true;
            } else {
                Log.e(TAG, "mBluetoothGatt disconnect.");
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        refreshDeviceCache(mBluetoothGatt);
        Log.d(TAG, "Trying to create a new connection.");
        Log.e(TAG, "refresh = " + refreshDeviceCache(mBluetoothGatt));
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        if(Value.phonename.matches("OPPO")){
            oppo = true;
            mBluetoothGatt.connect();
        }
        mBluetoothGatt.disconnect();
        close();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        // This is specific to Heart Rate Measurement.
        if (UUID.fromString(HEART_RATE_MEASUREMENT).equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;
        return mBluetoothGatt.getServices();
    }

    public boolean refreshDeviceCache(BluetoothGatt gatt) {
        if (mBluetoothGatt != null) {
            try {
                BluetoothGatt localBluetoothGatt = mBluetoothGatt;
                Method localMethod = localBluetoothGatt.getClass().getMethod(
                        "refresh", new Class[0]);
                if (localMethod != null) {
                    boolean bool = ((Boolean) localMethod.invoke(
                            localBluetoothGatt, new Object[0])).booleanValue();
                    return bool;
                }
            } catch (Exception localException) {
                Log.e(TAG, "An exception occured while refreshing device");
            }
        }
        return false;
    }

    private void showMessage(String msg) {
        Log.d(TAG, msg);
    }
}