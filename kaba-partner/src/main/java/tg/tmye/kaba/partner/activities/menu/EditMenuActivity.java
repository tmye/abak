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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner._commons.adapter.EditMenuListAdapter;
import tg.tmye.kaba.partner._commons.adapter.SimpleTextAdapter;
import tg.tmye.kaba.partner.activities.menu.contract.EditMenuContract;
import tg.tmye.kaba.partner.activities.menu.presenter.EditMenuPresenter;
import tg.tmye.kaba.partner.cviews.CustomProgressbar;
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.partner.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.partner.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.partner.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.partner.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.partner.data.Restaurant.source.RestaurantOnlineRepository;

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

    FloatingActionButton fab_add_menu;

    private SimpleTextAdapter simpleTextAdapter;

    Button bt_tryagain;

    TextView tv_menu_count;
    private LoadingDialogFragment loadingDialogFragment;

    EditMenuListAdapter adapter;

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
        fab_add_menu.setOnClickListener(this);

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
        // usor menu food with priority
        menu_food.sort(new Comparator<Restaurant_SubMenuEntity>() {
            @Override
            public int compare(Restaurant_SubMenuEntity o1, Restaurant_SubMenuEntity o2) {
                return Integer.parseInt(o2.priority) - Integer.parseInt(o1.priority);
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        this.menu_food = menu_food;

        /* set strip together with viewpager */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                lny_error_box.setVisibility(View.GONE);
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.VISIBLE);

                inflateRecyclerView(menu_food);
            }
        });
    }

    @Override
    public void showNoDataMessage() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                recyclerview.setVisibility(View.GONE);

                String message = getResources().getString(R.string.no_data);
                tv_messages.setText(message);

                progressBar.setVisibility(View.GONE);
//                frame_container.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
                tv_messages.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.GONE);

                tv_menu_count.setText(getString(R.string.available_menu)+0);

            }
        });
    }


    private void inflateRecyclerView(List<Restaurant_SubMenuEntity> menu_food) {

        // set the count

        tv_menu_count.setText(getString(R.string.available_menu)+getNonHiddenMenuCount(menu_food));
        recyclerview.setNestedScrollingEnabled(false);
        adapter = new EditMenuListAdapter(this, menu_food);
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
    public void inflateFoods(RestaurantEntity restaurantEntity, List<Restaurant_Menu_FoodEntity> menu_food) {

    }

    @Override
    public void foodHiddenError() {

    }

    @Override
    public void foodHiddenSuccess() {

    }

    @Override
    public void menuDeletedSuccess() {
        presenter.populateViews();
    }

    @Override
    public void menuDeletedError() {
        mToast(getString(R.string.sys_error));
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void foodDeletedSuccess() {

    }

    @Override
    public void foodDeletedError() {

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
            presenter.start();
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
        fab_add_menu =  findViewById(R.id.fab_add_menu);
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
            case R.id.fab_add_menu:
                // create new menu
                Intent intent = new Intent(this, EditSingleMenuActivity.class);
                startActivity(intent);
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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_search, menu);
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

    @Override
    public void enterSubMenuFoodList(int sub_menu_id) {
        jumpToFoodList(sub_menu_id);
    }

    @Override
    public void hideSubMenu(int menu_id) {
        presenter.hideMenu(menu_id);
    }

    @Override
    public void deleteSubMenu(int menu_id) {



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.cancel_dialog_info, null, false);

        TextView tv_message = view.findViewById(R.id.tv_message);
        Button bt_cancel = view.findViewById(R.id.bt_cancel);
        Button bt_confirm = view.findViewById(R.id.bt_confirm);

        alertDialogBuilder.setView(view);
        tv_message.setText(R.string.are_you_sure_delete_menu);

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
                presenter.deleteMenu(menu_id);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

}
