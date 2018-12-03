package tg.tmye.kaba._commons.cviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.intf.ImagePreviewerListener;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.home.views.fragment.F_Home_1_Fragment;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


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


    public void load(List<AdsBanner> food_details_pictures, F_Home_1_Fragment.OnFragmentInteractionListener listener, FragmentManager childFragmentManager) {

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

            return ImageFragment.newInstance(food_details_pictures, food_details_pictures.get(position));
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

        public static ImageFragment newInstance(List<AdsBanner> food_details_pictures, AdsBanner adsBanner) {

            ImageFragment imageFragment = new ImageFragment();
            Bundle args = new Bundle();
            args.putParcelable("ad", adsBanner);
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
            ad = getArguments().getParcelable("ad");
            food_details_pictures = (AdsBanner[]) getArguments().getParcelableArray("fdp");
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
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getContext().getApplicationContext()).getGlideAnimation()  ))
//                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(imageView);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {

                        /* send a list of all of them */
                        ((ImagePreviewerListener) getContext()).onShowPic(imageView, food_details_pictures);
                    }
                }
            });
        }
    }
}
