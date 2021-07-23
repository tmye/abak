package tg.tmye.kaba_i_deliver.activity.command.contract;

import java.util.List;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.hsn.HSN;


/**
 * By abiguime on 23/05/2018.
 * email: 2597434002@qq.com
 */

public interface MyCommandContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isLoading);

        /* different list of commands */
        void inflateCommands(List<Command> preorders, List<HSN> hsnList, List<Command> wait_for_me, List<Command> yet_to_ship, List<Command> shipping_done);

        void exitDeliveryModeSuccess(boolean state, boolean wantToLogout);
    }

    interface Presenter extends BasePresenter {

        /* update commands data basically */
        void loadActualCommandsList();

        void stopDeliveryService(boolean wantToLogout);
    }

}
