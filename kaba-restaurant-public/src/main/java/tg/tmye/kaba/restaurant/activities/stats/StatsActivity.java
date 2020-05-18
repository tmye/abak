package tg.tmye.kaba.restaurant.activities.stats;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant._commons.adapter.CommandRecyclerAdapter;
import tg.tmye.kaba.restaurant._commons.adapter.StatsRecyclerAdapter;
import tg.tmye.kaba.restaurant._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba.restaurant._commons.decorator.StatsListSpacesItemDecoration;
import tg.tmye.kaba.restaurant.activities.stats.contract.RestaurantStatsContract;
import tg.tmye.kaba.restaurant.activities.stats.presenter.StatsPresenter;
import tg.tmye.kaba.restaurant.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.restaurant.data._OtherEntities.StatsEntity;
import tg.tmye.kaba.restaurant.data._OtherEntities.source.StatsRepository;

public class StatsActivity extends AppCompatActivity  implements RestaurantStatsContract.View, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;

    SwipeRefreshLayout swiperefreshlayout;

    StatsPresenter presenter;

    private StatsRepository repo;

    TextView tv_no_data_message;

    private StatsListSpacesItemDecoration statsListDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        /**/
        initViews();

        repo = new StatsRepository(this);

        presenter = new StatsPresenter(this, repo);

        swiperefreshlayout.setOnRefreshListener(this);

        presenter.start();
    }

    private void initViews() {

        recyclerView = findViewById(R.id.recyclerview);
        swiperefreshlayout = findViewById(R.id.swiperefreshlayout);
        tv_no_data_message = findViewById(R.id.tv_no_data_message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                swiperefreshlayout.setRefreshing(isLoading);

                tv_no_data_message.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });
    }


    @Override
    public void networkError() {

        /* network error */
    }

    @Override
    public void syserror() {

        /* sys error */
    }

    @Override
    public void inflate7LastDaysStats(final List<StatsEntity> statsEntities) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (statsEntities != null && statsEntities.size() == 0) {
                    /* nothing to show */
                    recyclerView.setVisibility(View.GONE);
                    tv_no_data_message.setVisibility(View.VISIBLE);
                } else {
                    tv_no_data_message.setVisibility(View.GONE);
                    initRecyclerview(statsEntities);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecyclerview(List<StatsEntity> statsEntities) {


        if (statsListDecorator == null) {
            statsListDecorator = new StatsListSpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                    getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
            );
        }

        recyclerView.removeAllViews();

        if (recyclerView.getItemDecorationCount() == 0)
            recyclerView.addItemDecoration(statsListDecorator);

        /* according */
        tv_no_data_message.setVisibility(View.GONE);
        StatsRecyclerAdapter adapter = new StatsRecyclerAdapter(this, statsEntities);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onRefresh() {
        presenter.load7LastDaysStats();
    }
}
