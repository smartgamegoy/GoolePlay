package com.jetec.nordic_googleplay.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interval {

    private Context context;
    private double all_Width, all_Height;
    private LayoutInflater inflater;
    private BluetoothLeService bluetoothLeService;
    private boolean c = false;
    private String description;

    public Interval(Context context, double all_Width, double all_Height,
                    BluetoothLeService bluetoothLeService, String description){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.all_Width = all_Width;
        this.all_Height = all_Height;
        this.bluetoothLeService = bluetoothLeService;
        this.description = description;
    }

    public void showDialog() {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.show();

        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.interval, null);
        ConstraintLayout constraint = v.findViewById(R.id.constraint);
        EditText e1 = v.findViewById(R.id.editText1);
        EditText e2 = v.findViewById(R.id.editText2);
        EditText e3 = v.findViewById(R.id.editText3);
        Button by = v.findViewById(R.id.button2);
        Button bn = v.findViewById(R.id.button1);

        by.setOnClickListener(v1 -> {
            String hour, minute, second;
            int ihour = 0, iminute = 0, isecond = 0, sum;
            Log.e("btn", "c = " + c);
            if(c) {
                if (!e1.getText().toString().trim().matches("")) {
                    hour = e1.getText().toString().trim();
                    ihour = Integer.valueOf(hour);
                }
                if (!e2.getText().toString().trim().matches("")) {
                    minute = e2.getText().toString().trim();
                    iminute = Integer.valueOf(minute);
                }
                if (!e3.getText().toString().trim().matches("")) {
                    second = e3.getText().toString().trim();
                    isecond = Integer.valueOf(second);
                }
                sum = ihour * 3600 + iminute * 60 + isecond;
                try {
                    if(sum <= 3600 /*&& sum >= 30*/) {
                        progressDialog.dismiss();
                        sending(String.valueOf(sum));
                    }
                    else {
                        Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
            }
        });

        bn.setOnClickListener(v12 -> progressDialog.dismiss());

        e1.setKeyListener(DigitsKeyListener.getInstance("01"));
        e1.setHint("0 or 1");
        e1.addTextChangedListener(new TextWatcher() {
            int l = 0;    //記錄字串删除字元之前，字串的長度
            int location = 0; //記錄光標位置
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                l = s.length();
                location = e1.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("^[0-1]$");    //^\-?[0-5](.[0-9])?$
                Matcher m = p.matcher(s.toString());
                c = m.find() || ("").equals(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        e2.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        e2.setHint("1 ~ 59");
        e2.addTextChangedListener(new TextWatcher() {
            int l = 0;    //記錄字串删除字元之前，字串的長度
            int location = 0; //記錄光標位置
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                l = s.length();
                location = e1.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("^[1-5]?[0-9]?$");    //^\-?[0-5](.[0-9])?$
                Matcher m = p.matcher(s.toString());
                c = m.find() || ("").equals(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        e3.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        e3.setHint("30 ~ 59");
        e3.addTextChangedListener(new TextWatcher() {
            int l = 0;    //記錄字串删除字元之前，字串的長度
            int location = 0; //記錄光標位置
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                l = s.length();
                location = e1.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("^[1-5]?[0-9]?$");    //^\-?[0-5](.[0-9])?$
                Matcher m = p.matcher(s.toString());
                c = m.find() || ("").equals(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        progressDialog.setContentView(constraint, new LinearLayout.LayoutParams((int)(3 * all_Width / 4),
                (int)(all_Height / 2)));

    }

    private void sending(String value) throws UnsupportedEncodingException {
        if(value.length() < 5) {
            int i = 5 - value.length();
            StringBuilder valueBuilder = new StringBuilder(value);
            for(int j = 0; j < i; j++){
                valueBuilder.insert(0, "0");
            }
            value = valueBuilder.toString();
            String change = "INTER" + value;
            byte[] sends;
            sends = change.getBytes("UTF-8");
            bluetoothLeService.writeRXCharacteristic(sends);
        }
    }
}
