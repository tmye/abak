package tg.experta.kaba.activities.Menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.experta.kaba.activities.FoodDetails.FoodDetailsActivity;
import tg.experta.kaba.R;
import tg.experta.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.experta.kaba.data.Food.Restaurant_Menu_FoodEntityDao;
import tg.experta.kaba.data.Food.source.FoodDataSource;
import tg.experta.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.experta.kaba.data.Menu.Restaurant_SubMenuEntityDao;
import tg.experta.kaba.data.Menu.source.MenuDbLocalSource;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;
import tg.experta.kaba.data._OtherEntities.DaoSession;
import tg.experta.kaba.syscore.MyKabaApp;

public class RestaurantMenuActivity extends AppCompatActivity implements RestaurantSubMenuFragment.OnFragmentInteractionListener{


    // constants
    public static final String RESTAURANT_VAL = "RESTAURANT_VAL";
    public static final int RESTAURANT_ITEM_RESULT = 5000;


    // views
    ViewPager vp_menus;
    TabLayout tablelayout_title_strip;
    private List<Restaurant_SubMenuEntity> menus;

    RestaurantEntity restaurantEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        restaurantEntity = getIntent().getParcelableExtra(RESTAURANT_VAL);

        initViews ();
        initData ();
        initMenus ();

        tablelayout_title_strip.setupWithViewPager(vp_menus);
    }

    private void initData() {

        DaoSession daoSession = ((MyKabaApp) getApplication()).getDaoSession();
        Restaurant_Menu_FoodEntityDao restoDao = daoSession.getRestaurant_Menu_FoodEntityDao();


        menus = MenuDbLocalSource.loadAllSubMenusOfRestaurant(daoSession.getRestaurant_SubMenuEntityDao(), restaurantEntity);

        if (menus != null)
            for (int i = 0; i < menus.size(); i++) {
                Restaurant_SubMenuEntity subMenu = menus.get(i);
                menus.get(i).foods = FoodDataSource.findBySubMenuId(restoDao, subMenu.getId());
            }
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
    }

    @Override
    public void onFoodInteraction(Restaurant_Menu_FoodEntity food) {

                  Intent intent = new Intent(this, FoodDetailsActivity.class);
                    intent.putExtra(FoodDetailsActivity.FOOD_ID, food.get_id());
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
              restaurantEntity = data.getParcelableExtra(RESTAURANT_VAL);
            }
        }
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
                        menus.get(position)));
            }
            return frg_map.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menus.get(position).getTitle().toUpperCase();
        }

        @Override
        public int getCount() {
            return menus.size();
        }
    }





    //

}
