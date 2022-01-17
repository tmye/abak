package tg.tmye.kaba.partner.activities.hsn.contract;

import java.util.List;

import tg.tmye.kaba.partner.BasePresenter;
import tg.tmye.kaba.partner.BaseView;
import tg.tmye.kaba.partner.activities.commands.contract.MyCommandContract;
import tg.tmye.kaba.partner.data.command.Command;
import tg.tmye.kaba.partner.data.district.DeliveryDistrict;

/**
 * By abiguime on 2021/9/15.
 * email: 2597434002@qq.com
 */
public interface HSNContract {

    interface View extends BaseView<MyCommandContract.Presenter> {

        void showLoading(boolean isLoading);

        void sysError();

        void networkError();

        void inflateDeliveryDistricts(List<DeliveryDistrict> districts);

        void inflateDeliveryPrice(DeliveryDistrict deliveryDistrict, String deliveryFees);

        void hsnCreateState(boolean isCreatedSuccessfully);

        void inflateDeliveryDistrictsError();

        void inflateDeliveryPriceError();

        void outOfRange();
    }


    interface Presenter extends BasePresenter {

        /* update commands data basically */
        void loadDeliveryDistricts ();

        /* load stats */
        void computeDeliveryBilling (DeliveryDistrict deliveryDistrict);

        public void createHSN(String district_id, String phone_number, String food_price, String more_informations);


    }

}
