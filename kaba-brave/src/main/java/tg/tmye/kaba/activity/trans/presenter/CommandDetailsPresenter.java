package tg.tmye.kaba.activity.trans.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.activity.trans.contract.CommandDetailsContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.command.source.CommandRepository;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.shoppingcart.source.BasketRepository;

/**
 * By abiguime on 20/04/2018.
 * email: 2597434002@qq.com
 */

public class CommandDetailsPresenter implements CommandDetailsContract.Presenter {

    private final CommandDetailsContract.View view;


    private final CommandRepository commandRepository;
    private final BasketRepository basketRepository;
    CustomerDataRepository customerDataRepository;

    public CommandDetailsPresenter (CommandDetailsContract.View view, CommandRepository commandRepository, CustomerDataRepository customerDataRepository) {

        this.view = view;
        this.commandRepository = commandRepository;
        /* repo, and view */
        basketRepository = null;
        this.customerDataRepository = customerDataRepository;
    }

    public CommandDetailsPresenter(BasketRepository basketRepository, CommandRepository commandRepository, CommandDetailsContract.View view) {
        this.view = view;
        this.commandRepository = commandRepository;
        this.basketRepository = basketRepository;
    }

    @Override
    public void start() {
    }


    @Override
    public void addCommandToBasket(Map<Restaurant_Menu_FoodEntity, Integer> commands) {

        /* add command to basket */
        view.showLoading(true);

        customerDataRepository.sendPushData();

        if (basketRepository != null) {

            basketRepository.addFoodToBasket (commands, new NetworkRequestThreadBase.NetRequestIntf<String>() {
                @Override
                public void onNetworkError() {
                    view.addToBasketSuccessfull(false);
                    view.showLoading(false);
                }

                @Override
                public void onSysError() {
                    view.addToBasketSuccessfull(false);
                    view.showLoading(false);
                }

                @Override
                public void onSuccess(String jsonResponse) {

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();

                    if (error == 0) {
                        view.addToBasketSuccessfull(true);
                    } else {
                        view.addToBasketSuccessfull(false);
                    }

                    Log.d(Constant.APP_TAG, " -  - "+jsonResponse);
                    view.showLoading(false);
                }
            });

        } else {
            /* error in adding to basket */
        }
    }

    @Override
    public void purchaseNow(boolean payAtArrival, HashMap<Restaurant_Menu_FoodEntity, Integer> food_quantity, DeliveryAddress deliveryAddress) {

        customerDataRepository.sendPushData();
        /*  */

        commandRepository.purchaseNow(payAtArrival, food_quantity, deliveryAddress, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {

            }

            @Override
            public void onSysError() {

            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int error = obj.get("error").getAsInt();

                if (error == 0) {
                    view.purchaseSuccessfull(true);
                } else {
                    view.purchaseSuccessfull(false);
                }

                Log.d(Constant.APP_TAG, " -  - "+jsonResponse);
                view.showLoading(false);
            }
        });
    }


}
