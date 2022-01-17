package tg.tmye.kaba_i_deliver.activity.dailyreport.contract;

import java.util.List;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.activity.delivery.contract.DeliveryModeContract;
import tg.tmye.kaba_i_deliver.data.dailyreport.DailyReport;

/**
 * By abiguime on 2021/12/2.
 * email: 2597434002@qq.com
 */
public interface DailyReportHistoryContract {

    interface Presenter extends BasePresenter {

        void searchHistoryFromToDate(String fromDate, String toDate);
    }

    interface View extends BaseView<DeliveryModeContract.Presenter> {

        void networkError();

        void sysError();

        void showLoading(boolean isLoading);

         void showDailyReportHistorysResult(List<DailyReport> data);

    }

}