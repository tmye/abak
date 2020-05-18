package tg.tmye.kaba_i_deliver.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.syscore.Config;
import tg.tmye.kaba_i_deliver.syscore.Constant;
import tg.tmye.kaba_i_deliver.syscore.ILog;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

/**
 * By abiguime on 30/05/2018.
 * email: 2597434002@qq.com
 */

public class MyDeliverManFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = "MyDeliverManFirebaseInstanceIDService";

    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        /* save token locally */
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Config.FIREBASE_PUSH_SHPF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(Config.PHONE_IS_OK_WITH_SERVER, false);
        edit.putString(Config.PHONE_FIREBASE_PUSH_TOKEN, refreshedToken);
        edit.commit();

        JSONObject object = new JSONObject();
        try {
            object.put("os_version", System.getProperty("os.version"));
            object.put("build_device", Build.DEVICE);
            object.put("version_sdk", Build.VERSION.SDK);
            object.put("build_model", Build.MODEL);
            object.put("build_product", Build.PRODUCT);
            object.put("push_token", refreshedToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* send the string by push */

        NetworkRequestThreadBase networkRequestBase = ((MyKabaDeliverApp) getApplicationContext()).getNetworkRequestBase();
        networkRequestBase.postJsonData(Config.LINK_PHONE_UPDATE_SERVER_PUSH_TOKEN,
                object.toString(),
                new NetworkRequestThreadBase.NetRequestIntf<String>() {

                    @Override
                    public void onNetworkError() {}

                    @Override
                    public void onSysError() {}

                    @Override
                    public void onSuccess(String jsonResponse) {
                        /* save a local system error to the database */
                    }
                });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ILog.print("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ILog.print(" onDestroy");
    }
}
