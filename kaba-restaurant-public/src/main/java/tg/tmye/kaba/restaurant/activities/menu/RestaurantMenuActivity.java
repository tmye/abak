package tg.tmye.kaba.restaurant.activities.menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant._commons.adapter.SimpleTextAdapter;
import tg.tmye.kaba.restaurant._commons.decorator.ListVerticalSpaceDecorator;
import tg.tmye.kaba.restaurant.activities.home.HomeActivity;
import tg.tmye.kaba.restaurant.activities.menu.Fragmentz.RestaurantSubMenuFragment;
import tg.tmye.kaba.restaurant.activities.menu.contract.RestaurantMenuContract;
import tg.tmye.kaba.restaurant.activities.menu.presenter.RestaurantMenuPresenter;
import tg.tmye.kaba.restaurant.cviews.CustomProgressbar;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.restaurant.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.restaurant.data.Restaurant.source.RestaurantOnlineRepository;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.GlideApp;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;


public class RestaurantMenuActivity extends AppCompatActivity implements
        RestaurantMenuContract.View , RestaurantSubMenuFragment.OnFragmentInteractionListener,
        View.OnClickListener {

    // constants
    public static final String RESTAURANT = "RESTAURANT";
    public static final String RESTAURANT_ID = "RESTAURANT_ID";
    public static final int RESTAURANT_ITEM_RESULT = 5000;
    public static final String MENU_ID = "MENU_ID";

    // views
//    ViewPager vp_menus;
//    TabLayout tablelayout_title_strip;
    CustomProgressbar progressBar;
    TextView tv_messages, tv_no_menu;
    FrameLayout frame_container;
    RecyclerView recyclerView;
//    FloatingActionButton fab_chart;

    LinearLayout lny_error_box, lny_content, lny_loading_frame;

//    CircleImageView cic_rest_pic;
//    TextView tv_restaurant_name;
//    TextView tv_working_time;
//    TextView tv_is_open;
//    static TextView tv_choosed_food_count;
    Button bt_tryagain;
    ImageView iv_edit_menu;


    static RestaurantEntity restaurantEntity;
    private RestaurantMenuContract.Presenter presenter;
    private MenuDb_OnlineRepository menu_repository;
    private List<Restaurant_SubMenuEntity> menu_food;
    private List<Restaurant_Menu_FoodEntity> drinks;
    private MenuViewpagerAdapter menuFragmentAdapter;

    private int actionbarheight;

    private int previousFragmentCode;

    private Toolbar toolbar;

    private static int BASKET_MAX = 5;

    private int preSelectedMenu = -1;

    private SimpleTextAdapter simpleTextAdapter;

    RestaurantEntity resto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        actionbarheight = toolbar.getHeight();

        toolbar.setTitle(getResources().getString(R.string.menu_name));

        initViews ();

        RestaurantOnlineRepository restaurantOnlineRepository = new RestaurantOnlineRepository(this);

          resto = restaurantOnlineRepository.getRestaurant();

        menu_repository = new MenuDb_OnlineRepository(this, resto);
        presenter = new RestaurantMenuPresenter(menu_repository, this);
        presenter.start();
        iv_edit_menu.setOnClickListener(this);
        bt_tryagain.setOnClickListener(this);
        if (basketFoods != null)
            basketFoods.clear();
    }

    private void initVectorDrawablesSet() {
    /*    Drawable bgDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_circlebackground_white_24dp, null);
        if (bgDrawable != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tv_choosed_food_count.setBackground(bgDrawable);
            }*/
    }


    @Override
    public void inflateMenus(final RestaurantEntity restaurantEntity, int maxCount, final List<Restaurant_SubMenuEntity> menu_food, final List<Restaurant_Menu_FoodEntity> drinks) {

        if (maxCount > 0) {
            this.BASKET_MAX = maxCount;
        }
        this.restaurantEntity = restaurantEntity;
        /* send list of menus and foods list */
        this.drinks = drinks;
        /* init pages */
        this.menu_food = menu_food;
        /* set strip together with viewpager */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                setBasketCountValue(0);

                /* init adapter and create the view */
                inflateRestaurant(restaurantEntity);
                inflateRecyclerview(menu_food, drinks);
                initMenus();

                /* select the preset menu */
                if (preSelectedMenu != -1) {
                    int position = getPreselectedMenuPosition(preSelectedMenu);
                    if (position != 0) {
                        onMenuInteraction(position);
                        if (simpleTextAdapter!=null)
                            simpleTextAdapter.setSelected_position(position);
                    }
                    preSelectedMenu = -1;
                }
            }
        });
    }

    private int getPreselectedMenuPosition(int preSelectedMenu) {

        int position = 0;
        for (int i = 0; i < menu_food.size(); i++) {
            if (menu_food.get(i).id == preSelectedMenu) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void inflateRestaurant(final RestaurantEntity restaurantEntity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                lny_error_box.setVisibility(View.GONE);
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.VISIBLE);

                toolbar.setTitle(restaurantEntity.name.toUpperCase());
            }
        });
    }

    private void inflateRecyclerview(List<Restaurant_SubMenuEntity> menu_list, List<Restaurant_Menu_FoodEntity> drinks) {

     /*   List<String> menu_titles = new ArrayList<>();
        for (int i = 0; i < menu_food.size(); i++) {
            menu_titles.add(menu_food.get(i).name);
        }*/
        simpleTextAdapter = new SimpleTextAdapter(this, menu_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*  */
        recyclerView.addItemDecoration(new ListVerticalSpaceDecorator(getResources().getDimensionPixelSize(R.dimen.one_px)
//                getResources().getDimensionPixelSize(R.dimen.command_item_header_height)
        ));
        recyclerView.setAdapter(simpleTextAdapter);
    }

    @Override
    public void showIsLoading(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                lny_content.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                lny_error_box.setVisibility(View.GONE);
//                tv_no_food_message.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onNetworkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void allDataFromMenuIdSuccess(RestaurantEntity restaurantEntity, List<Restaurant_SubMenuEntity> menuList) {

        this.restaurantEntity = restaurantEntity;
        this.menu_food = menuList;
        /* do inflate */
        inflateMenus(restaurantEntity, BASKET_MAX, menu_food, null);
    }

    @Override
    public void showNoDataMessage() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String message = getResources().getString(R.string.no_data);
                tv_messages.setText(message);

                progressBar.setVisibility(View.GONE);
                frame_container.setVisibility(View.GONE);
                bt_tryagain.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
                tv_messages.setVisibility(View.VISIBLE);

//                if (menu_food == null || menu_food.size() == 0) {
//                    findViewById(R.id.lny_container).setVisibility(View.GONE);
//                    tv_no_menu.setVisibility(View.VISIBLE);
//                }
            }
        });
    }

    @Override
    public void onMenuInteraction(int position) {

        /* switch fragment */
        Fragment frg = menuFragmentAdapter.getItem(position);
        performNoBackStackTransaction(getSupportFragmentManager(), "position"+position,  frg, position);
    }


    public void performNoBackStackTransaction(final FragmentManager fragmentManager,
                                              String tag,
                                              Fragment fragment,
                                              int fragmentIndex) {

        final int newBackStackLength = fragmentManager.getBackStackEntryCount() +1;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


        Fragment previsouFragment = getFragmentByCode(previousFragmentCode);
//
        /* hide fragment if it is there, and showed */
        if (previsouFragment != null)
            fragmentTransaction = fragmentTransaction.hide(previsouFragment);
        /* add fragment that doesnt exist yet */
        if (getSupportFragmentManager().findFragmentByTag("position"+fragmentIndex) == null) {
            fragmentTransaction = fragmentTransaction.add(R.id.frame_container, fragment, getFragmentTagByCode(fragmentIndex));
        }

        /* show fragment */
        fragmentTransaction = fragmentTransaction.show(fragment);

        /* commit change */
//        if (!onSaveInstanceState)
        fragmentTransaction/*.addToBackStack(tag)*/.commitAllowingStateLoss();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int nowCount = fragmentManager.getBackStackEntryCount();
                if (newBackStackLength != nowCount) {
                    // we don't really care if going back or forward. we already performed the logic here.
                    fragmentManager.removeOnBackStackChangedListener(this);

                    if ( newBackStackLength > nowCount ) { // user pressed back
                        // xx error xx
                        try {
                            fragmentManager.popBackStackImmediate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        previousFragmentCode = fragmentIndex;
    }

    private String getFragmentTagByCode(int fragmentIndex) {
        return "position"+fragmentIndex;
    }

    private Fragment getFragmentByCode(int previousFragmentCode) {

        return menuFragmentAdapter.getItem(previousFragmentCode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequestsRecursive();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequestsRecursive();
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
        this.menuFragmentAdapter = new MenuViewpagerAdapter(getSupportFragmentManager());
        onMenuInteraction(0);
    }

    private void initViews() {

        progressBar = findViewById(R.id.progress_bar);
        tv_messages = findViewById(R.id.tv_messages);
        frame_container = findViewById(R.id.frame_container);
        recyclerView = findViewById(R.id.rec_menu_list);
        tv_no_menu = findViewById(R.id.tv_no_menu);

//        tv_working_time = findViewById(R.id.tv_working_time);
//        tv_address_quartier = findViewById(R.id.tv_address_quartier);
//        cic_rest_pic = findViewById(R.id.cic_rest_pic);
//        tv_restaurant_name = findViewById(R.id.tv_restaurant_name);
//        tv_is_open = findViewById(R.id.tv_is_open);
//        tv_choosed_food_count = findViewById(R.id.tv_choosed_food_count);

        lny_content = findViewById(R.id.lny_content);
        lny_error_box = findViewById(R.id.lny_error_box);
        lny_loading_frame = findViewById(R.id.lny_loading);

        bt_tryagain = findViewById(R.id.bt_tryagain);
        iv_edit_menu = findViewById(R.id.iv_edit_menu);

    }

    @Override
    public void onFoodInteraction(Restaurant_Menu_FoodEntity food) {

      /*  Intent intent = new Intent(this, FoodDetailsActivity.class);
        intent.putExtra(FoodDetailsActivity.FOOD
                , food);
        intent.putExtra(FoodDetailsActivity.RESTAURANT_ENTITY, restaurantEntity);
        intent.putParcelableArrayListExtra(FoodDetailsActivity.RESTAURANT_SIMPLE_DRINKS, (ArrayList<? extends Parcelable>) drinks);
        *//* get drink list of the current restaurant *//*

//        RESTAURANT_SIMPLE_DRINKS
        startActivityForResult(intent, RESTAURANT_ITEM_RESULT);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);*/
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                if (presenter != null)
                    presenter.populateViews();
                break;
            case R.id.iv_edit_menu:
                _jumpToEditMenuPage();
                break;
        }
    }

    private void _jumpToEditMenuPage() {

        Intent intent = new Intent(RestaurantMenuActivity.this, EditMenuActivity.class);
        intent.putExtra(RESTAURANT_ID, resto.id);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (basketFoods != null)
            basketFoods.clear();
        super.onDestroy();
    }

    static HashMap<Restaurant_Menu_FoodEntity, Integer> basketFoods;

    private static void updateBasketCount() {

        if (basketFoods == null)
            setBasketCountValue(0);
        else {
            setBasketCountValue(getBasketTotalItemsCount());
        }
    }

    private static int getBasketTotalItemsCount() {
        int allFoodsCount = 0;
        if (basketFoods == null)
            return allFoodsCount;

        Set<Restaurant_Menu_FoodEntity> rmf = basketFoods.keySet();
        for (Restaurant_Menu_FoodEntity food : rmf) {
            allFoodsCount += basketFoods.get(food);
        }
        return allFoodsCount;
    }

    private static void setBasketCountValue(final int count) {

//        tv_choosed_food_count.setText((BASKET_MAX <= 9 ? "0" : "") + BASKET_MAX);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        /* check if connected then show another one */
        return super.onCreateOptionsMenu(menu);
    }

}
