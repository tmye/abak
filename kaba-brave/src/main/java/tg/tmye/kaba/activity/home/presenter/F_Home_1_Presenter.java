package tg.tmye.kaba.activity.home.presenter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.home.contracts.F_HomeContract;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.source.RestaurantRepository;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.Group10AdvertItem;
import tg.tmye.kaba.data.advert.ProductAdvertItem;
import tg.tmye.kaba.data.advert.source.AdvertRepository;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class F_Home_1_Presenter implements F_HomeContract.Presenter {


    private final RestaurantRepository restaurantRepository;
    private final AdvertRepository advertRepository;
    private final F_HomeContract.View home1View;

    public F_Home_1_Presenter (RestaurantRepository restaurantRepository,
                               AdvertRepository advertRepository, F_HomeContract.View home1View) {

        this.restaurantRepository = restaurantRepository;
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
        advertRepository.loadHomeAdStructure(new NetworkRequestThreadBase.NetRequestIntf() {
            @Override
            public void onNetworkError() {
                home1View.showLoadingProgress(false);
            }

            @Override
            public void onSysError() {
                home1View.showLoadingProgress(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                /* use the current json string and squeeze out all the need data */
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();

                // get daily restaurants
                loadSearchHint (data);
                loadMainRestaurant(data);

                home1View.showLoadingProgress(false);
            }
        });

        /* the solution is to check whether there is file.. if yes, work on it when downloading the newest
         otherwise, just, stay up there...
          * once the file is downloaded, just get the jsonObject that goes with it and inflate the view
          */


        /* update the restaurant DB right after checking the serial code */

        /* load top search hint */
//        advertRepository.loadSearchHint(new YesOrNoWithResponse(){
//            @Override
//            public void yes(Object data, boolean isFromOnline) {
//                home1View.showMainHint((String)data);
//            }
//
//            @Override
//            public void no(Object data, boolean isFromOnline) {
//
//            }
//        });

        /* 0 - stage */
//        advertRepository.loadHomeTopBanners(new YesOrNoWithResponse(){
//
//            @Override
//            public void yes(Object data, boolean isFromOnline) {
//                home1View.showMainSliding((List<AdsBanner>) data);
//            }
//
//            @Override
//            public void no(Object data, boolean isFromOnline) {
//
//            }
//        });

        /* 1- populate 6 main restaurants */
//        restaurantRepository.loadMainRestaurants(new YesOrNoWithResponse() {
//
//            @Override
//            public void yes(Object data, boolean isFromOnline) {
//                checkNotNull(data);
//                home1View.inflateMainRestaurants((List<RestaurantEntity>) data);
//                home1View.showLoadingProgress(false);
//            }
//
//            @Override
//            public void no(Object data, boolean isFromOnline) {
//
//            }
//        });

        /* 2- populate Group48 views */
//        advertRepository.load48MainAds(new YesOrNoWithResponse(){
//            @Override
//            public void yes(Object data, boolean isFromOnline) {
//                home1View.inflateMain48((List<ProductAdvertItem>) data);
//            }
//
//            @Override
//            public void no(Object data, boolean isFromOnline) {}
//        });

        /* 3- populate Group10 views */
//        advertRepository.loadGroup10Ads(new YesOrNoWithResponse(){
//            @Override
//            public void yes(Object data, boolean isFromOnline) {
//                home1View.inflateGroupsPubLongList((List<Group10AdvertItem>) data);
//            }
//
//            @Override
//            public void no(Object data, boolean isFromOnline) {
//
//            }
//        });

        // load previous data from the database
    }

    private void loadMainRestaurant(JsonObject data) {
        /* load restaurants */
        restaurantRepository.loadMainRestaurants(data, new YesOrNoWithResponse() {

            @Override
            public void yes(Object data, boolean isFromOnline) {
                checkNotNull(data);
                home1View.inflateMainRestaurants((List<RestaurantEntity>) data);
                home1View.showLoadingProgress(false);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {
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
