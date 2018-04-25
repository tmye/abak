package tg.tmye.kaba.activity.trans.contract;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.delivery.DeliveryAddress;

/**
 * By abiguime on 20/04/2018.
 * email: 2597434002@qq.com
 */

public interface CommandDetailsContract {

    public interface View extends BaseView {

        void showLoading(boolean isLoading);
        /* in case there is a network error */
        void networkError();

        void addToBasketSuccessfull(boolean isSuccessfull);

        void purchaseSuccessfull(boolean isSuccessfull);
    }


    public interface Presenter extends BasePresenter {

        /* upload command to server */
        void addCommandToBasket(Map<Restaurant_Menu_FoodEntity, Integer> commands);

        void purchaseNow(boolean payAtArrival, HashMap<Restaurant_Menu_FoodEntity, Integer> food_quantity, DeliveryAddress deliveryAddress);
    }

}
