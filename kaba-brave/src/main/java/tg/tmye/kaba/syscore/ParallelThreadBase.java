package tg.tmye.kaba.syscore;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import tg.tmye.kaba.config.Config;

/**
 * By abiguime on 04/07/2018.
 * email: 2597434002@qq.com
 */
public class ParallelThreadBase {


    private final Context ctx;
    private final Handler mHandler;

    public ParallelThreadBase(Context ctx){
        this.ctx = ctx;

        HandlerThread thread = new HandlerThread(Config.KABA_PARA_THREAD);
        thread.start();
        mHandler = new Handler(thread.getLooper());
    }


    public Handler getmHandler() {
        return mHandler;
    }
}
