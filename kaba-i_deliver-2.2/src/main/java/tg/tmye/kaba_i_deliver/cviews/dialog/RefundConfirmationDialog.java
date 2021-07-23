package tg.tmye.kaba_i_deliver.cviews.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.command.CommandDetailsActivity;

/**
 * By abiguime on 2021/7/13.
 * email: 2597434002@qq.com
 */
public class RefundConfirmationDialog extends DialogFragment {


    public static RefundConfirmationDialog newInstance(int orderAmount, int givenAmount, int leftAmount) {
        Bundle args = new Bundle();
        args.putInt("orderAmount", orderAmount);
        args.putInt("givenAmount", givenAmount);
        args.putInt("leftAmount", leftAmount);
        RefundConfirmationDialog fragment = new RefundConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    int orderAmount = 0;
    int givenAmount = 0;
    int leftAmount = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leftAmount = getArguments().getInt("leftAmount");
        givenAmount = getArguments().getInt("givenAmount");
        orderAmount = getArguments().getInt("orderAmount");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder
                .setTitle(leftAmount > 0 ? R.string.title_debit : R.string.title_credit)
                .setMessage(getString(leftAmount > 0 ? R.string.refund_amount_confirmation : R.string.debit_amount_confirmation, Math.abs(leftAmount)))
                .setPositiveButton(R.string.refund, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((CommandDetailsActivity)getActivity()).lanchRefund(orderAmount, givenAmount, leftAmount);
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