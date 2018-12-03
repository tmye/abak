package tg.tmye.kaba.activity.menu;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.ShoppingCartRecyclerAdapter;
import tg.tmye.kaba._commons.adapters.SimpleTextAdapter;
import tg.tmye.kaba._commons.cviews.CustomProgressbar;
import tg.tmye.kaba._commons.decorator.ListVerticalSpaceDecorator;
import tg.tmye.kaba._commons.intf.OnMenuAddedToBasket;
import tg.tmye.kaba._commons.intf.YesOrNo;
import tg.tmye.kaba._commons.utils.CircleAnimationUtil;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.FoodDetails.frag.AddDrinkDialogFragment;
import tg.tmye.kaba.activity.UserAuth.login.LoginActivity;
import tg.tmye.kaba.activity.cart.ShoppingCartActivity;
import tg.tmye.kaba.activity.menu.Fragmentz.RestaurantSubMenuFragment;
import tg.tmye.kaba.activity.menu.contract.RestaurantMenuContract;
import tg.tmye.kaba.activity.menu.presenter.RestaurantMenuPresenter;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.LightRestaurant;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

import static tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity.ACTION_BUY;

public class RestaurantMenuActivity extends AppCompatActivity implements
        RestaurantMenuContract.View ,RestaurantSubMenuFragment.OnFragmentInteractionListener,
        View.OnClickListener,
        OnMenuAddedToBasket {

    // constants
    public static final String RESTAURANT = "RESTAURANT";
    public static final int RESTAURANT_ITEM_RESULT = 5000;

    // views
//    ViewPager vp_menus;
//    TabLayout tablelayout_title_strip;
    CustomProgressbar progressBar;
    TextView tv_messages, tv_no_menu;
    FrameLayout frame_container;
    RecyclerView recyclerView;
//    FloatingActionButton fab_chart;

    LinearLayout lny_error_box, lny_content, lny_loading_frame;


    CircleImageView cic_rest_pic;
    TextView tv_restaurant_name;
    TextView tv_address_quartier;
    TextView tv_working_time;
    static TextView tv_choosed_food_count;
    Button bt_tryagain;


    static RestaurantEntity restaurantEntity;
    private RestaurantMenuContract.Presenter presenter;
    private MenuDb_OnlineRepository menu_repository;
    private List<Restaurant_SubMenuEntity> menu_food;
    private List<Restaurant_Menu_FoodEntity> drinks;
    private MenuViewpagerAdapter menuFragmentAdapter;

    private int actionbarheight;

    private int previousFragmentCode;

    private Toolbar toolbar;

    private BasketContentFragment basketContentFragment;

    private static int BASKET_MAX = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        actionbarheight = toolbar.getHeight();

        toolbar.setTitle(getResources().getString(R.string.menu_name));

        initViews ();

//        fab_chart.setOnClickListener(this);

        /* either you know the restaurant id */
        Object tmp_restaurant = getIntent().getParcelableExtra(RESTAURANT);
        if (tmp_restaurant != null) {
            if (tmp_restaurant instanceof RestaurantEntity) {
                restaurantEntity = (RestaurantEntity) tmp_restaurant;
            } else if (tmp_restaurant instanceof LightRestaurant) {
                // find the restaurant in the db that corresponds --
                // -- if it doesnt
                restaurantEntity = RestaurantEntity.fromLightRestaurant((LightRestaurant) tmp_restaurant);
            }

            /* either you know the menu id */


            menu_repository = new MenuDb_OnlineRepository(this, restaurantEntity);
            presenter = new RestaurantMenuPresenter(menu_repository, this);
        } else {

            /* check if there is menu id */
            menu_repository = new MenuDb_OnlineRepository(25,this);
            presenter = new RestaurantMenuPresenter(menu_repository, this);
        }

        presenter.start();
        bt_tryagain.setOnClickListener(this);
        if (basketFoods !=null)
            basketFoods.clear();
    }


    @Override
    public void inflateMenus(final RestaurantEntity restaurantEntity, final List<Restaurant_SubMenuEntity> menu_food, final List<Restaurant_Menu_FoodEntity> drinks) {

        /* send list of menus and foods list */
        this.drinks = drinks;
        /* init pages */
        this.menu_food = menu_food;
        /* set strip together with viewpager */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                tablelayout_title_strip.setupWithViewPager(vp_menus);
                /* init adapter and create the view */
                inflateRestaurant(restaurantEntity);
                inflateRecyclerview(menu_food, drinks);
                initMenus();
            }
        });
    }

    private void inflateRestaurant(final RestaurantEntity restaurantEntity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                lny_error_box.setVisibility(View.GONE);
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.VISIBLE);

                tv_address_quartier.setText(restaurantEntity.address);
                tv_restaurant_name.setText(restaurantEntity.name);
                tv_working_time.setText(UtilFunctions.strToHour(restaurantEntity.working_hour));

                toolbar.setTitle(restaurantEntity.name.toUpperCase());

                if (!RestaurantMenuActivity.this.isFinishing())
                    GlideApp.with(RestaurantMenuActivity.this)
                            .load(Constant.SERVER_ADDRESS+ "/" +restaurantEntity.pic)
                            .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                            .placeholder(R.drawable.placeholder_kaba)
                            .centerCrop()
                            .into(cic_rest_pic);
            }
        });
    }

    private void inflateRecyclerview(List<Restaurant_SubMenuEntity> menu_food, List<Restaurant_Menu_FoodEntity> drinks) {

        List<String> menu_titles = new ArrayList<>();
        for (int i = 0; i < menu_food.size(); i++) {
            menu_titles.add(menu_food.get(i).name);
        }
        SimpleTextAdapter adapter = new SimpleTextAdapter(this, menu_titles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*  */
        recyclerView.addItemDecoration(new ListVerticalSpaceDecorator(getResources().getDimensionPixelSize(R.dimen.one_px),
                getResources().getDimensionPixelSize(R.dimen.command_item_header_height)));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showIsLoading(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                lny_content.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                lny_error_box.setVisibility(View.GONE);
//                tv_no_food_message.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onNetworkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void allDataFromMenuIdSuccess(RestaurantEntity restaurantEntity, List<Restaurant_SubMenuEntity> menuList) {

        this.restaurantEntity = restaurantEntity;
        this.menu_food = menuList;
        /* do inflate */
        inflateMenus(restaurantEntity, menu_food, null);
    }

    @Override
    public void showNoDataMessage() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String message = getResources().getString(R.string.no_data);
                tv_messages.setText(message);

                progressBar.setVisibility(View.GONE);
                frame_container.setVisibility(View.GONE);
                bt_tryagain.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
                tv_messages.setVisibility(View.VISIBLE);

//                if (menu_food == null || menu_food.size() == 0) {
//                    findViewById(R.id.lny_container).setVisibility(View.GONE);
//                    tv_no_menu.setVisibility(View.VISIBLE);
//                }
            }
        });
    }

    @Override
    public void onMenuInteraction(int position) {

        /* switch fragment */
        Fragment frg = menuFragmentAdapter.getItem(position);
        performNoBackStackTransaction(getSupportFragmentManager(), "position"+position,  frg, position);
    }


    public void performNoBackStackTransaction(final FragmentManager fragmentManager,
                                              String tag,
                                              Fragment fragment,
                                              int fragmentIndex) {

        final int newBackStackLength = fragmentManager.getBackStackEntryCount() +1;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


        Fragment previsouFragment = getFragmentByCode(previousFragmentCode);
//
        /* hide fragment if it is there, and showed */
        if (previsouFragment != null)
            fragmentTransaction = fragmentTransaction.hide(previsouFragment);
        /* add fragment that doesnt exist yet */
        if (getSupportFragmentManager().findFragmentByTag("position"+fragmentIndex) == null) {
            fragmentTransaction = fragmentTransaction.add(R.id.frame_container, fragment, getFragmentTagByCode(fragmentIndex));
        }

        /* show fragment */
        fragmentTransaction = fragmentTransaction.show(fragment);

        /* commit change */
//        if (!onSaveInstanceState)
        fragmentTransaction/*.addToBackStack(tag)*/.commitAllowingStateLoss();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int nowCount = fragmentManager.getBackStackEntryCount();
                if (newBackStackLength != nowCount) {
                    // we don't really care if going back or forward. we already performed the logic here.
                    fragmentManager.removeOnBackStackChangedListener(this);

                    if ( newBackStackLength > nowCount ) { // user pressed back
                        // xx error xx
                        try {
                            fragmentManager.popBackStackImmediate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        previousFragmentCode = fragmentIndex;
    }

    private String getFragmentTagByCode(int fragmentIndex) {
        return "position"+fragmentIndex;
    }

    private Fragment getFragmentByCode(int previousFragmentCode) {

        return menuFragmentAdapter.getItem(previousFragmentCode);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_basket:
                checkBasketContent();
                break;
        }
        return false;
    }

    private void initMenus() {

        // menu should be gotten in the json file also
        this.menuFragmentAdapter = new MenuViewpagerAdapter(getSupportFragmentManager());
        onMenuInteraction(0);
    }

    private void initViews() {

        progressBar = findViewById(R.id.progress_bar);
        tv_messages = findViewById(R.id.tv_messages);
        frame_container = findViewById(R.id.frame_container);
        recyclerView = findViewById(R.id.rec_menu_list);

        tv_working_time = findViewById(R.id.tv_working_time);
        tv_address_quartier = findViewById(R.id.tv_address_quartier);
        cic_rest_pic = findViewById(R.id.cic_rest_pic);
        tv_restaurant_name = findViewById(R.id.tv_restaurant_name);
        tv_no_menu = findViewById(R.id.tv_no_menu);
        tv_choosed_food_count = findViewById(R.id.tv_choosed_food_count);

        lny_content = findViewById(R.id.lny_content);
        lny_error_box = findViewById(R.id.lny_error_box);
        lny_loading_frame = findViewById(R.id.lny_loading);

        bt_tryagain = findViewById(R.id.bt_tryagain);
    }

    @Override
    public void onFoodInteraction(Restaurant_Menu_FoodEntity food) {

        Intent intent = new Intent(this, FoodDetailsActivity.class);
        intent.putExtra(FoodDetailsActivity.FOOD
                , food);
        intent.putExtra(FoodDetailsActivity.RESTAURANT_ENTITY, restaurantEntity);
        intent.putParcelableArrayListExtra(FoodDetailsActivity.RESTAURANT_SIMPLE_DRINKS, (ArrayList<? extends Parcelable>) drinks);
        /* get drink list of the current restaurant */

//        RESTAURANT_SIMPLE_DRINKS
        startActivityForResult(intent, RESTAURANT_ITEM_RESULT);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }


    private void checkBasketContent() {

        if (basketContentFragment == null)
            basketContentFragment = BasketContentFragment.newInstance();

        if (getBasketTotalItemsCount() == 0) {
            /* there is problem here. */
            Snackbar.make(findViewById(R.id.coordinator_container), getResources().getString(R.string.there_is_no_food_in_basket), Snackbar.LENGTH_SHORT).show();
        } else {
            basketContentFragment.show(getSupportFragmentManager(), AddDrinkDialogFragment.TAG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* when the activity comes back */
        // Check which request we're responding to
        if (requestCode == RESTAURANT_ITEM_RESULT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                restaurantEntity = data.getParcelableExtra(RESTAURANT);
            }
        }
    }

    @Override
    public void setPresenter(RestaurantMenuContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_chart:
                openShopCart();
                break;
            case R.id.bt_tryagain:
                if (presenter != null)
                    presenter.populateViews();
                break;
        }
    }


    @Override
    public void addMenu(CircleImageView origin_view, Restaurant_Menu_FoodEntity food_item) {

        if (basketFoods == null) {
            basketFoods = new HashMap<>();
        }

        if (getBasketTotalItemsCount() >= BASKET_MAX) {
            Snackbar.make(findViewById(R.id.coordinator_container), getResources().getString(R.string.too_much), Snackbar.LENGTH_SHORT).show();
            return;
        } else {
            int currentCount = 0;
            if (basketFoods.get(food_item) != null)
                currentCount = basketFoods.get(food_item);
            currentCount++;
            basketFoods.put(food_item, currentCount);
            makeFlyAnimation(origin_view);
        }
    }

    @Override
    protected void onDestroy() {
        if (basketFoods != null)
            basketFoods.clear();
        super.onDestroy();
    }

    private void makeFlyAnimation(ImageView targetView) {

        View destView = findViewById(R.id.action_basket);

        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                addItemToCart();
                /* +increment the other view -- */
                updateBasketCount();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).startAnimation();

    }

    static HashMap<Restaurant_Menu_FoodEntity, Integer> basketFoods;

    private static void updateBasketCount() {

        if (basketFoods == null)
            setBasketCountValue(0);
        else {
            setBasketCountValue(getBasketTotalItemsCount());
        }
    }

    private static int getBasketTotalItemsCount() {
        int allFoodsCount = 0;
        if (basketFoods == null)
            return allFoodsCount;

        Set<Restaurant_Menu_FoodEntity> rmf = basketFoods.keySet();
        for (Restaurant_Menu_FoodEntity food : rmf) {
            allFoodsCount += basketFoods.get(food);
        }
        return allFoodsCount;
    }

    private static void setBasketCountValue(int count) {

        tv_choosed_food_count.setText((count <= 9 ? "0" : "")+count);
    }

    private class MenuViewpagerAdapter extends FragmentPagerAdapter {

        public MenuViewpagerAdapter(FragmentManager fm) {
            super(fm);
            frg_map = new HashMap<>();
        }

        Map<Integer, Fragment> frg_map;

        @Override
        public Fragment getItem(int position) {

            if (frg_map == null)
                frg_map = new HashMap<>();

            if (frg_map.get(position) == null) {
                frg_map.put(position, RestaurantSubMenuFragment.instantiate(RestaurantMenuActivity.this,
                        menu_food.get(position)));
            }
            return frg_map.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menu_food.get(position).getName().toUpperCase();
        }

        @Override
        public int getCount() {
            return menu_food.size();
        }
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

    private void openShopCart() {
        String token = ((MyKabaApp) getApplication()).getAuthToken().trim();
        // if try to do a transaction and it fails, then you know there is someth wrong
        if (token == null || token.equals("")) {
            Intent in = new Intent(this, LoginActivity.class);
            startActivityForResult(in, Config.LOGIN_SUCCESS);
        } else {
            // if try to do a transaction and it fails, then you know there is someth wrong
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /* check if connected then show another one */
        getMenuInflater().inflate(R.menu.in_shopping_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public static class BasketContentFragment extends BottomSheetDialogFragment {

        RecyclerView rec_cart_items;
        Button bt_buy_now;

        public BasketContentFragment() {
            // Required empty public constructor
        }

        public static BasketContentFragment newInstance() {
            BasketContentFragment fragment = new BasketContentFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            /* adapt the adapter of shoppingchartinneritems to this*/
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_basket_content, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            rec_cart_items = view.findViewById(R.id.rec_cart_items);
            bt_buy_now = view.findViewById(R.id.bt_buy_now);

            CartRecyclerAdapter adapter = new CartRecyclerAdapter();
            rec_cart_items.setLayoutManager(new LinearLayoutManager(getContext()));
            rec_cart_items.setAdapter(adapter);

            bt_buy_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();

                    /* check if the guy is logged as a user before doing any action */
                    checkLogin (new YesOrNo() {
                        @Override
                        public void yes() {
                            /* buy */
                            Intent intent = new Intent(getActivity(), ConfirmCommandDetailsActivity.class);
                            intent.putExtra(ConfirmCommandDetailsActivity.RESTAURANT_ENTITY, restaurantEntity);
                            intent.putExtra(ConfirmCommandDetailsActivity.DATA, basketFoods);
                            intent.putExtra(ConfirmCommandDetailsActivity.ACTION, ACTION_BUY);
                            startActivityForResult(intent, ACTION_BUY);
                        }

                        @Override
                        public void no() {
                            Toast.makeText(getActivity(), getResources().getString(R.string.please_login_first), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        private void checkLogin(YesOrNo yesOrNo) {

            String token = ((MyKabaApp)getActivity().getApplicationContext()).getAuthToken().trim();
            // if try to do a transaction and it fails, then you know there is someth wrong
            if (token == null || token.equals("")) {
                Intent in = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(in, Config.LOGIN_SUCCESS);
            } else {
                yesOrNo.yes();
            }
        }

        class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder
                        (LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_shoppingcart_inner_food_layout, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                Restaurant_Menu_FoodEntity food = (Restaurant_Menu_FoodEntity) basketFoods.keySet().toArray()[position];

               /* BasketInItem basketInItem = data.get(groupItemoPosition).food_list.get(position);
                basketInItem.quantity = 1;
*/
                holder.food = food;
                holder.tv_food_name.setText(food.name);

                holder.quantity = basketFoods.get(food);
                holder.ed_quantity.setText("" + basketFoods.get(food));

                holder.tv_food_price.setText("" + food.price);

                GlideApp.with(getContext())
                        .load(Constant.SERVER_ADDRESS + "/" + food.pic)
                        .transition(GenericTransitionOptions.with(((MyKabaApp) getContext().getApplicationContext()).getGlideAnimation()))
//                .placeholder(R.drawable.white_placeholder)
                        .centerCrop()
                        .into(holder.iv_pic);

                holder.ib_basket_delete.setOnClickListener(new DeleteFromItemBasket(food, position));
            }

            @Override
            public int getItemCount() {
                if (basketFoods == null)
                    return 0;
                return basketFoods.keySet().size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

                ImageButton ib_quantity_del;
                ImageButton ib_quantity_add;
                ImageButton ib_basket_delete;
                ImageView iv_pic;
                TextView tv_food_name;
                TextView tv_food_price;
                EditText ed_quantity;

                Restaurant_Menu_FoodEntity food;

                public ViewHolder(View itemView) {

                    super(itemView);
                    iv_pic = itemView.findViewById(R.id.iv_pic);
                    tv_food_name = itemView.findViewById(R.id.tv_food_name);
                    tv_food_price = itemView.findViewById(R.id.tv_food_price);
                    ed_quantity = itemView.findViewById(R.id.ed_quantity);
                    ib_quantity_add = itemView.findViewById(R.id.ib_quantity_del);
                    ib_quantity_del = itemView.findViewById(R.id.ib_quantity_add);
                    ib_basket_delete = itemView.findViewById(R.id.ib_basket_delete);

                    ib_quantity_add.setOnClickListener(this);
                    ib_quantity_del.setOnClickListener(this);
                }


                public int quantity;


                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.ib_quantity_add:
                            /* get the total of the list */
                            if (getBasketTotalItemsCount() >= BASKET_MAX) {
                                mToast(getResources().getString(R.string.quantity_to_much));
                                return;
                            }
                            if (basketFoods.get(food) == 9) {
                                mToast(getResources().getString(R.string.quantity_to_much));
                            } else {
                                int new_quantity = basketFoods.get(food)+1;
                                basketFoods.put(food,new_quantity);
                                ed_quantity.setText("" + new_quantity);
                                updateBasketCount();
                            }
                            /* adjust price of englobing item */
//                            viewHolder.updatePrice();
                            break;
                        case R.id.ib_quantity_del:

                            if (getBasketTotalItemsCount() <= 0) {
                                mToast(getResources().getString(R.string.quantity_cannot_less));
                                return;
                            }
                            if (basketFoods.get(food) <= 0) {
                                mToast(getResources().getString(R.string.quantity_to_much));
                            } else {
                                int new_quantity = basketFoods.get(food)-1;
                                basketFoods.put(food,new_quantity);
                                ed_quantity.setText("" + new_quantity);
                                updateBasketCount();
                            }
                            break;
                    }
                }

                private void mToast(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

            }

            private class DeleteFromItemBasket implements View.OnClickListener {
                private final int position;
                private Restaurant_Menu_FoodEntity food;

                public DeleteFromItemBasket(Restaurant_Menu_FoodEntity food, int position) {
                    this.food = food;
                    this.position = position;
                }

                @Override
                public void onClick(View view) {
                    /* confirm and delete basket item */
//                    shoppingBasketAdapter.removeItemAt(groupPosition, itemPosition);
                    basketFoods.remove(food);
                    notifyItemRemoved(position);
                    updateBasketCount();
                    if (getBasketTotalItemsCount() == 0)
                        dismiss();
                }

            }


        }

    }

}
