package tg.tmye.kaba_i_deliver.activity.command.contract;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.delivery.DeliveryAddress;


/**
 * By abiguime on 20/04/2018.
 * email: 2597434002@qq.com
 */

public interface CommandDetailsContract {

    public interface View extends BaseView {

        void showLoading(boolean isLoading);
        /* in case there is a network error */
        void networkError();

        void inflateCommandDetails(Command command, DeliveryAddress deliveryAddress);

        void syserror();

        void success(boolean succesfull, int newState);

        void setShippingDoneSuccess(boolean isSuccess);

        void startShippingSuccess(boolean isSuccessfull);
        /* inflate data */
    }


    public interface Presenter extends BasePresenter {

        void loadCommandItems();

        void loadCommandDetails(String command_id);

        void launchRefund(int command_id, int orderAmount, int givenAmount, int leftAmount);
    }

}
