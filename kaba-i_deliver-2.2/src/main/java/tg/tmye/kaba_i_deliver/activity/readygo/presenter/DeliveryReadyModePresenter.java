package tg.tmye.kaba_i_deliver.activity.readygo.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.delivery.contract.DeliveryModeContract;
import tg.tmye.kaba_i_deliver.activity.readygo.contract.DeliveryReadyModeContract;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;
import tg.tmye.kaba_i_deliver.syscore.ILog;

/**
 * By abiguime on 2021/7/23.
 * email: 2597434002@qq.com
 */
public class DeliveryReadyModePresenter  implements DeliveryReadyModeContract.Presenter {


    private DeliveryReadyModeContract.View view;
    private CommandRepository repository;
    private Gson gson = new Gson();

    private boolean isLoading = false;

    public DeliveryReadyModePresenter(DeliveryReadyModeContract.View view, CommandRepository repository) {

        this.view = view;
        this.repository = repository;
    }

    @Override
    public void start() {

    }

    @Override
    public void startDeliveryMode() {


        if (isLoading)
            return;
        isLoading = true;
        /* get the informations of the restaurant, and retrieve the list of commands that have to
         * be retrieven */
        view.showLoading(true);

        repository.startDeliveryMode(new NetworkRequestThreadBase.NetRequestIntf<String>() {

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
                    int error = obj.get("error").getAsInt();
                    view.enterDeliveryModeSuccess(error == 0 );
                } catch (Exception e) {
                    e.printStackTrace();
                    view.enterDeliveryModeSuccess(false);
                }
                view.showLoading(false);
            }
        });

    }


}