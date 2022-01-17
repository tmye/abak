package tg.tmye.kaba.partner.activities.commands.presenter;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.partner._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.partner.activities.commands.contract.MyCommandContract;
import tg.tmye.kaba.partner.data.command.Command;
import tg.tmye.kaba.partner.data.command.source.CommandRepository;
import tg.tmye.kaba.partner.syscore.Constant;

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

//                L"error":0,"message":"","data":{"pending":[{"id":21,"customer_username":"92109474","last_update":1549061761,"last_update_sample":"2019-02-01 22:56:01","start_time":"2019-02-01 22:56:01","start_date":"2019-02-01","state":0,"reason":null,"state_show":"En attente","restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"shipping_address":{"id":77,"user_id":77,"location":"6.211997:1.185470","name":"Chez moi","description":"Non loin de l'Eglise MIGEC","indication":null,"quartier":"Cacaveli","description_details":"Non loin de l'Eglise MIGEC","near":"","picture":[],"phone_number":"92109474"},"shipping_pricing":"500","food_list":[{"id":390,"name":"Cordon bleu","description":null,"priority":0,"promotion":1,"promotion_price":"2300","pic":"food_pic\/oTXoWvvyXjeNYCr.jpg","food_details_pictures":["food_pic\/KHABkSwLIIca4bN.jpg","food_pic\/cLYqDgfsgGuL3BA.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"29-01-2019 16:09:18pm","rating_quantity":0,"rating_percentage":0,"quantity":1,"food_description":null},{"id":391,"name":"Filet de b\u0153uf ","description":null,"priority":0,"promotion":0,"promotion_price":null,"pic":"food_pic\/ldvUjWgXHKX6pEs.jpg","food_details_pictures":["food_pic\/7mjjCwewcHE4jqk.jpg","food_pic\/eqWEyLuASAJPzoY.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"10-10-2018 15:41:47pm","rating_quantity":0,"rating_percentage":0,"quantity":1,"food_description":null}],"passphrase":"connecter","is_payed_at_arrival":false,"price_command":7000,"price_remise":5800,"total":6300}],"cooking":[],"shipping":[{"id":20,"customer_username":"92109474","last_update":1549062902,"last_update_sample":"2019-02-01 23:15:02","start_time":"2019-02-01 20:45:01","start_date":"2019-02-01","state":2,"reason":null,"state_show":"En livraison","livreur":{"id":1,"name":"Tani Yao","is_active":true,"workcontact":"0987654","employee_entity":{"id":1,"first_name":"Tani","last_name":"Yao","address":"23456789","job_title":"Livreur","contact":"23456789","num_cnss":"4567890","num_cni":"456789","pic":"employee_picture\/"},"restaurant":[{"id":7,"name":"Le phenicien","description":"Un autre restaurant upp\u00e9 de la capitale","email":"lephenicien@kaba.com","working_hour":"10:00-23:00","is_open":0,"self_open":0,"address":"Lom\u00e9","pic":"\/resto_pic\/re6.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null},{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null},{"id":10,"name":"Festival des Glaces","description":"Tu veux des glaces? Tu sais ou aller!","email":"festivaldesglaces@kaba.com","working_hour":"10:00-22:00","is_open":0,"self_open":0,"address":"Dekon","pic":"\/resto_pic\/restaurant_101541001630.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme101536898883.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null}],"vehicle":"CB TG 1744","is_available":true},"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"shipping_address":{"id":57,"user_id":57,"location":"6.217675:1.188539","name":"Chez Francis - Ul.","description":"Ago\u00e8-Assiy\u00e9y\u00e9, Agouenyive, R\u00e9gion Maritime, BP 02, Togo","indication":null,"quartier":"Ago\u00e8-Assiy\u00e9y\u00e9","description_details":"Ago\u00e8-Assiy\u00e9y\u00e9, Agouenyive, R\u00e9gion Maritime, BP 02, Togo","near":"Non Loin de Fil O Parc","picture":["wfWUX7JsbKEbQh2.jpg"],"phone_number":"99105978"},"shipping_pricing":"1000","food_list":[{"id":390,"name":"Cordon bleu","description":null,"priority":0,"promotion":1,"promotion_price":"2300","pic":"food_pic\/oTXoWvvyXjeNYCr.jpg","food_details_pictures":["food_pic\/KHABkSwLIIca4bN.jpg","food_pic\/cLYqDgfsgGuL3BA.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"29-01-2019 16:09:18pm","rating_quantity":0,"rating_percentage":0,"quantity":2,"food_description":null},{"id":391,"name":"Filet de b\u0153uf ","description":null,"priority":0,"promotion":0,"promotion_price":null,"pic":"food_pic\/ldvUjWgXHKX6pEs.jpg","food_details_pictures":["food_pic\/7mjjCwewcHE4jqk.jpg","food_pic\/eqWEyLuASAJPzoY.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"10-10-2018 15:41:47pm","rating_quantity":0,"rating_percentage":0,"quantity":2,"food_description":null}],"passphrase":"irritation","is_payed_at_arrival":false,"price_command":14000,"price_remise":11600,"total":12600}],"delivered":[],"cancelled":[],"rejected":[]}}og.d(Constant.APP_TAG, jsonResponse);
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

                    int coming_soon = data.get("restaurant").getAsJsonObject().get("coming_soon").getAsInt();

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

                    int calendar_is_open = data.get("restaurant").getAsJsonObject().get("is_open").getAsInt();
                    int manual_open_state = data.get("restaurant").getAsJsonObject().get("manual_open_state").getAsInt();

                    viewHomePage.inflateStats(calendar_is_open, manual_open_state, coming_soon, head_pic, resto_name, foods_sold, recipes);
                    viewHomePage.inflateCountStats(waiting_count, shipping_count, cooking_count);
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHomePage.sysError();
                }
            }
        });
    }

    @Override
    public void checkOpened(boolean isOpened) {

        if (isLoading)
            return;
        viewHomePage.showLoading(true);

        commandRepository.checkOpened (isOpened, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                viewHomePage.showLoading(false);
                viewHomePage.networkError();
            }

            @Override
            public void onSysError() {
                viewHomePage.showLoading(false);
                viewHomePage.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {
                try {
                    viewHomePage.showLoading(false);

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();

                    boolean is_opened = data.get("status").getAsBoolean();
                    viewHomePage.updateHomepage ();

                } catch (Exception e) {
                    e.printStackTrace();
             //       viewHomePage.presenterSwitchOpened (false);
                }
            }
        });
    }

    @Override
    public void start() {

    }
}
