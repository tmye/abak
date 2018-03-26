package tg.tmye.kaba.activity.favorite.contract;

import java.util.List;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.activity.home.contracts.F_CommandContract;
import tg.tmye.kaba.data.favorite.Favorite;

/**
 * By abiguime on 27/02/2018.
 * email: 2597434002@qq.com
 */

public class FavoriteContract {

    public interface View extends BaseView<F_CommandContract.Presenter> {

        void networkError();

        void showIsLoading(boolean isLoading);

        void inflateFavoriteList(List<Favorite> data);

        void showErrorPage(boolean isShowed);
    }

    public interface Presenter extends BasePresenter {
        void update();
    }

}
