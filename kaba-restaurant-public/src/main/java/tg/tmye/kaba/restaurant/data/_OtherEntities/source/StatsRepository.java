package tg.tmye.kaba.restaurant.data._OtherEntities.source;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba.restaurant._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.syscore.Config;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;

/**
 * By abiguime on 08/08/2018.
 * email: 2597434002@qq.com
 */
public class StatsRepository {

    private final Context context;
    private final NetworkRequestThreadBase networkRequestBase;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;

    Gson gson = new Gson();

    public StatsRepository (Context context) {
        this.context = context;
        /* get threads */
        networkRequestBase = ((MyRestaurantApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyRestaurantApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }

    public void load7LastDaysStats(NetworkRequestThreadBase.NetRequestIntf netRequestIntf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject obj = new JSONObject();

        networkRequestBase.postJsonDataWithToken(Config.LINK_GET_RESTAURANT_7_STATS, obj.toString(), authToken, netRequestIntf);
    }

    public void searchStaticsFromToDate(String fromDate, String endDate, NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {
        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject obj = new JSONObject();

        try {
            obj.put("search", 1);
            obj.put("start_date", fromDate);
            obj.put("end_date", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        networkRequestBase.postJsonDataWithToken(Config.LINK_GET_RESTAURANT_STATS_FROM_TO_DATE, obj.toString(), authToken, netRequestIntf);
    }
}
