package tg.tmye.kaba.partner.activities.menu.contract;

import tg.tmye.kaba.partner.BasePresenter;
import tg.tmye.kaba.partner.BaseView;
import tg.tmye.kaba.partner.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.partner.data.Menu.Restaurant_SubMenuEntity;

//import tg.tmye.kaba.syscore.baseobj.BasePresenter;
//import tg.tmye.kaba.syscore.baseobj.BaseView;
//import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
//import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
//import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public interface EditSingleFoodContract {

    public interface View extends BaseView<Presenter> {

        public void showIsLoading(boolean isLoading);

        void onSysError();

        void onNetworkError();

        void foodUpdateSuccess();

        void foodUpdateError ();

    }

    public interface Presenter extends BasePresenter {

        void populateViews();

        // update menu
        void updateFood(Restaurant_Menu_FoodEntity foodEntity);

    }

}
