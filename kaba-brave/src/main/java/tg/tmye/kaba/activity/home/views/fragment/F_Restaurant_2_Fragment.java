package tg.tmye.kaba.activity.home.views.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.RestaurantRecyclerAdapter;
import tg.tmye.kaba._commons.cviews.OffRecyclerview;
import tg.tmye.kaba._commons.decorator.LastItemListSpaceDecoration;
import tg.tmye.kaba.activity.home.contracts.F_RestaurantContract;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;


public class F_Restaurant_2_Fragment extends BaseFragment implements F_RestaurantContract.View {


    private OnFragmentInteractionListener mListener;

    private static String RESTOZ = "RESTOZ";
    private int mColumnCount = 1;

    /* views */
    RecyclerView restaurantRecyclerView;

    TextView tv_error_message;

    /* presenter */
    private F_RestaurantContract.Presenter presenter;

    /* adapter */
    private RestaurantRecyclerAdapter resListAdapter;

    public F_Restaurant_2_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View rootView) {
        tv_error_message = rootView.findViewById(R.id.tv_messages);
        restaurantRecyclerView = rootView.findViewById(R.id.list);
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
    public void inflateRestaurantList(List<RestaurantEntity> restaurantEntities) {

        if (restaurantEntities == null || restaurantEntities.size() == 0) {
            restaurantRecyclerView.setVisibility(View.GONE);
            /* show error message */
            tv_error_message.setVisibility(View.VISIBLE);
        } else {
            tv_error_message.setVisibility(View.GONE);
            restaurantRecyclerView.setVisibility(View.VISIBLE);
            restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            restaurantRecyclerView.addItemDecoration(new LastItemListSpaceDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.menu_food_item_height)));
            resListAdapter = new RestaurantRecyclerAdapter(getContext(), restaurantEntities, mListener);
            restaurantRecyclerView.setAdapter(resListAdapter);
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

        void onRestaurantInteraction(RestaurantEntity resto);
    }
}
