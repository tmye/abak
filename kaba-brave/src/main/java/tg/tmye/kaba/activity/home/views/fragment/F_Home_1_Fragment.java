package tg.tmye.kaba.activity.home.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.GroupAdsAdapter;
import tg.tmye.kaba._commons.adapters.Home_1_MainRestaurantAdapter;
import tg.tmye.kaba._commons.cviews.GifFrameSpaceView;
import tg.tmye.kaba._commons.cviews.Grid48View;
import tg.tmye.kaba._commons.cviews.HomeFeedsAdapter;
import tg.tmye.kaba._commons.cviews.OffRecyclerview;
import tg.tmye.kaba._commons.cviews.SlidingBanner_LilRound;
import tg.tmye.kaba._commons.cviews.custom_swipe.HomeCustom_SwipeRefreshLayout;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba._commons.decorator.ListVerticalSpaceDecorator;
import tg.tmye.kaba.activity.Splash.SplashActivity;
import tg.tmye.kaba.activity.home.HomeActivity;
import tg.tmye.kaba.activity.home.contracts.F_HomeContract;
import tg.tmye.kaba.activity.home.presenter.F_Home_1_Presenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Feeds.NewsFeed;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.Group10AdvertItem;


public class F_Home_1_Fragment extends BaseFragment implements F_HomeContract.View, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    /* use butterkniffe */
    LinearLayoutCompat lny_error;
    NestedScrollView nested_main_content;


    /* views */
    SlidingBanner_LilRound slidingBanner_lilRound; // home top sliding
    OffRecyclerview or_6restaurant;
    Grid48View grid48View;
    OffRecyclerview groupAds10;
    OffRecyclerview or_newsfeed;
    HomeCustom_SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_search_query;
    ImageButton ib_search_bt;
    Button bt_tryagain;
    Button bt_all_restaurants;

    /* adapters */
    Home_1_MainRestaurantAdapter home_1_mainRestaurantAdapter;


    private OnFragmentInteractionListener mListener;

    /* home 1 presenter */
    private F_Home_1_Presenter presenter;
    private Toolbar toolbar;
    private String searchHint = null;

    GifFrameSpaceView frame_gif_space;

    public F_Home_1_Fragment() {
        // Required empty public constructor
    }

    boolean is_first_loading = true;

    @Override
    public void onResume() {
        super.onResume();
        /* if data was already loaded do nothing */
        if (searchHint == null || searchHint.trim().length() == 0)
            if (presenter != null)
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
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_home_1_;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // inflate the views with all the available data
            initViews(view);
            buildUpDrawables();

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    presenter.updateView();
                }
            });

            tv_search_query.setOnClickListener((HomeActivity) getActivity());
            bt_tryagain.setOnClickListener(this);
            bt_all_restaurants.setOnClickListener(((HomeActivity) getActivity()));
        } catch (Exception e) {
            e.printStackTrace();

            /* restart activity */
            restartThisActivity();
        }
    }

    private void restartThisActivity() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        startActivity(intent);
    }

    private void buildUpDrawables() {
        bt_all_restaurants.setCompoundDrawablesWithIntrinsicBounds(null, null,
                VectorDrawableCompat.create(getResources(), R.drawable.ic_chevron_right_red_24dp, null),
                null);
    }


    /* passive functions */
    @Override
    public void showMainSliding(final List<AdsBanner> ads) {

        is_first_loading = false;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                slidingBanner_lilRound.load(ads,
                        ((F_Home_1_Fragment.OnFragmentInteractionListener)getActivity()), getChildFragmentManager());
            }
        });
    }

    @Override
    public void inflateMainRestaurants(final List<RestaurantEntity> restaurantEntityList) {

        is_first_loading = false;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int spanCount = 3;

                if (restaurantEntityList.size() == 2) {
                    spanCount = 2;
                }

                if (restaurantEntityList.size() == 1) {
                    spanCount = 1;
                }

                if (restaurantEntityList.size() == 0) {
                    /* do something with 0 */
                    or_6restaurant.setVisibility(View.GONE);
                } else {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount);
                    home_1_mainRestaurantAdapter = new Home_1_MainRestaurantAdapter(getContext(), restaurantEntityList, mListener);
                    or_6restaurant.setLayoutManager(gridLayoutManager);
                    or_6restaurant.setAdapter(home_1_mainRestaurantAdapter);
                    or_6restaurant.setVisibility(View.VISIBLE);
                }
            }
        });
    }


