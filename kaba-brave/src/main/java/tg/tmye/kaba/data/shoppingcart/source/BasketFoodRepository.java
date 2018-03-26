package tg.tmye.kaba.data.shoppingcart.source;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data.favorite.Favorite;
import tg.tmye.kaba.data.favorite.FavoriteDao;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketForDb;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketForDbDao;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketForView;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 22/02/2018.
 * email: 2597434002@qq.com
 */

public class BasketFoodRepository {

    private final NetworkRequestThreadBase networkRequestBase;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;
    private final Context context;
    private Gson gson = new Gson();

    public BasketFoodRepository (Context context) {

        this.context = context;

        /* get threads */
        networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyKabaApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }


    public void onlineBasketLoad (final YesOrNoWithResponse yesOrNoWithResponse) {

        /* loading failure*/
           /* check network and launch request, if there is a pblem, launch local data */
        networkRequestBase.run(Config.LINK_MY_BASKET, new NetworkRequestThreadBase.NetRequestIntf() {

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
                ShoppingBasketForView[] ShoppingBasketForView =
                        gson.fromJson(data.get("shoppingbasket"), new TypeToken<Favorite[]>(){}.getType());
                List<ShoppingBasketForView> shoppingBasketForViews = Arrays.asList(ShoppingBasketForView);
                yesOrNoWithResponse.yes(ShoppingBasketForView, true);
            }
        });
    }

    public void offlineBasketLoad (YesOrNoWithResponse yesOrNoWithResponse) {

        // load favorite list from database
        ShoppingBasketForDbDao shoppingBasketForDbDao = ((MyKabaApp)context.getApplicationContext()).getDaoSession().getShoppingBasketForDbDao();
        List<ShoppingBasketForDb> shoppingBasketForDbDaoList = shoppingBasketForDbDao.loadAll();
        /* transform basket for db into basket for view */
        yesOrNoWithResponse.yes(/*shoppingBasketForDbDaoList*/ShoppingBasketForView.fakeList(3), false);
    }

    /* get mybasket food list */

    /* delete item from my basket */

    /* add item to my basket */

}
