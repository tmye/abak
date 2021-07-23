package tg.tmye.kaba_i_deliver.cviews.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.command.CommandDetailsActivity;


/**
 * By abiguime on 07/04/2018.
 * email: 2597434002@qq.com
 */

public class RefundDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    private int givenAmount = 0;

    public static RefundDialogFragment newInstance(int orderAmount, int orderId) {

        Bundle args = new Bundle();
        args.putInt("orderAmount", orderAmount);
        args.putInt("orderId", orderId);
        RefundDialogFragment fragment = new RefundDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderAmount = getArguments().getInt("orderAmount");
        orderId = getArguments().getInt("orderId");

    }

    int orderAmount;
    int orderId;
    int leftAmount = -1;


    EditText ed_order_amount, ed_customer_giving, ed_customer_left;
    LinearLayoutCompat lny_bg;
    TextView tv_end_action;

    private void initViews(View view) {
        ed_order_amount = view.findViewById(R.id.ed_order_amount);
        ed_customer_giving = view.findViewById(R.id.ed_customer_giving);
        ed_customer_left = view.findViewById(R.id.ed_customer_left);
        lny_bg = view.findViewById(R.id.lny_bg);
        tv_end_action = view.findViewById(R.id.tv_end_action);
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.dialog_refund, null, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        ed_order_amount.setText(String.valueOf(orderAmount));

        view.setOnClickListener(this);
        Button bt_confirm_home = view.findViewById(R.id.tv_confirm_home);
     /*   bt_confirm_home.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.common_full_open_on_phone, null),
                null, null, null);*/
        bt_confirm_home.setOnClickListener(this);
        ed_customer_giving.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    String amount = s.toString();
                    givenAmount = 0;
                    int value = 0;
                    try {
                        value = Integer.parseInt(amount);
                        givenAmount = value;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    leftAmount = value - orderAmount;
                    ed_customer_left.setText(String.valueOf(Math.abs(leftAmount)));
                    if (leftAmount > 0) {
                        lny_bg.setBackgroundColor(Color.argb(255,156,240,255)); // blue
                        tv_end_action.setText(getContext().getString(R.string.to_credit_amount));
                    } else if (leftAmount < 0) {
                        lny_bg.setBackgroundColor(Color.argb(255,255,112,151)); // red
                        tv_end_action.setText(getContext().getString(R.string.to_debit_amount));
                    } else {
                        lny_bg.setBackgroundColor(Color.WHITE);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm_home:
                ((CommandDetailsActivity) getActivity()).confirmRefund(orderAmount, givenAmount, leftAmount);
                dismiss();
                break;
        }
    }


}
