package tg.tmye.kaba.restaurant.data.Restaurant.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba.restaurant._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.activities.home.HomeActivity;
import tg.tmye.kaba.restaurant.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.restaurant.syscore.Config;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;
import tg.tmye.kaba.restaurant.R;


/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public class RestaurantOnlineRepository {

    private static final String TAG =  "RORepository";
    private final Context context;

    DatabaseRequestThreadBase databaseRequestThreadBase;
    NetworkRequestThreadBase networkRequestThreadBase;


    public RestaurantOnlineRepository(Context context) {
        this.context = context;
        this.databaseRequestThreadBase = ((MyRestaurantApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyRestaurantApp)context.getApplicationContext()).getNetworkRequestBase();
    }

    /* login restaurant activity */
    public void login (String username, String password, NetworkRequestThreadBase.NetRequestIntf intf) {

        Map<String, String> params = new HashMap<>();
        params.put("_username", username);
        params.put("_password", password);

        /* send login params by post */
        networkRequestThreadBase.postWithParams (Config.LINK_RESTAURANT_LOGIN, params, intf);
    }


    public void saveToken(String token) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.RESTAURANT_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Config.SYSTOKEN, token);
        edit.commit();
    }

    public void saveRestaurant(RestaurantEntity restaurantEntity) {

        /* persist restaurant informations locally */
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.RESTAURANT_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(Config.RESTAURANT_ID, restaurantEntity.id);
        edit.putString(Config.RESTAURANT_NAME, restaurantEntity.name);
        edit.putString(Config.RESTAURANT_EMAIL, restaurantEntity.email);
        edit.putString(Config.RESTAURANT_ADDRESS, restaurantEntity.address);
        edit.putString(Config.RESTAURANT_ICON_PICTURE, restaurantEntity.pic);
        edit.putString(Config.RESTAURANT_THEME, restaurantEntity.theme_pic);
        edit.putString(Config.RESTAURANT_DESCRIPTION, restaurantEntity.description);
        edit.putString(Config.RESTAURANT_WORKING_HOUR, restaurantEntity.working_hour);
        edit.commit();
    }


    public RestaurantEntity getRestaurant() {

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        /* persist restaurant informations locally */
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.RESTAURANT_SHARED_PREFS, Context.MODE_PRIVATE);
        restaurantEntity.id = sharedPreferences.getInt(Config.RESTAURANT_ID,0);
        restaurantEntity.name =  sharedPreferences.getString(Config.RESTAURANT_NAME, restaurantEntity.name);
        return restaurantEntity;
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

        String token = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();

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

                Log.d(Constant.APP_TAG, jsonResponse);
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

    public static void deleteRestaurantInfos(Context context) {

        SharedPreferences sp = context.getSharedPreferences(Config.RESTAURANT_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Config.RESTAURANT_ID);
        editor.remove(Config.RESTAURANT_NAME);
        editor.remove(Config.RESTAURANT_EMAIL);
        editor.remove(Config.RESTAURANT_ADDRESS);
        editor.remove(Config.RESTAURANT_ICON_PICTURE);
        editor.remove(Config.RESTAURANT_THEME);
        editor.remove(Config.RESTAURANT_DESCRIPTION);
        editor.remove(Config.RESTAURANT_WORKING_HOUR);
        editor.remove(Config.SYSTOKEN);
        editor.commit();
        /* remove token */
    }

    public static void deleteToken(Context context) {

        ((MyRestaurantApp)context.getApplicationContext()).setAuthToken("");
    }
}


