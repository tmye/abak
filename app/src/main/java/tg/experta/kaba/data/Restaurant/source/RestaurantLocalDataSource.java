package tg.experta.kaba.data.Restaurant.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

import tg.experta.kaba.activities.Home.HomeActivity;
import tg.experta.kaba.config.Config;
import tg.experta.kaba.data.Food.Food_Tag;
import tg.experta.kaba.data.Food.Food_TagDao;
import tg.experta.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;
import tg.experta.kaba.data.Restaurant.RestaurantEntityDao;
import tg.experta.kaba.data._OtherEntities.DaoSession;
import tg.experta.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 20/12/2017.
 * email: 2597434002@qq.com
 */

public class RestaurantLocalDataSource {

    static Gson gson = new Gson();

    public static RestaurantEntity findRestoById(RestaurantEntityDao dao, long restaurant_id) {

        List<RestaurantEntity> restaurantEntities = dao.queryBuilder()
                .where(RestaurantEntityDao.Properties.Id.eq(restaurant_id))
                .orderAsc(RestaurantEntityDao.Properties.Id)
                .limit(1)
                .list();
        if (restaurantEntities != null && restaurantEntities.size() > 0)
            return restaurantEntities.get(0);
        else
            return null;
    }

    public static void clearAllDynamicData(DaoSession daoSession) {

        // drop restaurant, contact, food, submenus, foodtags --tables
        for (AbstractDao abstractDao : daoSession.getAllDaos()){
            abstractDao.deleteAll();
        }
    }

    public static List<RestaurantEntity> getDailyRestaurants(Context ctx) {

        DaoSession daoSession = ((MyKabaApp) ctx.getApplicationContext()).getDaoSession();
        RestaurantEntityDao restaurantEntityDao = daoSession.getRestaurantEntityDao();

        // share daily restaurants to shared preferences
        SharedPreferences sp = ctx.getSharedPreferences(Config.SYS_SHARED_PREFS, Context.MODE_PRIVATE);
        String jsonData = sp.getString(Config.DAILY_RESTAURANTS_SP_VAL, "");

        int[] daily_resto_idz = gson.fromJson(jsonData, new TypeToken<int[]>(){}.getType());

        List<RestaurantEntity> rs = new ArrayList<>();

        if (daily_resto_idz != null && daily_resto_idz.length > 0){
            for (int i = 0; i < daily_resto_idz.length; i++) {
                rs.add(findRestoById(restaurantEntityDao, daily_resto_idz[i]));
            }
        }
        return rs;
    }

    public static List<RestaurantEntity> getAllRestaurants(Context ctx) {


        DaoSession daoSession = ((MyKabaApp) ctx.getApplicationContext()).getDaoSession();
        RestaurantEntityDao restaurantEntityDao = daoSession.getRestaurantEntityDao();

        // all all restaurants
        List<RestaurantEntity> rs = restaurantEntityDao.loadAll();

        return rs;
    }
}
