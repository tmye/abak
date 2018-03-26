package tg.tmye.kaba.activity.home.presenter;

import java.util.List;

import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.home.contracts.F_HomeContract;
import tg.tmye.kaba.activity.home.contracts.F_RestaurantContract;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.source.RestaurantRepository;
import tg.tmye.kaba.data.advert.source.AdvertRepository;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class F_Restaurant_2_Presenter implements F_RestaurantContract.Presenter {


    private final RestaurantRepository restaurantRepository;
    private final F_RestaurantContract.View restaurant2View;


    public F_Restaurant_2_Presenter(RestaurantRepository restaurantRepository,
                                    F_RestaurantContract.View restaurant2View) {

        this.restaurantRepository = restaurantRepository;
        this.restaurant2View = restaurant2View;
        this.restaurant2View.setPresenter(this);
    }


    @Override
    public void start() {

        populateViews();
    }

    private void populateViews() {

        /* populate restaurant list */
        restaurantRepository.loadRestaurantList(new YesOrNoWithResponse(){
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
