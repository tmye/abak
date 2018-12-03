package tg.tmye.kaba.activity.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.ShoppingCartRecyclerAdapter;
import tg.tmye.kaba._commons.cviews.CustomProgressbar;
import tg.tmye.kaba._commons.cviews.OffRecyclerview;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba.activity.UserAuth.login.LoginActivity;
import tg.tmye.kaba.activity.cart.contract.ShoppingContract;
import tg.tmye.kaba.activity.cart.presenter.ShoppingCartPresenter;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;
import tg.tmye.kaba.data.shoppingcart.source.BasketRepository;
import tg.tmye.kaba.syscore.MyKabaApp;

import static tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity.ACTION_BUY;


public class ShoppingCartActivity extends AppCompatActivity implements ShoppingContract.View, SwipeRefreshLayout.OnRefreshListener {


    /* presenter */
    ShoppingCartPresenter presenter;
    /* contract */
    /* model */
    BasketRepository basketRepository;

    /* views */
    OffRecyclerview shoppingBasketRecyclerview;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView tv_no_command_in_basket;
    CustomProgressbar customProgressbar;

    private CommandListSpacesItemDecoration shoppingBasketListDecorator;
    private ShoppingCartRecyclerAdapter shoppingBasketAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        initViews();

        basketRepository = new BasketRepository(this);
        presenter = new ShoppingCartPresenter(basketRepository, this);
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
        tv_no_command_in_basket = findViewById(R.id.tv_no_food_message);
        customProgressbar = findViewById(R.id.progress_bar);
    }

    @Override
    public void showBasketList(final List<ShoppingBasketGroupItem> shoppingbasketContent) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                /* check content an if there is nothing, show a string */

                /* learn how to show the loading thing on command */

                if (shoppingbasketContent == null || shoppingbasketContent.size() == 0) {

                    shoppingBasketRecyclerview.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    tv_no_command_in_basket.setVisibility(View.VISIBLE);

                } else {

                    shoppingBasketRecyclerview.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    tv_no_command_in_basket.setVisibility(View.GONE);

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

                    shoppingBasketAdapter = new ShoppingCartRecyclerAdapter(ShoppingCartActivity.this, shoppingbasketContent, presenter);
                    shoppingBasketRecyclerview.setAdapter(shoppingBasketAdapter);
                }
            }
        });
    }

    @Override
    public void deleteBasketItem(BasketInItem basketInItem) {

    }

    @Override
    public void updateBasketItem() {
        presenter.populateBasket();
    }

    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shoppingBasketRecyclerview.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                swipeRefreshLayout.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                swipeRefreshLayout.setRefreshing(isLoading);
                tv_no_command_in_basket.setVisibility(View.GONE);
                customProgressbar.setVisibility(!isLoading ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public void networkError() {

    }

    @Override
    public void removeItemAt(final int groupPosition, final int itemPosition) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                shoppingBasketAdapter.removeItemAt(groupPosition, itemPosition);
            }
        });
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onRefresh() {
        presenter.updateBasket();
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

    /**
     * args.
     *  - buy
     *  - addToBasket
     */
    public void confirmSelectionBox(RestaurantEntity restaurantEntity, HashMap<Restaurant_Menu_FoodEntity, Integer> selection_map) {

        Intent intent = new Intent(this, ConfirmCommandDetailsActivity.class);

        intent.putExtra(ConfirmCommandDetailsActivity.RESTAURANT_ENTITY, restaurantEntity);
        intent.putExtra(ConfirmCommandDetailsActivity.DATA, selection_map);
        intent.putExtra(ConfirmCommandDetailsActivity.ACTION, ACTION_BUY);
        startActivityForResult(intent, ACTION_BUY);
    }

    public void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }
}
