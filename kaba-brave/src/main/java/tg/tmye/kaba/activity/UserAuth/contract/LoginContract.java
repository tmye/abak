package tg.tmye.kaba.activity.UserAuth.contract;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.activity.home.contracts.F_HomeContract;
import tg.tmye.kaba.data.customer.Customer;

/**
 * By abiguime on 13/03/2018.
 * email: 2597434002@qq.com
 */

public class LoginContract {

    public interface View extends BaseView<LoginContract.Presenter> {

        void showLoading(boolean isLoading);
        void loginSuccess ();
        void loginFailure (String message);

        void toast(String message);
    }


    public interface Presenter extends BasePresenter {

        //        void connect (Customer customer, String passwd, String phonecode);
        void login (String password, String phoneCode);
    }

}
