package com.jetec.nordic_googleplay.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jetec.nordic_googleplay.CheckDeviceName;
import com.jetec.nordic_googleplay.GetDeviceName;
import com.jetec.nordic_googleplay.GetDeviceNum;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import com.jetec.nordic_googleplay.Value;

import java.util.ArrayList;
import java.util.Objects;

import static com.jetec.nordic_googleplay.Activity.DeviceList.getManager;
import static java.lang.Thread.sleep;

public class Engineer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Vibrator vibrator;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private boolean s_connect = false;
    private String TAG = "Engineer";
    private Intent intents;
    private ArrayList<String> List_d_function, List_d_num;
    private CheckDeviceName checkDeviceName;
    private GetDeviceName getDeviceName;
    private GetDeviceNum getDeviceNum;
    private Dialog progressDialog2 = null;
    private float MaxEH1, MinEL1, MaxEH2, MinEL2, MaxEH3, MinEL3, MaxIH1, MinIL1, MaxIH2, MinIL2, MaxIH3, MinIL3;
    private NavigationView navigationView;

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
        ConfigurationChange();
    }

    private void getW_H() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Value.all_Width = dm.widthPixels;
        Value.all_Height = dm.heightPixels;
        Log.e(TAG, "height : " + Value.all_Height + "dp" + " " + " width : " + Value.all_Width + "dp");
    }

    private void all_list() {
        List_d_function = new ArrayList<>();
        List_d_num = new ArrayList<>();
    }

    private void ConfigurationChange() {
        getW_H();
        all_list();
        get_intent();
    }

    private void get_intent() {

        setContentView(R.layout.engineer_function);

        checkDeviceName = new CheckDeviceName();
        if (progressDialog2 != null) {
            progressDialog2.dismiss();
        }

        List_d_function.clear();
        List_d_num.clear();

        LinearLayout l_b = findViewById(R.id.linear_button);
        ImageButton b_save = findViewById(R.id.button1);
        ImageButton b_load = findViewById(R.id.button2);
        ImageButton b_download = findViewById(R.id.button3);
        ImageButton b_dialog = findViewById(R.id.button4);
        ImageButton b_re = findViewById(R.id.button5);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ListView listname = findViewById(R.id.list_name_function);

        List_d_function.add(getString(R.string.device_name));
        List_d_num.add(Value.BName);

        getDeviceName = new GetDeviceName(this, Value.deviceModel);
        getDeviceNum = new GetDeviceNum();

        for (int i = 0; i < Value.return_RX.size(); i++) {
            List_d_function.add(getDeviceName.get(Value.return_RX.get(i)));
            List_d_num.add(getDeviceNum.get(Value.return_RX.get(i)));
        }

        Log.e(TAG, "DP1 = " + Value.IDP1);
        Log.e(TAG, "DP2 = " + Value.IDP2);
        Log.e(TAG, "DP3 = " + Value.IDP3);

        getM();

        Value.btn = Value.name.indexOf('L') != -1;

        if (Value.passwordFlag == 1) {
            DrawerLayout(myToolbar);
            //engin.setVisibility(View.VISIBLE);
            //header.setVisibility(View.GONE);
            //button.setVisibility(View.GONE);
            b_save.setVisibility(View.VISIBLE);
            b_load.setVisibility(View.VISIBLE);
            b_download.setVisibility(View.VISIBLE);
            b_dialog.setVisibility(View.VISIBLE);
            b_re.setVisibility(View.VISIBLE);
            l_b.setVisibility(View.VISIBLE);
        }
    }

    private void DrawerLayout(Toolbar myToolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View v = layoutInflater.inflate(R.layout.test, null);
        ConstraintLayout nav = v.findViewById(R.id.nav);

        nav.setMinHeight((int)Value.all_Height);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        //setContentView(R.layout.device_function);
        //int id = item.getItemId();
        vibrator.vibrate(100);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            case KeyEvent.KEYCODE_BACK:
                vibrator.vibrate(100);
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            if (mBluetoothAdapter != null)
                //noinspection deprecation
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Service_close();
            Value.connected = false;
            disconnect();
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

    @SuppressLint("HandlerLeak")
    private Handler connectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Remote_connec();
        }
    };

    private void getM() {
        Log.e(TAG, "List_d_num = " + List_d_num);
        if (Value.SelectItem.indexOf("EH1") != -1) {
            MaxEH1 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH1")));
            Log.e(TAG, "MaxEH1 = " + MaxEH1);
        }
        if (Value.SelectItem.indexOf("EL1") != -1) {
            MinEL1 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL1")));
            Log.e(TAG, "MinEL1 = " + MinEL1);
        }
        if (Value.SelectItem.indexOf("EH2") != -1) {
            MaxEH2 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH2")));
            Log.e(TAG, "MaxEH2 = " + MaxEH2);
        }
        if (Value.SelectItem.indexOf("EL2") != -1) {
            MinEL2 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL2")));
            Log.e(TAG, "MinEL2 = " + MinEL2);
        }
        if (Value.SelectItem.indexOf("EH3") != -1) {
            MaxEH3 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH3")));
            Log.e(TAG, "MaxEH3 = " + MaxEH3);
        }
        if (Value.SelectItem.indexOf("EL3") != -1) {
            MinEL3 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL3")));
            Log.e(TAG, "MinEL3 = " + MinEL3);
        }
        if (Value.SelectItem.indexOf("IH1") != -1) {
            MaxIH1 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("IH1")));
            Log.e(TAG, "MaxIH1 = " + MaxIH1);
        }
        if (Value.SelectItem.indexOf("IL1") != -1) {
            MinIL1 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("IL1")));
            Log.e(TAG, "MinIL1 = " + MinIL1);
        }
        if (Value.SelectItem.indexOf("IH2") != -1) {
            MaxIH2 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("IH2")));
            Log.e(TAG, "MaxIH2 = " + MaxIH2);
        }
        if (Value.SelectItem.indexOf("IL2") != -1) {
            MinIL2 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("IL2")));
            Log.e(TAG, "MinIL2 = " + MinIL2);
        }
        if (Value.SelectItem.indexOf("IH3") != -1) {
            MaxIH3 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("IH3")));
            Log.e(TAG, "MaxIH3 = " + MaxIH3);
        }
        if (Value.SelectItem.indexOf("IL3") != -1) {
            MinIL3 = Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("IL3")));
            Log.e(TAG, "MinIL3 = " + MinIL3);
        }
    }

    private void Remote_connec() {
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        s_connect = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.e(TAG, "s_connect = " + s_connect);
        if (s_connect) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        } else {
            Log.e(TAG, "服務綁訂狀態  = " + false);
        }
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
                s_connect = false;
                Log.e(TAG, "連線中斷" + Value.connected);

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                //displayGattServices(mBluetoothLeService.getSupportedGattServices());
                Log.e(TAG, "連線狀態改變");
                mBluetoothLeService.enableTXNotification();

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                runOnUiThread(() -> {

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
