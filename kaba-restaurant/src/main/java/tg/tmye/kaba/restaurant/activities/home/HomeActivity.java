package tg.tmye.kaba.restaurant.activities.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import tg.tmye.kaba.restaurant._commons.utils.UtilFunctions;
import tg.tmye.kaba.restaurant.activities.commands.MyCommandsActivity;
import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.activities.commands.contract.MyCommandContract;
import tg.tmye.kaba.restaurant.activities.commands.presenter.MyCommandsPresenter;
import tg.tmye.kaba.restaurant.activities.login.RestaurantLoginActivity;
import tg.tmye.kaba.restaurant.activities.stats.StatsActivity;
import tg.tmye.kaba.restaurant.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.restaurant.data.Restaurant.source.RestaurantOnlineRepository;
import tg.tmye.kaba.restaurant.data.command.source.CommandRepository;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.GlideApp;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, MyCommandContract.HomePageView {

    private RestaurantOnlineRepository restaurantOnlineRepository;

    MyCommandsPresenter myCommandsPresenter;

    private CommandRepository commandRepo;

    private LoadingDialogFragment loadingDialogFragment;

    private TextView tv_money, tv_quantity;

    CardView bt_my_commands, bt_weekly_stats;

    private TextView tv_header_resto_name;
    ImageView header_resto_cic;

    private TextView tv_state_opend;

    private TextView tv_waiting_count, tv_cooking_count, tv_shipping_count;

    LinearLayout lny_1_waiting, lny_2_preparing, lny_3_shipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* check if logged in, if not gerra here */
        initViews();

        bt_my_commands.setOnClickListener(this);
        bt_weekly_stats.setOnClickListener(this);

        lny_1_waiting.setOnClickListener(this);
        lny_2_preparing.setOnClickListener(this);
        lny_3_shipping.setOnClickListener(this);

        restaurantOnlineRepository = new RestaurantOnlineRepository(this);
        commandRepo = new CommandRepository(this);
        myCommandsPresenter = new MyCommandsPresenter(this, commandRepo);
        restaurantOnlineRepository.sendPushData();

        myCommandsPresenter.loadStats();
    }

    private void initViews() {
        bt_my_commands = findViewById(R.id.bt_my_commands);

        tv_quantity = findViewById(R.id.tv_quantity);
        tv_money = findViewById(R.id.tv_money);

        bt_my_commands = findViewById(R.id.bt_my_commands);
        bt_weekly_stats = findViewById(R.id.bt_weekly_stats);

        header_resto_cic = findViewById(R.id.header_resto_cic);
        tv_header_resto_name = findViewById(R.id.tv_header_resto_name);
        tv_state_opend = findViewById(R.id.tv_state_opend);

        tv_waiting_count = findViewById(R.id.tv_waiting_count);
        tv_cooking_count = findViewById(R.id.tv_cooking_count);
        tv_shipping_count = findViewById(R.id.tv_shipping_count);

        lny_1_waiting = findViewById(R.id.lny_1_waiting);
        lny_2_preparing = findViewById(R.id.lny_2_preparing);
        lny_3_shipping = findViewById(R.id.lny_3_shipping);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_my_commands:
                showMyCommands();
                break;
            case R.id.bt_weekly_stats:
                showStats();
                break;
            case R.id.lny_2_preparing:
                showMyCommands(1);
                break;
            case R.id.lny_1_waiting:
                showMyCommands(0);
                break;
            case R.id.lny_3_shipping:
                showMyCommands(2);
                break;
        }
    }

    private void showStats() {
        /* open my commands activity */
        Intent intent = new Intent(HomeActivity.this, StatsActivity.class);
        startActivity(intent);
    }

    private void showMyCommands() {
        /* open my commands activity */
       showMyCommands(0); // waiting
    }

    private void showMyCommands(int destination) {
        /* open my commands activity */
        Intent intent = new Intent(HomeActivity.this, MyCommandsActivity.class);
        intent.putExtra(MyCommandsActivity.DESTINATION, destination);
        startActivity(intent);
    }

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
    public void inflateStats (final int is_opened, final String head_pic, final String resto_name, final String quantity_count, final String amount_money) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_money.setText(UtilFunctions.intToMoney(amount_money)+" FCFA");
                tv_quantity.setText(quantity_count);

                tv_header_resto_name.setText(resto_name);
                GlideApp.with(HomeActivity.this)
                        .load(Constant.SERVER_ADDRESS+ "/" +head_pic)
                        .transition(GenericTransitionOptions.with(  ((MyRestaurantApp)getApplicationContext()).getGlideAnimation()  ))
                        .placeholder(R.drawable.placeholder_kaba)
                        .centerCrop()
                        .into(header_resto_cic);

                if (is_opened == 1) {
                    /* open*/
                    tv_state_opend.setTextColor(Color.WHITE);
                    tv_state_opend.setText(getResources().getText(R.string.is_opened));
                    tv_state_opend.setBackgroundResource(R.drawable.bg_green_rounded);
                } else {
                    /* closed */
                    tv_state_opend.setTextColor(Color.WHITE);
                    tv_state_opend.setText(getResources().getText(R.string.is_closed));
                    tv_state_opend.setBackgroundResource(R.drawable.bg_resto_closed_rounded);
                }
            }
        });
    }

    @Override
    public void sysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_money.setText(" --- FCFA");
                tv_quantity.setText("---");
            }
        });
    }

    @Override
    public void networkError() {
    }

    @Override
    public void inflateCountStats(final int waiting_count, final int shipping_count, final int cooking_count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_waiting_count.setText(""+waiting_count);
                tv_cooking_count.setText(""+cooking_count);
                tv_shipping_count.setText(""+shipping_count);
            }
        });
    }

    @Override
    public void setPresenter(MyCommandContract.Presenter presenter) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myCommandsPresenter != null)
            myCommandsPresenter.loadStats();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
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

}
