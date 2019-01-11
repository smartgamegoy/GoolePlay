package com.jetec.nordic_googleplay.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.SendValue;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.VIBRATOR_SERVICE;
import static java.lang.Thread.sleep;

public class SetTime {

    private Context context;
    private double all_Width, all_Height;
    private Vibrator vibrator;
    private SendValue sendValue;
    private LayoutInflater inflater;

    public SetTime(Context context, double all_Width, double all_Height,
                   BluetoothLeService bluetoothLeService) {
        this.context = context;
        this.all_Width = all_Width;
        this.all_Height = all_Height;
        inflater = LayoutInflater.from(context);
        sendValue = new SendValue(bluetoothLeService);
    }

    public void setDialog() {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        vibrator = (Vibrator) this.context.getSystemService(VIBRATOR_SERVICE);
        @SuppressLint("InflateParams")
        View v = inflater.inflate(R.layout.time_set, null);
        ConstraintLayout constraint = v.findViewById(R.id.constraint);
        EditText e1 = v.findViewById(R.id.editText1);
        EditText e2 = v.findViewById(R.id.editText2);
        EditText e3 = v.findViewById(R.id.editText3);
        Button by = v.findViewById(R.id.button2);
        Button bn = v.findViewById(R.id.button1);
        Button bquick = v.findViewById(R.id.button3);

        bn.setOnClickListener(v12 -> {
            vibrator.vibrate(100);
            progressDialog.dismiss();
        });

        by.setOnClickListener(v13 -> {
            vibrator.vibrate(100);
            String hour = "", minute = "", second = "";
            if (!e1.getText().toString().trim().matches("")) {
                hour = e1.getText().toString().trim();
            }
            if (!e2.getText().toString().trim().matches("")) {
                minute = e2.getText().toString().trim();
            }
            if (!e3.getText().toString().trim().matches("")) {
                second = e3.getText().toString().trim();
            }

            String gettime = hour + ":" + minute + ":" + second;
            Boolean t = timecheck(gettime);
            if (t) {
                try {
                    String Time = hour + minute + second;
                    sendValue.send("TIME" + Time);
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            } else {
                Toast.makeText(context, context.getString(R.string.formatwrong), Toast.LENGTH_SHORT).show();
            }
        });

        bquick.setOnClickListener(v1 -> {
            vibrator.vibrate(100);
            fastset();
            progressDialog.dismiss();
        });

        e1.setHint("ex:05");
        e2.setHint("ex:07");
        e3.setHint("ex:00");

        progressDialog.setContentView(constraint, new LinearLayout.LayoutParams((int) (3 * all_Width / 4),
                (int) (3 * all_Height / 4)));
    }

    private void fastset() {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat get_time = new SimpleDateFormat("HHmmss"); //for 12hours use hhmmss
            Date date = new Date();
            String strtime = get_time.format(date);
            Log.e("fastset", "strtime = " + strtime);
            sendValue.send("TIME" + strtime);
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Boolean timecheck(String time) {
        boolean convertSuccess = true;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        if(time.length() != 8){
            convertSuccess = false;
        }
        else {
            try {
                format.setLenient(false);
                format.parse(time);
            } catch (ParseException e) {
                convertSuccess = false;
            }
        }
        return convertSuccess;
    }
}
