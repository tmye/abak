package tg.tmye.kaba.data.delivery.source;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 06/04/2018.
 * email: 2597434002@qq.com
 */

public class DeliveryAdresseRepo {

    private final Context context;

    NetworkRequestThreadBase networkRequestThreadBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;

    public DeliveryAdresseRepo (Context context) {
        this.context = context;
        /* get the threads */
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
    }

    public void loadUserDeliveryAdresses(final NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        /* token of the user */
        final String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

      /*  networkRequestThreadBase.run(new NetworkRequestThreadBase.OnNetworkAction() {
            @Override
            public void run() {
                networkRequestThreadBase.loggedOnGet (
                        Config.LINK_GET_ADRESSES, token, netRequestIntf
                );
            }
        });*/
      networkRequestThreadBase.getDataWithToken(Config.LINK_GET_ADRESSES, null, token, netRequestIntf);
    }

    public void postNewAdressToServer(DeliveryAddress address, final NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        Map<String, Object> data = new HashMap<>();
        data.put("id", address.id);
        data.put("name", address.name);
        data.put("location", address.location);
        data.put("phone_number", address.phone_number);
        data.put("description", address.description);
        data.put("description_details", address.description);
        data.put("near", address.near);
        data.put("quartier", address.quartier);
        if (address.picture != null && address.picture.length > 0)
            data.put("picture", UtilFunctions.toJsonData(address.picture));

        networkRequestThreadBase.postMapDataWithToken(Config.LINK_CREATE_NEW_ADRESS, data, token, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                netRequestIntf.onNetworkError();
            }

            @Override
            public void onSysError() {
                netRequestIntf.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                netRequestIntf.onSuccess(jsonResponse);
            }
        });
    }

    public void deleteDeliveryAddress(DeliveryAddress address, final NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        Map<String, Object> data = new HashMap<>();
        data.put("id", address.id);

        networkRequestThreadBase.postMapDataWithToken(Config.LINK_DELETE_ADRESS, data, token, netRequestIntf);
    }

    public void getGpsPositionDetails(LatLng location, final NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("#.######");
//        df.setCurrency(Curre);
        df.setRoundingMode(RoundingMode.CEILING);

        String lat = df.format(location.latitude);
        String lon = df.format(location.longitude);

        String position = lat+":"+lon;

        Map<String, Object> data = new HashMap<>();
        data.put("coordinates", position);

        networkRequestThreadBase.postMapDataWithToken(Config.LINK_GET_LOCATION_DETAILS, data, token, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                intf.onNetworkError();
            }

            @Override
            public void onSysError() {
                intf.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {
                intf.onSuccess(jsonResponse);
            }
        });
    }
}
