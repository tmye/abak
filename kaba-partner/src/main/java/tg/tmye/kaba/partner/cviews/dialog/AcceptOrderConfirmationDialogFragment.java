package tg.tmye.kaba.partner.cviews.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import tg.tmye.kaba.partner.ILog;
import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner.activities.commands.CommandDetailsActivity;

/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class AcceptOrderConfirmationDialogFragment extends DialogFragment implements View.OnClickListener {

    public static AcceptOrderConfirmationDialogFragment newInstance() {

        Bundle args = new Bundle();
        AcceptOrderConfirmationDialogFragment fragment = new AcceptOrderConfirmationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initViews(View view) {
        radioGroup_motive = view.findViewById(R.id.radiogroup_motive);
        bt_confirm = view.findViewById(R.id.bt_confirm);
        bt_cancel = view.findViewById(R.id.bt_cancel);
    }

    RadioGroup radioGroup_motive;
    Button bt_confirm, bt_cancel;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.accept_dialog_info, null, false);
        initViews(view);

        bt_cancel.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);

        alertDialogBuilder.setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_confirm:
                launchAccepting();
                break;
            case R.id.bt_cancel:
                dismiss();
                break;
        }
    }

    private void launchAccepting() {

        if (getActivity() != null) {
            ((CommandDetailsActivity) (getActivity())).launchAccepting();
        }
    }


}
