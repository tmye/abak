package tg.tmye.kaba.activity.UserAcc.personnalinfo.contract;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 03/03/2018.
 * email: 2597434002@qq.com
 */

public interface PersonnalInfoContract {

    public interface View extends AuthBaseView<Presenter> {

        void inflateCustomerData(Customer customer);

        void showLoading(boolean isVisible);

        void finishActivity();

        void toast(int data_error);


    }

    public interface Presenter extends BasePresenter {

        void updateUserInformations(Customer customer);
    }
}
