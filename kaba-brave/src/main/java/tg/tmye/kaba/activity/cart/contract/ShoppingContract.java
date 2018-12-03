package tg.tmye.kaba.activity.cart.contract;

import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 22/02/2018.
 * email: 2597434002@qq.com
 */

public interface ShoppingContract {

    public interface View extends AuthBaseView {

        /* show basket item list */
        void showBasketList(List<ShoppingBasketGroupItem> shoppingbasketContent);

        /* delete basket list Item */
        void deleteBasketItem(BasketInItem basketInItem);

        /* update basket list item */
        void updateBasketItem();

        void showLoading(boolean isLoading);

        /* in case there is a network error */
        void networkError();

        void removeItemAt(int groupPosition, int itemPosition);
    }


    public interface Presenter extends AuthBasePresenter {

        /* update basket list */
        void updateBasket();

          void deleteBasketItem(BasketInItem basketInItem, int groupPosition, int itemPosition);
    }

}
