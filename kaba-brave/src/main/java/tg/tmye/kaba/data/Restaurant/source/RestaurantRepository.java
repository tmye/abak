package tg.tmye.kaba.data.Restaurant.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.AbstractDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNo;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntityDao;
import tg.tmye.kaba.data._OtherEntities.Contact;
import tg.tmye.kaba.data._OtherEntities.DaoSession;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 20/02/2018.
 * email: 2597434002@qq.com
 *
 * Restaurant source Object.
 * Manage the data webGet - localStoring process without
 *  saying it.
 *
 */
public class RestaurantRepository {

    private final Context context;
    RestaurantDbOnlineSource onlineSource;

    /* threads */
    DatabaseRequestThreadBase databaseRequestThreadBase;
    NetworkRequestThreadBase networkRequestBase;


    private static Gson gson = new Gson();

    public RestaurantRepository (Context context) {
        this.context = context;
        this.onlineSource = new RestaurantDbOnlineSource();
        this.databaseRequestThreadBase = new DatabaseRequestThreadBase(context);
        this.networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
    }

    public void loadMainRestaurants(JsonObject data, final YesOrNoWithResponse yesOrNo) {

        /* get the array of ids from class */
        RestaurantEntityDao restaurantEntityDao = ((MyKabaApp)context.getApplicationContext()).getDaoSession().getRestaurantEntityDao();
        int[] daily_restoz =
                gson.fromJson(data.get("daily_restaurants"), new TypeToken<int[]>(){}.getType());
        /* once we have them, look for them in the local db */
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for (int i = 0; i < daily_restoz.length; i++) {
            restaurantEntities.add(RestaurantLocalDataSource.findRestoById(restaurantEntityDao, daily_restoz[i]));
        }
        yesOrNo.yes(restaurantEntities, false);
    }

    public void loadRestaurantList(YesOrNoWithResponse yesOrNoWithResponse) {

        // load restaurant list
        yesOrNoWithResponse.yes(RestaurantLocalDataSource.getAllRestaurants(context), false);
    }

    public static class RestaurantDbOnlineSource {


        public static void loadRestaurantList (Context ctx, String jsonData) {

            Log.d(Constant.APP_TAG, jsonData);

            JsonObject obj = new JsonParser().parse(jsonData).getAsJsonObject();
            JsonObject data = obj.get("data").getAsJsonObject();

            // get daily restaurants
            int[] daily_restoz =
                    gson.fromJson(data.get("daily_restaurants"), new TypeToken<int[]>(){}.getType());
            // get restaurants list
            RestaurantEntity[] restoz =
                    gson.fromJson(data.get("restaurants"), new TypeToken<RestaurantEntity[]>(){}.getType());
            // get all menus restaurants list
            Restaurant_SubMenuEntity[] submenus =
                    gson.fromJson(data.get("sub_menus"), new TypeToken<Restaurant_SubMenuEntity[]>(){}.getType());
            // get all food lists
            Restaurant_Menu_FoodEntity[] foods_list =
                    gson.fromJson(data.get("foods"), new TypeToken<Restaurant_Menu_FoodEntity[]>(){}.getType());
            // get all food tags
            Food_Tag[] food_tags =
                    gson.fromJson(data.get("food_tags"), new TypeToken<Food_Tag[]>(){}.getType());
            // get all food_tags
            Contact[] contacts =
                    gson.fromJson(data.get("contacts"), new TypeToken<Contact[]>(){}.getType());

            // save all of the in the db
            saveRestaurantEntity(ctx, restoz);
            saveRestaurantSubMenuEntity(ctx, submenus);
            saveRestaurantFoodEntity(ctx, foods_list);
            saveFoodTags(ctx, food_tags);
            saveRestaurantContactEntity(ctx, contacts);
            saveDailyRestaurants(ctx, daily_restoz);
        }

        private static void saveDailyRestaurants(Context ctx, int[] daily_restoz) {

            // share daily restaurants to shared preferences
            SharedPreferences sp = ctx.getSharedPreferences(Config.SYS_SHARED_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Config.DAILY_RESTAURANTS_SP_VAL, gson.toJson(daily_restoz));
            editor.commit();
        }

        private static void saveRestaurantContactEntity(Context ctx, Contact[] contacts) {

            if(contacts!=null) {
                // get the note DAO
                DaoSession daoSession = ((MyKabaApp) ctx.getApplicationContext()).getDaoSession();
                // save items to the database
                for (int i = 0; i < contacts.length; i++) {
                    Log.d(Constant.APP_TAG, contacts[i].toString());
                    daoSession.getContactDao().insertOrReplace(contacts[i]);
                }
            }
        }

