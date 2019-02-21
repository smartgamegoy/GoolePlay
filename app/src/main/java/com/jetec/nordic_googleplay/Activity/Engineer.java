package com.jetec.nordic_googleplay.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jetec.nordic_googleplay.DialogFunction.ENGIN;
import com.jetec.nordic_googleplay.Initialization;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.SendValue;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import com.jetec.nordic_googleplay.Value;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

import static com.jetec.nordic_googleplay.Activity.DeviceList.getManager;
import static java.lang.Thread.sleep;

public class Engineer extends AppCompatActivity {

    private Vibrator vibrator;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private boolean s_connect = false;
    private String TAG = "Engineer";
    private SendValue sendValue;
    private Intent intents;
    private ListView list1;
    private ArrayAdapter<String> listAdapter;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        BluetoothManager bluetoothManager = getManager(this);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothLeService == null) {
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            s_connect = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
            if (s_connect)
                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            else
                Log.e(TAG, "連線失敗");
        }
        get_intent();
    }

    private void get_intent() {

        setContentView(R.layout.engineer_title);

        flag = 0;

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        ENGIN engin = new ENGIN(this);
        list1 = findViewById(R.id.listMessage);
        EditText e1 = findViewById(R.id.sendText);
        Button b1 = findViewById(R.id.sendButton);
        Button fix = findViewById(R.id.button);
        Button SP1 = findViewById(R.id.button2);
        Button ER1 = findViewById(R.id.button3);
        Button SV1 = findViewById(R.id.button4);
        Button GET = findViewById(R.id.button5);
        Button SP2 = findViewById(R.id.button6);
        Button ER2 = findViewById(R.id.button7);
        Button SV2 = findViewById(R.id.button8);
        Button init = findViewById(R.id.button9);
        Button SP3 = findViewById(R.id.button10);
        Button ER3 = findViewById(R.id.button11);
        Button SV3 = findViewById(R.id.button12);

        listAdapter = new ArrayAdapter<>(this, R.layout.message_detail);
        list1.setAdapter(listAdapter);
        list1.setDivider(null);

        b1.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        fix.setOnClickListener(v -> {
            vibrator.vibrate(100);
            try {
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                sendValue = new SendValue(mBluetoothLeService);
                sendValue.send("FIX");
                sleep(100);
                listAdapter.add("[" + currentDateTimeString + "] send: " + "FIX");
                list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                e1.setText("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        SP1.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "SP1", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ER1.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "ER1", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        SV1.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "SV1", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        GET.setOnClickListener(v -> {
            vibrator.vibrate(100);
            try {
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                sendValue = new SendValue(mBluetoothLeService);
                sendValue.send("get");
                sleep(100);
                listAdapter.add("[" + currentDateTimeString + "] send: " + "get");
                list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                e1.setText("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        SP2.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "SP2", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ER2.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "ER2", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        SV2.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "SV2", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        init.setOnClickListener(v -> {
            vibrator.vibrate(100);
            sendValue = new SendValue(mBluetoothLeService);
            int i = 0;
            if (Value.deviceModel.substring(3, 4).matches("1")) {
                i = 1;    //幾排
            } else if (Value.deviceModel.substring(3, 4).matches("2")) {
                i = 2;
            } else if (Value.deviceModel.substring(3, 4).matches("3")) {
                i = 3;
            }

            String sp, er, sv;

            for (int j = 0; j < i; j++) {
                try {
                    sp = "SP" + i + "+1250.0";
                    sendValue.send(sp);
                    sleep(100);
                    er = "ER" + i + "+1000.0";
                    sendValue.send(er);
                    sleep(100);
                    sv = "SV" + i + "+0000.0";
                    sendValue.send(sv);
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        SP3.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "SP3", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ER3.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "ER3", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        SV3.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String message = e1.getText().toString().trim();
            if (!message.matches("")) {
                try {
                    message = engin.todo(Float.valueOf(message), "SV3", message);
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    sendValue = new SendValue(mBluetoothLeService);
                    sendValue.send(message);
                    sleep(100);
                    listAdapter.add("[" + currentDateTimeString + "] send: " + message);
                    list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    e1.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
    protected void onPause() {
        super.onPause();
        if (s_connect)
            unregisterReceiver(mGattUpdateReceiver);
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
    protected void onStop() {
        super.onStop();
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                vibrator.vibrate(100);
                new AlertDialog.Builder(Engineer.this)
                        .setTitle("結束連線")
                        .setMessage("斷開藍牙")
                        .setPositiveButton(R.string.butoon_yes, (dialog, which) -> {
                            vibrator.vibrate(100);
                            if (mBluetoothAdapter != null)
                                //noinspection deprecation
                                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            Service_close();
                            Value.connected = false;
                            disconnect();
                        })
                        .setNeutralButton(R.string.butoon_no, (dialog, which) -> {
                        })
                        .show();
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.engin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //toolbar menu item
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            vibrator.vibrate(100);
            Intent intent = new Intent(Engineer.this, DeviceEngineer.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.reset) {
            vibrator.vibrate(100);
            Initialization initialization = new Initialization(Value.deviceModel, mBluetoothLeService);
            try {
                initialization.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        return true;
    }

    public void Service_close() {
        if (mBluetoothLeService == null) {
            Log.e(TAG, "masaga");
            return;
        }
        mBluetoothLeService.disconnect();
    }

    private void disconnect() {
        Intent intent = new Intent(this, MainActivity.class);
        //Value.Jsonlist.clear();
        startActivity(intent);
        finish();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            (device, rssi, scanRecord) -> runOnUiThread(() -> runOnUiThread(this::addDevice));

    private void addDevice() {
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
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

    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intents = intent;
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.e(TAG, "連線成功");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                //s_connect = false;
                Log.e(TAG, "連線中斷" + Value.connected);
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                listAdapter.add("[" + currentDateTimeString + "] 連線中斷: " + "自動重連中...");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                //displayGattServices(mBluetoothLeService.getSupportedGattServices());
                Log.e(TAG, "連線狀態改變");
                mBluetoothLeService.enableTXNotification();
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                listAdapter.add("[" + currentDateTimeString + "] 連線狀態: " + "已重新連線");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                runOnUiThread(() -> {
                    try {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        byte[] txValue = intents.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                        String text = new String(txValue, "UTF-8");
                        listAdapter.add("[" + currentDateTimeString + "] RE: " + text);
                        list1.smoothScrollToPosition(listAdapter.getCount() - 1);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
            }
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
}
