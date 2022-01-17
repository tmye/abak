package tg.tmye.kaba.partner.activities.stats.contract;

import java.util.List;

import tg.tmye.kaba.partner.BasePresenter;
import tg.tmye.kaba.partner.BaseView;
import tg.tmye.kaba.partner.data._OtherEntities.StatsEntity;


/**
 * By abiguime on 20/04/2018.
 * email: 2597434002@qq.com
 */

public interface RestaurantStatsContract {

    public interface View extends BaseView {

        void showLoading(boolean isLoading);

        /* in case there is a network error */
        void networkError();

        void syserror();

        void inflateStats(List<StatsEntity> statsEntities);
    }


    public interface Presenter extends BasePresenter {

        void load7LastDaysStats();

        void searchStaticsFromToDate(String fromDate, String toDate);
    }

}
