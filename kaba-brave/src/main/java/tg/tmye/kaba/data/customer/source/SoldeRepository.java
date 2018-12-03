package tg.tmye.kaba.data.customer.source;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 13/07/2018.
 * email: 2597434002@qq.com
 */
public class SoldeRepository {

    public String TAG = "SoldeRepository";

    private final Context context;
    private final DatabaseRequestThreadBase databaseRequestHandler;
    private final NetworkRequestThreadBase networkRequestHandler;

    private Gson gson = new Gson();

    public SoldeRepository(Context context) {
        this.context = context;
        this.networkRequestHandler = ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
        this.databaseRequestHandler = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
    }

    public void checkSolde(NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        /* check my solde */
        String authToken = ((MyKabaApp) context.getApplicationContext()).getAuthToken();
        networkRequestHandler.getDataWithToken(Config.LINK_GET_BALANCE, null, authToken, netRequestIntf);
    }

    public void updateSolde (String solde) {
        /* open user shared preferences */
    }
}
