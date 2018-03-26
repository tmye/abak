package tg.experta.kaba._commons.cviews;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import tg.experta.kaba.R;
import tg.experta.kaba.config.Constant;
import tg.experta.kaba.syscore.GlideApp;

/**
 * By abiguime on 2017/11/23.
 * email: 2597434002@qq.com
 */

public class SlidingBanner_Directionnal extends FrameLayout implements Runnable, View.OnClickListener {


    // slidingbanner_sliding_lapse
    private static final long SLIDING_LAPSE = 4000;

    // viewpager_adapter
    private PagerAdapter adapter;

    // parameters
    private boolean autoScroll = false;

    // set up the onclick listener of the thing
    private View.OnClickListener mPageOnclickListener;


    public SlidingBanner_Directionnal(@NonNull Context context) {
        super(context);
    }

    public SlidingBanner_Directionnal(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingBanner_Directionnal(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidingBanner_Directionnal(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    ViewPager vp;
    RelativeLayout nav_left;
    RelativeLayout nav_right;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // get every items and get them safe
        vp = findViewById(R.id.viewpager_sliding_banner);
        nav_left = findViewById(R.id.rel_nav_left);
        nav_right = findViewById(R.id.rel_nav_right);

        adapter = new VpPageAdapter(getContext());
        nav_left.setOnClickListener(this);
        nav_right.setOnClickListener(this);
    }

    public void load () {
        // load pictures links into the viewpager -- and when we click, we are redirected to pages inside or outside
        // the application
        vp.setAdapter(adapter);
        Log.d("xxx", "loading slidingbanner");
        run();
    }

    Handler mHandler = new Handler();

    @Override
    public void run() {

        int newScrolledPosition = vp.getCurrentItem();
        newScrolledPosition =  (newScrolledPosition+1 >= vp.getChildCount() ? 0 : newScrolledPosition+1);

        // slide once
        vp.setCurrentItem(
                newScrolledPosition
                , true);
        // every to sec we slide
        mHandler.postDelayed(this, SLIDING_LAPSE);
        // if the thread is killed, he must be created again!
    }

    @Override
    public void onClick(View view) {
        mHandler.removeCallbacks(this);
        int newScrolledPosition = vp.getCurrentItem();

        switch (view.getId()) {
            case R.id.rel_nav_left:
                newScrolledPosition =  (newScrolledPosition-1 <= 0 ? vp.getChildCount() : newScrolledPosition-1);
                vp.setCurrentItem(newScrolledPosition, true);
                break;
            case R.id.rel_nav_right:
                newScrolledPosition =  (newScrolledPosition+1 >= vp.getChildCount() ? 0 : newScrolledPosition+1);
                vp.setCurrentItem(newScrolledPosition, true);
                break;
        }

        if (view instanceof ImageView) {
            // get the image that is inside the tag, and call the function inside the activity

        }

        mHandler.postDelayed(this, SLIDING_LAPSE);
    }


    class VpPageAdapter extends PagerAdapter {

        private static final int SLIDE_COUNT = 3;
        private Context mContext;
        public String[] slidesLink = new String[SLIDE_COUNT];

        public VpPageAdapter(Context context) {
            mContext = context;
            for (int i = 0; i < SLIDE_COUNT; i++) {
                String tmp = Constant.SAMPLE_IMAGES_BANNER[  (new Random().nextInt(Constant.SAMPLE_IMAGES_BANNER.length))%Constant.SAMPLE_IMAGES_BANNER.length  ];
                slidesLink[i] = tmp;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            ImageView iv = (ImageView) inflater.inflate(R.layout.sliding_banner_iv, collection, false);
            collection.addView(iv);

            // use picasso to compress the pictures  before getting them in the list
            GlideApp.with(mContext)
                    .load(slidesLink[position])
                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(iv);

            iv.setOnClickListener(SlidingBanner_Directionnal.this);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return SLIDE_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    // force the containing activity to keep an action listener
    private OnViewpagerInteractionListener mListener;


    public interface OnViewpagerInteractionListener {
        // TODO: Update argument type and name
        void onPageInteraction(Uri uri);
    }

}
