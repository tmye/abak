package tg.tmye.kaba.restaurant.cviews;

/**
 * By abiguime on 2020/6/19.
 * email: 2597434002@qq.com
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.HashMap;
import java.util.List;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant._commons.utils.UtilFunctions;
import tg.tmye.kaba.restaurant.activities.menu.Fragmentz.RestaurantSubMenuFragment;
import tg.tmye.kaba.restaurant.data.advert.AdsBanner;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.GlideApp;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;


/**
 * By abiguime on 2017/11/23.
 * email: 2597434002@qq.com
 */

public class SlidingBanner_LilRound extends FrameLayout implements Runnable  {

    // slidingbanner_sliding_lapse
    private static final long SLIDING_LAPSE = 4000;
    private boolean autoCompute = true;

    // viewpager_adapter
    private VpPageAdapter adapter;

    // parameters
    private boolean autoScroll = false;

    // set up the onclick listener of the thing
    private OnClickListener mPageOnclickListener;

    private int[] roundDrawableResources = {
            R.drawable.ic_round_white_empty_24dp,
            R.drawable.ic_round_white_full_24dp
    };

    // is viewpager touched
    private boolean vp_is_touched = false;

    // food details list
//    public static List<AdsBanner> food_details_pictures;

    // direction of the dragging
    private boolean leftToRight = true;


    private FragmentManager ourFragmentManager;

    /* is thread running */
    private boolean isRunning = false;


    public SlidingBanner_LilRound(@NonNull Context context) {
        super(context);
    }

    public SlidingBanner_LilRound(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.customSlider);
            autoCompute = a.getBoolean(R.styleable.customSlider_autocompute, true);
            a.recycle();
        }
    }

    public SlidingBanner_LilRound(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.customSlider);
            autoCompute = a.getBoolean(R.styleable.customSlider_autocompute, true);
            a.recycle();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidingBanner_LilRound(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.customSlider);
            autoCompute = a.getBoolean(R.styleable.customSlider_autocompute, true);
            a.recycle();
        }
    }

    /* get size of the rounds */
    AutoScrollViewPager autoScrollViewpager;

    LinearLayout lny_lil_round;

    ImageView[] iv_rounds;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // get every items and get them safe
        autoScrollViewpager = findViewById(R.id.viewpager_sliding_banner);
        lny_lil_round = findViewById(R.id.lny_lil_round);
