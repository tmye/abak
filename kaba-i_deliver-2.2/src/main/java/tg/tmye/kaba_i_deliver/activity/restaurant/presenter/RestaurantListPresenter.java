package tg.tmye.kaba_i_deliver.activity.restaurant.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.restaurant.contract.RestaurantListContract;
import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba_i_deliver.data.Restaurant.source.RestaurantRepository;
import tg.tmye.kaba_i_deliver.syscore.ILog;

/**
 * By abiguime on 04/06/2018.
 * email: 2597434002@qq.com
 */

public class RestaurantListPresenter implements RestaurantListContract.Presenter {


    private RestaurantListContract.View view;
    private RestaurantRepository repository;
    private Gson gson = new Gson();

    private boolean isLoading = false;

    public RestaurantListPresenter(RestaurantListContract.View view, RestaurantRepository repository) {

        this.view = view;
        this.repository = repository;
    }

  /*  @Override
    public void loadDistanceOptimizedDeliverList() {

        if (!isLoading) {

            view.showLoading(true);
            isLoading = true;

            repository.loadDistanceOptimizedDeliverList(new NetworkRequestThreadBase.NetRequestIntf<String>() {

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

                    isLoading = false;

                    try {
                        ILog.print(jsonResponse);
                        *//* deflate all elements and send the commands list to where it should *//*
                        JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                        JsonObject data = obj.get("data").getAsJsonObject();

                        Command[] commands =
                                gson.fromJson(data.get("commands"), new TypeToken<Command[]>() {
                                }.getType());

                        List<Command> commandz = new ArrayList<>(Arrays.asList(commands));
                        view.inflateCommandList(commandz);
                        view.showLoading(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.inflateCommandList(null);
                        view.showLoading(false);
                    }
                }
            });
        }
    }
*/

    @Override
    public void start() {

    }

    @Override
    public void getRestaurants() {
        if (!isLoading) {

            view.showLoading(true);
            isLoading = true;

            repository.loadKabaRestaurant(new NetworkRequestThreadBase.NetRequestIntf<String>() {

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

                    isLoading = false;

                    try {
                        ILog.print(jsonResponse);
                        /* deflate all elements and send the commands list to where it should */
                        JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                        JsonObject data = obj.get("data").getAsJsonObject();

                        RestaurantEntity[] res =
                                gson.fromJson(data.get("restaurants"), new TypeToken<RestaurantEntity[]>() {
                                }.getType());

                        List<RestaurantEntity> restaurants = new ArrayList<>(Arrays.asList(res));
                        view.inflateRestaurantList(restaurants);
                        view.showLoading(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.inflateRestaurantList(null);
                        view.showLoading(false);
                    }
                }
            });
        }
    }
}
