package tg.tmye.kaba.activity.WebArticle;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.WebArticle.contract.WebArticleContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.article.WebArticle;
import tg.tmye.kaba.data.article.source.WebArticleRepository;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.data.favorite.Favorite;

/**
 * By abiguime on 15/03/2018.
 * email: 2597434002@qq.com
 */

public class WebArticlePresenter implements WebArticleContract.Presenter {

    private final WebArticleRepository webArticleRepository;
    private final WebArticleContract.View articleView;


    public WebArticlePresenter (WebArticleContract.View articleView, WebArticleRepository webArticleRepository /* login repository */) {

        this.webArticleRepository = webArticleRepository;
        this.articleView = articleView;
    }


    @Override
    public void start() {

        /* populate views, but there is nothing to pop */
    }

    @Override
    public void loadArticle(final int articleId) {

        articleView.showLoading(true);
        webArticleRepository.loadArticle(articleId, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                articleView.showLoading(false);
            }

            @Override
            public void onSysError() {
                articleView.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);

                Gson gson = new Gson();

                // turn result into a list of favorite objects
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                WebArticle webArticle =
                        gson.fromJson(data, new TypeToken<WebArticle>(){}.getType());

                articleView.inflateArticle(webArticle);
//                articleView.showLoading(false);
            }
        });
    }

}
