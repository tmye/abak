package tg.tmye.kaba.activity.UserAcc.cash_transaction.contract;

import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 19/09/2018.
 * email: 2597434002@qq.com
 */
public interface TopUpContract {

    public interface View extends AuthBaseView<SoldeContract.Presenter> {

        /* */

        void onSysError();

        void onNetworkError();

        void showLoading(boolean isLoading);

        void mToast(String jsonResponse);

        void onTopUpLaunchSuccess(String link);

        void launchTMoneyTopUp(String phone_number, String amount);

        void onTopUpLaunchSuccess();
    }

    public interface Presenter extends AuthBasePresenter {

//        void checkSolde();

//        void checkHistoric();

//        void checkAvailableOperator();

        void launchTMoneyTopUp(String phone_number, String amount);

        void launchFloozTopUp(String phone_number, String amount);
    }


}
