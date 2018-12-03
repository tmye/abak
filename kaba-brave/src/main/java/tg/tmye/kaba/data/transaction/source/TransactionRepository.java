package tg.tmye.kaba.data.transaction.source;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 17/07/2018.
 * email: 2597434002@qq.com
 */
public class TransactionRepository {

    private final Context context;

    NetworkRequestThreadBase networkRequestThreadBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;

    public TransactionRepository (Context context) {
        this.context = context;
        /* get the threads */
        this.databaseRequestThreadBase = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
        this.networkRequestThreadBase =  ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
    }

    public void loadTransactionHistory(final NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        /**/
        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_GET_TRANSACTION_HISTORY, "", token, netRequestIntf);

    }
}
