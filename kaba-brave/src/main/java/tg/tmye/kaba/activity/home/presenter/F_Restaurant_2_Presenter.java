package tg.tmye.kaba.activity.home.presenter;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.home.contracts.F_RestaurantContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.source.RestaurantDbRepository;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class F_Restaurant_2_Presenter implements F_RestaurantContract.Presenter {


    private final RestaurantDbRepository restaurantDbRepository;
    private final F_RestaurantContract.View restaurant2View;


    public F_Restaurant_2_Presenter(RestaurantDbRepository restaurantDbRepository,
                                    F_RestaurantContract.View restaurant2View) {

        this.restaurantDbRepository = restaurantDbRepository;
        this.restaurant2View = restaurant2View;
        this.restaurant2View.setPresenter(this);
    }


    @Override
    public void start() {

        populateViews();
    }

    private void populateViews() {

        /* populate restaurant list */
        restaurant2View.showLoading(true);
        restaurantDbRepository.loadRestaurantList(new NetworkRequestThreadBase.NetRequestIntf<String>(){
            @Override
            public void onNetworkError() {
                restaurant2View.showNetworkError();
                restaurant2View.showLoading(false);
            }

            @Override
            public void onSysError() {
                restaurant2View.showSysError();
                restaurant2View.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                /* string*/
                Log.d(Constant.APP_TAG, jsonResponse);
//                restaurant2View.inflateRestaurantList(data);
                /* use the current json string and squeeze out all the need data */
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();

                /* check and save */
                restaurantDbRepository.checkAndSaveRestaurantList(data, jsonResponse);

                /* load restaurant into a list */
                loadRestaurantList (data);

                restaurant2View.showLoading(false);
            }

        });
    }

    private void loadRestaurantList(JsonObject data) {

        restaurantDbRepository.loadRestaurantList(data, new YesOrNoWithResponse<List<RestaurantEntity>>(){
            @Override
            public void yes(List<RestaurantEntity> data, boolean isFromOnline) {
                restaurant2View.inflateRestaurantList(data);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {

            }
        });
    }


}
