package tg.tmye.kaba_i_deliver.activity.command;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver._commons.utils.UtilFunctions;
import tg.tmye.kaba_i_deliver.activity.command.contract.MyCommandContract;
import tg.tmye.kaba_i_deliver.activity.command.frag.RestaurantSubCommandListFragment;
import tg.tmye.kaba_i_deliver.activity.command.presenter.MyCommandsPresenter;
import tg.tmye.kaba_i_deliver.activity.delivery.DeliveryModeActivity;
import tg.tmye.kaba_i_deliver.activity.login.DeliverManLoginActivity;
import tg.tmye.kaba_i_deliver.activity.restaurant.RestaurantListActivity;
import tg.tmye.kaba_i_deliver.cviews.CustomProgressbar;
import tg.tmye.kaba_i_deliver.cviews.NoScrollViewPager;
import tg.tmye.kaba_i_deliver.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.data.delivery.KabaShippingMan;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;

import static tg.tmye.kaba_i_deliver.activity.command.CommandDetailsActivity.COMMAND_ITEM;


public class MyCommandsActivity extends AppCompatActivity implements MyCommandContract.View, RestaurantSubCommandListFragment.OnFragmentInteractionListener, SwipeRefreshLayout.OnRefreshListener, ViewPager.OnPageChangeListener {

    CommandRepository commandRepository;

    MyCommandsPresenter presenter;
    /* have an global update for the activity that applies to the sub fragments */

    /* views */
    NoScrollViewPager viewpager_commands;
    ProgressBar progressBar;
    TabLayout tablelayout_title_strip;
    TextView tv_no_food_message;
    SwipeRefreshLayout swiperefreshlayout;

    /* create a fragment for each slot */
    RestaurantSubCommandListFragment frg_waiting, frg_cooking, frg_delivering, frg_others;

    private CommandsViewpagerAdapter adapter;

    Map<Integer, Fragment> frg_map;

    private String[] command_top_titles;

    private Toolbar mToolbar;
    private View mRevealBackgroundView;
    private View mRevealView;
    private DeliveryManRepository deliveryManRepository;

    private LoadingDialogFragment loadingDialogFragment;

