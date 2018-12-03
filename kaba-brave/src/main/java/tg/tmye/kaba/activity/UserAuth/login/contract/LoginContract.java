package tg.tmye.kaba.activity.UserAuth.login.contract;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;

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
