package tg.tmye.kaba.restaurant.activities.commands;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.activities.LoadingIsOkActivtity;
import tg.tmye.kaba.restaurant.activities.commands.contract.MyCommandContract;
import tg.tmye.kaba.restaurant.activities.commands.frag.RestaurantSubCommandListFragment;
import tg.tmye.kaba.restaurant.activities.commands.presenter.MyCommandsPresenter;
import tg.tmye.kaba.restaurant.activities.login.RestaurantLoginActivity;
import tg.tmye.kaba.restaurant.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.restaurant.data.Restaurant.source.RestaurantOnlineRepository;
import tg.tmye.kaba.restaurant.data.command.Command;
import tg.tmye.kaba.restaurant.data.command.source.CommandRepository;
import tg.tmye.kaba.restaurant.syscore.Constant;


public class MyCommandsActivity extends LoadingIsOkActivtity implements MyCommandContract.View, RestaurantSubCommandListFragment.OnFragmentInteractionListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    public static final String DESTINATION = "DESTINATION";
    RestaurantOnlineRepository restaurantOnlineRepository;

    CommandRepository commandRepository;

    MyCommandsPresenter presenter;
    /* have an global update for the activity that applies to the sub fragments */

    /* views */

    FrameLayout frame_container;
    TextView tv_no_food_message;
    SwipeRefreshLayout swiperefreshlayout;
    private Button bt_tryagain;
    LinearLayout lny_loading;

    /* create a fragment for each slot */
    RestaurantSubCommandListFragment frg_waiting, frg_cooking, frg_delivering, frg_others;

    HashMap<Integer, Fragment> frg_map;

    private String[] command_top_titles;

    private Toolbar mToolbar;

