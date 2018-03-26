package tg.tmye.kaba.activity.home.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.Grid48ViewAdapter;
import tg.tmye.kaba._commons.adapters.GroupAdsAdapter;
import tg.tmye.kaba._commons.adapters.Home_1_MainRestaurantAdapter;
import tg.tmye.kaba._commons.cviews.Grid48View;
import tg.tmye.kaba._commons.cviews.Group10AdsView;
import tg.tmye.kaba._commons.cviews.HomeCustom_SwipeRefreshLayout;
import tg.tmye.kaba._commons.cviews.HomeFeedsAdapter;
import tg.tmye.kaba._commons.cviews.OffRecyclerview;
import tg.tmye.kaba._commons.cviews.SlidingBanner_LilRound;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba._commons.decorator.ListVerticalSpaceDecorator;
import tg.tmye.kaba.activity.home.HomeActivity;
import tg.tmye.kaba.activity.home.contracts.F_HomeContract;
import tg.tmye.kaba.activity.scanner.ScannerActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Feeds.NewsFeed;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.Group10AdvertItem;
import tg.tmye.kaba.data.advert.ProductAdvertItem;


public class F_Home_1_Fragment extends BaseFragment implements F_HomeContract.View, Toolbar.OnMenuItemClickListener {

    /* use butterkniffe */

    /* views */
    SlidingBanner_LilRound slidingBanner_lilRound; // home top sliding
    OffRecyclerview or_6restaurant;
    Grid48View grid48View;
    OffRecyclerview groupAds10;
    OffRecyclerview or_newsfeed;
    HomeCustom_SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_search_query;
    ImageButton ib_search_bt;


    /* adapters */
    Home_1_MainRestaurantAdapter home_1_mainRestaurantAdapter;


    private OnFragmentInteractionListener mListener;

    /* home 1 presenter */
    private F_HomeContract.Presenter presenter;
    private Toolbar toolbar;
    private String searchHint;

    public F_Home_1_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    // TODO: Rename and change types and number of parameters
    public static F_Home_1_Fragment newInstance() {
        F_Home_1_Fragment fragment = new F_Home_1_Fragment();
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
//        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_1_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inflate the views with all the available data
        initViews(view);
//        toolbar.inflateMenu(R.menu.home_page_menu);
//        toolbar.setOnMenuItemClickListener(this);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.updateView();
            }
        });

        tv_search_query.setOnClickListener((HomeActivity)getActivity());
        ib_search_bt.setOnClickListener((HomeActivity)getActivity());
    }

    /* passive functions */
    @Override
    public void showMainSliding(final List<AdsBanner> ads) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                slidingBanner_lilRound.load((ArrayList)ads);
            }
        });
    }

    @Override
    public void inflateMainRestaurants(final List<RestaurantEntity> restaurantEntityList) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                home_1_mainRestaurantAdapter = new Home_1_MainRestaurantAdapter(getContext(), restaurantEntityList, mListener);
                or_6restaurant.setLayoutManager(gridLayoutManager);
                or_6restaurant.setAdapter(home_1_mainRestaurantAdapter);
            }
        });
    }

    @Override
    public void inflateMain48(List<ProductAdvertItem> productAdvertItems) {
        if (productAdvertItems == null || productAdvertItems.size() == 0)
            grid48View.setVisibility(View.GONE);
        else {
            grid48View.setVisibility(View.VISIBLE);
            grid48View.inflateAds(productAdvertItems);
        }
    }

    @Override
    public void inflateGroupsPubLongList(List<Group10AdvertItem> group10AdvertItems) {

        if (group10AdvertItems == null || group10AdvertItems.size() == 0) {
            groupAds10.setVisibility(View.GONE);
        } else {
            groupAds10.setVisibility(View.VISIBLE);
            groupAds10.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
            groupAds10.addItemDecoration(new ListVerticalSpaceDecorator(getContext().getResources().getDimensionPixelSize(R.dimen.adgroup_bottom_space)));
            groupAds10.setAdapter(new GroupAdsAdapter(getContext(), group10AdvertItems));
        }
    }

    @Override
    public void inflateFeedsList(List<NewsFeed> newsFeeds) {

        if (newsFeeds == null || newsFeeds.size() == 0) {
            or_newsfeed.setVisibility(View.GONE);
        } else {
            or_newsfeed.setVisibility(View.VISIBLE);
            or_newsfeed.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            or_newsfeed.addItemDecoration(new CommandInnerFoodLineDecorator(ContextCompat.getDrawable(getContext(), R.drawable.feeds_item_divider)));
            or_newsfeed.setAdapter(new HomeFeedsAdapter(getContext(), null));
        }
    }


    @Override
    public void showLoadingProgress(final boolean isVisible) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isVisible);
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        /* make a push of the error */
    }


    @Override
    public void openRestaurant(RestaurantEntity restaurantEntity) {
        mListener.onRestaurantInteraction(restaurantEntity);
    }

    @Override
    public void openKabaShowPicture(SimplePicture.KabaShowPic pic) {
        mListener.onShowPic(pic);
    }

    @Override
    public void openProductAdvert(ProductAdvertItem productAdvertItem) {

    }

    @Override
    public void openAdBanner(AdsBanner ad) {
        mListener.onAdsInteraction (ad);
    }

    @Override
    public void showMainHint(final String data) {
        if (data != null && data.trim().length()>0) {
            this.searchHint = data;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tv_search_query.setText(data);
                }
            });
        }
    }

    @Override
    public void setPresenter(F_HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();
        Toast.makeText(getContext(), "item"+id, Toast.LENGTH_SHORT).show();
        if (id == R.id.action_settings) {
            Log.d(Constant.APP_TAG, "action_settings");
            return true;
        } else if (id == R.id.action_mymessages) {
            Log.d(Constant.APP_TAG, "action_mymessages");
            return true;
        } else if (id == android.R.id.home) {
            Log.d(Constant.APP_TAG, "home");
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            startActivity(intent);
        }
        return false;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        /* when the restaurant is clicked */
        void onRestaurantInteraction(RestaurantEntity restaurantEntity);
        void onAdsInteraction(AdsBanner ad);

        void onShowPic(SimplePicture.KabaShowPic pic);
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


    private void initViews(View rootView) {
        tv_search_query = rootView.findViewById(R.id.tv_search_query);
        ib_search_bt = rootView.findViewById(R.id.ib_search_bt);
        toolbar = rootView.findViewById(R.id.toolbar);
        slidingBanner_lilRound = rootView.findViewById(R.id.sliding_banner);
        or_6restaurant = rootView.findViewById(R.id.recyclerview_home_restaurant_icon);
        grid48View = rootView.findViewById(R.id.recyclerview_home_4_adspace);
        groupAds10 = rootView.findViewById(R.id.recyclerview_home_group_ads);
        or_newsfeed = rootView.findViewById(R.id.recyclerview_home_feeds);
        swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_1);
    }

}
