package tg.tmye.kaba_i_deliver.activity.statistics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba_i_deliver.activity.statistics.contract.StatisticsContract;
import tg.tmye.kaba_i_deliver.activity.statistics.presenter.StatisticPresenter;
import tg.tmye.kaba_i_deliver.cviews.dialog.InfoDialogFragment;
import tg.tmye.kaba_i_deliver.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.data.stats.StatisticResults;
import tg.tmye.kaba_i_deliver.syscore.ILog;

public class StatisticsActivity extends AppCompatActivity implements StatisticsContract.View, View.OnClickListener {

    private LoadingDialogFragment loadingDialogFragment;

    StatisticPresenter presenter;
    private CommandRepository repository;

    Button bt_ok;
    TextView tv_from_date;
    TextView tv_to_date;
    RecyclerView recyclerView;

    Date fromDate = new Date(), toDate = new Date();

    List<StatisticResults> data;

    StatisticAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        setTitle(getString(R.string.stats_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);


        repository = new CommandRepository(this);
        presenter = new StatisticPresenter(this, repository);

        bt_ok = findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);

        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        recyclerView = findViewById(R.id.recyclerview);

        tv_to_date.setOnClickListener(this);
        tv_from_date.setOnClickListener(this);

        tv_from_date.setText(dateToString(fromDate));
        tv_to_date.setText(dateToString(toDate));

        data = new ArrayList<>();

        adapter = new StatisticAdapter(this, data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        HistoricListSpacesItemDecoration historicListSpacesItemDecoration = new HistoricListSpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                    getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
            );

        if (recyclerView.getItemDecorationCount() == 0)
            recyclerView.addItemDecoration(historicListSpacesItemDecoration);

        recyclerView.setAdapter(adapter);

        presenter.searchStaticsFromToDate(null, null);
    }


    private String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return dateFormat.format(date);
    }

    @Override
    public void showLoading(boolean isLoading) {
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

    private static void mToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void networkError() {
        mToast(this, getString(R.string.network_error));
    }

    @Override
    public void syserror() {
        mToast(this, getString(R.string.sys_error));
    }

    @Override
    public void showStatisticsResult(List<StatisticResults> data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data.size() == 0) {
                    mToast(StatisticsActivity.this,"Not results");
                } else {
                    adapter.setData(data);
                }
            }
        });
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                String fromDate = getFromDate();
                String toDate = getToDate();
                // control date
                try {
                    Date fDate=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                    Date tDate=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                    if (fDate.compareTo(tDate) < 1){
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(String tag) {
        DialogFragment newFragment = DatePickerFragment.newInstance("tv_to_date" == tag ? getToDate(): getFromDate());
        newFragment.show(getSupportFragmentManager(), tag);
    }

    private String getFromDate() {
        String fromDate = tv_from_date.getText().toString();
        return fromDate;
    }

    private String getToDate() {
        String toDate = tv_to_date.getText().toString();
        return toDate;
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
                    ((StatisticsActivity)getActivity()).setToDate(year+"-"+(month < 10 ? "0":"")+month+"-"+(day < 10 ? "0":"")+day);
                    break;
                case "tv_from_date":
                    ((StatisticsActivity)getActivity()).setFromDate(year+"-"+(month < 10 ? "0":"")+month+"-"+(day < 10 ? "0":"")+day);
                    break;
            }
        }
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

    class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {

        private final Context ctx;
        private List<StatisticResults> data;

        public StatisticAdapter (Context ctx, List<StatisticResults> results) {
            this.ctx = ctx;
            this.data = results;
        }

        @NonNull
        @Override
        public StatisticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.statistic_list_item, null, false));
        }

        @Override
        public void onBindViewHolder(@NonNull StatisticAdapter.ViewHolder holder, int position) {
            StatisticResults results = data.get(position);
            holder.tv_date_content.setText(results.date);

            holder.tv_command_ca_arrival.setText(results.ca_app_pa);
            holder.tv_command_count_arrival.setText(results.nb_app_command_pa);

            holder.tv_command_ca_prepaid.setText(results.ca_app_pp);
            holder.tv_command_count_prepaid.setText(results.nb_app_command_pp);

            holder.tv_command_total_ca.setText(results.ca);
            holder.tv_command_total_count.setText(results.nb_command);

            holder.tv_hsn_ca.setText(results.ca_hsn);
            holder.tv_hsn_count.setText(results.nb_hsn);
        }

        @Override
        public int getItemCount() {
            return this.data.size();
        }

        public void setData(List<StatisticResults> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_date_content,

                    tv_command_total_ca,
                    tv_command_total_count,

                    tv_command_ca_arrival,
                            tv_command_count_arrival,
                            tv_command_ca_prepaid,
                            tv_command_count_prepaid,
                    tv_hsn_ca,
                    tv_hsn_count;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.tv_date_content = itemView.findViewById(R.id.tv_date_content);
                this.tv_command_total_ca = itemView.findViewById(R.id.tv_command_total_ca);
                this.tv_command_total_count = itemView.findViewById(R.id.tv_command_total_count);
                this.tv_command_ca_prepaid = itemView.findViewById(R.id.tv_command_ca_prepaid);
                this.tv_command_count_prepaid = itemView.findViewById(R.id.tv_command_count_prepaid);
                this.tv_command_ca_arrival = itemView.findViewById(R.id.tv_command_ca_arrival);
                this.tv_command_count_arrival = itemView.findViewById(R.id.tv_command_count_arrival);
                this.tv_hsn_ca = itemView.findViewById(R.id.tv_hsn_ca);
                this.tv_hsn_count = itemView.findViewById(R.id.tv_hsn_count);
            }
        }
    }

    private class HistoricListSpacesItemDecoration  extends RecyclerView.ItemDecoration {

            private final int bottom;
            private final int top;

            public HistoricListSpacesItemDecoration(int top, int bottom) {
                this.top = top;
                this.bottom = bottom;
            }


            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {

                outRect.top = top;
                outRect.left = top;
                outRect.right = top;

                if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1) {
                    outRect.bottom = bottom;
                }
            }

    }
}