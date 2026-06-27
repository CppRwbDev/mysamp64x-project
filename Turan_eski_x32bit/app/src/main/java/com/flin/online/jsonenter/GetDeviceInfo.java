package com.flin.online.jsonenter;

import android.annotation.SuppressLint;
import android.os.Build;

public class GetDeviceInfo {


    @SuppressLint("MissingPermission")
    public String GetDeviceInfo() {
        String model = Build.MODEL;
        String brand = Build.BRAND;
        int sdk = Build.VERSION.SDK_INT;
        String modelID = android.os.Build.ID;

        return brand+" "+model+" "+sdk+" "+modelID;
    }
}