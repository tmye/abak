package tg.experta.kaba.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * By abiguime on 29/01/2018.
 * email: 2597434002@qq.com
 */

public class KabaCentralService extends Service {


    /**
     * first step make the service immortel pour gerer les notifications et
     * toutes les actions dont nous avons besoin dans le background
     **/
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}
