package tg.tmye.kaba.activity.favorite.presenter;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.favorite.contract.FavoriteContract;
import tg.tmye.kaba.data.favorite.Favorite;
import tg.tmye.kaba.data.favorite.source.FavoriteRepository;

/**
 * By abiguime on 27/02/2018.
 * email: 2597434002@qq.com
 */

public class FavoritePresenter implements FavoriteContract.Presenter {


    private final FavoriteContract.View favoriteView;
    private final FavoriteRepository favoriteRepository;

    public FavoritePresenter (FavoriteRepository favoriteRepository, FavoriteContract.View favoriteView) {

        this.favoriteRepository = favoriteRepository;
        this.favoriteView = favoriteView;
    }


    @Override
    public void update() {
        populateViews();
    }

    private void populateViews() {

        favoriteView.showIsLoading(true);
        favoriteRepository.onlineFavoriteLoad(new YesOrNoWithResponse(){

            @Override
            public void yes(Object data, boolean isFromOnline) {
                /* load favorite list from online -
                *   * if online is not accessible, show local, and tell the customer
                **/
                List<Favorite> favoriteList = (ArrayList<Favorite>) data;
                favoriteView.showIsLoading(false);
                favoriteView.inflateFavoriteList(favoriteList);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {

                if (isFromOnline)
                    favoriteRepository.offlineFavoriteLoad(this);
                else {
                    favoriteView.networkError();
                    favoriteView.showIsLoading(false);
                }
            }
        });
    }



    @Override
    public void start() {
        populateViews();
    }
}
