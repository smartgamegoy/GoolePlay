package com.jetec.nordic_googleplay.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.jetec.nordic_googleplay.R;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private static final int REQUEST_ENABLE_BT = 1;

    private Vibrator vibrator;
    private BluetoothAdapter mBluetoothAdapter;

    private String TAG = "MainActivity";
    private double all_Width, all_Height;
    private int jsonflag, send, connect_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        all_Width = dm.widthPixels;
        all_Height = dm.heightPixels;
        Log.e(TAG, "height : " + all_Height + "dp" + " " + " width : " + all_Width + "dp");
        String phone = Build.BRAND;    //手機廠商
        Log.e(TAG,"phone = " + phone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //未取得權限，向使用者要求允許權限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            } else {
                meun_click();
                //已有權限，可進行檔案存取
            }
        }
        else {
            meun_click();
        }
    }

    private void meun_click() {
        setContentView(R.layout.menu_main);

        jsonflag = 0;

        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send = 0;
                connect_flag = 1;
                vibrator.vibrate(100);
                scan_ble();
            }
        });
    }

    private void scan_ble(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isLocationEnable(MainActivity.this))  {
                final AlertDialog.Builder show_mess = new AlertDialog.Builder(MainActivity.this);
                final AlertDialog alertDialog = show_mess.show();
                show_mess.setTitle(getString(R.string.mes_title));
                show_mess.setMessage(getString(R.string.mes_mess));
                show_mess.setPositiveButton(getString(R.string.mes_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
                        alertDialog.dismiss();
                    }
                });
                show_mess.setNegativeButton(getString(R.string.mes_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
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
                    return;
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
                return;
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
        intent.putExtra("all_Height", all_Height);
        intent.putExtra("all_Width", all_Width);
        intent.putExtra("send", send);
        intent.putExtra("jsonflag", jsonflag);
        intent.putExtra("connect_flag", connect_flag);

        startActivity(intent);
        finish();
    }

    private boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider)
            return true;
        return false;
    }

    private BluetoothManager getManager(Context context) {    //獲取此設備默認藍芽適配器
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

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
                        .setPositiveButton(R.string.app_message_b1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.app_message_b2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
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
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case REQUEST_CODE_ACCESS_COARSE_LOCATION:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    meun_click();
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
}