package tg.tmye.kaba.restaurant.activities.stats.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.activities.stats.contract.RestaurantStatsContract;
import tg.tmye.kaba.restaurant.data._OtherEntities.StatsEntity;
import tg.tmye.kaba.restaurant.data._OtherEntities.source.StatsRepository;
import tg.tmye.kaba.restaurant.syscore.Constant;

/**
 * By abiguime on 08/08/2018.
 * email: 2597434002@qq.com
 */
public class StatsPresenter implements RestaurantStatsContract.Presenter {


    private final StatsRepository repository;
    private final RestaurantStatsContract.View view;
    private Gson gson = new Gson();

    private boolean isLoading = false;

    public StatsPresenter (RestaurantStatsContract.View view, StatsRepository repository) {

        this.view = view;
        this.repository = repository;
    }

    @Override
    public void load7LastDaysStats() {

        if (isLoading == true)
            return;

        isLoading = true;
        view.showLoading(true);
        repository.load7LastDaysStats(new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                isLoading = false;
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                isLoading = false;
                view.showLoading(false);
                view.syserror();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);
                isLoading = false;
                /* parse the json obj */
                try {

                    Log.d(Constant.APP_TAG, jsonResponse);

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();

                    JsonObject data = obj.get("data").getAsJsonObject();

                    StatsEntity[] stats = gson.fromJson(data.get("stats"), new TypeToken<StatsEntity[]>() {}.getType());

                    List<StatsEntity> statsEntities = Arrays.asList(stats);

                    view.inflate7LastDaysStats(statsEntities);

                } catch (Exception e) {
                    e.printStackTrace();
                    view.syserror();
                }
            }
        });
    }

    @Override
    public void start() {
load7LastDaysStats();
    }


}
