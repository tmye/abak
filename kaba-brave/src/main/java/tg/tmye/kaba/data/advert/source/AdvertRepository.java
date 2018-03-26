package tg.tmye.kaba.data.advert.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.Group10AdvertItem;
import tg.tmye.kaba.data.advert.ProductAdvertItem;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class AdvertRepository {

    private final Context context;

    DatabaseRequestThreadBase databaseRequestThreadBase;
    NetworkRequestThreadBase networkRequestThreadBase;

    public AdvertRepository(Context context) {
        this.context = context;
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
    }

    public void loadHomeAdStructure (final NetworkRequestThreadBase.NetRequestIntf netRequestIntf) {

        /* if there is standard one stored locally, then get it and work with it */
        SharedPreferences preferences = context.getSharedPreferences(Config.HOMEPAGE_SP_VAL, Context.MODE_PRIVATE);
        final String lastPageFileName = preferences.getString(Config.LAST_HOME_PAGE_JSON, "");

        /* read current file content */
        networkRequestThreadBase.run(new NetworkRequestThreadBase.OnNetworkAction() {
            @Override
            public void run() {
                if (lastPageFileName.trim() != null) {
                    String json = UtilFunctions.readFromFile(context, lastPageFileName);
                    if (!"".equals(json)) {
                        /* load it up */
                        netRequestIntf.onSuccess(json);
                    }
                }
                networkRequestThreadBase.run(Config.LINK_HOME_PAGE, netRequestIntf);
            }
        });
    }

    public void loadHomeTopBanners(JsonObject data, final YesOrNoWithResponse yesOrNoWithResponse) {
        databaseRequestThreadBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
            @Override
            public void run() {

                List<String> ls = Arrays.asList(Constant.SAMPLE_IMAGES_BANNER);
                List<AdsBanner> adsBanners = new ArrayList<>();
                for (String l : ls) {
                    // inner object
                    SimplePicture.Banner ad1 = new SimplePicture.Banner();
                    ad1.path = l;
                    // object
                    AdsBanner adb = new AdsBanner();
                    adb.adsBanner = ad1;
                    adsBanners.add(adb);
                }
                yesOrNoWithResponse.yes(adsBanners, false);
            }
        });
    }


    public void load48MainAds(JsonObject data, final YesOrNoWithResponse yesOrNoWithResponse) {
        databaseRequestThreadBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
            @Override
            public void run() {
                yesOrNoWithResponse.yes(ProductAdvertItem.fakeList(8), false);
            }
        });
    }

    public void loadGroup10Ads(JsonObject data, YesOrNoWithResponse yesOrNoWithResponse) {

        List<Group10AdvertItem> group10AdvertItems = Group10AdvertItem.fakeList(5);
        yesOrNoWithResponse.yes(group10AdvertItems, false);
    }

    public void loadSearchHint(final JsonObject data, final YesOrNoWithResponse yesOrNoWithResponse) {

        databaseRequestThreadBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
            @Override
            public void run() {
                try {
//                    String hint = data.get("search_hint").getAsString();
                    String hint = "#Pizza - Tous au GreenField ce soir!";
                /* load from database*/
                    yesOrNoWithResponse.yes(hint, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
