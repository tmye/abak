package tg.tmye.kaba.data.Menu.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntityDao;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/12/2017.
 * email: 2597434002@qq.com
 */

public class MenuDb_OnlineRepository {

    /* i should be knowing which restaurant im working with */

    private final Context context;
    private final RestaurantEntity restaurantEntity;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;
    private final NetworkRequestThreadBase networkRequestThreadBase;
    Restaurant_SubMenuEntityDao menuEntityDao;

    public MenuDb_OnlineRepository (Context context, RestaurantEntity restaurantEntity) {

        this.context = context;
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();

        this.restaurantEntity = restaurantEntity;

        menuEntityDao = ((MyKabaApp)context.getApplicationContext()).getDaoSession(
                UtilFunctions.superTrim(restaurantEntity.restaurant_name))
                .getRestaurant_SubMenuEntityDao();
    }

    public void loadAllSubMenusOfRestaurant(NetworkRequestThreadBase.NetRequestIntf intf) {

        /* also get the serial of the restaurant database right now */
        final SharedPreferences preferences = context.getSharedPreferences(Config.RESTAURANT_DB_SERIAL, Context.MODE_PRIVATE);
        final int restaurant_serial = preferences.getInt(UtilFunctions.superTrim(restaurantEntity.restaurant_name), 0);
        if (restaurant_serial > 0) {
            List<Restaurant_SubMenuEntity> menus = menuEntityDao
                    .queryBuilder()
                    .where(Restaurant_SubMenuEntityDao.Properties.Restaurant_id.eq(restaurantEntity.getId()))
                    .list();
        /* if we have lists, send it back ...*/
            if (menus != null && menus.size() > 0) {
            /* load*/
                intf.onSuccess(""); // send back list of submenus
            }
        }

        /* load restaurant data from online */
        networkRequestThreadBase.run(Config.LINK_RESTO_FOOD_DB + "/" +
                restaurantEntity.id, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {

            }

            @Override
            public void onSysError() {

            }

            @Override
            public void onSuccess(String jsonResponse) {

                /* get the serial of the current request */
//                checkAndSaveMenuFoodDb(jsonResponse);
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();

                int serial = data.get("serial").getAsInt();
                if (serial > restaurant_serial) {
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putInt(UtilFunctions.superTrim(restaurantEntity.restaurant_name), serial);
                    edit.commit();

                    UtilFunctions.writeToFile(context, UtilFunctions.superTrim(restaurantEntity.restaurant_name), jsonResponse);

                    /* get an array of submenus / array of foods / then persist it */

                    /* drop all menus and food */
                    
                }

                /* can save the db to local, drop the current database, and recreate it .*/

                /*if (serial_home > local_serial) {
            *//* save current file *//*
                    UtilFunctions.writeToFile(context, ""+serial_home, jsonResponse);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putInt(Config.LAST_HOME_PAGE_JSON, serial_home);
                    edit.commit();
                    return true;
                }
                return false;*/
            }
        });
    }


}
