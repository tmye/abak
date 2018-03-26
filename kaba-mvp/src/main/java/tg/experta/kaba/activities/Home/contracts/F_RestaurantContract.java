package tg.experta.kaba.activities.Home.contracts;


import tg.experta.kaba.BasePresenter;
import tg.experta.kaba.BaseView;


/**
 * By abiguime on 18/02/2018.
 * email: 2597434002@qq.com
 */

public interface F_RestaurantContract {


    interface View extends BaseView<F_RestaurantContract.Presenter> {

        void showRestaurantList();

    }

    interface Presenter extends BasePresenter {

        void loadRestaurants();
    }
}
