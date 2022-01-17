package tg.tmye.kaba.partner.activities.stats;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner._commons.adapter.StatsRecyclerAdapter;
import tg.tmye.kaba.partner._commons.decorator.StatsListSpacesItemDecoration;
import tg.tmye.kaba.partner.activities.stats.contract.RestaurantStatsContract;
import tg.tmye.kaba.partner.activities.stats.presenter.StatsPresenter;
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.partner.data._OtherEntities.StatsEntity;
import tg.tmye.kaba.partner.data._OtherEntities.source.StatsRepository;

public class StatsActivity extends AppCompatActivity implements RestaurantStatsContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    RecyclerView recyclerView;

//    SwipeRefreshLayout swiperefreshlayout;

    StatsPresenter presenter;

    private StatsRepository repo;

    TextView tv_no_data_message;

    private StatsListSpacesItemDecoration statsListDecorator;

    Button bt_ok;
    TextView tv_from_date;
    TextView tv_to_date;

    Date fromDate = new Date(), toDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        /**/
        initViews();

        tv_from_date.setText(dateToString(fromDate));
        tv_to_date.setText(dateToString(toDate));

        bt_ok.setOnClickListener(this);
        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);

        repo = new StatsRepository(this);

        presenter = new StatsPresenter(this, repo);

        presenter.start();
    }

    private String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return dateFormat.format(date);
    }

    private void initViews() {

        recyclerView = findViewById(R.id.recyclerview);
//        swiperefreshlayout = findViewById(R.id.swiperefreshlayout);
        tv_no_data_message = findViewById(R.id.tv_no_data_message);

        bt_ok = findViewById(R.id.bt_ok);

        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private LoadingDialogFragment loadingDialogFragment;

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
                tv_no_data_message.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
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

    private void showFragment(DialogFragment dialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(dialogFragment, tag);
        else
            ft.show(dialogFragment);
        ft.commitAllowingStateLoss();
    }


    @Override
    public void networkError() {

        /* network error */
    }

    @Override
    public void syserror() {

        /* sys error */
    }

    @Override
    public void inflateStats(final List<StatsEntity> statsEntities) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (statsEntities != null && statsEntities.size() == 0) {
                    /* nothing to show */
                    recyclerView.setVisibility(View.GONE);
                    tv_no_data_message.setVisibility(View.VISIBLE);
                } else {
                    tv_no_data_message.setVisibility(View.GONE);
                    initRecyclerview(statsEntities);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecyclerview(List<StatsEntity> statsEntities) {


        if (statsListDecorator == null) {
            statsListDecorator = new StatsListSpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                    getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
            );
        }

        recyclerView.removeAllViews();

        if (recyclerView.getItemDecorationCount() == 0)
            recyclerView.addItemDecoration(statsListDecorator);

        /* according */
        tv_no_data_message.setVisibility(View.GONE);
        StatsRecyclerAdapter adapter = new StatsRecyclerAdapter(this, statsEntities);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       recyclerView.setVisibility(View.VISIBLE);
                   }
               });
            }
        }, 1000);

    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onRefresh() {
        presenter.load7LastDaysStats();
    }

    public String getFromDate() {
        String fromDate = tv_from_date.getText().toString();
        return fromDate;
    }

    public String getToDate() {
        String toDate = tv_to_date.getText().toString();
        return toDate;
    }

    private static void mToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                recyclerView.setVisibility(View.GONE);
                String fromDate = getFromDate();
                String toDate = getToDate();
                // control date
                try {
                    Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                    Date tDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                    if (fDate.compareTo(tDate) < 1) {
                        // good
                        presenter.searchStaticsFromToDate(getFromDate(), getToDate());
                    } else {
                        // time error
                        mToast(getBaseContext(), "TIME ERROR");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    mToast(getBaseContext(), "TIME ERROR");
                }

                break;
            case R.id.tv_from_date:
                // pick from date
                showDatePickerDialog("tv_from_date");
                break;
            case R.id.tv_to_date:
                // pick to date
                showDatePickerDialog("tv_to_date");
                break;
        }
    }

    public void showDatePickerDialog(String tag) {
        DialogFragment newFragment = DatePickerFragment.newInstance("tv_to_date" == tag ? getToDate(): getFromDate());
        newFragment.show(getSupportFragmentManager(), tag);
    }

    private void setToDate(String date) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_to_date.setText(date);
            }
        });
    }

    private void setFromDate(String date) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_from_date.setText(date);
            }
        });
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private String date;

        public static DatePickerFragment newInstance(String date) {

            Bundle args = new Bundle();
            args.putString("date", date);
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.date = getArguments().getString("date");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker


            int year = Integer.valueOf(date.split("-")[0]);
            int month = Integer.valueOf(date.split("-")[1]) -1;
            int day = Integer.valueOf(date.split("-")[2]);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert,this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            month+=1;
            switch (getTag()){
                case "tv_to_date":
                    ((StatsActivity)getActivity()).setToDate(year+"-"+(month < 10 ? "0":"")+month+"-"+(day < 10 ? "0":"")+day);
                    break;
                case "tv_from_date":
                    ((StatsActivity)getActivity()).setFromDate(year+"-"+(month < 10 ? "0":"")+month+"-"+(day < 10 ? "0":"")+day);
                    break;
            }
        }
    }
}
