package tg.tmye.kaba.data.customer.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAuth.login.LoginPresenter;
import tg.tmye.kaba.activity.UserAuth.register.RegisterPresenter;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class CustomerDataRepository {


    private static final String TGO = "228";
    public String TAG = "CustomerDataRepository";

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

        Customer customer = new Customer();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.CUSTOMER_USERNAME, "");
        int c_id = sharedPreferences.getInt(Config.CUSTOMER_ID, -1);

        if (c_id == -1 || "".equals(username.trim())) {
            yesOrNoWithResponse.no("logout", false);
        }

        customer.username = username;
        customer.phone_number = sharedPreferences.getString(Config.CUSTOMER_PHONE_NUMBER, "");
        customer.birthday = sharedPreferences.getString(Config.CUSTOMER_BIRTHDAY, "");
        customer.nickname = sharedPreferences.getString(Config.CUSTOMER_NICKNAME, "");
        customer.profilePicture = sharedPreferences.getString(Config.CUSTOMER_PROFILE_PICTURE, "");
        customer.theme_picture = sharedPreferences.getString(Config.CUSTOMER_THEME_PICTURE, "");

        yesOrNoWithResponse.yes(customer, false);
    }


    public void updatePushCustomerInfo (Customer customer) {

        /* using http to push it to the server */
        String jsonData = UtilFunctions.toJsonData(customer);
        networkRequestHandler.postJsonData(Config.LINK_MY_ACCOUNT_INFO, jsonData, new NetworkRequestThreadBase.NetRequestIntf<String>() {

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

    /* register */
    public void register(final String phonecode, final String password, String nickname, String request_id, final RegisterPresenter.RegisterImpl register) {

        Map<String,Object> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("password", password);
        params.put("phone_number", phonecode);
        params.put("request_id", request_id);

        networkRequestHandler.postJsonData(Config.LINK_USER_REGISTER, params, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
            }

            @Override
            public void onSysError() {
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);
                /* if ok, analyse and manage. */
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();

                int errorCode = obj.get("error").getAsInt();

                switch (errorCode) {
                    case RegisterPresenter.SUCCESS:
//                        JsonObject data = obj.get("data").getAsJsonObject();
                        register.yes(phonecode, password);
                        break;
                    default:
                        register.no(obj.get("message").getAsString(), true);
                        break;
                }
            }
        });
    }

    /* login */
    public void login (String username, String password, final LoginPresenter.LoginImpl loginImpl) {

        Map<String, String> params = new HashMap<>();
        params.put("_username", username);
        params.put("_password", password);

        /* send login params by post */
        networkRequestHandler.postWithParams (Config.LINK_USER_LOGIN, params, new NetworkRequestThreadBase.NetRequestIntf<String>(){

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

                Log.d(Constant.APP_TAG, jsonResponse);
                JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                int errorCode = obj.get("error").getAsInt();
                if (errorCode == 0) {
                    String token = obj.get("data").getAsJsonObject().get("payload").getAsJsonObject().get("token").getAsString();
                    Customer customer =
                            gson.fromJson(obj.get("data").getAsJsonObject().get("customer"), new TypeToken<Customer>(){}.getType());
                    saveToken(token);
                    saveCustomer(customer);
                    loginImpl.yes(token, customer, true);
                } else {
                    String message = obj.get("message").getAsString();
                    loginImpl.no(message, true);
                }
            }
        });
    }


    public void saveCustomer(Customer customer) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(Config.CUSTOMER_ID, customer.id);
        edit.putString(Config.CUSTOMER_USERNAME, customer.username);
        edit.putString(Config.CUSTOMER_NICKNAME, customer.nickname);
        edit.putString(Config.CUSTOMER_PHONE_NUMBER, customer.username);
        edit.putString(Config.CUSTOMER_BIRTHDAY, customer.birthday);
        edit.putString(Config.CUSTOMER_PROFILE_PICTURE, customer.profilePicture);
        edit.putString(Config.CUSTOMER_THEME_PICTURE, customer.theme_picture);
        edit.putInt(Config.CUSTOMER_GENDER, customer.gender);
        edit.putInt(Config.CUSTOMER_IS_GENDER_TO_SET, customer.isSet);

        edit.commit();
    }


    public void saveToken(String token) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Config.SYSTOKEN, token);
        edit.commit();
    }

    public void saveToApp(String token) {

        ((MyKabaApp)context.getApplicationContext()).setAuthToken(token);
    }

    public void sendPushData() {

        /*  */
        final SharedPreferences sharedPreferences = context.getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(Config.PHONE_IS_OK_WITH_SERVER, false)) {
            return;
        }

        String push_token = sharedPreferences.getString(Config.PHONE_FIREBASE_PUSH_TOKEN, "");
        if ("".equals("")) return;

        JSONObject object = new JSONObject();
        try {
            object.put("push_token", push_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String token = ((MyKabaApp)context.getApplicationContext()).getAuthToken();

        networkRequestHandler.postJsonDataWithToken(Config.LINK_PHONE_UPDATE_SERVER_PUSH_TOKEN, object.toString(), token, new NetworkRequestThreadBase.NetRequestIntf() {
            @Override
            public void onNetworkError() {
                Log.d(TAG, "posting firebase push token - failure");
            }

            @Override
            public void onSysError() {
                Log.d(TAG, "posting firebase push token - failure");
            }

            @Override
            public void onSuccess(Object jsonResponse) {
                Log.d(TAG, "posting firebase push token - success");

                /* set success into the sharedprefs */
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean(Config.PHONE_IS_OK_WITH_SERVER, true);
                edit.commit();
            }
        });
    }

    public void setIsNotOkWithServer() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(Config.PHONE_IS_OK_WITH_SERVER, false);
        edit.commit();
    }

    public void updateCustomerInformations(Customer customer, NetworkRequestThreadBase.AuthNetRequestIntf<String> intf) {

        /*        "nickname" : string,
                "email" : string,
                "password" : string,
                "gender" : string,
                "bithday" : string,
                "profile_picture" : base64,
                "theme_picture" : base64*/

        String authToken = ((MyKabaApp) context.getApplicationContext()).getAuthToken();

        JSONObject object = new JSONObject();
        try {
            object.put("nickname", customer.nickname);
            object.put("gender", customer.gender);
            object.put("birthday", ""+customer.birthday);
            object.put("phone_number", ""+customer.phone_number);

            /* if there is base64 then, write it */
            if (customer.profilePicture != null && UtilFunctions.isBase64(customer.profilePicture)) {

                /* change local path to base 64 */
                object.put("profile_picture", customer.profilePicture);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestHandler.postJsonDataWithToken(Config.LINK_UPDATE_USER_INFORMATIONS, object.toString(),
                authToken, intf);
    }

    public static String getDevicePushToken (Context ctx) {
        /* save token locally */
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(Config.PHONE_FIREBASE_PUSH_TOKEN, "");
    }

    public static void deleteCustomerInfos(Context context) {

        SharedPreferences sp = context.getSharedPreferences(Config.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Config.CUSTOMER_NICKNAME);
        editor.remove(Config.CUSTOMER_THEME_PICTURE);
        editor.remove(Config.CUSTOMER_PROFILE_PICTURE);
        editor.remove(Config.CUSTOMER_BIRTHDAY);
        editor.remove(Config.CUSTOMER_USERNAME);
        editor.remove(Config.CUSTOMER_ID);
        editor.remove(Config.CUSTOMER_GENDER);
        editor.remove(Config.CUSTOMER_IS_GENDER_TO_SET);
        editor.remove(Config.CUSTOMER_PHONE_NUMBER);
        editor.remove(Config.SYSTOKEN);
        /* delete money account relative informations */
        editor.remove(Config.BALANCE_FIELD);

        editor.commit();
        /* remove token */
    }

    public static void deleteToken(Context context) {

        ((MyKabaApp)context.getApplicationContext()).setAuthToken("");
    }

    public void sendVerificationCode(String phone_number, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

//        /api/code/request
        JSONObject object = new JSONObject();
        try {
            object.put("phone_number", TGO+phone_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestHandler.postJsonData(Config.LINK_SEND_VERIFCATION_SMS, object.toString(), intf);
    }

    public void postSuggestion(String message, NetworkRequestThreadBase.AuthNetRequestIntf<String> netRequestIntf) {

        JSONObject object = new JSONObject();
        try {
            object.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String authToken = ((MyKabaApp) context.getApplicationContext()).getAuthToken();
        networkRequestHandler.postJsonDataWithToken(Config.LINK_POST_SUGGESTION, object.toString(), authToken, netRequestIntf);
    }

    public void check_verification_code(String code, String request_id, NetworkRequestThreadBase.NetRequestIntf intf) {

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("request_id", request_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestHandler.postJsonData(Config.LINK_CHECK_VERIFCATION_CODE, object.toString(), intf);
    }

    public void recoverPassword(String account_no, String new_password, NetworkRequestThreadBase.NetRequestIntf<String> netRequestIntf) {

        /* send data to the server, and make sure there is a success */
        JSONObject object = new JSONObject();
        try {
            object.put("code", account_no);
            object.put("request_id", new_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
