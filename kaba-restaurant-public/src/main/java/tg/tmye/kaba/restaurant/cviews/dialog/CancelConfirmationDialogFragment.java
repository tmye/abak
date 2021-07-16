package tg.tmye.kaba.restaurant.cviews.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.activities.commands.CommandDetailsActivity;
import tg.tmye.kaba.restaurant.syscore.Constant;

/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class CancelConfirmationDialogFragment extends DialogFragment implements View.OnClickListener {

    public static CancelConfirmationDialogFragment newInstance() {

        Bundle args = new Bundle();
        CancelConfirmationDialogFragment fragment = new CancelConfirmationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initContent() {

        motives = getContext().getResources().getStringArray(R.array.motives);

        radioGroup_motive.removeAllViews();

        for (int i = 0; i < motives.length; i++) {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.radiobutton_layout, null, false);
            radioButton.setId(i+1000);
            radioButton.setText(motives[i]);
            radioButton.setTag(i+1); // 1 , 2 , 3
            radioGroup_motive.addView(radioButton);
            if (i == 0) {
                radioButton.setChecked(i == 0 ? true : false);
            }
        }
        radioGroup_motive.clearCheck();
    }

    private void initViews(View view) {
        radioGroup_motive = view.findViewById(R.id.radiogroup_motive);
        bt_confirm = view.findViewById(R.id.bt_confirm);
        bt_cancel = view.findViewById(R.id.bt_cancel);
    }

    RadioGroup radioGroup_motive;
    Button bt_confirm, bt_cancel;

    String[] motives;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cancel_dialog_info, null, false);
        initViews(view);

        bt_cancel.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);

        initContent();
        alertDialogBuilder.setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_confirm:
                launchCancelling();
                break;
            case R.id.bt_cancel:
                dismiss();
                break;
        }
    }

    private void launchCancelling() {
        int radioButtonId = radioGroup_motive.getCheckedRadioButtonId();

        Log.d(Constant.APP_TAG, "" + radioButtonId);

//        String motif = motives[radioButtonId-1000];

        if (getActivity() != null) {
//            ((CommandDetailsActivity) (getActivity())).cancelCommand((Integer) radioGroup_motive.getTag());
            ((CommandDetailsActivity) (getActivity())).cancelCommand(radioButtonId);
        }
    }


}
