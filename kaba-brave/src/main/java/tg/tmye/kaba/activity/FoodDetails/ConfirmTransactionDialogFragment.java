package tg.tmye.kaba.activity.FoodDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import tg.tmye.kaba.R;


/**
 * By abiguime on 2017/12/6.
 * email: 2597434002@qq.com
 */

public class ConfirmTransactionDialogFragment extends DialogFragment {


    Listener mListener;

    private double latitude, longitude;

    // TODO: Customize parameters
    public static ConfirmTransactionDialogFragment newInstance() {
        final ConfirmTransactionDialogFragment fragment = new ConfirmTransactionDialogFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {

        super.onResume();

        int width = getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    ImageButton ib_confirm;
    TextView tv_select_address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirm_transaction_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ib_confirm = view.findViewById(R.id.ib_confirm);
        tv_select_address = view.findViewById(R.id.tv_select_address);

        ib_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tv_select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAddress();
            }
        });
    }

    private void selectAddress() {

        // startActivityForResult

    }

    private void selectAdress() {

        // select address of the delivery
    }

    private static final int TWO_MINUTES = 1000 * 60 * 2;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {

    }

}
