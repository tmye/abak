package tg.tmye.kaba.activity.home.contracts;


import java.util.List;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.Feeds.NewsFeed;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.LightRestaurant;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.Group10AdvertItem;
import tg.tmye.kaba.data.advert.ProductAdvertItem;


/**
 * By abiguime on 18/02/2018.
 * email: 2597434002@qq.com
 */

public interface F_HomeContract {


    interface View extends BaseView<F_HomeContract.Presenter> {

        /* passive actions of the view */
        void showMainSliding(List<AdsBanner> ads);
        void inflateMainRestaurants(List<LightRestaurant> restaurantEntityList);

        void inflateMain48(List<AdsBanner> productAdvertItems);
        void inflateGroupsPubLongList(List<Group10AdvertItem> group10AdvertItems);
        void inflateFeedsList(List<NewsFeed> newsFeeds);

        /* loading acting with users */
        void showLoadingProgress(boolean isVisible);
        void showErrorMessage(String message); // may create an action in case

        /* active actions from the view to the presenter */
        void openRestaurant(LightRestaurant restaurantEntity);
        void openKabaShowPicture(SimplePicture.KabaShowPic pic);
        void openProductAdvert(AdsBanner productAdvertItem);
        void openAdBanner(AdsBanner ad); /* those that slide */

        /*  */
        void showMainHint(String data);
    }

    interface Presenter extends BasePresenter {

        // online
        void updateView();

        //
//        void openRestaurant(RestaurantEntity restaurantEntity);
//        void openAd(Ad ad);
//        void openSearchVue(String trendRequest);
    }
}
