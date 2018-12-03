package tg.tmye.kaba.data.advert.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.FTXAd;
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

    private Gson gson = new Gson();

    boolean isFirstRequest;

    public AdvertRepository(Context context) {
        this.context = context;
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
        isFirstRequest = true;
    }

    public void loadHomeAdStructure (final NetworkRequestThreadBase.NetRequestIntf netRequestIntf) {

        /* if there is standard one stored locally, then get it and work with it */
        SharedPreferences preferences = context.getSharedPreferences(Config.HOMEPAGE_SP_VAL, Context.MODE_PRIVATE);
        final int lastPageFileName = preferences.getInt(Config.LAST_HOME_PAGE_JSON, 0);

        /* read current file content */
        networkRequestThreadBase.run(new NetworkRequestThreadBase.OnNetworkAction() {
            @Override
            public void run() {

                Log.d(Constant.APP_SUPER_TAG, "lastpagefilename = "+lastPageFileName);

                if (lastPageFileName != 0) {
                    String json = UtilFunctions.readFromFile(context, "home-"+lastPageFileName);
                    if (!"".equals(json)) {
                        /* load it up */
                        if (isFirstRequest) {
                            netRequestIntf.onSuccess(json);
                            isFirstRequest = false;
                        }
                    }
                }
                networkRequestThreadBase.run(Config.LINK_HOME_PAGE, netRequestIntf);
            }
        });
    }

    public void loadHomeTopBanners(final JsonObject data, final YesOrNoWithResponse yesOrNoWithResponse) {
        databaseRequestThreadBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
            @Override
            public void run() {

                AdsBanner[] home_banners =
                        gson.fromJson(data.get("slider"), new TypeToken<AdsBanner[]>(){}.getType());
                yesOrNoWithResponse.yes(Arrays.asList(home_banners), false);
            }
        });
    }

    public void loadGifSpace(final JsonObject data, final YesOrNoWithResponse yesOrNoWithResponse) {

        databaseRequestThreadBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
            @Override
            public void run() {

                try {

                    AdsBanner[] kaba_pubs =
                            gson.fromJson(data.get("kaba_pub"), new TypeToken<AdsBanner[]>(){}.getType());
                    yesOrNoWithResponse.yes(Arrays.asList(kaba_pubs), false);
                } catch (Exception e) {
                    e.printStackTrace();
                    yesOrNoWithResponse.yes(null, false);
                }
            }
        });
    }

    public void load48MainAds(JsonObject data, final YesOrNoWithResponse yesOrNoWithResponse) {


    /*    ProductAdvertItem[] productAdvertItems =
                gson.fromJson(data.get("fourtosix"), new TypeToken<ProductAdvertItem[]>(){}.getType());
        yesOrNoWithResponse.yes(Arrays.asList(productAdvertItems), false);*/
        yesOrNoWithResponse.yes(data, true);
    }

    public void loadGroup10Ads(JsonObject data, YesOrNoWithResponse yesOrNoWithResponse) {

        Group10AdvertItem[] group10AdvertItems =
                gson.fromJson(data.get("groupad"), new TypeToken<Group10AdvertItem[]>(){}.getType());
        yesOrNoWithResponse.yes(Arrays.asList(group10AdvertItems), false);
    }

    public void loadSearchHint(final JsonObject data, final YesOrNoWithResponse yesOrNoWithResponse) {

        databaseRequestThreadBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
            @Override
            public void run() {
                try {
                    String hint = data.get("feed").getAsString();
//                    String hint = "#Pizza - Tous au GreenField ce soir!";
                    /* load from database*/
                    yesOrNoWithResponse.yes(hint, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean checkAndSave(String jsonResponse) {
        JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
        JsonObject data = obj.get("data").getAsJsonObject();

        int serial_home = data.get("serial_home").getAsInt();
        SharedPreferences preferences = context.getSharedPreferences(Config.HOMEPAGE_SP_VAL, Context.MODE_PRIVATE);
        int local_serial = preferences.getInt(Config.LAST_HOME_PAGE_JSON, 0);

        if (serial_home != local_serial) {
            /* save current file */
            UtilFunctions.writeToFile(context, "home-"+serial_home, jsonResponse);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt(Config.LAST_HOME_PAGE_JSON, serial_home);
            edit.commit();
            return true;
        }
        return false;
    }


}
