package tg.tmye.kaba.activity.home.presenter;

import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.home.contracts.F_UserMeContract;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class F_UserAccount_4_Presenter implements F_UserMeContract.Presenter {

    private final CustomerDataRepository customerDataRepository;
    private F_UserMeContract.View account4View;

    public F_UserAccount_4_Presenter(CustomerDataRepository customerDataRepository,
                                     F_UserMeContract.View account4View) {

        this.customerDataRepository = customerDataRepository;
        this.account4View = account4View;
        account4View.setPresenter((F_UserMeContract.Presenter) this);
    }


    @Override
    public void start() {

        populateViews();
    }

    private void populateViews() {

        /* 1- balance */
        customerDataRepository.getCustomerInfo(new YesOrNoWithResponse() {
            @Override
            public void yes(Object data, boolean isFromOnline) {
account4View.inflateCustomerInfo((Customer)data);
            }

            @Override
            public void no(Object data, boolean isFromOnline) {

            }
        });

        /* 2- useraccountinformations */


        /* 3- horoscope */
    }



}
