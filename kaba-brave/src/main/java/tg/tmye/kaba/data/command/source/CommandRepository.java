package tg.tmye.kaba.data.command.source;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class CommandRepository {

    private final Context context;
    private final NetworkRequestThreadBase networkRequestBase;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;

    Gson gson = new Gson();

    public CommandRepository (Context context) {
        this.context = context;

        /* get threads */
        networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyKabaApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }

    public void getUpdateCommandList(NetworkRequestThreadBase.AuthNetRequestIntf intf) {

        /* send back a map that contains restaurants as a key, and list of basketItem as value */


        /* give my token also */
        Map<String, Object> map = new HashMap<String, Object>();
        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase().postMapDataWithToken(
                Config.LINK_MY_COMMANDS_GET_CURRENT, map, token, intf
        );

    }

    public void purchaseNow(String code, HashMap<Restaurant_Menu_FoodEntity, Integer> foods_command, DeliveryAddress address, NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

         /*
          * -parameters: {
            "transaction_password": string,
            "device": {
                "push_token": string,
                "os_version": sting,
                "build_device": string,
                "version_sdk": int,
                "build_model": string,
                "build_product": string
            },
            "food_command" : [
                    {"food_id": int,"quantity": int}
                ],
            "shipping_address" : int
            }
           * */


        JSONArray dataArray = new JSONArray();

        Object[] keys = foods_command.keySet().toArray();

        for (Object key : keys) {
            Restaurant_Menu_FoodEntity entity = (Restaurant_Menu_FoodEntity) key;
            int quantity = foods_command.get(key);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("food_id", entity.id);
                jsonObject.put("quantity", quantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataArray.put(jsonObject);
        }

        String refreshedToken = CustomerDataRepository.getDevicePushToken(context);

        JSONObject object = new JSONObject();

        try {

            /* get device information */
            JSONObject device_object = new JSONObject();
            device_object.put("os_version", System.getProperty("os.version"));
            device_object.put("build_device", Build.DEVICE);
            device_object.put("version_sdk", Build.VERSION.SDK);
            device_object.put("build_model", Build.MODEL);
            device_object.put("build_product", Build.PRODUCT);
            device_object.put("push_token", refreshedToken);

            // total stuff
            object.put("device", device_object);
            object.put("food_command", dataArray);
            object.put("shipping_address", address.id);
            object.put("transaction_password", code);

            /* encode password then send */
//            "transaction_password": string,

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(Constant.APP_TAG, " + + "+    object.toString());

        String authToken = ((MyKabaApp) context.getApplicationContext()).getAuthToken();

        networkRequestBase.postJsonDataWithToken(Config.LINK_CREATE_COMMAND, object.toString(), authToken, netRequestIntf);
    }

    public void getCommandDetails(String command_id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyKabaApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("command_id",command_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_GET_COMMAND_DETAILS, command_obj.toString(), authToken, intf);
    }

    public void checKRestaurantIsOpen (RestaurantEntity restaurantEntity, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyKabaApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("restaurant_id",restaurantEntity.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_CHECK_RESTAURANT_IS_OPEN, command_obj.toString(), authToken, intf);
    }

    public void computePricing(RestaurantEntity restaurantEntity, Map<Restaurant_Menu_FoodEntity, Integer> foods_command, DeliveryAddress deliveryAddress, NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {

        JSONArray dataArray = new JSONArray();

        Object[] keys = foods_command.keySet().toArray();

        for (Object key : keys) {
            Restaurant_Menu_FoodEntity entity = (Restaurant_Menu_FoodEntity) key;
            int quantity = foods_command.get(key);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("food_id", entity.id);
                jsonObject.put("quantity", quantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataArray.put(jsonObject);
        }

        JSONObject main_object = new JSONObject();
        try {
            main_object.put("food_command", dataArray);
            main_object.put( "restaurant_id", restaurantEntity.id);
            main_object.put("shipping_address", deliveryAddress.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*  */
            String authToken = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        networkRequestBase.postJsonDataWithToken(Config.LINK_COMPUTE_BILLING, main_object.toString(), authToken, netRequestIntf);
    }

    public void getAllCommandListFrom(int from, NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        String link = Config.LINK_GET_ALL_COMMAND_LIST+"?from="+from;
        String authToken = ((MyKabaApp)context.getApplicationContext()).getAuthToken();
        networkRequestBase.getDataWithToken(link, null, authToken, netRequestIntf);
    }
}
