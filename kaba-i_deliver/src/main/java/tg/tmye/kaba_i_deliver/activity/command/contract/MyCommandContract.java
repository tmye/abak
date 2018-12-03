package tg.tmye.kaba_i_deliver.activity.command.contract;

import java.util.List;

import tg.tmye.kaba_i_deliver.BasePresenter;
import tg.tmye.kaba_i_deliver.BaseView;
import tg.tmye.kaba_i_deliver.data.command.Command;


/**
 * By abiguime on 23/05/2018.
 * email: 2597434002@qq.com
 */

public interface MyCommandContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isLoading);

        /* different list of commands */
        void inflateCommands(List<Command> yet_to_ship, List<Command> shipping_done);
    }

    interface Presenter extends BasePresenter {

        /* update commands data basically */
        void loadActualCommandsList();
    }

}
