package tg.tmye.kaba.restaurant.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant._commons.adapter.EditMenuListAdapter;
import tg.tmye.kaba.restaurant._commons.adapter.SimpleTextAdapter;
import tg.tmye.kaba.restaurant.activities.menu.contract.EditMenuContract;
import tg.tmye.kaba.restaurant.activities.menu.presenter.EditMenuPresenter;
import tg.tmye.kaba.restaurant.cviews.CustomProgressbar;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.restaurant.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.restaurant.data.Restaurant.source.RestaurantOnlineRepository;

public class EditMenuActivity extends AppCompatActivity implements
        EditMenuContract.View , OnMenuInteractionListener,
        View.OnClickListener {

    // constants
    public static final String RESTAURANT = "RESTAURANT";
    public static final String RESTAURANT_ID = "RESTAURANT_ID";
    public static final int RESTAURANT_ITEM_RESULT = 5000;
    public static final String MENU_ID = "MENU_ID";

    // views
    CustomProgressbar progressBar;
    TextView tv_messages, tv_no_menu;
    FrameLayout frame_container;

    LinearLayout lny_error_box, lny_content, lny_loading_frame;

    RecyclerView recyclerview;


    static RestaurantEntity restaurantEntity;
    private EditMenuContract.Presenter presenter;
    private MenuDb_OnlineRepository menu_repository;
    private List<Restaurant_SubMenuEntity> menu_food;
    private List<Restaurant_Menu_FoodEntity> drinks;

    private Toolbar toolbar;

    private SimpleTextAdapter simpleTextAdapter;

    Button bt_tryagain;

    TextView tv_menu_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        toolbar.setTitle(getResources().getString(R.string.menu_name));

        initViews ();

        RestaurantOnlineRepository restaurantOnlineRepository = new RestaurantOnlineRepository(this);

        RestaurantEntity resto = restaurantOnlineRepository.getRestaurant();

        bt_tryagain.setOnClickListener(this);

        menu_repository = new MenuDb_OnlineRepository(this, resto);
        presenter = new EditMenuPresenter(menu_repository, this);
        presenter.start();

    }



    @Override
    public void inflateMenus(RestaurantEntity entity, final List<Restaurant_SubMenuEntity> menu_food, List<Restaurant_Menu_FoodEntity> drinks) {

        this.restaurantEntity = restaurantEntity;
        /* send list of menus and foods list */
        this.drinks = drinks;
        /* init pages */
        this.menu_food = menu_food;

        /* set strip together with viewpager */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                lny_error_box.setVisibility(View.GONE);
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.VISIBLE);

                inflateRecyclerView(menu_food);
            }
        });
    }

    private void inflateRecyclerView(List<Restaurant_SubMenuEntity> menu_food) {

        // set the count

        tv_menu_count.setText("Menu: "+getNonHiddenMenuCount(menu_food));

        recyclerview.setNestedScrollingEnabled(false);

        EditMenuListAdapter adapter = new EditMenuListAdapter(this, menu_food);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);

    }

    private int getNonHiddenMenuCount(List<Restaurant_SubMenuEntity> menu_food) {
        int count = 0;
        for (int i = 0; i < menu_food.size(); i++) {
            if (menu_food.get(i).is_hidden == 0) // is not hidden
                count++;
        }
        return count;
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
    public void inflateFoods(RestaurantEntity restaurantEntity, List<Restaurant_Menu_FoodEntity> menu_food) {

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

    private void initViews() {

        progressBar = findViewById(R.id.progress_bar);
        tv_messages = findViewById(R.id.tv_messages);
        frame_container = findViewById(R.id.frame_container);

        bt_tryagain = findViewById(R.id.bt_tryagain);

        lny_content = findViewById(R.id.lny_content);
        lny_error_box = findViewById(R.id.lny_error_box);
        lny_loading_frame = findViewById(R.id.lny_loading);

        tv_menu_count = findViewById(R.id.tv_menu_count);

        recyclerview = findViewById(R.id.recyclerview);
    }

    public void jumpToFoodList(int sub_menu_id){
        Intent intent = new Intent(this, EditFoodActivity.class);
        intent.putExtra("SUB_MENU_ID", sub_menu_id);
        startActivity(intent);
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
    public void setPresenter(EditMenuContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                if (presenter != null)
                    presenter.populateViews();
                break;
        }
    }



    @Override
    protected void onDestroy() {
        if (basketFoods != null)
            basketFoods.clear();
        super.onDestroy();
    }

    static HashMap<Restaurant_Menu_FoodEntity, Integer> basketFoods;


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

    @Override
    public void enterSubMenuFoodList(int sub_menu_id) {
        jumpToFoodList(sub_menu_id);
    }
}
