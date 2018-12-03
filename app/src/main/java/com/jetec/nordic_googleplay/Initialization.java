package com.jetec.nordic_googleplay;

import android.annotation.SuppressLint;

import com.jetec.nordic_googleplay.Service.BluetoothLeService;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Thread.sleep;

public class Initialization {

    private SendValue sendValue;
    private String model;
    private String nowDate;
    private String nowTime;

    public Initialization(String model, BluetoothLeService bluetoothLeService){
        this.model = model;
        Date date = new Date();
        getDateTime(date);
        sendValue = new SendValue(bluetoothLeService);
    }

    public void start() throws InterruptedException {
        switch (model){
            case ("default"):{
                for(int i = 0; i < Value.Jsonlist.size(); i++) {
                    //noinspection StatementWithEmptyBody
                    if (Value.Jsonlist.get(i).matches("OVER")){

                    }
                    else if(Value.Jsonlist.get(i).matches("ADR")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("CR2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("CR1")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("EL2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("EH2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("EL1")){
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("EH1")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("PV2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("PV1")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                }
            }
            case ("BT-2-IIL"):{
                for(int i = 0; i < Value.Jsonlist.size(); i++) {
                    //noinspection StatementWithEmptyBody
                    if (Value.Jsonlist.get(i).matches("OVER")){
                    }
                    else //noinspection StatementWithEmptyBody
                        if(Value.Jsonlist.get(i).matches("LOG")){
                    }
                    else if(Value.Jsonlist.get(i).matches("TIME")){
                        sendValue.send(Value.Jsonlist.get(i) + nowTime);
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("DATE")){
                        sendValue.send(Value.Jsonlist.get(i) + nowDate);
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("INTER")){
                        sendValue.send(Value.Jsonlist.get(i) + "00030");
                        sleep(500);
                    }
                    else //noinspection StatementWithEmptyBody
                        if(Value.Jsonlist.get(i).matches("COUNT")){

                    }
                    else if(Value.Jsonlist.get(i).matches("DP2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("DP1")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("SPK")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("CR2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("CR1")){
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("EL2")){
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("EH2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("EL1")){
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("EH1")){
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("PV2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("PV1")){
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("IL2")){
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("IH2")){
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("IL1")){
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    }
                    else if(Value.Jsonlist.get(i).matches("IH1")){
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    }
                }
            }
        }

    }

    private void getDateTime(Date date){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdFormat = new SimpleDateFormat("yyMMdd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdFormat2 = new SimpleDateFormat("HHmmss");

        nowDate = sdFormat.format(date);
        nowTime = sdFormat2.format(date);
    }
}
