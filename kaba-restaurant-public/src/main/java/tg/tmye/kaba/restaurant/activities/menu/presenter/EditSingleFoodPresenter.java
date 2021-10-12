package tg.tmye.kaba.restaurant.activities.menu.presenter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.activities.menu.contract.EditSingleFoodContract;
import tg.tmye.kaba.restaurant.activities.menu.contract.EditSingleFoodContract;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.Menu.source.MenuDb_OnlineRepository;

/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public class EditSingleFoodPresenter implements EditSingleFoodContract.Presenter {

    MenuDb_OnlineRepository menuDb_onlineRepository;
    EditSingleFoodContract.View view;


    public EditSingleFoodPresenter(MenuDb_OnlineRepository menuDb_onlineRepository,
                                   EditSingleFoodContract.View view) {

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
    }

    @Override
    public void updateFood(Restaurant_Menu_FoodEntity foodEntity) {

        view.showIsLoading(true);
        menuDb_onlineRepository.updateFoodEntity(foodEntity, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
//                    JsonObject data = obj.get("data").getAsJsonObject();p

                    // get the error object and make sure it's == 0
                    int error = obj.get("error").getAsInt();

                    if (error == 0)
                        view.foodUpdateSuccess();
                    else
                        view.foodUpdateError();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
