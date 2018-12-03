package tg.tmye.kaba.activity.ad_categories.best_seller.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.ad_categories.best_seller.contract.BestSellerContract;
import tg.tmye.kaba.data.bestsellers.BestSeller;
import tg.tmye.kaba.data.bestsellers.source.BestSellerRepository;
import tg.tmye.kaba.data.command.Command;

/**
 * By abiguime on 09/07/2018.
 * email: 2597434002@qq.com
 */
public class BestSellerPresenter implements BestSellerContract.Presenter {


    private final BestSellerRepository repository;
    private final BestSellerContract.View view;
    private Gson gson = new Gson();

    public BestSellerPresenter (BestSellerContract.View view, BestSellerRepository repository) {

        this.view = view;
        this.repository = repository;
    }


    @Override
    public void loadBestSellers() {

        view.showLoading (true);
        repository.getLastBestSellers(new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.onNetworkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);
                /* get a list of best sellers */
                try {

                    List<BestSeller> bestSellerList= null;

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();

                    // get daily restaurants
                    BestSeller[] bestSellers =
                            gson.fromJson(obj.get("data"), new TypeToken<BestSeller[]>() {
                            }.getType());
                    bestSellerList = Arrays.asList(bestSellers);

                    view.inflateBestSellers(bestSellerList);

                } catch (Exception e) {
                    e.printStackTrace();
                    view.onSysError();
                }
            }
        });
    }

    @Override
    public void start() {
        loadBestSellers();
    }
}
