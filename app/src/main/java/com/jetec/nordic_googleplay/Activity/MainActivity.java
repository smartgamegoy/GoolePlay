package com.jetec.nordic_googleplay.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;
import com.jetec.nordic_googleplay.Firstpage_buttonstyle;
import com.jetec.nordic_googleplay.SQL.ModelSQL;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Value;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private static final int REQUEST_ENABLE_BT = 1;

    private Vibrator vibrator;
    private BluetoothAdapter mBluetoothAdapter;
    private ModelSQL modelSQL;
    private String TAG = "MainActivity";

    private static final String[] default_model = {"BT-2-THD", "PV1", "PV2", "EH1", "EL1", "EH2",
            "EL2", "CR1", "CR2", "ADR", "OVER"};
    //↑第一台功能設定
    /*private static final String[] BT_2_IIL = {"BT-2-IIL", "IH1", "IL1", "IH2", "IL2", "PV1", "PV2",
            "EH1", "EL1", "EH2", "EL2", "CR1", "CR2", "SPK", "DP1", "DP2", "COUNT", "INTER", "DATE",
            "TIME", "LOG", "OVER"};
    private static final String[] BT_2_II = {"BT-2-II", "IH1", "IL1", "IH2", "IL2", "PV1", "PV2",
            "EH1", "EL1", "EH2", "EL2", "CR1", "CR2", "SPK", "DP1", "DP2", "OVER"};
    private static final String[] BT_2_TH = {"BT-2-TH", "PV1", "PV2", "EH1", "EL1", "EH2", "EL2",
            "CR1", "CR2", "SPK", "OVER"};
    private static final String[] BT_1_I = {"BT-1-I", "IH1", "IL1", "PV1", "EH1", "EL1", "CR1",
            "SPK", "DP1", "OVER"};
    private static final String[] BT_3_THC = {"BT-3-THC", "PV1", "PV2", "PV3", "EH1", "EL1", "EH2",
            "EL2", "EH3", "EL3", "CR1", "CR2", "CR3", "SPK", "OVER"};
    private static final String[] BT_3_THD = {"BT-3-THD", "PV1", "PV2", "PV3", "EH1", "EL1", "EH2",
            "EL2", "EH3", "EL3", "CR1", "CR2", "CR3", "SPK", "OVER"};
    private static final String[] BT_3_THE = {"BT-3-THE", "PV1", "PV2", "PV3", "EH1", "EL1", "EH2",
            "EL2", "EH3", "EL3", "CR1", "CR2", "CR3", "SPK", "OVER"};
    private static final String[] BT_2_TY = {"BT-2-TY", "PV1", "EH1", "EL1", "CR1", "SPK", "OVER"};
    private static final String[] BT_3_THY = {"BT-3-THY", "PV1", "PV2", "EH1", "EL1", "EH2", "EL2",
            "CR1", "CR2", "SPK", "OVER"};*/
    private String[][] All_model = {default_model/*, BT_2_IIL, BT_2_II, BT_2_TH,
            BT_1_I, BT_3_THC, BT_3_THD, BT_3_THE, BT_2_TY, BT_3_THY*/};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelSQL = new ModelSQL(this);
        modelSQL.deleteAll();
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        getW_H();
        String phone = android.os.Build.BRAND;    //手機廠商
        Value.phonename = phone;
        Log.e(TAG,"phone = " + phone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //未取得權限，向使用者要求允許權限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            } else {    //已有權限，可進行檔案存取
                if(modelSQL.getCount() == 0){
                    new Thread(getmodel).start();
                    meun_click();
                }
                else {
                    meun_click();
                }
            }
        }
        else {
            if(modelSQL.getCount() == 0){
                new Thread(getmodel).start();
                meun_click();
            }
            else {
                meun_click();
            }
        }
    }

    private void getW_H(){
        modelSQL = new ModelSQL(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Value.all_Width = dm.widthPixels;
        Value.all_Height = dm.heightPixels;
        Log.e(TAG, "height : " + Value.all_Height + "dp" + " " + " width : " + Value.all_Width + "dp");
    }

    private void meun_click() {
        setContentView(R.layout.firstpage);

        Button btn = findViewById(R.id.button);
        Firstpage_buttonstyle firstpage_buttonstyle = new Firstpage_buttonstyle(this,
                Value.all_Width, Value.all_Height);
        firstpage_buttonstyle.buttonstyle(btn);

        btn.setOnClickListener(v -> {
            vibrator.vibrate(100);
            scan_ble();
        });
    }

    private void scan_ble(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isLocationEnable(MainActivity.this))  {
                final AlertDialog.Builder show_mess = new AlertDialog.Builder(MainActivity.this);
                final AlertDialog alertDialog = show_mess.show();
                show_mess.setTitle(getString(R.string.mes_title));
                show_mess.setMessage(getString(R.string.mes_mess));
                show_mess.setPositiveButton(getString(R.string.mes_yes), (dialog, which) -> {
                    Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
                    alertDialog.dismiss();
                });
                show_mess.setNegativeButton(getString(R.string.mes_no), (dialog, which) -> alertDialog.dismiss());
                show_mess.show();
            }
            else {
                BluetoothManager bluetoothManager = getManager(MainActivity.this);
                if (bluetoothManager != null) {
                    mBluetoothAdapter = bluetoothManager.getAdapter();
                }
                if ((mBluetoothAdapter == null) || (!mBluetoothAdapter.isEnabled())) {
                    Toast.makeText(MainActivity.this, getString(R.string.BLE_adp), Toast.LENGTH_SHORT).show();
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                else {
                    Toast.makeText(MainActivity.this, getString(R.string.search_device), Toast.LENGTH_SHORT).show();
                    show_device();
                }
            }
        }
        else {
            BluetoothManager bluetoothManager = getManager(MainActivity.this);
            if (bluetoothManager != null) {
                mBluetoothAdapter = bluetoothManager.getAdapter();
            }
            if ((mBluetoothAdapter == null) || (!mBluetoothAdapter.isEnabled())) {
                Toast.makeText(MainActivity.this, getString(R.string.BLE_adp), Toast.LENGTH_SHORT).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else {
                Toast.makeText(MainActivity.this, getString(R.string.search_device), Toast.LENGTH_LONG).show();
                show_device();
            }
        }
    }

    private void show_device(){
        Intent intent = new Intent(MainActivity.this, DeviceList.class);
        intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intent.putExtra("default_model", default_model);
        startActivity(intent);
        modelSQL.close();
        finish();
    }

    private BluetoothManager getManager(Context context) {    //獲取此設備默認藍芽適配器
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    private boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return networkProvider || gpsProvider;
    }

    private void AddJsonSQL(String[] set_array){
        String modelname = "";
        ArrayList<String> list = new ArrayList<>();
        list.clear();
        for(int i = 0; i < set_array.length; i++){
            if(i == 0){
                modelname = set_array[i];
            }
            else {
                if(set_array[i].matches("OVER")){
                    list.add(set_array[i]);
                    JSONArray json = new JSONArray(list);
                    modelSQL.insert(modelname, json);
                    list.clear();
                }
                else {
                    list.add(set_array[i]);
                }
            }
        }
    }

    private Runnable getmodel = () -> {
        for (String[] aAll_model : All_model) {
            AddJsonSQL(aAll_model);
        }
    };

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                vibrator.vibrate(100);
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_name)
                        .setIcon(R.drawable.icon)
                        .setMessage(R.string.app_message)
                        .setPositiveButton(R.string.app_message_b1, (dialog, which) -> finish())
                        .setNegativeButton(R.string.app_message_b2, (dialog, which) -> {
                            // TODO Auto-generated method stub
                        }).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_CODE_ACCESS_COARSE_LOCATION:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(modelSQL.getCount() == 0){
                        new Thread(getmodel).start();
                        meun_click();
                    }
                    else {
                        meun_click();
                    }
                    //取得聯絡人權限，進行工作
                } else {
                    finish();
                    //使用者拒絕權限，顯示對話框告知
                }
            }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
            setContentView(R.layout.logview);
            getW_H();
            meun_click();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            setContentView(R.layout.logview);
            getW_H();
            meun_click();
        }
    }
}
