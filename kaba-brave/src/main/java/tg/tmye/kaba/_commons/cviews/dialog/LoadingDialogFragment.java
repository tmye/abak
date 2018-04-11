package tg.tmye.kaba._commons.cviews.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tg.tmye.kaba.R;

/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class LoadingDialogFragment extends DialogFragment {

    public static LoadingDialogFragment newInstance(String message) {

        Bundle args = new Bundle();
        args.putString("message", message);
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TextView tv_message;

    private void initMessage() {
        String message = getArguments().getString("message");
        if (message != null && !"".equals(message.trim()))
            tv_message.setText(message);
    }

    private void initViews(View view) {
        tv_message = view.findViewById(R.id.tv_message);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.frg_dialog_loading, null, false);
        initViews(view);
        initMessage();
        alertDialogBuilder.setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(STYLE_NO_TITLE);
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }
}
