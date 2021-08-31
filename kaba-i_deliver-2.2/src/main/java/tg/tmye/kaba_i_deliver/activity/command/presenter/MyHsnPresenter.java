package tg.tmye.kaba_i_deliver.activity.command.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.command.contract.HsnDetailsContract;
import tg.tmye.kaba_i_deliver.activity.command.contract.MyCommandContract;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.data.hsn.HSN;
import tg.tmye.kaba_i_deliver.syscore.ILog;

/**
 * By abiguime on 2021/7/21.
 * email: 2597434002@qq.com
 */
public class MyHsnPresenter implements HsnDetailsContract.Presenter {

    private final CommandRepository commandRepository;
    private final HsnDetailsContract.View view;
    private Gson gson = new Gson();

    public boolean isLoading = false;

    /* repository, and view */
   public MyHsnPresenter (HsnDetailsContract.View view, CommandRepository commandRepository) {

        this.view = view;
        this.commandRepository = commandRepository;
    }

    @Override
    public void setHsnDelivered(HSN hsn) {

        if (isLoading)
            return;
        isLoading = true;

        /* get the informations of the restaurant, and retrieve the list of commands that have to
         * be retrieven */
        view.showLoading(true);

        commandRepository.setHsnDeliveredSuccess(hsn.id, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.showLoading(false);
                isLoading = false;
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                isLoading = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {

                isLoading = false;
                ILog.print(jsonResponse);
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
//                    JsonObject data = obj.get("data").getAsJsonObject();
int error = obj.get("error").getAsInt();
if (error == 0) {
    view.setShippingDoneSuccess(true);
} else {
    view.setShippingDoneSuccess(false);
}

                } catch (Exception e) {
                    e.printStackTrace();
                    view.setShippingDoneSuccess(false);
                }
                view.showLoading(false);
            }
        });
    }

    @Override
    public void start() {

    }
}
