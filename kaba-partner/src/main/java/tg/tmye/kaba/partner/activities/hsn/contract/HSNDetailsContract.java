package tg.tmye.kaba.partner.activities.hsn.contract;

import java.util.List;

import tg.tmye.kaba.partner.BasePresenter;
import tg.tmye.kaba.partner.BaseView;
import tg.tmye.kaba.partner.data.hsn.HSN;


/**
 * By abiguime on 23/05/2018.
 * email: 2597434002@qq.com
 */

public interface HSNDetailsContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isLoading);

        void sysError();

        void networkError();

        void cancelHsnSuccessful(boolean isSuccessfull);
    }


    interface Presenter extends BasePresenter {

        void cancelHSN(int hsn_id);
    }

}
