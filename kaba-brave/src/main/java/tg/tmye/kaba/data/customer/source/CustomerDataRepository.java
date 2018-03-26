package tg.tmye.kaba.data.customer.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAuth.LoginPresenter;
import tg.tmye.kaba.activity.home.contracts.F_UserMeContract;
import tg.tmye.kaba.activity.home.presenter.F_UserAccount_4_Presenter;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class CustomerDataRepository {


    private final Context context;
    private final DatabaseRequestThreadBase databaseRequestHandler;
    private final NetworkRequestThreadBase networkRequestHandler;

    private Gson gson = new Gson();

    public CustomerDataRepository(Context context) {
        this.context = context;
        this.networkRequestHandler = ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase();
        this.databaseRequestHandler = ((MyKabaApp)context.getApplicationContext()).getDatabaseRequestThreadBase();
    }


    public void getCustomerInfo(YesOrNoWithResponse yesOrNoWithResponse) {

        /* get customer id into shared preference */
//        SharedPreferences preferences = context.getSharedPreferences()

        /* load data from local*/

        /* load from online also, when online is here, we update local one */

        yesOrNoWithResponse.yes(Customer.fakeCustomer(), false);
    }


    public void updatePushCustomerInfo (Customer customer) {

        /* using http to push it to the server */
        String jsonData = UtilFunctions.toJsonData(customer);
        networkRequestHandler.postJsonData(Config.LINK_MY_ACCOUNT_INFO, jsonData, new NetworkRequestThreadBase.NetRequestIntf() {

            @Override
            public void onNetworkError() {
                /* nothing */
            }

            @Override
            public void onSysError() {
                /* error */
            }

            @Override
            public void onSuccess(String jsonResponse) {
                /* data updated */
            }
        });
    }



    /* login */
    public void login (String username, String password, final LoginPresenter.LoginImpl loginImpl) {

        Map<String, String> params = new HashMap<>();
        params.put("_username", username);
        params.put("_password", password);

           /* send login params by post */
        networkRequestHandler.postWithParams (Config.LINK_LOGIN_USER, params, new NetworkRequestThreadBase.NetRequestIntf(){

            @Override
            public void onNetworkError() {
                loginImpl.no(context.getResources().getString(R.string.network_error), false);
            }

            @Override
            public void onSysError() {
                loginImpl.no(context.getResources().getString(R.string.sys_error), false);
            }

            @Override
            public void onSuccess(String jsonResponse) {

                /* work on the network thread, and do what is there to do */
                    /* message
                    *
                    * {
                    * error : 0 (success) / 1 error
                    * message : "",
                    * data : {"token": "XXXKIAJFAJFKAJFAL",
                     * "customer" : {
                    *       "id" : 3,
                    *       "phone" : 906297XX,
                    *       "firstname" : "Ulrich",
                    *       "surname" : "Abiguime",
                    *       "nickname" : "geeky"
                    *  }
                    * }
                    * }
                    * */

                Log.d(Constant.APP_TAG, jsonResponse);

                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int errorCode = obj.get("error").getAsInt();
                String message = obj.get("message").getAsString();
                if (errorCode == 0) {
                    /* get token */
                    String token = obj.get("data").getAsJsonObject().get("token").getAsString();
                    /* get customer */
                    Customer customer =
                            gson.fromJson(obj.get("data").getAsJsonObject().get("customer"), new TypeToken<Customer>(){}.getType());
                    /* save it locally */
                    saveToken(token);
                    /* */
                    loginImpl.yes(token, customer, true);
                } else {
                    loginImpl.no(message, true);
                }
            }
        });


    }

    public void saveToken(String token) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Config.SYSTOKEN, token);
        edit.commit();
    }
}
