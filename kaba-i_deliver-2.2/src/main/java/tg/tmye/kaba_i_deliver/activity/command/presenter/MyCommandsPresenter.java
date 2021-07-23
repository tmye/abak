package tg.tmye.kaba_i_deliver.activity.command.presenter;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.command.contract.MyCommandContract;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.data.hsn.HSN;
import tg.tmye.kaba_i_deliver.syscore.ILog;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class MyCommandsPresenter implements MyCommandContract.Presenter {

    private final CommandRepository commandRepository;
    private final MyCommandContract.View view;
    private Gson gson = new Gson();

    public boolean isLoading = false;

    /* repository, and view */
    public MyCommandsPresenter(MyCommandContract.View view, CommandRepository commandRepository) {

        this.view = view;
        this.commandRepository = commandRepository;
    }


    @Override
    public void loadActualCommandsList() {

        if (isLoading)
            return;
        isLoading = true;
        /* get the informations of the restaurant, and retrieve the list of commands that have to
         * be retrieven */
        view.showLoading(true);

        commandRepository.getRestaurantCommands_Deliver(new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.showLoading(false);
                isLoading = false;
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                isLoading = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {

                isLoading = false;

                ILog.print(jsonResponse);

                // pending - shipping - delivered - cancelled - rejected
                try {

                    List<Command> preorders = new ArrayList<>();
                    List<Command> wait_for_me = new ArrayList<>();
                    List<Command> shipping = new ArrayList<>();
                    List<Command> delivered = new ArrayList<>();
                    List<HSN> hsns = new ArrayList<>();

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();
                    // get daily restaurants
                    Command[] preorders_array =
                            gson.fromJson(data.get("preorders"), new TypeToken<Command[]>() {
                            }.getType());

                    HSN[] hsn_array =
                            gson.fromJson(data.get("hsn"), new TypeToken<HSN[]>() {
                            }.getType());

                    Command[] waiting_array =
                            gson.fromJson(data.get("waiting"), new TypeToken<Command[]>() {
                            }.getType());

                    Command[] shipping_array =
                            gson.fromJson(data.get("shipping"), new TypeToken<Command[]>() {
                            }.getType());

                    Command[] delivered_array =
                            gson.fromJson(data.get("delivered"), new TypeToken<Command[]>() {
                            }.getType());

                    preorders = new ArrayList<>(Arrays.asList(preorders_array));
                    wait_for_me = new ArrayList<>(Arrays.asList(waiting_array));
                    shipping = new ArrayList<>(Arrays.asList(shipping_array));
                    delivered = new ArrayList<>(Arrays.asList(delivered_array));
                    hsns = new ArrayList<>(Arrays.asList(hsn_array));

                    view.inflateCommands(preorders, hsns, wait_for_me, shipping, delivered);

                } catch (Exception e) {
                    e.printStackTrace();
                    view.inflateCommands(null, null,null,null, null);
                }
                view.showLoading(false);
            }
        });

    }

    @Override
    public void stopDeliveryService(boolean wantToLogout) {


        if (isLoading)
            return;
        isLoading = true;
        /* get the informations of the restaurant, and retrieve the list of commands that have to
         * be retrieven */
        view.showLoading(true);

        commandRepository.stopDeliveryMode(new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.showLoading(false);
                isLoading = false;
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                isLoading = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {

                isLoading = false;
                ILog.print(jsonResponse);
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();
                    view.exitDeliveryModeSuccess(error == 0 , wantToLogout);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.exitDeliveryModeSuccess(false, wantToLogout);
                }
                view.showLoading(false);
            }
        });
    }

    @Override
    public void start() {

    }
}
