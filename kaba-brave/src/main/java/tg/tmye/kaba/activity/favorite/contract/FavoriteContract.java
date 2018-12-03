package tg.tmye.kaba.activity.favorite.contract;

import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.favorite.Favorite;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 27/02/2018.
 * email: 2597434002@qq.com
 */

public interface FavoriteContract {

    public interface View extends AuthBaseView<Presenter> {

        void networkError();

        void showIsLoading(boolean isLoading);

        void inflateFavoriteList(List<Favorite> data);

        void showErrorPage(boolean isShowed);
    }

    public interface Presenter extends AuthBasePresenter {
        void update();
    }

}
