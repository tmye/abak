package tg.tmye.kaba.activity.cart;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.ShoppingCartRecyclerAdapter;
import tg.tmye.kaba._commons.cviews.OffRecyclerview;
import tg.tmye.kaba._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba.activity.cart.contract.ShoppingContract;
import tg.tmye.kaba.activity.cart.presenter.ShoppingCartPresenter;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketForView;
import tg.tmye.kaba.data.shoppingcart.source.BasketFoodRepository;


public class ShoppingCartActivity extends AppCompatActivity implements ShoppingContract.View, SwipeRefreshLayout.OnRefreshListener {


    /* presenter */
    ShoppingCartPresenter presenter;
    /* contract */
    /* model */
    BasketFoodRepository basketFoodRepository;

    /* views */
    OffRecyclerview shoppingBasketRecyclerview;
    SwipeRefreshLayout swipeRefreshLayout;


    private CommandListSpacesItemDecoration shoppingBasketListDecorator;
    private ShoppingCartRecyclerAdapter shoppingBasketAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        basketFoodRepository = new BasketFoodRepository(this);
        presenter = new ShoppingCartPresenter(basketFoodRepository, this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    private void initViews() {

        shoppingBasketRecyclerview = findViewById(R.id.rec_basketlist);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
    }

    @Override
    public void showBasketList(final List<ShoppingBasketForView> shoppingbasketContent) {
        if (shoppingbasketContent == null)
            return;

         runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (shoppingBasketListDecorator == null) {
                    shoppingBasketListDecorator = new CommandListSpacesItemDecoration(
                            getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                        getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
                    );
                }
                if (shoppingBasketRecyclerview.getItemDecorationCount() == 0)
                    shoppingBasketRecyclerview.addItemDecoration(shoppingBasketListDecorator);

                if (shoppingBasketRecyclerview.getLayoutManager() == null)
                    shoppingBasketRecyclerview.setLayoutManager(new LinearLayoutManager(ShoppingCartActivity.this));

                shoppingBasketAdapter = new ShoppingCartRecyclerAdapter(ShoppingCartActivity.this, shoppingbasketContent);
                shoppingBasketRecyclerview.setAdapter(shoppingBasketAdapter);
            }
        });
    }

    @Override
    public void deleteBasketItem() {

    }

    @Override
    public void updateBasketItem() {

    }

    @Override
    public void showLoading(boolean isLoading) {
        if (swipeRefreshLayout.isRefreshing() == isLoading) return;
        swipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void networkError() {

    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onRefresh() {
        presenter.updateBasket();
    }
}
