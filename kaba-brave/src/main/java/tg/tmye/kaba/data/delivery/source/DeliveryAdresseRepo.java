package tg.tmye.kaba.data.delivery.source;

import android.content.Context;

import java.util.HashMap;
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

    public void loadUserDeliveryAdresses(final NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {

        /* token of the user */
        final String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        networkRequestThreadBase.run(new NetworkRequestThreadBase.OnNetworkAction() {
            @Override
            public void run() {
                networkRequestThreadBase.loggedOnGet (
                        Config.LINK_GET_ADRESSES, token, netRequestIntf
                );
            }
        });
    }

    public void postNewAdressToServer(DeliveryAddress address, final NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        Map<String, Object> data = new HashMap<>();
        data.put("id", address.id);
        data.put("name", address.name);
        data.put("location", address.location);
        data.put("phone_number", address.phone_number);
        data.put("description", address.description);
        data.put("picture", UtilFunctions.toJsonData(address.picture));

        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_CREATE_NEW_ADRESS, data, token, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {

            }

            @Override
            public void onSysError() {

            }

            @Override
            public void onSuccess(String jsonResponse) {

                netRequestIntf.onSuccess(jsonResponse);
            }
        });
    }

    public void deleteDeliveryAddress(DeliveryAddress address, final NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        Map<String, Object> data = new HashMap<>();
        data.put("id", address.id);

        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_DELETE_ADRESS, data, token, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {

            }

            @Override
            public void onSysError() {

            }

            @Override
            public void onSuccess(String jsonResponse) {

                netRequestIntf.onSuccess(jsonResponse);
            }
        });

    }
}
