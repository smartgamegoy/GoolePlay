package com.jetec.nordic_googleplay.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.text.InputType;
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

public class ModifyPassword {

    private Context context;
    private double all_Width, all_Height;
    private LayoutInflater inflater;
    private String P_word, G_word, E_word, I_word, newpassword,toast1, toast2, toast3, toast4, toast5, toast6;
    private BluetoothLeService bluetoothLeService;

    public ModifyPassword(Context context, double all_Width, double all_Height, String P_word, String G_word,
                          String E_word, String I_word, String toast1, String toast2, String toast3,
                          String toast4, String toast5, String toast6, BluetoothLeService bluetoothLeService) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.all_Width = all_Width;
        this.all_Height = all_Height;
        this.P_word = P_word;
        this.G_word = G_word;
        this.E_word = E_word;
        this.I_word = I_word;
        this.toast1 = toast1;
        this.toast2 = toast2;
        this.toast3 = toast3;
        this.toast4 = toast4;
        this.toast5 = toast5;
        this.toast6 = toast6;
        this.bluetoothLeService = bluetoothLeService;
    }

    public Dialog modifyDialog(Vibrator vibrator) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.modifypassword, null);
        ConstraintLayout modifylist = v.findViewById(R.id.modifypassword);
        EditText e1 = v.findViewById(R.id.editText1);
        EditText e2 = v.findViewById(R.id.editText2);   //android:digits="0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@"
        Button by = v.findViewById(R.id.button2);
        Button bn = v.findViewById(R.id.button1);

        e1.setKeyListener(DigitsKeyListener.getInstance(".,$%&^!()-_=+';:|}{[]*→←↘↖、，。?~～#€￠" +
                "￡￥abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@>/<"));
        //e1.setKeyListener(DigitsKeyListener.getInstance("abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789."));
        e1.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        e2.setKeyListener(DigitsKeyListener.getInstance(".,$%&^!()-_=+';:|}{[]*→←↘↖、，。?~～#€￠" +
                "￡￥abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@>/<"));
        //e1.setKeyListener(DigitsKeyListener.getInstance("abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789."));
        e2.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        by.setOnClickListener(v12 -> {
            vibrator.vibrate(100);
            if(e1.getText().toString().trim().matches(P_word)) {
                if (e2.getText().toString().trim().matches(e1.getText().toString().trim()))
                    Toast.makeText(context, toast1, Toast.LENGTH_SHORT).show();
                else if (e2.getText().toString().trim().matches(G_word))
                    Toast.makeText(context, toast2, Toast.LENGTH_SHORT).show();
                else if (e2.getText().toString().trim().matches(E_word))
                    Toast.makeText(context, toast3, Toast.LENGTH_SHORT).show();
                else if(e2.getText().toString().trim().matches(I_word))
                    Toast.makeText(context, toast3, Toast.LENGTH_SHORT).show();
                else if(e2.getText().toString().trim().matches("")){
                    Toast.makeText(context, toast6, Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        newpassword = e2.getText().toString().trim();
                        if(newpassword.length() == 6) {
                            Toast.makeText(context, toast4, Toast.LENGTH_SHORT).show();
                            sending(newpassword);
                            Log.e("ModifyPassword", "success = " + newpassword);
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(context, toast6, Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
                Toast.makeText(context, toast5, Toast.LENGTH_SHORT).show();
        });

        bn.setOnClickListener(v1 -> {
            vibrator.vibrate(100);
            progressDialog.dismiss();
        });

        if(all_Height > all_Width) {
            progressDialog.setContentView(modifylist, new ConstraintLayout.LayoutParams((int) (3 * all_Width / 5),
                    (int) (all_Height / 2)));
        }
        else {
            progressDialog.setContentView(modifylist, new ConstraintLayout.LayoutParams((int) (2 * all_Width / 5),
                    (int) (5 * all_Height / 6)));
        }

        return progressDialog;
    }

    private void sending(String value) throws UnsupportedEncodingException {
        String change = "PWR=" + value;
        byte[] sends;
        sends = change.getBytes("UTF-8");
        bluetoothLeService.writeRXCharacteristic(sends);
    }
}
