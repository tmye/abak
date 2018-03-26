package tg.experta.kaba.syscore;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import tg.experta.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.experta.kaba.config.Config;
import tg.experta.kaba.data._OtherEntities.DaoMaster;
import tg.experta.kaba.data._OtherEntities.DaoSession;


/**
 * By abiguime on 19/12/2017.
 * email: 2597434002@qq.com
 */

public class MyKabaApp extends Application {

    DaoSession daoSession;

    NetworkRequestThreadBase networkRequestBase;

    /* sys token */
    private String authToken = "";

    @Override
    public void onCreate() {
        super.onCreate();

        initDb();
        networkRequestBase = new NetworkRequestThreadBase(this);
        // load token inside the app
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
        SharedPreferences pref = getSharedPreferences(Config.SYS_SHARED_PREFS, MODE_PRIVATE);
        return pref.getString(Config.SYSTOKEN, "");
    }

}
