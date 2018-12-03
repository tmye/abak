package tg.tmye.kaba_i_deliver.activity.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver._commons.adapter.CommandRecyclerAdapter;
import tg.tmye.kaba_i_deliver._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba_i_deliver.activity.command.CommandDetailsActivity;
import tg.tmye.kaba_i_deliver.activity.command.MyCommandsActivity;
import tg.tmye.kaba_i_deliver.activity.command.frag.RestaurantSubCommandListFragment;
import tg.tmye.kaba_i_deliver.activity.delivery.contract.DeliveryModeContract;
import tg.tmye.kaba_i_deliver.activity.delivery.presenter.DeliverModePresenter;
import tg.tmye.kaba_i_deliver.cviews.OffRecyclerview;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;

public class DeliveryModeActivity extends AppCompatActivity implements DeliveryModeContract.View, SwipeRefreshLayout.OnRefreshListener,
        RestaurantSubCommandListFragment.OnFragmentInteractionListener{

    DeliveryManRepository repository;
    private DeliveryModeContract.Presenter presenter;

    OffRecyclerview recyclerView;
    TextView tv_no_food_message;
    SwipeRefreshLayout refreshLayout;
    NestedScrollView nestedScrollView;
    ProgressBar progress_bar;
    TextView tv_deliverymode_command_count;

    private CommandListSpacesItemDecoration commandListDecorator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_mode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        repository = new DeliveryManRepository(this);
        presenter = new DeliverModePresenter(this, repository);
        presenter.start();

        refreshLayout.setOnRefreshListener(this);
    }


    private void initViews() {
        refreshLayout = findViewById(R.id.swiperefreshlayout);
        recyclerView = findViewById(R.id.recyclerview);
        tv_no_food_message = findViewById(R.id.tv_no_food_message);
        progress_bar = findViewById(R.id.progress_bar);
        nestedScrollView = findViewById(R.id.nestedscrollview);
        tv_deliverymode_command_count = findViewById(R.id.tv_deliverymode_command_count);
//        android:text="@string/there_is_no_food"
    }

    @Override
    public void onRefresh() {
        presenter.loadDistanceOptimizedDeliverList();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        presenter.loadDistanceOptimizedDeliverList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.delivery_mode_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*  */
        switch(item.getItemId()) {
            case R.id.action_exit_delivery_mode:
                exitDeliveryMode();
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exitDeliveryMode() {
        DeliveryManRepository.exitDeliveryMode(this, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                networkError();
            }

            @Override
            public void onSysError() {
                // error
                sysError();
            }

            @Override
            public void onSuccess(String jsonResponse) {
                /* success */
                mSnack(recyclerView, getResources().getString(R.string.no_command_to_ship));
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DeliveryModeActivity.this, MyCommandsActivity.class));
                        finish();
                    }
                }, 2000);
            }
        });
    }

    private void mSnack(View view, String message) {
        Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void networkError() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_no_food_message.setText(getResources().getString(R.string.network_error));
                recyclerView.setVisibility(View.GONE);
                tv_no_food_message.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void sysError() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_no_food_message.setText(getResources().getString(R.string.sys_error));
                recyclerView.setVisibility(View.GONE);
                tv_no_food_message.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                refreshLayout.setRefreshing(isLoading);
                progress_bar.setVisibility(isLoading ? View.VISIBLE : View.GONE);

                tv_no_food_message.setVisibility(View.GONE);
                recyclerView.setVisibility(!isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void inflateCommandList(final List<Command> commands) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (commands == null || commands.size() == 0) {
                    exitDeliveryMode();
                    return;
                }

                if (commandListDecorator == null) {
                    commandListDecorator = new CommandListSpacesItemDecoration(
                            getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                            getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
                    );
                }

                if (recyclerView.getItemDecorationCount() == 0)
                    recyclerView.addItemDecoration(commandListDecorator);

                /* according */
                if (commands != null && commands.size() > 0) {
                    tv_no_food_message.setVisibility(View.GONE);
                    CommandRecyclerAdapter adapter = new CommandRecyclerAdapter(DeliveryModeActivity.this, commands);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(DeliveryModeActivity.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(adapter);
                } else {
                    tv_no_food_message.setText(getResources().getString(R.string.there_is_no_food));
                    tv_no_food_message.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                tv_deliverymode_command_count.setText(formatNo(commands == null ? 0 : commands.size()));
            }
        });
    }


    /*
     *
     * Function to format the number so that :
     * 6 be written : 06
     * 12 be written: 12
     * ...
     * */
    private String formatNo(int no) {
        String numb = "";
        if (no < 10) {
            numb+="0";
        }
        numb += (no+"");
        return numb;
    }


    @Override
    public void onCommandInteraction(Command food) {

        /* start this activity -  */
        Intent intent = new Intent(this, CommandDetailsActivity.class);
        intent.putExtra(CommandDetailsActivity.ID, food.id);
        startActivity(intent);
    }

    @Override
    public void setPresenter(DeliveryModeContract.Presenter presenter) {

    }

}
