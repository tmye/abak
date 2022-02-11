package tg.tmye.kaba_i_deliver.activity.dailyreport.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.dailyreport.contract.DailyReportContract;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;
import tg.tmye.kaba_i_deliver.syscore.ILog;

/**
 * By abiguime on 2021/12/1.
 * email: 2597434002@qq.com
 */
public class DailyReportPresenter implements DailyReportContract.Presenter {

    private DailyReportContract.View view;
    private DeliveryManRepository repository;
    private Gson gson = new Gson();

    private boolean isLoading = false;

    public DailyReportPresenter (DailyReportContract.View view, DeliveryManRepository repository) {

        this.view = view;
        this.repository = repository;
    }

    public void uploadDailyReport(int id, int fuel, int credit, int reparation, int losses, int parking, int various) {
        if (!isLoading) {

            view.showLoading(true);
            isLoading = true;

            repository.uploadDailyReport(id, fuel, credit, reparation, losses, parking, various, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
                        JsonObject data = obj.get("data").getAsJsonObject();
                        int id_point = data.get("id").getAsInt();
                        view.showLoading(false);
                        if (id_point > 0)
                            view.dailyReportSuccess(true);
                    } catch (Exception e) {
                        e.printStackTrace();
//                        view.inflateCommandList(null);
                        view.showLoading(false);
                        view.dailyReportSuccess(false);
                    }
                }
            });
        }
    }

    @Override
    public void start() {
    }

}
