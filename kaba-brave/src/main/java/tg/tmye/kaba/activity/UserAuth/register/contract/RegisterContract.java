package tg.tmye.kaba.activity.UserAuth.register.contract;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;

/**
 * By abiguime on 13/03/2018.
 * email: 2597434002@qq.com
 */

public class RegisterContract {

    public interface View extends BaseView<RegisterContract.Presenter> {

        void showLoading(boolean isLoading);
        void registerSuccess(String phone_number, String password);
//        void registerFailure(String message);

        void toast(String message);

        void onNetworkError();

        void onSysError();

        void onSysError(String message);

        void keepRequestId(String phone_number, String request_id);

        void disableCodeButton(boolean isDisabled);

        void codeIsOk(boolean isOk);

        void userExistsAlready();
    }


    public interface Presenter extends BasePresenter {

        //        void connect (Customer customer, String passwd, String phonecode);
        void register(String phone_number, String password, String username, String request_id);

        void sendVerificationCode(String phone_number);

        void check_verification_code(String code, String request_id);
    }

}
