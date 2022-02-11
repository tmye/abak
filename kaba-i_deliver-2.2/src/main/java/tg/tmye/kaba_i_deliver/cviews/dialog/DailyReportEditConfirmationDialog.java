package tg.tmye.kaba_i_deliver.cviews.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.command.CommandDetailsActivity;
import tg.tmye.kaba_i_deliver.activity.dailyreport.DailyReportHistoryActivity;
import tg.tmye.kaba_i_deliver.data.dailyreport.DailyReport;

/**
 * By abiguime on 2021/7/13.
 * email: 2597434002@qq.com
 */
public class DailyReportEditConfirmationDialog extends DialogFragment {

    private DailyReport report;

    public static DailyReportEditConfirmationDialog newInstance(DailyReport report) {
        Bundle args = new Bundle();
        args.putParcelable("report", report);
        DailyReportEditConfirmationDialog fragment = new DailyReportEditConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.report = getArguments().getParcelable("report");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder
                .setTitle(R.string.confirmation)
                .setMessage(getString(R.string.daily_report_question, report.makeAt))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((DailyReportHistoryActivity)getActivity()).editReport(report);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}