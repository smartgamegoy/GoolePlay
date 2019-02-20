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
import android.widget.Switch;
import android.widget.TextView;

import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.SendValue;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import com.jetec.nordic_googleplay.Value;

import static java.lang.Thread.sleep;

public class SwitchDialog {

    private Context context;
    private Dialog dialog;
    private String TAG = "SwitchDialog", switch_p;
    private SendValue sendValue;
    private Boolean getboolean;

    public SwitchDialog(Context context, BluetoothLeService bluetoothLeService) {
        this.context = context;
        sendValue = new SendValue(bluetoothLeService);
    }

    public void setDialog(Dialog getDialog) {
        this.dialog = getDialog;
    }

    public Dialog chose(String position, String num, String poositionName, Vibrator vibrator) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.chose_switch, null);
        ConstraintLayout layout = v.findViewById(R.id.switch_chose);
        TextView title = v.findViewById(R.id.textView1);
        Button by = v.findViewById(R.id.button2);
        Button bn = v.findViewById(R.id.button1);
        Switch chose = v.findViewById(R.id.switch1);
        title.setText(poositionName);

        if (num.matches("On"))
            chose.setChecked(true);
        else
            chose.setChecked(false);

        chose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO Auto-generated method stub
            vibrator.vibrate(100);
            if (isChecked) {
                switch_p = position + "+0001.0";
                getboolean = true;
                Log.e(TAG, "ON");
            } else {
                switch_p = position + "+0000.0";
                getboolean = false;
                Log.e(TAG, "OFF");
            }
        });

        by.setOnClickListener(v1 -> {
            vibrator.vibrate(100);
            if (position.matches("DP1")) {
                if (switch_p != null) {
                    try {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        sendValue.send(switch_p);
                        Value.IDP1 = getboolean;
                        sendset(position);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (position.matches("DP2")) {
                if (switch_p != null) {
                    try {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        sendValue.send(switch_p);
                        Value.IDP2 = getboolean;
                        sendset(position);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (position.matches("DP3")) {
                if (switch_p != null) {
                    try {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        sendValue.send(switch_p);
                        Value.IDP3 = getboolean;
                        sendset(position);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (position.matches("SPK")) {
                if (switch_p != null) {
                    try {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        sendValue.send(switch_p);
                        Value.SPK = getboolean;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            dialog.dismiss();
        });

        bn.setOnClickListener(v12 -> {
            vibrator.vibrate(100);
            dialog.dismiss();
        });

        progressDialog.setContentView(layout, new ConstraintLayout.LayoutParams((int) (3 * Value.all_Width / 5),
                (int) (Value.all_Height / 4)));

        return progressDialog;
    }

    private void sendset(String str) throws InterruptedException {
        if (str.matches("DP1") && Value.IDP1) {
            sleep(500);
            float IH1 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("IH1")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("IH1")).length()));
            String getIH1 = String.valueOf(IH1 / 10);
            int iIH1 = getIH1.indexOf(".");
            StringBuilder setIH1 = new StringBuilder("0");
            if (iIH1 != 4) {
                for (int j = 0; j < (4 - iIH1); j++)
                    setIH1.append("0");
                if (IH1 != 0) {
                    if (IH1 > 0) {
                        setIH1 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iIH1); j++) {
                            setIH1.append("0");
                        }
                        String numIH1_1 = getIH1.substring(0, getIH1.indexOf("."));
                        String numIH1_2 = getIH1.substring(getIH1.indexOf("."), getIH1.indexOf(".") + 2);
                        String out = "IH1" + "+" + setIH1 + numIH1_1 + numIH1_2;
                        sendValue.send(out);
                    } else {
                        String numIH1_1 = getIH1.substring(1, getIH1.indexOf("."));
                        String numIH1_2 = getIH1.substring(getIH1.indexOf("."), getIH1.indexOf(".") + 2);
                        String out = "IH1" + "-" + setIH1 + numIH1_1 + numIH1_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (IH1 != 0) {
                    if (IH1 > 0) {
                        String numIH1_1 = getIH1.substring(0, getIH1.indexOf("."));
                        String numIH1_2 = getIH1.substring(getIH1.indexOf("."), getIH1.indexOf(".") + 2);
                        String out = "IH1" + "+" + numIH1_1 + numIH1_2;
                        sendValue.send(out);
                    } else {
                        String numIH1_1 = getIH1.substring(1, getIH1.indexOf("."));
                        String numIH1_2 = getIH1.substring(getIH1.indexOf("."), getIH1.indexOf(".") + 2);
                        String out = "IH1" + "-" + numIH1_1 + numIH1_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float IL1 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("IL1")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("IL1")).length()));
            String getIL1 = String.valueOf(IL1 / 10);
            int iIL1 = getIL1.indexOf(".");
            StringBuilder setIL1 = new StringBuilder("0");
            if (iIL1 != 4) {
                for (int j = 0; j < (4 - iIL1); j++)
                    setIL1.append("0");
                if (IL1 != 0) {
                    if (IL1 > 0) {
                        setIL1 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iIL1); j++) {
                            setIL1.append("0");
                        }
                        String numIL1_1 = getIL1.substring(0, getIL1.indexOf("."));
                        String numIL1_2 = getIL1.substring(getIL1.indexOf("."), getIL1.indexOf(".") + 2);
                        String out = "IL1" + "+" + setIL1 + numIL1_1 + numIL1_2;
                        sendValue.send(out);
                    } else {
                        String numIL1_1 = getIL1.substring(1, getIL1.indexOf("."));
                        String numIL1_2 = getIL1.substring(getIL1.indexOf("."), getIL1.indexOf(".") + 2);
                        String out = "IL1" + "-" + setIL1 + numIL1_1 + numIL1_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (IL1 != 0) {
                    if (IL1 > 0) {
                        String numIL1_1 = getIL1.substring(0, getIL1.indexOf("."));
                        String numIL1_2 = getIL1.substring(getIL1.indexOf("."), getIL1.indexOf(".") + 2);
                        String out = "IL1" + "+" + numIL1_1 + numIL1_2;
                        sendValue.send(out);
                    } else {
                        String numIL1_1 = getIL1.substring(1, getIL1.indexOf("."));
                        String numIL1_2 = getIL1.substring(getIL1.indexOf("."), getIL1.indexOf(".") + 2);
                        String out = "IL1" + "-" + numIL1_1 + numIL1_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float EH1 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("EH1")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("EH1")).length()));
            String getEH1 = String.valueOf(EH1 / 10);
            int iEH1 = getEH1.indexOf(".");
            StringBuilder setEH1 = new StringBuilder("0");
            if (iEH1 != 4) {
                for (int j = 0; j < (4 - iEH1); j++)
                    setEH1.append("0");
                if (EH1 != 0) {
                    if (EH1 > 0) {
                        setEH1 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iEH1); j++) {
                            setEH1.append("0");
                        }
                        String numEH1_1 = getEH1.substring(0, getEH1.indexOf("."));
                        String numEH1_2 = getEH1.substring(getEH1.indexOf("."), getEH1.indexOf(".") + 2);
                        String out = "EH1" + "+" + setEH1 + numEH1_1 + numEH1_2;
                        sendValue.send(out);
                    } else {
                        String numEH1_1 = getEH1.substring(1, getEH1.indexOf("."));
                        String numEH1_2 = getEH1.substring(getEH1.indexOf("."), getEH1.indexOf(".") + 2);
                        String out = "EH1" + "-" + setEH1 + numEH1_1 + numEH1_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (EH1 != 0) {
                    if (EH1 > 0) {
                        String numEH1_1 = getEH1.substring(0, getEH1.indexOf("."));
                        String numEH1_2 = getEH1.substring(getEH1.indexOf("."), getEH1.indexOf(".") + 2);
                        String out = "EH1" + "+" + numEH1_1 + numEH1_2;
                        sendValue.send(out);
                    } else {
                        String numEH1_1 = getEH1.substring(1, getEH1.indexOf("."));
                        String numEH1_2 = getEH1.substring(getEH1.indexOf("."), getEH1.indexOf(".") + 2);
                        String out = "EH1" + "-" + numEH1_1 + numEH1_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float EL1 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("EL1")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("EL1")).length()));
            String getEL1 = String.valueOf(EL1 / 10);
            int iEL1 = getEL1.indexOf(".");
            StringBuilder setEL1 = new StringBuilder("0");
            if (iEL1 != 4) {
                for (int j = 0; j < (4 - iEL1); j++)
                    setEL1.append("0");
                if (EL1 != 0) {
                    if (EL1 > 0) {
                        setEL1 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iEL1); j++) {
                            setEL1.append("0");
                        }
                        String numEL1_1 = getEL1.substring(0, getEL1.indexOf("."));
                        String numEL1_2 = getEL1.substring(getEL1.indexOf("."), getEL1.indexOf(".") + 2);
                        String out = "EL1" + "+" + setEL1 + numEL1_1 + numEL1_2;
                        sendValue.send(out);
                    } else {
                        String numEL1_1 = getEL1.substring(1, getEL1.indexOf("."));
                        String numEL1_2 = getEL1.substring(getEL1.indexOf("."), getEL1.indexOf(".") + 2);
                        String out = "EL1" + "-" + setEL1 + numEL1_1 + numEL1_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (EL1 != 0) {
                    if (EL1 > 0) {
                        String numEL1_1 = getEL1.substring(0, getEL1.indexOf("."));
                        String numEL1_2 = getEL1.substring(getEL1.indexOf("."), getEL1.indexOf(".") + 2);
                        String out = "EL1" + "+" + numEL1_1 + numEL1_2;
                        sendValue.send(out);
                    } else {
                        String numEL1_1 = getEL1.substring(1, getEL1.indexOf("."));
                        String numEL1_2 = getEL1.substring(getEL1.indexOf("."), getEL1.indexOf(".") + 2);
                        String out = "EL1" + "-" + numEL1_1 + numEL1_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float PV1 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("PV1")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("PV1")).length()));
            String getPV1 = String.valueOf(PV1 / 10);
            int iPV1 = getPV1.indexOf(".");
            StringBuilder setPV1 = new StringBuilder("0");
            if (iPV1 != 4) {
                for (int j = 0; j < (4 - iPV1); j++)
                    setPV1.append("0");
                if (PV1 != 0) {
                    if (PV1 > 0) {
                        setPV1 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iPV1); j++) {
                            setPV1.append("0");
                        }
                        String numPV1_1 = getPV1.substring(0, getPV1.indexOf("."));
                        String numPV1_2 = getPV1.substring(getPV1.indexOf("."), getPV1.indexOf(".") + 2);
                        String out = "PV1" + "+" + setPV1 + numPV1_1 + numPV1_2;
                        Log.e(TAG, "numPV1_1 = " + numPV1_1);
                        Log.e(TAG, "setPV1 = " + setPV1);
                        sendValue.send(out);
                    } else {
                        String numPV1_1 = getPV1.substring(1, getPV1.indexOf("."));
                        String numPV1_2 = getPV1.substring(getPV1.indexOf("."), getPV1.indexOf(".") + 2);
                        String out = "PV1" + "-" + setPV1 + numPV1_1 + numPV1_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (PV1 != 0) {
                    if (PV1 > 0) {
                        String numPV1_1 = getPV1.substring(0, getPV1.indexOf("."));
                        String numPV1_2 = getPV1.substring(getPV1.indexOf("."), getPV1.indexOf(".") + 2);
                        String out = "PV1" + "+" + numPV1_1 + numPV1_2;
                        sendValue.send(out);
                    } else {
                        String numPV1_1 = getPV1.substring(1, getPV1.indexOf("."));
                        String numPV1_2 = getPV1.substring(getPV1.indexOf("."), getPV1.indexOf(".") + 2);
                        String out = "PV1" + "-" + numPV1_1 + numPV1_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float CR1 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("CR1")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("CR1")).length()));
            String getCR1 = String.valueOf(CR1 / 10);
            int iCR1 = getCR1.indexOf(".");
            StringBuilder setCR1 = new StringBuilder("0");
            if (iCR1 != 4) {
                for (int j = 0; j < (4 - iCR1); j++)
                    setCR1.append("0");
                if (CR1 != 0) {
                    if (CR1 > 0) {
                        setCR1 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iCR1); j++) {
                            setCR1.append("0");
                        }
                        String numCR1_1 = getCR1.substring(0, getCR1.indexOf("."));
                        String numCR1_2 = getCR1.substring(getCR1.indexOf("."), getCR1.indexOf(".") + 2);
                        String out = "CR1" + "+" + setCR1 + numCR1_1 + numCR1_2;
                        sendValue.send(out);
                    } else {
                        String numCR1_1 = getCR1.substring(1, getCR1.indexOf("."));
                        String numCR1_2 = getCR1.substring(getCR1.indexOf("."), getCR1.indexOf(".") + 2);
                        String out = "CR1" + "-" + setCR1 + numCR1_1 + numCR1_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (CR1 != 0) {
                    if (CR1 > 0) {
                        String numCR1_1 = getCR1.substring(0, getCR1.indexOf("."));
                        String numCR1_2 = getCR1.substring(getCR1.indexOf("."), getCR1.indexOf(".") + 2);
                        String out = "CR1" + "+" + numCR1_1 + numCR1_2;
                        sendValue.send(out);
                    } else {
                        String numCR1_1 = getCR1.substring(1, getCR1.indexOf("."));
                        String numCR1_2 = getCR1.substring(getCR1.indexOf("."), getCR1.indexOf(".") + 2);
                        String out = "CR1" + "-" + numCR1_1 + numCR1_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
        } else if (str.matches("DP2") && Value.IDP2) {
            sleep(500);
            float IH2 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("IH2")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("IH2")).length()));
            String getIH2 = String.valueOf(IH2 / 10);
            int iIH2 = getIH2.indexOf(".");
            StringBuilder setIH2 = new StringBuilder("0");
            if (iIH2 != 4) {
                for (int j = 0; j < (4 - iIH2); j++)
                    setIH2.append("0");
                if (IH2 != 0) {
                    if (IH2 > 0) {
                        setIH2 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iIH2); j++) {
                            setIH2.append("0");
                        }
                        String numIH2_1 = getIH2.substring(0, getIH2.indexOf("."));
                        String numIH2_2 = getIH2.substring(getIH2.indexOf("."), getIH2.indexOf(".") + 2);
                        String out = "IH2" + "+" + setIH2 + numIH2_1 + numIH2_2;
                        sendValue.send(out);
                    } else {
                        String numIH2_1 = getIH2.substring(1, getIH2.indexOf("."));
                        String numIH2_2 = getIH2.substring(getIH2.indexOf("."), getIH2.indexOf(".") + 2);
                        String out = "IH2" + "-" + setIH2 + numIH2_1 + numIH2_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (IH2 != 0) {
                    if (IH2 > 0) {
                        String numIH2_1 = getIH2.substring(0, getIH2.indexOf("."));
                        String numIH2_2 = getIH2.substring(getIH2.indexOf("."), getIH2.indexOf(".") + 2);
                        String out = "IH2" + "+" + numIH2_1 + numIH2_2;
                        sendValue.send(out);
                    } else {
                        String numIH2_1 = getIH2.substring(1, getIH2.indexOf("."));
                        String numIH2_2 = getIH2.substring(getIH2.indexOf("."), getIH2.indexOf(".") + 2);
                        String out = "IH2" + "-" + numIH2_1 + numIH2_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float IL2 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("IL2")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("IL2")).length()));
            String getIL2 = String.valueOf(IL2 / 10);
            int iIL2 = getIL2.indexOf(".");
            StringBuilder setIL2 = new StringBuilder("0");
            if (iIL2 != 4) {
                for (int j = 0; j < (4 - iIL2); j++)
                    setIL2.append("0");
                if (IL2 != 0) {
                    if (IL2 > 0) {
                        setIL2 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iIL2); j++) {
                            setIL2.append("0");
                        }
                        String numIL2_1 = getIL2.substring(0, getIL2.indexOf("."));
                        String numIL2_2 = getIL2.substring(getIL2.indexOf("."), getIL2.indexOf(".") + 2);
                        String out = "IL2" + "+" + setIL2 + numIL2_1 + numIL2_2;
                        sendValue.send(out);
                    } else {
                        String numIL2_1 = getIL2.substring(1, getIL2.indexOf("."));
                        String numIL2_2 = getIL2.substring(getIL2.indexOf("."), getIL2.indexOf(".") + 2);
                        String out = "IL2" + "-" + setIL2 + numIL2_1 + numIL2_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (IL2 != 0) {
                    if (IL2 > 0) {
                        String numIL2_1 = getIL2.substring(0, getIL2.indexOf("."));
                        String numIL2_2 = getIL2.substring(getIL2.indexOf("."), getIL2.indexOf(".") + 2);
                        String out = "IL2" + "+" + numIL2_1 + numIL2_2;
                        sendValue.send(out);
                    } else {
                        String numIL2_1 = getIL2.substring(1, getIL2.indexOf("."));
                        String numIL2_2 = getIL2.substring(getIL2.indexOf("."), getIL2.indexOf(".") + 2);
                        String out = "IL2" + "-" + numIL2_1 + numIL2_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float EH2 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("EH2")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("EH2")).length()));
            String getEH2 = String.valueOf(EH2 / 10);
            int iEH2 = getEH2.indexOf(".");
            StringBuilder setEH2 = new StringBuilder("0");
            if (iEH2 != 4) {
                for (int j = 0; j < (4 - iEH2); j++)
                    setEH2.append("0");
                if (EH2 != 0) {
                    if (EH2 > 0) {
                        setEH2 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iEH2); j++) {
                            setEH2.append("0");
                        }
                        String numEH2_1 = getEH2.substring(0, getEH2.indexOf("."));
                        String numEH2_2 = getEH2.substring(getEH2.indexOf("."), getEH2.indexOf(".") + 2);
                        String out = "EH2" + "+" + setEH2 + numEH2_1 + numEH2_2;
                        sendValue.send(out);
                    } else {
                        String numEH2_1 = getEH2.substring(1, getEH2.indexOf("."));
                        String numEH2_2 = getEH2.substring(getEH2.indexOf("."), getEH2.indexOf(".") + 2);
                        String out = "EH2" + "-" + setEH2 + numEH2_1 + numEH2_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (EH2 != 0) {
                    if (EH2 > 0) {
                        String numEH2_1 = getEH2.substring(0, getEH2.indexOf("."));
                        String numEH2_2 = getEH2.substring(getEH2.indexOf("."), getEH2.indexOf(".") + 2);
                        String out = "EH2" + "+" + numEH2_1 + numEH2_2;
                        sendValue.send(out);
                    } else {
                        String numEH2_1 = getEH2.substring(1, getEH2.indexOf("."));
                        String numEH2_2 = getEH2.substring(getEH2.indexOf("."), getEH2.indexOf(".") + 2);
                        String out = "EH2" + "-" + numEH2_1 + numEH2_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float EL2 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("EL2")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("EL2")).length()));
            String getEL2 = String.valueOf(EL2 / 10);
            int iEL2 = getEL2.indexOf(".");
            StringBuilder setEL2 = new StringBuilder("0");
            if (iEL2 != 4) {
                for (int j = 0; j < (4 - iEL2); j++)
                    setEL2.append("0");
                if (EL2 != 0) {
                    if (EL2 > 0) {
                        setEL2 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iEL2); j++) {
                            setEL2.append("0");
                        }
                        String numEL2_1 = getEL2.substring(0, getEL2.indexOf("."));
                        String numEL2_2 = getEL2.substring(getEL2.indexOf("."), getEL2.indexOf(".") + 2);
                        String out = "EL2" + "+" + setEL2 + numEL2_1 + numEL2_2;
                        sendValue.send(out);
                    } else {
                        String numEL2_1 = getEL2.substring(1, getEL2.indexOf("."));
                        String numEL2_2 = getEL2.substring(getEL2.indexOf("."), getEL2.indexOf(".") + 2);
                        String out = "EL2" + "-" + setEL2 + numEL2_1 + numEL2_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (EL2 != 0) {
                    if (EL2 > 0) {
                        String numEL2_1 = getEL2.substring(0, getEL2.indexOf("."));
                        String numEL2_2 = getEL2.substring(getEL2.indexOf("."), getEL2.indexOf(".") + 2);
                        String out = "EL2" + "+" + numEL2_1 + numEL2_2;
                        sendValue.send(out);
                    } else {
                        String numEL2_1 = getEL2.substring(1, getEL2.indexOf("."));
                        String numEL2_2 = getEL2.substring(getEL2.indexOf("."), getEL2.indexOf(".") + 2);
                        String out = "EL2" + "-" + numEL2_1 + numEL2_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float PV2 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("PV2")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("PV2")).length()));
            String getPV2 = String.valueOf(PV2 / 10);
            int iPV2 = getPV2.indexOf(".");
            StringBuilder setPV2 = new StringBuilder("0");
            if (iPV2 != 4) {
                for (int j = 0; j < (4 - iPV2); j++)
                    setPV2.append("0");
                if (PV2 != 0) {
                    if (PV2 > 0) {
                        setPV2 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iPV2); j++) {
                            setPV2.append("0");
                        }
                        String numPV2_1 = getPV2.substring(0, getPV2.indexOf("."));
                        String numPV2_2 = getPV2.substring(getPV2.indexOf("."), getPV2.indexOf(".") + 2);
                        String out = "PV2" + "+" + setPV2 + numPV2_1 + numPV2_2;
                        sendValue.send(out);
                    } else {
                        String numPV2_1 = getPV2.substring(1, getPV2.indexOf("."));
                        String numPV2_2 = getPV2.substring(getPV2.indexOf("."), getPV2.indexOf(".") + 2);
                        String out = "PV2" + "-" + setPV2 + numPV2_1 + numPV2_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (PV2 != 0) {
                    if (PV2 > 0) {
                        String numPV2_1 = getPV2.substring(0, getPV2.indexOf("."));
                        String numPV2_2 = getPV2.substring(getPV2.indexOf("."), getPV2.indexOf(".") + 2);
                        String out = "PV2" + "+" + numPV2_1 + numPV2_2;
                        sendValue.send(out);
                    } else {
                        String numPV2_1 = getPV2.substring(1, getPV2.indexOf("."));
                        String numPV2_2 = getPV2.substring(getPV2.indexOf("."), getPV2.indexOf(".") + 2);
                        String out = "PV2" + "-" + numPV2_1 + numPV2_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float CR2 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("CR2")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("CR2")).length()));
            String getCR2 = String.valueOf(CR2 / 10);
            int iCR2 = getCR2.indexOf(".");
            StringBuilder setCR2 = new StringBuilder("0");
            if (iCR2 != 4) {
                for (int j = 0; j < (4 - iCR2); j++)
                    setCR2.append("0");
                if (CR2 != 0) {
                    if (CR2 > 0) {
                        setCR2 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iCR2); j++) {
                            setCR2.append("0");
                        }
                        String numCR2_1 = getCR2.substring(0, getCR2.indexOf("."));
                        String numCR2_2 = getCR2.substring(getCR2.indexOf("."), getCR2.indexOf(".") + 2);
                        String out = "CR2" + "+" + setCR2 + numCR2_1 + numCR2_2;
                        sendValue.send(out);
                    } else {
                        String numCR2_1 = getCR2.substring(1, getCR2.indexOf("."));
                        String numCR2_2 = getCR2.substring(getCR2.indexOf("."), getCR2.indexOf(".") + 2);
                        String out = "CR2" + "-" + setCR2 + numCR2_1 + numCR2_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (CR2 != 0) {
                    if (CR2 > 0) {
                        String numCR2_1 = getCR2.substring(0, getCR2.indexOf("."));
                        String numCR2_2 = getCR2.substring(getCR2.indexOf("."), getCR2.indexOf(".") + 2);
                        String out = "CR2" + "+" + numCR2_1 + numCR2_2;
                        sendValue.send(out);
                    } else {
                        String numCR2_1 = getCR2.substring(1, getCR2.indexOf("."));
                        String numCR2_2 = getCR2.substring(getCR2.indexOf("."), getCR2.indexOf(".") + 2);
                        String out = "CR2" + "-" + numCR2_1 + numCR2_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
        } else if (str.matches("DP3") && Value.IDP3) {
            sleep(500);
            float IH3 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("IH3")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("IH3")).length()));
            String getIH3 = String.valueOf(IH3 / 10);
            int iIH3 = getIH3.indexOf(".");
            StringBuilder setIH3 = new StringBuilder("0");
            if (iIH3 != 4) {
                for (int j = 0; j < (4 - iIH3); j++)
                    setIH3.append("0");
                if (IH3 != 0) {
                    if (IH3 > 0) {
                        setIH3 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iIH3); j++) {
                            setIH3.append("0");
                        }
                        String numIH3_1 = getIH3.substring(0, getIH3.indexOf("."));
                        String numIH3_2 = getIH3.substring(getIH3.indexOf("."), getIH3.indexOf(".") + 2);
                        String out = "IH3" + "+" + setIH3 + numIH3_1 + numIH3_2;
                        sendValue.send(out);
                    } else {
                        String numIH3_1 = getIH3.substring(1, getIH3.indexOf("."));
                        String numIH3_2 = getIH3.substring(getIH3.indexOf("."), getIH3.indexOf(".") + 2);
                        String out = "IH3" + "-" + setIH3 + numIH3_1 + numIH3_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (IH3 != 0) {
                    if (IH3 > 0) {
                        String numIH3_1 = getIH3.substring(0, getIH3.indexOf("."));
                        String numIH3_2 = getIH3.substring(getIH3.indexOf("."), getIH3.indexOf(".") + 2);
                        String out = "IH3" + "+" + numIH3_1 + numIH3_2;
                        sendValue.send(out);
                    } else {
                        String numIH3_1 = getIH3.substring(1, getIH3.indexOf("."));
                        String numIH3_2 = getIH3.substring(getIH3.indexOf("."), getIH3.indexOf(".") + 2);
                        String out = "IH3" + "-" + numIH3_1 + numIH3_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float IL3 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("IL3")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("IL3")).length()));
            String getIL3 = String.valueOf(IL3 / 10);
            int iIL3 = getIL3.indexOf(".");
            StringBuilder setIL3 = new StringBuilder("0");
            if (iIL3 != 4) {
                for (int j = 0; j < (4 - iIL3); j++)
                    setIL3.append("0");
                if (IL3 != 0) {
                    if (IL3 > 0) {
                        setIL3 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iIL3); j++) {
                            setIL3.append("0");
                        }
                        String numIL3_1 = getIL3.substring(0, getIL3.indexOf("."));
                        String numIL3_2 = getIL3.substring(getIL3.indexOf("."), getIL3.indexOf(".") + 2);
                        String out = "IL3" + "+" + setIL3 + numIL3_1 + numIL3_2;
                        sendValue.send(out);
                    } else {
                        String numIL3_1 = getIL3.substring(1, getIL3.indexOf("."));
                        String numIL3_2 = getIL3.substring(getIL3.indexOf("."), getIL3.indexOf(".") + 2);
                        String out = "IL3" + "-" + setIL3 + numIL3_1 + numIL3_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (IL3 != 0) {
                    if (IL3 > 0) {
                        String numIL3_1 = getIL3.substring(0, getIL3.indexOf("."));
                        String numIL3_2 = getIL3.substring(getIL3.indexOf("."), getIL3.indexOf(".") + 2);
                        String out = "IL3" + "+" + numIL3_1 + numIL3_2;
                        sendValue.send(out);
                    } else {
                        String numIL3_1 = getIL3.substring(1, getIL3.indexOf("."));
                        String numIL3_2 = getIL3.substring(getIL3.indexOf("."), getIL3.indexOf(".") + 2);
                        String out = "IL3" + "-" + numIL3_1 + numIL3_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float EH3 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("EH3")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("EH3")).length()));
            String getEH3 = String.valueOf(EH3 / 10);
            int iEH3 = getEH3.indexOf(".");
            StringBuilder setEH3 = new StringBuilder("0");
            if (iEH3 != 4) {
                for (int j = 0; j < (4 - iEH3); j++)
                    setEH3.append("0");
                if (EH3 != 0) {
                    if (EH3 > 0) {
                        setEH3 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iEH3); j++) {
                            setEH3.append("0");
                        }
                        String numEH3_1 = getEH3.substring(0, getEH3.indexOf("."));
                        String numEH3_2 = getEH3.substring(getEH3.indexOf("."), getEH3.indexOf(".") + 2);
                        String out = "EH3" + "+" + setEH3 + numEH3_1 + numEH3_2;
                        sendValue.send(out);
                    } else {
                        String numEH3_1 = getEH3.substring(1, getEH3.indexOf("."));
                        String numEH3_2 = getEH3.substring(getEH3.indexOf("."), getEH3.indexOf(".") + 2);
                        String out = "EH3" + "-" + setEH3 + numEH3_1 + numEH3_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (EH3 != 0) {
                    if (EH3 > 0) {
                        String numEH3_1 = getEH3.substring(0, getEH3.indexOf("."));
                        String numEH3_2 = getEH3.substring(getEH3.indexOf("."), getEH3.indexOf(".") + 2);
                        String out = "EH3" + "+" + numEH3_1 + numEH3_2;
                        sendValue.send(out);
                    } else {
                        String numEH3_1 = getEH3.substring(1, getEH3.indexOf("."));
                        String numEH3_2 = getEH3.substring(getEH3.indexOf("."), getEH3.indexOf(".") + 2);
                        String out = "EH3" + "-" + numEH3_1 + numEH3_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float EL3 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("EL3")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("EL3")).length()));
            String getEL3 = String.valueOf(EL3 / 10);
            int iEL3 = getEL3.indexOf(".");
            StringBuilder setEL3 = new StringBuilder("0");
            if (iEL3 != 4) {
                for (int j = 0; j < (4 - iEL3); j++)
                    setEL3.append("0");
                if (EL3 != 0) {
                    if (EL3 > 0) {
                        setEL3 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iEL3); j++) {
                            setEL3.append("0");
                        }
                        String numEL3_1 = getEL3.substring(0, getEL3.indexOf("."));
                        String numEL3_2 = getEL3.substring(getEL3.indexOf("."), getEL3.indexOf(".") + 2);
                        String out = "EL3" + "+" + setEL3 + numEL3_1 + numEL3_2;
                        sendValue.send(out);
                    } else {
                        String numEL3_1 = getEL3.substring(1, getEL3.indexOf("."));
                        String numEL3_2 = getEL3.substring(getEL3.indexOf("."), getEL3.indexOf(".") + 2);
                        String out = "EL3" + "-" + setEL3 + numEL3_1 + numEL3_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (EL3 != 0) {
                    if (EL3 > 0) {
                        String numEL3_1 = getEL3.substring(0, getEL3.indexOf("."));
                        String numEL3_2 = getEL3.substring(getEL3.indexOf("."), getEL3.indexOf(".") + 2);
                        String out = "EL3" + "+" + numEL3_1 + numEL3_2;
                        sendValue.send(out);
                    } else {
                        String numEL3_1 = getEL3.substring(1, getEL3.indexOf("."));
                        String numEL3_2 = getEL3.substring(getEL3.indexOf("."), getEL3.indexOf(".") + 2);
                        String out = "EL3" + "-" + numEL3_1 + numEL3_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float PV3 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("PV3")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("PV3")).length()));
            String getPV3 = String.valueOf(PV3 / 10);
            int iPV3 = getPV3.indexOf(".");
            StringBuilder setPV3 = new StringBuilder("0");
            if (iPV3 != 4) {
                for (int j = 0; j < (4 - iPV3); j++)
                    setPV3.append("0");
                if (PV3 != 0) {
                    if (PV3 > 0) {
                        setPV3 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iPV3); j++) {
                            setPV3.append("0");
                        }
                        String numPV3_1 = getPV3.substring(0, getPV3.indexOf("."));
                        String numPV3_2 = getPV3.substring(getPV3.indexOf("."), getPV3.indexOf(".") + 2);
                        String out = "PV3" + "+" + setPV3 + numPV3_1 + numPV3_2;
                        sendValue.send(out);
                    } else {
                        String numPV3_1 = getPV3.substring(1, getPV3.indexOf("."));
                        String numPV3_2 = getPV3.substring(getPV3.indexOf("."), getPV3.indexOf(".") + 2);
                        String out = "PV3" + "-" + setPV3 + numPV3_1 + numPV3_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (PV3 != 0) {
                    if (PV3 > 0) {
                        String numPV3_1 = getPV3.substring(0, getPV3.indexOf("."));
                        String numPV3_2 = getPV3.substring(getPV3.indexOf("."), getPV3.indexOf(".") + 2);
                        String out = "PV3" + "+" + numPV3_1 + numPV3_2;
                        sendValue.send(out);
                    } else {
                        String numPV3_1 = getPV3.substring(1, getPV3.indexOf("."));
                        String numPV3_2 = getPV3.substring(getPV3.indexOf("."), getPV3.indexOf(".") + 2);
                        String out = "PV3" + "-" + numPV3_1 + numPV3_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
            float CR3 = Float.valueOf(Value.DataSave.get(Value.SelectItem.indexOf("CR3")).
                    substring(3, Value.DataSave.get(Value.SelectItem.indexOf("CR3")).length()));
            String getCR3 = String.valueOf(CR3 / 10);
            int iCR3 = getCR3.indexOf(".");
            StringBuilder setCR3 = new StringBuilder("0");
            if (iCR3 != 4) {
                for (int j = 0; j < (4 - iCR3); j++)
                    setCR3.append("0");
                if (CR3 != 0) {
                    if (CR3 > 0) {
                        setCR3 = new StringBuilder("0");
                        for (int j = 1; j < (4 - iCR3); j++) {
                            setCR3.append("0");
                        }
                        String numCR3_1 = getCR3.substring(0, getCR3.indexOf("."));
                        String numCR3_2 = getCR3.substring(getCR3.indexOf("."), getCR3.indexOf(".") + 2);
                        String out = "CR3" + "+" + setCR3 + numCR3_1 + numCR3_2;
                        sendValue.send(out);
                    } else {
                        String numCR3_1 = getCR3.substring(1, getCR3.indexOf("."));
                        String numCR3_2 = getCR3.substring(getCR3.indexOf("."), getCR3.indexOf(".") + 2);
                        String out = "CR3" + "-" + setCR3 + numCR3_1 + numCR3_2;
                        sendValue.send(out);
                    }
                }
            } else {
                if (CR3 != 0) {
                    if (CR3 > 0) {
                        String numCR3_1 = getCR3.substring(0, getCR3.indexOf("."));
                        String numCR3_2 = getCR3.substring(getCR3.indexOf("."), getCR3.indexOf(".") + 2);
                        String out = "CR3" + "+" + numCR3_1 + numCR3_2;
                        sendValue.send(out);
                    } else {
                        String numCR3_1 = getCR3.substring(1, getCR3.indexOf("."));
                        String numCR3_2 = getCR3.substring(getCR3.indexOf("."), getCR3.indexOf(".") + 2);
                        String out = "CR3" + "-" + numCR3_1 + numCR3_2;
                        sendValue.send(out);
                    }
                }
            }
            sleep(500);
        } else if (str.matches("DP1") && !Value.IDP1) {
            Log.e(TAG, "SelectItem = " + Value.SelectItem);
            Log.e(TAG, "return_RX = " + Value.return_RX);
            if (Value.SelectItem.indexOf("IH1") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("IH1") - 1));
            }
            if (Value.SelectItem.indexOf("IL1") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("IL1") - 1));
            }
            if (Value.SelectItem.indexOf("EH1") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("EH1") - 1));
            }
            if (Value.SelectItem.indexOf("EL1") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("EL1") - 1));
            }
            if (Value.SelectItem.indexOf("PV1") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("PV1") - 1));
            }
            if (Value.SelectItem.indexOf("CR1") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("CR1") - 1));
            }
        } else if (str.matches("DP2") && !Value.IDP2) {
            if (Value.SelectItem.indexOf("IH2") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("IH2") - 1));
            }
            if (Value.SelectItem.indexOf("IL2") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("IL2") - 1));
            }
            if (Value.SelectItem.indexOf("EH2") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("EH2") - 1));
            }
            if (Value.SelectItem.indexOf("EL2") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("EL2") - 1));
            }
            if (Value.SelectItem.indexOf("PV2") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("PV2") - 1));
            }
            if (Value.SelectItem.indexOf("CR2") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("CR2") - 1));
            }
        } else if (str.matches("DP3") && !Value.IDP3) {
            if (Value.SelectItem.indexOf("IH3") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("IH3") - 1));
            }
            if (Value.SelectItem.indexOf("IL3") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("IL3") - 1));
            }
            if (Value.SelectItem.indexOf("EH3") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("EH3") - 1));
            }
            if (Value.SelectItem.indexOf("EL3") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("EL3") - 1));
            }
            if (Value.SelectItem.indexOf("PV3") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("PV3") - 1));
            }
            if (Value.SelectItem.indexOf("CR3") != -1) {
                sleep(500);
                sendValue.send(Value.return_RX.get(Value.SelectItem.indexOf("CR3") - 1));
            }
        }
    }
}
