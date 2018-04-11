package tg.tmye.kaba.activity.UserAuth.register.contract;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;

/**
 * By abiguime on 13/03/2018.
 * email: 2597434002@qq.com
 */

public class RegisterContract {

    public interface View extends BaseView<RegisterContract.Presenter> {

        void showLoading(boolean isLoading);
        void registerSuccess();
        void registerFailure(String message);

        void toast(String message);
    }


    public interface Presenter extends BasePresenter {

        //        void connect (Customer customer, String passwd, String phonecode);
        void register(String phone_number, String password, String username);
    }

}
