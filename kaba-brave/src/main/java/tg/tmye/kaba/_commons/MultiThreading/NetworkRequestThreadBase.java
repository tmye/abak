package tg.tmye.kaba._commons.MultiThreading;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tg.tmye.kaba.config.Config;

/**
 * By abiguime on 21/12/2017.
 * email: 2597434002@qq.com
 */

public class NetworkRequestThreadBase {

    static OkHttpClient okHttpClient = new OkHttpClient();

    Context ctx;

    Handler mHandler;

    public NetworkRequestThreadBase(Context ctx){
        this.ctx = ctx;

        HandlerThread thread = new HandlerThread(Config.KABA_CUSTOM_NETWORK_THREAD);
        thread.start();
        mHandler = new Handler(thread.getLooper());
    }

    public void run (final String url, final NetRequestIntf intf) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                runThread(url, intf);
            }
        });
    }

    private void runThread(String url, NetRequestIntf intf) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;

        if (IsNetwork_On()) {
            try {
                response = okHttpClient.newCall(request).execute();
                intf.onSuccess(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
                intf.onSysError();
            }
        } else {
            intf.onNetworkError();
        }
    }

    public void run(final OnNetworkAction networkAction) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                networkAction.run();
            }
        });
    }

    /**
     * Posting data to server
     */
    public void postJsonData(String url, String postdata, NetRequestIntf inft) {

        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");

       /* JSONObject postdata = new JSONObject();

        try {
            postdata.put("UserName", "Abhay Anand");
            postdata.put("Email", "anand.abhay1910@gmail.com");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Your Token")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
            /* in case there is network exception or whatsoever */
            inft.onNetworkError();
        }
    }

    private boolean IsNetwork_On() {
        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private boolean isNetwork_Wifi () {

        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        return isWiFi;
    }

    public void postWithParams(String url, Map<String, String> params, NetRequestIntf intf) {

        OkHttpClient okHttpClient = new OkHttpClient();

//        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
//        client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        String[] entries = (String[]) params.keySet().toArray();
        for (int i = 0; i < entries.length; i++) {
            builder.addFormDataPart(entries[i], params.get(entries[i]));
        }

        RequestBody body = builder.build();

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            intf.onSuccess(okHttpClient.newCall(request).execute().body().string());
        } catch (Exception e) {
            e.printStackTrace();
            /* in case there is network exception or whatsoever */
            intf.onNetworkError();
        }
    }


    public interface NetRequestIntf<T> {

        void onNetworkError();
        void onSysError();
        void onSuccess(T jsonResponse);
    }

    public interface OnNetworkAction extends Runnable {
        void run();
    }

    /*public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        System.out.println(response);
    }*/
}
