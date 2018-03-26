package tg.experta.kaba.activities.Home.presenters;

import tg.experta.kaba.activities.Home.contracts.F_HomeContract;
import tg.experta.kaba.data.Feeds.Ad;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;
import tg.experta.kaba.data.Restaurant.source.RestaurantDbOnlineSource;
import tg.experta.kaba.data.Restaurant.source.RestaurantLocalDataSource;

/**
 * By abiguime on 19/02/2018.
 * email: 2597434002@qq.com
 */

public class F_HomePresenter implements F_HomeContract.Presenter {


    private final F_HomeContract.View f_Home_1_presenter;


    /* data source */
    private final RestaurantDbOnlineSource onlineSource;
    private final RestaurantLocalDataSource localDataSource;


    public F_HomePresenter (RestaurantLocalDataSource localDataSource,
                            RestaurantDbOnlineSource onlineSource,
                            F_HomeContract.View f_Home_1_presenter) {

        this.localDataSource = localDataSource;
        this.onlineSource = onlineSource;
        this.f_Home_1_presenter = f_Home_1_presenter;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadOnline() {

        /* i guess when the loading is done, we can notify the view */
    }

    @Override
    public void persistOnlineLocally() {

    }

    @Override
    public void loadFromLocal() {

    }

    @Override
    public void openRestaurant(RestaurantEntity restaurantEntity) {

    }

    @Override
    public void openAd(Ad ad) {

    }

    @Override
    public void openSearchVue(String trendRequest) {

    }


}
