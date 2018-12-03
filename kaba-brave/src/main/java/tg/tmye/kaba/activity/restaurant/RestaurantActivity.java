package tg.tmye.kaba.activity.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

import static tg.tmye.kaba.activity.menu.RestaurantMenuActivity.RESTAURANT;


public class RestaurantActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    /* */
    TextView tv_restaurant_name, tv_rd_description;

    /* restaurant main pic */
    CircleImageView circleMainImageview;

    /* restaurant theme pic */
    ImageView imageView_theme_image;


    private RestaurantEntity restaurantEntity;

    Button tv_enter_menu;

    TextView tv_rd_address,tv_rd_workingtime;

    private ImageView iv_promotion_image;

    CardView cardview_promotion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("");

        /* get restaurant entity */

        /* menu light restaurants must become full restaurants */

        restaurantEntity = getIntent().getParcelableExtra(RESTAURANT);

        if (restaurantEntity == null) {
            mToast("Sorry, this restaurant entity is stupid!");
            finish();
        }

        bindActivity();

        bindIcons();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        // init basic data
        initBasicData();

        mAppBarLayout.addOnOffsetChangedListener(this);

//        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        tv_enter_menu.setOnClickListener(this);
    }

    private void bindIcons() {
//        tv_enter_menu
//        tv_rd_address
//        tv_rd_workingtime
        tv_enter_menu.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_list_white_24dp, null),
                null, null, null);

        tv_rd_address.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_address_edit_blue_24dp, null),
                null,null, null);

        tv_rd_workingtime.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_working_time_black_24dp, null),
                null,null, null);
    }


    private void initBasicData() {

        /* text data */
        mTitle.setText(restaurantEntity.name);
        tv_restaurant_name.setText(restaurantEntity.name);
        tv_rd_workingtime.setText("   "+  restaurantEntity.working_hour); //UtilFunctions.strToHour(restaurantEntity.working_hour));
        tv_rd_address.setText("   "+restaurantEntity.address);
        tv_rd_description.setText(restaurantEntity.description);

        /* show if restaurant */

        /* restaurant main pic */
        GlideApp.with(this)
                .load(Constant.SERVER_ADDRESS +"/"+ restaurantEntity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(circleMainImageview);

        /* restaurant theme pic */
        GlideApp.with(this)
                .load(Constant.SERVER_ADDRESS +"/"+ restaurantEntity.theme_pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.dark_gray_placeholder)
                .centerCrop()
                .into(imageView_theme_image);

        /* if there is a promotion -- show it  in the restaurant ; otherwise, do not show it */
        CardView.LayoutParams promoParams = (CardView.LayoutParams) iv_promotion_image.getLayoutParams();
        promoParams.width = UtilFunctions.getScreenSize(this)[0];
        promoParams.height = (UtilFunctions.getScreenSize(this)[0]*9)/16;
        iv_promotion_image.setLayoutParams(promoParams);
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);

        imageView_theme_image = findViewById(R.id.main_imageview_placeholder);
        circleMainImageview = findViewById(R.id.main_cic);
        tv_restaurant_name = findViewById(R.id.tv_restaurant_name);
        tv_enter_menu = findViewById(R.id.tv_enter_menu);
        tv_rd_workingtime = findViewById(R.id.tv_rd_workingtime);
        tv_rd_address = findViewById(R.id.tv_rd_address);
        tv_rd_description = findViewById(R.id.tv_rd_description);
        iv_promotion_image = findViewById(R.id.iv_promotion_image);
        cardview_promotion = findViewById(R.id.cardview_promotion);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }/*{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_enter_menu:
                /* start restaurant menu activity */
                Intent intent = new Intent(this, RestaurantMenuActivity.class);
                intent.putExtra(RestaurantMenuActivity.RESTAURANT, restaurantEntity);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequestsRecursive();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequestsRecursive();
    }

}
