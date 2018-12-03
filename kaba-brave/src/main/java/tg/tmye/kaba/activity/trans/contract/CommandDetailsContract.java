package tg.tmye.kaba.activity.trans.contract;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 20/04/2018.
 * email: 2597434002@qq.com
 */

public interface CommandDetailsContract {

    public interface View extends AuthBaseView {

        void showLoading(boolean isLoading);
        /* in case there is a network error */
        void networkError();

        /* make actions  */
        void addToBasketSuccessfull(boolean isSuccessfull);
        void purchaseSuccessfull(boolean isSuccessfull);

        void inflateCommandDetails(Command command, DeliveryAddress deliveryAddress);

        void isRestaurantAvailableForBuy(boolean isOpen);

        void sysError();

        /* inflate data */
        void inflateBillingComputations (boolean out_of_range, String price_total, String price_delivery,
                                         String price_remise, String price_net_to_pay,
                                         String price_command);

        void inflateMySolde(String solde);
    }


    public interface Presenter extends AuthBasePresenter {

        /* upload command to server */
        void addCommandToBasket(Map<Restaurant_Menu_FoodEntity, Integer> commands);

        void purchaseNow(String code, HashMap<Restaurant_Menu_FoodEntity, Integer> food_quantity, DeliveryAddress deliveryAddress);

        void loadCommandItems ();

        void loadCommandDetails(String command_id);

        void computePricing (RestaurantEntity restaurantEntity, Map<Restaurant_Menu_FoodEntity, Integer> commands, DeliveryAddress deliveryAddress);
    }

}
