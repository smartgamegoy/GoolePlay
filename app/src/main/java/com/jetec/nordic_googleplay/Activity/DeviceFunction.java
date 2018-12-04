package com.jetec.nordic_googleplay.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jetec.nordic_googleplay.CheckDeviceName;
import com.jetec.nordic_googleplay.Dialog.Interval;
import com.jetec.nordic_googleplay.Dialog.ModifyPassword;
import com.jetec.nordic_googleplay.Dialog.SwitchDialog;
import com.jetec.nordic_googleplay.DialogFunction.CR;
import com.jetec.nordic_googleplay.DialogFunction.EH;
import com.jetec.nordic_googleplay.DialogFunction.EL;
import com.jetec.nordic_googleplay.DialogFunction.IH;
import com.jetec.nordic_googleplay.DialogFunction.IL;
import com.jetec.nordic_googleplay.DialogFunction.PV;
import com.jetec.nordic_googleplay.EditManagert.EditChangeName;
import com.jetec.nordic_googleplay.GetDeviceName;
import com.jetec.nordic_googleplay.GetDeviceNum;
import com.jetec.nordic_googleplay.GetString;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.SQL.JsonSQL;
import com.jetec.nordic_googleplay.SQL.ModelSQL;
import com.jetec.nordic_googleplay.SQL.SQLData;
import com.jetec.nordic_googleplay.SendLog;
import com.jetec.nordic_googleplay.SendValue;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import com.jetec.nordic_googleplay.Thread.ConnectThread;
import com.jetec.nordic_googleplay.Value;
import com.jetec.nordic_googleplay.ViewAdapter.DataList;
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
import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jetec.nordic_googleplay.Activity.DeviceList.getManager;
import static java.lang.Thread.sleep;

