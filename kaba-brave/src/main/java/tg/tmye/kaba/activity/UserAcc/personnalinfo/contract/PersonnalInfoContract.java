package tg.tmye.kaba.activity.UserAcc.personnalinfo.contract;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.customer.Customer;

/**
 * By abiguime on 03/03/2018.
 * email: 2597434002@qq.com
 */

public interface PersonnalInfoContract {

    public interface View extends BaseView<PersonnalInfoContract.Presenter> {

        void inflateCustomerData(Customer customer);
    }

    public interface Presenter extends BasePresenter {

    }
}
