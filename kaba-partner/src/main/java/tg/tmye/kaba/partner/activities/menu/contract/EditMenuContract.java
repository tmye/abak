package tg.tmye.kaba.partner.activities.menu.contract;

import java.util.List;

import tg.tmye.kaba.partner.BasePresenter;
import tg.tmye.kaba.partner.BaseView;
import tg.tmye.kaba.partner.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.partner.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.partner.data.Restaurant.RestaurantEntity;

//import tg.tmye.kaba.syscore.baseobj.BasePresenter;
//import tg.tmye.kaba.syscore.baseobj.BaseView;
//import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
//import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
//import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public interface EditMenuContract {

    public interface View extends BaseView<Presenter> {

        public void inflateMenus(RestaurantEntity entity, List<Restaurant_SubMenuEntity> menu_food, List<Restaurant_Menu_FoodEntity> drinks);

        public void showIsLoading(boolean isLoading);

        void showNoDataMessage();

        void onMenuInteraction(int position);

        void onSysError();

        void onNetworkError();

        void inflateFoods(RestaurantEntity restaurantEntity, List<Restaurant_Menu_FoodEntity> menu_food);

        void foodHiddenError();

        void foodHiddenSuccess();

        void menuDeletedSuccess();

        void menuDeletedError();

        void foodDeletedSuccess();

        void foodDeletedError();
    }

    public interface Presenter extends BasePresenter {

        void populateViews();

        void populateFoodFromMenudId(int menu_id);

        void hideFood(int food_id);

        void deleteFood(int food_id);

        void hideMenu(int menu_id);

        void deleteMenu(int menu_id);
    }

}
