package tg.tmye.kaba.syscore;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data._OtherEntities.DaoMaster;
import tg.tmye.kaba.data._OtherEntities.DaoSession;


/**
 * By abiguime on 19/12/2017.
 * email: 2597434002@qq.com
 */

public class MyKabaApp extends Application {

    DaoSession daoSession;

    NetworkRequestThreadBase networkRequestBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;

    /* sys token */
    private String authToken = "";

    /* personnal db restaurant_name */
    public static String personnal_db = "personnal";

    @Override
    public void onCreate() {
        super.onCreate();

//        initDb();
        daoSessionMap = new HashMap<>();
        networkRequestBase = new NetworkRequestThreadBase(this);
        databaseRequestThreadBase = new DatabaseRequestThreadBase(this);
        // load token inside the app

        /* update the whole database of the restaurants and everything */
//        RestaurantDbRepository.RestaurantDbOnlineSource.update(this,networkRequestBase, databaseRequestThreadBase, null);
    }

    private void initDb(String restaurantCodeName) {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, restaurantCodeName+"-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        daoSessionMap.put(restaurantCodeName, daoSession);
    }


    public NetworkRequestThreadBase getNetworkRequestBase() {
        if (networkRequestBase == null)
            networkRequestBase = new NetworkRequestThreadBase(this);
        return networkRequestBase;
    }


    Map<String, DaoSession> daoSessionMap;


    public DaoSession getDaoSession(String restaurantCodeName) {

        if (daoSessionMap.get(restaurantCodeName) == null)
            initDb(restaurantCodeName);
        return daoSessionMap.get(restaurantCodeName);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        if (authToken == null || authToken.equals(""))
            return loadToken();
        return this.authToken;
    }

    private String loadToken() {
        SharedPreferences pref = getSharedPreferences(Config.USER_SHARED_PREFS, MODE_PRIVATE);
        return pref.getString(Config.SYSTOKEN, "");
    }

    public DatabaseRequestThreadBase getDatabaseRequestThreadBase() {
        return databaseRequestThreadBase;
    }
}
