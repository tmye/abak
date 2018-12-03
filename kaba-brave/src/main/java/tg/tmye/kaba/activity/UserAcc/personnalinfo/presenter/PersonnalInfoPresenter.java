package tg.tmye.kaba.activity.UserAcc.personnalinfo.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.UserAcc.personnalinfo.contract.PersonnalInfoContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;

/**
 * By abiguime on 03/03/2018.
 * email: 2597434002@qq.com
 */

public class PersonnalInfoPresenter implements PersonnalInfoContract.Presenter {


    private final CustomerDataRepository customerDataRepository;
    private final PersonnalInfoContract.View personnalInfoView;


    private Gson gson = new Gson();

//CustomerDataRepository


    public PersonnalInfoPresenter (CustomerDataRepository customerDataRepository, PersonnalInfoContract.View personnalInfoView) {

        this.customerDataRepository = customerDataRepository;
        this.personnalInfoView =  personnalInfoView;

        personnalInfoView.setPresenter(this);
    }

    @Override
    public void start() {
        populateView();
    }

    private void populateView() {

        customerDataRepository.getCustomerInfo(new YesOrNoWithResponse() {
            @Override
            public void yes(Object data, boolean isFromOnline) {
                    /* customer item */
                Customer customer = (Customer) data;
                personnalInfoView.inflateCustomerData(customer);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {
                /* toast, not able to get data */
            }

            @Override
            public void onLoggingTimeout() {
                personnalInfoView.onLoggingTimeout();
            }
        });
    }

    @Override
    public void updateUserInformations(final Customer customer) {

        /* convert customer to json and send it back */
        personnalInfoView.showLoading(true);
        customerDataRepository.updateCustomerInformations (customer, new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                personnalInfoView.showLoading(false);
            }

            @Override
            public void onSysError() {
                personnalInfoView.showLoading(false);
            }

            @Override
            public void onSuccess(String jsonResponse) {
                Log.d(Constant.APP_TAG, jsonResponse);
                personnalInfoView.showLoading(false);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int error = obj.get("error").getAsInt();
                JsonObject data = obj.get("data").getAsJsonObject();
                if (error == 0) {

                    /* update local data */
                    Customer new_customer = gson.fromJson(data, new TypeToken<Customer>(){}.getType());
                    customerDataRepository.saveCustomer(new_customer);

                    personnalInfoView.finishActivity();
                } else {
                    personnalInfoView.toast(R.string.data_error);
                }
            }

            @Override
            public void onLoggingTimeout() {
                personnalInfoView.onLoggingTimeout();
            }
        });

    }
}
