package tg.tmye.kaba.activity.UserAuth;

import android.content.SharedPreferences;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.UserAuth.contract.LoginContract;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;

/**
 * By abiguime on 15/03/2018.
 * email: 2597434002@qq.com
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final CustomerDataRepository customerDataRepository;
    private final LoginContract.View loginView;


    public LoginPresenter (LoginContract.View loginView, CustomerDataRepository customerDataRepository /* login repository */) {

        this.customerDataRepository = customerDataRepository;
        this.loginView = loginView;
    }


    @Override
    public void login(String passwd, String phonecode) {

        loginView.showLoading(true);
        customerDataRepository.login(phonecode, passwd, new LoginImpl() {

            @Override
            public void yes(String token, Customer customer, boolean isFromOnline) {

                    /* get the answer, and do what should be done */
                loginView.loginSuccess();
                loginView.showLoading(false);
            }

            @Override
            public void no(String message, boolean isFromOnline) {

                    /* login failure */
                    /* network issue */
                loginView.showLoading(false);
                loginView.toast(message);
            }
        });
    }

    @Override
    public void start() {

        /* populate views, but there is nothing to pop */
    }


    public interface LoginImpl {
        public void no(String message, boolean isFromOnline);
        public void yes(String token, Customer customer, boolean isFromOnline);

    }

}
