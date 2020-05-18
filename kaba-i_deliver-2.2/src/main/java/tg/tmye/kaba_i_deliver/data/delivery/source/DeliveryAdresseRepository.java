package tg.tmye.kaba_i_deliver.data.delivery.source;

import android.content.Context;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;


/**
 * By abiguime on 06/04/2018.
 * email: 2597434002@qq.com
 */

public class DeliveryAdresseRepository {

    private final Context context;

    NetworkRequestThreadBase networkRequestThreadBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;

    public DeliveryAdresseRepository(Context context) {
        this.context = context;
        /* get the threads */
        this.databaseRequestThreadBase = ((MyKabaDeliverApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaDeliverApp)context.getApplicationContext()).getNetworkRequestBase();
    }

}
