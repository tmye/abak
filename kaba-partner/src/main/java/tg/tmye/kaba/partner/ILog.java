package tg.tmye.kaba.partner;


import android.util.Log;

import tg.tmye.kaba.partner.syscore.Constant;

/**
 * By abiguime on 2020/4/27.
 * email: 2597434002@qq.com
 */
public class ILog{

    public static void print(String message) {
        if (Constant.DEBUG)
            Log.d(Constant.APP_TAG, message);
    }

    public static void e(String message) {
        if (Constant.DEBUG)
            Log.e(Constant.APP_TAG, message);
    }
}
