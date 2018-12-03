package tg.tmye.kaba.activity.UserAcc.cash_transaction.contract;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 13/07/2018.
 * email: 2597434002@qq.com
 */
public interface SoldeContract {

    public interface View extends AuthBaseView<Presenter> {

        /* */
        void inflateSolde(String solde);

        void onSysError();

        void onNetworkError();

        void showLoading(boolean isLoading);
    }

    public interface Presenter extends AuthBasePresenter {

        void checkSolde();

        void checkHistoric();
    }

}
