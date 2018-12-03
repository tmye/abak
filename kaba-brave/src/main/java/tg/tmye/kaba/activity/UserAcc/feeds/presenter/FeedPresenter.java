package tg.tmye.kaba.activity.UserAcc.feeds.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.UserAcc.feeds.contract.FeedContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Feeds.Feeds;
import tg.tmye.kaba.data.Feeds.source.FeedDataRepository;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;

/**
 * By abiguime on 03/03/2018.
 * email: 2597434002@qq.com
 */

public class FeedPresenter implements FeedContract.Presenter {


    private final FeedDataRepository feedDataRepository;
    private final FeedContract.View view;


    private Gson gson = new Gson();

    //CustomerDataRepository

    public FeedPresenter(FeedDataRepository feedDataRepository, FeedContract.View view) {

        this.feedDataRepository = feedDataRepository;
        this.view =  view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        populateView();
    }

    private void populateView() {
        loadFeeds();
    }

    @Override
    public void loadFeeds() {

        view.showLoading(true);
        feedDataRepository.loadFeeds (new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                /*  */
                Log.d(Constant.APP_TAG, jsonResponse);
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();

                    List<Feeds> articles_list = new ArrayList<>();
                    List<Feeds> notifications_list = new ArrayList<>();

                    JsonObject data = obj.get("data").getAsJsonObject();
                    // get daily restaurants
                    Feeds[] article_feeds_array =
                            gson.fromJson(data.get("article_feeds"), new TypeToken<Feeds[]>() {
                            }.getType());

                    Feeds[] notification_feeds_array =
                            gson.fromJson(data.get("notification_feeds"), new TypeToken<Feeds[]>() {
                            }.getType());

                    articles_list = new ArrayList<>(Arrays.asList(article_feeds_array));
                    notifications_list = new ArrayList<>(Arrays.asList(notification_feeds_array));

                    view.inflateFeeds(articles_list, notifications_list);
                } catch (Exception e){
                    e.printStackTrace();
                    view.sysError();
                }
                view.showLoading(false);
            }

            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }
        });
    }
}
