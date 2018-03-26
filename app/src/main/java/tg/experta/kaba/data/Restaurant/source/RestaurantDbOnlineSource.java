package tg.experta.kaba.data.Restaurant.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import tg.experta.kaba.config.Config;
import tg.experta.kaba.config.Constant;
import tg.experta.kaba.data.FakeLoader;
import tg.experta.kaba.data.Food.Food_Tag;
import tg.experta.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.experta.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;
import tg.experta.kaba.data._OtherEntities.Contact;
import tg.experta.kaba.data._OtherEntities.DaoSession;
import tg.experta.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/12/2017.
 * email: 2597434002@qq.com
 */

public class RestaurantDbOnlineSource {

    private static Gson gson = new Gson();

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


}
