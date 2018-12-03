package tg.tmye.kaba.activity.home.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 22/05/2018.
 * email: 2597434002@qq.com
 */

public class SystemUtils {

    public static void refreshPushTokenIfNeeded(final Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);
        /* quit thread if phone is ok with server*/
        if (sharedPreferences.getBoolean(Config.PHONE_IS_OK_WITH_SERVER, false)) {
            return;
        }


        /* get refresh token */
        SharedPreferences preferences = context.getSharedPreferences("com.google.android.gms.appid", Context.MODE_PRIVATE);

        final String jsonRefreshedToken = preferences.getString("|T|7649846363|*", "");


        /* quit thread if no string been found yet */
        if ("".equals(jsonRefreshedToken)){
            return;
        }

        final JsonObject jsonObject = new JsonParser().parse(jsonRefreshedToken).getAsJsonObject();

        JSONObject object = new JSONObject();
        try {
            object.put("os_version", System.getProperty("os.version"));
            object.put("build_device", Build.DEVICE);
            object.put("version_sdk", Build.VERSION.SDK);
            object.put("build_model", Build.MODEL);
            object.put("build_product", Build.PRODUCT);
            object.put("push_token", jsonObject.get("token").getAsString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* send the string by push */

        NetworkRequestThreadBase networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
        networkRequestBase.postJsonData(Config.LINK_REGISTER_PUSH_TOKEN,
                object.toString(),
                new NetworkRequestThreadBase.NetRequestIntf() {
                    @Override
                    public void onNetworkError() {
                    }

                    @Override
                    public void onSysError() {
                    }

                    @Override
                    public void onSuccess(Object jsonResponse) {
                        /* save a local system error to the database */
                        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putBoolean(Config.PHONE_IS_OK_WITH_SERVER, true);
                        edit.putString(Config.PHONE_FIREBASE_PUSH_TOKEN, jsonObject.get("token").getAsString());
                        edit.commit();
                    }
                });
    }


}
