package com.jetec.nordic_googleplay.Dialog;

import android.app.Dialog;
import android.content.Context;
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
    private String P_word, G_word, E_word, newpassword,toast1, toast2, toast3, toast4, toast5, toast6, toast7;
    private BluetoothLeService bluetoothLeService;

    public ModifyPassword(Context context, double all_Width, double all_Height, String P_word, String G_word,
                          String E_word, String toast1, String toast2, String toast3, String toast4, String toast5,
                          String toast6, String toast7, BluetoothLeService bluetoothLeService) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.all_Width = all_Width;
        this.all_Height = all_Height;
        this.P_word = P_word;
        this.G_word = G_word;
        this.E_word = E_word;
        this.toast1 = toast1;
        this.toast2 = toast2;
        this.toast3 = toast3;
        this.toast4 = toast4;
        this.toast5 = toast5;
        this.toast6 = toast6;
        this.toast7 = toast7;
        this.bluetoothLeService = bluetoothLeService;
    }

    public String modifyDialog() {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.show();

        View v = inflater.inflate(R.layout.modifypassword, null);
        LinearLayout modifylist = (LinearLayout)v.findViewById(R.id.modifylist);
        EditText e1 = (EditText) v.findViewById(R.id.editText1);
        EditText e2 = (EditText) v.findViewById(R.id.editText2);
        Button by = (Button)v.findViewById(R.id.button2);
        Button bn = (Button)v.findViewById(R.id.button1);

        by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.getText().toString().trim().matches(P_word)) {
                    if (e2.getText().toString().trim().matches(e1.getText().toString().trim()))
                        Toast.makeText(context, toast1, Toast.LENGTH_SHORT).show();
                    else if (e2.getText().toString().trim().matches(G_word))
                        Toast.makeText(context, toast2, Toast.LENGTH_SHORT).show();
                    else if (e2.getText().toString().trim().matches(E_word))
                        Toast.makeText(context, toast3, Toast.LENGTH_SHORT).show();
                    else if(e2.getText().toString().trim().matches("")){
                        Toast.makeText(context, toast6, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        try {
                            newpassword = e2.getText().toString().trim();
                            if(newpassword.length() > 5){
                                Toast.makeText(context, toast4, Toast.LENGTH_SHORT).show();
                                sending(newpassword);
                                Log.e("ModifyPassword", "success = " + newpassword);
                                progressDialog.dismiss();
                            }
                            else
                                Toast.makeText(context, toast7, Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                    Toast.makeText(context, toast5, Toast.LENGTH_SHORT).show();
            }
        });

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sending(P_word);
                    progressDialog.dismiss();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        progressDialog.setContentView(modifylist, new LinearLayout.LayoutParams((int)(3 * all_Width / 5),
                (int)(2 * all_Height / 7)));

        return newpassword;
    }

    private void sending(String value) throws UnsupportedEncodingException {
        String change = "PWR=" + value;
        byte[] sends;
        sends = change.getBytes("UTF-8");
        bluetoothLeService.writeRXCharacteristic(sends);
    }
}
