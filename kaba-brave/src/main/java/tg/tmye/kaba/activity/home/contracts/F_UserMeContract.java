package tg.tmye.kaba.activity.home.contracts;


import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.customer.Customer;

/**
 * By abiguime on 18/02/2018.
 * email: 2597434002@qq.com
 */

public interface F_UserMeContract {


    interface View extends BaseView<Presenter> {

        void inflateCustomerInfo(Customer data);

        void logout();

    }

    interface Presenter extends BasePresenter {

    }

}
