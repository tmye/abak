package tg.tmye.kaba.restaurant.activities.menu.presenter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import tg.tmye.kaba.restaurant.ILog;
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
        menuDb_onlineRepository.loadAllSubMenusOfRestaurantForEdit(new NetworkRequestThreadBase.NetRequestIntf<Object>() {
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


    @Override
    public void populateFoodFromMenudId(int menu_id){

        view.showIsLoading(true);
        menuDb_onlineRepository.loadAllFoodsFromMenuForEdit(menu_id, new NetworkRequestThreadBase.NetRequestIntf<List<Restaurant_Menu_FoodEntity>>() {

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
            public void onSuccess(List<Restaurant_Menu_FoodEntity> menu_food) {

                // send
                if (menu_food == null || menu_food.size() == 0) {
                    /* show error message*/
                    view.showNoDataMessage();
                } else {
                    view.inflateFoods(menuDb_onlineRepository.restaurantEntity, menu_food);
                    view.showIsLoading(false);
                }
            }
        });
    }

    @Override
    public void hideFood(int food_id) {
        view.showIsLoading(true);
        menuDb_onlineRepository.hideFood(food_id, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
            public void onSuccess(String response) {
                /* send something around here */

                view.showIsLoading(false);
                try {
                    JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();

                    // get the error object and make sure it's == 0
                    int error = data.get("error").getAsInt();

                    if (error == 0)
                        view.foodHiddenSuccess();
                    else
                        view.foodHiddenError();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
    });
    }

    @Override
    public void deleteFood(int food_id) {
        view.showIsLoading(true);
        menuDb_onlineRepository.deleteFood(food_id, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
            public void onSuccess(String response) {
                /* send something around here */

                view.showIsLoading(false);
                try {
                    ILog.print(response);
                    JsonObject obj = new JsonParser().parse(response).getAsJsonObject();

                    // get the error object and make sure it's == 0
                    int error = obj.get("error").getAsInt();

                    if (error == 0)
                        view.foodDeletedSuccess();
                    else
                        view.foodDeletedError();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void hideMenu(int menu_id) {
        menuDb_onlineRepository.hideMenu(menu_id, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
            public void onSuccess(String response) {
                /* send something around here */

                view.showIsLoading(false);
                try {
                    ILog.print(response);
                    JsonObject obj = new JsonParser().parse(response).getAsJsonObject();

                    // get the error object and make sure it's == 0
                    int error = obj.get("error").getAsInt();

                    if (error == 0)
                        view.foodHiddenSuccess();
                    else
                        view.foodHiddenError();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void deleteMenu(int menu_id) {
        menuDb_onlineRepository.deleteMenu(menu_id, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
            public void onSuccess(String response) {
                /* send something around here */

                view.showIsLoading(false);
                try {
                    JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();

                    // get the error object and make sure it's == 0
                    int error = data.get("error").getAsInt();

                    if (error == 0)
                        view.menuDeletedSuccess();
                    else
                        view.menuDeletedError();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}
