package tg.tmye.kaba.data.bestsellers.source;

import android.content.Context;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 06/07/2018.
 * email: 2597434002@qq.com
 */
public class BestSellerRepository {

    private final Context context;
    private final NetworkRequestThreadBase networkRequestBase;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;

    public BestSellerRepository (Context context) {

        this.context = context;
        /* get threads */
        networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyKabaApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }

    public void getLastBestSellers (NetworkRequestThreadBase.NetRequestIntf netRequestIntf) {

        /*  */
        networkRequestBase.postJsonData(Config.LINK_GET_BESTSELLERS_LIST, "", netRequestIntf);
    }


}
