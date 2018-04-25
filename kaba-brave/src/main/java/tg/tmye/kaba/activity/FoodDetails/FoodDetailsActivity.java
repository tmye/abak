package tg.tmye.kaba.activity.FoodDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.cviews.SlidingBanner_LilRound;
import tg.tmye.kaba.activity.FoodDetails.contract.FoodDetailsContract;
import tg.tmye.kaba.activity.FoodDetails.frag.AddDrinkDialogFragment;
import tg.tmye.kaba.activity.FoodDetails.frag.ConfirmTransactionDialogFragment;
import tg.tmye.kaba.activity.FoodDetails.presenter.FoodDetailPresenter;
import tg.tmye.kaba.activity.home.views.fragment.F_Home_1_Fragment;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Food.source.FoodRepository;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.LightRestaurant;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


public class FoodDetailsActivity extends AppCompatActivity implements OnImageClickListener,
        AddDrinkDialogFragment.Listener,
        ConfirmTransactionDialogFragment.Listener,
        F_Home_1_Fragment.OnFragmentInteractionListener,
        FoodDetailsContract.View {


    private static final int TAG_SPAN_COUNT = 4;

    public static final String FOOD = "FOOD_ENTITY";
    public static final String RESTAURANT_ENTITY = "RESTAURANT_ENTITY";
    private static final String RESTAURANT_SIMPLE_DRINKS = "RESTAURANT_SIMPLE_DRINKS";


    private RestaurantEntity resto;
    Restaurant_Menu_FoodEntity foodEntity;

    FoodRepository foodRepository;

    /* presenter */
    FoodDetailPresenter presenter;

    /* menu */
    private Menu menu;

    /* dialog fragment */
    private AddDrinkDialogFragment drinkDialogFragment;

    private SelectedDrinksAdapter selectedDrinksAdapter;


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRestaurantInteraction(LightRestaurant restaurantEntity) {

    }

    @Override
    public void onAdsInteraction(AdsBanner ad) {

    }

    @Override
    public void onShowPic(SimplePicture.KabaShowPic pic) {

    }

    @Override
    public void networkError() {

    }

    @Override
    public void showErrorPage(boolean isShowed) {

    }

    @Override
    public void setFavorite(final int isFavorite) {
        /*  */

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (menu != null) {
                    int icon = isFavorite == 0 ? R.drawable.ic_star_white_transparent_24dp : R.drawable.ic_star_white_full_24dp;
                    menu.getItem(0).setIcon(getResources().getDrawable(icon));
                }
            }
        });
    }

    @Override
    public void setPresenter(FoodDetailsContract.Presenter presenter) {

    }

    @Override
    public void onDrinkSelected(Restaurant_Menu_FoodEntity entity) {

        /*  */
        boolean isFound = false;
        Object[] keys = food_id_quantity.keySet().toArray();
        if (keys != null && keys.length != 0) {
            for (int i = 0; i < keys.length; i++) {

                if ((entity.id) == ((int)keys[i])) {
                    food_id_quantity.put((Integer) keys[i],
                            /* incrementer */
                            food_id_quantity.get(keys[i])+1
                    );
                    isFound = true;
                }
            }
        }

        if (!isFound) {
            /* add it to the map */
            food_id_quantity.put((int) entity.id, 1);
        }

        /* check if we should show or not the thing */
        holder.cardView_add_drinks.setVisibility(View.VISIBLE);

        /* work on a recyclerview XX listview that shows the list of drinks */
        updateSelectedDrinkRecyclerView();
    }

    private void updateSelectedDrinkRecyclerView() {

        if (selectedDrinksAdapter == null) {
            selectedDrinksAdapter = new SelectedDrinksAdapter();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            holder.recyclerView_add_drinks.setLayoutManager(linearLayoutManager);
            holder.recyclerView_add_drinks.setAdapter(selectedDrinksAdapter);
        } else {
            selectedDrinksAdapter.notifyDataSetChanged();
        }
    }


    private class FoodDetailsLayoutHolder {

        private final SlidingBanner_LilRound slidingBanner;
        private final ImageButton ib_add_to_basket;
        ImageButton ib_purchase_now;
        ImageView iv_add_a_drink;
        ImageView iv_resto_pic;
        TextView tv_food_name;
        TextView tv_food_price;
        TextView tv_restaurant_name;

        /* cardview for drinks */
        CardView cardView_add_drinks;

        /* recyclerview add drinks */
        RecyclerView recyclerView_add_drinks;

        /* */
        ImageButton ib_quantity_add;
        ImageButton ib_quantity_del;

        /* */
        EditText ed_quantity;

        public FoodDetailsLayoutHolder (View itemView) {

            this.tv_food_name = itemView.findViewById(R.id.tv_food_name);
            this.tv_food_price = itemView.findViewById(R.id.tv_food_price);
            this.tv_restaurant_name = itemView.findViewById(R.id.tv_restaurant_name);
            this.ib_purchase_now = itemView.findViewById(R.id.ib_purchase_now);
            this.iv_resto_pic = itemView.findViewById(R.id.iv_restaurant_pic);
            this.iv_add_a_drink = findViewById(R.id.iv_add_a_drink);
            this.ib_add_to_basket = findViewById(R.id.ib_add_to_basket);
            this.slidingBanner = findViewById(R.id.sliding_banner);

            this.cardView_add_drinks = itemView.findViewById(R.id.cardview_add_drink);
            this.recyclerView_add_drinks = itemView.findViewById(R.id.recyclerview_add_drink);
//            this.recyclerViewFoodTags = findViewById(R.id.recyclerview_food_tags);

            this.ib_quantity_add = itemView.findViewById(R.id.ib_quantity_add);
            this.ib_quantity_del = itemView.findViewById(R.id.ib_quantity_del);
            this.ed_quantity = itemView.findViewById(R.id.ed_quantity);
        }

        public int getSelectedQuantity() {
            return Integer.valueOf(ed_quantity.getText().toString());
        }
    }

    FoodDetailsLayoutHolder holder;

    /* variable that contains all the products that we want to buy and their quantity */
    Map<Integer, Integer> food_id_quantity;

    /* List of the drinks entities (food) */
    List<Restaurant_Menu_FoodEntity> restaurant_menu_drinks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        holder = new FoodDetailsLayoutHolder(toolbar.getRootView());

        /* send food entity */
        /* restaurant entity also */
        foodEntity = getIntent().getParcelableExtra(FOOD);
        resto = getIntent().getParcelableExtra(RESTAURANT_ENTITY);
        restaurant_menu_drinks = getIntent().getParcelableArrayListExtra(RESTAURANT_SIMPLE_DRINKS);

        if (restaurant_menu_drinks == null || restaurant_menu_drinks.size() == 0) {
            restaurant_menu_drinks = FoodRepository.fakeSimpleDrinks(3);
        }

        /* check food id ? food entity */
        if (foodEntity == null) {
            // show an error window because of the id that is wrong
            Toast.makeText(this, "No food id sent to this activity", Toast.LENGTH_LONG).show();
            finish();
        }

        if (resto == null) {

            Toast.makeText(this, "No valid restaurant sent to this activity", Toast.LENGTH_LONG).show();
            finish();
        }
        initSlidingBanner();
        initNameFood_N_RestaurantNames();

        food_id_quantity = new HashMap<>();

        foodRepository = new FoodRepository(this);
        presenter = new FoodDetailPresenter(this, foodRepository);

        holder.ib_add_to_basket.setOnClickListener(this);
        holder.iv_add_a_drink.setOnClickListener(this);
        holder.ib_quantity_add.setOnClickListener(this);
        holder.ib_quantity_del.setOnClickListener(this);
        holder.ib_purchase_now.setOnClickListener(this);
    }


    private void initNameFood_N_RestaurantNames() {

        if (resto == null)
            return;

        holder.tv_food_name.setText(foodEntity.name.toUpperCase());
        holder.tv_food_price.setText(foodEntity.price.toUpperCase());
        holder.tv_restaurant_name.setText(
                resto.name.toUpperCase()
        );

        // load resto image
        GlideApp.with(FoodDetailsActivity.this)
                .load(Constant.SERVER_ADDRESS+"/"+resto.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
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
        List<AdsBanner> adsBanners = new ArrayList<>();
        for (int i = 0; i < foodEntity.food_details_pictures.size(); i++) {
            AdsBanner adsBanner = new AdsBanner();
            adsBanner.name = foodEntity.food_details_pictures.get(i);
            adsBanners.add(adsBanner);
        }
        holder.slidingBanner.load(adsBanners, this, getSupportFragmentManager());
    }

    /* clicking on images */
    @Override
    public void onClick(View view) {

        int quantity;

        switch (view.getId()) {
            case R.id.ib_purchase_now:
                purchaseNow();
                break;
            case R.id.ib_add_to_basket:
                /* launch fragment to define how much */
                addToBasket();
                break;
            case R.id.iv_add_a_drink:
                /* launch fragment to define how much */
                addDrinks(restaurant_menu_drinks);
                break;
            case R.id.ib_quantity_add:
                quantity = (Integer.valueOf(holder.ed_quantity.getText().toString()));
                if (quantity == 9) {
                    mToast(getResources().getString(R.string.quantity_to_much));
                } else {
                    quantity++;
                    holder.ed_quantity.setText(""+quantity);
                }
                break;
            case R.id.ib_quantity_del:
                quantity = (Integer.valueOf(holder.ed_quantity.getText().toString()));
                if (quantity == 1) {
                    mToast(getResources().getString(R.string.quantity_cannot_less));
                } else {
                    quantity--;
                    holder.ed_quantity.setText(""+quantity);
                }
                break;
        }
    }

    private void purchaseNow() {
        confirmSelectionBox(ConfirmCommandDetailsActivity.ACTION_BUY);
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void addToBasket() {

        /* solutions simples */
        confirmSelectionBox(ConfirmCommandDetailsActivity.ACTION_BASKET);
        /* show a box that confirms the list of thing to send to the basket */
        /* if we have to add to basket, we'll just add the food + drinks in the basket */
    }

    /**
     * args.
     *  - buy
     *  - addToBasket
     */
    private void confirmSelectionBox(int action) {

        HashMap<Restaurant_Menu_FoodEntity, Integer> selection_map = new HashMap<>();

        selection_map.put(foodEntity, holder.getSelectedQuantity());

        Object[] keys = food_id_quantity.keySet().toArray();
        if (keys != null && keys.length != 0) {
            for (int i = 0; i < keys.length; i++) {
                selection_map.put(getDrinkEntityWithId((int)keys[i]), food_id_quantity.get(keys[i]));
            }
        }

        Intent intent = new Intent(this, ConfirmCommandDetailsActivity.class);
        intent.putExtra(ConfirmCommandDetailsActivity.DATA, selection_map);
        intent.putExtra(ConfirmCommandDetailsActivity.ACTION, action);
        startActivity(intent);
    }

    private void addDrinks(List<Restaurant_Menu_FoodEntity> restaurant_menu_drinks) {

        if (drinkDialogFragment == null)
            drinkDialogFragment = AddDrinkDialogFragment.newInstance(restaurant_menu_drinks);
        drinkDialogFragment.show(getSupportFragmentManager(), AddDrinkDialogFragment.TAG);
    }


    // finish activity for result
    public void finishForResult() {
        // send back the current one
        Intent data = new Intent();
        data.putExtra(RestaurantMenuActivity.RESTAURANT, resto);
        setResult(RestaurantMenuActivity.RESTAURANT_ITEM_RESULT, data);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_details, menu);
        this.menu = menu;
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
        } else if (id == R.id.action_favorite) {
            // launch presenter.
            presenter.setFavorite(foodEntity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class SelectedDrinksAdapter extends RecyclerView.Adapter<SelectedDrinksAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_drink_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            /* food idz */
            Object[] objects = food_id_quantity.keySet().toArray();

            Restaurant_Menu_FoodEntity drinkEntity = getDrinkEntityWithId((Integer) objects[position]);

            holder.tv_name.setText(drinkEntity.name.toUpperCase());

            GlideApp.with(FoodDetailsActivity.this)
                    .load(Constant.SERVER_ADDRESS+"/"+drinkEntity.pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                    .centerCrop()
                    .into(holder.iv_pic);

            holder.tv_quantity.setText(
                    "X "+ ((int)food_id_quantity.get(objects[position]))
            );

            holder.itemView.setOnClickListener(new OnDecreaseDrinkClickListener(position));
        }

        @Override
        public int getItemCount() {
            return food_id_quantity.keySet().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            TextView tv_quantity;
            TextView tv_name;
            ImageView iv_pic;

            public ViewHolder(View itemView) {
                super(itemView);

                this.iv_pic = itemView.findViewById(R.id.iv_pic);
                this.tv_quantity = itemView.findViewById(R.id.tv_drink_count);
                this.tv_name = itemView.findViewById(R.id.tv_drink_name);
            }

        }

        private class OnDecreaseDrinkClickListener implements View.OnClickListener {
            private final int position;

            public OnDecreaseDrinkClickListener(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View view) {
                /*  */
                /* get the map, and decrease */

                Object[] keys = food_id_quantity.keySet().toArray();
                int quantity = food_id_quantity.get(keys[position]);
                quantity --;
                food_id_quantity.put((Integer) keys[position], quantity);
                if (quantity == 0) {
                 /* remove the entry from the map */
                    food_id_quantity.remove(keys[position]);
                }
                if (food_id_quantity.keySet().size() > 0)
                    selectedDrinksAdapter.notifyDataSetChanged();
                else
                    holder.cardView_add_drinks.setVisibility(View.GONE);
            }
        }
    }

    private Restaurant_Menu_FoodEntity getDrinkEntityWithId(int drink_id) {

            /* it is an int */
        for (int i = 0; i < restaurant_menu_drinks.size(); i++) {
            if (restaurant_menu_drinks.get(i).id == drink_id) {
                return restaurant_menu_drinks.get(i);
            }
        }
        return null;
    }

}
