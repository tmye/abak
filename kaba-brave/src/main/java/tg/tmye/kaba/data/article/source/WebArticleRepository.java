package tg.tmye.kaba.data.article.source;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 02/05/2018.
 * email: 2597434002@qq.com
 */

public class WebArticleRepository {

    public String TAG = "WebArticleRepository";

    private final Context context;
    private final DatabaseRequestThreadBase databaseRequestHandler;
    private final NetworkRequestThreadBase networkRequestHandler;

    private Gson gson = new Gson();

    public WebArticleRepository(Context context) {
        this.context = context;
        this.networkRequestHandler = ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
        this.databaseRequestHandler = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
    }


    public void loadArticle(int articleId, final NetworkRequestThreadBase.NetRequestIntf<String> intf) {



        networkRequestHandler.run(Config.LINK_ARTICLE_INFORMATIONS+"/"+articleId, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                Log.d(Constant.APP_TAG, "NetworkError");
                intf.onNetworkError();
            }

            @Override
            public void onSysError() {
                Log.d(Constant.APP_TAG, "onSysError");
                intf.onNetworkError();
            }

            @Override
            public void onSuccess(String jsonResponse) {
                Log.d(Constant.APP_TAG, "onSuccess");
                intf.onSuccess(jsonResponse);
            }
        });

    }


}
