package tg.tmye.kaba.activity.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.cart.ShoppingCartActivity;
import tg.tmye.kaba.activity.favorite.contract.FavoriteContract;
import tg.tmye.kaba.activity.home.contracts.F_CommandContract;
import tg.tmye.kaba.data.favorite.Favorite;


public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener, FavoriteContract.View {


    RecyclerView recyclerview;

    SwipeRefreshLayout swipeRefreshLayout;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        fab.setOnClickListener(this);
    }


    private void initViews() {
        recyclerview = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
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
    public void showIsLoading(boolean isLoading) {

    }

    @Override
    public void inflateFavoriteList(List<Favorite> data) {

    }


    @Override
    public void showErrorPage(boolean isShowed) {

    }

    @Override
    public void setPresenter(F_CommandContract.Presenter presenter) {

    }
}