//        food_details_pictures = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /* set up the size of the view here */
        if (autoCompute) {
            int width = UtilFunctions.getScreenSize(getContext())[0];
            int height = 9 * width / 16;
            setMeasuredDimension(width, height);
        }
    }

    private void inflateLilRonds(LinearLayout lny, int count) {

        lny.removeAllViews();

        LayoutInflater inf = LayoutInflater.from(getContext());

        iv_rounds = new ImageView[count];

        for (int i = 0; i < count; i++) {
            ImageView v = (ImageView) inf.inflate(R.layout.lil_round, lny, false);
            int width = getResources().getDimensionPixelSize(R.dimen.lil_round_size);
            int height = width;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.width = params.height = getResources().getDimensionPixelSize(R.dimen.lil_round_size);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.lil_round_margin);

            if (i == 0)
                v.setImageResource(roundDrawableResources[0]);

            iv_rounds[i] = v;
            v.setTag("lil_round"+i);
            lny.addView(v, params);
        }
    }


    public void load(List<AdsBanner> food_details_pictures, FragmentManager childFragmentManager) {

        // load pictures links into the viewpager -- and when we click, we are redirected to pages inside or outside
        // the application
        if (childFragmentManager != null)
            this.ourFragmentManager = childFragmentManager;
        else
            this.ourFragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();

//        this.food_details_pictures = food_details_pictures;

        if (adapter == null) {
            adapter = new VpPageAdapter(getContext(), food_details_pictures);
        } else {
            adapter.setData(food_details_pictures);
        }

        autoScrollViewpager.setAdapter(adapter);

        if (autoScrollViewpager.getAdapter() != null)
            autoScrollViewpager.getAdapter().notifyDataSetChanged();


        inflateLilRonds(lny_lil_round, adapter.getCount());
        autoScrollViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (iv_rounds != null)
                    for (int i = 0; i < iv_rounds.length; i++) {
                        if (i == position)
                            iv_rounds[i].setImageResource(roundDrawableResources[1]);
                        else
                            iv_rounds[i].setImageResource(roundDrawableResources[0]);
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                /* if scrolling, kill the process, and startit again once the scrollstate has changed */
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
//                        Log.d(Constant.APP_TAG, "SCROLL_STATE_IDLE");
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        Log.d(Constant.APP_TAG, "SCROLL_STATE_DRAGGING");
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
//                        Log.d(Constant.APP_TAG, "SCROLL_STATE_SETTLING");
                        break;
                }
            }
        });

        autoScrollViewpager.setCurrentItem(0, false);
        autoScrollViewpager.setOffscreenPageLimit(adapter.getCount());

        /* if there is no thread for this, we should just */
        if (!isRunning)
            run();
    }


    Handler mHandler = new Handler();

    @Override
    public void run() {

        isRunning = true;

        int newScrolledPosition = autoScrollViewpager.getCurrentItem();

        /* change direction */
        if (leftToRight && newScrolledPosition+1>=adapter.getCount()
                ||
                !leftToRight && newScrolledPosition == 0)
            leftToRight=(!leftToRight);

        if (leftToRight)
            newScrolledPosition+=1;
        else
            newScrolledPosition-=1;

        if (!vp_is_touched) {
            // slide once

//            Log.d("sliding", "sliding from "+ autoScrollViewpager.getCurrentItem()+" -- to "+newScrolledPosition);
            autoScrollViewpager.setCurrentItem(
                    newScrolledPosition
                    , true);
        } else {
//            Log.d("sliding", "sliding is touched. NOT SCROLL!! ");
        }

        /* if we are settling or have an idle state, we should not scroll */

        // every to sec we slide
        mHandler.postDelayed(this, SLIDING_LAPSE);
        // if the thread is killed, he must be created again!
    }

    HashMap<Integer, ImageFragment> fragBase;

    class VpPageAdapter extends FragmentStatePagerAdapter {

        private Context mContext;
        private List<AdsBanner> food_details_pictures;

        public VpPageAdapter(Context context, List<AdsBanner> food_details_pictures) {
            super(ourFragmentManager);
            this.mContext = context;
            this.food_details_pictures = food_details_pictures;
        }

        @Override
        public Fragment getItem(int position) {

            return ImageFragment.newInstance(food_details_pictures, food_details_pictures.get(position), position);
        }

        @Override
        public int getCount() {
            return food_details_pictures.size();
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        public void setData(List<AdsBanner> food_details_pictures) {
            this.food_details_pictures = food_details_pictures;
        }
    }

    // force the containing activity to keep an action listener
    private OnViewpagerInteractionListener mListener;


    public interface OnViewpagerInteractionListener {
        // TODO: Update argument type and restaurant_name
        void onPageInteraction(Uri uri);
    }


    @SuppressLint("ValidFragment")
    public static class ImageFragment extends Fragment {

        private AdsBanner ad;

        AdsBanner[] food_details_pictures;
        private int position = 0;

        public static ImageFragment newInstance(List<AdsBanner> food_details_pictures, AdsBanner adsBanner, int position) {

            ImageFragment imageFragment = new ImageFragment();
            Bundle args = new Bundle();
            args.putParcelable("ad", adsBanner);
            args.putInt("position", position);
            args.putParcelableArray("fdp", UtilFunctions.convertToArray(food_details_pictures));
            imageFragment.setArguments(args);
            return imageFragment;
        }

        public AdsBanner getAd() {
            return ad;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                ad = getArguments().getParcelable("ad");
                position = getArguments().getInt("position");
                food_details_pictures = (AdsBanner[]) getArguments().getParcelableArray("fdp");
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().finish();
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.sliding_banner_iv, container, false);
        }

        ImageView imageView;

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            imageView = view.findViewById(R.id.iv_image);
            String picPath = "";

            picPath = Constant.SERVER_ADDRESS + "/" + ad.pic;

            GlideApp.with(getContext())
                    .load(picPath)
                    .transition(GenericTransitionOptions.with(((MyRestaurantApp)getContext().getApplicationContext()).getGlideAnimation()))
//                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(imageView);

        }
    }
}
