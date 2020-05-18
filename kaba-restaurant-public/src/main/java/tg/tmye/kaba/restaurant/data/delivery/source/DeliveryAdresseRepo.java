package tg.tmye.kaba.restaurant.data.delivery.source;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba.restaurant._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;


/**
 * By abiguime on 06/04/2018.
 * email: 2597434002@qq.com
 */

public class DeliveryAdresseRepo {

    private final Context context;

    NetworkRequestThreadBase networkRequestThreadBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;

    public DeliveryAdresseRepo (Context context) {
        this.context = context;
        /* get the threads */
        this.databaseRequestThreadBase = ((MyRestaurantApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyRestaurantApp)context.getApplicationContext()).getNetworkRequestBase();
    }

}
