package tg.tmye.kaba.activity.UserAcc.adresses.contract;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 06/04/2018.
 * email: 2597434002@qq.com
 */

public interface AdressesContract {

    public interface View extends AuthBaseView<Presenter> {

        void inflateAdresses(DeliveryAddress address);
        void inflateAdresses(List<DeliveryAddress> deliveryAddressList);
        void showLoading (boolean isLoading);

        void addressCreationSuccess();

        void addressCreationFailure();

        void addressDeletedFailure();

        void addressDeletedSuccess();

        void onAddressInteraction(DeliveryAddress address);

        void showCurrentAddressDetails(String quartier, String description_details);

        void onNetworkError();

        void onSysError();
    }

    public interface Presenter extends AuthBasePresenter {
        void populateViews();
        void uploadNewAdressToServer(DeliveryAddress adress);

        void presentAddress(DeliveryAddress address);

        void deleteAddress(DeliveryAddress address);

        void inflateLocation(LatLng location);
    }
}
