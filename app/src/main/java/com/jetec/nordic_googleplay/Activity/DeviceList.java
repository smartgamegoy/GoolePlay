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
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.jetec.nordic_googleplay.CheckDeviceName;
import com.jetec.nordic_googleplay.GetString;
import com.jetec.nordic_googleplay.SendLog;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import com.jetec.nordic_googleplay.Thread.ConnectThread;
import com.jetec.nordic_googleplay.ViewAdapter.DeviceAdapter;
import com.jetec.nordic_googleplay.Initialization;
import com.jetec.nordic_googleplay.SQL.ModelSQL;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.SendValue;
import com.jetec.nordic_googleplay.Value;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class DeviceList extends AppCompatActivity {

    private static final long SCAN_PERIOD = 8000; //8 seconds

    private ModelSQL modelSQL;
    private CheckDeviceName checkDeviceName;
    private GetString setString;
    private Initialization initialization;
    private Handler mHandler;
    private SendValue sendValue;
    private BluetoothDevice device;
    private DeviceAdapter deviceAdapter;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> deviceList;
    private Vibrator vibrator;
    private JSONArray modelJSON;
    private View no_device;
    private ListView list_device;
    private Intent intents;
    private ArrayList<String> return_RX, SelectItem, DataSave, Jsonlist;
    private Dialog progressDialog = null, progressDialog2 = null;
    private String TAG = "DeviceList";
    private String text;
    private String Jetec = "Jetec";
    private int check, flag;
    private boolean s_connect = false;
    private byte[] txValue;
    private SendLog sendLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        BluetoothManager bluetoothManager = getManager(this);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        modelSQL = new ModelSQL(this);
        try {
            get_intent();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler connectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Remote_connec();
        }
    };

    private void get_intent() throws JSONException {

        Intent intent = getIntent();
        String[] default_model = intent.getStringArrayExtra("default_model");
        modelJSON = new JSONArray(default_model);

        show_device();
    }

    private void show_device() {

        setContentView(R.layout.activity_main);
        flag = 0;

        mHandler = new Handler();
        checkDeviceName = new CheckDeviceName();
        setString = new GetString();

        no_device = findViewById(R.id.no_data);
        no_device.setVisibility(View.VISIBLE);   //VISIBLE / GONE
        device_list();
    }

    private void device_list() {
        return_RX = new ArrayList<>();
        SelectItem = new ArrayList<>();
        DataSave = new ArrayList<>();
        Jsonlist = new ArrayList<>();
        deviceList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(this, deviceList);

        list_device = findViewById(R.id.list_data);
        list_device.setAdapter(deviceAdapter);
        list_device.setOnItemClickListener(mDeviceClickListener);

        scanLeDevice();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            device = deviceList.get(position);
            //noinspection deprecation
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Log.e(TAG, "position = " + position);

            ConnectThread connectThread = new ConnectThread(connectHandler);
            connectThread.run();
        }
    };

    private void scanLeDevice() {
        mHandler.postDelayed(() -> {
            //noinspection deprecation
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }, SCAN_PERIOD);
        //noinspection deprecation
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            (device, rssi, scanRecord) -> runOnUiThread(() -> runOnUiThread(() -> addDevice(device)));

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
            list_device.setVisibility(View.VISIBLE);
            deviceAdapter.notifyDataSetChanged();
        }
    }

    private void Remote_connec() {
        Value.BID = device.getAddress();
        Value.BName = device.getName();
        Log.e(TAG, "BID = " + Value.BID);
        Intent gattServiceIntent = new Intent(DeviceList.this, BluetoothLeService.class);
        s_connect = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        if (s_connect) {
                progressDialog = writeDialog(DeviceList.this, getString(R.string.connecting));
            if(!progressDialog.isShowing()) {
                Log.e(TAG,"Dialog");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            }
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        } else {
            Log.e(TAG, "服務綁訂狀態  = " + false);
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.e(TAG, "連線中");
            //https://github.com/googlesamples/android-BluetoothLeGatt/tree/master/Application/src/main/java/com/example/android/bluetoothlegatt
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "初始化失敗");
            }
            mBluetoothLeService.connect(Value.BID);
            Log.e(TAG, "進入連線");
        }

        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            Log.e(TAG, "失去連線端");
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
                if (mBluetoothLeService != null)
                    unbindService(mServiceConnection);
                Log.e(TAG, "連線中斷" + Value.connected);
                //Toast.makeText(DeviceList.this, getString(R.string.connect_err), Toast.LENGTH_SHORT).show();
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Log.e(TAG,"Dialog.dismiss");
                }
                if (Value.connected) {
                    try {
                        //new Thread(connectfail).start();
                        Service_close();
                        sleep(2000);
                        ConnectThread newThread = new ConnectThread(connectHandler);
                        newThread.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                //displayGattServices(mBluetoothLeService.getSupportedGattServices());
                Log.e(TAG, "連線狀態改變");
                mBluetoothLeService.enableTXNotification();
                if (!Value.connected)
                    new Thread(sendcheck).start();
                else {
                    DataSave.clear();
                    return_RX.clear();
                    SelectItem.clear();
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendValue = new SendValue(mBluetoothLeService);
                    login();
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                runOnUiThread(() -> {
                    try {
                        txValue = intents.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                        text = new String(txValue, "UTF-8");
                        Log.e(TAG, "text = " + text);
                        if (text.startsWith("OK")) {
                            DataSave.clear();
                            return_RX.clear();
                            SelectItem.clear();
                            Jsonlist.clear();
                            new Thread(checkmodel).start();
                            for (; !Value.modelList; ) {
                                sleep(100);
                            }
                            new Thread(sendpassword).start();
                            /*sendLog = new SendLog();
                            sendLog.set_over(true);
                            sendLog.set_Service(mBluetoothLeService);*/
                        } else if (text.startsWith("BT")) {
                            Jsonlist.clear();
                            Value.model = true;
                            Value.deviceModel = text;
                            modelJSON = modelSQL.getJSON(text);
                            for (int i = 0; i < modelJSON.length(); i++) {
                                Jsonlist.add(modelJSON.get(i).toString());
                            }
                            Value.Jsonlist = Jsonlist;
                            Log.e(TAG, "Jsonlist = " + Jsonlist);
                        } else if (text.startsWith("ENGE")) {
                            Value.E_word = text.substring(4, text.length());
                            Log.e(TAG, "管理者密碼 = " + Value.E_word);
                        } else if (text.startsWith("PASS")) {
                            Value.P_word = text.substring(4, text.length());
                            Log.e(TAG, "客戶密碼 = " + Value.P_word);
                        } else if (text.startsWith("INIT")) {
                            Value.I_word = text.substring(4, text.length());
                            Log.e(TAG, "初始化密碼 = " + Value.I_word);
                        } else if (text.startsWith("Delay")) {
                            Log.e(TAG, "Delay時間 = " + text);
                        } else if (text.startsWith("GUES")) {
                            Value.G_word = text.substring(4, text.length());
                            Log.e(TAG, "訪客密碼 = " + Value.G_word);
                            Value.connected = true;
                            flag = 1;
                            check();
                        } else {
                            if (!text.startsWith("OVER")) {
                                if (!(text.startsWith("COUNT") || text.startsWith("DATE") ||
                                        text.startsWith("TIME") || text.matches("LOGON") ||
                                        text.matches("LOGOFF") || text.startsWith("LOG"))) {
                                    Log.e(TAG, "check = " + Value.Jsonlist.get(check));
                                    SelectItem.add(checkDeviceName.setName(text));
                                    return_RX.add(text);
                                    DataSave.add(text);
                                    check = check + 1;
                                } else if((text.startsWith("COUNT") || text.startsWith("DATE") ||
                                        text.startsWith("TIME") || text.matches("LOGON") ||
                                        text.matches("LOGOFF"))){
                                    setString.set(text, check);
                                    check = check + 1;
                                    Log.e(TAG, "check = " + Value.Jsonlist.get(check));
                                }else {
                                    Log.e(TAG, "Loging = " + text);
                                }
                            } else if (text.matches("OVER") && !text.startsWith("LOG")) {
                                //check = check + 1;
                                Log.e(TAG, "checkOVER = " + text);
                                Log.e(TAG, "check = " + check);
                                Log.e(TAG, "RX = " + return_RX);
                                Log.e(TAG, "SelectItem = " + SelectItem);
                                Log.e(TAG, "型號 = " + Value.deviceModel);
                                if (Value.Jsonlist.get(check).matches("OVER")) {
                                    Value.SelectItem = SelectItem;
                                    Value.DataSave = DataSave;
                                    Value.return_RX = return_RX;
                                    //sendLog.interrupt();
                                    Log.e(TAG,"Dialog.dismiss");
                                    Log.e(TAG,"Dialog.dismiss2");
                                    device_function();
                                }
                            } else {
                                Log.e(TAG, "Loging = " + text);
                            }
                        }
                    } catch (UnsupportedEncodingException | InterruptedException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    };

    private Runnable checkmodel = new Runnable() {
        @Override
        public void run() {
            try {
                sleep(200);
                if (!Value.model) {
                    for (int i = 1; i < modelJSON.length(); i++) {
                        Jsonlist.add(modelJSON.get(i).toString());
                    }
                    Value.Jsonlist = Jsonlist;
                    Value.deviceModel = modelJSON.get(0).toString();
                }
                Value.modelList = true;
                Log.e(TAG, "Jsonlist = " + Jsonlist);
                Log.e(TAG, "modelList = " + Value.modelList);
            } catch (InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //private Runnable connectfail = () -> unbindService(mServiceConnection);

    private Runnable sendcheck = () -> {
        try {
            sleep(500);
            Log.e(TAG, "sends = " + Jetec);
            if (s_connect) {
                //Value.bluetoothLeService = mBluetoothLeService;
                sendValue = new SendValue(mBluetoothLeService);
                sendValue.send(Jetec);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    private Runnable sendpassword = () -> {
        try {
            sleep(100);
            Log.e(TAG, "log delay時間");
            sendValue.send("Delay00015");
            sleep(100);
            Log.e(TAG, "管理者密碼確認");
            sendValue.send("ENGEWD");  //ENGEWD = 管理者密碼確認
            sleep(100);
            Log.e(TAG, "客戶密碼確認");
            sendValue.send("PASSWD");  //PASSWD = 客戶密碼確認(只有此密碼可以修改)
            sleep(100);
            Log.e(TAG, "初始化密碼確認");
            sendValue.send("INITWD");
            sleep(100);
            Log.e(TAG, "訪客密碼確認");
            sendValue.send("GUESWD");  //GUESWD = 訪客密碼確認
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    private void login() {
        if(progressDialog2 != null && progressDialog2.isShowing()){
            Log.e(TAG,"Dialog.dismiss2");
            progressDialog2.dismiss();
        }
        check = 0;
        SelectItem.add("NAME");
        DataSave.add(device.getName());
        sendValue.send("get");
        progressDialog2 = writeDialog(DeviceList.this, getString(R.string.login));
        if(!progressDialog2.isShowing()) {
            Log.e(TAG,"Dialog2");
            progressDialog2.show();
            progressDialog2.setCanceledOnTouchOutside(false);
        }
        new Thread(timedelay).start();
    }

    private Runnable timedelay = new Runnable() {
        @Override
        public void run() {
            try {
                sleep(2000);
                Log.e(TAG, "check = " + check);
                Log.e(TAG, "getitem = " + Value.Jsonlist.get(check));
                //noinspection StatementWithEmptyBody
                if (Value.Jsonlist.get(check).matches("OVER")) {
                } else {
                    //noinspection deprecation
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Service_close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private Dialog writeDialog(Context context, String message) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.running, null);
        LinearLayout layout = v.findViewById(R.id.ll_dialog);
        ProgressBar pb_progress_bar = v.findViewById(R.id.pb_progress_bar);
        pb_progress_bar.setVisibility(View.VISIBLE);
        TextView tv = v.findViewById(R.id.tv_loading);

        if (message == null || message.equals("")) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setText(message);
            tv.setTextColor(context.getResources().getColor(R.color.colorDialog));
        }

        progressDialog.setContentView(layout, new LinearLayout.LayoutParams((int) (Value.all_Width / 2),
                (int) (Value.all_Height / 5)));

        return progressDialog;
    }

    @SuppressLint("SetTextI18n")
    private void check() throws InterruptedException {

        setContentView(R.layout.checkpassword);

        Button by = findViewById(R.id.button2);
        Button bn = findViewById(R.id.button1);
        TextView t1 = findViewById(R.id.textView3);
        EditText e1 = findViewById(R.id.editText1);

        Log.e(TAG,"Dialog.dismiss");
        progressDialog.dismiss();
        sleep(30);

        t1.setText(getString(R.string.device_name) + "： " + device.getName());
        e1.setKeyListener(DigitsKeyListener.getInstance(".,$%&^!()-_=+';:|}{[]*→←↘↖、，。?~～#€￠" +
                "￡￥abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@>/<"));
        //e1.setKeyListener(DigitsKeyListener.getInstance("abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789."));
        e1.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        by.setOnClickListener(v -> {
            vibrator.vibrate(100);
            if (e1.getText().toString().length() == 6) {
                if (e1.getText().toString().trim().matches(Value.E_word)) {
                    Value.passwordFlag = 1;
                    Log.e(TAG, "管理者 登入");
                    login();
                } else if (e1.getText().toString().trim().matches(Value.P_word)) {
                    Value.passwordFlag = 2;
                    Log.e(TAG, "客戶 登入");
                    login();
                } else if (e1.getText().toString().trim().matches(Value.I_word)) {
                    Toast.makeText(DeviceList.this, getString(R.string.initialization), Toast.LENGTH_SHORT).show();
                    initialization = new Initialization(Value.deviceModel, mBluetoothLeService);
                    try {
                        initialization.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Value.passwordFlag = 3;
                    Log.e(TAG, "初始化 原廠設定");
                    login();
                } else if (e1.getText().toString().trim().matches(Value.G_word)) {
                    Value.passwordFlag = 4;
                    Log.e(TAG, "訪客 登入");
                    login();
                } else {
                    Toast.makeText(DeviceList.this, getString(R.string.passworderror), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DeviceList.this, getString(R.string.inputerror), Toast.LENGTH_SHORT).show();
            }
        });

        bn.setOnClickListener(v -> {
            vibrator.vibrate(100);
            if (mBluetoothAdapter != null && mBluetoothLeService != null) {
                //noinspection deprecation
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                Value.connected = false;
                Service_close();
                if (s_connect) {
                    s_connect = false;
                }
            }
            show_device();
        });
    }

    private void device_function() {

        Intent intent = new Intent(DeviceList.this, DeviceFunction.class);

        startActivity(intent);
        progressDialog.dismiss();
        progressDialog2.dismiss();
        finish();
    }

    private void Service_close() {
        if (mBluetoothLeService == null) {
            Log.e(TAG, "service close!");
            return;
        }
        mBluetoothLeService.disconnect();
    }

    public static BluetoothManager getManager(Context context) {    //獲取此設備默認藍芽適配器
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    private void backtofirst() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK:
                if (flag == 0) {
                    vibrator.vibrate(100);
                    backtofirst();
                } else if (flag == 1) {
                    vibrator.vibrate(100);
                    if (mBluetoothAdapter != null)
                        //noinspection deprecation
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
            if (s_connect) {
                unbindService(mServiceConnection);
                s_connect = false;
            }
            mBluetoothLeService.stopSelf();
            mBluetoothLeService = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (s_connect) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            if (mBluetoothLeService != null) {
                final boolean result = mBluetoothLeService.connect(Value.BID);
                Log.d(TAG, "Connect request result=" + result);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (s_connect)
            unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "flag = " + flag);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
            if (flag == 0) {
                show_device();
            } else if (flag == 1) {
                try {
                    check();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            if (flag == 0) {
                show_device();
            } else if (flag == 1) {
                try {
                    check();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
