package tg.tmye.kaba.restaurant.activities.login.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.activities.login.contract.LoginContract;
import tg.tmye.kaba.restaurant.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.restaurant.data.Restaurant.source.RestaurantOnlineRepository;
import tg.tmye.kaba.restaurant.syscore.Constant;


/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final RestaurantOnlineRepository restaurantOnlineRepository;
    private final LoginContract.View view;
    private Gson gson = new Gson();

    public LoginPresenter (LoginContract.View view, RestaurantOnlineRepository restaurantOnlineRepository)  {

        this.view = view;
        this.restaurantOnlineRepository = restaurantOnlineRepository;
    }

    public void login (String restaurant_nickname, String password) {

        view.showLoading(true);
        restaurantOnlineRepository.login(restaurant_nickname, password, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.networkError();
                view.showLoading(false);
            }

            @Override
            public void onSysError() {
                view.sysError();
                view.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {
                Log.d(Constant.APP_TAG, jsonResponse);
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int errorCode = obj.get("error").getAsInt();
                if (errorCode == 0) {
                    obj = obj.get("data").getAsJsonObject().get("payload").getAsJsonObject().get("data").getAsJsonObject();
                    String token = obj.get("payload").getAsJsonObject().get("token").getAsString();
                    RestaurantEntity restaurantEntity =
                            gson.fromJson(obj.get("customer"), new TypeToken<RestaurantEntity>(){}.getType());
                    restaurantOnlineRepository.saveToken(token);
                    restaurantOnlineRepository.saveRestaurant(restaurantEntity);
                    /* active updating data with server */
                    restaurantOnlineRepository.setIsNotOkWithServer();
                    /* send push token and user id to server */
                    restaurantOnlineRepository.sendPushData ();
                    view.loginSuccess(true);
                } else {
                    String message = obj.get("message").getAsString();
                    view.loginSuccess(false);
                }
            }
        });

        /**/
    }


    @Override
    public void start() {

    }
}
