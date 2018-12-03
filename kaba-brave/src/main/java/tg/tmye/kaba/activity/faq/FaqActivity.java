package tg.tmye.kaba.activity.faq;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ProgressBar;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.terms_and_condition.TermsAndConditionsActivity;

public class FaqActivity extends AppCompatActivity {


    private static final String LINK = "http://www.kaba-delivery.com/2018/10/30/faq/";

    public static final String DATA = "DATA";

    class WebHolder {

        public WebView main_webview;
        public ProgressBar progressbar;
        public Toolbar toolbar;

        public WebHolder (View rootview) {

            this.main_webview = rootview.findViewById(R.id.webview);
            this.progressbar = rootview.findViewById(R.id.customprogressbar);
            this.toolbar = rootview.findViewById(R.id.toolbar);
        }
    }

    WebHolder wbHolder;

    /* we need to receive data in a json from ... */

    /* text - image - video - text - image - possibility to like at the bottom of the page */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        wbHolder = new WebHolder(viewGroup);

        setSupportActionBar(wbHolder.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        wbHolder.main_webview.getSettings().setJavaScriptEnabled(true);
        wbHolder.main_webview.setWebViewClient(new KabaWebClient());
        wbHolder.main_webview.setWebChromeClient(new KabaWebChromeClient());

        wbHolder.main_webview.setScrollContainer(false);

        // load a link into the webview
        String tmpLink = getIntent().getStringExtra(DATA);
        if (tmpLink != null && tmpLink.trim().length() > 0 && UtilFunctions.checkLink(tmpLink))
            wbHolder.main_webview.loadUrl(tmpLink.trim());
        else
            wbHolder.main_webview.loadUrl(LINK);
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

}
