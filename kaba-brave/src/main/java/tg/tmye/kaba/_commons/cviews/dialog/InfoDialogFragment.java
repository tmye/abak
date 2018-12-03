package tg.tmye.kaba._commons.cviews.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.home.HomeActivity;

/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class InfoDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final int PURCHASE_SUCCESS = 94;
    public static final int BASKET_ADD = 95;


    public static InfoDialogFragment newInstance(int drawable_icon, String message, int type) {

        Bundle args = new Bundle();
        args.putInt("drawable", drawable_icon);
        args.putString("message", message);
        args.putInt("type", type);
        InfoDialogFragment fragment = new InfoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TextView tv_message;
    ImageView iv_drawable_icon;
    private TextView tv_confirm_home;
    private TextView tv_confirm_continue;
    int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = getArguments().getString("message");
        drawable_icon_id = getArguments().getInt("drawable");
        type = getArguments().getInt("type");
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
        tv_confirm_continue = view.findViewById(R.id.tv_confirm_continue);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = null;


        if (type == 0) {

              view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_info_ok, null, false);

            ImageView iv_drawable = view.findViewById(R.id.iv_drawable_icon);
            iv_drawable.setImageResource(R.drawable.ic_attach_money_colorprimary_24dp);
            TextView tv_message = view.findViewById(R.id.tv_message);
            tv_message.setText(R.string.flooz_success);
            Button tv_confirm = view.findViewById(R.id.tv_confirm);
            tv_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {

            view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_info, null, false);
            initViews(view);

            TextView bt_confirm_home = view.findViewById(R.id.tv_confirm_home);
/*
            bt_confirm_home.setCompoundDrawablesWithIntrinsicBounds(
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_home_white_drawable_red, null),
                    null, null, null);
*/
            bt_confirm_home.setText(getResources().getString(R.string.title_home));

            TextView bt_confirm_continue = view.findViewById(R.id.tv_confirm_continue);
/*
            bt_confirm_continue.setCompoundDrawablesWithIntrinsicBounds(
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_upward_navigation_24dp, null),
                    null, null, null);
*/
            bt_confirm_continue.setText(getResources().getString(R.string.icontinue));

            switch (type) {
                case BASKET_ADD:
                    tv_confirm_home.setVisibility(View.GONE);
                    tv_confirm_continue.setVisibility(View.VISIBLE);
                    break;
                case PURCHASE_SUCCESS:
                    tv_confirm_home.setVisibility(View.VISIBLE);
                    tv_confirm_continue.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    break;
            }

            view.setOnClickListener(this);
            tv_confirm_continue.setOnClickListener(this);
            tv_confirm_home.setOnClickListener(this);

            initContent();
        }

        alertDialogBuilder.setView(view);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    @Override
    public void onClick(View view) {

        if (type ==  BASKET_ADD) {
            switch (view.getId()){
                /* returned */
                case R.id.tv_confirm_continue:
                    dismiss();
                    break;
            }
        }

        if (type ==  PURCHASE_SUCCESS) {
            switch (view.getId()){
                /* returned */
                case R.id.tv_confirm_continue:
                    getActivity().finish();
                    break;
                case R.id.tv_confirm_home:
                    Intent home_intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(home_intent);
                    getActivity().finish();
                    break;
            }
        }
    }
}
