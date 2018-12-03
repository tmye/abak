package tg.tmye.kaba.activity.home.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.RestaurantRecyclerAdapter;
import tg.tmye.kaba._commons.cviews.CustomProgressbar;
import tg.tmye.kaba._commons.decorator.LastItemListSpaceDecoration;
import tg.tmye.kaba.activity.Splash.SplashActivity;
import tg.tmye.kaba.activity.home.HomeActivity;
import tg.tmye.kaba.activity.home.contracts.F_RestaurantContract;
import tg.tmye.kaba.activity.home.presenter.F_Restaurant_2_Presenter;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;


public class F_Restaurant_2_Fragment extends BaseFragment implements F_RestaurantContract.View, View.OnClickListener {


    public static int COMING_SOON = 1;
    private OnFragmentInteractionListener mListener;

    private static String RESTOZ = "RESTOZ";
    private int mColumnCount = 1;

    /* views */
    RecyclerView restaurantRecyclerView;

    TextView tv_error_message;
    Button bt_tryagain;

    CustomProgressbar progressBar;

    /* presenter */
    private F_RestaurantContract.Presenter presenter;

    /* adapter */
    private RestaurantRecyclerAdapter resListAdapter;
    private LinearLayout lny_error_box;

    public F_Restaurant_2_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (resListAdapter == null) {
                if (presenter == null)
                    presenter = (F_RestaurantContract.Presenter) ((HomeActivity) getActivity()).getPresenterWithNo(2);
                presenter.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();
        }
    }


    // TODO: Rename and change types and number of parameters
    public static F_Restaurant_2_Fragment newInstance() {
        F_Restaurant_2_Fragment fragment = new F_Restaurant_2_Fragment();
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
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {

    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_restaurant_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bt_tryagain.setOnClickListener(this);
    }

    private void initViews(View rootView) {

        lny_error_box = rootView.findViewById(R.id.lny_error_box);
        bt_tryagain = rootView.findViewById(R.id.bt_tryagain);
        tv_error_message = rootView.findViewById(R.id.tv_messages);
        restaurantRecyclerView = rootView.findViewById(R.id.list);
        progressBar = rootView.findViewById(R.id.progress_bar);
    }

    /*public void updateRestoz (List<RestaurantEntity> restoz) {

        this.allRestos = restoz;
        if (resListAdapter != null)
            resListAdapter.updateData(allRestos);
    }*/

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
    public void setPresenter(F_RestaurantContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void inflateRestaurantList(final List<RestaurantEntity> restaurantEntities) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                lny_error_box.setVisibility(View.GONE);

                if (restaurantEntities == null || restaurantEntities.size() == 0) {
                    restaurantRecyclerView.setVisibility(View.GONE);
                    /* show error message */
                    tv_error_message.setVisibility(View.VISIBLE);
                    tv_error_message.setText(getResources().getString(R.string.no_restaurant_available));
                } else {
                    tv_error_message.setVisibility(View.GONE);
                    restaurantRecyclerView.setVisibility(View.VISIBLE);
                    restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    if (restaurantRecyclerView.getItemDecorationCount() == 0)
                        restaurantRecyclerView.addItemDecoration(new LastItemListSpaceDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.menu_food_item_height)));
                    resListAdapter = new RestaurantRecyclerAdapter(getContext(), restaurantEntities, mListener);
                    restaurantRecyclerView.setAdapter(resListAdapter);
                }
            }
        });
    }

    @Override
    public void showLoading(final boolean isLoading) {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /* no views. */
                    lny_error_box.setVisibility(View.GONE);
                    restaurantRecyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                }
            });
    }

    @Override
    public void showNetworkError() {

        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_error_message.setText(getResources().getString(R.string.network_error));
                    tv_error_message.setVisibility(View.VISIBLE);
                    lny_error_box.setVisibility(View.VISIBLE);
                    bt_tryagain.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    /* hide everything else */
                    restaurantRecyclerView.setVisibility(View.GONE);

                    /* add a button try again */
                }
            });
    }

    @Override
    public void showSysError() {

        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tv_error_message.setText(getResources().getString(R.string.sys_error));
                    restaurantRecyclerView.setVisibility(View.GONE);
                    bt_tryagain.setVisibility(View.VISIBLE);
                    lny_error_box.setVisibility(View.VISIBLE);
                    tv_error_message.setVisibility(View.VISIBLE);
                }
            });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                presenter.populateViews();
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

        public static final int MENU = 9, RESTAURANT = 10;

        // TODO: Update argument type and restaurant_name
        void onFragmentInteraction(Uri uri);

        void onRestaurantInteraction(RestaurantEntity resto, int restaurant);

        void onComingSoonInteractionListener(RestaurantEntity resto);
    }
}
