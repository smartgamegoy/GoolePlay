package com.jetec.nordic_googleplay;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jetec.nordic_googleplay.Activity.DeviceList;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Thread.sleep;

public class Initialization {

    private SendValue sendValue;
    private String model;
    private String nowDate;
    private String nowTime;
    private String num;

    public Initialization(String model, BluetoothLeService bluetoothLeService) {
        this.model = model;
        Date date = new Date();
        getDateTime(date);
        sendValue = new SendValue(bluetoothLeService);
    }

    public void start() throws InterruptedException {
        if (model.matches("default")) {
            Log.e("初始化型號", "default");
            sendValue.send("NAMEJTC");
            sleep(500);
            sendValue.send("PWR=000000");
            sleep(500);
            for (int i = 0; i < Value.Jsonlist.size(); i++) {
                //noinspection StatementWithEmptyBody
                if (Value.Jsonlist.get(i).matches("OVER")) {

                } else if (Value.Jsonlist.get(i).matches("ADR")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("CR2")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("CR1")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("EL2")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("EH2")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("EL1")) {
                    sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("EH1")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("PV2")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("PV1")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    sleep(500);
                }
            }
        } else {
            Log.e("初始化型號", model);
            num = model.substring(model.lastIndexOf("-") + 1, model.length());
            if (num.contains("I")) {
                for (int i = 0; i < num.length(); i++) {
                    if (num.charAt(i) == 'I') {
                        sendValue.send("DP" + (i + 1));
                    }
                }
            }
            sendValue.send("NAMEJTC");
            sleep(500);
            sendValue.send("PWR=000000");
            sleep(500);
            if (Value.YMD) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat get_time = new SimpleDateFormat("HHmmss");
                Date date = new Date();
                String strtime = get_time.format(date);
                sendValue.send("TIME" + strtime);
                sleep(500);
            }
            for (int i = 0; i < Value.Jsonlist.size(); i++) {

                if (Value.Jsonlist.get(i).matches("OVER")) {
                    sendValue.send(Value.Jsonlist.get(i));
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("LOG")) {
                }
                else if (Value.Jsonlist.get(i).matches("TIME")) {
                    sendValue.send(Value.Jsonlist.get(i) + nowTime);
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("DATE")) {
                    sendValue.send(Value.Jsonlist.get(i) + nowDate);
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("INTER")) {
                    sendValue.send(Value.Jsonlist.get(i) + "00030");
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("COUNT")) {
                }
                else if (Value.Jsonlist.get(i).matches("DP3")) {
                }
                else if (Value.Jsonlist.get(i).matches("DP2")) {
                }
                else if (Value.Jsonlist.get(i).matches("DP1")) {
                }
                else if (Value.Jsonlist.get(i).matches("SPK")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("ADR")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("CR3")) {
                    if (num.charAt(2) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                    } else if (num.charAt(2) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0099.0");
                    } else if (num.charAt(2) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+2000.0");
                    } else if (num.charAt(2) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+3000.0");
                    } else if (num.charAt(2) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+5000.0");
                    } else if (num.charAt(2) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("CR2")) {
                    if (num.charAt(1) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                    } else if (num.charAt(1) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0099.0");
                    } else if (num.charAt(1) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+2000.0");
                    } else if (num.charAt(1) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+3000.0");
                    } else if (num.charAt(1) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+5000.0");
                    } else if (num.charAt(1) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("CR1")) {
                    if (num.charAt(0) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                    } else if (num.charAt(0) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0099.0");
                    } else if (num.charAt(0) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+2000.0");
                    } else if (num.charAt(0) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+3000.0");
                    } else if (num.charAt(0) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+5000.0");
                    } else if (num.charAt(0) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("IL3")) {
                    sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("IH3")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("IL2")) {
                    sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("IH2")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("IL1")) {
                    sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                    sleep(500);
                } else if (Value.Jsonlist.get(i).matches("IH1")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("EL3")) {
                    if (num.charAt(2) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                    } else if (num.charAt(2) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(2) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(2) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(2) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(2) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("EH3")) {
                    if (num.charAt(2) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                    } else if (num.charAt(2) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0099.0");
                    } else if (num.charAt(2) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+2000.0");
                    } else if (num.charAt(2) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+3000.0");
                    } else if (num.charAt(2) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+5000.0");
                    } else if (num.charAt(2) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("EL2")) {
                    if (num.charAt(1) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                    } else if (num.charAt(1) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(1) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(1) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(1) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(1) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("EH2")) {
                    if (num.charAt(1) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                    } else if (num.charAt(1) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0099.0");
                    } else if (num.charAt(1) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+2000.0");
                    } else if (num.charAt(1) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+3000.0");
                    } else if (num.charAt(1) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+5000.0");
                    } else if (num.charAt(1) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("EL1")) {
                    if (num.charAt(0) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                    } else if (num.charAt(0) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(0) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(0) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(0) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    } else if (num.charAt(0) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("EH1")) {
                    if (num.charAt(0) == 'T') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                    } else if (num.charAt(0) == 'H') {
                        sendValue.send(Value.Jsonlist.get(i) + "+0099.0");
                    } else if (num.charAt(0) == 'C') {
                        sendValue.send(Value.Jsonlist.get(i) + "+2000.0");
                    } else if (num.charAt(0) == 'D') {
                        sendValue.send(Value.Jsonlist.get(i) + "+3000.0");
                    } else if (num.charAt(0) == 'E') {
                        sendValue.send(Value.Jsonlist.get(i) + "+5000.0");
                    } else if (num.charAt(0) == 'I') {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                    }
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("PV3")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("PV2")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    sleep(500);
                }
                else if (Value.Jsonlist.get(i).matches("PV1")) {
                    sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                    sleep(500);
                }
            }
        }
        /*switch (model) {
            case ("default"): {
                Log.e("初始化型號", "default");
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    //noinspection StatementWithEmptyBody
                    if (Value.Jsonlist.get(i).matches("OVER")) {

                    } else if (Value.Jsonlist.get(i).matches("ADR")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                }
            }
            break;
            case ("BT-2-IIL"): {
                Log.e("初始化型號", "BT-2-IIL");
                sendValue.send("DP1" + "+0000.0");
                sleep(500);
                sendValue.send("DP2" + "+0000.0");
                sleep(500);
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    //noinspection StatementWithEmptyBody
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                    } else //noinspection StatementWithEmptyBody
                        if (Value.Jsonlist.get(i).matches("LOG")) {
                        } else if (Value.Jsonlist.get(i).matches("TIME")) {
                            sendValue.send(Value.Jsonlist.get(i) + nowTime);
                            sleep(500);
                        } else if (Value.Jsonlist.get(i).matches("DATE")) {
                            sendValue.send(Value.Jsonlist.get(i) + nowDate);
                            sleep(500);
                        } else if (Value.Jsonlist.get(i).matches("INTER")) {
                            sendValue.send(Value.Jsonlist.get(i) + "00030");
                            sleep(500);
                        } else //noinspection StatementWithEmptyBody
                            if (Value.Jsonlist.get(i).matches("COUNT")) {

                            } else if (Value.Jsonlist.get(i).matches("DP2")) {
                                //sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                                //sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("DP1")) {
                                //sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                                //sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("SPK")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("CR2")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("CR1")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("EL2")) {
                                sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("EH2")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("EL1")) {
                                sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("EH1")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("PV2")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("PV1")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("IL2")) {
                                sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("IH2")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("IL1")) {
                                sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                                sleep(500);
                            } else if (Value.Jsonlist.get(i).matches("IH1")) {
                                sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                                sleep(500);
                            }
                }
                sendValue.send("SP1+1250.0");
                sleep(500);
                sendValue.send("SV1+0000.0");
                sleep(500);
                sendValue.send("ER1+1000.0");
                sleep(500);
                sendValue.send("SP2+1250.0");
                sleep(500);
                sendValue.send("SV2+0000.0");
                sleep(500);
                sendValue.send("ER2+1000.0");
                sleep(500);
            }
            break;
            case ("BT-2-II"): {
                Log.e("初始化型號", "BT-2-II");
                sendValue.send("DP1" + "+0000.0");
                sleep(500);
                sendValue.send("DP2" + "+0000.0");
                sleep(500);
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                        //OVER
                    } else if (Value.Jsonlist.get(i).matches("DP2")) {
                        //sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        //sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("DP1")) {
                        //sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        //sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("SPK")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("IL2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("IH2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("IL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("IH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    }
                }
                sendValue.send("SP1+1250.0");
                sleep(500);
                sendValue.send("SV1+0000.0");
                sleep(500);
                sendValue.send("ER1+1000.0");
                sleep(500);
                sendValue.send("SP2+1250.0");
                sleep(500);
                sendValue.send("SV2+0000.0");
                sleep(500);
                sendValue.send("ER2+1000.0");
                sleep(500);
            }
            break;
            case ("BT-2-TH"): {
                Log.e("初始化型號", "BT_2_TH");
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                        //OVER
                    } else if (Value.Jsonlist.get(i).matches("SPK")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                }
            }
            break;
            case ("BT-1-I"): {
                Log.e("初始化型號", "BT-1-I");
                sendValue.send("DP1" + "+0000.0");
                sleep(500);
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                        //OVER
                    } else if (Value.Jsonlist.get(i).matches("DP1")) {
                        //sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        //sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("SPK")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("IL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0999.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("IH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+9999.0");
                        sleep(500);
                    }
                }
                sendValue.send("SP1+1250.0");
                sleep(500);
                sendValue.send("SV1+0000.0");
                sleep(500);
                sendValue.send("ER1+1000.0");
                sleep(500);
            }
            break;
            case ("BT-3-THC"): {
                Log.e("初始化型號", "BT-3-THC");
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                        //OVER
                    } else if (Value.Jsonlist.get(i).matches("SPK")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+2000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+2000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                }
            }
            break;
            case ("BT-3-THD"): {
                Log.e("初始化型號", "BT-3-THD");
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                        //OVER
                    } else if (Value.Jsonlist.get(i).matches("SPK")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+3000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+3000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                }
            }
            break;
            case ("BT-3-THE"): {
                Log.e("初始化型號", "BT-3-THE");
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                        //OVER
                    } else if (Value.Jsonlist.get(i).matches("SPK")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+5000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+5000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV3")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                }
            }
            break;
            case ("BT-2-TY"): {
                Log.e("初始化型號", "BT-2-TY");
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat get_time = new SimpleDateFormat("HHmmss");
                Date date = new Date();
                String strtime = get_time.format(date);
                sendValue.send("TIME" + strtime);
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                        //OVER
                    } else if (Value.Jsonlist.get(i).matches("SPK")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                }
            }
            break;
            case ("BT-3-THY"): {
                Log.e("初始化型號", "BT_3_THY");
                sendValue.send("NAMEJTC");
                sleep(500);
                sendValue.send("PWR=000000");
                sleep(500);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat get_time = new SimpleDateFormat("HHmmss");
                Date date = new Date();
                String strtime = get_time.format(date);
                sendValue.send("TIME" + strtime);
                sleep(500);
                for (int i = 0; i < Value.Jsonlist.size(); i++) {
                    if (Value.Jsonlist.get(i).matches("OVER")) {
                        //OVER
                    } else if (Value.Jsonlist.get(i).matches("SPK")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0001.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("CR1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0100.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EL1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "-0010.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("EH1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0065.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV2")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    } else if (Value.Jsonlist.get(i).matches("PV1")) {
                        sendValue.send(Value.Jsonlist.get(i) + "+0000.0");
                        sleep(500);
                    }
                }
            }
            break;
        }*/
    }

    private void getDateTime(Date date) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdFormat = new SimpleDateFormat("yyMMdd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdFormat2 = new SimpleDateFormat("HHmmss");

        nowDate = sdFormat.format(date);
        nowTime = sdFormat2.format(date);
    }
}
