package tg.tmye.kaba_i_deliver.syscore;

import android.util.Log;

/**
 * By abiguime on 2020/4/27.
 * email: 2597434002@qq.com
 */
public class ILog{

    public static void print(String message) {
        if (Constant.DEBUG)
            Log.d(Constant.APP_TAG, message);
    }
}
