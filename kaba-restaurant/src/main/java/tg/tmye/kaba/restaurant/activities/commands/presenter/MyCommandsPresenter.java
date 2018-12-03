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
import tg.tmye.kaba.restaurant.activities.commands.contract.MyCommandContract;
import tg.tmye.kaba.restaurant.data.command.Command;
import tg.tmye.kaba.restaurant.data.command.source.CommandRepository;
import tg.tmye.kaba.restaurant.syscore.Constant;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class MyCommandsPresenter implements MyCommandContract.Presenter {

    private final CommandRepository commandRepository;
    private MyCommandContract.View view = null;
    private MyCommandContract.HomePageView viewHomePage = null;
    private Gson gson = new Gson();

    boolean isLoading = false;

    /* repository, and view */
    public MyCommandsPresenter(MyCommandContract.View view, CommandRepository commandRepository) {

        this.view = view;
        this.commandRepository = commandRepository;
    }

    public MyCommandsPresenter(MyCommandContract.HomePageView view, CommandRepository commandRepository) {

        this.viewHomePage = view;
        this.commandRepository = commandRepository;
    }


    @Override
    public void loadActualCommandsList() {

        /* get the informations of the restaurant, and retrieve the list of commands that have to
         * be retrieven */
        if (isLoading)
            return;

        isLoading = true;
        view.showLoading(true);
        commandRepository.getRestaurantCommands(new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                isLoading = false;
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                isLoading = false;
                view.showLoading(false);
                view.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);
                view.showLoading(false);
                isLoading = false;
                // pending - shipping - delivered - cancelled - rejected
                try {
                    List<Command> pending = new ArrayList<>();
                    List<Command> cooking = new ArrayList<>();
                    List<Command> shipping = new ArrayList<>();
                    List<Command> delivered = new ArrayList<>();
                    List<Command> cancelled = new ArrayList<>();
                    List<Command> rejected = new ArrayList<>();

                    List<Command> others = new ArrayList<>();

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();

                    JsonObject data = obj.get("data").getAsJsonObject();
                    // get daily restaurants
                    Command[] pending_array =
                            gson.fromJson(data.get("pending"), new TypeToken<Command[]>() {
                            }.getType());
                    Command[] cooking_array =
                            gson.fromJson(data.get("cooking"), new TypeToken<Command[]>() {
                            }.getType());
                    Command[] shipping_array =
                            gson.fromJson(data.get("shipping"), new TypeToken<Command[]>() {
                            }.getType());
                    Command[] delivered_array =
                            gson.fromJson(data.get("delivered"), new TypeToken<Command[]>() {
                            }.getType());
                    Command[] cancelled_array =
                            gson.fromJson(data.get("cancelled"), new TypeToken<Command[]>() {
                            }.getType());
                    Command[] rejected_array =
                            gson.fromJson(data.get("rejected"), new TypeToken<Command[]>() {
                            }.getType());

                    pending = new ArrayList<>(Arrays.asList(pending_array));
                    cooking = new ArrayList<>(Arrays.asList(cooking_array));
                    shipping = new ArrayList<>(Arrays.asList(shipping_array));
                    delivered = new ArrayList<>(Arrays.asList(delivered_array));
                    cancelled = new ArrayList<>(Arrays.asList(cancelled_array));
                    rejected = new ArrayList<>(Arrays.asList(rejected_array));

                    /* merge, cannceled and rejected */

                    others.addAll(cancelled);
                    others.addAll(rejected);

                    view.inflateCommands(pending, cooking, shipping, delivered, others);
                } catch (Exception e) {
                    e.printStackTrace();
//                    view.inflateCommands(null, null, null, null, null);
                    view.sysError();
                }
            }
        });
    }

    @Override
    public void loadStats() {

        if (isLoading)
            return;
        viewHomePage.showLoading(true);

        commandRepository.loadStats (new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                isLoading = false;
                viewHomePage.showLoading(false);
                viewHomePage.networkError();
            }

            @Override
            public void onSysError() {
                isLoading = false;
                viewHomePage.showLoading(false);
                viewHomePage.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                isLoading = false;

                try {
                    viewHomePage.showLoading(false);

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();

                    String recipes = data.get("recipes").getAsString();
                    String foods_sold = data.get("foods_sold").getAsString();

                    String head_pic = data.get("restaurant").getAsJsonObject().get("pic").getAsString();
                    String resto_name = data.get("restaurant").getAsJsonObject().get("name").getAsString();

                    int waiting_count = 0;
                    int shipping_count = 0;
                    int cooking_count = 0;

                    try {
                        waiting_count = data.get("waiting_count").getAsInt();
                        shipping_count = data.get("shipping_count").getAsInt();
                        cooking_count = data.get("cooking_count").getAsInt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    int is_open = data.get("restaurant").getAsJsonObject().get("is_open").getAsInt();

                    viewHomePage.inflateStats(is_open, head_pic, resto_name, foods_sold, recipes);
                    viewHomePage.inflateCountStats(waiting_count, shipping_count, cooking_count);
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHomePage.sysError();
                }
            }
        });
    }

    @Override
    public void start() {

    }
}
