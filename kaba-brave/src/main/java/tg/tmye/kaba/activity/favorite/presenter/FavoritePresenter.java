package tg.tmye.kaba.activity.favorite.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.favorite.contract.FavoriteContract;
import tg.tmye.kaba.config.Constant;
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
    public void start() {
        populateViews();
    }

    private void populateViews() {

        favoriteView.showIsLoading(true);
        favoriteRepository.onlineFavoriteLoad(new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                favoriteView.networkError();
                favoriteView.showIsLoading(false);
            }

            @Override
            public void onSysError() {
                favoriteView.showIsLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d (Constant.APP_TAG, jsonResponse);

                Gson gson = new Gson();

                // turn result into a list of favorite objects
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                Favorite[] favorites =
                        gson.fromJson(data.get("favorites"), new TypeToken<Favorite[]>(){}.getType());
                List<Favorite> favoriteList = null;

                if (favorites != null) {
                    favoriteList = Arrays.asList(favorites);
                }

                favoriteView.inflateFavoriteList(favoriteList);
                favoriteView.showIsLoading(false);
            }

        });
    }

    @Override
    public void update() {
        populateViews();
    }
}
