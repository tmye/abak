package tg.tmye.kaba.activity.UserAcc.cash_transaction;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import tg.tmye.kaba.R;

public class TopUpWebActivity extends AppCompatActivity {



    private TopUpSmsBroadcastListener br;

    private CountDownTimer toastCountDown;

    public static final String DATA = "DATA";

    class WebHolder {

        public WebView main_webview;
        public ProgressBar progressbar;

        public WebHolder (View rootview) {

            this.main_webview = rootview.findViewById(R.id.webview);
            this.progressbar = rootview.findViewById(R.id.customprogressbar);
            this.progressbar.setVisibility(View.VISIBLE);
        }
    }

    WebHolder wbHolder;

    /* we need to receive data in a json from ... */

    /* text - image - video - text - image - possibility to like at the bottom of the page */

    @Override
    protected void onStart() {
        super.onStart();

        br = new TopUpSmsBroadcastListener ();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(br);
        if (toastCountDown != null)
            toastCountDown.cancel();
        if (mToastToShow != null)
            mToastToShow.cancel();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_web);

        final ViewGroup viewGroup = (this.findViewById(R.id.frame_container));

        wbHolder = new WebHolder(viewGroup);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if ("main".equals(getIntent().getStringExtra(DATA))) {

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_cross_primary_color_24dp);
        }

        String link = getIntent().getStringExtra(DATA);

        wbHolder.main_webview.getSettings().setJavaScriptEnabled(true);
        wbHolder.main_webview.setWebViewClient(new KabaWebClient());
        wbHolder.main_webview.setWebChromeClient(new KabaWebChromeClient());

        wbHolder.main_webview.setScrollContainer(false);

        wbHolder.main_webview.loadUrl(link);
    }


    private class KabaWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            wbHolder.main_webview.setVisibility(View.VISIBLE);
            wbHolder.progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            wbHolder.main_webview.setVisibility(View.VISIBLE);
            wbHolder.progressbar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            showKabaErrorRetryPage();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            showKabaErrorRetryPage();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            showKabaErrorRetryPage ();
        }
    }

    private void showKabaErrorRetryPage() {
        // hide the webview and show the overlaying page
        mToast(getString(R.string.an_error_occured));
        finish();
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class KabaWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }

    public class TopUpSmsBroadcastListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            String msgBody = msgs[i].getMessageBody();
                            if (msgBody != null) {
                                /* there are two kind of messages : tmoney / flooz */
                                if (msgBody.contains("code") && msgBody.contains("PayGate")) {
                                    /* we know its' tmoney */
                                    String noChain = get8NumberChainPrecededByColumn(msgBody);
                                    mCustomToast(getResources().getString(R.string.show_code, noChain));
                                } else if (msgBody.contains("*155*27#")) {
                                    /* this is a flooz toup */
                                    mCustomToast(getResources().getString(R.string.flooz));
                                    launchFloozConfirmationCall();
                                } else if (msgBody.contains("REF") && msgBody.toLowerCase().contains("paygate global")) {
                                        mToast(getResources().getString(R.string.topup_success));
                                        /* open solde activity */
                                    startActivity(new Intent(TopUpWebActivity.this, SoldeActivity.class));
                                        finish();
                                }else if (msgBody.toLowerCase().contains("flooz") && msgBody.toLowerCase().contains("succes")) {
                                    mToast(getResources().getString(R.string.topup_success));
                                    /* open solde activity */
                                    startActivity(new Intent(TopUpWebActivity.this, SoldeActivity.class));
                                    finish();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void mCustomToast(String message) {

        Toast toast = new Toast(this);
        TextView customView = (TextView) LayoutInflater.from(this).inflate(R.layout.tg_topup_toast_layout, null, false);
        customView.setText(message);
        toast.setView(customView);
        toast.setDuration(Toast.LENGTH_LONG);

        /* y offset should be height of the toolbar * 2 */
        int toolBarHeight = (getResources().getDimensionPixelOffset(R.dimen.toolbar_height)*6)/5;

        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,toolBarHeight);
        showToast(toast);
    }

    private void launchFloozConfirmationCall() {
        String call = "*155*27" + Uri.encode("#");
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    private String get8NumberChainPrecededByColumn(String msgBody) {
        int index_end_of_code = msgBody.lastIndexOf("code :")+("code :".length());
        String no_code = msgBody.substring(index_end_of_code).trim();
        /* check the realness of the number string. */
        return no_code;
    }


    Toast mToastToShow;

    private void showToast(Toast mt) {

        mToastToShow = mt;
        // Set the toast and duration
        int toastDurationInMilliSeconds = 60000;
        // Set the countdown to display the toast
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 300 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                if (mToastToShow != null)
                    mToastToShow.show();
            }
            public void onFinish() {
                if (mToastToShow != null)
                    mToastToShow.cancel();
            }
        };
        // Show the toast and starts the countdown
        mToastToShow.show();
        toastCountDown.start();
    }




}
