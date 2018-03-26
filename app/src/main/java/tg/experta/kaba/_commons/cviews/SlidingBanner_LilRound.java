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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tg.experta.kaba.R;
import tg.experta.kaba._commons.utils.UtilFunctions;
import tg.experta.kaba.config.Constant;
import tg.experta.kaba.syscore.GlideApp;

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
    private List<String> food_details_pictures;


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

    public void load(List<String> food_details_pictures) {
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
        newScrolledPosition =  (newScrolledPosition+1 >= vp.getChildCount() ? 0 : newScrolledPosition+1);

        if (!vp_is_touched) {
            // slide once
            vp.setCurrentItem(
                    newScrolledPosition
                    , true);
        }
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

        private static final int SLIDE_COUNT = 3;
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
            GlideApp.with(mContext)
                    .load(Constant.SERVER_ADDRESS+ food_details_pictures.get(position))
                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(iv);

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
        // TODO: Update argument type and name
        void onPageInteraction(Uri uri);
    }

}
