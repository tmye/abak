package tg.tmye.kaba.syscore;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data.Restaurant.source.RestaurantRepository;
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

    @Override
    public void onCreate() {
        super.onCreate();

        initDb();
        networkRequestBase = new NetworkRequestThreadBase(this);
        databaseRequestThreadBase = new DatabaseRequestThreadBase(this);
        // load token inside the app

        /* update the whole database of the restaurants and everything */
//        RestaurantRepository.RestaurantDbOnlineSource.update(this,networkRequestBase, databaseRequestThreadBase, null);
    }


    private void initDb() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "kaba-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public NetworkRequestThreadBase getNetworkRequestBase() {
        if (networkRequestBase == null)
            networkRequestBase = new NetworkRequestThreadBase(this);
        return networkRequestBase;
    }

    public DaoSession getDaoSession() {

        if (daoSession == null)
            initDb();
        return daoSession;
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
