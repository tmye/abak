package tg.tmye.kaba_i_deliver.activity.delivery.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.activity.delivery.contract.DeliveryModeContract;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;
import tg.tmye.kaba_i_deliver.syscore.ILog;

/**
 * By abiguime on 04/06/2018.
 * email: 2597434002@qq.com
 */

public class DeliverModePresenter implements DeliveryModeContract.Presenter {


    private DeliveryModeContract.View view;
    private DeliveryManRepository repository;
    private Gson gson = new Gson();

    private boolean isLoading = false;

    public DeliverModePresenter (DeliveryModeContract.View view, DeliveryManRepository repository) {

        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadDistanceOptimizedDeliverList() {

        if (!isLoading) {

            view.showLoading(true);
            isLoading = true;

            repository.loadDistanceOptimizedDeliverList(new NetworkRequestThreadBase.NetRequestIntf<String>() {

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

                    isLoading = false;

                    try {
                        ILog.print(jsonResponse);
                        /* deflate all elements and send the commands list to where it should */
                        JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                        JsonObject data = obj.get("data").getAsJsonObject();

                        Command[] commands =
                                gson.fromJson(data.get("commands"), new TypeToken<Command[]>() {
                                }.getType());

                        List<Command> commandz = new ArrayList<>(Arrays.asList(commands));
                        view.inflateCommandList(commandz);
                        view.showLoading(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.inflateCommandList(null);
                        view.showLoading(false);
                    }
                }
            });
        }
    }


    @Override
    public void start() {
        loadDistanceOptimizedDeliverList();
    }

}
