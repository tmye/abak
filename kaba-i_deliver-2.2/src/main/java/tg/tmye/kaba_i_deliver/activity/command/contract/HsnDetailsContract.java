package tg.tmye.kaba_i_deliver.activity.command.contract;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.delivery.DeliveryAddress;
import tg.tmye.kaba_i_deliver.data.hsn.HSN;


/**
 * By abiguime on 20/04/2018.
 * email: 2597434002@qq.com
 */

public interface HsnDetailsContract {

    public interface View extends BaseView {

        void showLoading(boolean isLoading);
        /* in case there is a network error */
        void networkError();

        void inflateHsnDetails();

        void success(boolean succesfull, int newState);

        void setShippingDoneSuccess(boolean isSuccess);

    }


    public interface Presenter extends BasePresenter {

        void setHsnDelivered(HSN hsn);

    }

}
