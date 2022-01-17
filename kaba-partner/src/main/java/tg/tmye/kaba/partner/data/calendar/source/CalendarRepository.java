package tg.tmye.kaba.partner.data.calendar.source;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba.partner._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba.partner._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.partner.data.calendar.CalendarModel;
import tg.tmye.kaba.partner.syscore.Config;
import tg.tmye.kaba.partner.syscore.MyRestaurantApp;

/**
 * By abiguime on 2021/9/28.
 * email: 2597434002@qq.com
 */
public class CalendarRepository {


        private final Context context;
        private final NetworkRequestThreadBase networkRequestBase;
        private final DatabaseRequestThreadBase databaseRequestThreadBase;

        Gson gson = new Gson();

        public CalendarRepository (Context context) {
            this.context = context;
            /* get threads */
            networkRequestBase = ((MyRestaurantApp) context.getApplicationContext()).getNetworkRequestBase();
            databaseRequestThreadBase = ((MyRestaurantApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
        }

    public void updateCalendar(CalendarModel[] calendar, NetworkRequestThreadBase.NetRequestIntf<String> intf) {
        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject obj = new JSONObject();

        try {
            for (int i = 0; i < calendar.length; i++) {
                JSONObject tmp = new JSONObject();
                tmp.put("open", ""+calendar[i].open);
                tmp.put("pause", ""+calendar[i].pause);
                tmp.put("start", ""+calendar[i].start);
                tmp.put("end", ""+calendar[i].end);
                tmp.put("pause_start", ""+calendar[i].pause_start);
                tmp.put("pause_end", ""+calendar[i].pause_end);
                obj.put(calendar[i].day_name, tmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        networkRequestBase.postJsonDataWithToken(Config.LINK_UPDATE_RESTAURANT_CALENDAR, obj.toString(), authToken, intf);
    }

    public void loadCalendar(NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject obj = new JSONObject();

        networkRequestBase.postJsonDataWithToken(Config.LINK_GET_RESTAURANT_CALENDAR, obj.toString(), authToken, intf);
    }


/*
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
        }*/
}
