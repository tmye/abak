package tg.tmye.kaba.data.Restaurant.source;

import android.content.Context;

import com.google.gson.Gson;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public class RestaurantOnlineRepository {

    private final Context context;

    DatabaseRequestThreadBase databaseRequestThreadBase;
    NetworkRequestThreadBase networkRequestThreadBase;

    private Gson gson = new Gson();

    public RestaurantOnlineRepository(Context context) {
        this.context = context;
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
    }

    /* */

}
