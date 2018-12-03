package tg.tmye.kaba.data.shoppingcart.source;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.favorite.Favorite;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 22/02/2018.
 * email: 2597434002@qq.com
 */

public class BasketRepository {

    private final NetworkRequestThreadBase networkRequestBase;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;
    private final Context context;
    private Gson gson = new Gson();

    public BasketRepository(Context context) {

        this.context = context;

        /* get threads */
        networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyKabaApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }


    public void addFoodToBasket(Map<Restaurant_Menu_FoodEntity, Integer> commands, NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        /* */
        JSONArray array = new JSONArray();

        Object[] keys = commands.keySet().toArray();

        for (Object key : keys) {
            Restaurant_Menu_FoodEntity entity = (Restaurant_Menu_FoodEntity) key;
            int quantity = commands.get(key);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("food_id", entity.id);
                jsonObject.put("quantity", quantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(jsonObject);
        }

        Log.d(Constant.APP_TAG, "posting "+array.toString());

        String authToken = ((MyKabaApp) context.getApplicationContext()).getAuthToken();
        networkRequestBase.postJsonDataWithToken(Config.LINK_MY_BASKET_CREATE, array.toString(), authToken, netRequestIntf);
    }

    public void loadBasketItems(NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        networkRequestBase.getDataWithToken(Config.LINK_MY_BASKET_GET, null, token, netRequestIntf);
    }

    public void deleteBasketItem(BasketInItem basketInItem, NetworkRequestThreadBase.AuthNetRequestIntf<String> intf) {

        /* */
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", basketInItem.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String authToken = ((MyKabaApp) context.getApplicationContext()).getAuthToken();
        networkRequestBase.postJsonDataWithToken(Config.LINK_MY_BASKET_DELETE, jsonObject.toString(), authToken, intf);
    }
}
