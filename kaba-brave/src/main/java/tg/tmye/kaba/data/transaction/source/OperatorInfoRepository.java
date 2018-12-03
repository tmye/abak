package tg.tmye.kaba.data.transaction.source;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 19/09/2018.
 * email: 2597434002@qq.com
 */
public class OperatorInfoRepository {

    private final Context context;

    NetworkRequestThreadBase networkRequestThreadBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;

    public OperatorInfoRepository (Context context) {
        this.context = context;
        /* get the threads */
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
    }

    public void checkAvailableOperators (final NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        /**/
        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();
        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_GET_TOPUP_CHOICES, "", token, netRequestIntf);
    }

    public void launchFloozTopUp(String phone_number, String amount, NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();
        /*
        *
            * {
	"phone_number": string
	"amount": string
            }
        * */
        JSONObject params = new JSONObject();
        try {
            params.put("phone_number", phone_number);
            params.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_TOPUP_FLOOZ, params.toString(), token, netRequestIntf);
    }

    public void launchTMoneyTopUp(String phone_number, String amount, NetworkRequestThreadBase.AuthNetRequestIntf<String> authNetRequestIntf) {

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

          /*
        *
            * {
	"phone_number": string
	"amount": string
            }
        * */
 JSONObject params = new JSONObject();
        try {
            params.put("phone_number", phone_number);
            params.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_TOPUP_TMONEY, params.toString(), token, authNetRequestIntf);

    }
}
