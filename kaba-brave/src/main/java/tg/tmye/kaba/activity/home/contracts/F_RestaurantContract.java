package tg.tmye.kaba.activity.home.contracts;


import java.util.List;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;


/**
 * By abiguime on 18/02/2018.
 * email: 2597434002@qq.com
 */

public interface F_RestaurantContract {


    interface View extends BaseView<F_RestaurantContract.Presenter> {

        void inflateRestaurantList(List<RestaurantEntity> restaurantEntities);

    }

    interface Presenter extends BasePresenter {

    }
}
