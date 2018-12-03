package tg.tmye.kaba.activity.UserAuth.register;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.UserAuth.register.contract.RegisterContract;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;

/**
 * By abiguime on 15/03/2018.
 * email: 2597434002@qq.com
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    public static final int NICKNAME_SHOULD_NOT_BE_NULL = 9;
    public static final int NUMBER_SHOULD_NOT_BE_NULL = 10;
    public static final int NUMBER_INVALID = 11;
    public static final int NUMBER_ALREADY_EXISTS = 12;
    public static final int PASSWORD_SHOULD_NOT_BE_NULL = 13;
    public static final int SUCCESS = 0;


    private final CustomerDataRepository customerDataRepository;
    private final RegisterContract.View registerView;


    private Gson gson = new Gson();
    private boolean is_working = false;


    public RegisterPresenter(RegisterContract.View registerView, CustomerDataRepository customerDataRepository /* login repository */) {

        this.customerDataRepository = customerDataRepository;
        this.registerView = registerView;
    }

    @Override
    public void register(String phone_number, String password, String username, String request_id) {

        if (is_working)
            return;

        is_working = true;

        registerView.showLoading(true);
        customerDataRepository.register(phone_number, password, username, request_id, new RegisterImpl() {

            @Override
            public void yes(String phonecode, String password) {
                registerView.showLoading(false);
                registerView.registerSuccess(phonecode, password);
                is_working = false;
            }

            @Override
            public void no(String message, boolean isFromOnline) {

                /* login failure */
                /* network issue */
                registerView.showLoading(false);
                registerView.toast(message);
                is_working = false;
            }

        });
    }

    @Override
    public void sendVerificationCode(final String phone_number) {

        if (is_working)
            return;

        is_working = true;

        registerView.showLoading(true);

        customerDataRepository.sendVerificationCode (phone_number, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                registerView.disableCodeButton(false);
                registerView.onNetworkError();
                registerView.showLoading(false);
                is_working = false;
            }

            @Override
            public void onSysError() {
                registerView.disableCodeButton(false);
                registerView.onSysError();
                registerView.showLoading(false);
                is_working = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {

                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();
                    if (error == 500) {
                        registerView.userExistsAlready();
                    } else if (error == 0) {
                        String req_id = obj.get("data").getAsJsonObject().get("request_id").getAsString();
                        registerView.keepRequestId (phone_number, req_id);
                    } else{
                        registerView.onSysError(obj.get("message").getAsString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                registerView.disableCodeButton(false);
                registerView.showLoading(false);
                is_working = false;
            }
        });
    }

    @Override
    public void check_verification_code(String code, String request_id) {

        if (is_working)
            return;

        is_working = true;

        /* make a request to check */
        registerView.showLoading(true);
        customerDataRepository.check_verification_code(code, request_id, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                registerView.onNetworkError();
                is_working = false;
            }

            @Override
            public void onSysError() {
                registerView.onSysError();
                is_working = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {

                /* send the message affiliated */
                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();
                    if (error == 0) {
                        registerView.codeIsOk(true);
                    } else {
                        registerView.codeIsOk(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    registerView.codeIsOk(false);
                }
                is_working = false;
            }
        });
    }

    /* register client */

    @Override
    public void start() {
        /* populate views, but there is nothing to pop */
    }

    public interface RegisterImpl {
        void no(String message, boolean isFromOnline);
        //        void yes(String token, Customer customer, boolean isFromOnline);
        void yes(String phonecode, String password);
    }

}
