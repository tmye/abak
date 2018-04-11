package tg.tmye.kaba.activity.menu.contract;

import java.util.List;
import java.util.Map;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;

/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public interface RestaurantMenuContract {

    public interface View extends BaseView<Presenter> {

        public void inflateMenus(List<Restaurant_SubMenuEntity> menu_food);
        public void showIsLoading (boolean isLoading);

        void showNoDataMessage();
    }

    public interface Presenter extends BasePresenter {

        void populateViews ();
    }

}
