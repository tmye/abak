package tg.tmye.kaba.activity.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.FavoriteRecyclerAdapter;
import tg.tmye.kaba._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba._commons.decorator.FavoriteListSpacesItemDecoration;
import tg.tmye.kaba.activity.cart.ShoppingCartActivity;
import tg.tmye.kaba.activity.favorite.contract.FavoriteContract;
import tg.tmye.kaba.activity.favorite.presenter.FavoritePresenter;
import tg.tmye.kaba.data.favorite.Favorite;
import tg.tmye.kaba.data.favorite.source.FavoriteRepository;


public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener, FavoriteContract.View, SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerview;
    SwipeRefreshLayout swipeRefreshLayout;

    FavoriteContract.Presenter presenter;

    private FavoriteRepository repo;

    /* rec item decoraton */
    private FavoriteListSpacesItemDecoration favoriteListDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_yellow_upward_navigation_24dp);

        initViews();

        repo = new FavoriteRepository(this);
        presenter = new FavoritePresenter(repo, this);

        swipeRefreshLayout.setOnRefreshListener(this);

        presenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        recyclerview = findViewById(R.id.recyclerview);
    }

    private void openShopCart() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                openShopCart();
                break;
        }
    }

    @Override
    public void networkError() {
    }

    @Override
    public void showIsLoading(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(isLoading);
            }
        });
    }


    @Override
    public void inflateFavoriteList(final List<Favorite> data) {
        /*  */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (favoriteListDecorator == null) {
                    favoriteListDecorator = new FavoriteListSpacesItemDecoration(
                            getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                            getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
                    );
                }
                if (recyclerview.getItemDecorationCount() == 0)
                    recyclerview.addItemDecoration(favoriteListDecorator);

                recyclerview.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this));
                recyclerview.setAdapter(new FavoriteRecyclerAdapter(FavoriteActivity.this, data));
            }
        });
    }

    @Override
    public void showErrorPage(boolean isShowed) {

    }

    @Override
    public void setPresenter(FavoriteContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefresh() {
        presenter.update();
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
}
