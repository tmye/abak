package tg.tmye.kaba.activity.WebArticle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.cviews.CustomProgressbar;
import tg.tmye.kaba._commons.cviews.SlidingBanner_LilRound;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.LoadingIsOkActivtity;
import tg.tmye.kaba.activity.WebArticle.contract.WebArticleContract;
import tg.tmye.kaba.activity.home.views.fragment.F_Home_1_Fragment;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.activity.restaurant.RestaurantActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.article.WebArticle;
import tg.tmye.kaba.data.article.source.WebArticleRepository;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


/* has to be an activity with transparent actionbar */
public class WebArticleActivity extends LoadingIsOkActivtity implements WebArticleContract.View , F_Home_1_Fragment.OnFragmentInteractionListener {


    public static final String ARTICLE_ID = "ARTICLE_ID";
    private static final long LAPSE_TIME = 500;


    /* the data has to be a json data - text - pic - video - json */

    /* inits */
    WebArticlePresenter presenter;
    WebArticleRepository repository;


    private LinearLayout lny_root;
    TextView tv_message;
    CustomProgressbar customProgressbar;
    NestedScrollView nestedscrollview;

    SlidingBanner_LilRound slidingBanner_lilRound;

    /* assets */
    public String style = "";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_article);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        int article_id = getIntent().getIntExtra(ARTICLE_ID, -1);

        if (article_id == -1) {
            mToast("Please specify article id");
            finish();
        }

        toolbar.setTitle("");
//        toolbar.getBackground().setAlpha(0);
//        toolbar.setBackgroundColor(0x00ffffff);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        /**/
        initViews();

        /* presenter sending me the data contained in here */
        repository = new WebArticleRepository(this);
        presenter = new WebArticlePresenter(this, repository);

        /* load style from assets */
        style = UtilFunctions.readAssetsStyleFile(this);

        initHtmlStartTagWithRobot();

        Log.d(Constant.APP_TAG, "style --> "+style);

        presenter.loadArticle(article_id);
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {

        lny_root = findViewById(R.id.lny_root);
        nestedscrollview = findViewById(R.id.nestedscrollview);
        customProgressbar = findViewById(R.id.progress_bar);
        tv_message = findViewById(R.id.tv_messages);
        slidingBanner_lilRound = findViewById(R.id.sliding_banner);
    }

    @Override
    public void showLoading(final boolean isLoading) {

        if (!isLoading) {

            slidingBanner_lilRound.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lny_root.setVisibility(View.VISIBLE);
                            nestedscrollview.setVisibility(View.VISIBLE);
                            /* drag nestedscrollview to the top */
                            nestedscrollview.scrollTo(0,0);
                        }
                    });
                }
            }, LAPSE_TIME);

            slidingBanner_lilRound.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            slidingBanner_lilRound.setVisibility(!isLoading ? View.VISIBLE : View.GONE);
                            tv_message.setVisibility(View.GONE);
                            customProgressbar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                        }
                    });
                }
            }, LAPSE_TIME*5);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    slidingBanner_lilRound.setVisibility(!isLoading ? View.VISIBLE : View.GONE);
                    tv_message.setVisibility(View.GONE);
                    customProgressbar.setVisibility(isLoading ? View.VISIBLE : View.GONE);

                    lny_root.setVisibility(View.INVISIBLE);
                    nestedscrollview.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void inflateArticle(final WebArticle article) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    WebArticleActivity.this.isOk = true;

                    /* work on header */
                    initSliderBanner(article);

                    customProgressbar.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);

                    /* clear all the views */
                    lny_root.removeAllViews();

                    /* inflate the article inside */
                    for (int i = 0; i < article.article.content.size(); i++) {

                        WebArticle.WebBloc bloc = article.article.content.get(i);
                        lny_root.addView(getViewForBloc(bloc));
                    }

                    /* show the views now */
