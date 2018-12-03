package tg.tmye.kaba.activity.UserAcc.cash_transaction.contract;

import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.transaction.Transaction;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 17/07/2018.
 * email: 2597434002@qq.com
 */
public interface TransactionContract {

    public interface View extends AuthBaseView<Presenter> {

        /* */
        void inflateTransactions (List<Transaction> transactions);

        void onSysError();

        void onNetworkError();

        void showLoading(boolean isLoading);
    }

    public interface Presenter extends AuthBasePresenter {

        void loadTransactionHistoric();


    }
}
