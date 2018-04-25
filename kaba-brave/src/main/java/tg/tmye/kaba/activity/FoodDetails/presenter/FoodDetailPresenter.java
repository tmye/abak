package tg.tmye.kaba.activity.FoodDetails.presenter;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.FoodDetails.contract.FoodDetailsContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Food.source.FoodRepository;

/**
 * By abiguime on 13/04/2018.
 * email: 2597434002@qq.com
 */

public class FoodDetailPresenter implements FoodDetailsContract.Presenter {

    FoodDetailsContract.View view;
    FoodRepository repository;


    public FoodDetailPresenter (FoodDetailsContract.View view, FoodRepository repository) {

        this.repository = repository;
        this.view = view;
    }

    @Override
    public void setFavorite(Restaurant_Menu_FoodEntity foodEntity) {

        /* send current, and send it back */
        repository.setFavorite(foodEntity, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {

            }

            @Override
            public void onSysError() {
            }

            @Override
            public void onSuccess(String jsonResponse) {
                Log.d(Constant.APP_TAG, jsonResponse);
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                int favorite = data.get("favorite").getAsInt();
                view.setFavorite(favorite);
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
