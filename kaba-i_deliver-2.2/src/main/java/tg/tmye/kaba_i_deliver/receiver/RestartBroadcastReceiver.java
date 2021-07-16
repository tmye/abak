package tg.tmye.kaba_i_deliver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import tg.tmye.kaba_i_deliver.activity.command.MyCommandsActivity;
import tg.tmye.kaba_i_deliver.activity.readygo.DeliveryReadyActivity;
import tg.tmye.kaba_i_deliver.service.TrackingService;
import tg.tmye.kaba_i_deliver.syscore.ILog;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

/**
 * By abiguime on 2021/7/15.
 * email: 2597434002@qq.com
 */
public class RestartBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /* we check the mode of the deliveryman, if he's in delivery or not
         * before eventually restarting it */
        String deliveryMode = ((MyKabaDeliverApp)context.getApplicationContext()).getDeliveryMode();
        if ("on".equals(deliveryMode)) {
            ILog.print("Service Stopped, but this is a never ending service.");
//            context.startService(new Intent(context, TrackingService.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, TrackingService.class));
            } else {
                context.startService(new Intent(context, TrackingService.class));
            }
        }
    }
}