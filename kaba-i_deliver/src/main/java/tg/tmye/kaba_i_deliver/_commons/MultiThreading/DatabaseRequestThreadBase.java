package tg.tmye.kaba_i_deliver._commons.MultiThreading;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import tg.tmye.kaba_i_deliver.syscore.Config;


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

    public void run(final OnDbTrans intf) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                intf.run();
            }
        });
    }


    public interface OnDbTrans {

        void run();
    }
}
