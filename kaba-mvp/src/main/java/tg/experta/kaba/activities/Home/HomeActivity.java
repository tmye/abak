package tg.experta.kaba.activities.Home;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import tg.experta.kaba.R;
import tg.experta.kaba._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.experta.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.experta.kaba._commons.OnImageClickListener;
import tg.experta.kaba._commons.intf.YesOrNo;
import tg.experta.kaba.activities.Home.Fragmentz.Home_1_Fragment;
import tg.experta.kaba.activities.Home.Fragmentz.Home_2_Restaurant;
import tg.experta.kaba.activities.Home.Fragmentz.Home_3_CommandList;
import tg.experta.kaba.activities.Home.Fragmentz.Home_4_MyAccount;
import tg.experta.kaba.activities.Menu.RestaurantMenuActivity;
import tg.experta.kaba.activities.UserAuth.LoginActivity;
import tg.experta.kaba.activities.scanner.ScannerActivity;
import tg.experta.kaba.config.Config;
import tg.experta.kaba.config.Constant;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;
import tg.experta.kaba.data.Restaurant.source.RestaurantDbOnlineSource;
import tg.experta.kaba.data.Restaurant.source.RestaurantLocalDataSource;
import tg.experta.kaba.data._OtherEntities.UpdateClient;
import tg.experta.kaba.data.advert.ProductAdvertItem;
import tg.experta.kaba.data.command.Command;
import tg.experta.kaba.syscore.MyKabaApp;

public class HomeActivity extends AppCompatActivity implements
        Home_1_Fragment.OnFragmentInteractionListener,
        Home_2_Restaurant.OnListFragmentInteractionListener,
        Home_3_CommandList.OnListFragmentInteractionListener,
        Home_4_MyAccount.OnFragmentInteractionListener,
        OnImageClickListener {

    private static final int HOME = 0;
    private static final int RESTAURANT = 1;
    private static final int COMMAND_LIST = 2;
    private static final int MY_ACCOUNT = 3;

    private static final long DELAY = 1000;

    ContentUpdateHolder holder;

    Gson gson = new Gson();

    // data
    private List<RestaurantEntity> daily_restoz;

    private List<RestaurantEntity> allRestaurants;

    String LINK_RESTO_FOOD_DB = Constant.SERVER_ADDRESS+
            "/sample/restaurant_menu_sample.json";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(HOME);
                    return true;
                case R.id.navigation_restaurant:
                    switchFragment(RESTAURANT);
                    return true;
                case R.id.navigation_command_list:
                    switchFragment(COMMAND_LIST);
                    return isLoggedIn;
                case R.id.navigation_my_account:
                    switchFragment(MY_ACCOUNT);
                    return isLoggedIn;
            }
            return false;
        }
    };


    // network threading
    private NetworkRequestThreadBase networkRequestBase;


    // database threading
    private DatabaseRequestThreadBase dbTransBase;


    private int previousFragmentCode = -1;

    private boolean isLoggedIn = false;


    @Override
    public void onCommandInteractionListener(Command command) {

    }

    @Override
    public void updateCommandList() {

        holder.setStartupRefreshing();
        holder.swp_home_frg.setRefreshing(true);

        // download all the data to the database
        if (networkRequestBase == null)
            networkRequestBase = ((MyKabaApp) getApplication()).getNetworkRequestBase();

        networkRequestBase.run(LINK_RESTO_FOOD_DB, new NetworkRequestThreadBase.NetRequestIntf() {

            @Override
            public void onNetworkError() {
                Log.d(Constant.APP_TAG, "NetworkError");
                holder.setNetworkError();
                notifyLoadingDone();
            }

            @Override
            public void onSysError() {
                Log.d(Constant.APP_TAG, "onSysError");
                holder.setNetworkError();
                holder.setTvMessage(getString(R.string.sys_error));
                notifyLoadingDone();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, "onSuccess");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (frg_3_command_list !=  null)
                            frg_3_command_list.update();
                    }
                });
                notifyLoadingDone();
                holder.setRefreshSuccess();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //
    class ContentUpdateHolder {

        public FrameLayout frm_main;
        public TextView tv_message;
        public Button bt_action;
        public SwipeRefreshLayout swp_home_frg;
        public BottomNavigationView navigation;
        public LinearLayout lny_message;
        public Toolbar toolbar;
        public CollapsingToolbarLayout collapsingToolbar;
        public RelativeLayout rel_toolbar_container;
        public FloatingActionButton cartFab;

        public ContentUpdateHolder (View item){

            frm_main = item.findViewById(R.id.frame_main_layout_content);
            tv_message = item.findViewById(R.id.tv_message);
            bt_action = item.findViewById(R.id.bt_action);
            swp_home_frg = findViewById(R.id.swp_home_frg);
            navigation = findViewById(R.id.navigation);
            lny_message = findViewById(R.id.lny_message);
            toolbar = findViewById(R.id.toolbar);
            collapsingToolbar = findViewById(R.id.collapsing_toolbar);
            rel_toolbar_container = findViewById(R.id.rel_toolbar_container);
            cartFab = findViewById(R.id.fab_chart);
        }

        public void setStartupRefreshing() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // remove on click listener
                    holder.bt_action.setOnClickListener(null);
                    frm_main.setVisibility(View.GONE);
                    lny_message.setVisibility(View.GONE);
                }
            });
        }

        public void setNetworkError() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    frm_main.setVisibility(View.GONE);
                    lny_message.setVisibility(View.VISIBLE);
                    bt_action.setVisibility(View.VISIBLE);
                    tv_message.setText(getString(R.string.network_error));
                    bt_action.setText(getString(R.string.try_again));

                    holder.bt_action.setOnClickListener(new AppRefreshListener());
                }
            });
        }

        public void setMessage (final String message) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    frm_main.setVisibility(View.GONE);
                    bt_action.setVisibility(View.GONE);
                    lny_message.setVisibility(View.VISIBLE);
                    tv_message.setText(message);
                }
            });
        }

        public void setRefreshSuccess() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    lny_message.setVisibility(View.GONE);
                    frm_main.setVisibility(View.VISIBLE);
                }
            });
        }

        public void setTvMessage(final String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tv_message.setText(message);
                }
            });
        }

        public void setSwipeWithMarginTop(boolean hasMargin) {
/*
            if (swp_home_frg != null && !hasMargin) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) swp_home_frg.getLayoutParams();
                params.topMargin = -getActionBarSize();
                swp_home_frg.setLayoutParams(params);
            }*/
        }
    }

    private Home_1_Fragment frg_1_home;
    private Home_2_Restaurant frg_2_restaurants;
    private Home_3_CommandList frg_3_command_list;
    private Home_4_MyAccount frg_4_myaccount;


    private void switchFragment(final int frgId) {

        /*swp_home_frg*/
        terminateRefreshing();
        switch (frgId) {
            case HOME:
//                holder.toolbar.setVisibility(View.VISIBLE);
                if (frg_1_home == null)
                    frg_1_home = Home_1_Fragment.instantiate(this, daily_restoz);
                performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_home),  frg_1_home, HOME);
                previousFragmentCode = frgId;
                break;
            case RESTAURANT:
