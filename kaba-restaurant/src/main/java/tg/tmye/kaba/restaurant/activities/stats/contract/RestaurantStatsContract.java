package tg.tmye.kaba.restaurant.activities.stats.contract;

import java.util.List;

import tg.tmye.kaba.restaurant.BasePresenter;
import tg.tmye.kaba.restaurant.BaseView;
import tg.tmye.kaba.restaurant.data._OtherEntities.StatsEntity;


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

        void inflate7LastDaysStats(List<StatsEntity> statsEntities);
    }


    public interface Presenter extends BasePresenter {

        void load7LastDaysStats();
    }

}
