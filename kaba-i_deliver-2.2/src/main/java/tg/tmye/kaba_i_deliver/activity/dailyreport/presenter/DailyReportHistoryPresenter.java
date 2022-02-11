package tg.tmye.kaba_i_deliver.activity.dailyreport.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.dailyreport.contract.DailyReportHistoryContract;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.dailyreport.DailyReport;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;
import tg.tmye.kaba_i_deliver.syscore.ILog;

/**
 * By abiguime on 2021/12/2.
 * email: 2597434002@qq.com
 */
public class DailyReportHistoryPresenter implements DailyReportHistoryContract.Presenter {

    private DailyReportHistoryContract.View view;
    private DeliveryManRepository repository;
    private Gson gson = new Gson();

    private boolean isLoading = false;

    public DailyReportHistoryPresenter (DailyReportHistoryContract.View view, DeliveryManRepository repository) {

        this.view = view;
        this.repository = repository;
    }

    @Override
    public void start() {
    }

    @Override
    public void searchHistoryFromToDate(String fromDate, String toDate) {
        if (!isLoading) {

            view.showLoading(true);
            isLoading = true;

            repository.searchHistoryFromToDate(fromDate, toDate, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
                    view.sysError();
                }

                @Override
                public void onSuccess(String jsonResponse) {

                    isLoading = false;

                    try {
                        ILog.print(jsonResponse);
                        /* deflate all elements and send the commands list to where it should */
                        JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
             //           JsonObject data = obj.get("data").getAsJsonObject();
                        DailyReport[] dailyReports =
                                gson.fromJson(obj.get("data").getAsJsonArray()/*.get("daily_report")*/, new TypeToken<DailyReport[]>() {
                                }.getType());
                        List<DailyReport> ddailyReport = new ArrayList<>(Arrays.asList(dailyReports));
                        Collections.reverse(ddailyReport); // reverse result
                        view.showDailyReportHistorysResult(ddailyReport);
                        view.showLoading(false);
                    } catch (Exception e) {
                        e.printStackTrace();
//                        view.inflateCommandList(null);
                        view.showLoading(false);
                    }
                }
            });
        }
    }
}
