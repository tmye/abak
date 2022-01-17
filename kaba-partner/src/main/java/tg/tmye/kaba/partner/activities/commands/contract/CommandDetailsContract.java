package tg.tmye.kaba.partner.activities.commands.contract;

import java.util.List;

import tg.tmye.kaba.partner.BasePresenter;
import tg.tmye.kaba.partner.BaseView;
import tg.tmye.kaba.partner.data.command.Command;
import tg.tmye.kaba.partner.data.delivery.DeliveryAddress;
import tg.tmye.kaba.partner.data.delivery.KabaShippingMan;


/**
 * By abiguime on 20/04/2018.
 * email: 2597434002@qq.com
 */

public interface CommandDetailsContract {

    public interface View extends BaseView {

        void showLoading(boolean isLoading);
        /* in case there is a network error */
        void networkError();

        void inflateCommandDetails(Command command, List<KabaShippingMan> kabaShippingManList, DeliveryAddress deliveryAddress);

        void syserror();

        void success(boolean succesfull, int newState);
        /* inflate data */

        void cancelCommand (int motif);

        void onCancelSuccess();

    }


    public interface Presenter extends BasePresenter {

        void loadCommandItems();

        void loadCommandDetails(String command_id);

        void acceptCommand (Command command);

        void sendCommandToShipping(Command command, KabaShippingMan shippingMan);

        void cancelCommand (int command_id, int motif);
    }

}
