package tg.tmye.kaba.data.Food.source;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.notification.NotificationBuilder;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 20/12/2017.
 * email: 2597434002@qq.com
 */

public class FoodRepository {

    private final Context context;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;
    private final NetworkRequestThreadBase networkRequestBase;

    public FoodRepository (Context context) {

        this.context = context;
        this.databaseRequestThreadBase = new DatabaseRequestThreadBase(context);
        this.networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
    }

    public void setFavorite(Restaurant_Menu_FoodEntity foodEntity, NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        /*  */
        Map<String, Object> data = new HashMap<>();
        data.put("food_id", foodEntity.id);

        networkRequestBase.postMapDataWithToken(Config.LINK_SET_FAVORITE, data, token, netRequestIntf);
    }

    public static List<Restaurant_Menu_FoodEntity> fakeSimpleDrinks(int count) {

        List<Restaurant_Menu_FoodEntity> foodEntities = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Restaurant_Menu_FoodEntity foodEntity = new Restaurant_Menu_FoodEntity();
            foodEntity.id = 1900+i;
            foodEntity.name = "GOOD _ "+i;
            foodEntities.add(foodEntity);
        }
        return  foodEntities;
    }

    public void loadFood(int food_id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        JSONObject object = new JSONObject();
        try {
            object.put("food_id", food_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        String link = "";

        if (token != null && !"".equals(token.trim())) {
            link = Config.LINK_GET_FOOD_DETAILS_LOGGED;
            networkRequestBase.postJsonDataWithToken(link, object.toString(), token, intf);
        } else {
            link = Config.LINK_GET_FOOD_DETAILS_SIMPLE;
            networkRequestBase.postJsonData(link, object.toString(), intf);
        }
        Log.d(Constant.APP_TAG, link);
        Log.d(Constant.APP_TAG, "token_is "+token);

    }
}
