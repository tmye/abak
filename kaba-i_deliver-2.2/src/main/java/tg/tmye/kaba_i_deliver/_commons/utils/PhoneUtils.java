package tg.tmye.kaba_i_deliver._commons.utils;

import android.os.Build;

/**
 * By abiguime on 24/04/2018.
 * email: 2597434002@qq.com
 */

public class PhoneUtils {

    public static String getPhoneData() {
        /*
        * System.getProperty("os.version"); // OS version
            android.os.Build.VERSION.SDK      // API Level
            android.os.Build.DEVICE           // Device
            android.os.Build.MODEL            // Model
            android.os.Build.PRODUCT
        * */

        String metadata = "";
        metadata += "OS VERSION: " + System.getProperty("os.version") + "\n";
        metadata += "VERSION.SDK: " + Build.VERSION.SDK + "\n";
        metadata += "Build.DEVICE: " + Build.DEVICE + "\n";
        metadata += "Build.MODEL: " + Build.MODEL + "\n";
        metadata += "Build.PRODUCT: " + Build.PRODUCT + "\n";
        return metadata;
    }

}