public class DeviceFunction extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<HashMap<String, String>> listData;
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private float MaxEH1, MinEL1, MaxEH2, MinEL2, MaxEH3, MinEL3, MaxIH1, MinIL1, MaxIH2, MinIL2, MaxIH3, MinIL3;
    private ModifyPassword modifyPassword;
    private String TAG = "devicefunction", text, log = "log", set, set2, showtext, getdate,
            getcount, gettime, gettoast1, gettoast2,
            gettoast3, gettoast4, gettoast5, gettoast6, get_fun = "get";
    private boolean s_connect = false, c = false;
    private int connect_flag, send, check, test = 0, totle, jsonflag, select_item = -1;
    private Dialog progressDialog2 = null, ask_Dialog = null, checkpassword = null, saveDialog = null,
            showDialog = null, inDialog = null, upDialog = null, choseDialog = null;
    private BluetoothLeService mBluetoothLeService;
    private Intent intents;
    private ConnectThread connectThread;
    private byte[] txValue;
    private ArrayList<String> SQLdata, Logdata, Firstlist, Secondlist, Thirdlist,
            timelist, charttime, List_d_num, List_d_function, Jsonlist;
    private View header, engin, button;
    private Function function;
    private TextView showing;
    private BluetoothAdapter mBluetoothAdapter;
    private ModelSQL modelSQL;
    private View view1;
    private ListView list;
    private JSONArray Chart_json, TimeList_json, Firstjson, Secondjson, Thirdjson;
    private Vibrator vibrator;
    private JsonSQL data_Json;
    private SQLData data_table;
    private DataList dataList;
    private Interval interval;
    private GetDeviceName getDeviceName;
    private GetDeviceNum getDeviceNum;
    private NavigationView navigationView;
    private SendValue sendValue;
    private CheckDeviceName checkDeviceName;
    private PV pv;
    private EH eh;
    private EL el;
    private CR cr;
    private IH ih;
    private IL il;
    private SwitchDialog switchDialog;
    private SendLog sendLog;

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
        all_list();
        get_intent();
    }

    private void all_list() {
        List_d_function = new ArrayList<>();
        List_d_num = new ArrayList<>();
        SQLdata = new ArrayList<>();
        Logdata = new ArrayList<>();
        Firstlist = new ArrayList<>();
        Secondlist = new ArrayList<>();
        timelist = new ArrayList<>();
        Thirdlist = new ArrayList<>();
        charttime = new ArrayList<>();
        Jsonlist = new ArrayList<>();
    }

    @SuppressLint("HandlerLeak")
    private Handler connectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Remote_connec();
        }
    };

    private void get_intent() {

        Intent intent = getIntent();
        s_connect = intent.getBooleanExtra("s_connect", s_connect);

        gettoast1 = getString(R.string.samepassword);
        gettoast2 = getString(R.string.samepassword2);
        gettoast3 = getString(R.string.samepassword3);
        gettoast4 = getString(R.string.success);
        gettoast5 = getString(R.string.originalpassworderror);
        gettoast6 = getString(R.string.inputerror);

        Log.e(TAG, "Value.BName = " + Value.deviceModel.substring(3, 4));
        if (Value.deviceModel.substring(3, 4).matches("1")) {
            Value.modelsign = 1;    //幾排
        } else if (Value.deviceModel.substring(3, 4).matches("2")) {
            Value.modelsign = 2;
        } else if (Value.deviceModel.substring(3, 4).matches("3")) {
            Value.modelsign = 3;
        }

        try {
            show_device_function();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void DrawerLayout(Toolbar myToolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!Value.downlog) {
            navigationView.getMenu().findItem(R.id.nav_share).setTitle(getString(R.string.start) + getString(R.string.LOG));
        } else {
            navigationView.getMenu().findItem(R.id.nav_share).setTitle(getString(R.string.end) + getString(R.string.LOG));
        }

        if (!Value.btn) {
            navigationView.getMenu().findItem(R.id.datadownload).setEnabled(false);
            SpannableString spanString1 = new SpannableString(navigationView.getMenu().
                    findItem(R.id.datadownload).getTitle().toString());
            spanString1.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanString1.length(), 0);
            navigationView.getMenu().findItem(R.id.datadownload).setTitle(spanString1);
            navigationView.getMenu().findItem(R.id.showdialog).setEnabled(false);
            SpannableString spanString2 = new SpannableString(navigationView.getMenu().
                    findItem(R.id.showdialog).getTitle().toString());
            spanString2.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanString2.length(), 0);
            navigationView.getMenu().findItem(R.id.showdialog).setTitle(spanString2);
            navigationView.getMenu().findItem(R.id.nav_share).setEnabled(false);
            SpannableString spanString3 = new SpannableString(navigationView.getMenu().
                    findItem(R.id.nav_share).getTitle().toString());
            spanString3.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanString3.length(), 0);
            navigationView.getMenu().findItem(R.id.nav_share).setTitle(spanString3);
        }
    }

    private void show_device_function() throws JSONException {

        setContentView(R.layout.device_function);

        data_table = new SQLData(this);
        data_Json = new JsonSQL(this);
        modelSQL = new ModelSQL(this);
        checkDeviceName = new CheckDeviceName();

        if (progressDialog2 != null) {
            progressDialog2.dismiss();
        }

        List_d_function.clear();
        List_d_num.clear();

        //engin = findViewById(R.id.engin);
        //header = findViewById(R.id.header);
        //button = findViewById(R.id.button);
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
        } else if (Value.passwordFlag == 2 || Value.passwordFlag == 3) {
            DrawerLayout(myToolbar);
            //header.setVisibility(View.VISIBLE);
            //button.setVisibility(View.VISIBLE);
            //engin.setVisibility(View.GONE);
            b_save.setVisibility(View.GONE);
            b_load.setVisibility(View.GONE);
            b_download.setVisibility(View.GONE);
            b_dialog.setVisibility(View.GONE);
            b_re.setVisibility(View.GONE);
            l_b.setVisibility(View.GONE);
        } else {
            //header.setVisibility(View.GONE);
            //button.setVisibility(View.GONE);
            //engin.setVisibility(View.GONE);
            b_save.setVisibility(View.GONE);
            b_load.setVisibility(View.GONE);
            b_download.setVisibility(View.GONE);
            b_dialog.setVisibility(View.GONE);
            b_re.setVisibility(View.GONE);
            l_b.setVisibility(View.GONE);
        }

        Log.e(TAG, "List_d_function = " + List_d_function);
        Log.e(TAG, "string = " + List_d_num);
        Log.e(TAG, "DP1 = " + Value.IDP1);
        Log.e(TAG, "DP2 = " + Value.IDP2);
        Log.e(TAG, "DP3 = " + Value.IDP3);

        function = new Function(this, List_d_function, List_d_num,
                Value.SelectItem, Value.all_Width);
        listname.setAdapter(function);
        listname.setOnItemClickListener(mSelectClickListener);

        if (data_Json.getCount() > 0) {

            String chart, time, t, h, c;

            ArrayList<String> jsonlist = new ArrayList<>();
            jsonlist.clear();
            jsonlist = data_Json.getlist(Value.BID);
            for (int i = 0; i < jsonlist.size(); i++) {
                switch (i) {
                    case 0: {
                        charttime.clear();
                        chart = jsonlist.get(i);
                        JSONArray json = new JSONArray(chart);
                        for (int j = 0; j < json.length(); j++) {
                            charttime.add(String.valueOf(json.get(j)));
                            Log.e(TAG, "補給你拉0");
                        }
                    }
                    case 1: {
                        timelist.clear();
                        time = jsonlist.get(i);
                        JSONArray json = new JSONArray(time);
                        for (int j = 0; j < json.length(); j++) {
                            timelist.add(String.valueOf(json.get(j)));
                            Log.e(TAG, "補給你拉1");
                        }
                    }
                    break;
                    case 2: {
                        Firstlist.clear();
                        t = jsonlist.get(i);
                        JSONArray json = new JSONArray(t);
                        for (int j = 0; j < json.length(); j++) {
                            Firstlist.add(String.valueOf(json.get(j)));
                            Log.e(TAG, "補給你拉2");
                        }
                    }
                    break;
                    case 3: {
                        Secondlist.clear();
                        h = jsonlist.get(i);
                        JSONArray json = new JSONArray(h);
                        for (int j = 0; j < json.length(); j++) {
                            Secondlist.add(String.valueOf(json.get(j)));
                            Log.e(TAG, "補給你拉3");
                        }
                    }
                    case 4: {
                        Thirdlist.clear();
                        c = jsonlist.get(i);
                        JSONArray json = new JSONArray(c);
                        for (int j = 0; j < json.length(); j++) {
                            Thirdlist.add(String.valueOf(json.get(j)));
                            Log.e(TAG, "補給你拉4");
                        }
                    }
                }
            }
        }
        b_save.setOnClickListener(v -> {
            vibrator.vibrate(100);
            saveDialog = dataDialog(DeviceFunction.this, Value.DataSave);
            saveDialog.show();
            saveDialog.setCanceledOnTouchOutside(false);
        });

        b_load.setOnClickListener(v -> {
            vibrator.vibrate(100);
            showDialog = loadDialog(DeviceFunction.this, Value.DataSave);
            showDialog.show();
            showDialog.setCanceledOnTouchOutside(false);
        });

        b_download.setOnClickListener(v -> {
            connect_flag = 3;
            vibrator.vibrate(100);
            ask_Dialog = askDialog(DeviceFunction.this);
            ask_Dialog.show();
            ask_Dialog.setCanceledOnTouchOutside(false);
        });

        b_dialog.setOnClickListener(v -> {
            vibrator.vibrate(100);
            if (timelist.size() == 0) {
                Log.e(TAG, "charttime.size() = " + timelist.size());
                Toast.makeText(DeviceFunction.this, getString(R.string.logdata), Toast.LENGTH_SHORT).show();
            } else {
                requeststorage();
            }
        });

        b_re.setOnClickListener(v -> {
            send = 5;
            vibrator.vibrate(100);
            modifyPassword = new ModifyPassword(DeviceFunction.this, Value.all_Width,
                    Value.all_Height, Value.P_word,
                    Value.G_word, Value.E_word, Value.I_word, gettoast1, gettoast2, gettoast3, gettoast4,
                    gettoast5, gettoast6, mBluetoothLeService);
            modifyPassword.modifyDialog(vibrator);
        });
    }

    private void requeststorage() {
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
        } else {
            intentlogview();
        }
    }

    private AdapterView.OnItemClickListener mSelectClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            String select = Value.SelectItem.get(position);
            //noinspection deprecation
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Log.e(TAG, "名稱 = " + List_d_function.get(position) + "輸出 = " + select);
            //String output = switch_dialog(select, List_d_function.get(position));
            if (select.startsWith("INTER")) {
                String description = getString(R.string.description);
                interval = new Interval(DeviceFunction.this, Value.all_Width, Value.all_Height, mBluetoothLeService, description);
                interval.showDialog();
            } else if (select.startsWith("DP") || select.startsWith("SPK")) {
                switchDialog = new SwitchDialog(DeviceFunction.this, mBluetoothLeService);
                choseDialog = switchDialog.chose(select, List_d_num.get(position), List_d_function.get(position), vibrator);
                switchDialog.setDialog(choseDialog);
                choseDialog.show();
                choseDialog.setCanceledOnTouchOutside(false);
            } else {
                inDialog = inputDialog(DeviceFunction.this, List_d_function.get(position), select);
                inDialog.show();
                inDialog.setCanceledOnTouchOutside(false);
            }
        }
    };

    private Dialog inputDialog(final Context context, String position, final String name) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        sendValue = new SendValue(mBluetoothLeService);
        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.alterdialog, null);
        ConstraintLayout layout = v.findViewById(R.id.input_dialog);
        TextView title = v.findViewById(R.id.textView1);
        Button by = v.findViewById(R.id.button2);
        Button bn = v.findViewById(R.id.button1);
        final EditText editText = v.findViewById(R.id.editText1);
        title.setText(position);
        by.setText(getString(R.string.butoon_yes));
        bn.setText(getString(R.string.butoon_no));
        by.setOnClickListener(v12 -> {
            vibrator.vibrate(100);
            if (c) {
                String gets = editText.getText().toString().trim();
                Log.e(TAG, "gets = " + gets);
                Log.e(TAG, "case = " + name);
                switch (name) {
                    case "NAME": {
                        String out = "NAME" + gets;
                        sendValue.send(out);
                        inDialog.dismiss();
                    }
                    break;
                    case "PV1": {
                        float t = Float.valueOf(gets);
                        pv = new PV(this, function);
                        pv.todo(t, name, inDialog, mBluetoothLeService, gets);
                    }
                    break;
                    case "PV2": {
                        float t = Float.valueOf(gets);
                        pv = new PV(this, function);
                        pv.todo(t, name, inDialog, mBluetoothLeService, gets);
                    }
                    break;
                    case "PV3": {
                        float t = Float.valueOf(gets);
                        pv = new PV(this, function);
                        pv.todo(t, name, inDialog, mBluetoothLeService, gets);
                    }
                    break;
                    case "EH1": {
                        float t = Float.valueOf(gets);
                        Log.e(TAG, "Text = " + t);
                        eh = new EH(this, function);
                        eh.todo(t, name, inDialog, mBluetoothLeService, gets, MinEL1);
                    }
                    break;
                    case "EH2": {
                        float t = Float.valueOf(gets);
                        eh = new EH(this, function);
                        eh.todo(t, name, inDialog, mBluetoothLeService, gets, MinEL2);
                    }
                    break;
                    case "EH3": {
                        float t = Float.valueOf(gets);
                        eh = new EH(this, function);
                        eh.todo(t, name, inDialog, mBluetoothLeService, gets, MinEL3);
                    }
                    break;
                    case "EL1": {
                        float t = Float.valueOf(gets);
                        el = new EL(this, function);
                        el.todo(t, name, inDialog, mBluetoothLeService, gets, MaxEH1);
                    }
                    break;
                    case "EL2": {
                        float t = Float.valueOf(gets);
                        el = new EL(this, function);
                        el.todo(t, name, inDialog, mBluetoothLeService, gets, MaxEH2);
                    }
                    break;
                    case "EL3": {
                        float t = Float.valueOf(gets);
                        el = new EL(this, function);
                        el.todo(t, name, inDialog, mBluetoothLeService, gets, MaxEH3);
                    }
                    break;
                    case "CR1": {
                        float t = Float.valueOf(gets);
                        cr = new CR(this, function);
                        cr.todo(t, name, inDialog, mBluetoothLeService, gets);
                    }
                    break;
                    case "CR2": {
                        float t = Float.valueOf(gets);
                        cr = new CR(this, function);
                        cr.todo(t, name, inDialog, mBluetoothLeService, gets);
                    }
                    break;
                    case "CR3": {
                        float t = Float.valueOf(gets);
                        cr = new CR(this, function);
                        cr.todo(t, name, inDialog, mBluetoothLeService, gets);
                    }
                    break;
                    case "IH1": {
                        float t = Float.valueOf(gets);
                        ih = new IH(this, function);
                        ih.todo(t, name, inDialog, mBluetoothLeService, gets, MinIL1);
                    }
                    break;
                    case "IH2": {
                        float t = Float.valueOf(gets);
                        ih = new IH(this, function);
                        ih.todo(t, name, inDialog, mBluetoothLeService, gets, MinIL2);
                    }
                    break;
                    case "IH3": {
                        float t = Float.valueOf(gets);
                        ih = new IH(this, function);
                        ih.todo(t, name, inDialog, mBluetoothLeService, gets, MinIL3);
                    }
                    break;
                    case "IL1": {
                        float t = Float.valueOf(gets);
                        il = new IL(this, function);
                        il.todo(t, name, inDialog, mBluetoothLeService, gets, MaxIH1);
                    }
                    break;
                    case "IL2": {
                        float t = Float.valueOf(gets);
                        il = new IL(this, function);
                        il.todo(t, name, inDialog, mBluetoothLeService, gets, MaxIH2);
                    }
                    break;
                    case "IL3": {
                        float t = Float.valueOf(gets);
                        il = new IL(this, function);
                        il.todo(t, name, inDialog, mBluetoothLeService, gets, MaxIH3);
                    }
                    break;
                    case "ADR": {
                        float t = Float.valueOf(gets);
                        if (t > 255.0 || t < 1.0) {
                            Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                        } else {
                            if (t == 0.0) {
                                String out = name + "+" + "0000.0";
                                Log.e(TAG, "out = " + out);
                                sendValue.send(out);
                                inDialog.dismiss();
                            } else {
                                if (gets.startsWith("-")) {
                                    gets = String.valueOf(t);
                                    int i = gets.indexOf(".");
                                    Log.e(TAG, "gets = " + gets);
                                    String num1 = gets.substring(1, gets.indexOf("."));
                                    String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                    StringBuilder set = new StringBuilder("0");
                                    for (int j = 0; j < (4 - i); j++)
                                        set.append("0");
                                    String out = name + "-" + set + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                    function.notifyDataSetChanged();
                                    inDialog.dismiss();
                                } else {
                                    gets = String.valueOf(t);
                                    int i = gets.indexOf(".");
                                    Log.e(TAG, "gets = " + gets);
                                    String num1 = gets.substring(0, gets.indexOf("."));
                                    String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                    StringBuilder set = new StringBuilder("0");
                                    for (int j = 1; j < (4 - i); j++)
                                        set.append("0");
                                    String out = name + "+" + set + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                    inDialog.dismiss();
                                }
                            }
                        }
                    }
                    break;
                    default:
                        Log.e(TAG, "Error");
                }
            } else {
                Toast.makeText(DeviceFunction.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
            }
        });

        bn.setOnClickListener(v1 -> {
            vibrator.vibrate(100);
            inDialog.dismiss();
        });
        switch (name) {
            case "NAME": {  //editChangeName
                c = true;
                editText.setHint(getString(R.string.changename));
                editText.addTextChangedListener(new EditChangeName(this, editText, 20));
            }
            break;
            case "PV1": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(0).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else if (Value.name.get(0).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(0).toString().matches("I")) {
                    c = true;
                    Log.e(TAG, "Value.IDP1 = " + Value.IDP1);
                    if (!Value.IDP1) {
                        editText.setHint(" 999 ~ -999");
                    } else {
                        editText.setHint(" 99.9 ~ -99.9");
                    }
                }
            }
            break;
            case "PV2": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(1).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else if (Value.name.get(1).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(1).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP2) {
                        editText.setHint(" 999 ~ -999");
                    } else {
                        editText.setHint(" 99.9 ~ -99.9");
                    }
                }
            }
            break;
            case "PV3": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(2).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else if (Value.name.get(2).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(2).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP3) {
                        editText.setHint(" 999 ~ -999");
                    } else {
                        editText.setHint(" 99.9 ~ -99.9");
                    }
                }
            }
            break;
            case "EH1": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(0).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(0).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP1) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "EH2": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(1).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(1).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP2) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "EH3": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(2).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(2).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP3) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "EL1": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(0).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(0).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP1) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "EL2": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(1).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(1).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP2) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "EL3": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(2).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(2).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP3) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "CR1": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(0).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(0).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP1) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "CR2": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(1).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(1).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP2) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "CR3": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(2).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(2).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP3) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "IH1": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(0).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(0).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP1) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "IH2": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(1).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(1).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP2) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "IH3": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(2).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(2).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP3) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "IL1": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(0).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(0).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(0).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP1) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "IL2": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(1).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(1).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(1).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP2) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "IL3": {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (Value.name.get(2).toString().matches("T")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("H")) {
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
                            c = m.find() || ("").equals(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else if (Value.name.get(2).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                } else if (Value.name.get(2).toString().matches("I")) {
                    c = true;
                    if (!Value.IDP3) {
                        editText.setHint(" 9999 ~ -999");
                    } else {
                        editText.setHint(" 999.9~-199.9");
                    }
                }
            }
            break;
            case "ADR": {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                        c = m.find() || ("").equals(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
            break;
            default:
                Log.e(TAG, "Error");
        }

        progressDialog.setContentView(layout, new ConstraintLayout.LayoutParams((int) (3 * Value.all_Width / 5),
                (int) (Value.all_Height / 4)));

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

    private void Remote_connec() {
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        s_connect = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.e(TAG, "s_connect = " + s_connect);
        if (s_connect) {
            /*if (Value.downloading) {
                Dialog progressDialog = writeDialog(DeviceFunction.this, getString(R.string.connecting));
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            }*/
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        } else {
            Log.e(TAG, "服務綁訂狀態  = " + false);
        }
    }

    private Dialog askDialog(Context context) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        progressDialog.dismiss();

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.askdialog, null);
        ProgressBar pb_progress_bar = v.findViewById(R.id.pb_progress_bar);
        ConstraintLayout down_dialog = v.findViewById(R.id.ask_dialog);
        showing = v.findViewById(R.id.textView4);
        Button bn = v.findViewById(R.id.button1);
        Button by = v.findViewById(R.id.button2);

        sendValue = new SendValue(mBluetoothLeService);

        bn.setText(getString(R.string.mes_no));
        by.setText(getString(R.string.mes_yes));
        pb_progress_bar.setVisibility(View.GONE);

        bn.setOnClickListener(v12 -> {
            vibrator.vibrate(100);
            Value.downloading = false;
            if (Logdata.size() == totle) {
                ask_Dialog.dismiss();
            } else {
                if (mBluetoothLeService.connect(Value.BID)) {
                    sendValue = new SendValue(mBluetoothLeService);
                    ask_Dialog.dismiss();
                } else {
                    ask_Dialog.dismiss();
                }
            }
        });

        by.setOnClickListener(v1 -> {
            try {
                vibrator.vibrate(100);
                pb_progress_bar.setVisibility(View.VISIBLE);
                if (mBluetoothLeService.connect(Value.BID)) {
                    Toast.makeText(DeviceFunction.this, getString(R.string.prepare), Toast.LENGTH_SHORT).show();
                    Value.downloading = true;
                    test = 0;
                    sendValue.send("END");
                    sleep(100);
                    sendValue.send("Delay00010");
                    sleep(100);
                    if (data_Json.getCount() > 0) {
                        data_Json.delete(Value.BID);
                        Log.e(TAG, "刪除紀錄 = " + data_Json.getCount());
                        data_Json.close();
                    }
                } else {
                    /*connectThread = new ConnectThread();
                    connectThread.run();*/
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        progressDialog.setContentView(down_dialog, new ConstraintLayout.LayoutParams((int) (2 * Value.all_Width / 3),
                (int) (2 * Value.all_Height / 5)));

        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            vibrator.vibrate(100);
            sendValue.send("STOP");
            return false;
        });
        return progressDialog;
    }

    private Dialog loadDialog(Context context, List<String> loadlist) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        sendValue = new SendValue(mBluetoothLeService);
        Log.e(TAG, "loadlist = " + loadlist);
        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.loading, null);
        ConstraintLayout setlinear = v.findViewById(R.id.loadlist);
        Button by = v.findViewById(R.id.button2);
        Button bn = v.findViewById(R.id.button1);
        ListView list = v.findViewById(R.id.loading);
        TextView t1 = v.findViewById(R.id.no_list);

        if (data_table.getCount() == 0) {
            list.setVisibility(View.GONE);
            t1.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            t1.setVisibility(View.GONE);
            listData = data_table.fillList();
            dataList = new DataList(this, listData, Value.all_Width);
            list.setAdapter(dataList);
            list.setOnItemClickListener(mLoadClickListener);
        }

        by.setOnClickListener(v12 -> {
            vibrator.vibrate(100);
            int j = 0, k = 0, l = 0;
            JSONArray a = data_table.getJSON(dataList.getItem(select_item));
            if (Value.SelectItem.indexOf("DP1") != -1) {
                j = Value.SelectItem.indexOf("DP1");
                try {
                    sendValue.send(a.get(j).toString());
                    sleep(500);
                } catch (InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
            if (Value.SelectItem.indexOf("DP2") != -1) {
                k = Value.SelectItem.indexOf("DP2");
                try {
                    sendValue.send(a.get(k).toString());
                    sleep(500);
                } catch (InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
            if (Value.SelectItem.indexOf("DP3") != -1) {
                l = Value.SelectItem.indexOf("DP3");
                try {
                    sendValue.send(a.get(l).toString());
                    sleep(500);
                } catch (InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < a.length(); i++) {
                if (i != 0 && i != j && i != k && i != l) {
                    //Log.e(TAG, "a.get(i) = " + a.get(i));
                    try {
                        sendValue.send(a.get(i).toString());
                        sleep(500);
                    } catch (InterruptedException ignored) {
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } /*else {
                    String dpp = a.get(i);
                    dpp = dpp.substring(dpp.indexOf("+") + 1, dpp.length());
                    Log.e(TAG, "dpp = " + dpp);
                    if (dpp.matches("Off")) {
                        try {
                            Log.e(TAG, "send = " + send);
                            Log.e(TAG, "sending = " + "DPP+0000.0");
                            sendValue.send("DPP+0000.0");
                            sleep(50);
                            sendValue.send(get_fun);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Log.e(TAG, "send = " + send);
                            Log.e(TAG, "sending = " + "DPP+0001.0");
                            sendValue.send("DPP+0001.0");
                            sleep(50);
                            sendValue.send(get_fun);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }*/
            }
            showDialog.dismiss();
        });

        bn.setOnClickListener(v1 -> {
            vibrator.vibrate(100);
            showDialog.dismiss();
        });

        progressDialog.setContentView(setlinear, new ConstraintLayout.LayoutParams((int) (3 * Value.all_Width / 4),
                (int) (5 * Value.all_Height / 7)));

        return progressDialog;
    }


    private AdapterView.OnItemClickListener mLoadClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            //一開始未選擇任何一個item所以為-1
            //======================
            //點選某個item並呈現被選取的狀態
            if ((select_item == -1) || (select_item == position)) {
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            } else {
                //noinspection deprecation
                view1.setBackgroundDrawable(null); //將上一次點選的View保存在view1
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }
            view1 = view; //保存點選的View
            select_item = position;//保存目前的View位置
            //======================
            if (SQLdata.size() == 0) {
                for (HashMap<String, String> map : listData) {
                    SQLdata.add(map.get("id"));
                }
            } else {
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

    private Dialog dataDialog(Context context, List<String> savelist) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Log.e(TAG, "savelist" + savelist);
        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.savedatalist, null);
        ConstraintLayout setlinear = v.findViewById(R.id.savelist);
        Button close = v.findViewById(R.id.button1);
        EditText name = v.findViewById(R.id.editText);
        Button add = v.findViewById(R.id.button2);
        Button del = v.findViewById(R.id.button3);
        Button update = v.findViewById(R.id.button4);
        list = v.findViewById(R.id.datalist1);
        TextView t1 = v.findViewById(R.id.no_list);

        if (data_table.getCount() == 0) {
            list.setVisibility(View.GONE);
            t1.setVisibility(View.VISIBLE);
        } else {
            select_item = -1;
            list.setVisibility(View.VISIBLE);
            t1.setVisibility(View.GONE);
            listData = data_table.fillList();
            dataList = new DataList(this, listData, Value.all_Width);
            list.setAdapter(dataList);
            list.setOnItemClickListener(mListClickListener);
        }

        close.setOnClickListener(v14 -> {
            vibrator.vibrate(100);
            saveDialog.dismiss();
        });

        add.setOnClickListener(v13 -> {
            vibrator.vibrate(100);
            String listname = name.getText().toString().trim();
            if (listname.matches(""))
                Toast.makeText(DeviceFunction.this, getString(R.string.addblank), Toast.LENGTH_SHORT).show();
            else {
                if (data_table.getCount(listname) == 0) {
                    JSONArray DataSave = new JSONArray(Value.DataSave);
                    data_table.insert(DataSave, listname);
                    SQLdata.clear();
                    list.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.GONE);
                    name.setText("");
                    try {
                        sleep(100);
                        listData = data_table.fillList();
                        dataList = new DataList(DeviceFunction.this, listData, Value.all_Width);
                        list.setAdapter(dataList);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DeviceFunction.this, getString(R.string.same), Toast.LENGTH_SHORT).show();
                }
            }
        });

        del.setOnClickListener(v12 -> {
            vibrator.vibrate(100);
            Log.e(TAG, "select = " + select_item);
            if (select_item != -1) {
                data_table.delete(Integer.valueOf(SQLdata.get(select_item)));
                SQLdata.remove(select_item);
                Log.e(TAG, "SQLdata = " + SQLdata);
                if (data_table.getCount() == 0) {
                    list.setVisibility(View.GONE);
                    t1.setVisibility(View.VISIBLE);
                    select_item = -1;
                } else {
                    listData = data_table.fillList();
                    dataList = new DataList(DeviceFunction.this, listData, Value.all_Width);
                    list.setAdapter(dataList);
                    select_item = -1;
                }
            }
        });

        update.setOnClickListener(v1 -> {
            vibrator.vibrate(100);
            if (data_table.getCount() != 0) {
                if (select_item != -1) {
                    upDialog = updateDialog(DeviceFunction.this);
                    upDialog.show();
                    upDialog.setCanceledOnTouchOutside(false);
                }
            }
        });

        progressDialog.setContentView(setlinear, new ConstraintLayout.LayoutParams((int) (3 * Value.all_Width / 4),
                (int) (5 * Value.all_Height / 7)));

        return progressDialog;
    }

    private Dialog updateDialog(final Context context) {

        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(DeviceFunction.this);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.updatename, null);
        LinearLayout setlinear = v.findViewById(R.id.uplist);
        Button by = v.findViewById(R.id.button2);
        Button bn = v.findViewById(R.id.button1);
        EditText ed1 = v.findViewById(R.id.editText1);

        by.setOnClickListener(v1 -> {
            vibrator.vibrate(100);
            String getname = ed1.getText().toString().trim();
            if (getname.matches("")) {
                Toast.makeText(DeviceFunction.this, getString(R.string.addblank), Toast.LENGTH_SHORT).show();
            } else {
                data_table.update(Integer.valueOf(SQLdata.get(select_item)), getname);
                listData = data_table.fillList();
                dataList = new DataList(DeviceFunction.this, listData, Value.all_Width);
                list.setAdapter(dataList);
                select_item = -1;
                upDialog.dismiss();
            }
        });

        bn.setOnClickListener(v12 -> {
            //select_item = -1;
            vibrator.vibrate(100);
            upDialog.dismiss();
        });

        progressDialog.setContentView(setlinear, new LinearLayout.LayoutParams((int) (3 * Value.all_Width / 5),
                (int) (Value.all_Height / 5)));

        return progressDialog;
    }

    private AdapterView.OnItemClickListener mListClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            //一開始未選擇任何一個item所以為-1
            //======================
            //點選某個item並呈現被選取的狀態
            if ((select_item == -1) || (select_item == position)) {
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            } else {
                //noinspection deprecation
                view1.setBackgroundDrawable(null); //將上一次點選的View保存在view1
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }
            view1 = view; //保存點選的View
            select_item = position;//保存目前的View位置
            //======================
            if (SQLdata.size() == 0) {
                for (HashMap<String, String> map : listData) {
                    SQLdata.add(map.get("id"));
                }
                Log.e(TAG, "SQLdata = " + SQLdata);
            } else {
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
                Log.e(TAG, "連線中斷" + Value.connected);
                if (Value.connected) {
                    try {
                        new Thread(connectfail).start();
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
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendValue = new SendValue(mBluetoothLeService);
                    if (!Value.downloading) {
                        ConnectThread reThread = new ConnectThread(connectHandler);
                        reThread.run();
                        if (ask_Dialog != null) {
                            ask_Dialog.dismiss();
                        }
                    } else {
                        Value.connected = false;
                        new Thread(connectfail).start();
                        Log.e(TAG, "走到這");
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ConnectThread reThread = new ConnectThread(connectHandler);
                        reThread.run();
                    }
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                runOnUiThread(() -> {
                    try {
                        txValue = intents.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                        text = new String(txValue, "UTF-8");
                        Log.e(TAG, "text = " + text);
                        if (!Value.downloading) {
                            if (text.startsWith("NAME")) {    //Nordic_UART
                                List_d_num.set(0, text.substring(4));
                                Value.DataSave.set(0, text.substring(4));
                                function.notifyDataSetChanged();
                            } else if (!text.matches("OVER") && !text.startsWith("END") &&
                                    !text.startsWith("START")) {
                                if (!(text.startsWith("COUNT") || text.startsWith("DATE") ||
                                        text.startsWith("TIME") || text.matches("LOGON") ||
                                        text.matches("LOGOFF") || text.startsWith("LOG"))) {

                                    int i = Value.SelectItem.indexOf(checkDeviceName.setName(text));
                                    Value.return_RX.set((i - 1), text);
                                    Value.DataSave.set(i, text);
                                    List_d_num.set(i, getDeviceNum.get(Value.return_RX.get(i - 1)));
                                    Log.e(TAG, "i = " + i);
                                    Log.e(TAG, "return_RX= " + Value.return_RX);
                                    Log.e(TAG, "List_d_num = " + List_d_num);
                                    Log.e(TAG, "DataSave = " + Value.DataSave);
                                    getM();
                                    function.notifyDataSetChanged();
                                }
                                else if (text.startsWith("COUNT")) {
                                    Log.e(TAG, "停止紀錄 = " + text);
                                    Value.downlog = false;
                                    if (!Value.downlog) {
                                        navigationView.getMenu().findItem(R.id.nav_share).setTitle(getString(R.string.start) + getString(R.string.LOG));
                                    } else {
                                        navigationView.getMenu().findItem(R.id.nav_share).setTitle(getString(R.string.end) + getString(R.string.LOG));
                                    }
                                }
                            }
                        } else {
                            if (text.startsWith("OK")) {
                                sendValue = new SendValue(mBluetoothLeService);
                                sendValue.send("DOWNLOAD");
                                /*sendLog = new SendLog();
                                sendLog.set_over(true);
                                sendLog.set_Service(mBluetoothLeService);*/
                            } else if (text.startsWith("COUNT")) {
                                Value.Count = text.substring(text.indexOf(Value.Jsonlist.get(check)) + 6, text.length());
                                totle = Integer.valueOf(Value.Count);
                                showtext = String.valueOf((test)) + " / " + String.valueOf(totle);
                            } else if (text.startsWith("OVER")) {
                                //sendLog.interrupt();
                                Log.e(TAG, "Logdata.size() = " + Logdata.size());
                                if (Logdata.size() == totle) {
                                    Value.downloading = false;
                                    jsonflag = 0;
                                    new Thread(analysislog).start();
                                    for (; jsonflag == 0; ) {
                                        try {
                                            sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    new Thread(maketime).start();
                                    for (; jsonflag == 1; ) {
                                        try {
                                            sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    new Thread(logSQL).start();
                                    for (; jsonflag == 2; ) {
                                        try {
                                            sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    ask_Dialog.dismiss();
                                    //unbindService(mServiceConnection);
                                }
                            } else if (text.startsWith("Delay")) {
                                Log.e(TAG, "Delaytime = " + text);
                                timelist.clear();
                                charttime.clear();
                                Logdata.clear();
                                Firstlist.clear();
                                Secondlist.clear();
                                Thirdlist.clear();
                                Value.connected = true;
                                Service_close();
                            } else if (text.startsWith("DATE")) {
                                Value.Date = text.substring(4, text.length());
                            } else if (text.startsWith("TIME")) {
                                Value.Time = text.substring(4, text.length());
                            } else if (text.startsWith("INTER")) {
                                Value.Inter = text.substring(5, text.length());
                            } else if (text.startsWith("+") || text.startsWith("-")) {
                                if (Value.modelsign == 1) {
                                    new Thread(down_log).start();
                                } else if (Value.modelsign == 2) {
                                    new Thread(down_log).start();   //showtext();
                                } else if (Value.modelsign == 3) {
                                    new Thread(down_log).start();
                                }
                            }
                        }
                        /*if (send == 0 || send == 1) {
                            if (text.matches("OVER")) {
                                    check = check + 1;
                                    Log.e(TAG, "checkOVER = " + text);
                                    Log.e(TAG, "check = " + check);
                                    Log.e(TAG, "RX = " + Value.return_RX);
                                    show_device_function();
                                    progressDialog2.dismiss();
                                } else {
                                    if (text.startsWith(Jsonlist.get(check))) {
                                        if (Value.SelectItem != null && Value.SelectItem.size() > 0 && Value.return_RX != null && Value.DataSave != null) {
                                            if(!(text.startsWith("COUNT") || text.startsWith("DATE") || text.startsWith("TIME") || text.startsWith("LOG"))) {
                                                Value.SelectItem.add(checkDeviceName.setName(text));
                                                Value.return_RX.add(text);
                                                Value.DataSave.add(text);
                                            }
                                            else {
                                                //Log.e(TAG, "額外處理");
                                                //getString(text);
                                            }
                                            check = check + 1;
                                            Log.e(TAG, "check = " + Jsonlist.get(check));
                                        }
                                    }
                                }
                            }
                            else if (send == 3) {
                                if (text.matches("OVER")) {
                                    Log.e(TAG, "checkOVER = " + text);
                                    if (checkDeviceName.setName(text).matches(Jsonlist.get(check))) {
                                        Log.e(TAG, "RX = " + Value.return_RX);
                                        show_device_function();
                                    }
                                } else {
                                    if (checkDeviceName.setName(text).matches(Jsonlist.get(check))) {
                                        Value.SelectItem.set(check + 1, checkDeviceName.setName(text));
                                        Value.return_RX.set(check, text);
                                        Value.DataSave.set(check + 1, text);
                                        check = check + 1;
                                        Log.e(TAG, "check = " + Jsonlist.get(check));
                                        Log.e(TAG, "SelectItem = " + Value.SelectItem);
                                        Log.e(TAG, "return_RX = " + Value.return_RX);
                                        Log.e(TAG, "DataSave = " + Value.DataSave);
                                    }
                                }
                            }
                            else if(send == 2){
                                if (text.startsWith("NAME")) {    //Nordic_UART
                                    List_d_num.set(0, text.substring(4));
                                    Value.DataSave.set(0, text.substring(4));
                                    function.notifyDataSetChanged();
                                }
                                else {
                                    if(!text.matches("OVER")) {
                                        int i = Value.SelectItem.indexOf(checkDeviceName.setName(text));
                                        Log.e(TAG, "i = " + i + "\n" + "text = " + text);
                                        Value.return_RX.set((i - 1), text);
                                        Value.DataSave.set(i, text);
                                        List_d_num.set(i, getDeviceNum.get(Value.return_RX.get(i - 1)));
                                        Log.e(TAG, "i = " + i);
                                        Log.e(TAG, "SelectItem = " + Value.SelectItem);
                                        Log.e(TAG, "return_RX= " + Value.return_RX);
                                        Log.e(TAG, "List_d_num = " + List_d_num);
                                        Log.e(TAG, "DataSave = " + Value.DataSave);
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
                                    //noinspection deprecation
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
                                    Value.P_word = text.substring(text.indexOf("=") + 1,text.length());
                                    Log.e(TAG, "P_word = " + Value.P_word);
                                }
                            }*/
                    } catch (UnsupportedEncodingException /*| InterruptedException | JSONException*/ e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            (device, rssi, scanRecord) -> runOnUiThread(() -> runOnUiThread(this::addDevice));

    private void addDevice() {
    }

    private Runnable connectfail = () -> unbindService(mServiceConnection);

    private Runnable sendcheck = () -> {
        try {
            sleep(500);
            sendValue = new SendValue(mBluetoothLeService);
            String jetec = "Jetec";
            Log.e(TAG, "sends = " + jetec);
            if (s_connect)
                sendValue.send(jetec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    private void login() {
        check = 0;
        send = 0;
        sendValue = new SendValue(mBluetoothLeService);
        Value.SelectItem.add("NAME");
        Value.DataSave.add(Value.deviceModel);
        sendValue.send(get_fun);
        if (progressDialog2 == null) {
            progressDialog2 = writeDialog(DeviceFunction.this, getString(R.string.login));
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
                sendValue = new SendValue(mBluetoothLeService);
                Log.e(TAG, "check = " + check);
                Log.e(TAG, "Jsonlist = " + Jsonlist.get(check));
                //noinspection StatementWithEmptyBody
                if (Jsonlist.get(check).matches("OVER")) {
                } else {
                    connect_flag = 4;
                    if (progressDialog2 != null)
                        progressDialog2.dismiss();
                    sendValue.send("STOP");
                    //noinspection deprecation
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Service_close();
                }
            } catch (InterruptedException e) {
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
            getcount = getcount.substring(getcount.indexOf("COU") + 3, getcount.length()).replaceFirst("^0*", "");
            Log.e(TAG, "getcount = " + getcount);
        }
    };

    private Runnable uptime = new Runnable() {
        @Override
        public void run() {
            gettime = gettime.substring(gettime.indexOf("INT") + 3, gettime.length()).replaceFirst("^0*", "");
            Log.e(TAG, "gettime = " + gettime);
        }
    };

    private Runnable down_log = new Runnable() {
        @Override
        public void run() {
            Message m2 = new Message();
            m2.obj = 2;
            downloading.sendMessage(m2);
        }
    };

    private void showtext() {
        test++;
        Logdata.add(text);
        showtext = String.valueOf(test) + " / " + String.valueOf(totle);
        showing.setText(showtext);
        Log.e(TAG, "test = " + test);
    }

    @SuppressLint("HandlerLeak")
    Handler downloading = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            test++;
            Logdata.add(text);
            showtext = String.valueOf(test) + " / " + String.valueOf(totle);
            showing.setText(showtext);
            Log.e(TAG, "test = " + test);
            /*if (Logdata.size() == totle) {
                connect_flag = 4;
                new Thread(analysislog).start();
                for (; jsonflag == 0; ) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(maketime).start();
                for (; jsonflag == 1; ) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(logSQL).start();
            }*/
        }
    };

    private Runnable analysislog = new Runnable() {
        @Override
        public void run() {
            if (Value.modelsign == 1) {
                for (int i = 0; i < Logdata.size(); i++) {
                    String firstrow = Logdata.get(i);
                    int math = (int) Math.pow(10, Integer.valueOf(firstrow.substring(firstrow.length() - 1, firstrow.length())));
                    if (firstrow.startsWith("+")) {
                        firstrow = firstrow.substring(1, firstrow.length() - 1);
                        Float num = Float.valueOf(firstrow) / math;
                        Firstlist.add(String.valueOf(num));
                    } else {
                        firstrow = firstrow.substring(0, firstrow.length() - 1);
                        Float num = Float.valueOf(firstrow) / math;
                        Firstlist.add(String.valueOf(num));
                    }
                }
            } else if (Value.modelsign == 2) {
                for (int i = 0; i < Logdata.size(); i++) {
                    String getvalue = Logdata.get(i);
                    String firstrow = getvalue.substring(0, 6);
                    String secondrow = getvalue.substring(6, getvalue.length());
                    int math = (int) Math.pow(10, Integer.valueOf(firstrow.substring(firstrow.length() - 1, firstrow.length())));
                    int math2 = (int) Math.pow(10, Integer.valueOf(secondrow.substring(firstrow.length() - 1, secondrow.length())));
                    if (firstrow.startsWith("+")) {
                        firstrow = firstrow.substring(1, 5);
                        Float num = Float.valueOf(firstrow) / math;
                        Firstlist.add(String.valueOf(num));
                    } else {
                        firstrow = firstrow.substring(0, 5);
                        Float num = Float.valueOf(firstrow) / math;
                        Firstlist.add(String.valueOf(num));
                    }
                    if (secondrow.startsWith("+")) {
                        Log.e(TAG, "secondrow = " + secondrow);
                        secondrow = secondrow.substring(1, 5);
                        Float num = Float.valueOf(secondrow) / math2;
                        Secondlist.add(String.valueOf(num));
                    } else {
                        secondrow = secondrow.substring(0, 5);
                        Float num = Float.valueOf(secondrow) / math2;
                        Secondlist.add(String.valueOf(num));
                    }
                }
            } else if (Value.modelsign == 3) {
                for (int i = 0; i < Logdata.size(); i++) {
                    String getvalue = Logdata.get(i);
                    String firstrow = getvalue.substring(0, 6);
                    String secondrow = getvalue.substring(6, 12);
                    String thirdrow = getvalue.substring(12, getvalue.length());
                    int math = (int) Math.pow(10, Integer.valueOf(firstrow.substring(firstrow.length() - 1, firstrow.length())));
                    int math2 = (int) Math.pow(10, Integer.valueOf(secondrow.substring(firstrow.length() - 1, secondrow.length())));
                    int math3 = (int) Math.pow(10, Integer.valueOf(thirdrow.substring(firstrow.length() - 1, thirdrow.length())));
                    if (firstrow.startsWith("+")) {
                        firstrow = firstrow.substring(1, 5);
                        Float num = Float.valueOf(firstrow) / math;
                        Firstlist.add(String.valueOf(num));
                    } else {
                        firstrow = firstrow.substring(0, 5);
                        Float num = Float.valueOf(firstrow) / math;
                        Firstlist.add(String.valueOf(num));
                    }
                    if (secondrow.startsWith("+")) {
                        secondrow = secondrow.substring(1, 5);
                        Float num = Float.valueOf(secondrow) / math2;
                        Secondlist.add(String.valueOf(num));
                    } else {
                        secondrow = secondrow.substring(0, 5);
                        Float num = Float.valueOf(secondrow) / math2;
                        Secondlist.add(String.valueOf(num));
                    }
                    if (thirdrow.startsWith("+")) {
                        thirdrow = thirdrow.substring(1, 5);
                        Float num = Float.valueOf(thirdrow) / math3;
                        Secondlist.add(String.valueOf(num));
                    } else {
                        thirdrow = thirdrow.substring(0, 5);
                        Float num = Float.valueOf(thirdrow) / math3;
                        Secondlist.add(String.valueOf(num));
                    }
                }
            }
            jsonflag = 1;
        }
    };

    private Runnable maketime = new Runnable() {
        @Override
        public void run() {
            try {
                charttime.clear();
                String formattime;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat log_date = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                String d_date = Value.Date;
                d_date = d_date.substring(0, 2) + "-" +
                        d_date.substring(2, 4) + "-" + d_date.substring(4, 6);
                String d_time = Value.Time;
                d_time = d_time.substring(0, 2) + ":" +
                        d_time.substring(2, 4) + ":" + d_time.substring(4, 6);
                String all_date = d_date + " " + d_time;
                Date date = log_date.parse(all_date);
                for (int i = Logdata.size(); i > 0; i--) {
                    date.setTime(date.getTime() + (Integer.valueOf(Value.Inter) * 1000));
                    formattime = log_date.format(date);
                    charttime.add(formattime);
                    formattime = formattime.substring(3, formattime.indexOf(" ")) + " " +
                            formattime.substring(formattime.indexOf(" ") + 1, formattime.length() - 3);
                    timelist.add(formattime);
                }
                Log.e(TAG, "timelist.size() = " + timelist.size());
                Log.e(TAG, "timelist = " + timelist);
                jsonflag = 2;
            } catch (ParseException e) {
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
                //if(Firstlist.size() != 0) {
                Firstjson = new JSONArray(Firstlist);
                sleep(30);
                //}
                //if(Secondlist.size() != 0) {
                Secondjson = new JSONArray(Secondlist);
                sleep(30);
                //}
                //if(Thirdlist.size() != 0){
                Log.e(TAG, "Thirdlist = " + Thirdlist.size());
                Thirdjson = new JSONArray(Thirdlist);
                sleep(30);
                //}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "charttime = " + charttime);
            Log.e(TAG, "Chart_json = " + Chart_json);

            data_Json.insert(Chart_json, TimeList_json, Firstjson,
                    Secondjson, Thirdjson, Value.BID);
            jsonflag = 3;
        }
    };

    private Dialog writeDialog(Context context, String message) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        progressDialog.dismiss();

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

        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            vibrator.vibrate(100);
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                //noinspection deprecation
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                if (checkpassword != null)
                    checkpassword.dismiss();
                return false;
            } else {
                return false;
            }
        });
        return progressDialog;
    }

    public void Service_close() {
        if (mBluetoothLeService == null) {
            Log.e(TAG, "masaga");
            return;
        }
        mBluetoothLeService.disconnect();
    }

    private void intentlogview() {

        Intent intent = new Intent(DeviceFunction.this, LogChartView.class);

        Value.charttime = charttime;
        Value.List_d_num = List_d_num;
        Value.timelist = timelist;
        Value.Firstlist = Firstlist;
        Value.Secondlist = Secondlist;
        Value.Thirdlist = Thirdlist;

        Log.e(TAG, "Firstlist = " + Firstlist);
        Log.e(TAG, "Secondlist = " + Secondlist);
        Log.e(TAG, "Thirdlist = " + Thirdlist);
        Log.e(TAG, "List_d_num = " + List_d_num);
        Log.e(TAG, "SelectItem = " + Value.SelectItem);

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
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

    private void getM() {
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

    private void disconnect() {
        Intent intent = new Intent(this, MainActivity.class);
        //Value.Jsonlist.clear();
        startActivity(intent);
        finish();
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
            connect_flag = 2;
            if (mBluetoothAdapter != null)
                //noinspection deprecation
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Service_close();
            Value.connected = false;
            data_table.close();
            disconnect();
            return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        // setContentView(R.layout.device_function);
        int id = item.getItemId();
        vibrator.vibrate(100);
        sendValue = new SendValue(mBluetoothLeService);

        if (!Value.downlog) {
            navigationView.getMenu().findItem(R.id.nav_share).setTitle(getString(R.string.start) + getString(R.string.LOG));
        } else {
            navigationView.getMenu().findItem(R.id.nav_share).setTitle(getString(R.string.end) + getString(R.string.LOG));
        }

        if (!Value.btn) {
            navigationView.getMenu().findItem(R.id.datadownload).setEnabled(false);
            SpannableString spanString1 = new SpannableString(navigationView.getMenu().
                    findItem(R.id.datadownload).getTitle().toString());
            spanString1.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanString1.length(), 0);
            navigationView.getMenu().findItem(R.id.datadownload).setTitle(spanString1);
            navigationView.getMenu().findItem(R.id.showdialog).setEnabled(false);
            SpannableString spanString2 = new SpannableString(navigationView.getMenu().
                    findItem(R.id.showdialog).getTitle().toString());
            spanString2.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanString2.length(), 0);
            navigationView.getMenu().findItem(R.id.showdialog).setTitle(spanString2);
            navigationView.getMenu().findItem(R.id.nav_share).setEnabled(false);
            SpannableString spanString3 = new SpannableString(navigationView.getMenu().
                    findItem(R.id.nav_share).getTitle().toString());
            spanString3.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanString3.length(), 0);
            navigationView.getMenu().findItem(R.id.nav_share).setTitle(spanString3);
        }

        if (id == R.id.savedialog) {
            vibrator.vibrate(100);
            saveDialog = dataDialog(DeviceFunction.this, Value.DataSave);
            saveDialog.show();
            saveDialog.setCanceledOnTouchOutside(false);
        } else if (id == R.id.loadbar) {
            vibrator.vibrate(100);
            showDialog = loadDialog(DeviceFunction.this, Value.DataSave);
            showDialog.show();
            showDialog.setCanceledOnTouchOutside(false);
        } else if (id == R.id.datadownload) {
            vibrator.vibrate(100);
            new AlertDialog.Builder(DeviceFunction.this)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.stoprecord)
                    .setPositiveButton(R.string.butoon_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Value.downloading = true;
                            sendValue.send("END");
                            ask_Dialog = askDialog(DeviceFunction.this);
                            ask_Dialog.show();
                            ask_Dialog.setCanceledOnTouchOutside(false);
                        }
                    })
                    .setNegativeButton(R.string.butoon_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Value.downloading = false;
                            Log.e(TAG, "取消下載");
                        }
                    })
                    .show();

        } else if (id == R.id.showdialog) {
            vibrator.vibrate(100);
            if (timelist.size() == 0) {
                Log.e(TAG, "charttime.size() = " + timelist.size());
                Toast.makeText(DeviceFunction.this, getString(R.string.logdata), Toast.LENGTH_SHORT).show();
            } else {
                requeststorage();
            }
        } else if (id == R.id.modifypassword) {
            vibrator.vibrate(100);
            modifyPassword = new ModifyPassword(DeviceFunction.this, Value.all_Width,
                    Value.all_Height, Value.P_word,
                    Value.G_word, Value.E_word, Value.I_word, gettoast1, gettoast2, gettoast3, gettoast4,
                    gettoast5, gettoast6, mBluetoothLeService);
            modifyPassword.modifyDialog(vibrator);
        } else if (id == R.id.nav_share) {
            if (!Value.downlog) {
                try {
                    Value.loading = true;
                    Value.downlog = true;
                    if (!Value.downlog) {
                        navigationView.getMenu().findItem(R.id.nav_share).setTitle(getString(R.string.start) + getString(R.string.LOG));
                    } else {
                        navigationView.getMenu().findItem(R.id.nav_share).setTitle(getString(R.string.end) + getString(R.string.LOG));
                    }
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat get_date = new SimpleDateFormat("yyMMdd");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat get_time = new SimpleDateFormat("hhmmss");
                    Date date = new Date();
                    String strDate = get_date.format(date);
                    String strtime = get_time.format(date);
                    sendValue.send(strDate);
                    sleep(100);
                    sendValue.send(strtime);
                    sleep(100);
                    Log.e(TAG, "Value.return_RX = " + Value.return_RX.get(Value.SelectItem.indexOf("INTER") - 1));
                    sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("INTER") - 1));
                    sleep(100);
                    sendValue.send("START");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Value.loading = false;
                sendValue.send("END");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            data_table.close();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            Value.all_Width = dm.widthPixels;
            Value.all_Height = dm.heightPixels;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            Value.all_Width = dm.widthPixels;
            Value.all_Height = dm.heightPixels;
        }
    }
}
