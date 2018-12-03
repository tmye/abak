package tg.tmye.kaba.activity.Splash;

import android.content.Intent;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tg.tmye.kaba.R;

public class KabaPreviewSlidingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_title;
    private ViewPager viewpager;
    private Button bt_continue;

    private int[] roundDrawableResources = {
            R.drawable.ic_round_yellow_full_24dp,
            R.drawable.ic_round_yellow_empty_24dp,
    };

    ImageView[] iv_rounds;

    LinearLayout lny_lil_round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaba_preview_sliding);

        View mContentView = findViewById(R.id.full_screen_content);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        int[] images = new int[]{R.drawable.check_1_present, R.drawable.check_2_paye, R.drawable.check_3_livre, R.drawable.check_4_circle};

        final int[] titles = new int[]{R.string.title_preview_1, R.string.title_preview_2, R.string.title_preview_3,  R.string.title_preview_4};

        final int[] bg_colors = new int[]{};

        viewpager = findViewById(R.id.viewpager);
        tv_title = findViewById(R.id.tv_mini_title);
        lny_lil_round = findViewById(R.id.lny_lil_round);

        /* setup the button */
        bt_continue = findViewById(R.id.bt_continue);
        bt_continue.setOnClickListener(this);
        bt_continue.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_sliding_banner_right_arrow_color_primary, null),
                null, null, null);

        /* setup adapter for this. */
        final PreviewPagerAdapter adapter = new PreviewPagerAdapter(getSupportFragmentManager(), images);

        viewpager.setAdapter(adapter);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                /* run an animation to show that */
                tv_title.setText(titles[position]);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(600);
                AlphaAnimation uncoverAnimation = new AlphaAnimation(0.0f, 1f);
                uncoverAnimation.setDuration(600);

                AnimationSet as = new AnimationSet(true);
                as.setFillEnabled(true);
                as.setInterpolator(new BounceInterpolator());

                as.addAnimation(scaleAnimation);
                as.addAnimation(uncoverAnimation);

                tv_title.startAnimation(as);
            }

            @Override
            public void onPageSelected(int position) {

                if (iv_rounds != null)
                    for (int i = 0; i < iv_rounds.length; i++) {
                        if (i == position)
                            iv_rounds[i].setImageResource(roundDrawableResources[0]);
                        else
                            iv_rounds[i].setImageResource(roundDrawableResources[1]);
                    }

                if (position == adapter.getCount()-1) {
                    bt_continue.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        inflateLilRonds(lny_lil_round, adapter.getCount());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_continue:
                /* start home activity */
                SplashActivity.isNoMoreFirstTime(KabaPreviewSlidingActivity.this);
                startActivity(new Intent(KabaPreviewSlidingActivity.this, SplashActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        SplashActivity.isNoMoreFirstTime(KabaPreviewSlidingActivity.this);
        super.onDestroy();
    }

    class PreviewPagerAdapter extends PagerAdapter {

        private final int[] images;
        private final FragmentManager fragmentManager;

        public PreviewPagerAdapter (FragmentManager fragmentManager, int[] images) {

            this.fragmentManager = fragmentManager;
            this.images = images;
        }


        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View itemView = getLayoutInflater().inflate(R.layout.pager_item, container, false);

            ImageView imageView = itemView.findViewById(R.id.imageview);
            imageView.setImageResource(images[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
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
            else
                v.setImageResource(roundDrawableResources[1]);

            iv_rounds[i] = v;
            v.setTag("lil_round"+i);
            lny.addView(v, params);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    public void startActivityWithNoAnimation(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }


}
