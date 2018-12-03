package tg.tmye.kaba.activity.FoodDetails.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.FoodDetails.contract.FoodDetailsContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Food.source.FoodRepository;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.advert.Group10AdvertItem;

/**
 * By abiguime on 13/04/2018.
 * email: 2597434002@qq.com
 */

public class FoodDetailPresenter implements FoodDetailsContract.Presenter {

    FoodDetailsContract.View view;
    FoodRepository repository;
    private Gson gson = new Gson();


    public FoodDetailPresenter (FoodDetailsContract.View view, FoodRepository repository) {

        this.repository = repository;
        this.view = view;
    }

    @Override
    public void setFavorite(Restaurant_Menu_FoodEntity foodEntity) {

        /* send current, and send it back */
        view.showLoading(true);
        repository.setFavorite(foodEntity, new NetworkRequestThreadBase.NetRequestIntf<String>() {
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
                Log.d(Constant.APP_TAG, jsonResponse);
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                int favorite = data.get("favorite").getAsInt();
                view.setFavorite(favorite);
            }
        });
    }

    @Override
    public void loadFood(int food_id) {

        view.showLoading(true);

        /* load food and drink entities */
        ///notification/food
        repository.loadFood(food_id, new NetworkRequestThreadBase.NetRequestIntf<String>(){
            @Override
            public void onNetworkError() {
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.showErrorPage(true);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();

                    RestaurantEntity restaurantEntity = gson.fromJson(data.get("restaurant"), RestaurantEntity.class);
                    Restaurant_Menu_FoodEntity foodEntity = gson.fromJson(data.get("food"), Restaurant_Menu_FoodEntity.class);
                    Restaurant_Menu_FoodEntity[] restaurant_drinks = gson.fromJson(data.get("restaurant_drinks"), new TypeToken<Restaurant_Menu_FoodEntity[]>() {
                    }.getType());

                    view.inflateFood(restaurantEntity, foodEntity, restaurant_drinks == null ? null : Arrays.asList(restaurant_drinks));
                    view.showLoading(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.finishActivity();
                }
            }
        });

    }

    @Override
    public void start() {
    }

    public void addToBasket(Restaurant_Menu_FoodEntity foodEntity, int quantiy) {

        /* add food and quantity to basket */

    }
}
