package tg.tmye.kaba.activity.UserAcc.personnalinfo.presenter;

import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.UserAcc.personnalinfo.contract.PersonnalInfoContract;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;

/**
 * By abiguime on 03/03/2018.
 * email: 2597434002@qq.com
 */

public class PersonnalInfoPresenter implements PersonnalInfoContract.Presenter {


    private final CustomerDataRepository customerDataRepository;
    private final PersonnalInfoContract.View personnalInfoView;

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
        });
    }

}
