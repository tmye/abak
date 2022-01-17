package tg.tmye.kaba.partner.activities.hsn.presenter;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.partner._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.partner.activities.hsn.contract.HSNDetailsContract;
import tg.tmye.kaba.partner.activities.hsn.contract.MyHSNContract;
import tg.tmye.kaba.partner.data.command.source.CommandRepository;
import tg.tmye.kaba.partner.data.hsn.HSN;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class HSNDetailsPresenter implements HSNDetailsContract.Presenter {

    private final CommandRepository commandRepository;
    private HSNDetailsContract.View view = null;
    private Gson gson = new Gson();

    boolean isLoading = false;

    /* repository, and view */
    public HSNDetailsPresenter(HSNDetailsContract.View view, CommandRepository commandRepository) {

        this.view = view;
        this.commandRepository = commandRepository;
    }


    @Override
    public void cancelHSN(int hsn_id) {
        if (isLoading)
            return;

        isLoading = true;
        view.showLoading(true);
        commandRepository.cancelHSN(hsn_id, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                isLoading = false;
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                isLoading = false;
                view.showLoading(false);
                view.sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading(false);
                isLoading = false;
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                   int error = obj.get("error").getAsInt();
            view.cancelHsnSuccessful(error == 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.sysError();
                }
            }
        });
    }

    @Override
    public void start() {

    }
}
