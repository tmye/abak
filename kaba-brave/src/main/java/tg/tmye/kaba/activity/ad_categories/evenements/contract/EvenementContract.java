package tg.tmye.kaba.activity.ad_categories.evenements.contract;

import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.evenement.Evenement;

/**
 * By abiguime on 05/07/2018.
 * email: 2597434002@qq.com
 */
public class EvenementContract {

    public interface View extends BaseView<BasePresenter> {

        /* show the list of best sellers. with statistics during the 3 past days each. no details. */
        void inflateEvenements(List<Evenement> evenements);

        void onNetworkError();

        void onSysError();

        void showLoading(boolean isLoading);

        void onEventClicked(Evenement evenement);
    }

    public interface Presenter extends BasePresenter {

        /* load evenements. */
        void loadEvenements();

        /*  */

    }

}
