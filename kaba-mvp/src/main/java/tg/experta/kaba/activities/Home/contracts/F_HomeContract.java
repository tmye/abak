package tg.experta.kaba.activities.Home.contracts;


import java.util.List;

import tg.experta.kaba.BasePresenter;
import tg.experta.kaba.BaseView;
import tg.experta.kaba.data.Feeds.Ad;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;


/**
 * By abiguime on 18/02/2018.
 * email: 2597434002@qq.com
 */

public interface F_HomeContract {


    interface View extends BaseView<F_HomeContract.Presenter> {

        /* passive actions of the view */
        void showMainSliding(List<Ad> ads);
        void inflateMainRestaurants(List<RestaurantEntity> restaurantEntityList);
        void showPubLongList();

        /* loading acting with users */
        void showLoadingProgress(boolean isVisible);
        void showErrorMessage(String message); // may create an action in case


        /* active actions from the view to the presenter */
        void openRestaurant(RestaurantEntity restaurantEntity);
        void openAd(Ad ad);
        void openSearchVue(String trendRequest);
    }

    interface Presenter extends BasePresenter {

        // online
        void loadOnline();
        void persistOnlineLocally();

        // local
        void loadFromLocal();


        //
        void openRestaurant(RestaurantEntity restaurantEntity);
        void openAd(Ad ad);
        void openSearchVue(String trendRequest);
    }
}
