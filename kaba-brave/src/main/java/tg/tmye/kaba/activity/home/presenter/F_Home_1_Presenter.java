package tg.tmye.kaba.activity.home.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.home.contracts.F_HomeContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.source.RestaurantDbRepository;
import tg.tmye.kaba.data._OtherEntities.LightRestaurant;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.FTXAd;
import tg.tmye.kaba.data.advert.Group10AdvertItem;
import tg.tmye.kaba.data.advert.ProductAdvertItem;
import tg.tmye.kaba.data.advert.source.AdvertRepository;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class F_Home_1_Presenter implements F_HomeContract.Presenter {


    private final RestaurantDbRepository restaurantDbRepository;
    private final AdvertRepository advertRepository;
    private final F_HomeContract.View home1View;
    private Gson gson = new Gson();

    public F_Home_1_Presenter (RestaurantDbRepository restaurantDbRepository,
                               AdvertRepository advertRepository, F_HomeContract.View home1View) {

        this.restaurantDbRepository = restaurantDbRepository;
        this.advertRepository = advertRepository;
        this.home1View = home1View;
        this.home1View.setPresenter(this);
    }

    @Override
    public void start() {
        populateViews();
    }

    private void populateViews() {

        /* if there is an old save, use it, when loading the newest */

        home1View.showLoadingProgress(true);

        /* update restaurants before updating the home page */
        advertRepository.loadHomeAdStructure(new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                home1View.showLoadingProgress(false);
                home1View.showErrorMessage("");
            }

            @Override
            public void onSysError() {
                home1View.showLoadingProgress(false);
                home1View.sysError("");
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, "jzon response - > "  +jsonResponse);
                /* use the current json string and squeeze out all the need data */
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();

                checkAndSaveLast_Home_Page(jsonResponse);

                // get daily restaurants
                loadSearchHint (data);
                loadTopBanners (data);
                loadMainRestaurant(data);
                load48Ads (data);
                loadGifSpace (data);
                loadGroup10Ads(data);

                home1View.showLoadingProgress(false);
                home1View.hideErrorPage();

            }
        });
    }

    private void loadGifSpace(JsonObject data) {
        advertRepository.loadGifSpace(data, new YesOrNoWithResponse() {
            @Override
            public void yes(Object data, boolean isFromOnline) {
                home1View.inflateGifSpace((List<AdsBanner>) data);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {
            }

            @Override
            public void onLoggingTimeout() {

            }
        });
    }

    private boolean checkAndSaveLast_Home_Page(String jsonResponse) {

        /* */
       return advertRepository.checkAndSave (jsonResponse);
    }

    private void loadGroup10Ads(JsonObject data) {
        advertRepository.loadGroup10Ads(data, new YesOrNoWithResponse(){
            @Override
            public void yes(Object data, boolean isFromOnline) {
                home1View.inflateGroupsPubLongList((List<Group10AdvertItem>) data);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {

            }

            @Override
            public void onLoggingTimeout() {

            }
        });

    }

    private void load48Ads(JsonObject data) {
        advertRepository.load48MainAds(data, new YesOrNoWithResponse(){
            @Override
            public void yes(Object dt, boolean isFromOnline) {
//                home1View.inflateMain48((List<AdsBanner>) data);
                JsonObject data = (JsonObject) dt;
                try {
                    AdsBanner promotion = gson.fromJson(data.get("promotion"), AdsBanner.class);
                    AdsBanner event = gson.fromJson(data.get("event"), AdsBanner.class);
                    home1View.inflateMain48(promotion, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void no(Object data, boolean isFromOnline) {}

            @Override
            public void onLoggingTimeout() {

            }
        });
    }

    private void loadTopBanners(JsonObject data) {
        advertRepository.loadHomeTopBanners(data, new YesOrNoWithResponse() {
            @Override
            public void yes(Object data, boolean isFromOnline) {
                home1View.showMainSliding((List<AdsBanner>) data);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {
            }

            @Override
            public void onLoggingTimeout() {

            }
        });
    }

    private void loadMainRestaurant(JsonObject data) {
        /* load restaurants */
        restaurantDbRepository.loadMainRestaurants(data, new YesOrNoWithResponse() {

            @Override
            public void yes(Object data, boolean isFromOnline) {
                checkNotNull(data);
                home1View.inflateMainRestaurants((List<RestaurantEntity>) data);
                home1View.showLoadingProgress(false);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {
            }

            @Override
            public void onLoggingTimeout() {

            }
        });
    }

    private void loadSearchHint(JsonObject data) {
        advertRepository.loadSearchHint(data, new YesOrNoWithResponse() {
            @Override
            public void yes(Object data, boolean isFromOnline) {
                home1View.showMainHint((String) data);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {
            }

            @Override
            public void onLoggingTimeout() {

            }
        });
    }


    @Override
    public void updateView() {

        // update views from online, then notify the views
        populateViews();
    }

    public void showHideBalance() {
    }
}
