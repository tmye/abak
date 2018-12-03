package tg.tmye.kaba.activity.UserAcc.cash_transaction.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.contract.TransactionContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.transaction.Transaction;
import tg.tmye.kaba.data.transaction.source.TransactionRepository;

/**
 * By abiguime on 17/07/2018.
 * email: 2597434002@qq.com
 */
public class TransactionPresenter implements TransactionContract.Presenter {

    private final TransactionRepository repository;
    private final TransactionContract.View view;
    private boolean isLoading = false;
    private Gson gson = new Gson();

    public TransactionPresenter (TransactionContract.View view, TransactionRepository repository) {

        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadTransactionHistoric() {

        if (isLoading)
            return;

        view.showLoading(true);

        isLoading = true;

        repository.loadTransactionHistory (new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.onNetworkError();
                isLoading = false;
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.onSysError();
                isLoading = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {

                isLoading = false;

                view.showLoading(false);
                Log.d(Constant.APP_TAG, jsonResponse);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                Transaction[] transactions = gson.fromJson(obj.get("data"), new TypeToken<Transaction[]>() {}.getType());
                view.inflateTransactions(Arrays.asList(transactions));
            }

            @Override
            public void onLoggingTimeout() {
                view.showLoading(false);
                view.onLoggingTimeout();
            }
        });
    }

    @Override
    public void start() {

    }
}
