package tg.tmye.kaba.activity.terms_and_condition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.Splash.SplashActivity;

public class TermsAndConditionsActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String LINK = "http://www.kaba-delivery.com/2018/09/14/cguv/";

    public static final String DATA = "DATA";
    public static final String TERMS_AND_CONDITIONS_SP = "TERMS_AND_CONDITIONS_SP", IS_OK = "IS_OK";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_agree:
                /* save that we agree */
                saveTermsAndConditionsAgreement();
                startActivity(new Intent(this, SplashActivity.class));
                break;
            case R.id.bt_disagree:
                finish();
                break;
            case R.id.bt_tryagain:
                if (isConnected(TermsAndConditionsActivity.this))
                    wbHolder.main_webview.loadUrl(LINK);
                else
                    Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void saveTermsAndConditionsAgreement() {

        SharedPreferences preferences = getSharedPreferences(TERMS_AND_CONDITIONS_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_OK, true);
        editor.commit();
    }

    class WebHolder {

        public WebView main_webview;
        public ProgressBar progressbar;

        public LinearLayout lny_bottom, lny_error;

        public Button bt_agree, bt_disagree, bt_tryagain;

        public WebHolder (View rootview) {

            this.main_webview = rootview.findViewById(R.id.webview);
            this.progressbar = rootview.findViewById(R.id.customprogressbar);

            this.bt_agree = rootview.findViewById(R.id.bt_agree);
            this.bt_disagree = rootview.findViewById(R.id.bt_disagree);
            this.lny_bottom = rootview.findViewById(R.id.lny_bottom);
            this.lny_error = rootview.findViewById(R.id.lny_error);
            this.bt_tryagain = rootview.findViewById(R.id.bt_tryagain);

            /* hide this bottom bar that is supposed to make me move elsewhere */
            this.lny_bottom.setVisibility(View.INVISIBLE);
            this.progressbar.setVisibility(View.VISIBLE);

            bt_tryagain.setOnClickListener(TermsAndConditionsActivity.this);
        }
    }

    WebHolder wbHolder;

    /* we need to receive data in a json from ... */

    /* text - image - video - text - image - possibility to like at the bottom of the page */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        final ViewGroup viewGroup = (this.findViewById(R.id.frame_container));

        wbHolder = new WebHolder(viewGroup);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if ("main".equals(getIntent().getStringExtra(TermsAndConditionsActivity.DATA))) {

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);
        }

        wbHolder.main_webview.getSettings().setJavaScriptEnabled(true);
        wbHolder.main_webview.setWebViewClient(new KabaWebClient());
        wbHolder.main_webview.setWebChromeClient(new KabaWebChromeClient());

        wbHolder.main_webview.setScrollContainer(false);

        // load a link into the webview
//        String tmpLink = getIntent().getStringExtra(DATA);
//        if (tmpLink != null && tmpLink.trim().length() > 0 && UtilFunctions.checkLink(tmpLink))
//            wbHolder.main_webview.loadUrl(tmpLink.trim());
//        else
        wbHolder.main_webview.loadUrl(LINK);

        /* check whether one should show the bottom bar, or not ... */
        if (checkHasAcceptedTermsAndConditions(this)) {
            wbHolder.lny_bottom.setVisibility(View.GONE);
        } else {
//            wbHolder.lny_bottom.setVisibility(View.VISIBLE);
            wbHolder.bt_agree.setOnClickListener(this);
            wbHolder.bt_disagree.setOnClickListener(this);
        }
    }


    public static boolean checkHasAcceptedTermsAndConditions(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(TermsAndConditionsActivity.TERMS_AND_CONDITIONS_SP, MODE_PRIVATE);
        return preferences.getBoolean(TermsAndConditionsActivity.IS_OK, false);
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
            wbHolder.main_webview.setVisibility(View.GONE);
            wbHolder.progressbar.setVisibility(View.VISIBLE);
            wbHolder.lny_bottom.setVisibility(View.GONE);
            wbHolder.lny_error.setVisibility(View.GONE);
        }

        /* on progress - if > 60%, start showing the web view */



        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            wbHolder.main_webview.setVisibility(View.VISIBLE);
            wbHolder.progressbar.setVisibility(View.GONE);

            if (!checkHasAcceptedTermsAndConditions(TermsAndConditionsActivity.this))
                wbHolder.lny_bottom.setVisibility(View.VISIBLE);
            else
                wbHolder.lny_bottom.setVisibility(View.GONE);

            showKabaErrorRetryPage();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
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
        if (!isConnected(this)) {
            Toast.makeText(this, "You are not connected / Network error", Toast.LENGTH_LONG).show();
            wbHolder.lny_bottom.setVisibility(View.GONE);
            wbHolder.main_webview.setVisibility(View.GONE);
            wbHolder.lny_error.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return is Device Connected
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();

            return (info != null && info.isConnected());
        }
        return false;
    }


    private class KabaWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress > 60 && wbHolder.main_webview.getVisibility() != View.VISIBLE){
                wbHolder.main_webview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wbHolder.main_webview.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }
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

}
