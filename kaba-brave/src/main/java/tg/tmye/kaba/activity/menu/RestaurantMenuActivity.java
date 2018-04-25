package tg.tmye.kaba.activity.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.CustomProgressbar;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.menu.contract.RestaurantMenuContract;
import tg.tmye.kaba.activity.menu.presenter.RestaurantMenuPresenter;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.LightRestaurant;

public class RestaurantMenuActivity extends AppCompatActivity implements
        RestaurantMenuContract.View ,RestaurantSubMenuFragment.OnFragmentInteractionListener{


    // constants
    public static final String RESTAURANT = "RESTAURANT";
    public static final int RESTAURANT_ITEM_RESULT = 5000;

    // views
    ViewPager vp_menus;
    TabLayout tablelayout_title_strip;

    FloatingActionButton cart_fab;

    RestaurantEntity restaurantEntity;

    private RestaurantMenuContract.Presenter presenter;

    private MenuDb_OnlineRepository menu_repository;

    private List<Restaurant_SubMenuEntity> menu_food;

    private CustomProgressbar progressBar;

    private TextView tv_no_food_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Object tmp_restaurant = getIntent().getParcelableExtra(RESTAURANT);

        if (tmp_restaurant instanceof RestaurantEntity) {
            restaurantEntity = (RestaurantEntity) tmp_restaurant;
        } else if (tmp_restaurant instanceof LightRestaurant) {

            // find the restaurant in the db that corresponds --
            // -- if it doesnt
            restaurantEntity = RestaurantEntity.fromLightRestaurant((LightRestaurant) tmp_restaurant);
        }

        initViews ();
//        initData ();
//        initMenus ();

        menu_repository = new MenuDb_OnlineRepository(this, restaurantEntity);
        presenter = new RestaurantMenuPresenter(menu_repository,this);

        presenter.start();
    }


    @Override
    public void inflateMenus (List<Restaurant_SubMenuEntity> menu_food) {

        /* send list of menus and foods list */

        /* init pages */
        this.menu_food = menu_food;
        /* set strip together with viewpager */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tablelayout_title_strip.setupWithViewPager(vp_menus);
                /* init adapter and create the view */
                initMenus();
            }
        });
    }

    @Override
    public void showIsLoading(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                vp_menus.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                tv_no_food_message.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showNoDataMessage() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String message = getResources().getString(R.string.no_data);
                tv_no_food_message.setText(message);

                progressBar.setVisibility(View.GONE);
                vp_menus.setVisibility(View.GONE);
                tv_no_food_message.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void initMenus() {

        // menu should be gotten in the json file also
        MenuViewpagerAdapter menuViewpagerAdapter = new MenuViewpagerAdapter(getSupportFragmentManager());
        vp_menus.setAdapter(menuViewpagerAdapter);
    }

    private void initViews() {

        vp_menus = findViewById(R.id.viewpager_menus);
        tablelayout_title_strip = findViewById(R.id.tablayout_vp_strip);
        progressBar = findViewById(R.id.progress_bar);
        tv_no_food_message = findViewById(R.id.tv_no_food_message);
    }

    @Override
    public void onFoodInteraction(Restaurant_Menu_FoodEntity food) {

        Intent intent = new Intent(this, FoodDetailsActivity.class);
        intent.putExtra(FoodDetailsActivity.FOOD
                , food);
        intent.putExtra(FoodDetailsActivity.RESTAURANT_ENTITY, restaurantEntity);
        startActivityForResult(intent, RESTAURANT_ITEM_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* when the activity comes back */
        // Check which request we're responding to
        if (requestCode == RESTAURANT_ITEM_RESULT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                restaurantEntity = data.getParcelableExtra(RESTAURANT);
            }
        }
    }

    @Override
    public void setPresenter(RestaurantMenuContract.Presenter presenter) {

        this.presenter = presenter;
    }

    private class MenuViewpagerAdapter extends FragmentPagerAdapter {

        public MenuViewpagerAdapter(FragmentManager fm) {
            super(fm);
            frg_map = new HashMap<>();
        }

        Map<Integer, Fragment> frg_map;

        @Override
        public Fragment getItem(int position) {

            if (frg_map == null)
                frg_map = new HashMap<>();

            if (frg_map.get(position) == null) {
                frg_map.put(position, RestaurantSubMenuFragment.instantiate(RestaurantMenuActivity.this,
                        menu_food.get(position)));
            }
            return frg_map.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menu_food.get(position).getName().toUpperCase();
        }

        @Override
        public int getCount() {
            return menu_food.size();
        }
    }

}
