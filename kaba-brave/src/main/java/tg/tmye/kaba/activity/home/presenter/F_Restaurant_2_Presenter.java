package tg.tmye.kaba.activity.home.presenter;

import java.util.List;

import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.home.contracts.F_RestaurantContract;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.source.RestaurantDbRepository;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class F_Restaurant_2_Presenter implements F_RestaurantContract.Presenter {


    private final RestaurantDbRepository restaurantDbRepository;
    private final F_RestaurantContract.View restaurant2View;


    public F_Restaurant_2_Presenter(RestaurantDbRepository restaurantDbRepository,
                                    F_RestaurantContract.View restaurant2View) {

        this.restaurantDbRepository = restaurantDbRepository;
        this.restaurant2View = restaurant2View;
        this.restaurant2View.setPresenter(this);
    }


    @Override
    public void start() {

        populateViews();
    }

    private void populateViews() {

        /* populate restaurant list */
        restaurantDbRepository.loadRestaurantList(new YesOrNoWithResponse(){
            @Override
            public void yes(Object data, boolean isFromOnline) {
                restaurant2View.inflateRestaurantList((List<RestaurantEntity>) data);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {

            }
        });
    }


}
