package tg.tmye.kaba.activity.UserAuth.getbackpassword;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.UserAuth.getbackpassword.contract.ForgotPasswordContract;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;

/**
 * By abiguime on 16/08/2018.
 * email: 2597434002@qq.com
 */
public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {

    private final ForgotPasswordContract.View view;
    private final CustomerDataRepository repository;
    private boolean is_working = false;

    public ForgotPasswordPresenter (ForgotPasswordContract.View view, CustomerDataRepository repository) {

        /* repo & view */
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void recoverPassword(String account_no, String new_password, String request_id) {

        repository.recoverPassword(account_no, new_password, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {

            }

            @Override
            public void onSysError() {

            }

            @Override
            public void onSuccess(String jsonResponse) {

            }
        });
    }

    @Override
    public void sendVerificationCode(final String phone_number) {

        if (is_working)
            return;

        is_working = true;

        view.showLoading(true);

        repository.sendVerificationCode (phone_number, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.disableCodeButton(false);
                view.onNetworkError();
                view.showLoading(false);
                is_working = false;
            }

            @Override
            public void onSysError() {
                view.disableCodeButton(false);
                view.onSysError();
                view.showLoading(false);
                is_working = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {

                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();
                    if (error == 500) {
                        view.userExistsAlready();
                    } else if (error == 0) {
                        String req_id = obj.get("data").getAsJsonObject().get("request_id").getAsString();
                        view.keepRequestId (phone_number, req_id);
                    } else{
                        view.onSysError();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.disableCodeButton(false);
                view.showLoading(false);
                is_working = false;
            }
        });
    }

    @Override
    public void start() {

    }


}