//                    lny_root.setVisibility(View.VISIBLE);
//                    nestedscrollview.setVisibility(View.VISIBLE);

                    /* inflate bottom */

                } catch (Exception e) {
                    e.printStackTrace();
                    /* article has a problem or has been deleted */
                    lny_root.setVisibility(View.GONE);
                    nestedscrollview.setVisibility(View.GONE);
                    customProgressbar.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(getResources().getString(R.string.article_may_have_problem));
                }
                showLoading(false);
            }
        });
    }

    private void initSliderBanner(WebArticle article) {

        final List<AdsBanner> adsBannerList = new ArrayList<>();
        String[] images = article.article.image_title; /*{"resto_pic/restaurant_121525697854.jpg","resto_pic/restaurant_121525697338.jpg",
                "resto_pic/re1.png","resto_pic/re2.jpg",
                "resto_pic/re4.png"};*/

        if (images == null || images.length == 0) {
            /*  */
            slidingBanner_lilRound.setVisibility(View.GONE);
        } else {
            if (images.length > 0) {
                for (int i = 0; i < images.length; i++) {
                    AdsBanner adsBanner = new AdsBanner();
                    adsBanner.pic = images[i];
                    adsBanner.link = "";
                    adsBannerList.add(adsBanner);
                }
                slidingBanner_lilRound.load(adsBannerList, null, getSupportFragmentManager());
                slidingBanner_lilRound.setVisibility(View.VISIBLE);

                /* make the imageviews clickable */
                slidingBanner_lilRound.setOnClickListener(new OnImageClickListener() {
                    @Override
                    public void onClick(View view) {

                        /* on transforme ca en ads */
                        Intent intent = new Intent(WebArticleActivity.this, ImagePreviewActivity.class);
                        intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, adsBannerList.toArray(new AdsBanner[adsBannerList.size()]));
                        startActivity(intent);
                    }
                });
            } else {
                slidingBanner_lilRound.setVisibility(View.GONE);
            }
        }
        toolbar.setSubtitle(article.article.title);
    }

    private View getViewForBloc(WebArticle.WebBloc bloc) {

        /* inflate an item that exists already for the view */
        View resView = null;
        //
        switch (bloc.type) {
            case WebArticle.WebBloc.SINGLE_IMAGE:
                /* inflate image */
                ImageView imageView = new ImageView(this);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getScreenWidth(), 9*getScreenWidth()/16);
                params.rightMargin = 0;
                params.leftMargin = 0;
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_half);
                params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_half);
                imageView.setLayoutParams(params);
                /* add dimension thing */
                GlideApp.with(this)
                        .load(Constant.SERVER_ADDRESS +"/"+ bloc.data)
                        .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                        .placeholder(R.drawable.dark_gray_placeholder)
                        .centerCrop()
                        .into(imageView);
                resView = imageView;
                break;
            case WebArticle.WebBloc.TEXT_CONTENT:
                /* inflate title */
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_half);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_half);
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_half);
                params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_half);

                WebView webview_content_text =  new WebView(this);
                webview_content_text.setLayoutParams(params);
                webview_content_text.loadDataWithBaseURL(null,
                        html_start_body_WITH_ROBOTO +
                                bloc.data +
                                html_end_body
                        , "text/html", "UTF-8", null);
                resView = webview_content_text;
                break;
        }

        return resView;
    }

    private int getScreenWidth() {
        return UtilFunctions.getScreenSize(this)[0];
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(WebArticleContract.Presenter presenter) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    String html_start_body = "\n" + "<!DOCTYPE html>\n" + "\n" + "\n" + "    <html>\n" + "\n" + "<head>\n" + "</head>" + "<body>";

    String html_start_body_WITH_ROBOTO = html_start_body;

    public void initHtmlStartTagWithRobot () {
        html_start_body_WITH_ROBOTO = "\n" + "<!DOCTYPE html>\n" + "\n" + "\n" + "    <html>\n" + "\n" + "<head> <style> "+
                style
                +"</style>" + "</head>" + "<body>";
    }

    String html_end_body = "</body>" + "</html>" + "\n";


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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onRestaurantInteraction(RestaurantEntity restaurantEntity) {

        /* this shouldnt be a light restaurant anymore */
        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra(RestaurantMenuActivity.RESTAURANT, restaurantEntity);
        startActivity(intent);
    }

    @Override
    public void onAdsInteraction(AdsBanner ads) {

        switch (ads.type) {
            case AdsBanner.TYPE_ARTICLE:
                /* launch article */
                Intent article_intent = new Intent(this, WebArticleActivity.class);
                article_intent.putExtra(WebArticleActivity.ARTICLE_ID, ads.entity_id);
                startActivity(article_intent);
                break;
            case AdsBanner.TYPE_MENU:
                /* scroll right to the menu -- with an indication */
                Intent menu_intent = new Intent(this, RestaurantMenuActivity.class);
                // add restaurant id and menu id too
//                menu_intent.putExtra(RestaurantMenuActivity.RESTAURANT, /*ad.entity_id*/);
//                startActivity(article_intent);
                break;
            case AdsBanner.TYPE_REPAS:
                Intent food_details_intent = new Intent(this, FoodDetailsActivity.class);
                food_details_intent.putExtra(FoodDetailsActivity.FOOD_ID, ads.entity_id);
                food_details_intent.putExtra(FoodDetailsActivity.HAS_SENT_ID, true);
                startActivity(food_details_intent);
                /* implement opening food with id only, and also, implemeting the reduction thing - reduire le prix de la chose. */
                break;
            case AdsBanner.TYPE_RESTAURANT:
                /* get in a restaurant when the principal card at the top  has to be an advertising one with expiration date.
                 *      - no clicking after a certain time */
                break;
        }
    }



    @Override
    public void onComingSoonInteractionListener(RestaurantEntity resto) {

    }

}
