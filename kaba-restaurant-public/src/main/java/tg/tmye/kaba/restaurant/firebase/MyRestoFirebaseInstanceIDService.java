package tg.tmye.kaba.restaurant.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.syscore.Config;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;

import static tg.tmye.kaba.restaurant.syscore.Constant.APP_TAG;

/**
 * By abiguime on 30/05/2018.
 * email: 2597434002@qq.com
 */

public class MyRestoFirebaseInstanceIDService extends FirebaseMessagingService {

    private String TAG = "MyRestoFirebaseInstanceIDService";

    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        super.onNewToken(refreshedToken);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(Config.PHONE_IS_OK_WITH_SERVER, false);
        edit.putString(Config.PHONE_FIREBASE_PUSH_TOKEN, refreshedToken);
        edit.commit();

        JSONObject object = new JSONObject();
        try {
            object.put("os_version", System.getProperty("os.version"));
            object.put("build_device", Build.DEVICE);
            object.put("version_sdk", Build.VERSION.SDK);
            object.put("build_model", Build.MODEL);
            object.put("build_product", Build.PRODUCT);
            object.put("push_token", refreshedToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* send the string by push */

        NetworkRequestThreadBase networkRequestBase = ((MyRestaurantApp) getApplicationContext()).getNetworkRequestBase();
        networkRequestBase.postJsonData(Config.LINK_PHONE_UPDATE_SERVER_PUSH_TOKEN,
                object.toString(),
                new NetworkRequestThreadBase.NetRequestIntf<String>() {

                    @Override
                    public void onNetworkError() {}

                    @Override
                    public void onSysError() {}

                    @Override
                    public void onSuccess(String jsonResponse) {
                        /* save a local system error to the database */
                    }

                });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d( TAG, TAG+" onCreate");
    }

}
