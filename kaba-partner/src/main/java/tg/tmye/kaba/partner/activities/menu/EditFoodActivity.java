package tg.tmye.kaba.partner.activities.menu;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner._commons.adapter.EditFoodListAdapter;
import tg.tmye.kaba.partner._commons.adapter.EditMenuListAdapter;
import tg.tmye.kaba.partner._commons.adapter.SimpleTextAdapter;
import tg.tmye.kaba.partner.activities.menu.Fragmentz.RestaurantSubMenuFragment;
import tg.tmye.kaba.partner.activities.menu.contract.EditMenuContract;
import tg.tmye.kaba.partner.activities.menu.presenter.EditMenuPresenter;
import tg.tmye.kaba.partner.cviews.CustomProgressbar;
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.partner.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.partner.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.partner.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.partner.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.partner.data.Restaurant.source.RestaurantOnlineRepository;

public class EditFoodActivity extends AppCompatActivity implements
        EditMenuContract.View , RestaurantSubMenuFragment.OnFragmentInteractionListener,
        View.OnClickListener, OnFoodInteractionListener {

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
    private List<Restaurant_Menu_FoodEntity> foods;

    private Toolbar toolbar;

    Button bt_tryagain;

    int sub_menu_id = -1;

    TextView tv_choosed_food_count;

    private LoadingDialogFragment loadingDialogFragment;

    EditFoodListAdapter adapter;

    FloatingActionButton fab_add_food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        toolbar.setTitle(getResources().getString(R.string.menu_name));

        initViews ();

        RestaurantOnlineRepository restaurantOnlineRepository = new RestaurantOnlineRepository(this);

        RestaurantEntity resto = restaurantOnlineRepository.getRestaurant();

        // get the id of the sub menu and show it.
        sub_menu_id = getIntent().getIntExtra("SUB_MENU_ID", -1);

        bt_tryagain.setOnClickListener(this);
        fab_add_food.setOnClickListener(this);

        menu_repository = new MenuDb_OnlineRepository(this, resto);
        presenter = new EditMenuPresenter(menu_repository, this);
        presenter.populateFoodFromMenudId(sub_menu_id);
    }

    public void createNewFood () {

        Intent intent = new Intent(this, EditSingleFoodActivity.class);
        intent.putExtra("menu_id", sub_menu_id);
  startActivity(intent);
    }


    @Override
    public void inflateFoods(RestaurantEntity restaurantEntity, List<Restaurant_Menu_FoodEntity> menu_food) {

        this.restaurantEntity = restaurantEntity;
        /* init pages */
        this.foods = menu_food;
        /* set strip together with viewpager */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_error_box.setVisibility(View.GONE);
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.VISIBLE);
                inflateRecyclerView(foods);
                tv_choosed_food_count.setText(getString(R.string.foods)+": "+getNonHiddenFoodCount(foods));
            }
        });
    }

    @Override
    public void foodHiddenError() {
        mToast("Please try again, there a system error");
    }

    @Override
    public void foodHiddenSuccess() {

    }

    @Override
    public void menuDeletedSuccess() {
//        presenter.populateViews();
    }

    @Override
    public void menuDeletedError() {
        mToast("Please try again, there a system error");
    }

    @Override
    public void foodDeletedSuccess() {
//        adapter.clearData();
        presenter.populateFoodFromMenudId(sub_menu_id);
    }

    @Override
    public void foodDeletedError() {
        mToast(getString(R.string.sys_error));
    }


    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void inflateMenus(RestaurantEntity entity, final List<Restaurant_SubMenuEntity> menu_food, List<Restaurant_Menu_FoodEntity> drinks) {


    }

    private int getNonHiddenFoodCount(List<Restaurant_Menu_FoodEntity> food_) {
        int count = 0;
        for (int i = 0; i < food_.size(); i++) {
            if (food_.get(i).is_hidden == 0) // is not hidden
                count++;
        }
        return count;
    }

    private void inflateRecyclerView(List<Restaurant_Menu_FoodEntity> menu_food) {

        /* make sure hidden food is below all */
        menu_food.sort(new Comparator<Restaurant_Menu_FoodEntity>() {
            @Override
            public int compare(Restaurant_Menu_FoodEntity o1, Restaurant_Menu_FoodEntity o2) {
                return Integer.parseInt(o2.priority) - Integer.parseInt(o1.priority);
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });

        recyclerview.setNestedScrollingEnabled(false);

          adapter = new EditFoodListAdapter(this, getSupportFragmentManager(), menu_food, sub_menu_id);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
    }


    @Override
    public void showIsLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (loadingDialogFragment == null) {
                    if (isLoading) {
                        loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.content_on_loading));
                        showFragment(loadingDialogFragment, "loadingbox", true);
                    }
                } else {
                    if (isLoading) {
                        showFragment(loadingDialogFragment, "loadingbox", false);
                    } else {
                        hideFragment();
                    }
                }
            }
        });
    }

    private void hideFragment() {
        if (loadingDialogFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(loadingDialogFragment);
            loadingDialogFragment = null;
            ft.commitAllowingStateLoss();
        }
    }

    private void showFragment(LoadingDialogFragment loadingDialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(loadingDialogFragment, tag);
        else
            ft.show(loadingDialogFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.VISIBLE);
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
                bt_tryagain.setVisibility(View.VISIBLE);
            }
        });
    }



    @Override
    public void showNoDataMessage() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String message = getResources().getString(R.string.no_data);
                tv_messages.setText(message);

                progressBar.setVisibility(View.GONE);
