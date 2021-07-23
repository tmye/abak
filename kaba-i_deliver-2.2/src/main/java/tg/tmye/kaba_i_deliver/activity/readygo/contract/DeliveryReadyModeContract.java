package tg.tmye.kaba_i_deliver.activity.readygo.contract;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;

/**
 * By abiguime on 2021/7/23.
 * email: 2597434002@qq.com
 */


public interface DeliveryReadyModeContract {

    public interface View extends BaseView {

        void showLoading(boolean isLoading);
        /* in case there is a network error */
        void networkError();

        void syserror();

        void enterDeliveryModeSuccess(boolean isSuccessfull);
        /* inflate data */
    }


    public interface Presenter extends BasePresenter {

        void startDeliveryMode();
    }

}
