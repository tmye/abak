package tg.tmye.kaba_i_deliver.activity.dailyreport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.dailyreport.contract.DailyReportHistoryContract;
import tg.tmye.kaba_i_deliver.activity.dailyreport.presenter.DailyReportHistoryPresenter;
import tg.tmye.kaba_i_deliver.activity.delivery.contract.DeliveryModeContract;
import tg.tmye.kaba_i_deliver.cviews.dialog.DailyReportEditConfirmationDialog;
import tg.tmye.kaba_i_deliver.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba_i_deliver.data.dailyreport.DailyReport;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;

public class DailyReportHistoryActivity extends AppCompatActivity implements View.OnClickListener, DailyReportHistoryContract.View {

    /* card list to show reports */

    private LoadingDialogFragment loadingDialogFragment;

    DailyReportHistoryPresenter presenter;
    private DeliveryManRepository repository;

    Button bt_ok;
    TextView tv_from_date;
    TextView tv_to_date;
    RecyclerView recyclerView;

    String fromDate = "";
    String toDate = "";

    List<DailyReport> data;

    DailyReportHistoryActivity.DailyReportHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report_history);

        setTitle(getString(R.string.daily_report_history));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        repository = new DeliveryManRepository(this);
        presenter = new DailyReportHistoryPresenter(this, repository);

        bt_ok = findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);

        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        recyclerView = findViewById(R.id.recyclerview);

        tv_to_date.setOnClickListener(this);
        tv_from_date.setOnClickListener(this);

        tv_from_date.setText(currentDateToString(null));
        tv_to_date.setText(currentDateToString(null));

        data = new ArrayList<>();

        adapter = new DailyReportHistoryActivity.DailyReportHistoryAdapter(this, data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DailyReportHistoryActivity.HistoricListSpacesItemDecoration historicListSpacesItemDecoration = new DailyReportHistoryActivity.HistoricListSpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
        );

        if (recyclerView.getItemDecorationCount() == 0)
            recyclerView.addItemDecoration(historicListSpacesItemDecoration);

        recyclerView.setAdapter(adapter);

        presenter.searchHistoryFromToDate(null, null);
    }

    private String currentDateToString(@Nullable Date currentDate) {
        DateFormat dateFormat = new SimpleDateFormat("y-M-d");
        return dateFormat.format(currentDate == null ? new Date() : currentDate);
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
    public void sysError() {
        mToast(this, getString(R.string.sys_error));
    }

    @Override
    public void showDailyReportHistorysResult(List<DailyReport> data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data.size() == 0) {
                    mToast(DailyReportHistoryActivity.this,"Not results");
                } else {
                    adapter.setData(data);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                  this.fromDate = getFromDate();
                  this.toDate = getToDate();
                // control date
                try {
                    Date fDate=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                    Date tDate=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                    if (fDate.compareTo(tDate) < 1){
                        // good
                        presenter.searchHistoryFromToDate(getFromDate(), getToDate());
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
        DialogFragment newFragment = DailyReportHistoryActivity.DatePickerFragment.newInstance("tv_to_date" == tag ? getToDate(): getFromDate());
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

    @Override
    public void setPresenter(DeliveryModeContract.Presenter presenter) {

    }

    public void editReport(DailyReport report) {
        // start editing activity
        Intent intent = new Intent(DailyReportHistoryActivity.this, DailyReportActivity.class);
        intent.putExtra("report", report);
        startActivity(intent);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private String date;

        public static DailyReportHistoryActivity.DatePickerFragment newInstance(String date) {

            Bundle args = new Bundle();
            args.putString("date", date);
            DailyReportHistoryActivity.DatePickerFragment fragment = new DailyReportHistoryActivity.DatePickerFragment();
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
                    ((DailyReportHistoryActivity)getActivity()).setToDate(year+"-"+(month < 10 ? "0":"")+month+"-"+(day < 10 ? "0":"")+day);
                    break;
                case "tv_from_date":
                    ((DailyReportHistoryActivity)getActivity()).setFromDate(year+"-"+(month < 10 ? "0":"")+month+"-"+(day < 10 ? "0":"")+day);
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

    @Override
    protected void onRestart() {
        super.onRestart();
    if (presenter != null) {

    }
    }

    class DailyReportHistoryAdapter extends RecyclerView.Adapter<DailyReportHistoryActivity.DailyReportHistoryAdapter.ViewHolder> {

        private final Context ctx;
        private List<DailyReport> data;

        public DailyReportHistoryAdapter (Context ctx, List<DailyReport> results) {
            this.ctx = ctx;
            this.data = results;
        }

        @NonNull
        @Override
        public DailyReportHistoryActivity.DailyReportHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DailyReportHistoryActivity.DailyReportHistoryAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.report_list_item, null, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DailyReportHistoryActivity.DailyReportHistoryAdapter.ViewHolder holder, int position) {
            DailyReport dailyReport = data.get(position);
            holder.tv_communication_credit.setText(String.valueOf(dailyReport.communicationCredit));
            holder.tv_fuel_amount.setText(String.valueOf(dailyReport.fuelAmount));
            holder.tv_losses_amount.setText(String.valueOf(dailyReport.lossAmount));
            holder.tv_parking_amount.setText(String.valueOf(dailyReport.parkingAmount));
            holder.tv_reparation_amount.setText(String.valueOf(dailyReport.reparationAmount));
            holder.tv_various_fees.setText(String.valueOf(dailyReport.various));
            holder.tv_date_content.setText(String.valueOf(dailyReport.makeAt));

            holder.itemView.setOnClickListener(new DailyReportHistoryActivity.DailyReportOnclickListener(dailyReport));
        }

        @Override
        public int getItemCount() {
            return this.data.size();
        }

        public void setData(List<DailyReport> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_date_content,
                    tv_fuel_amount,
                    tv_communication_credit,
                    tv_reparation_amount,
                    tv_losses_amount,
                    tv_parking_amount,
                    tv_various_fees
                            ;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.tv_date_content = itemView.findViewById(R.id.tv_date_content);
                this.tv_fuel_amount = itemView.findViewById(R.id.tv_fuel_amount);
                this.tv_communication_credit = itemView.findViewById(R.id.tv_communication_credit);
                this.tv_reparation_amount = itemView.findViewById(R.id.tv_reparation_amount);
                this.tv_losses_amount = itemView.findViewById(R.id.tv_losses_amount);
                this.tv_parking_amount = itemView.findViewById(R.id.tv_parking_amount);
                this.tv_various_fees = itemView.findViewById(R.id.tv_various_fees);
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

    private class DailyReportOnclickListener implements View.OnClickListener {
        private final DailyReport dailyReport;

        public DailyReportOnclickListener(DailyReport dailyReport) {
            this.dailyReport = dailyReport;
        }

        @Override
        public void onClick(View v) {
            // confirm with a dialog if you want to modify this one
            if (dailyReport.isVerify){
                // cannot modify because accepted
                AlertDialog.Builder builder = new AlertDialog.Builder(DailyReportHistoryActivity.this, R.style.AlertDialogTheme);
                builder
                        .setTitle(R.string.daily_report)
                        .setMessage(getString(R.string.daily_report_already_confirmed, dailyReport.makeAt))
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                // Create the AlertDialog object and return it
                 builder.create();
            } else {
                // can verify
//                DailyReportEditConfirmationDialog
                DailyReportEditConfirmationDialog dailyReportEditConfirmationDialog = DailyReportEditConfirmationDialog.newInstance(this.dailyReport);
                showFragment(dailyReportEditConfirmationDialog, "DailyReportEditConfirmationDialog+", true);
            }
        }
    }



}