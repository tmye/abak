package tg.tmye.kaba.activity.trans.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.activity.trans.contract.CommandDetailsContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.Error;
import tg.tmye.kaba.data.command.Command;
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
    private Gson gson = new Gson();
    private final BasketRepository basketRepository;
    CustomerDataRepository customerDataRepository;


    public CommandDetailsPresenter(BasketRepository basketRepository, CommandRepository commandRepository, CustomerDataRepository customerDataRepository, CommandDetailsContract.View view) {
        this.view = view;
        this.commandRepository = commandRepository;
        this.basketRepository = basketRepository;
        this.customerDataRepository = customerDataRepository;
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

            basketRepository.addFoodToBasket (commands, new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
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

                @Override
                public void onLoggingTimeout() {
                    view.onLoggingTimeout();
                }
            });

        } else {
            /* error in adding to basket */
        }
    }

    @Override
    public void purchaseNow(String code, HashMap<Restaurant_Menu_FoodEntity, Integer> food_quantity, DeliveryAddress deliveryAddress) {


        view.showLoading(true);
        customerDataRepository.sendPushData();
        /*  */

        commandRepository.purchaseNow(code, food_quantity, deliveryAddress, new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);

                try {
                    Log.d(Constant.APP_TAG, jsonResponse);

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();

                    if (error == 0) {
                        view.purchaseSuccessfull(true);
                    } else {
                        view.purchaseSuccessfull(false);
                    }

                    Log.d(Constant.APP_TAG, " -  - " + jsonResponse);
                } catch (Exception e){
                    e.printStackTrace();
                    this.onSysError();
                }
            }

            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }
        });
    }

    @Override
    public void loadCommandItems() {

        commandRepository.getUpdateCommandList(new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {

            }

            @Override
            public void onSysError() {

            }

            @Override
            public void onSuccess(String jsonResponse) {

                // work on the data, and get it to the view
//                        yesOrNoWithResponse.yes(Command.fakeList(6));
                /* inflate the result into a list of things */
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();
                    // get daily restaurants
                    Command[] commands =
                            gson.fromJson(data.get("commands"), new TypeToken<Command[]>() {
                            }.getType());
                    List<Command> commandList = Arrays.asList(commands);

//                    yesOrNoWithResponse.yes(commandList, true);
//                    view.
                } catch (Exception e){
                    e.printStackTrace();
//                    yesOrNoWithResponse.no(new Error.LocalError(), true);
                }
            }

            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }
        });
    }


    @Override
    public void loadCommandDetails(String command_id) {

        view.showLoading(true);
        commandRepository.getCommandDetails (command_id, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.networkError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);

                try {
                    /* retrive the object */
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();
                    // get daily restaurants
                    Command command =
                            gson.fromJson(data.get("command"), new TypeToken<Command>() {
                            }.getType());

                    DeliveryAddress deliveryAddress = gson.fromJson(data.get("command").getAsJsonObject().get("shipping_address"), new TypeToken<DeliveryAddress>() {
                    }.getType());

                    String price_total = data.get("command").getAsJsonObject().get("total_pricing").getAsString();
                    String price_delivery = data.get("command").getAsJsonObject().get("shipping_pricing").getAsString();
                    String price_remise = "-0";
                    String price_net_to_pay = data.get("command").getAsJsonObject().get("total_pricing").getAsString();
                    String price_command = data.get("command").getAsJsonObject().get("command_pricing").getAsString();

                    view.inflateBillingComputations(false, price_total, price_delivery, price_remise, price_net_to_pay, price_command);
                    view.inflateCommandDetails(command, deliveryAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.sysError();
                }
                view.showLoading(false);
            }

        });
    }

    @Override
    public void computePricing(RestaurantEntity restaurantEntity, final Map<Restaurant_Menu_FoodEntity, Integer> commands, DeliveryAddress deliveryAddress) {

        view.showLoading(true);
        commandRepository.computePricing (restaurantEntity, commands, deliveryAddress, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                try {
                    Log.d(Constant.APP_TAG, jsonResponse);
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();

                /*
                *
                * String price_total, String price_delivery,
                                         String price_remise, String price_net_to_pay,
                                         String price_command
                *
                * */
                    String price_total = data.get("total_pricing").getAsString();
                    String price_delivery = data.get("shipping_pricing").getAsString();
                    String price_remise = "-0";
                    String price_net_to_pay = data.get("total_pricing").getAsString();
                    String price_command = data.get("command_pricing").getAsString();
                    String solde = ""+data.get("account_balance").getAsInt();

                    boolean out_of_range = false;
                    try {
                        out_of_range = data.get("out_of_range").getAsBoolean();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out_of_range = true;
                    }

                    view.inflateMySolde(solde);
                    view.inflateBillingComputations(out_of_range, price_total, price_delivery, price_remise, price_net_to_pay, price_command);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                view.showLoading(false);
            }
        });
    }


    public void checkRestaurantOpen(RestaurantEntity restaurantEntity) {

        view.showLoading(true);
        commandRepository.checKRestaurantIsOpen(restaurantEntity, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.networkError();
            }

            @Override
            public void onSuccess(String jsonResponse) {
//                is_open
                Log.d(Constant.APP_TAG, jsonResponse);
                view.showLoading(false);
                try {
                    /* retrive the object */
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();
                    // get daily restaurants
                    boolean is_open = data.get("is_open").getAsBoolean();
                    view.isRestaurantAvailableForBuy(is_open);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.isRestaurantAvailableForBuy(false);
                }
            }
        });
    }
}
