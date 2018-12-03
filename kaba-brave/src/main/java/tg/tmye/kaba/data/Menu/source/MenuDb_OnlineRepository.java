package tg.tmye.kaba.data.Menu.source;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.utils.SimpleObjectHolder;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.syscore.MyKabaApp;

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
    private int main_menu_id;


    public MenuDb_OnlineRepository (Context context, RestaurantEntity restaurantEntity) {

        this.context = context;
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();

        this.restaurantEntity = restaurantEntity;
    }

    public MenuDb_OnlineRepository(int menu_id, Context context) {

        this.context = context;
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();

        this.restaurantEntity = null;
        this.main_menu_id = menu_id;
    }

    public void loadAllDataFromMenuId (final NetworkRequestThreadBase.NetRequestIntf intf) {

        /* we need to get menus list, so that the first be the one we are looking for */

        /* we need also all the restaurant informations. */
        Map<String, Object> data = new HashMap<>();
        data.put("menu_id", ""+main_menu_id);

        networkRequestThreadBase.postJsonData(Config.LINK_MENU_BY_ID, data, intf);
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

                Log.d(Constant.APP_TAG, jsonResponse);
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


}
