package tg.tmye.kaba.restaurant.activities.calendar.contract;

import java.util.List;

import tg.tmye.kaba.restaurant.BasePresenter;
import tg.tmye.kaba.restaurant.BaseView;
import tg.tmye.kaba.restaurant.data.calendar.CalendarModel;
import tg.tmye.kaba.restaurant.data.command.Command;
import tg.tmye.kaba.restaurant.data.delivery.DeliveryAddress;
import tg.tmye.kaba.restaurant.data.delivery.KabaShippingMan;

/**
 * By abiguime on 2021/9/28.
 * email: 2597434002@qq.com
 */

public interface CalendarContract {

    public interface View extends BaseView {

        void showLoading(boolean isLoading);
        /* in case there is a network error */
        void networkError();

        void syserror();

        void modificationSuccess(boolean succesfull);

        void inflateCalendar(CalendarModel[] calendar);
        /* inflate data */
    }


    public interface Presenter extends BasePresenter {

        void loadCalendar();

        void updateCalendar (CalendarModel[] models);
    }

}
