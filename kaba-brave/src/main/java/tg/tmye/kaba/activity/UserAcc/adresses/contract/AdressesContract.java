package tg.tmye.kaba.activity.UserAcc.adresses.contract;

import java.util.List;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.delivery.DeliveryAddress;

/**
 * By abiguime on 06/04/2018.
 * email: 2597434002@qq.com
 */

public interface AdressesContract {

    public interface View extends BaseView<AdressesContract.Presenter> {

        void inflateAdresses(DeliveryAddress address);
        void inflateAdresses(List<DeliveryAddress> deliveryAddressList);
        void showLoading (boolean isLoading);

        void addressCreationSuccess();

        void addressCreationFailure();

        void showDeletingSuspendedLoadingBox();

        void addressDeletedFailure();

        void addressDeletedSuccess();
    }

    public interface Presenter extends BasePresenter {
        void populateViews();
        void uploadNewAdressToServer(DeliveryAddress adress);

        void presentAddress(DeliveryAddress address);

        void deleteAddress(DeliveryAddress address);
    }
}
