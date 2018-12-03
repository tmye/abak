package tg.tmye.kaba.activity.ad_categories.evenements.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.ad_categories.evenements.contract.EvenementContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.bestsellers.BestSeller;
import tg.tmye.kaba.data.bestsellers.source.BestSellerRepository;
import tg.tmye.kaba.data.evenement.Evenement;
import tg.tmye.kaba.data.evenement.EvenementRepository;

/**
 * By abiguime on 09/07/2018.
 * email: 2597434002@qq.com
 */
public class EvenementPresenter implements EvenementContract.Presenter {


    private final EvenementRepository repository;
    private final EvenementContract.View view;
    private Gson gson = new Gson();

    public EvenementPresenter(EvenementContract.View view, EvenementRepository repository) {

        this.view = view;
        this.repository = repository;
    }


    @Override
    public void loadEvenements() {

        view.showLoading (true);
        repository.getLastEvenements(new NetworkRequestThreadBase.NetRequestIntf<String>() {

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

                Log.d(Constant.APP_TAG, jsonResponse);
                view.showLoading(false);
                /* get a list of best sellers */
                try {

                    List<Evenement> evenementList = null;

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();

                    // get daily restaurants
                    Evenement[] evenements =
                            gson.fromJson(obj.get("data"), new TypeToken<Evenement[]>() {
                            }.getType());
                    evenementList = Arrays.asList(evenements);

                    view.inflateEvenements(evenementList);

                } catch (Exception e) {
                    e.printStackTrace();
                    view.onSysError();
                }
            }
        });
    }

    @Override
    public void start() {
        loadEvenements();
    }
}
