package tg.tmye.kaba.activity.FoodDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
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
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.cviews.CustomProgressbar;
import tg.tmye.kaba._commons.cviews.SlidingBanner_LilRound;
import tg.tmye.kaba._commons.cviews.dialog.InfoDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.intf.ImagePreviewerListener;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.FoodDetails.contract.FoodDetailsContract;
import tg.tmye.kaba.activity.FoodDetails.frag.AddDrinkDialogFragment;
import tg.tmye.kaba.activity.FoodDetails.frag.ConfirmTransactionDialogFragment;
import tg.tmye.kaba.activity.FoodDetails.presenter.FoodDetailPresenter;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.LoadingIsOkActivtity;
import tg.tmye.kaba.activity.UserAuth.login.LoginActivity;
import tg.tmye.kaba.activity.WebArticle.WebArticleActivity;
import tg.tmye.kaba.activity.cart.ShoppingCartActivity;
import tg.tmye.kaba.activity.home.views.fragment.F_Home_1_Fragment;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.activity.restaurant.RestaurantActivity;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Food.source.FoodRepository;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


public class FoodDetailsActivity extends LoadingIsOkActivtity implements OnImageClickListener,
        AddDrinkDialogFragment.Listener,
        ConfirmTransactionDialogFragment.Listener,
        F_Home_1_Fragment.OnFragmentInteractionListener,
        FoodDetailsContract.View, FacebookCallback<Sharer.Result>,
        ImagePreviewerListener {


    private static final int TAG_SPAN_COUNT = 4;

    public static final String FOOD = "FOOD_ENTITY";
    public static final String RESTAURANT_ENTITY = "RESTAURANT_ENTITY";
    public static final String RESTAURANT_SIMPLE_DRINKS = "RESTAURANT_SIMPLE_DRINKS";
    public static final String FOOD_ID = "FOOD_ID";
    public static final String HAS_SENT_ID = "HAS_SENT_ID";


    private static final int SOME_ACTION = 34;


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
    private int food_id;
    private int MAX_FOOD_PER_DELIVERY = 9;


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onRestaurantInteraction(RestaurantEntity restaurantEntity) {

        /* this shouldnt be a light restaurant anymore */
        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra(RestaurantMenuActivity.RESTAURANT, restaurantEntity);
        startActivity(intent);
    }

    @Override
    public void onAdsInteraction(AdsBanner ads) {

        switch (ads.type) {
            case AdsBanner.TYPE_ARTICLE:
                /* launch article */
                Intent article_intent = new Intent(this, WebArticleActivity.class);
                article_intent.putExtra(WebArticleActivity.ARTICLE_ID, ads.entity_id);
                startActivity(article_intent);
                break;
            case AdsBanner.TYPE_MENU:
                /* scroll right to the menu -- with an indication */
                Intent menu_intent = new Intent(this, RestaurantMenuActivity.class);
                // add restaurant id and menu id too
//                menu_intent.putExtra(RestaurantMenuActivity.RESTAURANT, /*ad.entity_id*/);
//                startActivity(article_intent);
                break;
            case AdsBanner.TYPE_REPAS:
                Intent food_details_intent = new Intent(this, FoodDetailsActivity.class);
                food_details_intent.putExtra(FoodDetailsActivity.FOOD_ID, ads.entity_id);
                food_details_intent.putExtra(FoodDetailsActivity.HAS_SENT_ID, true);
                startActivity(food_details_intent);
                /* implement opening food with id only, and also, implemeting the reduction thing - reduire le prix de la chose. */
                break;
            case AdsBanner.TYPE_RESTAURANT:
                /* get in a restaurant when the principal card at the top  has to be an advertising one with expiration date.
                 *      - no clicking after a certain time */
                break;
        }
    }

    @Override
    public void onComingSoonInteractionListener(RestaurantEntity resto) {
    }

    @Override
    public void networkError() {
        /* show on top of everything an interface for error... that makes close the interface when pressed */
    }

    @Override
    public void showErrorPage(boolean isShowed) {
        /* show on top of everything an interface for error... that makes close the interface when pressed */
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

    LoadingDialogFragment loadingDialogFragment;

    @Override
    public void showLoading(boolean isLoading) {

        if (isLoading) {
            if (loadingDialogFragment == null)
                loadingDialogFragment = LoadingDialogFragment.newInstance("", LoadingDialogFragment.CUSTOM_PROGRESS);
            loadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            if (loadingDialogFragment != null)
                loadingDialogFragment.dismiss();
        }
    }

    @Override
    public void inflateFood(final RestaurantEntity restaurantEntity, final Restaurant_Menu_FoodEntity foodEntity, final List<Restaurant_Menu_FoodEntity> restaurant_menu_drinks) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FoodDetailsActivity.this.isOk = true;
                FoodDetailsActivity.this.resto = restaurantEntity;
                FoodDetailsActivity.this.foodEntity = foodEntity;
                FoodDetailsActivity.this.restaurant_menu_drinks = restaurant_menu_drinks;
                initSlidingBanner();
                initNameFood_N_RestaurantNames();

                int icon = foodEntity.is_favorite == 0 ? R.drawable.ic_star_white_transparent_24dp : R.drawable.ic_star_white_full_24dp;
                menu.getItem(0).setIcon(getResources().getDrawable(icon));
            }
        });
    }

    @Override
    public void finishActivity() {
        mToast(getResources().getString(R.string.sys_error));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        InfoDialogFragment infoDialogFragment;
        String message;

        /* if coming back from adding to basket, show a box -  R.drawable.ic_added_shopping_cart_success */
        switch (requestCode) {
            case ConfirmCommandDetailsActivity.ACTION_BASKET:
                if (resultCode == RESULT_OK) {
                    message = data.getStringExtra("message");
                    infoDialogFragment = InfoDialogFragment.newInstance(R.drawable.ic_added_shopping_cart_success, message, InfoDialogFragment.BASKET_ADD);
                    infoDialogFragment.show(getSupportFragmentManager(), "loadingbox");
                }
                break;
        }

        /* facebook */
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setPresenter(FoodDetailsContract.Presenter presenter) {

    }

    @Override
    public void onDrinkSelected(Restaurant_Menu_FoodEntity entity) {

        /*  */
        if (food_total_count() >= MAX_FOOD_PER_DELIVERY) {
            mToast(getResources().getString(R.string.quantity_to_much));
            return;
        }

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
        updateTotalPrice();
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

    public boolean isOk() {
        return isOk;
    }


    private class FoodDetailsLayoutHolder {

        private final SlidingBanner_LilRound slidingBanner;
        private final ImageButton  ib_share_wha;
        private final ImageButton ib_share_fb;
        TextView ib_purchase_now;
        TextView ib_add_to_basket;
        ImageView iv_add_a_drink;
        ImageView iv_resto_pic;
        TextView tv_food_name;
        TextView tv_food_price;
        TextView tv_total_price;
        TextView tv_restaurant_name;
        TextView tv_food_description;

        CustomProgressbar customProgressbar;

        /* cardview for drinks */
        CardView cardView_add_drinks;

        /* recyclerview add drinks */
        RecyclerView recyclerView_add_drinks;

        /* */
        ImageButton ib_quantity_add;
        ImageButton ib_quantity_del;

        /* */
        EditText ed_quantity;

        /* views */
        FloatingActionButton cartFab;

        public FoodDetailsLayoutHolder (View itemView) {

            this.tv_food_name = itemView.findViewById(R.id.tv_food_name);
            this.tv_total_price = itemView.findViewById(R.id.tv_total_price);
            this.tv_food_price = itemView.findViewById(R.id.tv_food_price);
            this.tv_restaurant_name = itemView.findViewById(R.id.tv_restaurant_name);
            this.ib_purchase_now = itemView.findViewById(R.id.ib_purchase_now);
            this.iv_resto_pic = itemView.findViewById(R.id.iv_restaurant_pic);
            this.iv_add_a_drink = findViewById(R.id.iv_add_a_drink);
            this.ib_add_to_basket = findViewById(R.id.ib_add_to_basket);
            this.slidingBanner = findViewById(R.id.sliding_banner);
            this.tv_food_description = findViewById(R.id.tv_food_description);

            this.ib_share_wha = findViewById(R.id.ib_share_wha);
            this.ib_share_fb = findViewById(R.id.ib_share_fb);

            this.cardView_add_drinks = itemView.findViewById(R.id.cardview_add_drink);
            this.recyclerView_add_drinks = itemView.findViewById(R.id.recyclerview_add_drink);

            this.ib_quantity_add = itemView.findViewById(R.id.ib_quantity_add);
            this.ib_quantity_del = itemView.findViewById(R.id.ib_quantity_del);
            this.ed_quantity = itemView.findViewById(R.id.ed_quantity);

            this.customProgressbar = itemView.findViewById(R.id.progress_bar);
            this.cartFab = itemView.findViewById(R.id.fab_chart);

        }

        public void bindIcons () {
//            ib_add_to_basket ic_shopping_add_to_card_red_24dp
            ib_add_to_basket.setCompoundDrawablesWithIntrinsicBounds(
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_shopping_add_to_card_red_24dp, null),
                    null, null, null);
            // ib_purchase_now ic_purchase_now_red_24dp
            ib_purchase_now.setCompoundDrawablesWithIntrinsicBounds(
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_purchase_now_red_24dp, null),
                    null, null, null);
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

        holder.bindIcons();

        showLoading(false);

        foodRepository = new FoodRepository(this);
        presenter = new FoodDetailPresenter(this, foodRepository);

//        boolean has_sent_id = getIntent().getBooleanExtra(HAS_SENT_ID, false);
//        if (has_sent_id) {
        food_id = getIntent().getIntExtra(FOOD_ID, -1);
        /* launch presenter */
        if (food_id == -1) {
            foodEntity = getIntent().getParcelableExtra(FOOD);
            food_id = (int) foodEntity.id;
            if (food_id == 0 || food_id == -1) {
                mToast(getString(R.string.sys_error));
                finish();
            }
        }

        presenter.loadFood (food_id);

        food_id_quantity = new HashMap<>();

        holder.ib_add_to_basket.setOnClickListener(this);
        holder.iv_add_a_drink.setOnClickListener(this);
        holder.ib_quantity_add.setOnClickListener(this);
        holder.ib_quantity_del.setOnClickListener(this);
        holder.ib_purchase_now.setOnClickListener(this);

        holder.ib_share_wha.setOnClickListener(this);
        holder.ib_share_fb.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, this);

        holder.cartFab.setOnClickListener(this);
    }

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    private void initNameFood_N_RestaurantNames() {

        if (resto == null)
            return;

        holder.tv_food_name.setText(foodEntity.name.toUpperCase());
        holder.tv_food_price.setText(UtilFunctions.intToMoney(foodEntity.price));
        holder.tv_total_price.setText(UtilFunctions.intToMoney(foodEntity.price));
        holder.tv_food_description.setText(foodEntity.description);
        holder.tv_restaurant_name.setText(
                resto.name.toUpperCase()
        );

        // load resto image
        if (!FoodDetailsActivity.this.isFinishing())
            GlideApp.with(FoodDetailsActivity.this)
                    .load(Constant.SERVER_ADDRESS+"/"+resto.pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(holder.iv_resto_pic);

        // init bottom stars

    }

    @Override
    public void onBackPressed() {
        if (isOk) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    private void initSlidingBanner() {

        /* transform image items into kabashowpics */
        List<AdsBanner> adsBanners = new ArrayList<>();
        try {
            for (int i = 0; i < foodEntity.food_details_pictures.size(); i++) {
                AdsBanner adsBanner = new AdsBanner();
                adsBanner.pic = foodEntity.food_details_pictures.get(i);
                adsBanners.add(adsBanner);
            }
            holder.slidingBanner.load(adsBanners, this, getSupportFragmentManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* clicking on images */
    @Override
    public void onClick(View view) {

        int quantity;

        switch (view.getId()) {
            case R.id.fab_chart:
                openShopCart();
                break;
            case R.id.ib_purchase_now:
                purchaseNow();
                break;
            case R.id.ib_add_to_basket:
                /* launch fragment to define how much */
                addToBasket();
                break;
            case R.id.iv_add_a_drink:
                /* launch fragment to define how much */
                if (restaurant_menu_drinks == null || restaurant_menu_drinks.size() == 0) {
                    mToast(getResources().getString(R.string.restaurant_no_drinks));
                } else {
                    addDrinks(restaurant_menu_drinks);
                }
                break;
            case R.id.ib_quantity_add:
                if (food_total_count() >= MAX_FOOD_PER_DELIVERY) {
                    mToast(getResources().getString(R.string.quantity_to_much));
                    return;
                }
                quantity = (Integer.valueOf(holder.ed_quantity.getText().toString()));
                if (quantity == MAX_FOOD_PER_DELIVERY) {
                    mToast(getResources().getString(R.string.quantity_to_much));
                } else {
                    quantity++;
                    holder.ed_quantity.setText(""+quantity);
                    updateTotalPrice();
                }
                break;
            case R.id.ib_quantity_del:
                quantity = (Integer.valueOf(holder.ed_quantity.getText().toString()));
                if (quantity == 1) {
                    mToast(getResources().getString(R.string.quantity_cannot_less));
                } else {
                    quantity--;
                    holder.ed_quantity.setText(""+quantity);
                    updateTotalPrice();
                }
                break;
            case R.id.ib_share_wha:
                shareOnWhatsapp();
                break;
            case R.id.ib_share_fb:
                shareOnFacebook();
                break;
        }
    }

    private void sendFavoriteRequest () {

        /* run something that makes it */

        String token = ((MyKabaApp) getApplication()).getAuthToken().trim();
        // if try to do a transaction and it fails, then you know there is someth wrong
        if (token == null || token.equals("")) {
            Intent in = new Intent(this, LoginActivity.class);
            startActivityForResult(in, Config.LOGIN_SUCCESS);
        } else {
            // if try to do a transaction and it fails, then you know there is someth wrong
            presenter.setFavorite(foodEntity);
        }
    }

    private void openShopCart() {
        String token = ((MyKabaApp) getApplication()).getAuthToken().trim();
        // if try to do a transaction and it fails, then you know there is someth wrong
        if (token == null || token.equals("")) {
            Intent in = new Intent(this, LoginActivity.class);
            startActivityForResult(in, Config.LOGIN_SUCCESS);
        } else {
            // if try to do a transaction and it fails, then you know there is someth wrong
            Intent in = new Intent(this, ShoppingCartActivity.class);
            startActivity(in);
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

        // check login
        String token = ((MyKabaApp) getApplication()).getAuthToken().trim();
        // if try to do a transaction and it fails, then you know there is someth wrong
        if (token == null || token.equals("")) {
            Intent in = new Intent(this, LoginActivity.class);
            startActivityForResult(in, Config.LOGIN_SUCCESS);
            return;
        }

        HashMap<Restaurant_Menu_FoodEntity, Integer> selection_map = new HashMap<>();

        selection_map.put(foodEntity, holder.getSelectedQuantity());

        Object[] keys = food_id_quantity.keySet().toArray();
        if (keys != null && keys.length != 0) {
            for (int i = 0; i < keys.length; i++) {
                selection_map.put(getDrinkEntityWithId((int)keys[i]), food_id_quantity.get(keys[i]));
            }
        }

        Intent intent = new Intent(this, ConfirmCommandDetailsActivity.class);
        intent.putExtra(ConfirmCommandDetailsActivity.RESTAURANT_ENTITY, resto);
        intent.putExtra(ConfirmCommandDetailsActivity.DATA, selection_map);
        intent.putExtra(ConfirmCommandDetailsActivity.ACTION, action);
//        startActivity(intent);
        startActivityForResult(intent, action);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    private void addDrinks(List<Restaurant_Menu_FoodEntity> restaurant_menu_drinks) {

        if (drinkDialogFragment == null)
            drinkDialogFragment = AddDrinkDialogFragment.newInstance(new ArrayList<>(restaurant_menu_drinks));
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
            /* check if loaded */
            sendFavoriteRequest();
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
            holder.tv_drink_price.setText(UtilFunctions.intToMoney(drinkEntity.price));

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
            TextView tv_name, tv_drink_price;
            ImageView iv_pic;

            public ViewHolder(View itemView) {
                super(itemView);

                this.iv_pic = itemView.findViewById(R.id.iv_pic);
                this.tv_quantity = itemView.findViewById(R.id.tv_drink_count);
                this.tv_name = itemView.findViewById(R.id.tv_drink_name);
                this.tv_drink_price = itemView.findViewById(R.id.tv_drink_price);
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

                updateTotalPrice();
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

    private void shareOnWhatsapp () {

        String productLink = Constant.SERVER_ADDRESS+"/food/"+foodEntity.id;

        String message = getResources().getString(R.string.share_on_platform, getResources().getString(R.string.app_name), productLink);

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            mToast(getResources().getString(R.string.wha_not_installed));
        }
    }

    private void shareOnFacebook() {

        String productLink = Constant.SERVER_ADDRESS+"/food/"+foodEntity.id;

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(productLink))
                    .build();
            shareDialog.show(linkContent);
        } else {
            mToast("Sharedialog NOT BE SHOWNED");
        }
    }

    @Override
    public void onSuccess(Sharer.Result result) {
        mToast("Facebook onSuccess ");
    }

    @Override
    public void onCancel() {
        mToast("Facebook onCancel ");
    }

    @Override
    public void onError(FacebookException error) {
        mToast("Facebook onError ");
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

    public int computeTotalPrice () {

        int price = 0;
        /* food price * food count */
        String food_count = holder.ed_quantity.getText().toString();
        price += (Integer.valueOf( foodEntity.price) * Integer.valueOf(food_count));
        /* drink list price */
        Object[] keys = food_id_quantity.keySet().toArray();
        if (keys != null && keys.length != 0) {
            for (int i = 0; i < keys.length; i++) {
                Restaurant_Menu_FoodEntity drinkEntity = getDrinkEntityWithId((int) keys[i]);
                price+=(Integer.valueOf(drinkEntity.price) * food_id_quantity.get(keys[i]).intValue());
            }
        }
        /* set food price */
        return price;
    }

    public void updateTotalPrice () {

        /* update total price */
        holder.tv_total_price.setText(UtilFunctions.intToMoney(""+computeTotalPrice()));
    }

    public int food_total_count () {

        int total_count = 0;
        int food_count = Integer.valueOf(holder.ed_quantity.getText().toString());
        total_count += food_count;
        Object[] keys = food_id_quantity.keySet().toArray();
        if (keys != null && keys.length != 0) {
            for (int i = 0; i < keys.length; i++) {
                total_count += food_id_quantity.get(keys[i]).intValue();
            }
        }
        return total_count;
    }

    public void startActivityWithNoAnimation(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onShowPic(View originView, AdsBanner adsBanner) {

//        pic = Constant.SERVER_ADDRESS+"/"+pic;
//        String[] imgs = new String[]{pic};
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, adsBanner);
        startActivityWithNoAnimation(intent);
    }


    /* adapt showItems/pubs to the view we're seeing here. */

    @Override
    public void onShowPic(View originView, AdsBanner[] adsBanners) {

        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, adsBanners);
        startActivityWithNoAnimation(intent);
    }
}
