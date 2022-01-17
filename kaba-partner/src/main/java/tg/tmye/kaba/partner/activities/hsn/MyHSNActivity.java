package tg.tmye.kaba.partner.activities.hsn;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner.activities.LoadingIsOkActivtity;
import tg.tmye.kaba.partner.activities.commands.CommandDetailsActivity;
import tg.tmye.kaba.partner.activities.hsn.contract.MyHSNContract;
import tg.tmye.kaba.partner.activities.hsn.frag.RestaurantSubHSNListFragment;
import tg.tmye.kaba.partner.activities.hsn.presenter.MyHSNPresenter;
import tg.tmye.kaba.partner.activities.login.RestaurantLoginActivity;
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.partner.data.Restaurant.source.RestaurantOnlineRepository;
import tg.tmye.kaba.partner.data.command.Command;
import tg.tmye.kaba.partner.data.command.source.CommandRepository;
import tg.tmye.kaba.partner.data.hsn.HSN;

public class MyHSNActivity extends LoadingIsOkActivtity implements MyHSNContract.View, RestaurantSubHSNListFragment.OnFragmentInteractionListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    public static final String DESTINATION = "DESTINATION";
    RestaurantOnlineRepository restaurantOnlineRepository;

    CommandRepository commandRepository;

    MyHSNPresenter presenter;
    /* have an global update for the activity that applies to the sub fragments */

    /* views */

    FrameLayout frame_container;
    TextView tv_no_food_message;
    SwipeRefreshLayout swiperefreshlayout;
    private Button bt_tryagain;
    LinearLayout lny_loading;

    /* create a fragment for each slot */
    RestaurantSubHSNListFragment frg_waiting, frg_cooking, frg_delivering, frg_others;

    HashMap<Integer, Fragment> frg_map;

    private String[] command_top_titles;

    private Toolbar mToolbar;

    private int previousFragmentCode = WAITING;
    private BottomNavigationView navigation;

    public static final int WAITING = 0;
    public static final int COOKING = 1;
    public static final int SHIPPING = 2;
    public static final int DELIVERED = 3;
    public static final int REJECTED = 4;
    public static final int CANCELLED = 5;

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
//                case R.id.navigation_rejected:
                default:
                    return switchFragment(REJECTED);
//                case R.id.navigation_cancelled:
//                    return switchFragment(CANCELLED);
            }
//            return false;
        }
    };

    private RestaurantSubHSNListFragment frg_1_waiting,  frg_2_shipping, frg_6_cooking, frg_3_delivered, frg_4_rejected/*, frg_5_cancelled*/; // -1 attente -2 annulé 0 livraison 1 livré

    private LoadingDialogFragment loadingDialogFragment;

    /* this is the first transaction */
    private boolean isFirstTransaction = true;

    private List<HSN> waiting_hsn;
    private List<HSN> cooking_hsn;
    private List<HSN> shipping_hsn;
    private List<HSN> delivered_hsn;
    private List<HSN> cancelled_hsn;
    private List<HSN> rejected_hsn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hsnactivity);
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
        presenter = new MyHSNPresenter(this, commandRepository);
        /* subdivision is made like this - : waiting - cooking - into delivery - and the rest  */
        swiperefreshlayout.setOnRefreshListener(this);
        restaurantOnlineRepository.sendPushData();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        int destination = getIntent().getExtras().getInt(DESTINATION, 0);
        /* set bottom tabs labels constantly visible*/