//    frame_gif_space

    @Override
    public void inflateMain48(final AdsBanner best_sellers, final AdsBanner evenements) {

        is_first_loading = false;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                grid48View.setVisibility(View.VISIBLE);
//                grid48View.inflateAds(adsBanners, mListener);

                if (evenements != null && best_sellers!= null) {
                    evenements.type = AdsBanner.EVENEMENT;
                    best_sellers.type = AdsBanner.BEST_SELLER;

                    /* ads banner to best sellers */
                    AdsBanner[] banners = new AdsBanner[]{best_sellers, evenements};
                    grid48View.inflateAds(Arrays.asList(banners), (OnFragmentInteractionListener) getContext());
                    /* inflate with specific listener */
                }
            }
        });
    }

    @Override
    public void inflateGifSpace(final List<AdsBanner> gif) {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                /* create a custom space for */
                if (gif != null && gif.size() > 0) {
                    frame_gif_space.load(gif,
                            ((F_Home_1_Fragment.OnFragmentInteractionListener) getActivity()), getChildFragmentManager());
                    frame_gif_space.setVisibility(View.VISIBLE);
                } else {
                    frame_gif_space.setVisibility(View.GONE);
                }
            }

        });
    }


    @Override
    public void inflateGroupsPubLongList(final List<Group10AdvertItem> group10AdvertItems) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (group10AdvertItems == null || group10AdvertItems.size() == 0) {
                    groupAds10.setVisibility(View.GONE);
                } else {
                    groupAds10.setVisibility(View.VISIBLE);
                    groupAds10.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    if (groupAds10.getItemDecorationCount() == 0)
                        groupAds10.addItemDecoration(new ListVerticalSpaceDecorator(getContext().getResources().getDimensionPixelSize(R.dimen.adgroup_bottom_space)));
                    groupAds10.setAdapter(new GroupAdsAdapter(getContext(), group10AdvertItems,
                            ((F_Home_1_Fragment.OnFragmentInteractionListener)getActivity())));
                }
            }
        });
    }


    @Override
    public void inflateFeedsList(List<NewsFeed> newsFeeds) {

        if (newsFeeds == null || newsFeeds.size() == 0) {
            or_newsfeed.setVisibility(View.GONE);
        } else {
            or_newsfeed.setVisibility(View.VISIBLE);
            or_newsfeed.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            if (or_newsfeed.getItemDecorationCount() == 0)
                or_newsfeed.addItemDecoration(new CommandInnerFoodLineDecorator(ContextCompat.getDrawable(getContext(), R.drawable.feeds_item_divider)));
            or_newsfeed.setAdapter(new HomeFeedsAdapter(getContext(), null));
        }
    }

    @Override
    public void showLoadingProgress(final boolean isVisible) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_error.setVisibility(View.GONE);
                nested_main_content.setVisibility(View.GONE);
                swipeRefreshLayout.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (!is_first_loading)
                                    swipeRefreshLayout.setRefreshing(isVisible);
                                try {((HomeActivity) getActivity()).hideMainLoading();} catch (Exception e){e.printStackTrace();}
                            }});
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        /* make a push of the error */
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if ((groupAds10 != null && groupAds10.getChildCount() > 0)
                        || (home_1_mainRestaurantAdapter != null && home_1_mainRestaurantAdapter.getItemCount() > 0)
                        ) {
                    nested_main_content.setVisibility(View.VISIBLE);
                    lny_error.setVisibility(View.GONE);
                } else {
                    /* check if the views contains something */
                    nested_main_content.setVisibility(View.GONE);
                    lny_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void sysError(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ((groupAds10 != null && groupAds10.getChildCount() > 0)
                        || (home_1_mainRestaurantAdapter != null && home_1_mainRestaurantAdapter.getItemCount() > 0)
                        ) {
                    nested_main_content.setVisibility(View.VISIBLE);
                    lny_error.setVisibility(View.GONE);
                } else {
                    /* check if the views contains something */
                    nested_main_content.setVisibility(View.GONE);
                    lny_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void showMainHint(final String data) {
        if (data != null && data.trim().length()>0) {
            this.searchHint = data;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_search_query.setText(searchHint);
                }
            });
        }
    }

    @Override
    public void hideErrorPage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_error.setVisibility(View.GONE);
                nested_main_content.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void setPresenter(F_HomeContract.Presenter presenter) {
        this.presenter = (F_Home_1_Presenter) presenter;
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

        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                /* reload */
                if (presenter != null)
                    presenter.updateView();
                else
                    restartApp();
                break;
        }
    }

    private void restartApp() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        startActivity(intent);
        getActivity().finish();
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

        /* when the restaurant is clicked */
        void onRestaurantInteraction(RestaurantEntity restaurantEntity);
//        void onAdsInteraction(AdsBanner[] ads);
        void onAdsInteraction(AdsBanner ad);

        void onComingSoonInteractionListener(RestaurantEntity resto);
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
//        ib_search_bt = rootView.findViewById(R.id.ib_search_bt);
        toolbar = rootView.findViewById(R.id.toolbar);
        slidingBanner_lilRound = rootView.findViewById(R.id.sliding_banner);
        or_6restaurant = rootView.findViewById(R.id.recyclerview_home_restaurant_icon);
        grid48View = rootView.findViewById(R.id.recyclerview_home_4_adspace);
        groupAds10 = rootView.findViewById(R.id.recyclerview_home_group_ads);
        or_newsfeed = rootView.findViewById(R.id.recyclerview_home_feeds);
        swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_1);

        lny_error = rootView.findViewById(R.id.lny_error);
        nested_main_content = rootView.findViewById(R.id.nested_main_content);
        bt_tryagain = rootView.findViewById(R.id.bt_tryagain);
        bt_all_restaurants = rootView.findViewById(R.id.bt_all_restaurants);

        frame_gif_space = rootView.findViewById(R.id.frame_gif_space);
    }

}
