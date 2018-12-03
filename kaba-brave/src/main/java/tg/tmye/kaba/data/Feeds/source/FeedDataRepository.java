package tg.tmye.kaba.data.Feeds.source;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 26/06/2018.
 * email: 2597434002@qq.com
 */
public class FeedDataRepository {

    private final Context context;
    private final NetworkRequestThreadBase networkRequestBase;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;

    Gson gson = new Gson();

    public FeedDataRepository (Context context) {

        this.context = context;
        /* get threads */
        networkRequestBase = ((MyKabaApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyKabaApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }


    public void loadFeeds(NetworkRequestThreadBase.AuthNetRequestIntf<String> intf) {

        JSONObject args = new JSONObject();
        String authToken = ((MyKabaApp)context.getApplicationContext()).getAuthToken();
        networkRequestBase.getDataWithToken(Config.LINK_GET_LASTEST_FEEDS, null, authToken, intf);
    }

}
