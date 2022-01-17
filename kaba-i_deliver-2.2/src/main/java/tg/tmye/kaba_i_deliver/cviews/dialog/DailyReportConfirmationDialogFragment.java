package tg.tmye.kaba_i_deliver.cviews.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.command.CommandDetailsActivity;
import tg.tmye.kaba_i_deliver.activity.dailyreport.DailyReportActivity;


/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class DailyReportConfirmationDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    private int givenAmount = 0;
    private int fuel;
    private int credit;
    private int reparation;
    private int losses;
    private int parking;
    private int various;

    public static DailyReportConfirmationDialogFragment newInstance(int fuel, int credit, int reparation, int losses, int parking, int various) {

        Bundle args = new Bundle();
        args.putInt("fuel", fuel);
        args.putInt("credit", credit);
        args.putInt("reparation", reparation);
        args.putInt("losses", losses);
        args.putInt("parking", parking);
        args.putInt("various", various);
        DailyReportConfirmationDialogFragment fragment = new DailyReportConfirmationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fuel = getArguments().getInt("fuel");
        credit = getArguments().getInt("credit");
        reparation = getArguments().getInt("reparation");
        losses = getArguments().getInt("losses");
        parking = getArguments().getInt("parking");
        various = getArguments().getInt("various");
    }

    LinearLayoutCompat lny_bg;
    TextView tv_fuel, tv_credit, tv_reparation, tv_losses, tv_parking, tv_various;
Button bt_confirm_daily_report;


    private void initViews(View view) {
        tv_fuel = view.findViewById(R.id.ed_fuel_amount);
        tv_credit = view.findViewById(R.id.ed_communication_credit);
        tv_reparation = view.findViewById(R.id.ed_reparation_amount);
        tv_losses = view.findViewById(R.id.ed_losses_amount);
        tv_parking = view.findViewById(R.id.ed_parking_amount);
        tv_various = view.findViewById(R.id.ed_various_fees);
        bt_confirm_daily_report = view.findViewById(R.id.bt_confirm_daily_report);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.dialog_daily_report_confirmation, null, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        tv_various.setText(String.valueOf(various));
        tv_fuel.setText(String.valueOf(fuel));
        tv_reparation.setText(String.valueOf(reparation));
        tv_losses.setText(String.valueOf(losses));
        tv_credit.setText(String.valueOf(credit));
        tv_parking.setText(String.valueOf(parking));

        bt_confirm_daily_report.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_confirm_daily_report:
                ((DailyReportActivity) getActivity()).confirmReport(fuel, credit, reparation, losses, parking, various);
                dismiss();
                break;
        }
    }


}