        private static void saveFoodTags(Context ctx, Food_Tag[] food_tags) {
            if(food_tags!=null) {
                // get the note DAO
                DaoSession daoSession = ((MyKabaApp) ctx.getApplicationContext()).getDaoSession();
                // save items to the database
                for (int i = 0; i < food_tags.length; i++) {
                    Log.d(Constant.APP_TAG, food_tags[i].toString());
                    daoSession.getFood_TagDao().insertOrReplace(food_tags[i]);
                }
            }
        }

        private static void saveRestaurantFoodEntity(Context ctx, Restaurant_Menu_FoodEntity[] foods_list) {
            if(foods_list!=null) {
                // get the note DAO
                DaoSession daoSession = ((MyKabaApp) ctx.getApplicationContext()).getDaoSession();
                // save items to the database
                for (int i = 0; i < foods_list.length; i++) {
                    Log.d(Constant.APP_TAG, foods_list[i].toString());
                    daoSession.getRestaurant_Menu_FoodEntityDao().insertOrReplace(foods_list[i]);
                }
            }
        }

        private static void saveRestaurantSubMenuEntity(Context ctx, Restaurant_SubMenuEntity[] submenus) {
            if(submenus!=null) {
                // get the note DAO
                DaoSession daoSession = ((MyKabaApp) ctx.getApplicationContext()).getDaoSession();
                // save items to the database
                for (int i = 0; i < submenus.length; i++) {
                    Log.d(Constant.APP_TAG, submenus[i].toString());
                    daoSession.getRestaurant_SubMenuEntityDao().insertOrReplace(submenus[i]);
                }
            }
        }

        private static void saveRestaurantEntity(Context ctx, RestaurantEntity[] restoz) {
            if(restoz!=null) {
                // get the note DAO
                DaoSession daoSession = ((MyKabaApp) ctx.getApplicationContext()).getDaoSession();
                // save items to the database
                for (int i = 0; i < restoz.length; i++) {
                    Log.d(Constant.APP_TAG, restoz[i].toString());
                    daoSession.getRestaurantEntityDao().insertOrReplace(restoz[i]);
                }
            }
        }

        // reads resources regardless of their size
        public static byte[] getResource(int id, Context context) throws IOException {
            Resources resources = context.getResources();
            InputStream is = resources.openRawResource(id);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            byte[] readBuffer = new byte[4 * 1024];

            try {
                int read;
                do {
                    read = is.read(readBuffer, 0, readBuffer.length);
                    if(read == -1) {
                        break;
                    }
                    bout.write(readBuffer, 0, read);
                } while(true);

                return bout.toByteArray();
            } finally {
                is.close();
            }
        }

        // reads an UTF-8 string resource
        public static String getStringResource(int id, Context context) throws IOException {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                return new String(getResource(id, context), Charset.forName("UTF-8"));
            }
            return new String(getResource(id, context));
        }


        public static void update(final Context ctx, NetworkRequestThreadBase networkRequestBase, final DatabaseRequestThreadBase databaseRequestThreadBase, final YesOrNo yesOrNo) {
            // when updated, send a broadcast to the rest of the application
            networkRequestBase.run(Config.LINK_RESTO_FOOD_DB, new NetworkRequestThreadBase.NetRequestIntf() {
                @Override
                public void onNetworkError() {
                    Log.d(Constant.APP_TAG, "NetworkError");
                    yesOrNo.no();
                }

                @Override
                public void onSysError() {
                    Log.d(Constant.APP_TAG, "onSysError");
                    yesOrNo.no();
                }

                @Override
                public void onSuccess(String jsonResponse) {
                    Log.d(Constant.APP_TAG, "onSuccess");
                    doHouseWork (ctx, databaseRequestThreadBase, jsonResponse, yesOrNo);
                }
            });
        }

        private static void doHouseWork(final Context ctx, DatabaseRequestThreadBase databaseRequestThreadBase, final String jsonResponse, final YesOrNo yesOrNo) {

            // run the transactions on another database
            databaseRequestThreadBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
                @Override
                public void run() {
                    // clear database --  we catch the error
                    try {
                        RestaurantLocalDataSource.clearAllDynamicData(((MyKabaApp)ctx.getApplicationContext()).getDaoSession());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // save to database and inflate current objects
                    RestaurantDbOnlineSource.loadRestaurantList(ctx, jsonResponse);
                    // reload data into the current database
                    Toast.makeText(ctx, "Background Loading Success", Toast.LENGTH_SHORT);
                    yesOrNo.yes();
                }
            });
        }
    }

    public static class RestaurantLocalDataSource {

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
                try{
                    abstractDao.deleteAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
}
