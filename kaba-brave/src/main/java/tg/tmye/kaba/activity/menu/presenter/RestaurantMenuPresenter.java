package tg.tmye.kaba.activity.menu.presenter;

import java.util.List;
import java.util.Map;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.menu.contract.RestaurantMenuContract;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data.Menu.source.MenuDb_OnlineRepository;

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
        menuDb_onlineRepository.loadAllSubMenusOfRestaurant(new NetworkRequestThreadBase.NetRequestIntf<Map<Restaurant_SubMenuEntity, List<Restaurant_Menu_FoodEntity>>>() {
            @Override
            public void onNetworkError() {}

            @Override
            public void onSysError() {}

            @Override
            public void onSuccess(Map<Restaurant_SubMenuEntity, List<Restaurant_Menu_FoodEntity>> menu_food_list) {
                view.inflateMenus(menu_food_list);
            }

        });

        /* then show it in the current interface */
    }
}
