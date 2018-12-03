package tg.tmye.kaba._commons.cviews.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.CustomProgressbar;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.LoadingIsOkActivtity;

/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class LoadingDialogFragment extends DialogFragment {

    public static final String TAG = "LoadingDialogFragment";
    public static final int CUSTOM_PROGRESS = 30;
    private String message;
    private int icon;

    public static LoadingDialogFragment newInstance(String message, int icon_drawable) {

        Bundle args = new Bundle();
        args.putString("message", message);
        args.putInt("icon", icon_drawable);
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static LoadingDialogFragment newInstance(String message) {

        Bundle args = new Bundle();
        args.putString("message", message);
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static LoadingDialogFragment newInstance() {

        Bundle args = new Bundle();
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        message = getArguments().getString("message", "");
        icon = getArguments().getInt("icon", -1);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

        FragmentActivity activity = getActivity();
        if (activity instanceof LoadingIsOkActivtity) {
            boolean isOk = ((LoadingIsOkActivtity) activity).isOk();
            if (isOk) {
                super.onCancel(dialog);
            } else {
                activity.finish();
            }
        }
    }

    /*  @Override
    public void show(FragmentTransaction transaction, String tag) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }*/

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
        View view = null;

//        if (icon == -1) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.frg_dialog_loading, null, false);
//        } else {
//            view = LayoutInflater.from(getContext()).inflate(R.layout.frg_dialog_loading_custom, null, false);
//            CustomProgressbar progressbar = view.findViewById(R.id.progress_bar);
//            progressbar.setVisibility(View.VISIBLE);
//        }
        initViews(view);
        initMessage();
        alertDialogBuilder.setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(STYLE_NO_TITLE);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return alertDialog;
    }


}
