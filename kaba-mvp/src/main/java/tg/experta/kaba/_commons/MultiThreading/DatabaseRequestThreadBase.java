package tg.experta.kaba._commons.MultiThreading;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tg.experta.kaba.config.Config;

/**
 * By abiguime on 21/12/2017.
 * email: 2597434002@qq.com
 */

public class DatabaseRequestThreadBase {


    Context ctx;

    Handler mHandler;

    public DatabaseRequestThreadBase(Context ctx){
        this.ctx = ctx;

        HandlerThread thread = new HandlerThread(Config.KABA_CUSTOM_DATABASE_THREAD);
        thread.start();
        mHandler = new Handler(thread.getLooper());
    }

    public void run (final OnDbTrans intf) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                intf.action();
            }
        });
    }


    public interface OnDbTrans {

        void action();
    }
}
