package tg.tmye.kaba_i_deliver.activity.command.frag;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver._commons.adapter.CommandRecyclerAdapter;
import tg.tmye.kaba_i_deliver._commons.adapter.HSNCommandRecycler;
import tg.tmye.kaba_i_deliver._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.hsn.HSN;

/**
 * By abiguime on 2021/7/19.
 * email: 2597434002@qq.com
 */
public class HsnListFragment extends Fragment {


    public static final int WAITING = 0;
    public static final int COOKING = 1;
    public static final int SHIPPING = 2;
    public static final int DELIVERED = 3;
    public static final int REJECTED = 4;
    public static final int CANCELLED = 5;

    private static final String DATA_1 = "subMenu";
    private OnFragmentInteractionListener mListener;
    private CommandListSpacesItemDecoration commandListDecorator;

    public static HsnListFragment instantiate (Context ctx, List<HSN> hsnList) {

        HsnListFragment frg = new HsnListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA_1, (ArrayList<? extends Parcelable>) hsnList);
        frg.setArguments(args);
        return frg;
    }

    List<HSN> hsnList;

    private RecyclerView recyclerview;

    private TextView tv_no_food_message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the bundle data
        Bundle args = getArguments();
        if (args!= null)
            hsnList = args.getParcelableArrayList(DATA_1);
    }


    public HsnListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_sub_commands, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.recyclerview);
        tv_no_food_message = view.findViewById(R.id.tv_no_food_message);

        initRecyclerview(hsnList);
    }

    private void initRecyclerview(List<HSN>  hsnList) {

        if (commandListDecorator == null) {
            commandListDecorator = new CommandListSpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                    getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
            );
        }

        if (recyclerview.getItemDecorationCount() == 0)
            recyclerview.addItemDecoration(commandListDecorator);

        /* according */
        if (hsnList != null && hsnList.size() > 0) {
            tv_no_food_message.setVisibility(View.GONE);
            HSNCommandRecycler adapter = new HSNCommandRecycler(getContext(), hsnList);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            recyclerview.setLayoutManager(manager);
            recyclerview.setAdapter(adapter);
            recyclerview.setVisibility(View.VISIBLE);
        } else {
            tv_no_food_message.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
    }

    private void showFood() {

        tv_no_food_message.setVisibility(View.GONE);
        recyclerview.setVisibility(View.VISIBLE);
    }

    private void noFood() {

        tv_no_food_message.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.GONE);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setData(List<HSN> hsnList) {
        initRecyclerview(hsnList);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and restaurant_name
        void onCommandInteraction(Command food);

        void onHSNInteraction(HSN hsn);
    }


}
