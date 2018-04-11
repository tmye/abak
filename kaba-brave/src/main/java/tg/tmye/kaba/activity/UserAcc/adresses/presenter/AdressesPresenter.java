package tg.tmye.kaba.activity.UserAcc.adresses.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAcc.adresses.contract.AdressesContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.delivery.source.DeliveryAdresseRepo;

/**
 * By abiguime on 06/04/2018.
 * email: 2597434002@qq.com
 */

public class AdressesPresenter implements AdressesContract.Presenter {

    private final DeliveryAdresseRepo repo;
    private final AdressesContract.View view;

    public AdressesPresenter (AdressesContract.View view, DeliveryAdresseRepo repo) {

        this.view = view;
        this.repo = repo;
    }

    @Override
    public void start() {
        populateViews();
    }

    @Override
    public void populateViews() {

        view.showLoading(true);
        repo.loadUserDeliveryAdresses (new NetworkRequestThreadBase.NetRequestIntf<String>() {
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

                Gson gson = new Gson();
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();

                /* get fields */
                DeliveryAddress[] deliveryAddresses =
                        gson.fromJson(data.get("adresses"), new TypeToken<DeliveryAddress[]>(){}.getType());

                view.inflateAdresses(Arrays.asList(deliveryAddresses));
                view.showLoading(false);
            }
        });
    }


    @Override
    public void uploadNewAdressToServer(DeliveryAddress adress) {

        /* convert path into base64 */

        for (int i = 0; i < adress.picture.length; i++) {

            String tmp = adress.picture[i];
            if (!UtilFunctions.isBase64(tmp) && UtilFunctions.checkFileExistsLocally(tmp)) {
                tmp = UtilFunctions.changePathToBase64(adress.picture[i]);
            }
            adress.picture[i] = tmp;
        }

        /* post address */
        repo.postNewAdressToServer (adress, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
                int error = obj.get("error").getAsInt();
                if (error == 0) {
                    view.addressCreationSuccess();
                } else {
                    view.addressCreationFailure();
                }
            }

        });
    }

    @Override
    public void presentAddress(DeliveryAddress address) {
        view.inflateAdresses(address);
    }

    @Override
    public void deleteAddress(DeliveryAddress address) {

        /* show covered loading box ->  */
        view.showDeletingSuspendedLoadingBox ();
        repo.deleteDeliveryAddress(address, new NetworkRequestThreadBase.NetRequestIntf<String>() {
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
                int error = obj.get("error").getAsInt();
                if (error == 0) {
                    view.addressDeletedSuccess();
                } else {
                    view.addressDeletedFailure();
                }
            }
        });
    }

}
