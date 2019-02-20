package com.jetec.nordic_googleplay.EditManagert;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditChangeName implements TextWatcher {

    private Context context;
    private EditText editText;
    private int len;

    public EditChangeName(Context context, EditText editText, int len) {
        this.context = context;
        this.editText = editText;
        this.len = len;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int len = 15;
        byte[] bytes = String.valueOf(editable).getBytes();
        if (bytes.length > len) {
            byte[] newBytes = new byte[len];
            for (int i = 0; i < len; i++) {
                newBytes[i] = bytes[i];
            }
            String newStr = new String(newBytes);
            editText.setText(newStr);
        }
    }
}
