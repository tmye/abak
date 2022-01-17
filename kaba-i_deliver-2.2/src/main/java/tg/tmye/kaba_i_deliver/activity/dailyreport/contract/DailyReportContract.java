package tg.tmye.kaba_i_deliver.activity.dailyreport.contract;

import java.util.List;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.activity.delivery.contract.DeliveryModeContract;
import tg.tmye.kaba_i_deliver.data.command.Command;

/**
 * By abiguime on 2021/12/1.
 * email: 2597434002@qq.com
 */
public interface DailyReportContract {

    interface Presenter extends BasePresenter {

        void uploadDailyReport(int id, int fuel, int credit, int reparation, int losses, int parking, int various);
    }

    interface View extends BaseView<tg.tmye.kaba_i_deliver.activity.delivery.contract.DeliveryModeContract.Presenter> {

        void networkError();

        void sysError();

        void showLoading(boolean isLoading);

        void dailyReportSuccess(boolean isSuccessful);

    }

}
