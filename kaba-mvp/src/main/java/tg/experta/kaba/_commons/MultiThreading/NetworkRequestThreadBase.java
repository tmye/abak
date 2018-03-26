package tg.experta.kaba._commons.MultiThreading;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tg.experta.kaba.config.Config;

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


    public interface NetRequestIntf {

        void onNetworkError();
        void onSysError();
        void onSuccess(String jsonResponse);
    }

    /*public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        System.out.println(response);
    }*/
}
