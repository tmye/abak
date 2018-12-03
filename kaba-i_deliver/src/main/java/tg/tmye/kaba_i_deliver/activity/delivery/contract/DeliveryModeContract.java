package tg.tmye.kaba_i_deliver.activity.delivery.contract;

import java.util.List;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.activity.login.contract.LoginContract;
import tg.tmye.kaba_i_deliver.data.command.Command;

/**
 * By abiguime on 04/06/2018.
 * email: 2597434002@qq.com
 */

public interface DeliveryModeContract {

    interface Presenter extends BasePresenter {

        void loadDistanceOptimizedDeliverList();
    }

    interface View extends BaseView<DeliveryModeContract.Presenter> {

        void networkError();

        void sysError();

        void showLoading(boolean isLoading);

        void inflateCommandList (List<Command> commandList);
    }

}
