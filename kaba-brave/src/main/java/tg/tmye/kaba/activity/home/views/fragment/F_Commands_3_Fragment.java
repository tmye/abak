package tg.tmye.kaba.activity.home.views.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.CommandRecyclerAdapter;
import tg.tmye.kaba._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba.activity.home.contracts.F_CommandContract;
import tg.tmye.kaba.data.command.Command;


public class F_Commands_3_Fragment extends BaseFragment implements F_CommandContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    private OnFragmentInteractionListener mListener;

    /* views */
    private RecyclerView commandRecyclerview;
    private SwipeRefreshLayout swiperefresh;
    private LinearLayoutCompat lny;
    private Button bt_refresh;


    private F_CommandContract.Presenter presenter;
    private android.support.v7.widget.RecyclerView.ItemDecoration commandListDecorator;
    private CommandRecyclerAdapter commandsAdapter;

    public F_Commands_3_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    // TODO: Rename and change types and number of parameters
    public static F_Commands_3_Fragment newInstance() {
        F_Commands_3_Fragment fragment = new F_Commands_3_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_command_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initDrawable();
        swiperefresh.setOnRefreshListener(this);
        bt_refresh.setOnClickListener(this);
    }

    private void initDrawable() {
//        android:drawableStart="@drawable/ic_refresh_icon"
        Drawable refresh_icon = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_refresh_icon, null);
        bt_refresh.setCompoundDrawablesWithIntrinsicBounds (refresh_icon, null, null, null);
    }

    private void initViews(View rootView) {
        bt_refresh = rootView.findViewById(R.id.bt_tryagain);
        lny = rootView.findViewById(R.id.lny_error);
        commandRecyclerview = rootView.findViewById(R.id.recyclerview);
        swiperefresh = rootView.findViewById(R.id.swiperefresh);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void setPresenter(F_CommandContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void networkError() {
        /* hide recyclerview ...
        * show message
        * */
        this.showErrorPage(true);
    }

    @Override
    public void showIsLoading(final boolean isLoading) {
        if (swiperefresh.isRefreshing() == isLoading) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swiperefresh.setRefreshing(isLoading);
            }
        });
    }

    @Override
    public void inflateCommandList(final List<Command> data) {

        if (data == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (commandListDecorator == null) {
                    commandListDecorator = new CommandListSpacesItemDecoration(
                            getContext().getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                            getContext().getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
                    );
                }
                if (commandRecyclerview.getItemDecorationCount() == 0)
                    commandRecyclerview.addItemDecoration(commandListDecorator);

                if ( commandRecyclerview.getLayoutManager() == null)
                    commandRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

                commandsAdapter = new CommandRecyclerAdapter(getContext(), data);
                commandRecyclerview.setAdapter(commandsAdapter);
            }
        });
    }

    @Override
    public void showErrorPage(final boolean isShowed) {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    lny.setVisibility(isShowed ? View.VISIBLE : View.GONE);
                    commandRecyclerview.setVisibility(isShowed ? View.GONE : View.VISIBLE);
                }
            });
    }

    @Override
    public void onRefresh() {
        presenter.update();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_tryagain:
                presenter.update();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and restaurant_name
        void onFragmentInteraction(Uri uri);
    }
}
