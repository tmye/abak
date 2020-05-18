package tg.tmye.kaba_i_deliver.activity.login.contract;


import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public interface LoginContract {

    interface Presenter extends BasePresenter {

        void login(String username, String password);
    }

    interface View extends BaseView<Presenter> {

        void loginSuccess(boolean isSuccess);

        void networkError();

        void showLoading(boolean isLoading);

        void sysError(int errorCode, String message);
    }
}
