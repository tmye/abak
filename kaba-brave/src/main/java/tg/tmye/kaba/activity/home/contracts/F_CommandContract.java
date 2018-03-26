package tg.tmye.kaba.activity.home.contracts;


import java.util.HashMap;
import java.util.List;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.command.Command;

/**
 * By abiguime on 18/02/2018.
 * email: 2597434002@qq.com
 */

public interface F_CommandContract {


    interface View extends BaseView<Presenter> {

        void networkError();

        void showIsLoading(boolean isLoading);

        void inflateCommandList(List<Command> data);

        void showErrorPage(boolean isShowed);
    }

    interface Presenter extends BasePresenter {
          void update();
    }
}
