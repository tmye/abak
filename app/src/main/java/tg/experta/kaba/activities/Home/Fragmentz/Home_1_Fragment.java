package tg.experta.kaba.activities.Home.Fragmentz;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.experta.kaba.R;
import tg.experta.kaba._commons.OnImageClickListener;
import tg.experta.kaba._commons.adapters.GroupAdsAdapter;
import tg.experta.kaba._commons.cviews.Ad_4_8_View;
import tg.experta.kaba._commons.cviews.SlidingBanner_LilRound;
import tg.experta.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.experta.kaba._commons.decorator.ListVerticalSpaceDecorator;
import tg.experta.kaba.activities.Home.HomeActivity;
import tg.experta.kaba.config.Constant;
import tg.experta.kaba.data.Feeds.Feeds;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;
import tg.experta.kaba.data.advert.ProductAdvertItem;
import tg.experta.kaba.syscore.GlideApp;


public class Home_1_Fragment extends BaseFragment {

    // Constants
    private static final int MENU_SPAN_COUNT = 3;

    private static final String DAILY_RESTAURANTS = "DAILY_RESTAURANTS";

    // restaurants
    private MainRestaurantIconAdapter mainRestaurantIconAdapter;


    public static Home_1_Fragment instantiate (Context ctx, List<RestaurantEntity> restoz) {

        Home_1_Fragment frg = new Home_1_Fragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DAILY_RESTAURANTS, (ArrayList<? extends Parcelable>) restoz);
        frg.setArguments(args);
        return frg;
    }

    public Home_1_Fragment() {
        // Required empty public constructor
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daily_restaurants = getArguments().getParcelableArrayList(DAILY_RESTAURANTS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_1_, container, false);
    }

    // content variables
    List<RestaurantEntity> daily_restaurants;
    List<ProductAdvertItem> ad_4_6;

    // views
    RecyclerView recyclerview_home_restaurant_icon;
    SlidingBanner_LilRound slidingBanner;
    HomeActivity activity;
    Ad_4_8_View recyclerview_home_4_adspace;
    RecyclerView recyclerview_home_feeds;
    RecyclerView recyclerView_home_GroupAdsAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview_home_restaurant_icon = view.findViewById(R.id.recyclerview_home_restaurant_icon);
        slidingBanner = view.findViewById(R.id.sliding_banner);
        recyclerview_home_4_adspace = view.findViewById(R.id.recyclerview_home_4_adspace);
        recyclerview_home_feeds = view.findViewById(R.id.recyclerview_home_feeds);
        recyclerView_home_GroupAdsAdapter = view.findViewById(R.id.recyclerview_home_group_ads);

        activity = (HomeActivity) getActivity();

        recyclerview_home_4_adspace.inflateAds(null);
        loadRestaurantList();
        fakeLoadSlidingBanner();
        fakeLoadGroupAds();
        fakeLoadFeeds();
    }

    private void fakeLoadGroupAds() {
        recyclerView_home_GroupAdsAdapter.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));
        recyclerView_home_GroupAdsAdapter.addItemDecoration(new ListVerticalSpaceDecorator(getContext().getResources().getDimensionPixelSize(R.dimen.adgroup_bottom_space)));
        recyclerView_home_GroupAdsAdapter.setAdapter(new GroupAdsAdapter(activity));
    }

    private void fakeLoadFeeds () {
        recyclerview_home_feeds.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));
        recyclerview_home_feeds.addItemDecoration(new CommandInnerFoodLineDecorator(ContextCompat.getDrawable(activity, R.drawable.feeds_item_divider)));
        recyclerview_home_feeds.setAdapter(new HomeFeedsAdapter(activity, null));
    }

    private void fakeLoadSlidingBanner() {

        // create a customview viewgroup, that contains a viewpager and navigation button
        List<String> ls = Arrays.asList(Constant.SAMPLE_IMAGES_BANNER);
        slidingBanner.load(ls);
        slidingBanner.setOnClickListener((OnImageClickListener)activity);
    }

    private void loadRestaurantList() {

        LinearLayoutManager lnyMng = new GridLayoutManager(getContext(), MENU_SPAN_COUNT);
        mainRestaurantIconAdapter = new MainRestaurantIconAdapter(getContext(), daily_restaurants);
        recyclerview_home_restaurant_icon.setLayoutManager(lnyMng);
        recyclerview_home_restaurant_icon.setAdapter(mainRestaurantIconAdapter);
    }

    public void updateDailyResto(List<RestaurantEntity> daily_restoz) {

        this.daily_restaurants = daily_restoz;
        mainRestaurantIconAdapter.updateData(daily_restaurants);
    }



    /* main restaurant 3+3 icons */
    class MainRestaurantIconAdapter extends RecyclerView.Adapter<MainRestaurantIconAdapter.ViewHolder> {

        private List<RestaurantEntity> data;
        private final Context ctx;

        public MainRestaurantIconAdapter(Context context, List<RestaurantEntity> data) {

            this.ctx = context;
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.main_restaurant_icon, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {


            final RestaurantEntity item = data.get(position);

            // bind the data.
            holder.tv_resto_name.setText(item.restaurant_name);

            GlideApp.with(ctx)
                    .load(Constant.SERVER_ADDRESS+item.restaurant_logo)
                    .placeholder(R.drawable.kaba_pic)
                    .centerCrop()
                    .into(holder.iv_resto_icon);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onRestaurantInteraction(
                            item
                    );
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void updateData(List<RestaurantEntity> daily_restaurants) {
            this.data = daily_restaurants;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public View rootView;
            public TextView tv_resto_name;
            public ImageView iv_resto_icon;

            public ViewHolder(View itemView) {
                super(itemView);
                this.rootView = itemView;
                this.tv_resto_name = itemView.findViewById(R.id.tv_restaurant_name);
                this.iv_resto_icon = itemView.findViewById(R.id.iv_restaurant_icon);
            }
        }
    }

    /* feeds adapter */
    class HomeFeedsAdapter extends RecyclerView.Adapter<HomeFeedsAdapter.ViewHolder> {

        private final List<Feeds> feeds;
        private final Context ctx;

        public HomeFeedsAdapter (Context ctx, List<Feeds> feeds) {
            this.ctx = ctx;
            this.feeds = feeds;
        }

        @Override
        public HomeFeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HomeFeedsAdapter.ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.feed_layout_item, parent, false));
        }

        @Override
        public void onBindViewHolder(HomeFeedsAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return MENU_SPAN_COUNT+3;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }

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


    public interface OnFragmentInteractionListener {
        void onRestaurantInteraction(RestaurantEntity entity);
        void onProductAddPressed (ProductAdvertItem productAdvertItem);
    }


}
