package tg.tmye.kaba.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.PreviewImageFragment;
import tg.tmye.kaba.activity.home.HomeActivity;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.evenement.Evenement;

import static tg.tmye.kaba.activity.home.HomeActivity.VIEW_INFO_EXTRA;

public class ImagePreviewActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String IMG_URL_TAG = "IMG_URL_TAG";
    public static final String EVENT_TAG = "EVENT_TAG";

    public String IMG_URL = "";

    // Bundle that will contain the transition start values
    private Bundle mStartValues;
    // Bundle that will contain the transition end values
    final private Bundle mEndValues = new Bundle();

    private int[] roundDrawableResources = {
            R.drawable.ic_round_white_empty_24dp,
            R.drawable.ic_round_white_full_24dp,
    };

    private PreviewImageAdapter myAdapter;

    ViewPager viewPager;
    private LinearLayout lny_lil_round;
    ImageView[] iv_rounds;
    ImageButton ib_close;

    /* Containing fragments */
    private HashMap<Object, Fragment> fragmentHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            // set an enter transition
            getWindow().setEnterTransition(new Explode());
            // set an exit transition
//            getWindow().setExitTransition(new Fade());
        }

        extractViewInfoFromBundle(getIntent());

        setContentView(R.layout.activity_preview_image);

        viewPager = findViewById(R.id.viewpager);

        ib_close = findViewById(R.id.ib_close);

        Bundle bundle = getIntent().getExtras();

        AdsBanner[] adsBanners = null/*(AdsBanner[]) getIntent().getParcelableArrayExtra(IMG_URL_TAG)*/;

        if (bundle.containsKey(IMG_URL_TAG)) {

            Parcelable[] a = bundle.getParcelableArray(IMG_URL_TAG);
            adsBanners = Arrays.copyOf(a, a.length, AdsBanner[].class);
            myAdapter = new PreviewImageAdapter(getSupportFragmentManager(), adsBanners);
        }

        Evenement[] evenements = null/*(Evenement[]) getIntent().getParcelableArrayExtra(EVENT_TAG)*/;

        if (bundle.containsKey(EVENT_TAG)) {

//            myAdapter = new PreviewImageAdapter(getSupportFragmentManager(), evenements);
            Parcelable[] a = bundle.getParcelableArray(EVENT_TAG);
            evenements = Arrays.copyOf(a, a.length, Evenement[].class);
            myAdapter = new PreviewImageAdapter(getSupportFragmentManager(), evenements);
        }

        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(myAdapter.getCount());

        lny_lil_round = findViewById(R.id.lny_lil_round);
        inflateLilRonds(lny_lil_round, myAdapter.adsBanners.length);
        initOnScrollListener();
        ib_close.setOnClickListener(this);
    }

    private void extractViewInfoFromBundle(Intent intent) {
        mStartValues = intent.getBundleExtra(VIEW_INFO_EXTRA);
        /* send the bundle to the first fragment that is created */
    }

    private void initOnScrollListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
        viewPager.setCurrentItem(0, false);
    }

    private void inflateLilRonds(LinearLayout lny, int count) {

        lny.removeAllViews();

        LayoutInflater inf = LayoutInflater.from(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_close:
                finish();
                break;
        }
    }


    class PreviewImageAdapter extends FragmentPagerAdapter {

        private final AdsBanner[] adsBanners;

        public PreviewImageAdapter(FragmentManager fm, AdsBanner[] adsBanners) {
            super(fm);
            this.adsBanners = adsBanners;
        }


        public PreviewImageAdapter(FragmentManager supportFragmentManager, Evenement[] evenements) {
            super(supportFragmentManager);
            this.adsBanners = evenements;
        }

        @Override
        public Fragment getItem(int position) {

            if (fragmentHashMap == null)
                fragmentHashMap = new HashMap<>();

            if (fragmentHashMap.get(position) == null) {
                fragmentHashMap.put(position, PreviewImageFragment.newInstance(adsBanners[position], mStartValues));
            }

            return fragmentHashMap.get(position);
        }

        @Override
        public int getCount() {
            return adsBanners.length;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreviewImageFragment fragment = (PreviewImageFragment) fragmentHashMap.get(viewPager.getCurrentItem());
        try {
            if (fragment != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    fragment.runExitAnimation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }
}

