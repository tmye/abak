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
        view.showIsLoading(true);
        menuDb_onlineRepository.loadAllSubMenusOfRestaurant(new NetworkRequestThreadBase.NetRequestIntf<List<Restaurant_SubMenuEntity>>() {
            @Override
            public void onNetworkError() {
                view.showIsLoading(false);
            }

            @Override
            public void onSysError() {
                view.showIsLoading(false);
            }

            @Override
            public void onSuccess(List<Restaurant_SubMenuEntity> menu_food) {

                if (menu_food == null || menu_food.size() == 0) {
                    /* show error message*/
                    view.showNoDataMessage();
                } else {
                    view.inflateMenus(menu_food);
                    view.showIsLoading(false);
                }
            }

        });

        /* then show it in the current interface */
    }
}
