package tg.tmye.kaba.restaurant.activities.commands.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.activities.commands.contract.CommandDetailsContract;
import tg.tmye.kaba.restaurant.data.command.Command;
import tg.tmye.kaba.restaurant.data.command.source.CommandRepository;
import tg.tmye.kaba.restaurant.data.delivery.DeliveryAddress;
import tg.tmye.kaba.restaurant.data.delivery.KabaShippingMan;
import tg.tmye.kaba.restaurant.syscore.Constant;

/**
 * By abiguime on 20/04/2018.
 * email: 2597434002@qq.com
 */

public class CommandDetailsPresenter implements CommandDetailsContract.Presenter {

    private final CommandDetailsContract.View view;


    private final CommandRepository commandRepository;
    private Gson gson = new Gson();


    public CommandDetailsPresenter(CommandRepository commandRepository, CommandDetailsContract.View view) {
        this.view = view;
        this.commandRepository = commandRepository;
    }

    @Override
    public void start() {
    }


    @Override
    public void loadCommandItems() {

        view.showLoading(true);
        commandRepository.getUpdateCommandList(new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.syserror();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);
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
        });
    }


    @Override
    public void loadCommandDetails(String command_id) {

        view.showLoading(true);
        commandRepository.getCommandDetails (command_id, new NetworkRequestThreadBase.NetRequestIntf<String>() {
                    @Override
                    public void onNetworkError() {
                        view.showLoading(false);
                        view.networkError();
                    }

                    @Override
                    public void onSysError() {
                        view.showLoading(false);
                        view.networkError();
                    }

                    @Override
                    public void onSuccess(String jsonResponse) {

                        view.showLoading(false);

                        Log.d(Constant.APP_TAG, jsonResponse);

                        /* retrieve the object */
                        JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                        JsonObject data = obj.get("data").getAsJsonObject();
                        // get daily restaurants
                        Command command =
                                gson.fromJson(data.get("command"), new TypeToken<Command>() {
                                }.getType());

                        DeliveryAddress deliveryAddress = gson.fromJson(data.get("command").getAsJsonObject().get("shipping_address"), new TypeToken<DeliveryAddress>(){}.getType());

                        List<KabaShippingMan> kabaShippingMen = new ArrayList<>();

                        if (command.state == 1) {
                            KabaShippingMan[] shippingMan = gson.fromJson(data.get("livreurs"), new TypeToken<KabaShippingMan[]>() {}.getType());
                            kabaShippingMen = new ArrayList<>(Arrays.asList(shippingMan));
                        }

                        view.inflateCommandDetails(command, kabaShippingMen, deliveryAddress);
                    }
                }
        );
    }

    @Override
    public void acceptCommand(final Command command) {

        view.showLoading(true);

        commandRepository.acceptCommand (command, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.syserror();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);

                Log.d(Constant.APP_TAG, jsonResponse);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                // get daily restaurants
                int error = gson.fromJson(obj.get("error"),Integer.class);
                if (error != 0) {
                    view.success(false, 0);
                    view.syserror();
                } else {
                    view.success(true, 1);
                }

            }
        });
    }

    @Override
    public void sendCommandToShipping(Command command, KabaShippingMan shippingMan) {

        /*  */
        view.showLoading(true);
        commandRepository.sendCommandToShipping (command, shippingMan, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                // get daily restaurants
                int error = gson.fromJson(obj.get("error"),Integer.class);
                if (error != 0) {
                    view.syserror();
                    view.success(false, 0);
                } else {
                    view.success(true, 2);
                }

            }
        });
    }

    @Override
    public void cancelCommand(int command_id, String motif) {

        /*  */
        view.showLoading(true);
        commandRepository.cancelCommand (command_id, motif, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                // get daily restaurants
                int error = gson.fromJson(obj.get("error"),Integer.class);

                view.onCancelSuccess();
            }
        });
    }


}
