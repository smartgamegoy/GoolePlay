package com.jetec.nordic_googleplay.Activity;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.jetec.nordic_googleplay.Dialog.ModifyPassword;
import com.jetec.nordic_googleplay.EditManagert.EditChangeName;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.SQL.JsonSQL;
import com.jetec.nordic_googleplay.SQL.SQLData;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import com.jetec.nordic_googleplay.ViewAdapter.DataList;
import com.jetec.nordic_googleplay.ViewAdapter.DeviceAdapter;
import com.jetec.nordic_googleplay.ViewAdapter.Function;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.jetec.nordic_googleplay.Activity.DeviceList.getManager;
import static java.lang.Thread.sleep;

public class DeviceFunction extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> listData;
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private float Max1 = 65, Min1 = -10, Max2 = 99, Min2 = 0;
    private ModifyPassword modifyPassword;
    private String BID, TAG = "devicefunction", text, log = "log", set, set2, showtext, getdate, getcount,
            gettime, Jetec = "Jetec", getvalue, temperature, humidity, E_word, P_word, G_word, device_name,
            gettoast1, gettoast2, gettoast3, gettoast4, gettoast5, gettoast6, gettoast7, switch_p, get_fun = "get";
    private boolean s_connect = false, setdpp = false, c = false;
    private int connect_flag, send, check, test = 0, totle, jsonflag, select_item = -1;
    private Dialog progressDialog = null, progressDialog2 = null, ask_Dialog = null, checkpassword = null,
            saveDialog = null, showDialog = null, chDialog = null, inDialog = null, upDialog = null;
    private BluetoothLeService mBluetoothLeService;
    private Intent intents;
    private ConnectThread connectThread;
    private byte[] txValue;
    private ArrayList<String> SQLdata, DataSave, return_RX, SelectItem, Logdata, Tlist, Hlist, Clist,
            timelist, charttime, List_d_num, List_d_function;
    private String[] check_arr = {"PV1", "PV2", "EH1", "EL1", "EH2", "EL2", "CR1", "CR2", "ADR"/*, "DPP"*/, "OVER"};
    private Function function;
    private DeviceAdapter deviceAdapter;
    private TextView showing;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> deviceList;
    private View no_device, view1;
    private ListView list_device, list;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private int mState = UART_PROFILE_DISCONNECTED;
    private JSONArray Chart_json, TimeList_json, T_json, H_json;
    private Vibrator vibrator;
    private JsonSQL data_Json;
    private SQLData data_table;
    private double all_Width, all_Height;
    private DataList dataList;
    private IBinder B_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        BluetoothManager bluetoothManager = getManager(this);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if(mBluetoothLeService == null) {
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            s_connect = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
            if(s_connect)
                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            else
                Log.e(TAG,"しっぱいする");
        }
        all_list();
        try {
            get_intent();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void all_list(){
        return_RX = new ArrayList<String>();
        List_d_function = new ArrayList<String>();
        List_d_num = new ArrayList<String>();
        SelectItem = new ArrayList<String>();
        DataSave = new ArrayList<String>();
        SQLdata = new ArrayList<String>();
        Logdata = new ArrayList<String>();
        Tlist = new ArrayList<String>();
        Hlist = new ArrayList<String>();
        timelist = new ArrayList<String>();
        Clist = new ArrayList<String>();
        charttime = new ArrayList<String>();
    }

    private void get_intent() throws InterruptedException, JSONException {

        Intent intent = getIntent();
        device_name = intent.getStringExtra("device_name");
        BID = intent.getStringExtra("device_address");
        E_word = intent.getStringExtra("E_word");
        P_word = intent.getStringExtra("P_word");
        G_word = intent.getStringExtra("G_word");
        all_Width = intent.getDoubleExtra("all_Width", all_Width);
        all_Height = intent.getDoubleExtra("all_Height", all_Height);
        jsonflag = intent.getIntExtra("jsonflag", jsonflag);
        send = intent.getIntExtra("send", send);
        connect_flag = intent.getIntExtra("connect_flag", connect_flag);
        check = intent.getIntExtra("check", check);
        s_connect = intent.getBooleanExtra("s_connect", s_connect);
        setdpp = intent.getBooleanExtra("setdpp", setdpp);
        return_RX = intent.getStringArrayListExtra("return_RX");
        SelectItem = intent.getStringArrayListExtra("SelectItem");
        DataSave = intent.getStringArrayListExtra("DataSave");
        SQLdata = intent.getStringArrayListExtra("SQLdata");
        Logdata = intent.getStringArrayListExtra("Logdata");

        show_device_function();
    }

    private void show_device_function() throws InterruptedException, JSONException {
        setContentView(R.layout.device_function);
        data_table = new SQLData(this);
        data_Json = new JsonSQL(this);

        gettoast1 = getString(R.string.samepassword);
        gettoast2 = getString(R.string.samepassword2);
        gettoast3 = getString(R.string.samepassword3);
        gettoast4 = getString(R.string.success);
        gettoast5 = getString(R.string.originalpassworderror);
        gettoast6 = getString(R.string.inputerror);
        gettoast7 = getString(R.string.sixbytes);
        sleep(30);
        if(progressDialog2 != null) {
            progressDialog2.dismiss();
            sleep(30);
        }
        List_d_function.clear();
        List_d_num.clear();
        double bar = 0;
        connect_flag = 4;
        if(send != 1)
            send = 2;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton b_save = (ImageButton) findViewById(R.id.button1) ;
        ImageButton b_load = (ImageButton) findViewById(R.id.button2) ;
        /*ImageButton b_download = (ImageButton) findViewById(R.id.button3) ;
        ImageButton b_dialog = (ImageButton) findViewById(R.id.button4) ;*/
        ImageButton b_re = (ImageButton) findViewById(R.id.button5) ;

        LinearLayout list_function = (LinearLayout)findViewById(R.id.list_function);
        ListView listname = (ListView) findViewById(R.id.list_name_function);

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            bar = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        Log.e(TAG,"bar_height = " + bar);

        list_function.setPadding(0,(int)bar,0,0);

        List_d_function.add(getString(R.string.device_name));
        List_d_num.add(device_name);

        for(int i = 0; i < return_RX.size(); i++){
            List_d_function.add(getDeviceName(return_RX.get(i)));
            List_d_num.add(getDeviceNum(return_RX.get(i), setdpp));
        }
        Log.e(TAG,"List_d_function = " + List_d_function);
        Log.e(TAG,"string = " + List_d_num);
        Log.e(TAG,"setdpp = " + setdpp);
        Max1 = Float.valueOf(List_d_num.get(3));
        Min1 = Float.valueOf(List_d_num.get(4));
        Max2 = Float.valueOf(List_d_num.get(5));
        Min2 = Float.valueOf(List_d_num.get(6));

        function = new Function(this, List_d_function, List_d_num, SelectItem, all_Width);
        listname.setAdapter(function);
        listname.setOnItemClickListener(mSelectClickListener);

        /*if(data_Json.getCount() > 0){

            String chart, time, t, h;

            ArrayList<String> jsonlist = new ArrayList<String>();
            jsonlist.clear();
            jsonlist = data_Json.getlist(BID);
            for(int i = 0; i < jsonlist.size(); i++){
                switch(i){
                    case 0:{
                        charttime.clear();
                        chart = jsonlist.get(i);
                        JSONArray json = new JSONArray(chart);
                        for(int j = 0; j < json.length(); j++){
                            charttime.add(String.valueOf(json.get(j)));
                            Log.e(TAG,"補給你拉0");
                        }
                    }
                    case 1:{
                        timelist.clear();
                        time = jsonlist.get(i);
                        JSONArray json = new JSONArray(time);
                        for(int j = 0; j < json.length(); j++){
                            timelist.add(String.valueOf(json.get(j)));
                            Log.e(TAG,"補給你拉1");
                        }
                    }
                    break;
                    case 2:{
                        Tlist.clear();
                        t = jsonlist.get(i);
                        JSONArray json = new JSONArray(t);
                        for(int j = 0; j < json.length(); j++){
                            Tlist.add(String.valueOf(json.get(j)));
                            Log.e(TAG,"補給你拉2");
                        }
                    }
                    break;
                    case 3:{
                        Hlist.clear();
                        h = jsonlist.get(i);
                        JSONArray json = new JSONArray(h);
                        for(int j = 0; j < json.length(); j++){
                            Hlist.add(String.valueOf(json.get(j)));
                            Log.e(TAG,"補給你拉3");
                        }
                    }
                }
            }
        }*/
        if(send == 2) {
            b_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    saveDialog = dataDialog(DeviceFunction.this, DataSave);
                    saveDialog.show();
                    saveDialog.setCanceledOnTouchOutside(false);
                }
            });

            b_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    showDialog = loadDialog(DeviceFunction.this, DataSave);
                    showDialog.show();
                    showDialog.setCanceledOnTouchOutside(false);
                }
            });

            /*b_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connect_flag = 3;
                    vibrator.vibrate(100);
                    ask_Dialog = askDialog(DeviceFunction.this, false);
                    ask_Dialog.show();
                    ask_Dialog.setCanceledOnTouchOutside(false);
                }
            });

            b_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    if(timelist.size() == 0) {
                        Log.e(TAG,"charttime.size() = " + timelist.size());
                        Toast.makeText(DeviceFunction.this, getString(R.string.logdata), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        requeststorage();
                    }
                }
            });*/

            b_re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    send = 5;
                    vibrator.vibrate(100);
                    modifyPassword = new ModifyPassword(DeviceFunction.this, all_Width, all_Height, P_word,
                            G_word, E_word, gettoast1, gettoast2, gettoast3, gettoast4, gettoast5, gettoast6, gettoast7, mBluetoothLeService);
                    modifyPassword.modifyDialog();
                }
            });
        }
    }

    private void requeststorage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //未取得權限，向使用者要求允許權限
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        REQUEST_EXTERNAL_STORAGE);
            } else {
                intentlogview();
                //已有權限，可進行工作
            }
        }
        else {
            intentlogview();
        }
    }

    private AdapterView.OnItemClickListener mSelectClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            String select = SelectItem.get(position);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Log.e(TAG,"名稱 = " + List_d_function.get(position) + "輸出 = " + select);
            //String output = switch_dialog(select, List_d_function.get(position));
            if(select.matches("DPP")){
                chDialog = switchDialog(DeviceFunction.this, List_d_function.get(position), List_d_num.get(position));
                chDialog.show();
                chDialog.setCanceledOnTouchOutside(false);
            }
            else {
                inDialog = inputDialog(DeviceFunction.this, List_d_function.get(position), select);
                inDialog.show();
                inDialog.setCanceledOnTouchOutside(false);
            }
        }
    };

    private Dialog inputDialog(final Context context, String position, final String name) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        View v = inflater.inflate(R.layout.alterdialog, null);
        LinearLayout setlinear = (LinearLayout)v.findViewById(R.id.setlinear1);
        TextView title = (TextView)v.findViewById(R.id.textView1);
        Button by = (Button)v.findViewById(R.id.button2);
        Button bn = (Button)v.findViewById(R.id.button1);
        final EditText editText = (EditText)v.findViewById(R.id.editText1);
        title.setText(position);
        by.setText(getString(R.string.butoon_yes));
        bn.setText(getString(R.string.butoon_no));
        by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                if(c) {
                    String gets = editText.getText().toString().trim();
                    switch(name){
                        case  "NAME":{
                            try {
                                String out = "NAME" + gets;
                                sending(out);
                                inDialog.dismiss();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case "PV1":{
                            float t = Float.valueOf(gets);
                            if(t > 5.0 || t < -5.0){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(t == 0.0){
                                    try {
                                        String out = name + "+" + "0000.0";
                                        Log.e(TAG, "out = " + out);
                                        sending(out);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    inDialog.dismiss();
                                }
                                else {
                                    if (gets.startsWith("-")) {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(1, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 0; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "-" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        function.notifyDataSetChanged();
                                        inDialog.dismiss();
                                    } else {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(0, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 1; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "+" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    }
                                }
                            }
                        }
                        break;
                        case "PV2":{
                            float t = Float.valueOf(gets);
                            if(t > 10.0 || t < -10.0){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(t == 0.0){
                                    try {
                                        String out = name + "+" + "0000.0";
                                        Log.e(TAG, "out = " + out);
                                        sending(out);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    inDialog.dismiss();
                                }
                                else {
                                    if (gets.startsWith("-")) {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(1, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 0; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "-" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        function.notifyDataSetChanged();
                                        inDialog.dismiss();
                                    } else {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(0, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 1; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "+" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    }
                                }
                            }
                        }
                        break;
                        case "PV3":{

                        }
                        break;
                        case "EH1":{
                            float t = Float.valueOf(gets);
                            if(t > 65.0 || t < -9.9){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Max1 = t;
                                if(Max1 > Min1) {
                                    if (t == 0.0) {
                                        try {
                                            String out = name + "+" + "0000.0";
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    } else {
                                        if (gets.startsWith("-")) {
                                            gets = String.valueOf(t);
                                            int i = gets.indexOf(".");
                                            Log.e(TAG, "gets = " + gets);
                                            String num1 = gets.substring(1, gets.indexOf("."));
                                            String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                            String set = "0";
                                            for (int j = 0; j < (4 - i); j++)
                                                set = set + "0";
                                            try {
                                                String out = name + "-" + set + num1 + num2;
                                                Log.e(TAG, "out = " + out);
                                                sending(out);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            function.notifyDataSetChanged();
                                            inDialog.dismiss();
                                        } else {
                                            gets = String.valueOf(t);
                                            int i = gets.indexOf(".");
                                            Log.e(TAG, "gets = " + gets);
                                            String num1 = gets.substring(0, gets.indexOf("."));
                                            String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                            String set = "0";
                                            for (int j = 1; j < (4 - i); j++)
                                                set = set + "0";
                                            try {
                                                String out = name + "+" + set + num1 + num2;
                                                Log.e(TAG, "out = " + out);
                                                sending(out);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            inDialog.dismiss();
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(DeviceFunction.this, getString(R.string.MAX), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        break;
                        case "EH2":{
                            float t = Float.valueOf(gets);
                            if(t > 99.0 || t < 0.1){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Max2 = t;
                                if(Max2 > Min2) {
                                    if (t == 0.0) {
                                        try {
                                            String out = name + "+" + "0000.0";
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    } else {
                                        if (gets.startsWith("-")) {
                                            gets = String.valueOf(t);
                                            int i = gets.indexOf(".");
                                            Log.e(TAG, "gets = " + gets);
                                            String num1 = gets.substring(1, gets.indexOf("."));
                                            String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                            String set = "0";
                                            for (int j = 0; j < (4 - i); j++)
                                                set = set + "0";
                                            try {
                                                String out = name + "-" + set + num1 + num2;
                                                Log.e(TAG, "out = " + out);
                                                sending(out);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            function.notifyDataSetChanged();
                                            inDialog.dismiss();
                                        } else {
                                            gets = String.valueOf(t);
                                            int i = gets.indexOf(".");
                                            Log.e(TAG, "gets = " + gets);
                                            String num1 = gets.substring(0, gets.indexOf("."));
                                            String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                            String set = "0";
                                            for (int j = 1; j < (4 - i); j++)
                                                set = set + "0";
                                            try {
                                                String out = name + "+" + set + num1 + num2;
                                                Log.e(TAG, "out = " + out);
                                                sending(out);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            inDialog.dismiss();
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(DeviceFunction.this, getString(R.string.MAX), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        break;
                        case "EH3":{

                        }
                        break;
                        case "EL1":{
                            float t = Float.valueOf(gets);
                            if(t > 64.9 || t < -10.0){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Min1 = t;
                                if(Min1 < Max1) {
                                    if (t == 0.0) {
                                        try {
                                            String out = name + "+" + "0000.0";
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    } else {
                                        if (gets.startsWith("-")) {
                                            gets = String.valueOf(t);
                                            int i = gets.indexOf(".");
                                            Log.e(TAG, "gets = " + gets);
                                            String num1 = gets.substring(1, gets.indexOf("."));
                                            String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                            String set = "0";
                                            for (int j = 0; j < (4 - i); j++)
                                                set = set + "0";
                                            try {
                                                String out = name + "-" + set + num1 + num2;
                                                Log.e(TAG, "out = " + out);
                                                sending(out);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            function.notifyDataSetChanged();
                                            inDialog.dismiss();
                                        } else {
                                            gets = String.valueOf(t);
                                            int i = gets.indexOf(".");
                                            Log.e(TAG, "gets = " + gets);
                                            String num1 = gets.substring(0, gets.indexOf("."));
                                            String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                            String set = "0";
                                            for (int j = 1; j < (4 - i); j++)
                                                set = set + "0";
                                            try {
                                                String out = name + "+" + set + num1 + num2;
                                                Log.e(TAG, "out = " + out);
                                                sending(out);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            inDialog.dismiss();
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(DeviceFunction.this, getString(R.string.MIN), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        break;
                        case "EL2":{
                            float t = Float.valueOf(gets);
                            if(t > 98.9 || t < 0.0){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Min2 = t;
                                if(Min2 < Max2) {
                                    if (t == 0.0) {
                                        try {
                                            String out = name + "+" + "0000.0";
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    } else {
                                        if (gets.startsWith("-")) {
                                            gets = String.valueOf(t);
                                            int i = gets.indexOf(".");
                                            Log.e(TAG, "gets = " + gets);
                                            String num1 = gets.substring(1, gets.indexOf("."));
                                            String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                            String set = "0";
                                            for (int j = 0; j < (4 - i); j++)
                                                set = set + "0";
                                            try {
                                                String out = name + "-" + set + num1 + num2;
                                                Log.e(TAG, "out = " + out);
                                                sending(out);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            function.notifyDataSetChanged();
                                            inDialog.dismiss();
                                        } else {
                                            gets = String.valueOf(t);
                                            int i = gets.indexOf(".");
                                            Log.e(TAG, "gets = " + gets);
                                            String num1 = gets.substring(0, gets.indexOf("."));
                                            String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                            String set = "0";
                                            for (int j = 1; j < (4 - i); j++)
                                                set = set + "0";
                                            try {
                                                String out = name + "+" + set + num1 + num2;
                                                Log.e(TAG, "out = " + out);
                                                sending(out);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            inDialog.dismiss();
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(DeviceFunction.this, getString(R.string.MIN), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        break;
                        case "EL3":{

                        }
                        break;
                        case "CR1":{
                            float t = Float.valueOf(gets);
                            if(t > 65.0 || t < -10.0){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(t == 0.0){
                                    try {
                                        String out = name + "+" + "0000.0";
                                        Log.e(TAG, "out = " + out);
                                        sending(out);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    inDialog.dismiss();
                                }
                                else {
                                    if (gets.startsWith("-")) {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(1, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 0; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "-" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        function.notifyDataSetChanged();
                                        inDialog.dismiss();
                                    } else {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(0, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 1; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "+" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    }
                                }
                            }
                        }
                        break;
                        case "CR2":{
                            float t = Float.valueOf(gets);
                            if(t > 99.0 || t < 0.0){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(t == 0.0){
                                    try {
                                        String out = name + "+" + "0000.0";
                                        Log.e(TAG, "out = " + out);
                                        sending(out);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    inDialog.dismiss();
                                }
                                else {
                                    if (gets.startsWith("-")) {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(1, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 0; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "-" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        function.notifyDataSetChanged();
                                        inDialog.dismiss();
                                    } else {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(0, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 1; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "+" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    }
                                }
                            }
                        }
                        break;
                        case "CR3":{

                        }
                        break;
                        case "ADR":{
                            float t = Float.valueOf(gets);
                            if(t > 255.0 || t < 1.0){
                                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(t == 0.0){
                                    try {
                                        String out = name + "+" + "0000.0";
                                        Log.e(TAG, "out = " + out);
                                        sending(out);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    inDialog.dismiss();
                                }
                                else {
                                    if (gets.startsWith("-")) {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(1, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 0; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "-" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        function.notifyDataSetChanged();
                                        inDialog.dismiss();
                                    } else {
                                        gets = String.valueOf(t);
                                        int i = gets.indexOf(".");
                                        Log.e(TAG, "gets = " + gets);
                                        String num1 = gets.substring(0, gets.indexOf("."));
                                        String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                        String set = "0";
                                        for (int j = 1; j < (4 - i); j++)
                                            set = set + "0";
                                        try {
                                            String out = name + "+" + set + num1 + num2;
                                            Log.e(TAG, "out = " + out);
                                            sending(out);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        inDialog.dismiss();
                                    }
                                }
                            }
                        }
                        break;
                        default:
                            Log.e(TAG,"Error");
                    }
                }
                else {
                    Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                inDialog.dismiss();
            }
        });
        switch(name){
            case  "NAME":{  //editChangeName
                c = true;
                editText.setHint(getString(R.string.changename));
                editText.addTextChangedListener(new EditChangeName(this, editText, 20));
            }
            break;
            case "PV1":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("-.0123456789"));
                editText.setHint(" - 5 ~ 5");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            case "PV2":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("-.0123456789"));
                editText.setHint(" - 10 ~ 10");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            case "PV3":{

            }
            break;
            case "EH1":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("-.0123456789"));
                editText.setHint(" - 10 ~ 65");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            case "EH2":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("-.0123456789"));
                editText.setHint(" 0 ~ 99");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            case "EH3":{

            }
            break;
            case "EL1":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("-.0123456789"));
                editText.setHint(" - 10 ~ 65");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            case "EL2":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("-.0123456789"));
                editText.setHint(" 0 ~ 99");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            case "EL3":{

            }
            break;
            case "CR1":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("-.0123456789"));
                editText.setHint(" - 10 ~ 65");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            case "CR2":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("-.0123456789"));
                editText.setHint(" 0 ~ 99");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            case "CR3":{

            }
            break;
            case "ADR":{
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                editText.setHint(" 1 ~ 255");
                editText.addTextChangedListener(new TextWatcher() {
                    int l = 0;    //記錄字串删除字元之前，字串的長度
                    int location = 0; //記錄光標位置
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        l = s.length();
                        location = editText.getSelectionStart();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                        Matcher m = p.matcher(s.toString());
                        if(m.find() || ("").equals(s.toString())){
                            c = true;
                        }else{
                            c = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            break;
            default:
                Log.e(TAG,"Error");
        }

        progressDialog.setContentView(setlinear, new LinearLayout.LayoutParams((int)(3 * all_Width / 5),
                (int)(all_Height / 4)));

        return progressDialog;
    }

    private Dialog switchDialog(final Context context, String position, String num) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        View v = inflater.inflate(R.layout.chose_switch, null);
        LinearLayout setlinear = (LinearLayout)v.findViewById(R.id.setlinear);
        TextView title = (TextView)v.findViewById(R.id.textView1);
        Button by = (Button)v.findViewById(R.id.button2);
        Button bn = (Button)v.findViewById(R.id.button1);
        Switch chose = (Switch)v.findViewById(R.id.switch1);
        title.setText(position);

        by.setText(getString(R.string.butoon_yes));
        bn.setText(getString(R.string.butoon_no));
        if(num.matches("On"))
            chose.setChecked(true);
        else
            chose.setChecked(false);

        chose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    switch_p = "DPP+0001.0";
                    Log.e(TAG,"ON");
                } else {
                    switch_p = "DPP+0000.0";
                    Log.e(TAG,"OFF");
                }
            }
        });

        by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    vibrator.vibrate(100);
                    if(switch_p != null) {
                        sending(switch_p);
                        sleep(400);
                        sending(get_fun);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                chDialog.dismiss();
            }
        });

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                chDialog.dismiss();
            }
        });

        progressDialog.setContentView(setlinear, new LinearLayout.LayoutParams((int)(3 * all_Width / 5),
                (int)(all_Height / 4)));

        return progressDialog;
    }

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void Remote_connec(){
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        s_connect = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.e(TAG, "s_connect = " + s_connect);
        if(s_connect == true){
            if(connect_flag != 3 && connect_flag != 4) {
                progressDialog = writeDialog(DeviceFunction.this, false, getString(R.string.connecting));
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            }

            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }
        else {
            Log.e(TAG,"服務綁訂狀態  = " + s_connect );
        }
    }

    private Dialog askDialog(Context context, boolean isAlpha) {
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
        View v = inflater.inflate(R.layout.askdialog, null);
        ProgressBar pb_progress_bar = (ProgressBar) v.findViewById(R.id.pb_progress_bar);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.askdia);
        TextView warn = (TextView) v.findViewById(R.id.textView1);
        showing = (TextView) v.findViewById(R.id.textView4);
        Button bn = (Button)v.findViewById(R.id.button1);
        Button by = (Button)v.findViewById(R.id.button2);

        bn.setText(getString(R.string.mes_no));
        by.setText(getString(R.string.mes_yes));
        pb_progress_bar.setVisibility(View.GONE);

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                if(Logdata.size() == totle){
                    connect_flag = 4;
                    ask_Dialog.dismiss();
                }
                else {
                    if (mBluetoothLeService.connect(BID)) {
                        try {
                            connect_flag = 4;
                            sending("STOP");
                            ask_Dialog.dismiss();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        connect_flag = 4;
                        ask_Dialog.dismiss();
                    }
                }
            }
        });

        by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    vibrator.vibrate(100);
                    pb_progress_bar.setVisibility(View.VISIBLE);
                    if(mBluetoothLeService.connect(BID)) {
                        Toast.makeText(DeviceFunction.this, getString(R.string.prepare), Toast.LENGTH_SHORT).show();
                        connect_flag = 3;
                        jsonflag = 0;
                        send = 4;
                        totle = 100;
                        sleep(30);
                        sending("Count+00099.0");
                        sleep(30);
                        if(data_Json.getCount() > 0){
                            data_Json.delete(BID);
                            Log.e(TAG,"爽拉 = " + data_Json.getCount());
                            data_Json.close();
                        }
                    }
                    else {
                        send = 4;
                        connectThread = new ConnectThread();
                        connectThread.run();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        progressDialog.setContentView(layout, new LinearLayout.LayoutParams((int)(2 * all_Width / 3),
                (int)(2 * all_Height / 7)));

        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            vibrator.vibrate(100);
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                return true;
            }
            else {
                return false;
            }
        });
        return progressDialog;
    }

    private Dialog loadDialog(Context context, List<String> loadlist) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Log.e(TAG, "loadlist = " + loadlist);
        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        View v = inflater.inflate(R.layout.loading, null);
        LinearLayout setlinear = (LinearLayout)v.findViewById(R.id.loadlist);
        Button by = (Button)v.findViewById(R.id.button2);
        Button bn = (Button)v.findViewById(R.id.button1);
        ListView list = (ListView)v.findViewById(R.id.loading);
        TextView t1 = (TextView)v.findViewById(R.id.no_list);

        if(data_table.getCount() == 0){
            list.setVisibility(View.GONE);
            t1.setVisibility(View.VISIBLE);
        }
        else {
            list.setVisibility(View.VISIBLE);
            t1.setVisibility(View.GONE);
            listData = data_table.fillList();
            dataList = new DataList(this, listData, all_Width);
            list.setAdapter(dataList);
            list.setOnItemClickListener(mLoadClickListener);
        }

        by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                List<String> a = data_table.getlist(dataList.getItem(select_item));
                Log.e(TAG,"a = " + a);
                Log.e(TAG,"List_d_num = " + List_d_num);
                for(int i = 0; i < a.size(); i++){
                    if(i == 0){
                        /*try{
                            String name = "NAME" + a.get(i);
                            sending(name);
                            sleep(50);
                        }catch (InterruptedException e){

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }*/
                    }
                    else if(i < 10){
                        Log.e(TAG,"a.get(i) = " + a.get(i));
                        try {
                            sending(a.get(i));
                            sleep(500);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e){
                        }
                    }
                    else{
                        String dpp = a.get(i);
                        dpp = dpp.substring(dpp.indexOf("+") + 1, dpp.length());
                        Log.e(TAG,"dpp = " + dpp);
                        if(dpp.matches("Off")) {
                            try {
                                Log.e(TAG,"send = " + send);
                                Log.e(TAG,"sending = " + "DPP+0000.0");
                                sending("DPP+0000.0");
                                sleep(500);
                                sending(get_fun);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            try {
                                Log.e(TAG,"send = " + send);
                                Log.e(TAG,"sending = " + "DPP+0001.0");
                                sending("DPP+0001.0");
                                sleep(500);
                                sending(get_fun);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                showDialog.dismiss();
            }
        });

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                showDialog.dismiss();
            }
        });

        progressDialog.setContentView(setlinear, new LinearLayout.LayoutParams((int)(3 * all_Width / 4),
                (int)(4 * all_Height / 5)));

        return progressDialog;
    }

    private AdapterView.OnItemClickListener mLoadClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            //一開始未選擇任何一個item所以為-1
            //======================
            //點選某個item並呈現被選取的狀態
            if ((select_item == -1) || (select_item==position)){
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }else{
                view1.setBackgroundDrawable(null); //將上一次點選的View保存在view1
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }
            view1 = view; //保存點選的View
            select_item = position;//保存目前的View位置
            //======================
            if(SQLdata.size() == 0) {
                for (HashMap<String, String> map : listData) {
                    SQLdata.add(map.get("id"));
                }
            }
            else {
                int i = 0;
                for (HashMap<String, String> map : listData) {
                    SQLdata.set(i, map.get("id"));
                    i++;
                }
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
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

    private Dialog dataDialog(Context context, List<String> savelist) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Log.e(TAG, "savelist" + savelist);
        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        View v = inflater.inflate(R.layout.savedatalist, null);
        LinearLayout setlinear = (LinearLayout)v.findViewById(R.id.savelist);
        TextView title = (TextView)v.findViewById(R.id.textView2);
        Button close = (Button)v.findViewById(R.id.button1);
        EditText name = (EditText)v.findViewById(R.id.editText);
        Button add = (Button)v.findViewById(R.id.button2);
        Button del = (Button)v.findViewById(R.id.button3);
        Button update = (Button)v.findViewById(R.id.button4);
        list = (ListView)v.findViewById(R.id.datalist1);
        TextView t1 = (TextView)v.findViewById(R.id.no_list);

        if(data_table.getCount() == 0){
            list.setVisibility(View.GONE);
            t1.setVisibility(View.VISIBLE);
        }
        else {
            select_item = -1;
            list.setVisibility(View.VISIBLE);
            t1.setVisibility(View.GONE);
            listData = data_table.fillList();
            dataList = new DataList(this, listData, all_Width);
            list.setAdapter(dataList);
            list.setOnItemClickListener(mListClickListener);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                saveDialog.dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                String listname = name.getText().toString().trim();
                if(listname.matches(""))
                    Toast.makeText(DeviceFunction.this, getString(R.string.addblank), Toast.LENGTH_SHORT).show();
                else {
                    if(data_table.getCount(listname) == 0) {
                        data_table.insert(DataSave, listname);
                        SQLdata.clear();
                        list.setVisibility(View.VISIBLE);
                        t1.setVisibility(View.GONE);
                        name.setText("");
                        try {
                            sleep(100);
                            listData = data_table.fillList();
                            dataList = new DataList(DeviceFunction.this, listData, all_Width);
                            list.setAdapter(dataList);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(DeviceFunction.this, getString(R.string.same), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Log.e(TAG,"select = " + select_item);
                if(select_item != -1){
                    data_table.delete(Integer.valueOf(SQLdata.get(select_item)));
                    SQLdata.remove(select_item);
                    Log.e(TAG,"SQLdata = " + SQLdata);
                    if(data_table.getCount() == 0){
                        list.setVisibility(View.GONE);
                        t1.setVisibility(View.VISIBLE);
                        select_item = -1;
                    }
                    else {
                        listData = data_table.fillList();
                        dataList = new DataList(DeviceFunction.this, listData, all_Width);
                        list.setAdapter(dataList);
                        select_item = -1;
                    }
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                if(data_table.getCount() != 0) {
                    if(select_item != -1) {
                        upDialog = updateDialog(DeviceFunction.this);
                        upDialog.show();
                        upDialog.setCanceledOnTouchOutside(false);
                    }
                }
            }
        });

        progressDialog.setContentView(setlinear, new LinearLayout.LayoutParams((int)(3 * all_Width / 4),
                (int)(4 * all_Height / 5)));

        return progressDialog;
    }

    private Dialog updateDialog(final Context context) {

        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        View v = inflater.inflate(R.layout.updatename, null);
        LinearLayout setlinear = (LinearLayout)v.findViewById(R.id.uplist);
        Button by = (Button)v.findViewById(R.id.button2);
        Button bn = (Button)v.findViewById(R.id.button1);
        EditText ed1 = (EditText)v.findViewById(R.id.editText1);

        by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                String getname = ed1.getText().toString().trim();
                if(getname.matches("")){
                    Toast.makeText(DeviceFunction.this, getString(R.string.addblank), Toast.LENGTH_SHORT).show();
                }
                else {
                    data_table.update(Integer.valueOf(SQLdata.get(select_item)), getname);
                    listData = data_table.fillList();
                    dataList = new DataList(DeviceFunction.this, listData, all_Width);
                    list.setAdapter(dataList);
                    select_item = -1;
                    upDialog.dismiss();
                }
            }
        });

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select_item = -1;
                vibrator.vibrate(100);
                upDialog.dismiss();
            }
        });

        progressDialog.setContentView(setlinear, new LinearLayout.LayoutParams((int)(3 * all_Width / 5),
                (int)(all_Height / 5)));

        return progressDialog;
    }

    private AdapterView.OnItemClickListener mListClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            //一開始未選擇任何一個item所以為-1
            //======================
            //點選某個item並呈現被選取的狀態
            if ((select_item == -1) || (select_item==position)){
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }else{
                view1.setBackgroundDrawable(null); //將上一次點選的View保存在view1
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }
            view1 = view; //保存點選的View
            select_item = position;//保存目前的View位置
            //======================
            if(SQLdata.size() == 0) {
                for (HashMap<String, String> map : listData) {
                    SQLdata.add(map.get("id"));
                }
                Log.e(TAG, "SQLdata = " + SQLdata);
            }
            else {
                int i = 0;
                for (HashMap<String, String> map : listData) {
                    SQLdata.set(i, map.get("id"));
                    i++;
                }
            }
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
                if (connect_flag == 1) {
                    Log.e(TAG, "連線中斷" + connect_flag);
                    Toast.makeText(DeviceFunction.this, getString(R.string.connect_err), Toast.LENGTH_SHORT).show();
                    new Thread(connectfail).start();
                }
                else if(connect_flag == 2){
                    Log.e(TAG, "連線中斷" + connect_flag);
                    Toast.makeText(DeviceFunction.this, getString(R.string.disconnect), Toast.LENGTH_SHORT).show();
                    new Thread(connectfail).start();
                }
                else if(connect_flag == 3){
                    Log.e(TAG, "連線中斷" + connect_flag);
                    try {
                        //Toast.makeText(DeviceFunction.this, getString(R.string.reconnecting), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DeviceFunction.this, getString(R.string.reconnecting), Toast.LENGTH_SHORT).show();
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
                                    login();
                                }
                                else {
                                    Logdata.clear();
                                    Tlist.clear();
                                    Hlist.clear();
                                    Clist.clear();
                                    timelist.clear();
                                    charttime.clear();
                                    sending(log);
                                }
                            }
                            else {
                                if (send == 0 || send == 1) {
                                    if (text.matches("OVER")) {
                                        //check = check + 1;
                                        Log.e(TAG, "checkOVER = " + text);
                                        Log.e(TAG,"check = " + check);
                                        Log.e(TAG,"return_RX.size() = " + return_RX.size());
                                        if (checkDeviceName(text).matches(check_arr[check])) {
                                            if(return_RX.size() != 8) {
                                                Log.e(TAG, "RX = " + return_RX);
                                                show_device_function();
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
                                                    check = check + 1;
                                                    Log.e(TAG, "check = " + check_arr[check]);
                                                }
                                            }
                                        }
                                    }
                                }
                                else if (send == 3) {
                                    if (text.matches("OVER")) {
                                        Log.e(TAG, "checkOVER = " + text);
                                        if (checkDeviceName(text).matches(check_arr[check])) {
                                            Log.e(TAG, "RX = " + return_RX);
                                            show_device_function();
                                        }
                                    } else {
                                        if (text.startsWith("DPP") && text.substring(text.indexOf("+") + 1, text.length()).replaceFirst("^0*", "").matches("1.0")) {
                                            setdpp = true;
                                            if (checkDeviceName(text).matches(check_arr[check])) {
                                                SelectItem.set(check + 1, checkDeviceName(text));
                                                return_RX.set(check, getString(R.string.DPP_status_on));
                                                DataSave.set(check + 1, getString(R.string.DPP_status_on));
                                                check = check + 1;
                                            } else {
                                            }
                                        } else if (text.startsWith("DPP") && text.substring(text.indexOf("+") + 1, text.length()).matches("0000.0")) {
                                            setdpp = false;
                                            if (checkDeviceName(text).matches(check_arr[check])) {
                                                SelectItem.set(check + 1, checkDeviceName(text));
                                                return_RX.set(check, getString(R.string.DPP_status_off));
                                                DataSave.set(check + 1, getString(R.string.DPP_status_off));
                                                check = check + 1;
                                            } else {
                                            }
                                        } else {
                                            if (checkDeviceName(text).matches(check_arr[check])) {
                                                SelectItem.set(check + 1, checkDeviceName(text));
                                                return_RX.set(check, text);
                                                DataSave.set(check + 1, text);
                                                check = check + 1;
                                                Log.e(TAG, "check = " + check_arr[check]);
                                                Log.e(TAG, "SelectItem = " + SelectItem);
                                                Log.e(TAG, "return_RX = " + return_RX);
                                                Log.e(TAG, "DataSave = " + DataSave);
                                            }
                                        }
                                    }
                                }
                                else if(send == 2){
                                    if (text.startsWith("NAME")) {    //Nordic_UART
                                        List_d_num.set(0, text.substring(4));
                                        DataSave.set(0, text.substring(4));
                                        function.notifyDataSetChanged();
                                    } else if (text.startsWith("DPP")) {
                                        if (setdpp) {
                                            set = getString(R.string.DPP_status_on);
                                            set2 = "On";
                                        } else {
                                            set = getString(R.string.DPP_status_off);
                                            set2 = "Off";
                                        }
                                        if (text.startsWith("DPP") && text.substring(text.indexOf("+") + 1, text.length()).replaceFirst("^0*", "").matches("1.0")) {
                                            setdpp = true;
                                            return_RX.set(return_RX.indexOf(set), getString(R.string.DPP_status_on));
                                            DataSave.set(DataSave.indexOf(set), getString(R.string.DPP_status_on));
                                            List_d_num.set(List_d_num.indexOf(set2), "On");
                                        } else if (text.startsWith("DPP") && text.substring(text.indexOf("+") + 1, text.length()).matches("0000.0")) {
                                            setdpp = false;
                                            return_RX.set(return_RX.indexOf(set), getString(R.string.DPP_status_off));
                                            DataSave.set(DataSave.indexOf(set), getString(R.string.DPP_status_off));
                                            List_d_num.set(List_d_num.indexOf(set2), "Off");
                                        }
                                        function.notifyDataSetChanged();
                                    } else {
                                        if(!text.matches("OVER")) {
                                            int i = SelectItem.indexOf(checkDeviceName(text));
                                            return_RX.set((i - 1), text);
                                            DataSave.set(i, text);
                                            List_d_num.set(i, getDeviceNum(return_RX.get(i - 1), setdpp));
                                            Log.e(TAG, "i = " + i);
                                            Log.e(TAG, "SelectItem = " + SelectItem);
                                            Log.e(TAG, "return_RX= " + return_RX);
                                            Log.e(TAG, "List_d_num = " + List_d_num);
                                            Log.e(TAG, "DataSave = " + DataSave);
                                            function.notifyDataSetChanged();
                                        }
                                    }
                                }
                                else if (send == 4){
                                    if(text.startsWith("Count")){
                                        test = 0;
                                        showtext = String.valueOf((test)) + " / " + String.valueOf(totle);
                                        showing.setText(showtext);
                                        sleep(50);
                                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                                        Service_close();
                                    }
                                    else if(text.startsWith("START")){
                                        getdate = text.substring(text.indexOf("START") + 5, text.length());
                                        new Thread(timeprocess).start();
                                    }
                                    else if(text.startsWith("COU")){
                                        getcount = text;
                                        new Thread(getc).start();
                                    }
                                    else if(text.startsWith("INT")){
                                        gettime = text;
                                        new Thread(uptime).start();
                                    }
                                    else if(text.startsWith("STOP")){
                                        send = 2;
                                    }
                                    else {
                                        if(text.startsWith("T")) {
                                            Logdata.add(text);
                                            new Thread(down_log).start();
                                            if(String.valueOf(test).matches(getcount)){
                                                send = 2;
                                                connect_flag = 4;
                                                ask_Dialog.dismiss();
                                            }
                                        }
                                    }
                                }
                                else {
                                    send = 2;
                                    Log.e(TAG, "text = " + text);
                                    if(text.startsWith("PWR=")){
                                        P_word = text.substring(text.indexOf("=") + 1,text.length());
                                        Log.e(TAG, "P_word = " + P_word);
                                    }
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };

    private void sending(String value) throws UnsupportedEncodingException {
        byte[] sends;
        sends = value.getBytes("UTF-8");
        if(mBluetoothLeService == null) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) B_service).getService();
            mBluetoothLeService.writeRXCharacteristic(sends);
        }
        else {
            mBluetoothLeService.writeRXCharacteristic(sends);
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

    private void login() throws UnsupportedEncodingException, InterruptedException {
        check = 0;
        send = 0;
        SelectItem.add("NAME");
        DataSave.add(device_name);
        sending(get_fun);
        if(progressDialog2 == null) {
            progressDialog2 = writeDialog(DeviceFunction.this, false, getString(R.string.login));
            progressDialog2.show();
            progressDialog2.setCanceledOnTouchOutside(false);
        }
        new Thread(timedelay).start();
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
                    if(progressDialog2 != null)
                        progressDialog2.dismiss();
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

    private Runnable timeprocess = new Runnable() {
        @Override
        public void run() {
            String year, month, day, hour, minute, second;
            year = getdate.substring(0, 4);
            month = getdate.substring(4, 6);
            day = getdate.substring(6, 8);
            hour = getdate.substring(8, 10); //24hour
            minute = getdate.substring(10, 12);
            second = getdate.substring(12, 14);
            getdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        }
    };

    private Runnable getc = new Runnable() {
        @Override
        public void run() {
            String count = getcount.substring(getcount.indexOf("COU") + 3, getcount.length()).replaceFirst("^0*", "");
            getcount = count;
            Log.e(TAG,"getcount = " + getcount);
        }
    };

    private Runnable uptime = new Runnable() {
        @Override
        public void run() {
            String up = gettime.substring(gettime.indexOf("INT") + 3, gettime.length()).replaceFirst("^0*", "");
            gettime = up;
            Log.e(TAG,"gettime = " + gettime);
        }
    };

    private Runnable down_log = new Runnable() {
        @Override
        public void run() {
            Message m2 = new Message();
            m2.obj=(Integer)2;
            downloading.sendMessage(m2);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler downloading = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            test++;
            showtext = String.valueOf(test) + " / " + String.valueOf(totle);
            showing.setText(showtext);
            Log.e(TAG,"test = " + test);
            if(Logdata.size() == totle){
                connect_flag = 4;
                new Thread(analysislog).start();
                for(;jsonflag == 0;){
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(maketime).start();
                for(;jsonflag == 1;){
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(logSQL).start();
            }
        }
    };

    private Runnable analysislog = new Runnable() {
        @Override
        public void run() {
            for(int i = 0; i < Logdata.size(); i++){
                getvalue = Logdata.get(i);
                temperature = getvalue.substring(getvalue.indexOf("T") + 1, getvalue.indexOf("H"));
                humidity = getvalue.substring(getvalue.indexOf("H") + 1, getvalue.length());
                if(temperature.startsWith("+")) {
                    temperature = temperature.substring(temperature.indexOf("+") + 1, temperature.length());
                    Tlist.add(temperature);
                }
                else
                    Tlist.add(temperature);
                if(humidity.startsWith("+")){
                    humidity = humidity.substring(humidity.indexOf("+") + 1, humidity.length());
                    Hlist.add(humidity);
                }
                else
                    Hlist.add(humidity);
            }
            jsonflag = 1;
            //Log.e(TAG,"Tlist = " + Tlist + "\n" + "Hlist = " + Hlist);
        }
    };

    private Runnable maketime = new Runnable() {
        @Override
        public void run() {
            try {
                String formattime;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(getdate);
                for(int i = 0; i < Logdata.size(); i ++) {
                    date.setTime(date.getTime() + Integer.valueOf(gettime) * 1000);
                    formattime = sdf.format(date);
                    charttime.add(formattime);
                    formattime = formattime.substring(5, formattime.indexOf(" ")) + " " +
                            formattime.substring(formattime.indexOf(" ") + 1, formattime.length() - 3);
                    timelist.add(formattime);
                }
                jsonflag = 2;
                //Log.e(TAG,"timelist = " + timelist);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
    };

    private Runnable logSQL = new Runnable() {
        @Override
        public void run() {
            try {
                Chart_json = new JSONArray(charttime);
                sleep(30);
                TimeList_json = new JSONArray(timelist);
                sleep(30);
                T_json = new JSONArray(Tlist);
                sleep(30);
                H_json = new JSONArray(Hlist);
                sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG,"charttime = " + charttime);
            Log.e(TAG,"Chart_json = " +Chart_json);

            data_Json.insert(Chart_json, TimeList_json, T_json, H_json, BID);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler_remote_connec = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Remote_connec();
        }
    };

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

    private String getDeviceName(String name){

        String rename = "";
        //String sub = name.substring(0, name.indexOf("+"));

        if(name.startsWith("PV1")){
            rename = getString(R.string.PV1);
        }
        if(name.startsWith("PV2")){
            rename = getString(R.string.PV2);
        }
        if(name.startsWith("PV3")){
            rename = getString(R.string.PV3);
        }
        if(name.startsWith("EH1")){
            rename = getString(R.string.EH1);
        }
        if(name.startsWith("EH2")){
            rename = getString(R.string.EH2);
        }
        if(name.startsWith("EH3")){
            rename = getString(R.string.EH3);
        }
        if(name.startsWith("EL1")){
            rename = getString(R.string.EL1);
        }
        if(name.startsWith("EL2")){
            rename = getString(R.string.EL2);
        }
        if(name.startsWith("EL3")){
            rename = getString(R.string.EL3);
        }
        if(name.startsWith("CR1")){
            rename = getString(R.string.CR1);
        }
        if(name.startsWith("CR2")){
            rename = getString(R.string.CR2);
        }
        if(name.startsWith("CR3")){
            rename = getString(R.string.CR3);
        }
        if(name.startsWith("ADR")){
            rename = getString(R.string.ADR);
        }
        if(name.startsWith("DPP")){
            rename = getString(R.string.DPP);
        }
        return rename;
    }

    private String getDeviceNum(String num, Boolean setdpp){

        String renum = "";

        if(num.indexOf("+") != -1){
            if(num.substring(num.indexOf("+") + 1, num.length()).matches("On") || num.substring(num.indexOf("+") + 1, num.length()).matches("Off")) {
                String setStr = num.substring(num.indexOf("+") + 1, num.length());
                renum = setStr;
            }
            else {
                if (num.substring(num.indexOf("+") + 1, num.length()).matches("0000.0")) {
                    renum = "0";
                } else {
                    String newStr = num.substring(num.indexOf("+") + 1, num.length()).replaceFirst("^0*", "");
                    if (setdpp) {
                        if(newStr.startsWith(".")){
                            renum = "0" + newStr;
                        }
                        else {
                            renum = newStr;
                        }
                    }
                    if (!setdpp) {
                        String setStr = num.substring(num.indexOf("+") + 1, num.indexOf(".")).replaceFirst("^0*", "");
                        if(setStr.length() == 0)
                            renum = "0";
                        else
                            renum = setStr;
                    }
                }
            }
        }
        else if(num.indexOf("-") != -1){
            if(num.substring(num.indexOf("-") + 1, num.length()).matches("0000.0")){
                renum = "0";
            }
            else{
                String newStr = num.substring(num.indexOf("-") + 1, num.length()).replaceFirst("^0*", "");
                if (setdpp) {
                    if (newStr.startsWith(".")) {
                        renum = "0" + newStr;
                    } else {
                        renum = "- " + newStr;
                    }
                }
                if (!setdpp) {
                    String setStr = num.substring(num.indexOf("-") + 1, num.indexOf(".")).replaceFirst("^0*", "");
                    if(setStr.length() == 0){
                        renum = "0";
                    }
                    else {
                        renum = "- " + setStr;
                    }
                }
            }
        }
        return renum;
    }

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

        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            vibrator.vibrate(100);
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                if(checkpassword != null)
                    checkpassword.dismiss();
                return false;
            }
            else {
                return false;
            }
        });
        return progressDialog;
    }

    public void Service_close() {
        if (mBluetoothLeService == null) {
            Log.e(TAG,"masaga");
            return;
        }
        mBluetoothLeService.disconnect();
    }

    private void intentlogview(){

        Intent intent = new Intent(DeviceFunction.this, LogChartView.class);
        intent.putStringArrayListExtra("charttime", charttime);
        intent.putStringArrayListExtra("Tlist", Tlist);
        intent.putStringArrayListExtra("Hlist", Hlist);
        intent.putStringArrayListExtra("Clist", Clist);
        intent.putStringArrayListExtra("List_d_num", List_d_num);
        intent.putStringArrayListExtra("timelist", timelist);
        intent.putExtra("all_Width",all_Width);
        intent.putExtra("all_Height",all_Height);
        intent.putExtra("device", device_name);
        intent.putExtra("setdpp", setdpp);

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    intentlogview();
                    //取得聯絡人權限，進行工作
                } else {
                    if (mBluetoothLeService != null) {
                        if (s_connect) {
                            unbindService(mServiceConnection);
                            s_connect = false;
                        }
                        mBluetoothLeService.stopSelf();
                        mBluetoothLeService = null;
                    }
                    finish();
                    //使用者拒絕權限，顯示對話框告知
                }
            }
            break;
        }
    }

    private void disconnect(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //toolbar menu
        MenuInflater inflater = getMenuInflater();
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        //updateMenuTitles();
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
            connect_flag = 2;
            if(mBluetoothAdapter != null)
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Service_close();
            data_table.close();
            disconnect();
            return true;
        }
        return true;
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
            data_table.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(s_connect)
            unregisterReceiver(mGattUpdateReceiver);
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
    protected void onStop(){
        super.onStop();
    }

    private class ConnectThread extends Thread {
        public void run() {
            Message m2 = new Message();
            m2.obj=(Integer)2;
            handler_remote_connec.sendMessage(m2);
            Log.e(TAG, "沒進來嗎?");
        }
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
}
