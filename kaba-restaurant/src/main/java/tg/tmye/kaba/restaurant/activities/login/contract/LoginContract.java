package tg.tmye.kaba.restaurant.activities.login.contract;


import tg.tmye.kaba.restaurant.BasePresenter;
import tg.tmye.kaba.restaurant.BaseView;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public interface LoginContract {

    interface Presenter extends BasePresenter {

        void login (String username, String password);
    }

    interface View extends BaseView<Presenter> {

        void loginSuccess (boolean isSuccess);

        void networkError();

        void sysError();

        void showLoading(boolean isLoading);
    }
}
