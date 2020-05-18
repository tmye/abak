package tg.tmye.kaba_i_deliver.data.delivery.source;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.command.CommandDetailsActivity;
import tg.tmye.kaba_i_deliver.activity.command.MyCommandsActivity;
import tg.tmye.kaba_i_deliver.activity.delivery.DeliveryModeActivity;
import tg.tmye.kaba_i_deliver.activity.login.DeliverManLoginActivity;
import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba_i_deliver.data.delivery.KabaShippingMan;
import tg.tmye.kaba_i_deliver.syscore.Config;
import tg.tmye.kaba_i_deliver.syscore.Constant;
import tg.tmye.kaba_i_deliver.syscore.ILog;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;


/**
 * By abiguime on 06/04/2018.
 * email: 2597434002@qq.com
 */

public class DeliveryManRepository {

    public static final String TAG = "DeliveryManRepository";

    private final Context context;

    NetworkRequestThreadBase networkRequestThreadBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;

    public DeliveryManRepository(Context context) {
        this.context = context;
        /* get the threads */
        this.databaseRequestThreadBase = ((MyKabaDeliverApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaDeliverApp)context.getApplicationContext()).getNetworkRequestBase();
    }

    /*  */
      /* login restaurant activity */
    public void login (String username, String password, NetworkRequestThreadBase.NetRequestIntf intf) {

        Map<String, String> params = new HashMap<>();
        params.put("_username", username);
        params.put("_password", password);

           /* send login params by post */
        networkRequestThreadBase.postWithParams (Config.LINK_DELIVERMAN_LOGIN, params, intf);
    }


    public void saveToken(String token) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Config.SYSTOKEN, token);
        edit.commit();
    }

    public void saveDeliverman (KabaShippingMan kabaShippingMan) {

        /* persist restaurant informations locally */
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(Config.DELIVERMAN_ID, kabaShippingMan.id);
        edit.putString(Config.DELIVERMAN_NAME, kabaShippingMan.name);
        edit.putString(Config.VEHICULE_SERIAL, kabaShippingMan.vehicle_serial_code);
        edit.commit();
    }

    public void setIsNotOkWithServer() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(Config.PHONE_IS_OK_WITH_SERVER, false);
        edit.commit();
    }

    public void sendPushData() {
    /*  */
        final SharedPreferences sharedPreferences = context.getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(Config.PHONE_IS_OK_WITH_SERVER, false)) {
            return;
        }

        String push_token = sharedPreferences.getString(Config.PHONE_FIREBASE_PUSH_TOKEN, "");
        if ("".equals(push_token)) return;

        JSONObject object = new JSONObject();
        try {
            object.put("os_version", System.getProperty("os.version"));
            object.put("build_device", Build.DEVICE);
            object.put("version_sdk", Build.VERSION.SDK);
            object.put("build_model", Build.MODEL);
            object.put("build_product", Build.PRODUCT);
            object.put("push_token", push_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String token = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();

        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_PHONE_UPDATE_SERVER_PUSH_TOKEN, object.toString(), token, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                Log.d(TAG, "posting firebase push token - failure");
            }

            @Override
            public void onSysError() {
                Log.d(TAG, "posting firebase push token - failure");
            }

            @Override
            public void onSuccess(String jsonResponse) {

                ILog.print(jsonResponse);
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int errorCode = obj.get("error").getAsInt();
                    if (errorCode == 0) {
                        Log.d(TAG, "posting firebase push token - success");
                /* set success into the sharedprefs */
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putBoolean(Config.PHONE_IS_OK_WITH_SERVER, true);
                        edit.apply();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadDistanceOptimizedDeliverList(final NetworkRequestThreadBase.NetRequestIntf<String> intf) {


        /* check locally if im in delivery mode. */

        JSONObject object = new JSONObject();
        try {
            object.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String token = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();

        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_DELIVER_MAN_START_SHIPPING, object.toString(), token, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                Log.d(TAG, "retrieving optimized list");
                intf.onNetworkError();
            }

            @Override
            public void onSysError() {
                Log.d(TAG, "retrieving optimized list");
                intf.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {
                Log.d(TAG, jsonResponse);
//                intf.onSuccess(jsonResponse);
                enterDeliverMode();
                /* if successfull, then do the other one */
                /* register into my shared preferences that im on delivering mode even if i go out and come back. */
                intf.onSuccess(jsonResponse);
            }
        });
    }


    public static void deleteDelivermanInfos(Context context) {

        SharedPreferences sp = context.getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Config.DELIVERMAN_ID);
        editor.remove(Config.DELIVERMAN_NAME);
        editor.remove(Config.DELIVERMAN_ICON_PICTURE);
        editor.remove(Config.DELIVERMAN_EMAIL);
        editor.remove(Config.DELIVERMAN_DESCRIPTION);
        editor.remove(Config.DELIVERMAN_WORKING_HOUR);
        editor.remove(Config.DELIVERMAN_THEME);
        editor.remove(Config.DELIVERMAN_ADDRESS);
        editor.remove(Config.SYSTOKEN);
        editor.remove(Config.VEHICULE_SERIAL);
        editor.commit();
    }

    public static void deleteToken(Context context) {
        ((MyKabaDeliverApp)context.getApplicationContext()).setAuthToken("");
        // delete token stuffs
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Config.PHONE_IS_OK_WITH_SERVER);
        editor.commit();
    }

    private void enterDeliverMode() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(Config.DELIVERMAN_SHIPPING_MODE_ENABLED, true);
        edit.commit();
    }

    public static void exitDeliveryMode(final Context context, final NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        /* make online request - if ok... do ok */
//        /livreur/api/stop-shipping
        String token = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();

        NetworkRequestThreadBase networkRequestThreadBase = ((MyKabaDeliverApp)context.getApplicationContext()).getNetworkRequestBase();

        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_DELIVER_MAN_STOP_SHIPPING, "", token, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                Log.d(TAG, "retrieving optimized list");
            }

            @Override
            public void onSysError() {
                Log.d(TAG, "retrieving optimized list");
            }

            @Override
            public void onSuccess(String jsonResponse) {
                Log.d(TAG, jsonResponse);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int error = obj.get("error").getAsInt();
                if (error == 0) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.remove(Config.DELIVERMAN_SHIPPING_MODE_ENABLED);
                    edit.commit();
                    intf.onSuccess("");
                } else {
                    intf.onSysError();
                }
            }
        });

    }

    public static void checkIsInDeliveryMode(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Config.DELIVERMAN_SHIPPING_MODE_ENABLED, false)) {
            // yes
            context.startActivity(new Intent(context, DeliveryModeActivity.class));
            ((MyCommandsActivity)context).finish();
        }
    }

    public static KabaShippingMan getShippingMan(Context context) {

        KabaShippingMan kabaShippingMan = new KabaShippingMan();
        SharedPreferences sp = context.getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, Context.MODE_PRIVATE);
        kabaShippingMan.id = sp.getInt(Config.DELIVERMAN_ID, -1);
        kabaShippingMan.name = sp.getString(Config.DELIVERMAN_NAME, "");
        kabaShippingMan.vehicle_serial_code = sp.getString(Config.VEHICULE_SERIAL, "");
        return kabaShippingMan;
    }
}