//                frame_container.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.GONE);
                tv_messages.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
                tv_choosed_food_count.setText(getString(R.string.foods)+": "+0);
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
        if (presenter != null)
            presenter.populateFoodFromMenudId(sub_menu_id);

        if(presenter != null && sub_menu_id != 0)
            presenter.populateFoodFromMenudId(sub_menu_id);
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

        recyclerview = findViewById(R.id.recyclerview);
        tv_choosed_food_count = findViewById(R.id.tv_choosed_food_count);

        fab_add_food = findViewById(R.id.fab_add_food);
    }

    @Override
    public void onFoodInteraction(Restaurant_Menu_FoodEntity food) {

        presenter.deleteFood((int) food.id);

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
    public void setPresenter(EditMenuContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                if (presenter != null)
                    presenter.populateFoodFromMenudId(sub_menu_id);
                break;
            case R.id.iv_edit_menu:
                _jumpToEditMenuPage();
                break;
            case R.id.fab_add_food:
              createNewFood();
                break;
        }
    }


    @Override
    public void hide(int food_id) {
        //
        presenter.hideFood(food_id);
    }

    @Override
    public void delete(int food_id) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.cancel_dialog_info, null, false);

        TextView tv_message = view.findViewById(R.id.tv_message);
        Button bt_cancel = view.findViewById(R.id.bt_cancel);
        Button bt_confirm = view.findViewById(R.id.bt_confirm);

        alertDialogBuilder.setView(view);
        tv_message.setText(R.string.are_you_sure_delete_food);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            alertDialog.dismiss();
            }
        });
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteFood(food_id);
                alertDialog.dismiss();
            }
        });

       alertDialog.show();
    }

    private void _jumpToEditMenuPage() {
    }


    @Override
    protected void onDestroy() {
        if (basketFoods != null)
            basketFoods.clear();
        super.onDestroy();
    }

    static HashMap<Restaurant_Menu_FoodEntity, Integer> basketFoods;


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


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_edit_search, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                _filterRestaurantList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                _filterRestaurantList(s);
                return false;
            }
        });
        return true;
    }

    private void _filterRestaurantList(String hint) {
        if (adapter!=null)
            adapter.getFilter().filter(hint.toString());
    }

}
