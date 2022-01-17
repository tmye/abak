package tg.tmye.kaba.partner.activities.commands.contract;

import java.util.List;

import tg.tmye.kaba.partner.BasePresenter;
import tg.tmye.kaba.partner.BaseView;
import tg.tmye.kaba.partner.data.command.Command;


/**
 * By abiguime on 23/05/2018.
 * email: 2597434002@qq.com
 */

public interface MyCommandContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isLoading);

        /* different list of commands */
        void inflateCommands (/*List<Command> preorder_command, */List<Command> waiting_command, List<Command> cooking_command,
                              List<Command> shipping_command, List<Command> delivered_commands, List<Command> others);

        void sysError();

        void networkError();

        void presenterSwitchOpened(boolean is_opened);
    }

    interface HomePageView extends BaseView<Presenter> {

        void showLoading(boolean isLoading);
        void inflateStats (int calendar, int manual_open_state, int coming_soon, String head_pic, String resto_name, String quantity_count, String amount_money);
        void sysError();
        void networkError();

        void inflateCountStats(int waiting_count, int shipping_count, int cooking_count);

        void presenterSwitchOpened(boolean is_opened);

        void updateHomepage();
    }

    interface Presenter extends BasePresenter {

        /* update commands data basically */
        void loadActualCommandsList ();

        /* load stats */
        void loadStats ();

        void checkOpened(boolean isOpened);
    }

}
