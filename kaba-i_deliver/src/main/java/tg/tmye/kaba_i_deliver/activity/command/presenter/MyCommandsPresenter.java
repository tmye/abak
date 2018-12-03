package tg.tmye.kaba_i_deliver.activity.command.presenter;


import android.util.Log;

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
import tg.tmye.kaba_i_deliver.syscore.Constant;

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

                Log.d(Constant.APP_TAG, jsonResponse);

                // pending - shipping - delivered - cancelled - rejected

                List<Command> shipping = new ArrayList<>();
                List<Command> delivered = new ArrayList<>();

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                // get daily restaurants
                Command[] shipping_array =
                        gson.fromJson(data.get("shipping"), new TypeToken<Command[]>() {
                        }.getType());

                Command[] delivered_array =
                        gson.fromJson(data.get("delivered"), new TypeToken<Command[]>() {
                        }.getType());

                shipping = new ArrayList<>(Arrays.asList(shipping_array));
                delivered = new ArrayList<>(Arrays.asList(delivered_array));

                /* merge, cannceled and rejected */

                view.inflateCommands(shipping, delivered);
                view.showLoading(false);
            }
        });
        view.inflateCommands(null, null);
//                view.inflateCommands(pending, cooking, shipping, delivered, others);
//        view.showLoading(false);
    }

    @Override
    public void start() {

    }
}
