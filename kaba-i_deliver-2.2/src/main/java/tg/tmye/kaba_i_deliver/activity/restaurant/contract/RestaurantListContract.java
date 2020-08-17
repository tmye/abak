package tg.tmye.kaba_i_deliver.activity.restaurant.contract;


import java.util.List;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public interface RestaurantListContract {

    interface Presenter extends BasePresenter {

//        void login(String username, String password);
        void getRestaurants();
    }

    interface View extends BaseView<Presenter> {

        void inflateRestaurantList(List<RestaurantEntity> restaurants);

        void networkError();

        void showLoading(boolean isLoading);

        void sysError();
    }
}
