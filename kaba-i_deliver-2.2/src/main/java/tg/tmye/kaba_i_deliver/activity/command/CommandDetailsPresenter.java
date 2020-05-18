package tg.tmye.kaba_i_deliver.activity.command;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.command.contract.CommandDetailsContract;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.data.delivery.DeliveryAddress;
import tg.tmye.kaba_i_deliver.data.delivery.KabaShippingMan;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;
import tg.tmye.kaba_i_deliver.syscore.Constant;
import tg.tmye.kaba_i_deliver.syscore.ILog;

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

        commandRepository.getUpdateCommandList(new NetworkRequestThreadBase.NetRequestIntf<String>() {
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

                        ILog.print(jsonResponse);
                        /* retrieve the object */
                        JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                        JsonObject data = obj.get("data").getAsJsonObject();
                        // get daily restaurants
                        Command command =
                                gson.fromJson(data.get("command"), new TypeToken<Command>() {
                                }.getType());

                        DeliveryAddress deliveryAddress = gson.fromJson(data.get("command").getAsJsonObject().get("shipping_address"), new TypeToken<DeliveryAddress>(){}.getType());
                        view.inflateCommandDetails(command, deliveryAddress);
                        view.showLoading(false);
                    }
                }
        );
    }

    public void setShippingToDone(Command command) {

        view.showLoading(true);
        commandRepository.setShippingToDone (command.id, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                    view.networkError();
                    view.showLoading(false);
            }

            @Override
            public void onSysError() {
                view.syserror();
                view.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                ILog.print(jsonResponse);
                        /* retrieve the object */
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int error = obj.get("error").getAsInt();
                // get daily restaurants
                if (error == 0) {
                    view.setShippingDoneSuccess(true);
                } else {
                    view.setShippingDoneSuccess(false);
                }
                /* if error is 0, then shipping done */
                view.showLoading(false);
            }
        });
    }

    public void startShipping(Command command) {

        view.showLoading(true);
        commandRepository.startShipping (command.id, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.networkError();
                view.showLoading(false);
            }

            @Override
            public void onSysError() {
                view.syserror();
                view.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                ILog.print(jsonResponse);
                /* retrieve the object */
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int error = obj.get("error").getAsInt();
                // get daily restaurants
                if (error == 0) {
                    view.startShippingSuccess(true);
                } else {
                    view.startShippingSuccess(false);
                }
                /* if error is 0, then shipping done */
                view.showLoading(false);
            }
        });
    }

    public void setPostponeCommand(Command command) {

    }
}
