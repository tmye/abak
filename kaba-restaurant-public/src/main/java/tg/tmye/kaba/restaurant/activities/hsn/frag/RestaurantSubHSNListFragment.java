package tg.tmye.kaba.restaurant.activities.hsn.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant._commons.adapter.CommandRecyclerAdapter;
import tg.tmye.kaba.restaurant._commons.adapter.HSNCommandRecycler;
import tg.tmye.kaba.restaurant._commons.baseviewstubfragment.BaseViewStubFragment;
import tg.tmye.kaba.restaurant._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba.restaurant.activities.commands.MyCommandsActivity;
import tg.tmye.kaba.restaurant.data.command.Command;
import tg.tmye.kaba.restaurant.data.hsn.HSN;

/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantSubHSNListFragment extends BaseViewStubFragment {


    private static final String DATA_1 = "subMenu";
    private OnFragmentInteractionListener mListener;
    private CommandListSpacesItemDecoration commandListDecorator;
    private Context context;


    public static RestaurantSubHSNListFragment instantiate (Context ctx, List<HSN>  subHSNList) {

        RestaurantSubHSNListFragment frg = new RestaurantSubHSNListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA_1, (ArrayList<? extends Parcelable>) subHSNList);
        frg.setArguments(args);
        return frg;
    }

    List<HSN>  subHSNList;

    private RecyclerView recyclerview;

    private TextView tv_no_food_message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the bundle data
        Bundle args = getArguments();
        if (args!= null)
            subHSNList = args.getParcelableArrayList(DATA_1);
    }


    public RestaurantSubHSNListFragment() {
    }

    @Override
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_restaurant_sub_commands;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerview = view.findViewById(R.id.recyclerview);
        tv_no_food_message = view.findViewById(R.id.tv_no_food_message);

        if (getContext() != null)
            this.context = getContext();
        initRecyclerview(subHSNList);
    }

    private void initRecyclerview(List<HSN> hsns) {

        if (commandListDecorator == null) {
            commandListDecorator = new CommandListSpacesItemDecoration(
                    this.context.getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                    this.context.getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
            );
        }

        recyclerview.removeAllViews();

        if (recyclerview.getItemDecorationCount() == 0)
            recyclerview.addItemDecoration(commandListDecorator);

        /* according */
        if (hsns != null && hsns.size() > 0) {
            tv_no_food_message.setVisibility(View.GONE);
            HSNCommandRecycler adapter = new HSNCommandRecycler(this.context, hsns);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.context);
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

    public void setData(Context context, List<HSN> hsnList) {
        this.context = context;
        this.subHSNList = hsnList;
        // if activity not created yet, just let it run.
        try {
            initRecyclerview(subHSNList);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(getActivity(), MyCommandsActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
//        if (isInLayout()) {
//        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and restaurant_name
        void onCommandInteraction(Command food);
    }


}
