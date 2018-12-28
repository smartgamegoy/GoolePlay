package com.jetec.nordic_googleplay;

import android.util.Log;
import static android.support.constraint.Constraints.TAG;

public class GetDeviceNum {

    private String TAG = "GetDeviceNum";

    public GetDeviceNum(){
        super();
    }

    public String get(String str){

        String renum = "";

        if(str.startsWith("DP1")){
            if(str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")){
                Value.IDP1 = false;
                Log.e("getDEVICE","Value.IDP1 = " + Value.IDP1);
                renum = "Off";
            }
            else if(str.substring(str.indexOf("+") + 1, str.length()).matches("0001.0")){
                Value.IDP1 = true;
                Log.e("getDEVICE","Value.IDP1 = " + Value.IDP1);
                renum = "On";
            }
        }
        else if(str.startsWith("DP2")){
            if(str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")){
                Value.IDP2 = false;
                renum = "Off";
            }
            else if(str.substring(str.indexOf("+") + 1, str.length()).matches("0001.0")){
                Value.IDP2 = true;
                renum = "On";
            }
        }
        else if(str.startsWith("DP3")){
            if(str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")){
                Value.IDP3 = false;
                renum = "Off";
            }
            else if(str.substring(str.indexOf("+") + 1, str.length()).matches("0001.0")){
                Value.IDP3 = true;
                renum = "On";
            }
        }
        else if(str.startsWith("SPK")){
            if(str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")){
                renum = "Off";
            }
            else if(str.substring(str.indexOf("+") + 1, str.length()).matches("0001.0")){
                renum = "On";
            }
        }
        else if(str.startsWith("T")){
            if(str.contains("+")) {
                if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                    renum = "0";
                }
                else {
                    String newStr = str.substring(str.indexOf("+") + 1, str.length()).replaceFirst("^0*", "");
                    if (newStr.startsWith(".")) {
                        renum = "0" + newStr;
                    }
                    else {
                        renum = newStr;
                    }
                }
            }
            else if(str.contains("-")){
                if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                    renum = "0";
                }
                else {
                    String newStr = str.substring(str.indexOf("-") + 1, str.length()).replaceFirst("^0*", "");
                    if (newStr.startsWith(".")) {
                        renum = "-" + "0" + newStr;
                    }
                    else {
                        renum = "-" + newStr;
                    }
                }
            }
        }
        else{
            if(str.startsWith("IH1") || str.startsWith("IL1") || str.startsWith("PV1") || str.startsWith("EH1") ||
                    str.startsWith("EL1") || str.startsWith("CR1")) {
                if(Value.IDP1) {
                    if (str.contains("+")) {
                        if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                            renum = "0";
                        }
                        else {
                            String newStr = str.substring(str.indexOf("+") + 1, str.length())
                                    .replaceFirst("^0*", "");
                            if (newStr.startsWith(".")) {
                                renum = "0" + newStr;
                            } else {
                                renum = newStr;
                            }
                        }
                    }
                    else {
                        if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                            renum = "0";
                        }
                        else {
                            String newStr = str.substring(str.indexOf("-") + 1, str.length())
                                    .replaceFirst("^0*", "");
                            if (newStr.startsWith(".")) {
                                renum = "-" + "0" + newStr;
                            } else {
                                renum = "-" + newStr;
                            }
                        }
                    }
                }
                else {
                    if(Value.name.get(0).toString().matches("T")) {
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.length())
                                        .replaceFirst("^0*", "");
                                Log.e("這裡","newStr = " + newStr);
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    if(newStr.startsWith(".")){
                                        renum = "0" + newStr;
                                    }
                                    else {
                                        renum = newStr;
                                        Log.e(TAG, "renum = " + renum);
                                    }
                                }
                            }
                        } else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.length())
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    if(newStr.startsWith(".")){
                                        renum = "-" + "0" + newStr;
                                    }
                                    else {
                                        renum = "-" + newStr;
                                    }
                                }
                            }
                        }
                    }
                    else if(Value.name.get(0).toString().matches("H")){
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        }
                        else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                    else if(Value.name.get(0).toString().matches("C") ||
                            Value.name.get(0).toString().matches("D") ||
                            Value.name.get(0).toString().matches("E")){
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        }
                        else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                    else if(Value.name.get(0).toString().matches("I")){
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        }
                        else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                }
            }
            else if(str.startsWith("IH2") || str.startsWith("IL2") || str.startsWith("PV2") || str.startsWith("EH2") ||
                    str.startsWith("EL2") || str.startsWith("CR2")) {
                if(Value.IDP2) {
                    if (str.contains("+")) {
                        if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                            renum = "0";
                        }
                        else {
                            String newStr = str.substring(str.indexOf("+") + 1, str.length())
                                    .replaceFirst("^0*", "");
                            if (newStr.startsWith(".")) {
                                renum = "0" + newStr;
                            } else {
                                renum = newStr;
                            }
                        }
                    }
                    else {
                        if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                            renum = "0";
                        }
                        else {
                            String newStr = str.substring(str.indexOf("-") + 1, str.length())
                                    .replaceFirst("^0*", "");
                            if (newStr.startsWith(".")) {
                                renum = "-" + "0" + newStr;
                            } else {
                                renum = "-" + newStr;
                            }
                        }
                    }
                }
                else {
                    if(Value.name.get(1).toString().matches("T")){
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.length())
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    if(newStr.startsWith(".")){
                                        renum = "0" + newStr;
                                    }
                                    else {
                                        renum = newStr;
                                        Log.e(TAG, "renum = " + renum);
                                    }
                                }
                            }
                        } else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.length())
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    if(newStr.startsWith(".")){
                                        renum = "-" + "0" + newStr;
                                    }
                                    else {
                                        renum = "-" + newStr;
                                    }
                                }
                            }
                        }
                    }
                    else if(Value.name.get(1).toString().matches("H")) {
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        } else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                    else if(Value.name.get(1).toString().matches("C") ||
                            Value.name.get(1).toString().matches("D") ||
                            Value.name.get(1).toString().matches("E")){
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        } else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                    else if(Value.name.get(0).toString().matches("I")){
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        }
                        else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                }
            }
            else if(str.startsWith("IH3") || str.startsWith("IL3") || str.startsWith("PV3") || str.startsWith("EH3") ||
                    str.startsWith("EL3") || str.startsWith("CR3")) {
                if(Value.IDP3) {
                    if (str.contains("+")) {
                        if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                            renum = "0";
                        }
                        else {
                            String newStr = str.substring(str.indexOf("+") + 1, str.length())
                                    .replaceFirst("^0*", "");
                            if (newStr.startsWith(".")) {
                                renum = "0" + newStr;
                            } else {
                                renum = newStr;
                            }
                        }
                    }
                    else {
                        if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                            renum = "0";
                        }
                        else {
                            String newStr = str.substring(str.indexOf("-") + 1, str.length())
                                    .replaceFirst("^0*", "");
                            if (newStr.startsWith(".")) {
                                renum = "-" + "0" + newStr;
                            } else {
                                renum = "-" + newStr;
                            }
                        }
                    }
                }
                else {
                    if(Value.name.get(2).toString().matches("T")) {
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.length())
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    if(newStr.startsWith(".")){
                                        renum = "0" + newStr;
                                    }
                                    else {
                                        renum = newStr;
                                        Log.e(TAG, "renum = " + renum);
                                    }
                                }
                            }
                        } else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.length())
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    if(newStr.startsWith(".")){
                                        renum = "-"+ "0" + newStr;
                                    }
                                    else {
                                        renum = "-" + newStr;
                                    }
                                }
                            }
                        }
                    }
                    else if(Value.name.get(2).toString().matches("H")) {
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        } else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                    else if(Value.name.get(2).toString().matches("C") ||
                            Value.name.get(2).toString().matches("D") ||
                            Value.name.get(2).toString().matches("E")){
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        } else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            } else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                    else if(Value.name.get(0).toString().matches("I")){
                        if (str.contains("+")) {
                            if (str.substring(str.indexOf("+") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("+") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = newStr;
                                }
                            }
                        }
                        else {
                            if (str.substring(str.indexOf("-") + 1, str.length()).matches("0000.0")) {
                                renum = "0";
                            }
                            else {
                                String newStr = str.substring(str.indexOf("-") + 1, str.indexOf("."))
                                        .replaceFirst("^0*", "");
                                if (newStr.matches("")) {
                                    renum = "0";
                                } else {
                                    renum = "-" + newStr;
                                }
                            }
                        }
                    }
                }
            }
            else {
                if(str.startsWith("INTER")){
                    str = str.substring(str.indexOf("INTER") + 5, str.length());
                    int i = Integer.valueOf(str);
                    if(i == 3600)
                        renum = "1h";
                    else if(60 <= i && i < 3600){
                        int j = i / 60;
                        int k = i % 60;
                        if(k == 0)
                            renum = String.valueOf(j) + "m";
                        else
                            renum = String.valueOf(j) + "m" + String.valueOf(k) + "s";
                    }
                    else
                        renum = String.valueOf(i) + "s";
                }
            }
        }
        return renum;
    }
}
