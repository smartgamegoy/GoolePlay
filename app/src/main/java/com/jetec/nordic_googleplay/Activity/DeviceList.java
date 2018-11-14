package com.jetec.nordic_googleplay.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import com.jetec.nordic_googleplay.ViewAdapter.DeviceAdapter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Thread.sleep;

public class DeviceList extends AppCompatActivity {

    private static final int UART_PROFILE_DISCONNECTED = 21;
    private int mState = UART_PROFILE_DISCONNECTED;
    private static final long SCAN_PERIOD = 8000; //8 seconds

    private ConnectThread connectThread;
    private Handler mHandler;
    private BluetoothDevice device;
    private DeviceAdapter deviceAdapter;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> deviceList;
    private Vibrator vibrator;
    private View no_device;
    private ListView list_device;
    private Intent intents;
    private ArrayList<String> return_RX, SelectItem, DataSave, SQLdata, Logdata;
    private String[] check_arr = {"PV1", "PV2", "EH1", "EL1", "EH2", "EL2", "CR1", "CR2", "ADR"/*, "DPP"*/, "OVER"};
    private Dialog progressDialog = null, progressDialog2 = null;
    private String TAG = "DeviceList", BID, text, log = "log", Jetec = "Jetec", E_word, P_word, G_word, get_fun = "get";
    private double all_Width, all_Height;
    private int jsonflag, send, connect_flag, check, flag;
    private boolean s_connect = false, setdpp = false;
    private byte[] txValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        BluetoothManager bluetoothManager = getManager(this);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        get_intent();
    }

    private void get_intent(){

        mHandler = new Handler();
        connectThread = new ConnectThread();

        Intent intent = getIntent();
        all_Width = intent.getDoubleExtra("all_Width", all_Width);
        all_Height = intent.getDoubleExtra("all_Height", all_Height);
        jsonflag = intent.getIntExtra("jsonflag", jsonflag);
        send = intent.getIntExtra("send", send);
        connect_flag = intent.getIntExtra("connect_flag", connect_flag);

        show_device();
    }

    private void show_device(){
        setContentView(R.layout.activity_main);

        flag = 0;
        double bar = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            bar = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        Log.e(TAG,"bar_height = " + bar);

        LinearLayout linear_view = (LinearLayout)findViewById(R.id.linear_view);
        no_device = findViewById( R.id.no_data);
        no_device.setVisibility( View.VISIBLE );   //VISIBLE / GONE
        linear_view.setPadding(0,(int)bar,0,0);

        device_list();
    }

    private void device_list(){
        return_RX = new ArrayList<String>();
        SelectItem = new ArrayList<String>();
        DataSave = new ArrayList<String>();
        SQLdata = new ArrayList<String>();
        Logdata = new ArrayList<String>();

        deviceList = new ArrayList<BluetoothDevice>();
        deviceAdapter = new DeviceAdapter(this, deviceList, all_Width);

        list_device = (ListView)findViewById(R.id.list_data);
        list_device.setAdapter(deviceAdapter);
        list_device.setOnItemClickListener(mDeviceClickListener);

        scanLeDevice(true);
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            device = deviceList.get(position);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Log.e(TAG,"position = " + position);

            connectThread.run();
        }
    };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addDevice(device);
                                }
                            });
                        }
                    });
                }
            };

    private void addDevice(BluetoothDevice device) {
        boolean deviceFound = false;

        for (BluetoothDevice listDev : deviceList) {
            if (listDev.getAddress().equals(device.getAddress())) {
                deviceFound = true;
                break;
            }
        }
        if (!deviceFound) {
            deviceList.add(device);
            no_device.setVisibility(View.GONE);
            list_device.setVisibility( View.VISIBLE );
            deviceAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler_remote_connec = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Remote_connec();
        }
    };

    private void Remote_connec(){
        BID = device.getAddress();
        Intent gattServiceIntent = new Intent(DeviceList.this, BluetoothLeService.class);
        s_connect = bindService(gattServiceIntent, mServiceConnection, DeviceList.this.BIND_AUTO_CREATE);
        if(s_connect == true){
            if(connect_flag != 3 && connect_flag != 4) {
                progressDialog = writeDialog(DeviceList.this, false, getString(R.string.connecting));
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            }
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }
        else {
            Log.e(TAG,"服務綁訂狀態  = " + s_connect );
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.e(TAG,"連線中" );
            //https://github.com/googlesamples/android-BluetoothLeGatt/tree/master/Application/src/main/java/com/example/android/bluetoothlegatt
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG,"初始化失敗");
            }
            mBluetoothLeService.connect(BID);
            Log.e(TAG,"進入連線");
        }
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            Log.e(TAG,"失去連線端");
        }
    };

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intents = intent;
            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.e(TAG, "連線成功");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                s_connect = false;
                progressDialog.dismiss();
                if (connect_flag == 1) {
                    Log.e(TAG, "連線中斷" + connect_flag);
                    Toast.makeText(DeviceList.this, getString(R.string.connect_err), Toast.LENGTH_SHORT).show();
                    new Thread(connectfail).start();
                }
                else if(connect_flag == 2){
                    Log.e(TAG, "連線中斷" + connect_flag);
                    Toast.makeText(DeviceList.this, getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
                    new Thread(connectfail).start();
                }
                else if(connect_flag == 3){
                    Log.e(TAG, "連線中斷" + connect_flag);
                    try {
                        sleep(500);
                        new Thread(connectfail).start();
                        sleep(2000);
                        connectThread = new ConnectThread();
                        connectThread.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if(connect_flag == 4){
                    Log.e(TAG, "連線中斷" + connect_flag);
                    try {
                        Toast.makeText(DeviceList.this, getString(R.string.reconnecting), Toast.LENGTH_SHORT).show();
                        new Thread(connectfail).start();
                        sleep(2000);
                        connectThread = new ConnectThread();
                        connectThread.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                //displayGattServices(mBluetoothLeService.getSupportedGattServices());
                Log.e(TAG,"連線狀態改變");
                mBluetoothLeService.enableTXNotification();
                new Thread(sendcheck).start();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            txValue = intents.getByteArrayExtra(mBluetoothLeService.EXTRA_DATA);
                            text = new String(txValue, "UTF-8");
                            Log.e(TAG,"send = " + send);
                            Log.e(TAG,"text = " + text);
                            Log.e(TAG,"check = " + check);
                            if (text.startsWith("OK")) {
                                if(connect_flag != 3) {
                                    SQLdata.clear();
                                    DataSave.clear();
                                    return_RX.clear();
                                    SelectItem.clear();
                                    Logdata.clear();
                                    new Thread(sendpassword).start();
                                }
                                else {
                                }
                            } else if (text.startsWith("ENGE")) {
                                E_word = text.substring(4, text.length());
                                Log.e(TAG, "管理者密碼 = " + E_word);
                            } else if (text.startsWith("PASS")) {
                                P_word = text.substring(4, text.length());
                                Log.e(TAG, "客戶密碼 = " + P_word);
                            } else if (text.startsWith("GUES")) {
                                G_word = text.substring(4, text.length());
                                if(connect_flag == 3){
                                }
                                else if (connect_flag == 4) {
                                    login();
                                }
                                else
                                    check();
                                Log.e(TAG, "訪客密碼 = " + G_word);
                            }
                            else {
                                if (send == 0 || send == 1) {
                                    if (text.matches("OVER")) {
                                        //check = check + 1;
                                        Log.e(TAG, "checkOVER = " + text);
                                        Log.e(TAG, "check = " + check);
                                        Log.e(TAG, "return_RX = " + return_RX);
                                        if (checkDeviceName(text).matches(check_arr[check])) {
                                            if (return_RX.size() != 8) {
                                                Log.e(TAG, "RX = " + return_RX);
                                                device_function();
                                                progressDialog2.dismiss();
                                            }
                                        }
                                    } else {
                                        if (text.startsWith("DPP") && text.substring(text.indexOf("+") + 1, text.length()).replaceFirst("^0*", "").matches("1.0")) {
                                            setdpp = true;
                                            if (checkDeviceName(text).matches(check_arr[check])) {
                                                SelectItem.add(checkDeviceName(text));
                                                return_RX.add(getString(R.string.DPP_status_on));
                                                DataSave.add(getString(R.string.DPP_status_on));
                                            } else {
                                            }
                                        } else if (text.startsWith("DPP") && text.substring(text.indexOf("+") + 1, text.length()).matches("0000.0")) {
                                            setdpp = false;
                                            if (checkDeviceName(text).matches(check_arr[check])) {
                                                SelectItem.add(checkDeviceName(text));
                                                return_RX.add(getString(R.string.DPP_status_off));
                                                DataSave.add(getString(R.string.DPP_status_off));
                                            } else {
                                            }
                                        } else {
                                            if (checkDeviceName(text).matches(check_arr[check])) {
                                                if (SelectItem != null && SelectItem.size() > 0 && return_RX != null && DataSave != null) {
                                                    SelectItem.add(checkDeviceName(text));
                                                    return_RX.add(text);
                                                    DataSave.add(text);
                                                    Log.e(TAG, "check = " + check_arr[check]);
                                                    check = check + 1;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };

    private Runnable connectfail = new Runnable() {
        @Override
        public void run() {
            mState = UART_PROFILE_DISCONNECTED;
            unbindService(mServiceConnection);
        }
    };

    private Runnable sendcheck = new Runnable() {
        @Override
        public void run() {
            try {
                sleep(500);
                Log.e(TAG, "sends = " + Jetec);
                if(s_connect)
                    sending(Jetec);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable sendpassword = new Runnable() {
        @Override
        public void run() {
            try {
                sleep(100);
                Log.e(TAG, "log delay時間");
                sending("Delay+00015.0");
                sleep(100);
                Log.e(TAG, "管理者密碼確認");
                sending("ENGEWD");  //ENGEWD = 管理者密碼確認
                sleep(100);
                Log.e(TAG, "客戶密碼確認");
                sending("PASSWD");  //PASSWD = 客戶密碼確認(只有此密碼可以修改)
                sleep(100);
                Log.e(TAG, "訪客密碼確認");
                sending("GUESWD");  //GUESWD = 訪客密碼確認
                sleep(100);
                if(connect_flag == 3) {
                    send = 4;
                    sending(log);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    private void sending(String value) throws UnsupportedEncodingException {
        byte[] sends;
        sends = value.getBytes("UTF-8");
        mBluetoothLeService.writeRXCharacteristic(sends);
    }

    private void login() throws UnsupportedEncodingException, InterruptedException {
        check = 0;
        SelectItem.add("NAME");
        DataSave.add(device.getName());
        sending(get_fun);
        if(connect_flag != 3 && connect_flag != 4) {
            progressDialog2 = writeDialog(DeviceList.this, false, getString(R.string.login));
            progressDialog2.show();
            progressDialog2.setCanceledOnTouchOutside(false);
        }
        new Thread(timedelay).start();
    }

    private String checkDeviceName(String name){

        String rename = "";
        //String sub = name.substring(0, name.indexOf("+"));

        if(name.startsWith("PV1")){
            rename = "PV1";
        }
        if(name.startsWith("PV2")){
            rename = "PV2";
        }
        if(name.startsWith("PV3")){
            rename = "PV3";
        }
        if(name.startsWith("EH1")){
            rename = "EH1";
        }
        if(name.startsWith("EH2")){
            rename = "EH2";
        }
        if(name.startsWith("EH3")){
            rename = "EH3";
        }
        if(name.startsWith("EL1")){
            rename = "EL1";
        }
        if(name.startsWith("EL2")){
            rename = "EL2";
        }
        if(name.startsWith("EL3")){
            rename = "EL3";
        }
        if(name.startsWith("CR1")){
            rename = "CR1";
        }
        if(name.startsWith("CR2")){
            rename = "CR2";
        }
        if(name.startsWith("CR3")){
            rename = "CR3";
        }
        if(name.startsWith("ADR")){
            rename = "ADR";
        }
        if(name.startsWith("DPP")){
            rename = "DPP";
        }
        if(name.startsWith("OVER")){
            rename = "OVER";
        }
        return rename;
    }

    private Runnable timedelay = new Runnable() {
        @Override
        public void run() {
            try {
                sleep(1500);
                Log.e(TAG,"check = " + check);
                Log.e(TAG,"check = " + check_arr[check]);
                if(check_arr[check].matches("OVER")) {
                }
                else {
                    connect_flag = 4;
                    sending("STOP");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Service_close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    private Dialog writeDialog(Context context, boolean isAlpha, String message) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (isAlpha) {
            WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();
            lp.alpha = 0.8f;
            progressDialog.getWindow().setAttributes(lp);
        } else {
            progressDialog.dismiss();
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.running, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.ll_dialog);
        ProgressBar pb_progress_bar = (ProgressBar) v.findViewById(R.id.pb_progress_bar);
        pb_progress_bar.setVisibility(View.VISIBLE);
        TextView tv = (TextView) v.findViewById(R.id.tv_loading);

        if (message == null || message.equals("")) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setText(message);
            tv.setTextColor(context.getResources().getColor(R.color.colorDialog));
        }

        progressDialog.setContentView(layout, new LinearLayout.LayoutParams((int)(all_Width / 2),
                (int)(all_Height / 5)));

        return progressDialog;
    }

    private void check() throws InterruptedException {

        setContentView(R.layout.checkpassword);

        connect_flag = 2;
        flag = 1;

        Button by = (Button)findViewById(R.id.button2);
        Button bn = (Button)findViewById(R.id.button1);
        TextView t1 = (TextView)findViewById(R.id.textView3);
        EditText e1 = (EditText)findViewById(R.id.editText1);

        progressDialog.dismiss();
        sleep(30);

        t1.setText(getString(R.string.device_name) + "： " + device.getName());

        by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                if(e1.getText().toString().length() == 6) {
                    if (e1.getText().toString().trim().matches(E_word) || e1.getText().toString().trim().matches(P_word)) {
                        try {
                            send = 0;
                            Log.e(TAG, "管理者/客戶 登入");
                            login();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (e1.getText().toString().trim().matches(G_word)) {
                        try {
                            send = 1;
                            Log.e(TAG, "訪客 登入");
                            login();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(DeviceList.this, getString(R.string.passworderror), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(DeviceList.this, getString(R.string.inputerror), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                if (mBluetoothAdapter != null && mBluetoothLeService != null) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Service_close();
                }
                show_device();
            }
        });
    }

    private void device_function(){

        Intent intent = new Intent(DeviceList.this, DeviceFunction.class);
        intent.putExtra("device_name", device.getName());
        intent.putExtra("device_address", device.getAddress());
        intent.putExtra("E_word", E_word);
        intent.putExtra("P_word", P_word);
        intent.putExtra("G_word", G_word);
        intent.putExtra("all_Width", all_Width);
        intent.putExtra("all_Height", all_Height);
        intent.putExtra("jsonflag", jsonflag);
        intent.putExtra("send", send);
        intent.putExtra("connect_flag", connect_flag);
        intent.putExtra("check", check);
        intent.putExtra("s_connect", s_connect);
        intent.putExtra("setdpp", setdpp);
        intent.putStringArrayListExtra("return_RX", return_RX);
        intent.putStringArrayListExtra("SelectItem", SelectItem);
        intent.putStringArrayListExtra("DataSave", DataSave);
        intent.putStringArrayListExtra("SQLdata", SQLdata);
        intent.putStringArrayListExtra("Logdata", Logdata);

        startActivity(intent);
        finish();
    }

    private void Service_close() {
        if (mBluetoothLeService == null) {
            Log.e(TAG,"masaga");
            return;
        }
        mBluetoothLeService.disconnect();
    }

    public static BluetoothManager getManager(Context context) {    //獲取此設備默認藍芽適配器
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    private class ConnectThread extends Thread {
        public void run() {
            Message message = new Message();
            message.obj=(Integer)2;
            handler_remote_connec.sendMessage(message);
        }
    }

    private void backtofirst(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK:
                if(flag == 0){
                    vibrator.vibrate(100);
                    backtofirst();
                }
                else if(flag == 1){
                    vibrator.vibrate(100);
                    if(mBluetoothAdapter != null)
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Service_close();
                    show_device();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        if (mBluetoothLeService != null) {
            if(s_connect) {
                unbindService(mServiceConnection);
                s_connect = false;
            }
            mBluetoothLeService.stopSelf();
            mBluetoothLeService = null;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(s_connect) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            if (mBluetoothLeService != null) {
                final boolean result = mBluetoothLeService.connect(BID);
                Log.d(TAG, "Connect request result=" + result);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(s_connect)
            unregisterReceiver(mGattUpdateReceiver);
    }
}