//    private View mRevealBackgroundView;
//    private View mRevealView;

    private int previousFragmentCode = WAITING;
    private BottomNavigationView navigation;

    private static final int WAITING = 1;
    private static final int COOKING = 2;
    private static final int SHIPPING = 3;
    private static final int DELIVERED = 4;
    private static final int OTHERS = 5;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_waiting:
                    return switchFragment(WAITING);
                case R.id.navigation_cooking:
                    return switchFragment(COOKING);
                case R.id.navigation_shipping:
                    return switchFragment(SHIPPING);
                case R.id.navigation_delivered:
                    return switchFragment(DELIVERED);
                case R.id.navigation_others:
                    return switchFragment(OTHERS);
            }
            return false;
        }
    };

    private RestaurantSubCommandListFragment frg_1_waiting;
    private RestaurantSubCommandListFragment frg_2_cooking;
    private RestaurantSubCommandListFragment frg_3_shipping;
    private RestaurantSubCommandListFragment frg_4_delivered;
    private RestaurantSubCommandListFragment frg_5_others;


    private List<Command> waiting_command;
    private List<Command> cooking_command;
    private List<Command> shipping_command;
    private List<Command> delivered_command;
    private List<Command> others;

    private LoadingDialogFragment loadingDialogFragment;

    /* this is the first transaction */
    private boolean isFirstTransaction = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commands_remasterised);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        initViews();
        bt_tryagain.setOnClickListener(this);
        command_top_titles = getResources().getStringArray(R.array.status_list_small);
        /* when data is found, apply it to the fragment */
        commandRepository = new CommandRepository(this);
        restaurantOnlineRepository = new RestaurantOnlineRepository(this);
        presenter = new MyCommandsPresenter(this, commandRepository);
        /* subdivision is made like this - : waiting - cooking - into delivery - and the rest  */
        swiperefreshlayout.setOnRefreshListener(this);
        restaurantOnlineRepository.sendPushData();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int destination = getIntent().getExtras().getInt(DESTINATION, 0);
        /* set bottom tabs labels constantly visible*/
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(dest[destination]);
    }

    int[] dest = new int[]{R.id.navigation_waiting, R.id.navigation_cooking, R.id.navigation_shipping,
    R.id.navigation_delivered, R.id.navigation_others};



    private void initViews() {
//        viewpager_commands = findViewById(R.id.viewpager_commands);
//        tablelayout_title_strip = findViewById(R.id.tablayout_vp_strip);
        frame_container = findViewById(R.id.frame_container);
//        recyclerView = findViewById(R.id.rec_menu_list);
        tv_no_food_message = findViewById(R.id.tv_no_food_message);
        swiperefreshlayout = findViewById(R.id.swiperefreshlayout);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        bt_tryagain = findViewById(R.id.bt_tryagain);
        lny_loading = findViewById(R.id.lny_loading);
//        mRevealView = findViewById(R.id.collapsing_toolbar);
//        mRevealBackgroundView = findViewById(R.id.appbarlayout);
        // disable viewpager scroll
    }

    @Override
    public void setPresenter(MyCommandContract.Presenter presenter) {}


    @Override
    public void showLoading(final boolean isLoading) {

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
                        showFragment(loadingDialogFragment, "loadingbox",false);
                    } else {
                        hideFragment();
                    }
                }

                frame_container.setVisibility(isLoading ? View.GONE : View.VISIBLE);

                swiperefreshlayout.setRefreshing(isLoading);
                lny_loading.setVisibility(View.GONE);
                tv_no_food_message.setVisibility(View.GONE);
                bt_tryagain.setVisibility(View.GONE);
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
    public void inflateCommands(final List<Command> waiting_command, final List<Command> cooking_command,
                                final List<Command> shipping_command, final List<Command> delivered_commands,
                                final List<Command> others) {

//        this.menu_food = menu_food;
        /* set strip together with viewpager */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /* remove all views */
                frame_container.removeAllViews();
//                frg_1_waiting = null;frg_2_cooking = frg_3_shipping = frg_4_delivered = frg_5_others = null;
                inflateStates(waiting_command, cooking_command, shipping_command, delivered_commands, others);
                switchFragment(previousFragmentCode);
            }
        });
    }

    @Override
    public void sysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showLoading(false);
                mToast(getResources().getString(R.string.sys_error));
                frame_container.setVisibility(View.GONE);

                lny_loading.setVisibility(View.VISIBLE);
                tv_no_food_message.setVisibility(View.VISIBLE);
                tv_no_food_message.setText(getResources().getString(R.string.sys_error));
                frame_container.setVisibility(View.GONE);
                /* show error page */
            }
        });
    }

    @Override
    public void networkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(false);
                mToast(getResources().getString(R.string.network_error));
                frame_container.setVisibility(View.GONE);

                lny_loading.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.VISIBLE);
                tv_no_food_message.setVisibility(View.VISIBLE);
                tv_no_food_message.setText(getResources().getString(R.string.network_error));

                /*show error page */
            }
        });
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void inflateStates (final List<Command> waiting_command, final List<Command> cooking_command, final List<Command> shipping_command, final List<Command> delivered_commands, final List<Command> others) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isFirstTransaction = true;
                setData(waiting_command, cooking_command, shipping_command, delivered_commands, others);
            }
        });
    }


    @Override
    public void onCommandInteraction(Command food) {

        /* start this activity -  */
        Intent intent = new Intent(this, CommandDetailsActivity.class);
        intent.putExtra(CommandDetailsActivity.ID, food.id);
        startActivity(intent);
    }


    @Override
    public void onRefresh() {
        /* remove all fragments */
        frame_container.removeAllViews();
        clearFragments();
        presenter.loadActualCommandsList();
    }

    private void clearFragments() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (frg_1_waiting != null)
            transaction = transaction.remove(frg_1_waiting);
        if (frg_2_cooking != null)
            transaction = transaction.remove(frg_2_cooking);
        if (frg_3_shipping != null)
            transaction = transaction .remove(frg_3_shipping);
        if (frg_4_delivered != null)
            transaction = transaction.remove(frg_4_delivered);
        if (frg_5_others != null)
            transaction = transaction.remove(frg_5_others);
        transaction.commitNow();
    }


    int lastPosition = -1;


    int[] colors = {R.color.command_state_0, R.color.command_state_1, R.color.command_state_2,
            R.color.command_state_3, R.color.command_state_4};


    public boolean performNoBackStackTransaction(final FragmentManager fragmentManager,
                                                 String tag,
                                                 Fragment fragment,
                                                 int fragmentIndex) {

        if (fragmentIndex == previousFragmentCode && !isFirstTransaction) {
            return false;
        }

        final int newBackStackLength = fragmentManager.getBackStackEntryCount() +1;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment previsouFragment = getFragmentByCode(previousFragmentCode);

        /* hide fragment if it is there, and showed */
        if (previsouFragment != null) {
            if (!isFirstTransaction) {
                fragmentTransaction = fragmentTransaction.hide(previsouFragment);
            } else
                isFirstTransaction = false;
        } else
            isFirstTransaction = false;

        /* add fragment that doesnt exist yet */
        if (getSupportFragmentManager().findFragmentByTag(getFragmentTagByCode(fragmentIndex)) == null) {
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
        return true;
    }

    private String getFragmentTagByCode(int previousFragmentCode) {

        switch (previousFragmentCode) {
            case WAITING:
                return getResources().getString(R.string.title_waiting);
            case COOKING:
                return getResources().getString(R.string.title_cooking);
            case SHIPPING:
                return getResources().getString(R.string.title_shipping);
            case DELIVERED:
                return getResources().getString(R.string.title_delivered);
            case OTHERS:
                return getResources().getString(R.string.title_others);
        }
        return null;
    }


    private Fragment getFragmentByCode(int previousFragmentCode) {

        switch (previousFragmentCode) {
            case WAITING:
                return frg_1_waiting;
            case COOKING:
                return frg_2_cooking;
            case SHIPPING:
                return frg_3_shipping;
            case DELIVERED:
                return frg_4_delivered;
            case OTHERS:
                return frg_5_others;
        }
        return null;
    }


    public void setData(List<Command> waiting_command, List<Command> cooking_command, List<Command> shipping_command, List<Command> delivered_command, List<Command> others) {

        this.waiting_command = waiting_command;
        this.cooking_command = cooking_command;
        this.shipping_command = shipping_command;
        this.delivered_command = delivered_command;
        this.others = others;

        /* update all the fragments */
        frg_1_waiting = null;
        frg_2_cooking = null;
        frg_3_shipping = null;
        frg_4_delivered = null;
        frg_5_others = null;
    }


    private boolean switchFragment(final int frgId) {

        boolean res = false;

        switch (frgId) {
            case WAITING:
                if (frg_1_waiting == null) {
                    frg_1_waiting = RestaurantSubCommandListFragment.instantiate(MyCommandsActivity.this, waiting_command);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_waiting),  frg_1_waiting, WAITING);
                break;
            case COOKING:
                if (frg_2_cooking == null) {
                    frg_2_cooking = RestaurantSubCommandListFragment.instantiate(MyCommandsActivity.this, cooking_command);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_cooking),  frg_2_cooking, COOKING);
                break;
            case SHIPPING:
                if (frg_3_shipping == null) {
                    frg_3_shipping = RestaurantSubCommandListFragment.instantiate(MyCommandsActivity.this, shipping_command);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_shipping),  frg_3_shipping, SHIPPING);
                break;
            case DELIVERED:
                if (frg_4_delivered == null) {
                    frg_4_delivered = RestaurantSubCommandListFragment.instantiate(MyCommandsActivity.this, delivered_command);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_delivered),  frg_4_delivered, DELIVERED);
                break;
            case OTHERS:
                if (frg_5_others == null) {
                    frg_5_others = RestaurantSubCommandListFragment.instantiate(MyCommandsActivity.this, others);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_others),  frg_5_others, OTHERS);
                break;
        }
        updateCommandCount();
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            initViews();
            frame_container.removeAllViews();
            clearFragments();
            presenter.loadActualCommandsList();

            /* check what is the actual fragment that is being showed, and setup the valude with it. */
            updateCommandCount();
        }
    }

    private void updateCommandCount() {
        int items_count = 0;
        switch (previousFragmentCode) {
            case WAITING:
                items_count = (waiting_command == null ? 0 : waiting_command.size());
                break;
            case COOKING:
                items_count = (cooking_command == null ? 0 : cooking_command.size());
                break;
            case SHIPPING:
                items_count = (shipping_command == null ? 0 : shipping_command.size());
                break;
            case DELIVERED:
                items_count = (delivered_command == null ? 0 : delivered_command.size());
                break;
            case OTHERS:
                items_count = (others == null ? 0 : others.size());
                break;
        }
        setTopCountValue(items_count);
    }

    private void setTopCountValue(int items_count) {

        TextView tv_resto_command_count = findViewById(R.id.tv_resto_command_count);
        tv_resto_command_count.setText((items_count < 10 ? "0"+items_count : ""+items_count));
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    public void startActivityWithNoAnimation(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            logout();
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        RestaurantOnlineRepository.deleteRestaurantInfos(this);
        RestaurantOnlineRepository.deleteToken(this);
        // restart home activity
        startActivity(new Intent(this, RestaurantLoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                presenter.loadActualCommandsList();
                break;
        }
    }

    public static class BottomNavigationViewHelper {
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e(Constant.APP_TAG, "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e(Constant.APP_TAG, "Unable to change value of shift mode", e);
            }
        }
    }


}


