package tg.tmye.kaba_i_deliver.activity.statistics.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.statistics.contract.StatisticsContract;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.data.stats.StatisticResults;
import tg.tmye.kaba_i_deliver.syscore.ILog;

/**
 * By abiguime on 2021/7/26.
 * email: 2597434002@qq.com
 */
public class StatisticPresenter implements StatisticsContract.Presenter {

    private StatisticsContract.View view;
    private CommandRepository repository;
    private Gson gson = new Gson();

    private boolean isLoading = false;

    public StatisticPresenter(StatisticsContract.View view, CommandRepository repository) {

        this.view = view;
        this.repository = repository;
    }


    @Override
    public void searchStaticsFromToDate(String fromDate, String toDate) {

        if (isLoading)
            return;
        isLoading = true;
        view.showLoading(true);

        repository.searchStaticsFromToDate(fromDate, toDate, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.showLoading(false);
                isLoading = false;
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                isLoading = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {
                isLoading = false;
                ILog.print(jsonResponse);
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();
                    int error = obj.get("error").getAsInt();
                    if (error == 0) {
                        StatisticResults[] results =
                                gson.fromJson(data.get("history"), new TypeToken<StatisticResults[]>() {
                                }.getType());

                        view.showStatisticsResult(reverseArray(new ArrayList<>(Arrays.asList(results))));
                    } else {
                        view.syserror();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.syserror();
                }
                view.showLoading(false);
            }
        });
    }

    private List<StatisticResults> reverseArray(ArrayList<StatisticResults> statisticResults) {
        List<StatisticResults> res = new ArrayList<>();
        for (int i = statisticResults.size()-1; i >= 0; i--) {
            res.add(statisticResults.get(i));
        }
        return res;
    }

    @Override
    public void start() {

    }
}