    TextView tv_delivery_man_name, tv_daily_delivery_fees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commands);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initViews();
        command_top_titles = getResources().getStringArray(R.array.delivery_status_list_small);
        /* when data is found, apply it to the fragment */
        commandRepository = new CommandRepository(this);
        presenter = new MyCommandsPresenter(this, commandRepository);
        /* subdivision is made like this - : waiting - cooking - into delivery - and the rest  */
        presenter.loadActualCommandsList();
        swiperefreshlayout.setOnRefreshListener(this);
        viewpager_commands.addOnPageChangeListener(this);

        /* check if user in shipping mode, if yes, jump directly to the view. */
        DeliveryManRepository.checkIsInDeliveryMode(this);
        deliveryManRepository = new DeliveryManRepository(this);
        deliveryManRepository.sendPushData();

        tv_delivery_man_name.setText(getDeliveryManName());

        /* create custom tabs for the viewpager */

        /* ajouter des textviews dans les tabhost pour pouvoir mettre les comptes */

        if (presenter != null)
            presenter.loadActualCommandsList();

        if (viewpager_commands != null && adapter != null && adapter.getCount() > lastPosition) {
            viewpager_commands.setCurrentItem(lastPosition);
            animateAppAndStatusBar(R.color.command_state_3, R.color.command_state_3, 0);
        }
    }

    private void initViews() {
        viewpager_commands = findViewById(R.id.viewpager_commands);
        progressBar = findViewById(R.id.progress_bar);
        tv_no_food_message = findViewById(R.id.tv_no_food_message);
        tablelayout_title_strip = findViewById(R.id.tablayout_vp_strip);
        swiperefreshlayout = findViewById(R.id.swiperefreshlayout);
        mRevealView = findViewById(R.id.collapsing_toolbar);
        mRevealBackgroundView = findViewById(R.id.appbarlayout);
        tv_delivery_man_name = findViewById(R.id.tv_delivery_man_name);
        tv_daily_delivery_fees = findViewById(R.id.tv_daily_delivery_fees);
        // disable viewpager scroll
    }

    @Override
    public void setPresenter(MyCommandContract.Presenter presenter) {}

    @Override
    public void showLoading(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swiperefreshlayout.setRefreshing(isLoading);
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                viewpager_commands.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                tv_no_food_message.setVisibility(View.GONE);

                /* show loading like fragment does it */
                showSuspendedLoading (isLoading);
            }
        });
    }

    public void showSuspendedLoading(final boolean isLoading) {

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

    int todayMoney = 0;

    @Override
    public void inflateCommands(final List<Command> preorders,final List<Command> wait_for_me, final List<Command> yet_to_ship, final List<Command> shipping_done) {

        todayMoney = 0;
        for (int i = 0; i < shipping_done.size(); i++) {
            if (shipping_done.get(i).is_email_account)
                todayMoney += (Integer.valueOf(shipping_done.get(i).shipping_pricing_minus_extra));
            else
                todayMoney += (Integer.valueOf(shipping_done.get(i).shipping_pricing));
        }

        for (int i = 0; i < preorders.size(); i++) {
            if (preorders.get(i).state == 3)
                todayMoney += (Integer.valueOf(preorders.get(i).shipping_pricing));
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tablelayout_title_strip.setupWithViewPager(viewpager_commands);

                /* init adapter and create the view */
//                initMenus();
                adapter = new CommandsViewpagerAdapter(getSupportFragmentManager());
                adapter.setData(preorders, wait_for_me, yet_to_ship, shipping_done);
                viewpager_commands.setOffscreenPageLimit(0);
                viewpager_commands.setAdapter(adapter);
                /* set the en livraison tab to default */
                viewpager_commands.setCurrentItem(1);
                /* animate from green to green */
                setUpCustomTabs (tablelayout_title_strip.getTabAt(0), tablelayout_title_strip.getTabAt(1), tablelayout_title_strip.getTabAt(2), tablelayout_title_strip.getTabAt(3));

                tv_tab_preorder_count.setText(""+(preorders != null ? preorders.size() : 0));
                tv_tab_waiting_count.setText(""+(wait_for_me != null ? wait_for_me.size() : 0));
                tv_tab_yet_count.setText(""+(yet_to_ship != null ? yet_to_ship.size() : 0));
                tv_tab_shipping_count.setText(""+(shipping_done != null ? shipping_done.size() : 0));

                tv_daily_delivery_fees.setText(UtilFunctions.intToMoney(todayMoney)+" F");
            }
        });


    }

    TextView tv_tab_preorder_count, tv_tab_yet_count, tv_tab_shipping_count, tv_tab_waiting_count;

    private void setUpCustomTabs(TabLayout.Tab preorder__tab, TabLayout.Tab waiting_tab, TabLayout.Tab yet_to_ship_tab, TabLayout.Tab shipping_done_tab) {

        /* inflate customviews each time */
        View preorder_view = LayoutInflater.from(this).inflate(R.layout.blue_tab_view, null, false);
        View waiting_count_view = LayoutInflater.from(this).inflate(R.layout.tab_view, null, false);
        View yet_to_view = LayoutInflater.from(this).inflate(R.layout.tab_view, null, false);
        View shipping_done_view = LayoutInflater.from(this).inflate(R.layout.tab_view, null, false);

        TextView tv_tab_preorder = preorder_view.findViewById(R.id.tv_tab_name);
        TextView tv_tab_waiting_name = waiting_count_view.findViewById(R.id.tv_tab_name);
        TextView tv_tab_yet_name = yet_to_view.findViewById(R.id.tv_tab_name);
        TextView tv_tab_shipping_name = shipping_done_view.findViewById(R.id.tv_tab_name);


        tv_tab_preorder.setText(command_top_titles[0].toUpperCase());
        tv_tab_waiting_name.setText(command_top_titles[1].toUpperCase());
        tv_tab_yet_name.setText(command_top_titles[2].toUpperCase());
        tv_tab_shipping_name.setText(command_top_titles[3].toUpperCase());


        tv_tab_preorder_count = preorder_view.findViewById(R.id.tv_tab_count);
        tv_tab_waiting_count = waiting_count_view.findViewById(R.id.tv_tab_count);
        tv_tab_yet_count = yet_to_view.findViewById(R.id.tv_tab_count);
        tv_tab_shipping_count = shipping_done_view.findViewById(R.id.tv_tab_count);

        preorder__tab.setCustomView(preorder_view);
        waiting_tab.setCustomView(waiting_count_view);
        yet_to_ship_tab.setCustomView(yet_to_view);
        shipping_done_tab.setCustomView(shipping_done_view);
    }


    @Override
    public void onCommandInteraction(Command command) {

        /* start this activity -  */
        Intent intent = new Intent(this, CommandDetailsActivity.class);
//        intent.putExtra(CommandDetailsActivity.ID, food.id);
        intent.putExtra(COMMAND_ITEM, command);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        animateAppAndStatusBar(R.color.command_state_3, R.color.command_state_3, 0);
        frg_map = new HashMap<>();
        viewpager_commands.removeAllViews();
        presenter.loadActualCommandsList();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    int lastPosition = -1;

    @Override
    public void onPageSelected(int position) {
        /* from to */
        animateAppAndStatusBar(lastPosition != -1 ? colors[lastPosition] : R.color.command_state_3, colors[position], lastPosition > position ? 1: 0);
        lastPosition = position;
    }

    int[] colors = {R.color.colorPrimary_yellow, R.color.command_state_4, R.color.command_state_yet, R.color.command_state_done};

    @Override
    public void onPageScrollStateChanged(int state) {}

    public String getDeliveryManName() {

        KabaShippingMan shippingMan = deliveryManRepository.getShippingMan(this);
        String name = shippingMan.name;
        return name;
    }

    private class CommandsViewpagerAdapter extends FragmentStatePagerAdapter {

        private List<Command> yet_to_ship;
        private List<Command> shipping_done;
        private List<Command> wait_for_me;
        private List<Command> preorders;

        public CommandsViewpagerAdapter(FragmentManager fm) {
            super(fm);
            frg_map = new HashMap<>();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {

            if (frg_map == null)
                frg_map = new HashMap<>();

            List<Command> data_command_list = null;
            /* according to position, set the data */

            switch (position) {
                case 0:
                    data_command_list = preorders;
                    break;
                case 1:
                    data_command_list = wait_for_me;
                    break;
                case 2:
                    data_command_list = yet_to_ship;
                    break;
                case 3:
                    data_command_list = shipping_done;
                    break;
            }

            frg_map.put(position, RestaurantSubCommandListFragment.instantiate(MyCommandsActivity.this,
                    data_command_list));
            return frg_map.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return command_top_titles[position].toUpperCase();
        }

        @Override
        public int getCount() {
            return command_top_titles.length;
        }

        public void setData(List<Command> preorders, List<Command> wait_for_me, List<Command> yet_to_ship, List<Command> shipping_done) {

            this.preorders = preorders;
            this.wait_for_me = wait_for_me;
            this.yet_to_ship = yet_to_ship;
            this.shipping_done = shipping_done;
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null)
            presenter.loadActualCommandsList();

        if (viewpager_commands != null && adapter != null && adapter.getCount() > lastPosition) {
            viewpager_commands.setCurrentItem(lastPosition);
            animateAppAndStatusBar(R.color.command_state_3, R.color.command_state_3, 0);
        }
    }

    /* mReveal view -> appbar ;;
     * mtoolbar */

    private void animateAppAndStatusBar(int fromColor, final int toColor, int direction) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(toColor);
        }

        if (mRevealBackgroundView.isAttachedToWindow()) {
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    mRevealView,
                    mToolbar.getWidth() * direction,
                    mToolbar.getHeight(),
                    0, // ??
                    mToolbar.getWidth() / 2);

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mRevealView.setBackgroundColor(getResources().getColor(toColor));
                }
            });

            /*mRevealBackgroundView*/
            mRevealBackgroundView.setBackgroundColor(getResources().getColor(fromColor));
            animator.setStartDelay(200);
            animator.setDuration(125);
            animator.start();
            mRevealView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_commands_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*  */
        switch(item.getItemId()) {
//            case R.id.action_enter_delivery_mode:
//                enterDeliveryMode();
//                break;
            case R.id.action_logout:
                /* clear logout and start the task bottom activity */
                logout();
                break;
            case R.id.action_restaurants:
                jumpToRestaurants();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void jumpToRestaurants() {
        Intent intent = new Intent(this, RestaurantListActivity.class);
        startActivity(intent);
    }


    private void logout() {
        DeliveryManRepository.deleteDelivermanInfos(this);
        DeliveryManRepository.deleteToken(this);
        // restart home activity
        startActivity(new Intent(this, DeliverManLoginActivity.class));
        finish();
    }


    private void enterDeliveryMode() {

        /* save this value inside the sharedpreference and check it each time we get into
         * the mycommand activity. */
        startActivity(new Intent(this, DeliveryModeActivity.class));
        finish();
    }

}


