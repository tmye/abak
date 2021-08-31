package tg.tmye.kaba_i_deliver.activity.statistics.contract;

import java.util.List;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.data.stats.StatisticResults;

/**
 * By abiguime on 2021/7/26.
 * email: 2597434002@qq.com
 */
public interface StatisticsContract {


    public interface View extends BaseView {

        void showLoading(boolean isLoading);

        /* in case there is a network error */
        void networkError();

        void syserror();

        void showStatisticsResult(List<StatisticResults> data);
        /* inflate data */
    }


    public interface Presenter extends BasePresenter {

        void searchStaticsFromToDate(String fromDate, String toDate);
    }

}