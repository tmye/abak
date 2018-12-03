package tg.tmye.kaba.activity.UserAcc.feeds;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.notification.NotificationItem;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.UserAcc.feeds.contract.FeedContract;
import tg.tmye.kaba.activity.UserAcc.feeds.frag.FeedsSingleFragment;
import tg.tmye.kaba.activity.UserAcc.feeds.presenter.FeedPresenter;
import tg.tmye.kaba.activity.WebArticle.WebArticleActivity;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.data.Feeds.Feeds;
import tg.tmye.kaba.data.Feeds.source.FeedDataRepository;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

import static tg.tmye.kaba.activity.WebArticle.WebArticleActivity.ARTICLE_ID;


public class FeedActivity extends AppCompatActivity implements FeedContract.View, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swp;
    ViewPager viewpager;

    private FeedsPagerAdapter viewpagerAdapter;
    private TabLayout tablayout_vp_strip;

    private FeedsSingleFragment articles_frg;
    private FeedsSingleFragment notifications_frg;

    FeedPresenter presenter;
    private FeedDataRepository feedRepo;

    /* create a presenter for all notification / articles that come to us. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        initViews();
        // load feeds
        // -- firstly load from local dabatase
        // -- load from online next
        swp.setOnRefreshListener(this);

        feedRepo = new FeedDataRepository(this);
        presenter = new FeedPresenter(feedRepo, this);

        presenter.start();
    }

    private void initViews() {

        swp = findViewById(R.id.swp);
        viewpager = findViewById(R.id.viewpager);
        tablayout_vp_strip = findViewById(R.id.tablayout_vp_strip);
    }

    @Override
    public void inflateFeeds(final List<Feeds> articles, final List<Feeds> notifications) {

        /* inflate the two fragment that are inside the viewpager*/
        /* first ; create an adapter and set it to the viewpager */
        /* second; update the fragments that is already created. */

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (viewpagerAdapter == null) {
                    viewpagerAdapter = new FeedsPagerAdapter(getSupportFragmentManager(), articles, notifications);
                    viewpager.setAdapter(viewpagerAdapter);
                    tablayout_vp_strip.setupWithViewPager(viewpager);
                } else {
                    viewpagerAdapter.setData(0, articles);
                    viewpagerAdapter.setData(1, notifications);
                }

            }
        });
    }

    @Override
    public void showLoading(final boolean isVisible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swp.setRefreshing(isVisible);
            }
        });
    }

    @Override
    public void onRefresh() {
        presenter.loadFeeds();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void toast(int data_error) {
        mToast(""+data_error);
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sysError() {

    }

    @Override
    public void networkError() {

    }

    @Override
    public void setPresenter(FeedContract.Presenter presenter) {

    }

    public void onFeedItemSelected(int type, int product_id) {

        Intent intent = null;

        if (type == NotificationItem.NotificationFDestination.ARTICLE_DETAILS) {
            mToast("ARTICLE_DETAILS");
            intent = new Intent(this, WebArticleActivity.class);
            intent.putExtra(ARTICLE_ID, product_id);
            startActivity(intent);
        } else {
            /* do something else */
            switch (type) {
                case NotificationItem.NotificationFDestination.FOOD_DETAILS:
                    mToast("FOOD_DETAILS");
                    /* open activity */
                    intent = new Intent(this, FoodDetailsActivity.class);
                    intent.putExtra(FoodDetailsActivity.HAS_SENT_ID, true);
                    intent.putExtra(FoodDetailsActivity.FOOD_ID, product_id);
                    startActivity(intent);
                    break;
                case NotificationItem.NotificationFDestination.RESTAURANT_MENU:
                    mToast("RESTAURANT_MENU");
                    intent = new Intent(this, RestaurantMenuActivity.class);
                    RestaurantEntity restaurantEntity = new RestaurantEntity();
                    restaurantEntity.id = product_id;
                    intent.putExtra(RestaurantMenuActivity.RESTAURANT, restaurantEntity);
                    /* send a restaurant object */
                    startActivity(intent);
                    break;
                default:
                    mToast("default");
                    break;
            }
        }
    }

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }

    class FeedsPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Feeds> articles;
        private final List<Feeds> notifications;

        public FeedsPagerAdapter(FragmentManager fm, List<Feeds> articles, List<Feeds> notifications) {
            super(fm);
            this.articles = articles;
            this.notifications = notifications;
        }

        @Override
        public Fragment getItem(int position) {

            /* instantiate fragments only */
            switch (position) {
                case 0:
                    if (articles_frg == null) {
                        articles_frg = FeedsSingleFragment.newInstance(articles);
                    }
                    return articles_frg;
                case 1:
                    if (notifications_frg == null) {
                        notifications_frg = FeedsSingleFragment.newInstance(notifications);
                    }
                    return notifications_frg;
            }
            return null;
        }

        /* update fragment -- set data through */
        public void setData (int position, List<Feeds> feeds_data) {

            switch (position) {
                case 0:
                    if (articles_frg == null) {
                        articles_frg = FeedsSingleFragment.newInstance(articles);
                    } else {
                        articles_frg.inflateData(feeds_data);
                    }
                    break;
                case 1:
                    if (notifications_frg == null) {
                        notifications_frg = FeedsSingleFragment.newInstance(notifications);
                    } else {
                        notifications_frg.inflateData(feeds_data);
                    }
                    break;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.feeds_tabs)[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        /* get fragment */
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
