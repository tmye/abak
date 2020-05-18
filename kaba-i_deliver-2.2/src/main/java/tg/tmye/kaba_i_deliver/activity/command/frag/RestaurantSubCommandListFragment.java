package tg.tmye.kaba_i_deliver.activity.command.frag;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver._commons.adapter.CommandRecyclerAdapter;
import tg.tmye.kaba_i_deliver._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba_i_deliver.data.command.Command;

/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantSubCommandListFragment extends Fragment {


    private static final String DATA_1 = "subMenu";
    private OnFragmentInteractionListener mListener;
    private CommandListSpacesItemDecoration commandListDecorator;

    public static RestaurantSubCommandListFragment instantiate (Context ctx,  List<Command>  subCommandList) {

        RestaurantSubCommandListFragment frg = new RestaurantSubCommandListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA_1, (ArrayList<? extends Parcelable>) subCommandList);
        frg.setArguments(args);
        return frg;
    }

    List<Command>  subCommandList;

    private RecyclerView recyclerview;

    private TextView tv_no_food_message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the bundle data
        Bundle args = getArguments();
        if (args!= null)
            subCommandList = args.getParcelableArrayList(DATA_1);
    }


    public RestaurantSubCommandListFragment() {
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

        initRecyclerview(subCommandList);
    }

    private void initRecyclerview(List<Command>  commands) {

        if (commandListDecorator == null) {
            commandListDecorator = new CommandListSpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                    getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
            );
        }

        if (recyclerview.getItemDecorationCount() == 0)
            recyclerview.addItemDecoration(commandListDecorator);

        /* according */
        if (commands != null && commands.size() > 0) {
            tv_no_food_message.setVisibility(View.GONE);
            CommandRecyclerAdapter adapter = new CommandRecyclerAdapter(getContext(), commands);
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

    public void setData(List<Command> data_command_list) {
        initRecyclerview(data_command_list);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and restaurant_name
        void onCommandInteraction(Command food);
    }


}
