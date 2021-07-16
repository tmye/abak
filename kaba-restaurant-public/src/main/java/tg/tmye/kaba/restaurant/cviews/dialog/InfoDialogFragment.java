package tg.tmye.kaba.restaurant.cviews.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import tg.tmye.kaba.restaurant.activities.home.HomeActivity;
import tg.tmye.kaba.restaurant.R;

/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class InfoDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final int PURCHASE_SUCCESS = 94;
    public static final int BASKET_ADD = 95;


    public static InfoDialogFragment newInstance(int drawable_icon, String message, boolean success) {

        Bundle args = new Bundle();
        args.putInt("drawable", drawable_icon);
        args.putString("message", message);
        args.putBoolean("success", success);
        InfoDialogFragment fragment = new InfoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TextView tv_message;
    ImageView iv_drawable_icon;
    private TextView tv_confirm_home;
    boolean isSuccess;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = getArguments().getString("message");
        drawable_icon_id = getArguments().getInt("drawable");
        isSuccess = getArguments().getBoolean("success");
    }

    int drawable_icon_id;
    String message;

    private void initContent() {
        tv_message.setText(message);
        iv_drawable_icon.setImageResource(drawable_icon_id);
    }

    private void initViews(View view) {
        tv_message = view.findViewById(R.id.tv_message);
        iv_drawable_icon = view.findViewById(R.id.iv_drawable_icon);
        tv_confirm_home = view.findViewById(R.id.tv_confirm_home);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_info, null, false);
        initViews(view);

        Button bt_confirm_home = view.findViewById(R.id.tv_confirm_home);
        bt_confirm_home.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_orange_upward_navigation_24dp, null),
                null, null, null);

        /*
        *tv_confirm_continue
        *  android:drawableStart="@drawable/ic_home_white_drawable_red"
            android:drawableLeft="@drawable/ic_home_white_drawable_red"
        * */

        view.setOnClickListener(this);
        bt_confirm_home.setOnClickListener(this);

        initContent();
        alertDialogBuilder.setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            /* returned */
            case R.id.tv_confirm_home:
                dismiss();
                if (isSuccess)
                    getActivity().finish();
                break;
        }
         /*   switch (view.getId()){
            *//* returned *//*
                case R.id.tv_confirm_continue:
                    getActivity().finish();
                    break;
                case R.id.tv_confirm_home:
                    Intent home_intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(home_intent);
                    getActivity().finish();
                    break;
            }*/
    }


}
