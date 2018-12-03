package tg.tmye.kaba.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 24/04/2018.
 * email: 2597434002@qq.com
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = "MyFirebaseInstanceIDService";


    /* metadata += "VERSION.SDK: " + android.os.Build.VERSION.SDK + "\n";
        metadata += "Build.DEVICE: " + Build.DEVICE + "\n";
        metadata += "Build.MODEL: " + Build.MODEL + "\n";
        metadata += "Build.PRODUCT: " + Build.PRODUCT + "\n";*/

    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        /* save token locally */
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

        NetworkRequestThreadBase networkRequestBase = ((MyKabaApp) getApplicationContext()).getNetworkRequestBase();
        networkRequestBase.postJsonData(Config.LINK_REGISTER_PUSH_TOKEN,
                object.toString(),
                new NetworkRequestThreadBase.NetRequestIntf() {

                    @Override
                    public void onNetworkError() {}

                    @Override
                    public void onSysError() {}

                    @Override
                    public void onSuccess(Object jsonResponse) {
                        /* save a local system error to the database */
                    }

                });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constant.APP_TAG, TAG+" onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constant.APP_TAG, TAG+" onDestroy");
    }

}
