package tg.tmye.kaba.activity.home.contracts;


import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.Feeds.NewsFeed;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.Group10AdvertItem;


/**
 * By abiguime on 18/02/2018.
 * email: 2597434002@qq.com
 */

public interface F_HomeContract {


    interface View extends BaseView<F_HomeContract.Presenter> {

        /* passive actions of the view */
        void showMainSliding(List<AdsBanner> ads);
        void inflateMainRestaurants(List<RestaurantEntity> restaurantEntityList);

        void inflateMain48(AdsBanner promotion, AdsBanner events);
        void inflateGifSpace (List<AdsBanner> gif);
        void inflateGroupsPubLongList(List<Group10AdvertItem> group10AdvertItems);
        void inflateFeedsList(List<NewsFeed> newsFeeds);

        /* loading acting with users */
        void showLoadingProgress(boolean isVisible);
        void showErrorMessage(String message); // may create an action in case
        void sysError(String message);

        /* active actions from the view to the presenter */

        /*  */
        void showMainHint(String data);

        void hideErrorPage();
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
