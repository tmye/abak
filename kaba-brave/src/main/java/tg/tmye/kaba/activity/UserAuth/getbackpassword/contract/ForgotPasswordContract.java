package tg.tmye.kaba.activity.UserAuth.getbackpassword.contract;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;

/**
 * By abiguime on 16/08/2018.
 * email: 2597434002@qq.com
 */
public class ForgotPasswordContract {

    public interface View extends BaseView<ForgotPasswordContract.Presenter> {

        void showLoading(boolean isLoading);

        void toast(String message);

        void keepRequestId(String phone_number, String request_id);

        void disableCodeButton(boolean isDisabled);

        void codeIsOk(boolean isOk);

        void userExistsAlready();

        void onSysError();

        void onNetworkError();
    }


    public interface Presenter extends BasePresenter {

       void recoverPassword(String account_no, String new_password, String request_id);

        void sendVerificationCode(String current_phone_number);
    }


}
