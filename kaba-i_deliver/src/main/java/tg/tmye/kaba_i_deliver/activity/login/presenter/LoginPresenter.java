package tg.tmye.kaba_i_deliver.activity.login.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.login.contract.LoginContract;
import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba_i_deliver.data.delivery.KabaShippingMan;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;
import tg.tmye.kaba_i_deliver.syscore.Constant;


/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final DeliveryManRepository livreurOnlineRepository;
    private final LoginContract.View view;
    private Gson gson = new Gson();

    public LoginPresenter (LoginContract.View view, DeliveryManRepository livreurOnlineRepository)  {

        this.view = view;
        this.livreurOnlineRepository = livreurOnlineRepository;
    }

    public void login (String livreur_nickname, String password) {

        view.showLoading(true);
        livreurOnlineRepository.login(livreur_nickname, password, new NetworkRequestThreadBase.NetRequestIntf<String>() {
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
                    String token = obj.get("data").getAsJsonObject().get("payload").getAsJsonObject().get("token").getAsString();
                    KabaShippingMan shippingMan =
                            gson.fromJson(obj.get("data").getAsJsonObject().get("customer"), new TypeToken<KabaShippingMan>(){}.getType());
                    livreurOnlineRepository.saveToken(token);
                    livreurOnlineRepository.saveDeliverman(shippingMan);

                   /* active updating data with server */
                    livreurOnlineRepository.setIsNotOkWithServer();
                /* send push token and user id to server */
                    livreurOnlineRepository.sendPushData ();

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
