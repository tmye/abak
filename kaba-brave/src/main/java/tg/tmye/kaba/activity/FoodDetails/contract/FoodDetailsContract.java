package tg.tmye.kaba.activity.FoodDetails.contract;

import java.util.List;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.favorite.Favorite;

/**
 * By abiguime on 13/04/2018.
 * email: 2597434002@qq.com
 */

public interface FoodDetailsContract {

    public interface View extends BaseView<FoodDetailsContract.Presenter> {

        void networkError();

        void showErrorPage(boolean isShowed);

        void setFavorite (int isFavorite);
    }

    public interface Presenter extends BasePresenter {

         void setFavorite(Restaurant_Menu_FoodEntity foodEntity);
    }

}
