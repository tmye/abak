package tg.tmye.kaba.activity.command;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.CommandRecyclerAdapter;
import tg.tmye.kaba._commons.cviews.custom_swipe.HomeCustom_SwipeRefreshLayout;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba.activity.command.contract.CommandHistoryContract;
import tg.tmye.kaba.activity.command.presenter.CommandHistoryPresenter;
import tg.tmye.kaba.activity.home.views.fragment.F_Commands_3_Fragment;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.data.command.source.CommandRepository;

public class CommandHistoryActivity extends AppCompatActivity implements CommandHistoryContract.View, SwipeRefreshLayout.OnRefreshListener,
        F_Commands_3_Fragment.OnFragmentInteractionListener, View.OnClickListener {


    RecyclerView recyclerView;

    TextView tv_message;

    LinearLayout lny_error_box;

    HomeCustom_SwipeRefreshLayout swipeRefreshLayout;

    List<Command> all_commands;

    Button bt_tryagain;

//   a m o r o 93 70 30 50

    CommandHistoryPresenter presenter;

    CommandListSpacesItemDecoration commandListDecorator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        /* load data from recyclervie */
        initViews();
        swipeRefreshLayout.setOnRefreshListener(this);
        bt_tryagain.setOnClickListener(this);
        /* launch presenter to send the stuff */
        CommandRepository commandRepo = new CommandRepository(this);
        presenter = new CommandHistoryPresenter(this, commandRepo);
        presenter.loadCommand();
    }

    private void initViews() {

        tv_message = findViewById(R.id.tv_message);
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        lny_error_box = findViewById(R.id.lny_error_box);
        bt_tryagain = findViewById(R.id.bt_tryagain);
    }

    @Override
    public void inflateList(final List<Command> commands) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (commands == null || commands.size() == 0) {
                    lny_error_box.setVisibility(View.VISIBLE);
                    tv_message.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tv_message.setText(getResources().getString(R.string.no_data));
                }

                /**/
                lny_error_box.setVisibility(View.GONE);
                tv_message.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (all_commands == null || all_commands.size() == 0) {
                    all_commands = commands;
                } else {
                    all_commands.addAll(commands);
                }

                if (adapter == null) {
                    adapter = new CommandRecyclerAdapter(CommandHistoryActivity.this, all_commands);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommandHistoryActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    if (commandListDecorator == null) {
                        commandListDecorator = new CommandListSpacesItemDecoration(
                                getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                                getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
                        );
                    }
                    if (recyclerView.getItemDecorationCount() == 0)
                        recyclerView.addItemDecoration(commandListDecorator);

                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyItemInserted(all_commands.size() - commands.size());
                }
            }
        });

        /* once we reach bottom, work on it. */
    }

    CommandRecyclerAdapter adapter;

    @Override
    public void onNetworkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(getResources().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void onSysEror() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(getResources().getString(R.string.sys_error));
            }
        });
    }

    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tv_message.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(isLoading);
            }
        });
    }

    @Override
    public void setPresenter(CommandHistoryContract.Presenter presenter) {

    }

    @Override
    public void onRefresh() {
        all_commands = null;
        presenter.loadCommand();
    }

    @Override
    public void onCommandInteraction(Command command) {
        Intent intent = new Intent(this, CommandDetailsActivity.class);
        intent.putExtra(CommandDetailsActivity.ID, command.id);
        startActivity(intent);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                if (presenter != null)
                    presenter.loadCommand();
                break;
        }
    }

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }
}
