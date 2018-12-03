package tg.tmye.kaba.restaurant._commons.MultiThreading;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tg.tmye.kaba.restaurant.syscore.Config;
import tg.tmye.kaba.restaurant.syscore.Constant;

/**
 * By abiguime on 21/12/2017.
 * email: 2597434002@qq.com
 */

public class NetworkRequestThreadBase {

    private static final long CONNECT_TIME_OUT = 30, WRITE_TIME_OUT = 30, READ_TIME_OUT = 30;


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

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

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


    public void postJsonData (final String url, final Map<String, Object> data, final NetRequestIntf intf) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                JSONObject postdata = new JSONObject();

                Object[] d = data.keySet().toArray();
                try {
                    for (int i = 0; i < data.keySet().toArray().length; i++) {
                        String s = (String) d[i];
                        postdata.put(s, data.get(s));
                    }
                    postJsonData(url, postdata.toString(), intf);
                } catch(JSONException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public void postJsonDataWithToken (final String url, final String jsonData, final String token, final NetRequestIntf intf) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                try {

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                            .build();

                    MediaType MEDIA_TYPE = MediaType.parse("application/json");

                    RequestBody body = RequestBody.create(MEDIA_TYPE, jsonData);

                    final Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer "+token)
                            .addHeader("cache-control", "no-cache")
                            .build();

                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String bodyString = response.body().string();
                        intf.onSuccess(bodyString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        /* in case there is network exception or whatsoever */
                        intf.onNetworkError();
                    }
                } catch(Exception e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public void postMapDataWithToken (final String url, final Map<String, Object> data, final String token, final NetRequestIntf intf) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                JSONObject postdata = new JSONObject();

                Object[] d = data.keySet().toArray();
                try {
                    for (int i = 0; i < data.keySet().toArray().length; i++) {
                        String s = (String) d[i];
                        postdata.put(s, data.get(s));
                    }

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                            .build();
                    MediaType MEDIA_TYPE = MediaType.parse("application/json");

                    RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                    final Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer "+token)
                            .addHeader("cache-control", "no-cache")
                            .build();

                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String bodyString = response.body().string();
                        intf.onSuccess(bodyString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        /* in case there is network exception or whatsoever */
                        intf.onNetworkError();
                    }
                } catch(JSONException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }


    public void getDataWithToken (final String url, final Map<String, Object> data, final String token, final NetRequestIntf intf) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                String params = "";

                if (data != null) {
                    Object[] d = data.keySet().toArray();
                    for (int i = 0; i < data.keySet().toArray().length; i++) {
                        String s = (String) d[i];
                        params += (s + "=" + data.get(s));
                        if (i + 1 < data.keySet().toArray().length) {
                            params += "&";
                        }
                    }
                }

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                        .build();

                final Request request = new Request.Builder()
                        .url(url+"?"+params)
                        .get()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+token)
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String bodyString = response.body().string();
                    intf.onSuccess(bodyString);
                } catch (Exception e) {
                    e.printStackTrace();
                    intf.onNetworkError();
                }
            }
        });
    }

    public String inThreadGetDataWithToken (final String url, final Map<String, Object> data, final String token) {

        String params = "";

        if (data != null) {
            Object[] d = data.keySet().toArray();
            for (int i = 0; i < data.keySet().toArray().length; i++) {
                String s = (String) d[i];
                params += (s + "=" + data.get(s));
                if (i + 1 < data.keySet().toArray().length) {
                    params += "&";
                }
            }
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(url+"?"+params)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+token)
                .addHeader("cache-control", "no-cache")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String bodyString = response.body().string();
            return bodyString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }


    /**
     * Posting data to server
     */
    public void postJsonData(final String url, final String postdata, final NetRequestIntf inft) {

        mHandler.post(new Runnable() {
                          @Override
                          public void run() {

                              OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                      .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                                      .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                                      .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                                      .build();
                              MediaType MEDIA_TYPE = MediaType.parse("application/json");

                              RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                              final Request request = new Request.Builder()
                                      .url(url)
                                      .post(body)
                                      .addHeader("Content-Type", "application/json")
                                      .addHeader("Authorization", "Your Token")
                                      .addHeader("cache-control", "no-cache")
                                      .build();

                              try {
                                  Response response = okHttpClient.newCall(request).execute();
                                  String bodyString = response.body().string();
                                  Log.d(Constant.APP_TAG, bodyString);
                                  inft.onSuccess(bodyString);
                              } catch (Exception e) {
                                  e.printStackTrace();
                                  /* in case there is network exception or whatsoever */
                                  inft.onNetworkError();
                              }
                          }
                      }
        );
    }

    /**
     * Posting data to server
     */
    public String inThreadPostJsonData(String url, String postdata, String token) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+token)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String bodyString = response.body().string();
            Log.d(Constant.APP_TAG, bodyString);
            return bodyString;
        } catch (Exception e) {
            e.printStackTrace();
            /* in case there is network exception or whatsoever */
        }
        return "";
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

    public void postWithParams(final String url, final Map<String, String> params, final NetRequestIntf intf) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                        .build();

                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);

                Object[] entries =  params.keySet().toArray();
                for (int i = 0; i < entries.length; i++) {
                    builder.addFormDataPart((String)entries[i], params.get(entries[i]));
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
        });
    }

    public void loggedOnGet (String url, String token, NetRequestIntf<String> netRequestIntf) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer "+token)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String bodyString = response.body().string();
            netRequestIntf.onSuccess(bodyString);
        } catch (Exception e) {
            e.printStackTrace();
            /* in case there is network exception or whatsoever */
            netRequestIntf.onNetworkError();
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
