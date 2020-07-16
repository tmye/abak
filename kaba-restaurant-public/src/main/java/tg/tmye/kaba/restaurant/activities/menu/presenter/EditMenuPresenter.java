package tg.tmye.kaba.restaurant.activities.menu.presenter;

import java.util.List;

import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant._commons.utils.SimpleObjectHolder;
import tg.tmye.kaba.restaurant.activities.menu.contract.EditMenuContract;
import tg.tmye.kaba.restaurant.activities.menu.contract.RestaurantMenuContract;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.Menu.source.MenuDb_OnlineRepository;

/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public class EditMenuPresenter implements EditMenuContract.Presenter {

    MenuDb_OnlineRepository menuDb_onlineRepository;
    EditMenuContract.View view;


    public EditMenuPresenter(MenuDb_OnlineRepository menuDb_onlineRepository,
                             EditMenuContract.View view) {

        /* view, datarepo */
        this.view = view;
        this.menuDb_onlineRepository = menuDb_onlineRepository;
    }

    @Override
    public void start() {

        populateViews();
    }

    @Override
    public void populateViews() {

        /* load restaurant menuz and food from locally or online -- */
        view.showIsLoading(true);
        menuDb_onlineRepository.loadAllSubMenusOfRestaurant(new NetworkRequestThreadBase.NetRequestIntf<Object>() {
            @Override
            public void onNetworkError() {
                view.showIsLoading(false);
                view.onNetworkError();
            }

            @Override
            public void onSysError() {
                view.showIsLoading(false);
                view.onSysError();
            }

            @Override
            public void onSuccess(Object result) {

                List<Restaurant_SubMenuEntity> menu_food = null;
                List<Restaurant_Menu_FoodEntity> drinks = null;
                int maxCount = 0;

                if (result instanceof SimpleObjectHolder) {

                    SimpleObjectHolder objectHolder = (SimpleObjectHolder) result;
                    menu_food = (List<Restaurant_SubMenuEntity>) objectHolder.arg1;
                    drinks = (List<Restaurant_Menu_FoodEntity>) objectHolder.arg2;
                } else if (result instanceof List) {
                    menu_food = (List<Restaurant_SubMenuEntity>) result;
                }

                if (menu_food == null || menu_food.size() == 0) {
                    /* show error message*/
                    view.showNoDataMessage();
                } else {
                    view.inflateMenus(menuDb_onlineRepository.restaurantEntity, menu_food, drinks);
                    view.showIsLoading(false);
                }
            }

        });

        /* then show it in the current interface */
    }

}