//                holder.toolbar.setVisibility(View.VISIBLE);
                if (frg_2_restaurants == null)
                    frg_2_restaurants = Home_2_Restaurant.instantiate(this, allRestaurants);
                performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_restaurant),  frg_2_restaurants, RESTAURANT);
                previousFragmentCode = frgId;
                break;
            case COMMAND_LIST:
                checkLogin(previousFragmentCode, COMMAND_LIST, new YesOrNo(){
                    @Override
                    public void yes() {
                        frg_3_command_list = null;
                        frg_3_command_list = Home_3_CommandList.instantiate(HomeActivity.this);
                        performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_command_list),  frg_3_command_list, COMMAND_LIST);
                        previousFragmentCode = frgId;
                        // switch bottom button to the third
                    }

                    @Override
                    public void no() {
                        // go back to the previous
                        switchFragment(getSwitchedOne(previousFragmentCode));
                    }
                });
                break;
            case MY_ACCOUNT:
                checkLogin(previousFragmentCode, MY_ACCOUNT, new YesOrNo() {
                    @Override
                    public void yes() {
                        if (frg_4_myaccount == null)
                            frg_4_myaccount = Home_4_MyAccount.newInstance();
                        performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_myaccount),  frg_4_myaccount, MY_ACCOUNT);
                        previousFragmentCode = frgId;
                        // switch bottom button to the fourth
                    }

                    @Override
                    public void no() {
                        // go back to the previous
                        switchFragment(getSwitchedOne(previousFragmentCode));
                    }
                });
                break;
        }
        // save the current fragment in the shared_pref and remember to delete before adding another one
    }

    private int getSwitchedOne (int frgId) {
        switch (frgId) {
            case HOME:
                return R.id.navigation_home;
            case RESTAURANT:
                return R.id.navigation_restaurant;
            case COMMAND_LIST:
                return R.id.navigation_command_list;
            case MY_ACCOUNT:
                return R.id.navigation_my_account;
        }
        return R.id.navigation_home;
    }


    private void checkLogin(int previousFragCode, int fragmentDestination, YesOrNo yesOrNo) {

        String token = ((MyKabaApp)getApplication()).getAuthToken().trim();
        // if try to do a transaction and it fails, then you know there is someth wrong
        if (token == null || token.equals("")) {
            Intent in = new Intent(this, LoginActivity.class);
            in.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, fragmentDestination);
            in.putExtra(Config.HOME_SWITCH_FRAG_PREVIOUS, previousFragCode);
            startActivityForResult(in, Config.LOGIN_SUCCESS);
        } else {
            yesOrNo.yes();
            isLoggedIn = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.LOGIN_SUCCESS:
                // if login success, confirm the switch
                if (data != null) {
                    isLoggedIn = true;
                    int dest = data.getIntExtra(Config.HOME_SWITCH_FRAG_DESTINATION, -1);
                    switchFragment(dest);
                }
                break;
            case Config.LOGIN_FAILURE:
                if (data != null) {
                    int prev = data.getIntExtra(Config.HOME_SWITCH_FRAG_PREVIOUS, -1);
                    if (prev != -1)
                        switchFragment(prev);
                } else
                    switchFragment(HOME);
                mToast("LOGIN FAILURE");
                break;
        }
    }

    public void performNoBackStackTransaction(final FragmentManager fragmentManager,
                                              String tag,
                                              Fragment fragment,
                                              int fragmentIndex) {

        final int newBackStackLength = fragmentManager.getBackStackEntryCount() +1;

        int anim_In, anim_Out;

        if (previousFragmentCode == -1) {
            anim_In = android.R.anim.fade_in;
            anim_Out = android.R.anim.fade_out;
        } else {
            if (previousFragmentCode < fragmentIndex) {
                anim_In = R.anim.enter_from_right;
                anim_Out = R.anim.exit_to_left;
            } else {
                anim_In = R.anim.enter_from_left;
                anim_Out = R.anim.exit_to_right;
            }
        }

        anim_Out = R.anim.exit_to_void;

        fragmentManager.beginTransaction()
                .setCustomAnimations(anim_In, anim_Out)
                .replace(R.id.frame_main_layout_content, fragment, tag)
                .addToBackStack(tag)
                .commit();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int nowCount = fragmentManager.getBackStackEntryCount();
                if (newBackStackLength != nowCount) {
                    // we don't really care if going back or forward. we already performed the logic here.
                    fragmentManager.removeOnBackStackChangedListener(this);

                    if ( newBackStackLength > nowCount ) { // user pressed back
                        fragmentManager.popBackStackImmediate();
                    }
                }
            }
        });


        // update theme
        if (previousFragmentCode == 3 && fragmentIndex < 3) {
            reveal(holder.collapsingToolbar, R.color.colorPrimary, R.color.colorPrimaryDark, true);
            revealFabCart(true);
            return;
        }
        if (previousFragmentCode < 3 && fragmentIndex == 3) {
            reveal(holder.collapsingToolbar, R.color.colorPrimary_yellow, R.color.colorPrimaryDark_yellow, false);
            revealFabCart(false);
        }
    }

    private void revealFabCart(boolean isFabCartVisible) {

        if (isFabCartVisible) {
            if (holder.cartFab != null)
                holder.cartFab.setVisibility(View.VISIBLE);
        } else {
            if (holder.cartFab != null)
                holder.cartFab.setVisibility(View.GONE);
        }
    }

    private void reveal(CollapsingToolbarLayout toolbarLayout, int colorPrimary, int colorPrimaryDark, boolean leftToRight){

        if (Build.VERSION.SDK_INT >= 21) {
            // get the center for the clipping circle
            int cx, cy;
            if (!leftToRight) {
                cx = toolbarLayout.getWidth();
                cy = 0;
                holder.rel_toolbar_container.setVisibility(View.GONE);
            } else {
                cx = 0;
                cy = toolbarLayout.getHeight();
                holder.rel_toolbar_container.setVisibility(View.VISIBLE);
            }

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(toolbarLayout, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
//            toolbarLayout.setBackgroundColor(colorPrimary);
            holder.toolbar.setBackgroundColor(getResources().getColor(colorPrimary));
            anim.start();
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(colorPrimary));
            toolbarLayout.setContentScrimColor(getResources().getColor(colorPrimaryDark));
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    public void terminateRefreshing() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (holder.swp_home_frg.isRefreshing()) {
                    holder.swp_home_frg.setRefreshing(false);
                    holder.swp_home_frg.destroyDrawingCache();
                    holder.swp_home_frg.clearAnimation();
                }
            }
        });
    }


    private void mToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    FrameLayout frm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        holder = new ContentUpdateHolder(viewGroup);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        holder.setStartupRefreshing();
        holder.swp_home_frg.setRefreshing(true);
        // launch load process
        launchLoadProcess();

        holder.swp_home_frg.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Log.d(Constant.APP_TAG, "refreshing -- app");

                holder.swp_home_frg.destroyDrawingCache();
                launchLoadProcess();
            }
        });
        // get the note DAO
    }

    private void launchLoadProcess() {

        // check if we have an update to make

        checkUpdate(new YesOrNo(){

            @Override
            public void yes() {

                // update sys datas
                updateAllNewData();
                mSnack(getString(R.string.update_done));
            }

            @Override
            public void no() {

                switchFragment(HOME);
                mSnack(getString(R.string.update_no_needed));
                notifyLoadingDone();
            }
        });
    }

    private void updateAllNewData() {



        // download all the data to the database
        if (networkRequestBase == null)
            networkRequestBase = ((MyKabaApp) getApplication()).getNetworkRequestBase();

        networkRequestBase.run(LINK_RESTO_FOOD_DB, new NetworkRequestThreadBase.NetRequestIntf() {
            @Override
            public void onNetworkError() {
                Log.d(Constant.APP_TAG, "NetworkError");
                holder.setNetworkError();
                notifyLoadingDone();
            }

            @Override
            public void onSysError() {
                Log.d(Constant.APP_TAG, "onSysError");
                holder.setNetworkError();
                holder.setTvMessage(getString(R.string.sys_error));
                notifyLoadingDone();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, "onSuccess");

                doHouseWork (jsonResponse, new YesOrNo() {
                    @Override
                    public void yes() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                holder.setRefreshSuccess();
                                // if current is not home, then switch
                                switchFragment(HOME);
                                holder.navigation.setSelectedItemId(R.id.navigation_home);
                                notifyLoadingDone();
                            }
                        });
                    }

                    @Override
                    public void no() {

                    }
                });

            }
        });

        // retrieve the data from the database again

        // get data from net
        /*DaoSession daoSession = ((MyKabaApp) getApplication()).getDaoSession();
        // drop all the tables inside this
        FakeLoader.fakeLoadRestaurantList(this);
        notifyLoadingDone();*/
    }

    private void doHouseWork(final String jsonResponse, final YesOrNo yesOrNo) {

        // on a new thread
        if (dbTransBase == null)
            dbTransBase = new DatabaseRequestThreadBase(HomeActivity.this);

        // run the transactions on another database
        dbTransBase.run(new DatabaseRequestThreadBase.OnDbTrans() {
            @Override
            public void action() {
                // clear database
                RestaurantLocalDataSource.clearAllDynamicData(((MyKabaApp)getApplication()).getDaoSession());
                // save to database and inflate current objects
                RestaurantDbOnlineSource.loadRestaurantList(HomeActivity.this, jsonResponse);
                // reload data into the current database

                //
                daily_restoz = RestaurantLocalDataSource.getDailyRestaurants(HomeActivity.this);
                allRestaurants = RestaurantLocalDataSource.getAllRestaurants(HomeActivity.this);


                upDateDailyResto();
                updateRestaurantFragment_2();

                yesOrNo.yes();
            }
        });
    }

    private void updateRestaurantFragment_2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (frg_2_restaurants != null)
                    frg_2_restaurants.updateRestoz(allRestaurants);
            }
        });
    }

    private void upDateDailyResto() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (frg_1_home != null)
                    frg_1_home.updateDailyResto(daily_restoz);
            }
        });
    }


    private void notifyLoadingDone() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                holder.swp_home_frg.setRefreshing(false);
            }
        });
    }


    private void checkUpdate(final YesOrNo yesOrNo) {

        holder.frm_main.postDelayed(new Runnable() {
            @Override
            public void run() {

                // httpclient to check the LINK_RESTO_FOOD_DB of the current version
                int version = 30;

                if (version > UpdateClient.getCurrentSysVersion()) {
                    yesOrNo.yes();
                } else
                    yesOrNo.no();
            }
        },  DELAY);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
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
        } else if (id == R.id.action_mymessages) {
            return true;
        } else if (id == android.R.id.home) {

            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestaurantInteraction(RestaurantEntity entity) {

      /*  NotificationBuilder.sendOpenWebpageNotification(this,
                "Gagnez des bons de reduction",
                "WAaaaaah, mettez vous rapidement sur la plateforme et venez vous ...",
                "");

        return;*/
        Intent intent = new Intent(this, RestaurantMenuActivity.class);
        intent.putExtra(RestaurantMenuActivity.RESTAURANT_VAL, entity);
        startActivity(intent);
    }

    @Override
    public void onProductAddPressed(ProductAdvertItem productAdvertItem) {
        // open product page.
    }

    private void mSnack(String s) {
        Snackbar.make(holder.navigation, s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        // always have an overlapping view for showing the images in huge ...
    }

    private class AppRefreshListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            holder.setStartupRefreshing();
            holder.swp_home_frg.setRefreshing(true);
            // launch load process
            launchLoadProcess();
        }
    }
}
