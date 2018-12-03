package tg.tmye.kaba.activity.command.contract;

import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 05/07/2018.
 * email: 2597434002@qq.com
 */
public interface CommandHistoryContract {

    /* view */
    public interface View extends AuthBaseView<Presenter> {

        void inflateList (List<Command> commandList);

        void onNetworkError();

        void onSysEror();

        void showLoading(boolean isLoading);
    }

    /* presenter */
    public interface Presenter extends AuthBasePresenter {

        /* load the newest 20 */
        void loadCommand();

        /* load twenty from -- */
        void loadCommandFrom (int from);

    }


}
