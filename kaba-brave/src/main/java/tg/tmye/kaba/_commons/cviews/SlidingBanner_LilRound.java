package tg.tmye.kaba._commons.cviews;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.syscore.GlideApp;


/**
 * By abiguime on 2017/11/23.
 * email: 2597434002@qq.com
 */

public class SlidingBanner_LilRound extends FrameLayout implements Runnable, View.OnTouchListener {


    // slidingbanner_sliding_lapse
    private static final long SLIDING_LAPSE = 4000;

    // viewpager_adapter
    private PagerAdapter adapter;

    // parameters
    private boolean autoScroll = false;

    // set up the onclick listener of the thing
    private OnClickListener mPageOnclickListener;

    private int[] roundDrawableResources = {
            R.drawable.ic_round_white_empty_24dp,
            R.drawable.ic_round_white_full_24dp
    };

    // is viewpager touched
    private boolean vp_is_touched;

    // food details list
    private List<AdsBanner> food_details_pictures;

    // direction of the dragging
    private boolean leftToRight = true;


    public SlidingBanner_LilRound(@NonNull Context context) {
        super(context);
    }

    public SlidingBanner_LilRound(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingBanner_LilRound(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidingBanner_LilRound(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* get size of the rounds */


    ViewPager vp;

    LinearLayout lny_lil_round;

    ImageView[] iv_rounds;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // get every items and get them safe
        vp = findViewById(R.id.viewpager_sliding_banner);
        lny_lil_round = findViewById(R.id.lny_lil_round);

        food_details_pictures = new ArrayList<>();
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


            iv_rounds[i] = (ImageView) v;
            v.setTag("lil_round"+i);
            lny.addView(v, params);
        }
    }


    public void load(List<AdsBanner> food_details_pictures) {
        // load pictures links into the viewpager -- and when we click, we are redirected to pages inside or outside
        // the application
        this.food_details_pictures = food_details_pictures;
        adapter = new VpPageAdapter(getContext());
        vp.setAdapter(adapter);
        inflateLilRonds(lny_lil_round, adapter.getCount());
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

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
        vp.setOnTouchListener(this);
        Log.d("xxx", "loading slidingbanner");
        run();
    }

    Handler mHandler = new Handler();

    @Override
    public void run() {


        int newScrolledPosition = vp.getCurrentItem();

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
            vp.setCurrentItem(
                    newScrolledPosition
                    , true);
        }

        /* if we are settling or have an idle state, we should not scroll */

        // every to sec we slide
        mHandler.postDelayed(this, SLIDING_LAPSE);
        // if the thread is killed, he must be created again!
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN
                || motionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            vp_is_touched = true;
        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                motionEvent.getAction() == MotionEvent.ACTION_POINTER_UP) {
            vp_is_touched = false;
        }
        return false;
    }


    class VpPageAdapter extends PagerAdapter {

        private Context mContext;

        public VpPageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            ImageView iv = (ImageView) inflater.inflate(R.layout.sliding_banner_iv, collection, false);
            collection.addView(iv);

            // use picasso to compress the pictures  before getting them in the list
            String picPath = "";
//            if (food_details_pictures.get(position) instanceof AdsBanner) {
//            picPath = Constant.SERVER_ADDRESS+ ((AdsBanner)food_details_pictures.get(position)).adsBanner.path;
//            } else  if (food_details_pictures.get(position) instanceof SimplePicture) {
//                picPath = Constant.SERVER_ADDRESS+ ((SimplePicture)food_details_pictures.get(position)).path;
//            }

            picPath = Constant.SERVER_ADDRESS + "/" + ((AdsBanner)food_details_pictures.get(position)).name;

            GlideApp.with(mContext)
                    .load(picPath)
                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(iv);

            /* when clicked */
            if (food_details_pictures.get(position) instanceof AdsBanner) {

            }

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return food_details_pictures.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    // force the containing activity to keep an action listener
    private OnViewpagerInteractionListener mListener;


    public interface OnViewpagerInteractionListener {
        // TODO: Update argument type and restaurant_name
        void onPageInteraction(Uri uri);
    }

}
