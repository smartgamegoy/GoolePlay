package com.jetec.nordic_googleplay.DialogFunction;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.SendValue;
import com.jetec.nordic_googleplay.Service.BluetoothLeService;
import com.jetec.nordic_googleplay.Value;
import com.jetec.nordic_googleplay.ViewAdapter.Function;

import static java.lang.Thread.sleep;

public class PV {

    private Context context;
    private Function function;

    public PV(Context context, Function function) {
        this.context = context;
        this.function = function;
    }

    public void todo(Float t, String name, Dialog inDialog, BluetoothLeService bluetoothLeService, String gets) throws InterruptedException {
        SendValue sendValue = new SendValue(bluetoothLeService);
        String TAG = "PV";
        if (name.matches("PV1")) {
            if (Value.name.get(0).toString().matches("T")) {
                if (10 * t > 50 || 10 * t < -50) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if (Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if (Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            gets = String.valueOf(t);
                            int i = gets.indexOf(".");
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
                            if (Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(0).toString().matches("H")) {
                if (10 * t > 100 || 10 * t < -100) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(0).toString().matches("C")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(0).toString().matches("D")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(0).toString().matches("E")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(0).toString().matches("I")) {
                if (!Value.IDP1) {
                    if (10 * t > 9990 || 10 * t < -9990) {
                        Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    } else {
                        if (t == 0.0) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            String out = name + "+" + "0000.0";
                            Log.e(TAG, "out = " + out);
                            sendValue.send(out);
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        } else {
                            if (gets.startsWith("-")) {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
                                gets = String.valueOf(t);
                                int i = gets.indexOf(".");
                                Log.e(TAG, "gets = " + gets);
                                String num1 = gets.substring(1, gets.indexOf("."));
                                String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                StringBuilder set = new StringBuilder("0");
                                if (i != 4) {
                                    for (int j = 0; j < (4 - i); j++)
                                        set.append("0");
                                    String out = name + "-" + set + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                } else {
                                    String out = name + "-" + "0" + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                }
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            } else {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
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
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            }
                        }
                    }
                } else {
                    if (10 * t > 999 || 10 * t < -999) {
                        Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    } else {
                        if (t == 0.0) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            String out = name + "+" + "0000.0";
                            Log.e(TAG, "out = " + out);
                            sendValue.send(out);
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        } else {
                            if (gets.startsWith("-")) {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
                                gets = String.valueOf(t);
                                int i = gets.indexOf(".");
                                Log.e(TAG, "gets = " + gets);
                                String num1 = gets.substring(1, gets.indexOf("."));
                                String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                StringBuilder set = new StringBuilder("0");
                                if (i != 4) {
                                    for (int j = 0; j < (4 - i); j++)
                                        set.append("0");
                                    String out = name + "-" + set + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                } else {
                                    String out = name + "-" + "0" + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                }
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            } else {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
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
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            }
                        }
                    }
                }
            }
        } else if (name.matches("PV2")) {
            if (Value.name.get(1).toString().matches("T")) {
                if (10 * t > 50 || 10 * t < -50) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            gets = String.valueOf(t);
                            int i = gets.indexOf(".");
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(1).toString().matches("H")) {
                if (10 * t > 100 || 10 * t < -100) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(1).toString().matches("C")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(1).toString().matches("D")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(1).toString().matches("E")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(1).toString().matches("I")) {
                if (!Value.IDP2) {
                    if (10 * t > 9990 || 10 * t < -9990) {
                        Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    } else {
                        if (t == 0.0) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            String out = name + "+" + "0000.0";
                            Log.e(TAG, "out = " + out);
                            sendValue.send(out);
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        } else {
                            if (gets.startsWith("-")) {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
                                gets = String.valueOf(t);
                                int i = gets.indexOf(".");
                                Log.e(TAG, "gets = " + gets);
                                String num1 = gets.substring(1, gets.indexOf("."));
                                String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                StringBuilder set = new StringBuilder("0");
                                if (i != 4) {
                                    for (int j = 0; j < (4 - i); j++)
                                        set.append("0");
                                    String out = name + "-" + set + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                } else {
                                    String out = name + "-" + "0" + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                }
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            } else {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
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
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            }
                        }
                    }
                } else {
                    if (10 * t > 999 || 10 * t < -999) {
                        Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    } else {
                        if (t == 0.0) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            String out = name + "+" + "0000.0";
                            Log.e(TAG, "out = " + out);
                            sendValue.send(out);
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        } else {
                            if (gets.startsWith("-")) {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
                                gets = String.valueOf(t);
                                int i = gets.indexOf(".");
                                Log.e(TAG, "gets = " + gets);
                                String num1 = gets.substring(1, gets.indexOf("."));
                                String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                StringBuilder set = new StringBuilder("0");
                                if (i != 4) {
                                    for (int j = 0; j < (4 - i); j++)
                                        set.append("0");
                                    String out = name + "-" + set + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                } else {
                                    String out = name + "-" + "0" + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                }
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            } else {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
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
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            }
                        }
                    }
                }
            }
        } else if (name.matches("PV3")) {
            if (Value.name.get(2).toString().matches("T")) {
                if (10 * t > 50 || 10 * t < -50) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            gets = String.valueOf(t);
                            int i = gets.indexOf(".");
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(2).toString().matches("H")) {
                if (10 * t > 100 || 10 * t < -100) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(2).toString().matches("C")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(2).toString().matches("D")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(2).toString().matches("E")) {
                if (10 * t > 5000 || 10 * t < -5000) {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else {
                    if (t == 0.0) {
                        if(Value.downlog) {
                            sendValue.send("END");
                            sleep(100);
                            Value.downlog = false;
                        }
                        String out = name + "+" + "0000.0";
                        Log.e(TAG, "out = " + out);
                        sendValue.send(out);
                        function.notifyDataSetChanged();
                        inDialog.dismiss();
                    } else {
                        if (gets.startsWith("-")) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
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
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        }
                    }
                }
            } else if (Value.name.get(2).toString().matches("I")) {
                if (!Value.IDP3) {
                    if (10 * t > 9990 || 10 * t < -9990) {
                        Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    } else {
                        if (t == 0.0) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            String out = name + "+" + "0000.0";
                            Log.e(TAG, "out = " + out);
                            sendValue.send(out);
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        } else {
                            if (gets.startsWith("-")) {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
                                gets = String.valueOf(t);
                                int i = gets.indexOf(".");
                                Log.e(TAG, "gets = " + gets);
                                String num1 = gets.substring(1, gets.indexOf("."));
                                String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                StringBuilder set = new StringBuilder("0");
                                if (i != 4) {
                                    for (int j = 0; j < (4 - i); j++)
                                        set.append("0");
                                    String out = name + "-" + set + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                } else {
                                    String out = name + "-" + "0" + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                }
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            } else {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
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
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            }
                        }
                    }
                } else {
                    if (10 * t > 999 || 10 * t < -999) {
                        Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    } else {
                        if (t == 0.0) {
                            if(Value.downlog) {
                                sendValue.send("END");
                                sleep(100);
                                Value.downlog = false;
                            }
                            String out = name + "+" + "0000.0";
                            Log.e(TAG, "out = " + out);
                            sendValue.send(out);
                            function.notifyDataSetChanged();
                            inDialog.dismiss();
                        } else {
                            if (gets.startsWith("-")) {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
                                gets = String.valueOf(t);
                                int i = gets.indexOf(".");
                                Log.e(TAG, "gets = " + gets);
                                String num1 = gets.substring(1, gets.indexOf("."));
                                String num2 = gets.substring(gets.indexOf("."), gets.indexOf(".") + 2);
                                StringBuilder set = new StringBuilder("0");
                                if (i != 4) {
                                    for (int j = 0; j < (4 - i); j++)
                                        set.append("0");
                                    String out = name + "-" + set + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                } else {
                                    String out = name + "-" + "0" + num1 + num2;
                                    Log.e(TAG, "out = " + out);
                                    sendValue.send(out);
                                }
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            } else {
                                if(Value.downlog) {
                                    sendValue.send("END");
                                    sleep(100);
                                    Value.downlog = false;
                                }
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
                                function.notifyDataSetChanged();
                                inDialog.dismiss();
                            }
                        }
                    }
                }
            }
        }
    }
}
