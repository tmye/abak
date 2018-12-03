package tg.tmye.kaba.activity.UserAcc.adresses.presenter;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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
        repo.loadUserDeliveryAdresses (new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.onNetworkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);

                view.showLoading(false);

                Gson gson = new Gson();
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();

                /* get fields */
                DeliveryAddress[] deliveryAddresses =
                        gson.fromJson(data.get("adresses"), new TypeToken<DeliveryAddress[]>(){}.getType());

                view.inflateAdresses(Arrays.asList(deliveryAddresses));
            }

            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }
        });
    }


    @Override
    public void uploadNewAdressToServer(DeliveryAddress adress) {

        /* convert path into base64 */
        view.showLoading(true);

        if (adress.picture != null)
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
                view.addressCreationFailure();
                view.showLoading(false);
            }

            @Override
            public void onSysError() {
                view.addressCreationFailure();
                view.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
//                JsonObject data = obj.get("data").getAsJsonObject();
                int error = obj.get("error").getAsInt();
                if (error == 0) {
                    view.addressCreationSuccess();
                } else {
                    view.addressCreationFailure();
                }
                view.showLoading(false);
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
        view.showLoading (true);
        repo.deleteDeliveryAddress(address, new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading (false);
            }

            @Override
            public void onSysError() {
                view.showLoading (false);
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
                view.showLoading (false);
            }

            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }
        });
    }

    @Override
    public void inflateLocation(LatLng location) {

        view.showLoading (true);
        repo.getGpsPositionDetails(location, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading (false);
            }

            @Override
            public void onSysError() {
                view.showLoading (false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int error = obj.get("error").getAsInt();

                JsonObject data = obj.get("data").getAsJsonObject();

                // get daily restaurants
//                Command[] commands =
//                        gson.fromJson(data.get("commands"), new TypeToken<Command[]>() {
//                        }.getType());

                String description_details = data.get("display_name").getAsString();
                String quartier = data.get("address").getAsJsonObject().get("suburb").getAsString();

                view.showCurrentAddressDetails(quartier, description_details);

                view.showLoading (false);
            }

        });
    }

}