//        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(dest[0]);
    }

    int[] dest = new int[]{R.id.navigation_waiting, R.id.navigation_shipping,
            R.id.navigation_delivered};

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
    public void setPresenter(MyHSNContract.Presenter presenter) {}


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
    public void inflateHSN(List<HSN> hsns) {

        // dispatch
        hsns.forEach(new Consumer<HSN>() {
            @Override
            public void accept(HSN hsn) {
                switch (hsn.state){
                    case WAITING:
//                        if (waiting_hsn == null)
                            waiting_hsn = new ArrayList<>();
                        waiting_hsn.add(hsn);
                        break;
                    case COOKING:
//                        if (cooking_hsn == null)
                            cooking_hsn = new ArrayList<>();
                        cooking_hsn.add(hsn);
                        break;
                    case SHIPPING:
//                        if (shipping_hsn == null)
                            shipping_hsn = new ArrayList<>();
                        shipping_hsn.add(hsn);
                        break;
                    case DELIVERED:
//                        if (delivered_hsn == null)
                            delivered_hsn = new ArrayList<>();
                        delivered_hsn.add(hsn);
                        break;
                  case CANCELLED:
//                        if (cancelled_hsn == null)
                            cancelled_hsn = new ArrayList<>();
                        cancelled_hsn.add(hsn);
                        break;
                    case REJECTED:
//                        if (rejected_hsn == null)
                            rejected_hsn = new ArrayList<>();
                        rejected_hsn.add(hsn);
                        break;

                }
            }
        });

//        this.menu_food = menu_food;
        /* set strip together with viewpager */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /* remove all views */
                frame_container.removeAllViews();
                inflateStates(waiting_hsn, cooking_hsn, shipping_hsn, delivered_hsn, rejected_hsn,cancelled_hsn);
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


    private void inflateStates(final List<HSN> waiting, final List<HSN> cooking, final List<HSN> shipping, final List<HSN> delivered, final List<HSN> rejected, final List<HSN> cancelled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isFirstTransaction = true;
                setData(waiting, cooking, shipping, delivered, rejected, cancelled);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                frame_container.removeAllViews();
                clearFragments();
                presenter.loadActualHSNList();
            }
        });
    }

    private void clearFragments() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (frg_1_waiting != null)
            transaction = transaction.remove(frg_1_waiting);
        if (frg_2_shipping != null)
            transaction = transaction.remove(frg_2_shipping);
        if (frg_3_delivered != null)
            transaction = transaction .remove(frg_3_delivered);
        if (frg_4_rejected != null)
            transaction = transaction .remove(frg_4_rejected);
        transaction.commitNow();
    }

    int lastPosition = -1;


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
            case SHIPPING:
                return getResources().getString(R.string.title_shipping);
            case DELIVERED:
                return getResources().getString(R.string.title_delivered);
        }
        return null;
    }


    private Fragment getFragmentByCode(int previousFragmentCode) {

        switch (previousFragmentCode) {
            case WAITING:
                return frg_1_waiting;
            case SHIPPING:
                return frg_2_shipping;
            case DELIVERED:
                return frg_3_delivered;
            case REJECTED:
                return frg_4_rejected;
        }
        return null;
    }


    public void setData(List<HSN> waiting_hsn, List<HSN> cooking_hsn,List<HSN> shipping_hsn, List<HSN> delivered_hsn, List<HSN> rejected_hsn, List<HSN> cancelled_hsn) {

        this.waiting_hsn = waiting_hsn;
        this.cooking_hsn = cooking_hsn;
        this.shipping_hsn = shipping_hsn;
        this.delivered_hsn = delivered_hsn;
        this.rejected_hsn = rejected_hsn;
        this.cancelled_hsn = cancelled_hsn;

        /* update all the fragments */
        frg_1_waiting = null;
        frg_6_cooking = null;
        frg_2_shipping = null;
        frg_3_delivered = null;
        frg_4_rejected = null;
//        frg_5_cancelled = null;
    }


    private boolean switchFragment(final int frgId) {

        boolean res = false;

        switch (frgId) {
            case WAITING:
                if (frg_1_waiting == null) {
                    frg_1_waiting = RestaurantSubHSNListFragment.instantiate(MyHSNActivity.this, waiting_hsn);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_waiting),  frg_1_waiting, WAITING);
                break;
            case COOKING:
                if (frg_6_cooking == null) {
                    frg_6_cooking = RestaurantSubHSNListFragment.instantiate(MyHSNActivity.this, cooking_hsn);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_cooking),  frg_6_cooking, COOKING);
                break;
            case SHIPPING:
                if (frg_2_shipping == null) {
                    frg_2_shipping = RestaurantSubHSNListFragment.instantiate(MyHSNActivity.this, shipping_hsn);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_shipping),  frg_2_shipping, SHIPPING);
                break;
            case DELIVERED:
                if (frg_3_delivered == null) {
                    frg_3_delivered = RestaurantSubHSNListFragment.instantiate(MyHSNActivity.this, delivered_hsn);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_delivered),  frg_3_delivered, DELIVERED);
                break;
//            case REJECTED:
            default:
                if (frg_4_rejected == null) {
                    frg_4_rejected = RestaurantSubHSNListFragment.instantiate(MyHSNActivity.this, delivered_hsn);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_rejected),  frg_4_rejected, REJECTED);
                break;
          /*  case CANCELLED:
                if (frg_5_cancelled == null) {
                    frg_5_cancelled = RestaurantSubHSNListFragment.instantiate(MyHSNActivity.this, cancelled_hsn);
                }
                res = performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_cancelled),  frg_5_cancelled, CANCELLED);
                break;*/
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
            presenter.loadActualHSNList();

            /* check what is the actual fragment that is being showed, and setup the valude with it. */
            updateCommandCount();
        }
    }

    private void updateCommandCount() {
        int items_count = 0;
        switch (previousFragmentCode) {
            case WAITING:
                items_count = (waiting_hsn == null ? 0 : waiting_hsn.size());
                break;
            case SHIPPING:
                items_count = (shipping_hsn == null ? 0 : shipping_hsn.size());
                break;
            case DELIVERED:
                items_count = (delivered_hsn == null ? 0 : delivered_hsn.size());
                break;
            case REJECTED:
                items_count = (rejected_hsn == null ? 0 : rejected_hsn.size());
                break;
        }
        setTopCountValue(items_count);
    }

    private void setTopCountValue(int items_count) {

        TextView tv_resto_hsn_count = findViewById(R.id.tv_resto_hsn_count);
        tv_resto_hsn_count.setText((items_count < 10 ? "0"+items_count : ""+items_count));
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
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
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
                presenter.loadActualHSNList();
                break;
        }
    }

    public void cancelHSN (int hsn_id) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.cancel_hsn_dialog_info, null, false);

        TextView tv_message = view.findViewById(R.id.tv_message);
        Button bt_cancel = view.findViewById(R.id.bt_cancel);
        Button bt_confirm = view.findViewById(R.id.bt_confirm);

        alertDialogBuilder.setView(view);

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
                presenter.cancelHSN(hsn_id);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void cancelHsnSuccessful(boolean isSuccessfull) {
        if (isSuccessfull) {
            // reload
//            presenter.loadActualHSNList();
            onRefresh();
        } else {
            // toast
        }
    }


    public void onHSNInteraction(HSN hsn) {
        Intent intent = new Intent(this, MyHSNDetailsActivity.class);
        intent.putExtra(MyHSNDetailsActivity.HSN_, hsn);
        startActivity(intent);
    }
}