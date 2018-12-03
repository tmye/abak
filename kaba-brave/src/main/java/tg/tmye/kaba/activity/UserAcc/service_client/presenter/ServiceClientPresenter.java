package tg.tmye.kaba.activity.UserAcc.service_client.presenter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.UserAcc.ServiceClientActivity;
import tg.tmye.kaba.activity.UserAcc.service_client.contract.ServiceClientContract;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;

/**
 * By abiguime on 05/07/2018.
 * email: 2597434002@qq.com
 */
public class ServiceClientPresenter implements ServiceClientContract.Presenter {

    private final CustomerDataRepository repository;
    private final ServiceClientContract.View view;

    public ServiceClientPresenter(ServiceClientContract.View view, CustomerDataRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void postSuggestion(String message) {

        view.showLoading (true);
        repository.postSuggestion (message, new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.isSuccess(false);
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.isSuccess(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                /* is success then ok. */

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int error = obj.get("error").getAsInt();
                if (error == 0) {
                    view.isSuccess(true);
                } else {
                    view.isSuccess(false);
                }

            }

            @Override
            public void onLoggingTimeout() {

            }
        });


    }

    @Override
    public void start() {

    }
}
