package tg.tmye.kaba.activity.cart.presenter;

import java.util.List;

import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.cart.contract.ShoppingContract;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketForView;
import tg.tmye.kaba.data.shoppingcart.source.BasketFoodRepository;

/**
 * By abiguime on 22/02/2018.
 * email: 2597434002@qq.com
 */

public class ShoppingCartPresenter implements ShoppingContract.Presenter {


    private final ShoppingContract.View shoppingCartView;
    private final BasketFoodRepository basketFoodRepository;

    /* repository */

    public ShoppingCartPresenter(BasketFoodRepository basketFoodRepository, ShoppingContract.View shoppingCartView) {

        this.basketFoodRepository = basketFoodRepository;
        this.shoppingCartView = shoppingCartView;
        shoppingCartView.setPresenter(this);
    }

    public void populateBasket () {

        /* load data from repo and put it into the view */
        /* get a map: key = restaurant ;; content is a list of basketItems */

        shoppingCartView.showLoading(true);
        basketFoodRepository.onlineBasketLoad(new YesOrNoWithResponse(){
            @Override
            public void yes(Object data, boolean isFromOnline) {
                shoppingCartView.showBasketList((List<ShoppingBasketForView>)data);
                shoppingCartView.showLoading(false);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {

                /* */
                shoppingCartView.showLoading(false);
                if (isFromOnline)
                    basketFoodRepository.offlineBasketLoad(this);
                else {
                    shoppingCartView.networkError();
                    shoppingCartView.showLoading(false);
                }
            }
        });
    }

    @Override
    public void updateBasket() {
        populateBasket();
    }

    @Override
    public void deleteBasketItem() {

    }

    @Override
    public void start() {
        populateBasket();
    }
}
