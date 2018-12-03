package tg.tmye.kaba.activity.UserAcc.cash_transaction.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.contract.TopUpContract;
import tg.tmye.kaba.data.transaction.source.OperatorInfoRepository;

/**
 * By abiguime on 19/09/2018.
 * email: 2597434002@qq.com
 */
public class TopUpPresenter implements TopUpContract.Presenter {


    private final TopUpContract.View view;
    private final OperatorInfoRepository repository;

    private Gson gson = new Gson();

    public TopUpPresenter (TopUpContract.View view, OperatorInfoRepository repository) {

        this.view = view;
        this.repository = repository;
    }

    @Override
    public void start() {

    }

    @Override
    public void launchTMoneyTopUp(String phone_number, String amount) {

        view.showLoading(true);
        repository.launchTMoneyTopUp(phone_number, amount , new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.onNetworkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                /*
                "error": 0,
                "message": "",
                "data": {
                    "url": "https://paygateglobal.com/v1/page?token=8e0d33d9-cb61-428c-8faa-8c304833eed9&amount=1&description=&identifier=228"
                }
                }*/

                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    // get daily restaurants
//                int error = gson.fromJson(obj.get("error"),Integer.class);
                    String url_ = obj.get("data").getAsJsonObject().get("url").getAsString();
                    view.onTopUpLaunchSuccess(url_);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.onSysError();
                }

                view.showLoading(false);
            }


            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }


        });
    }

    @Override
    public void launchFloozTopUp(String phone_number, String amount) {

        view.showLoading(true);
        repository.launchFloozTopUp(phone_number, amount, new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.showLoading(false);
                view.onNetworkError();
            }

            @Override
            public void onSysError() {
                view.showLoading(false);
                view.onSysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                /*  */
                view.onTopUpLaunchSuccess("http://kaba-delivery.com");
                view.showLoading(false);
            }

            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }

        });
    }

}
