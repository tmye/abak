package tg.tmye.kaba.activity.UserAcc.service_client.contract;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 05/07/2018.
 * email: 2597434002@qq.com
 */
public interface ServiceClientContract {

    interface View extends AuthBaseView<Presenter> {

        /* is data successfully posted ? */
        void isSuccess (boolean isSuccess);

        void showLoading(boolean isLoading);
    }

    interface Presenter extends AuthBasePresenter {

        /* post data online */
        void postSuggestion (String message);
    }

}
