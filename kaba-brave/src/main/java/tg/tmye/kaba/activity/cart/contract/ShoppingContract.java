package tg.tmye.kaba.activity.cart.contract;

import java.util.List;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;

/**
 * By abiguime on 22/02/2018.
 * email: 2597434002@qq.com
 */

public interface ShoppingContract {

    public interface View extends BaseView {

        /* show basket item list */
        void showBasketList(List<ShoppingBasketGroupItem> shoppingbasketContent);

        /* delete basket list Item */
        void deleteBasketItem();


        void updateBasketItem();

        void showLoading(boolean isLoading);

        /* in case there is a network error */
        void networkError();
    }


    public interface Presenter extends BasePresenter {

        /* update basket list */
        void updateBasket();

        void deleteBasketItem();

    }

}
