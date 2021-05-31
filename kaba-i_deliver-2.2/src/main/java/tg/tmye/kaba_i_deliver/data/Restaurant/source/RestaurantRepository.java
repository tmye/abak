package tg.tmye.kaba_i_deliver.data.Restaurant.source;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.syscore.Config;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

/**
 * By abiguime on 2020/7/31.
 * email: 2597434002@qq.com
 */
public class RestaurantRepository {

    public static final String TAG = "RestaurantRepository";

    private final Context context;

    NetworkRequestThreadBase networkRequestThreadBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;

    public RestaurantRepository(Context context) {
        this.context = context;
        /* get the threads */
        this.databaseRequestThreadBase = ((MyKabaDeliverApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaDeliverApp)context.getApplicationContext()).getNetworkRequestBase();
    }

    public void loadKabaRestaurant (NetworkRequestThreadBase.NetRequestIntf netRequestIntf) {

        String authToken = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();
        String last_location = "";
        JSONObject jsonObject = new JSONObject();

        LatLng lastLocation = ((MyKabaDeliverApp) context.getApplicationContext()).getLastLocation();
        if (lastLocation != null) {
            if (last_location.equals(lastLocation.latitude+":"+lastLocation.longitude) && lastLocation == null)
                netRequestIntf.onSuccess("-1");
            last_location = (lastLocation.latitude+":"+lastLocation.longitude);

            try {
                jsonObject.put("location", last_location);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_RESTAURANT_LIST, jsonObject.toString(), authToken, netRequestIntf);
    }

}
