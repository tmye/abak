package tg.tmye.kaba.activity.UserAcc.cash_transaction.presenter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.contract.SoldeContract;
import tg.tmye.kaba.data.customer.source.SoldeRepository;

/**
 * By abiguime on 13/07/2018.
 * email: 2597434002@qq.com
 */
public class SoldePresenter implements SoldeContract.Presenter {

    private final SoldeRepository repository;
    private final SoldeContract.View view;

    public SoldePresenter (SoldeContract.View view, SoldeRepository repository) {

        this.view = view;
        this.repository = repository;
    }


    @Override
    public void checkSolde() {

        view.showLoading(true);
        repository.checkSolde (new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);

                try {

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    String balance = obj.get("data").getAsJsonObject().get("balance").getAsString();
                    view.inflateSolde(balance);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.onSysError();
                }

                /* "error": 0,
                           "message": null,
                           "data": {
                               "balance": string
                           }*/
            }

            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }
        });
    }

    @Override
    public void checkHistoric() {

    }

    @Override
    public void start() {

    }
}
