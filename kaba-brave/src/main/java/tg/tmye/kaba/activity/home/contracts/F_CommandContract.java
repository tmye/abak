package tg.tmye.kaba.activity.home.contracts;


import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 18/02/2018.
 * email: 2597434002@qq.com
 */

public interface F_CommandContract {


    interface View extends AuthBaseView<Presenter> {

        void networkError();

        void showIsLoading(boolean isLoading);

        void inflateCommandList(List<Command> data);

        void showErrorPage(boolean isShowed);

    }

    interface Presenter extends AuthBasePresenter {
          void update();
    }
}
