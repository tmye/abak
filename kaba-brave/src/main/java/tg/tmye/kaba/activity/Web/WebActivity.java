package tg.tmye.kaba.activity.Web;

import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.utils.UtilFunctions;

public class WebActivity extends AppCompatActivity {


    private static final String LINK = "http://translate.tmye.org";

    private static final int PROGRESS_MAX = 100;
    private static final int PROGRESS_MIN = 0;

    // activity intent field
    public static final String DATA = "DATA";

    class WebHolder {

        public WebView main_webview;
        public ProgressBar progressbar;
        public Toolbar toolbar;
        public TextView tv_mPageTitle;
        public TextView tv_mPageBaseUrl;

        public WebHolder (View rootview) {

            this.main_webview = rootview.findViewById(R.id.webview);
            this.progressbar = rootview.findViewById(R.id.progressbar);
            this.toolbar = rootview.findViewById(R.id.toolbar);
            this.tv_mPageBaseUrl = rootview.findViewById(R.id.tv_page_main_url);
            this.tv_mPageTitle = rootview.findViewById(R.id.tv_page_title);
            initVal();
        }

        private void initVal() {
            this.progressbar.setMax(PROGRESS_MAX);
        }

        private void setProgress (int progress) {
            this.progressbar.setProgress(progress);
            if (progress >= 60) {
                this.main_webview.setVisibility(View.VISIBLE);
            }
            if (progress >= 95) {
                this.progressbar.setVisibility(View.GONE);
            }
        }
    }

    WebHolder wbHolder;

    /* we need to receive data in a json from ... */

    /* text - image - video - text - image - possibility to like at the bottom of the page */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);


        wbHolder = new WebHolder(viewGroup);

        setSupportActionBar(wbHolder.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_toolbar_cross_24dp);

        wbHolder.main_webview.getSettings().setJavaScriptEnabled(true);
        wbHolder.main_webview.setWebViewClient(new KabaWebClient());
        wbHolder.main_webview.setWebChromeClient(new KabaWebChromeClient());

//
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

            String websiteUrl = view.getUrl();
            wbHolder.tv_mPageBaseUrl.setText(websiteUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            String pageTitle = view.getTitle();
            wbHolder.tv_mPageTitle.setText(pageTitle);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
            showKabaErrorRetryPage();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//            super.onReceivedHttpError(view, request, errorResponse);
            showKabaErrorRetryPage();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            // show kaba local page for error - retry
            showKabaErrorRetryPage ();
        }

    }

    private void showKabaErrorRetryPage() {

        // hide the webview and show the overlaying page
    }

    private class KabaWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
            wbHolder.setProgress(newProgress);
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
}
