package tg.tmye.kaba_i_deliver.cviews.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import tg.tmye.kaba_i_deliver.R;


/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class InfoDialogFragment extends DialogFragment implements View.OnClickListener {


    public static final boolean CONFIRM_DELIVERING_SUCCESS = true;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_info, null, false);
        initViews(view);

        view.setOnClickListener(this);

        Button bt_confirm_home = view.findViewById(R.id.tv_confirm_home);
        bt_confirm_home.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_green_upward_navigation_24dp, null),
                null, null, null);

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
            case R.id.tv_confirm_home:
                dismiss();
                if (isSuccess)
                    getActivity().finish();
                break;
        }
    }


}
