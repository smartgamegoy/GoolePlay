package com.jetec.nordic_googleplay;

public class CheckDeviceName {

    public CheckDeviceName(){
        super();
    }

    public String setName(String name){

        String rename = "";

        if(name.startsWith("PV1")){
            rename = "PV1";
        }
        else if(name.startsWith("PV2")){
            rename = "PV2";
        }
        else if(name.startsWith("PV3")){
            rename = "PV3";
        }
        else if(name.startsWith("EH1")){
            rename = "EH1";
        }
        else if(name.startsWith("EH2")){
            rename = "EH2";
        }
        else if(name.startsWith("EH3")){
            rename = "EH3";
        }
        else if(name.startsWith("EL1")){
            rename = "EL1";
        }
        else if(name.startsWith("EL2")){
            rename = "EL2";
        }
        else if(name.startsWith("EL3")){
            rename = "EL3";
        }
        else if(name.startsWith("CR1")){
            rename = "CR1";
        }
        else if(name.startsWith("CR2")){
            rename = "CR2";
        }
        else if(name.startsWith("CR3")){
            rename = "CR3";
        }
        else if(name.startsWith("ADR")){
            rename = "ADR";
        }
        else if(name.startsWith("INTER")){
            rename = "INTER";
        }
        else if(name.startsWith("OVER")){
            rename = "OVER";
        }
        else if(name.startsWith("IH1")){
            rename = "IH1";
        }
        else if(name.startsWith("IH2")){
            rename = "IH2";
        }
        else if(name.startsWith("IH3")){
            rename = "IH3";
        }
        else if(name.startsWith("IL1")){
            rename = "IL1";
        }
        else if(name.startsWith("IL2")){
            rename = "IL2";
        }
        else if(name.startsWith("IL3")){
            rename = "IL3";
        }
        else if(name.startsWith("SPK")){
            rename = "SPK";
        }
        else if(name.startsWith("DP1")){
            rename = "DP1";
        }
        else if(name.startsWith("DP2")){
            rename = "DP2";
        }
        else if(name.startsWith("DP3")){
            rename = "DP3";
        }

        return rename;
    }
}
