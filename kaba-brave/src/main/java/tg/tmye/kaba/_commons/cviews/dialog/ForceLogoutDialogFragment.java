package tg.tmye.kaba._commons.cviews.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.Splash.SplashActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class ForceLogoutDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ForceLogoutDialogFragment";
    private String pic_link;

    public static ForceLogoutDialogFragment newInstance() {

        Bundle args = new Bundle();
//        args.putString("drawable", pic_link);
//        args.putString("message", message);
        ForceLogoutDialogFragment fragment = new ForceLogoutDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //    private ImageView iv_drawable_icon;
//    TextView tv_message;
    private TextView tv_confirm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        message = getArguments().getString("message");
//        pic_link = getArguments().getString("drawable");
    }

    String message;

    /*  private void initContent() {
          tv_message.setText(message);
          GlideApp.with(this)
                  .load(Constant.SERVER_ADDRESS +"/"+ pic_link)
                  .transition(GenericTransitionOptions.with(  ((MyKabaApp) getContext().getApplicationContext()).getGlideAnimation()  ))
                  .placeholder(R.drawable.placeholder_kaba)
                  .centerCrop()
                  .into(iv_drawable_icon);
      }
  */
    private void initViews(View view) {
//        tv_message = view.findViewById(R.id.tv_message);
//        iv_drawable_icon = view.findViewById(R.id.iv_drawable_icon);
        tv_confirm = view.findViewById(R.id.tv_confirm);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.force_logout_dialog_info, null, false);
        initViews(view);

       /* Button bt_confirm_home = view.findViewById(R.id.tv_confirm_home);
        bt_confirm_home.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_home_white_drawable_red, null),
                null, null, null);*/

        view.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

//        initContent();
        alertDialogBuilder.setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    @Override
    public void onClick(View view) {

        // dismiss
        dismissAllowingStateLoss();
        dismiss();

        /* logout */
        /* finish current activity --  */
        /* open splash activity again ! */
        CustomerDataRepository.deleteCustomerInfos(getActivity());
        CustomerDataRepository.deleteToken(getActivity());
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }
}
