package com.jetec.nordic_googleplay;

import android.util.Log;

public class GetString {

    public GetString(){
        super();
    }

    public void set(String text, int check){
        String TAG = "GetString";
        if(text.startsWith("COUNT")) {
            Value.Count = text.substring(text.indexOf(Value.Jsonlist.get(check)) + 5, text.length());
            Log.e(TAG, "Count = " + Value.Count);
        }
        if(text.startsWith("TIME")) {
            Value.Time = text.substring(text.indexOf(Value.Jsonlist.get(check)) + 4, text.length());
            Log.e(TAG, "Time = " + Value.Time);
        }
        if(text.startsWith("DATE")) {
            Value.Date = text.substring(text.indexOf(Value.Jsonlist.get(check)) + 4, text.length());
            Log.e(TAG, "Date = " + Value.Date);
        }
        if(text.startsWith("LOG")){
            Value.GetLog = text.substring(text.indexOf(Value.Jsonlist.get(check)) + 3, text.length());
            if(Value.GetLog.matches("ON")){
                Value.downlog = true;
            }
            else if(Value.GetLog.matches("OFF")){
                Value.downlog = false;
            }
            Log.e(TAG, "LOG = " + Value.GetLog);
        }
    }
}
