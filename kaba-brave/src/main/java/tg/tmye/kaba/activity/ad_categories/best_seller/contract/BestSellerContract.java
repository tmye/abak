package tg.tmye.kaba.activity.ad_categories.best_seller.contract;

import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.bestsellers.BestSeller;

/**
 * By abiguime on 05/07/2018.
 * email: 2597434002@qq.com
 */
public class BestSellerContract {

    public interface View extends BaseView<BasePresenter> {

        /* show the list of best sellers. with statistics during the 3 past days each. no details. */
        void inflateBestSellers (List<BestSeller> bestSellers);

        void onNetworkError();

        void onSysError();

        void showLoading(boolean isLoading);
    }

    public interface Presenter extends BasePresenter {

        /* load 7 best sellers. */
        void loadBestSellers();

        /*  */

    }

}
