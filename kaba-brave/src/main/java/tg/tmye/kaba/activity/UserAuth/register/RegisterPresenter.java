package tg.tmye.kaba.activity.UserAuth.register;

import tg.tmye.kaba.activity.UserAuth.login.contract.LoginContract;
import tg.tmye.kaba.activity.UserAuth.register.contract.RegisterContract;
import tg.tmye.kaba.data.customer.Customer;
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

    public static final int SUCCESS = 1;


    private final CustomerDataRepository customerDataRepository;
    private final RegisterContract.View registerView;


    public RegisterPresenter(RegisterContract.View registerView, CustomerDataRepository customerDataRepository /* login repository */) {

        this.customerDataRepository = customerDataRepository;
        this.registerView = registerView;
    }

    @Override
    public void register(String phone_number, String password, String username) {

        registerView.showLoading(true);
        customerDataRepository.register(phone_number, password, username, new RegisterImpl() {

            @Override
            public void yes(String token, Customer customer, boolean isFromOnline) {

                    /* get the answer, and do what should be done */
//                registerView.loginSuccess();
                registerView.showLoading(false);
            }

            @Override
            public void no(String message, boolean isFromOnline) {

                    /* login failure */
                    /* network issue */
                registerView.showLoading(false);
                registerView.toast(message);
            }
        });
    }

    @Override
    public void start() {

        /* populate views, but there is nothing to pop */
    }


    public interface RegisterImpl {
        public void no(String message, boolean isFromOnline);
        public void yes(String token, Customer customer, boolean isFromOnline);

    }

}
