package tg.tmye.kaba.activity.cart.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.cart.contract.ShoppingContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.favorite.Favorite;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;
import tg.tmye.kaba.data.shoppingcart.source.BasketRepository;

/**
 * By abiguime on 22/02/2018.
 * email: 2597434002@qq.com
 */

public class ShoppingCartPresenter implements ShoppingContract.Presenter {


    private final ShoppingContract.View shoppingCartView;
    private final BasketRepository basketRepository;

    /* repository */

    public ShoppingCartPresenter(BasketRepository basketRepository, ShoppingContract.View shoppingCartView) {

        this.basketRepository = basketRepository;
        this.shoppingCartView = shoppingCartView;
//        shoppingCartView.setPresenter(this);
    }

    public void populateBasket () {

        /* load data from repo and put it into the view */
        /* get a map: key = restaurant ;; content is a list of basketItems */

        shoppingCartView.showLoading(true);
//        basketRepository.onlineBasketLoad();
        basketRepository.loadBasketItems (new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                shoppingCartView.showLoading(false);
            }

            @Override
            public void onSysError() {
                shoppingCartView.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Gson gson = new Gson();

                Log.d(Constant.APP_TAG, " - - "+jsonResponse);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                ShoppingBasketGroupItem[] shoppingGroupItems =
                        gson.fromJson(data.get("basket"), new TypeToken<ShoppingBasketGroupItem[]>(){}.getType());
                List<ShoppingBasketGroupItem> shoppingBasketGroupItems = Arrays.asList(shoppingGroupItems);
                shoppingCartView.showBasketList(shoppingBasketGroupItems);
                shoppingCartView.showLoading(false);
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
