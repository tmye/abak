package tg.tmye.kaba.data.command.source;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data._OtherEntities.Error;
import tg.tmye.kaba.data.command.Command;
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

    public void getUpdateCommandList(final YesOrNoWithResponse yesOrNoWithResponse) {

        /* send back a map that contains restaurants as a key, and list of basketItem as value */
        ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase().run(
                Config.LINK_MY_COMMANDS, new NetworkRequestThreadBase.NetRequestIntf<String>() {
                    @Override
                    public void onNetworkError() {
                        yesOrNoWithResponse.no(new Error.NetworkError(), true);
                    }

                    @Override
                    public void onSysError() {
                        yesOrNoWithResponse.no(new Error.LocalError(), true);
                    }

                    @Override
                    public void onSuccess(String jsonResponse) {

                        // work on the data, and get it to the view
//                        yesOrNoWithResponse.yes(Command.fakeList(6));
                        /* inflate the result into a list of things */
                        try {
                            JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                            JsonObject data = obj.get("data").getAsJsonObject();
                            // get daily restaurants
                            Command[] commands =
                                    gson.fromJson(data.get("commands"), new TypeToken<Command[]>() {
                                    }.getType());
                            List<Command> commandList = Arrays.asList(commands);

                            yesOrNoWithResponse.yes(commandList, true);
                        } catch (Exception e){
                            e.printStackTrace();
                            yesOrNoWithResponse.no(new Error.LocalError(), true);
                        }
                    }
                }
        );

    }

    public void purchaseNow(boolean payAtArrival, HashMap<Restaurant_Menu_FoodEntity, Integer> foods_command, DeliveryAddress address, NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {

         /* */

//        List<JSONObject> command_items_list = new ArrayList<>();

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
//            command_items_list.add(jsonObject);
            dataArray.put(jsonObject);
        }

        JSONObject object = new JSONObject();

//        command_items_list.toArray();

        try {
            object.put("food_command", dataArray);
            object.put("shipping_address", address.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(Constant.APP_TAG, " + + "+    object.toString());

        String authToken = ((MyKabaApp) context.getApplicationContext()).getAuthToken();

        networkRequestBase.postJsonDataWithToken(Config.LINK_CREATE_COMMAND, object.toString(), authToken, netRequestIntf);
    }
}
