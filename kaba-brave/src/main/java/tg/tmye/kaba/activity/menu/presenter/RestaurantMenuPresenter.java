package tg.tmye.kaba.activity.menu.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.utils.SimpleObjectHolder;
import tg.tmye.kaba.activity.menu.contract.RestaurantMenuContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public class RestaurantMenuPresenter implements RestaurantMenuContract.Presenter {

    MenuDb_OnlineRepository menuDb_onlineRepository;
    RestaurantMenuContract.View view;


    public RestaurantMenuPresenter (MenuDb_OnlineRepository menuDb_onlineRepository,
                                    RestaurantMenuContract.View view) {

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


    public void loadAllDataFromMenuId () {

        menuDb_onlineRepository.loadAllDataFromMenuId(new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.onNetworkError();
            }

            @Override
            public void onSysError() {
                view.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);
                /* conver to json objects */

                RestaurantEntity restaurantEntity = new RestaurantEntity();
                List<Restaurant_SubMenuEntity> menuList = new ArrayList<>();

                view.allDataFromMenuIdSuccess(restaurantEntity, menuList);
            }
        });
    }
}
