package tg.tmye.kaba.activity.FoodDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.cviews.SlidingBanner_LilRound;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Food.source.FoodTagRepository;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.source.RestaurantRepository;
import tg.tmye.kaba.data._OtherEntities.DaoSession;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


public class FoodDetailsActivity extends AppCompatActivity implements OnImageClickListener,
        AddDrinkDialogFragment.Listener, ConfirmTransactionDialogFragment.Listener {


    public static final String FOOD_ID = "FOOD_ID_VALUE";

    private static final int TAG_SPAN_COUNT = 4;


    private RestaurantEntity resto;
    Restaurant_Menu_FoodEntity foodEntity;


    private DaoSession daoSession;


    private class FoodDetailsLayoutHolder {

        private final SlidingBanner_LilRound slidingBanner;
        private final RecyclerView recyclerViewFoodTags;
        ImageButton ib_purchase_now;
        ImageView iv_add_a_drink;
        ImageView iv_resto_pic;
        TextView tv_food_name;
        TextView tv_food_price;
        TextView tv_restaurant_name;

        public FoodDetailsLayoutHolder (View itemView) {

            this.tv_food_name = itemView.findViewById(R.id.tv_food_name);
            this.tv_food_price = itemView.findViewById(R.id.tv_food_price);
            this.tv_restaurant_name = itemView.findViewById(R.id.tv_restaurant_name);
            this.ib_purchase_now = itemView.findViewById(R.id.ib_purchase_now);
            this.iv_resto_pic = itemView.findViewById(R.id.iv_restaurant_pic);
            this.iv_add_a_drink = findViewById(R.id.iv_add_a_drink);
            this.slidingBanner = findViewById(R.id.sliding_banner);
            this.recyclerViewFoodTags = findViewById(R.id.recyclerview_food_tags);
        }
    }

    FoodDetailsLayoutHolder holder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        holder = new FoodDetailsLayoutHolder(toolbar.getRootView());

        long food_id = getIntent().getLongExtra(FOOD_ID, -1);

        if (food_id == -1) {
            // show an error window because of the id that is wrong
            food_id = 1;
        }

        daoSession = ((MyKabaApp) getApplication()).getDaoSession();

        // get food entity
        foodEntity = daoSession.getRestaurant_Menu_FoodEntityDao().loadByRowId(food_id);

        // get food_tags
        List<Food_Tag> tags = FoodTagRepository.findByFoodId(daoSession.getFood_TagDao(), foodEntity.id);

        foodEntity.food_tags = tags;

        // get images of the food and init the food_details_activity
        initSlidingBanner();
//        initFoodTags();
        initNameFood_N_RestaurantNames();

        holder.iv_add_a_drink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AddDrinkDialogFragment.newInstance(8).show(getSupportFragmentManager(), "add_drink_dialog");
            }
        });

        holder.ib_purchase_now.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ConfirmTransactionDialogFragment.newInstance().show(getSupportFragmentManager(), "confirm_dialog");
            }
        });
    }

    private void initNameFood_N_RestaurantNames() {

        resto =  RestaurantRepository.RestaurantLocalDataSource
                .findRestoById(daoSession.getRestaurantEntityDao(), foodEntity.restaurant_id);

        if (resto == null)
            return;

        holder.tv_food_name.setText(foodEntity.title.toUpperCase());
        holder.tv_food_price.setText(foodEntity.price.toUpperCase());
        holder.tv_restaurant_name.setText(
                resto.restaurant_name.toUpperCase()
        );

        // load resto image
        GlideApp.with(FoodDetailsActivity.this)
                .load(Constant.SERVER_ADDRESS+resto.restaurant_logo)
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.iv_resto_pic);

        // init bottom stars
        initFoodRankingStars();

        // init images of the slider
    }

    private void initFoodRankingStars() {

        double stars = foodEntity.stars;

        ImageView iv_1 = findViewById(R.id.iv_star_1);
        ImageView iv_2 = findViewById(R.id.iv_star_2);
        ImageView iv_3 = findViewById(R.id.iv_star_3);
        ImageView iv_4 = findViewById(R.id.iv_star_4);

        if (stars == 0.0){
            iv_1.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_border_red_24dp);
        }
        if (stars == 0.5){
            iv_1.setImageResource(R.drawable.ic_star_half_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_border_red_24dp);
        }
        if (stars == 1.0) {
            iv_1.setImageResource(R.drawable.ic_star_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_border_red_24dp);
        }
        if (stars == 1.5) {
            iv_1.setImageResource(R.drawable.ic_star_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_half_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_border_red_24dp);
        }
        if (stars == 2.0){
            iv_1.setImageResource(R.drawable.ic_star_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_border_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_border_red_24dp);
        }
        if (stars == 2.5){
            iv_1.setImageResource(R.drawable.ic_star_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_half_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_border_red_24dp);
        }
        if (stars == 3.0){
            iv_1.setImageResource(R.drawable.ic_star_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_border_red_24dp);
        }
        if (stars == 3.5){
            iv_1.setImageResource(R.drawable.ic_star_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_half_red_24dp);
        }
        if (stars == 4.0){
            iv_1.setImageResource(R.drawable.ic_star_red_24dp);
            iv_2.setImageResource(R.drawable.ic_star_red_24dp);
            iv_3.setImageResource(R.drawable.ic_star_red_24dp);
            iv_4.setImageResource(R.drawable.ic_star_red_24dp);
        }
    }

    private void initSlidingBanner() {
        /* transform image items into kabashowpics */
        List<SimplePicture.KabaShowPic> kabaShowPics = new ArrayList<>();
        for (int i = 0; i < foodEntity.food_details_pictures.size(); i++) {
            SimplePicture.KabaShowPic kb = new SimplePicture.KabaShowPic(foodEntity.food_details_pictures.get(i));
            kabaShowPics.add(kb);
        }
        holder.slidingBanner.load((ArrayList)kabaShowPics);
    }


    /* clicking on images */
    @Override
    public void onClick(View view) {

    }

    public void geek(View view) {
        Toast.makeText(this, "ok", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClicked(int position) {

    }


    // finish activity for result
    public void finishForResult() {
        // send back the current one
        Intent data = new Intent();
        data.putExtra(RestaurantMenuActivity.RESTAURANT_VAL, resto);
        setResult(RestaurantMenuActivity.RESTAURANT_ITEM_RESULT, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            finishForResult();
            return true;
        } else if (id == R.id.action_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
