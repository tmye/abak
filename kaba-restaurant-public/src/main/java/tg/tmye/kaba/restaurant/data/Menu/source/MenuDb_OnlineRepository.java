package tg.tmye.kaba.restaurant.data.Menu.source;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba.restaurant.ILog;
import tg.tmye.kaba.restaurant._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant._commons.utils.SimpleObjectHolder;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.restaurant.syscore.Config;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;


/**
 * By abiguime on 21/12/2017.
 * email: 2597434002@qq.com
 */

public class MenuDb_OnlineRepository {

    /* i should be knowing which restaurant im working with */

    private final Context context;
    public final RestaurantEntity restaurantEntity;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;
    private final NetworkRequestThreadBase networkRequestThreadBase;

    public MenuDb_OnlineRepository (Context context, RestaurantEntity restaurantEntity) {

        this.context = context;
        this.databaseRequestThreadBase = ((MyRestaurantApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyRestaurantApp)context.getApplicationContext()).getNetworkRequestBase();

        this.restaurantEntity = restaurantEntity;
    }

    public void loadAllSubMenusOfRestaurant(final NetworkRequestThreadBase.NetRequestIntf intf) {

        /* also get the serial of the restaurant database right now */
        Map<String, Object> data = new HashMap<>();
        data.put("id", restaurantEntity.id);

        // put restaurant id, and you send me back everything that is in the db
        // so that i can have a list of menus including their foods at the same time.
        networkRequestThreadBase.postJsonData(Config.LINK_MENU_BY_RESTAURANT_ID, data, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                intf.onNetworkError();
            }

            @Override
            public void onSysError() {
                intf.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                ILog.print(jsonResponse);
                /* conver to json objects */

                Gson gson = new Gson();
                JsonObject data = new JsonParser().parse(jsonResponse).getAsJsonObject().get("data").getAsJsonObject();
//                Jsono data = data.get("data").getAsJsonArray();

                /* get submenus and -- drinks */

                JsonArray menuJsonArray = data.get("menus").getAsJsonArray();

                List<Restaurant_SubMenuEntity> subMenuEntities = new ArrayList<>();

                for (int i = 0; i < menuJsonArray.size(); i++) {

                    Restaurant_SubMenuEntity subMenuEntity =
                            gson.fromJson(menuJsonArray.get(i).getAsJsonObject(), new TypeToken<Restaurant_SubMenuEntity>(){}.getType());

                    List<Restaurant_Menu_FoodEntity> foods =
                            gson.fromJson(
                                    menuJsonArray.get(i).getAsJsonObject().getAsJsonArray("foods"),
                                    new TypeToken<List<Restaurant_Menu_FoodEntity>>(){}.getType()
                            );

                    subMenuEntity.setFoods(foods);

                    subMenuEntities.add(subMenuEntity);
                }

                /* get drinks */
                JsonArray drinksJsonArray = data.get("drinks").getAsJsonArray();
                List<Restaurant_Menu_FoodEntity> drinks =
                        gson.fromJson(
                                drinksJsonArray,
                                new TypeToken<List<Restaurant_Menu_FoodEntity>>(){}.getType()
                        );

                SimpleObjectHolder simpleObjectHolder = new SimpleObjectHolder();
                simpleObjectHolder.arg1 = subMenuEntities;
                simpleObjectHolder.arg2 = drinks;

                intf.onSuccess(simpleObjectHolder);
            }
        });
    }

    public void loadAllSubMenusOfRestaurantForEdit(final NetworkRequestThreadBase.NetRequestIntf intf) {

        /* also get the serial of the restaurant database right now */
        Map<String, Object> data = new HashMap<>();
        data.put("id", restaurantEntity.id);

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();

        // put restaurant id, and you send me back everything that is in the db
        // so that i can have a list of menus including their foods at the same time.
        networkRequestThreadBase.postMapDataWithToken(Config.LINK_GET_MENU_BY_ID_SPECIFIC, data, authToken, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                intf.onNetworkError();
            }

            @Override
            public void onSysError() {
                intf.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                ILog.print(jsonResponse);
                /* conver to json objects */

                Gson gson = new Gson();
                JsonArray menuJsonArray = new JsonParser().parse(jsonResponse).getAsJsonObject().get("data").getAsJsonArray();
//                Json data = data.get("data").getAsJsonArray();

                /* get submenus and -- drinks */

//                JsonArray menuJsonArray = data;

                List<Restaurant_SubMenuEntity> subMenuEntities = new ArrayList<>();

                for (int i = 0; i < menuJsonArray.size(); i++) {

                    Restaurant_SubMenuEntity subMenuEntity =
                            gson.fromJson(menuJsonArray.get(i).getAsJsonObject(), new TypeToken<Restaurant_SubMenuEntity>(){}.getType());

                    List<Restaurant_Menu_FoodEntity> foods =
                            gson.fromJson(
                                    menuJsonArray.get(i).getAsJsonObject().getAsJsonArray("foods"),
                                    new TypeToken<List<Restaurant_Menu_FoodEntity>>(){}.getType()
                            );

                    subMenuEntity.setFoods(foods);

                    subMenuEntities.add(subMenuEntity);
                }

                /* get drinks */
              /*  JsonArray drinksJsonArray = data.get("drinks").getAsJsonArray();
                List<Restaurant_Menu_FoodEntity> drinks =
                        gson.fromJson(
                                drinksJsonArray,
                                new TypeToken<List<Restaurant_Menu_FoodEntity>>(){}.getType()
                        );*/

                SimpleObjectHolder simpleObjectHolder = new SimpleObjectHolder();
                simpleObjectHolder.arg1 = subMenuEntities;
//                simpleObjectHolder.arg2 = drinks;

                intf.onSuccess(simpleObjectHolder);
            }
        });
    }

    public void loadAllFoodsFromMenuForEdit(int menu_id, final NetworkRequestThreadBase.NetRequestIntf intf) {

        /* also get the serial of the restaurant database right now */
        Map<String, Object> data = new HashMap<>();
        data.put("menu_id", menu_id);

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();

        // put restaurant id, and you send me back everything that is in the db
        // so that i can have a list of menus including their foods at the same time.
        networkRequestThreadBase.postMapDataWithToken(Config.LINK_GET_FOOD_BY_ID_SPECIFIC, data, authToken, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                intf.onNetworkError();
            }

            @Override
            public void onSysError() {
                intf.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                ILog.print(jsonResponse);
                /* conver to json objects */

                Gson gson = new Gson();
                JsonArray foodJsonArray = new JsonParser().parse(jsonResponse).getAsJsonObject().get("data").getAsJsonArray();
//                Jsono data = data.get("data").getAsJsonArray();

                List<Restaurant_Menu_FoodEntity> foods = new ArrayList<>();

                for (int i = 0; i < foodJsonArray.size(); i++) {

                    Restaurant_Menu_FoodEntity food =
                            gson.fromJson(foodJsonArray.get(i).getAsJsonObject(), new TypeToken<Restaurant_Menu_FoodEntity>(){}.getType());

                    foods.add(food);
                }

                intf.onSuccess(foods);
            }
        });
    }

    public void updateSubMenuEntity(Restaurant_SubMenuEntity subMenuEntity, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject params = new JSONObject();
        try {
            params.put("id", subMenuEntity.id);
            params.put("name",subMenuEntity.name);
            params.put("is_hidden", ""+subMenuEntity.is_hidden);
            params.put("priority", subMenuEntity.priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // if no id, use create/add
        // if has id, use edit

        networkRequestThreadBase.postJsonDataWithToken((subMenuEntity.id != 0 && subMenuEntity.id > 1)? Config.LINK_MENU_EDIT : Config.LINK_MENU_ADD, params.toString(), authToken, intf);
    }


    public void updateFoodEntity(Restaurant_Menu_FoodEntity foodEntity, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject params = new JSONObject();
        try {
            params.put("id",foodEntity.id);
            params.put("name",foodEntity.name);
            params.put("description", foodEntity.description);
            params.put("price", foodEntity.price);
            params.put("priority", foodEntity.priority);
            params.put("promotion_price", foodEntity.promotion_price);
            params.put("is_hidden", String.valueOf(foodEntity.is_hidden));
            params.put("promotion", foodEntity.promotion); // 0 or 1
        } catch (JSONException e) {
            e.printStackTrace();
        }

        networkRequestThreadBase.postJsonDataWithToken((foodEntity.id != 0 && foodEntity.id > 1)? Config.LINK_FOOD_EDIT : Config.LINK_FOOD_ADD, params.toString(), authToken, intf);
    }

    public void hideFood(int foodId, NetworkRequestThreadBase.NetRequestIntf<String> intf) {
        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject params = new JSONObject();
        try {
            params.put("id",foodId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_MENU_DELETE, params.toString(), authToken, intf);
    }

    public void deleteFood(int food_id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject params = new JSONObject();
        try {
            params.put("id",food_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_FOOD_DELETE, params.toString(), authToken, intf);
    }

    public void hideMenu(int menu_id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {
        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject params = new JSONObject();
        try {
            params.put("id",menu_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_MENU_DELETE, params.toString(), authToken, intf);
    }

    public void deleteMenu(int menu_id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {
        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject params = new JSONObject();
        try {
            params.put("id",menu_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_MENU_DELETE, params.toString(), authToken, intf);
    }
}
