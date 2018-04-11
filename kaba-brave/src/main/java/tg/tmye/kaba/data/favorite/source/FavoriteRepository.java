package tg.tmye.kaba.data.favorite.source;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.favorite.Favorite;
import tg.tmye.kaba.data.favorite.FavoriteDao;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 27/02/2018.
 * email: 2597434002@qq.com
 */

public class FavoriteRepository {

    private final Context context;
    private final NetworkRequestThreadBase networkRequestBase;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;

    public Gson gson = new Gson();

    public FavoriteRepository (Context context) {
        this.context = context;

        /* get threads */
        networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyKabaApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }

    public void onlineFavoriteLoad(final YesOrNoWithResponse yesOrNoWithResponse) {

        /* check network and launch request, if there is a pblem, launch local data */

        networkRequestBase.run(Config.LINK_MY_FAVORITE, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                yesOrNoWithResponse.no(null, true);
            }

            @Override
            public void onSysError() {
                yesOrNoWithResponse.no(null, true);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                // turn result into a list of favorite objects
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                Favorite[] favorites =
                        gson.fromJson(data.get("favorites"), new TypeToken<Favorite[]>(){}.getType());
                List<Favorite> favoriteList = Arrays.asList(favorites);
                yesOrNoWithResponse.yes(favoriteList, true);
            }
        });
    }

    /* two functions, load online, and offline */

    public void offlineFavoriteLoad (final YesOrNoWithResponse yesOrNoWithResponse) {
        databaseRequestThreadBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
            @Override
            public void run() {

                // load favorite list from database
                FavoriteDao favoriteDao = ((MyKabaApp)context.getApplicationContext()).getDaoSession(MyKabaApp.personnal_db).getFavoriteDao();
                List<Favorite> favoriteList = favoriteDao.loadAll();
                yesOrNoWithResponse.yes(favoriteList, false);
            }
        });
    }

}
