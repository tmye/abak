package tg.tmye.kaba.activity.trans;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.HashMap;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.InfoDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAcc.MyAdressesActivity;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.ConfirmPayActivity;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.SoldeActivity;
import tg.tmye.kaba.activity.trans.contract.CommandDetailsContract;
import tg.tmye.kaba.activity.trans.presenter.CommandDetailsPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.data.command.source.CommandRepository;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.shoppingcart.source.BasketRepository;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

public class ConfirmCommandDetailsActivity extends AppCompatActivity implements CommandDetailsContract.View, View.OnClickListener {


    public static final String DATA = "DATA";
    public static final String ACTION = "ACTION";
    private static final String WORKING_HOUR = "WORKING_HOUR";
    public static final String RESTAURANT_ENTITY = "RESTAURANT_ENTITY";


    /* actions of the activity */
    public static final int ACTION_BUY = 90;
    public static final int ACTION_BASKET = 80;
    public static final int PASSWORD_RETRIEVE_SUCCESS = 600;

    public String balance;
    public String price;

    private String working_hour;


    private HashMap<Restaurant_Menu_FoodEntity, Integer> food_quantity;
    private RecyclerView recyclerview_items_to_buy;
    private ImageButton ib_confirm_purchase_now;
    private ImageButton ib_confirm_add_to_basket;
    private TextView tv_select_address;
    //    private CheckBox checkbox_pay_arrival;
    private TextView tv_address_description_preview;
    private TextView tv_address_title_preview;
    private RelativeLayout rel_address_preview;
    private TextView tv_press_to_add_to_basket;
    private View ib_delete_selected_address;
    private Button bt_cancel;
    private TextView tv_press_to_end, tv_press_to_end_purchase;

    /* actions */
    private int myACTION;
    /* address */
    private DeliveryAddress currentSelectedAddress;

    /* repositories */
    private BasketRepository basketRepository;
    private CommandRepository commandRepository;
    private CustomerDataRepository customerDataRepository;
    private RestaurantEntity restaurantEntity;

    /* presenter */
    private CommandDetailsPresenter presenter;

    /* loading dialog fragment */
    private LoadingDialogFragment loadingDialogFragment;


    TextView tv_price_delivery, tv_price_total, tv_price_remise, tv_price_net_to_pay, tv_price_command, tv_balance, tv_balance_not_enough;
    private CardView card_pricing;

    NestedScrollView nestedScrollView;

    LinearLayout lny_balance;

    Button bt_top_up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_command_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        /* get command items */
        restaurantEntity = getIntent().getParcelableExtra(RESTAURANT_ENTITY);
        food_quantity = (HashMap<Restaurant_Menu_FoodEntity, Integer>) getIntent().getSerializableExtra(DATA);

        /* action */
        this.myACTION = getIntent().getIntExtra(ACTION, ACTION_BASKET);

        /* get working hour of the restaurant  */
        this.working_hour = getIntent().getStringExtra(WORKING_HOUR);
        working_hour = "1000-2300";

        initViews();
        inflateRecyclerview();

        bindIcons();

        ib_confirm_purchase_now.setOnClickListener(this);
        tv_select_address.setOnClickListener(this);
        ib_confirm_add_to_basket.setOnClickListener(this);
        ib_delete_selected_address.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        bt_top_up.setOnClickListener(this);

        basketRepository = new BasketRepository(this);
        commandRepository = new CommandRepository(this);
        customerDataRepository = new CustomerDataRepository(this);
        presenter = new CommandDetailsPresenter(basketRepository, commandRepository, customerDataRepository, this);

        /* inflate action */
        inflateAction();
    }

    private void bindIcons() {

//        ic_pick_address_drawable_24dp tv_select_address
        tv_select_address.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_pick_address_drawable_24dp, null),
                null,null, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    private void inflateAction() {

        /* add to basket available all the time */

        /* purchase has not to be available above working time - from server () */
        bt_cancel.setVisibility(View.GONE);
        tv_press_to_end.setVisibility(View.GONE);
        tv_press_to_end_purchase.setVisibility(View.GONE);
        lny_balance.setVisibility(View.GONE);
        tv_balance_not_enough.setVisibility(View.GONE);

        if (myACTION == ACTION_BASKET) {
            tv_press_to_add_to_basket.setVisibility(View.VISIBLE);
            ib_confirm_add_to_basket.setVisibility(View.VISIBLE);
            ib_confirm_purchase_now.setVisibility(View.GONE);
            tv_press_to_end_purchase.setVisibility(View.GONE);
            tv_select_address.setVisibility(View.GONE);
        } else if (myACTION == ACTION_BUY) {
            presenter.checkRestaurantOpen(restaurantEntity);
        }
    }

    private void inflateRecyclerview() {

        SelectedFoodsAdapter adapter = new SelectedFoodsAdapter();
        recyclerview_items_to_buy.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_items_to_buy.setAdapter(adapter);
    }

    private void initViews() {

        this.nestedScrollView = findViewById(R.id.nestedscrollview);
        this.ib_confirm_purchase_now = findViewById(R.id.ib_confirm_purchase_now);
        this.ib_confirm_add_to_basket = findViewById(R.id.ib_confirm_add_to_basket);
        this.tv_select_address = findViewById(R.id.tv_select_address);
        this.recyclerview_items_to_buy = findViewById(R.id.recyclerview_items_to_buy);
        this.tv_address_description_preview = findViewById(R.id.tv_address_description_preview);
        this.tv_address_title_preview = findViewById(R.id.tv_address_title_preview);
        this.rel_address_preview = findViewById(R.id.rel_address_preview);
        this.tv_press_to_add_to_basket = findViewById(R.id.tv_press_to_add_to_basket);
        this.ib_delete_selected_address = findViewById(R.id.ib_delete_selected_address);
        this.bt_cancel = findViewById(R.id.bt_cancel);
        this.tv_press_to_end = findViewById(R.id.tv_press_to_end);
        this.tv_press_to_end_purchase = findViewById(R.id.tv_press_to_end_purchase);
        this.tv_balance_not_enough = findViewById(R.id.tv_balance_not_enough);

        this.card_pricing = findViewById(R.id.card_pricing);
        this.tv_price_command = findViewById(R.id.tv_price_command);
        this.tv_price_delivery = findViewById(R.id.tv_price_delivery);
        this.tv_price_net_to_pay = findViewById(R.id.tv_price_net_to_pay);
        this.tv_price_remise = findViewById(R.id.tv_price_remise);
        this.tv_price_total = findViewById(R.id.tv_price_total);
        this.lny_balance = findViewById(R.id.lny_balance);
        this.tv_balance = findViewById(R.id.tv_balance);
        this.bt_top_up = findViewById(R.id.bt_top_up);
    }

    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isLoading) {
                    /* stop animation of the button */
                    if (ib_confirm_purchase_now.getAnimation() != null)
                    ib_confirm_purchase_now.getAnimation().cancel();
                } else {
                    if (ib_confirm_purchase_now.getAnimation() != null)
                        ib_confirm_purchase_now.getAnimation().start();
                }

                if (loadingDialogFragment == null) {
                    if (isLoading) {
                        loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.content_on_loading));
                        showFragment(loadingDialogFragment, "loadingbox", true);
                    }
                } else {
                    if (isLoading) {
                        showFragment(loadingDialogFragment, "loadingbox", false);
                    } else {
                        hideFragment();
                    }
                }
            }
        });
    }

    private void hideFragment() {
        if (loadingDialogFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(loadingDialogFragment);
            loadingDialogFragment = null;
            ft.commitAllowingStateLoss();
        }
    }

    private void showFragment(LoadingDialogFragment loadingDialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(loadingDialogFragment, tag);
        else
            ft.show(loadingDialogFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void networkError() {
        mToast(getResources().getString(R.string.network_error));
    }


    @Override
    public void sysError() {
        mToast(getResources().getString(R.string.sys_error));
    }

    @Override
    public void inflateBillingComputations(final boolean out_of_range, final String price_total, final String price_delivery,
                                           final String price_remise, final String price_net_to_pay,
                                           final String price_command) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ConfirmCommandDetailsActivity.this.price = price_total;
                /*  */
                tv_price_total.setText(UtilFunctions.intToMoney(price_total));
                tv_price_remise.setText(price_remise);
                tv_price_delivery.setText(UtilFunctions.intToMoney(price_delivery));
                tv_price_net_to_pay.setText(UtilFunctions.intToMoney(price_net_to_pay));
                tv_price_command.setText(UtilFunctions.intToMoney(price_command));

                card_pricing.setVisibility(View.VISIBLE);

                /* check the money */
                if (Integer.valueOf(price) <= Integer.valueOf(balance) && !out_of_range) {

                    /* your balance good enough and your are in-range */
                    tv_balance_not_enough.setVisibility(View.GONE);
                    tv_press_to_end_purchase.setVisibility(View.VISIBLE);
                    tv_press_to_end_purchase.setText(getResources().getString(R.string.press_to_end_purchase));
                    ib_confirm_purchase_now.setVisibility(View.VISIBLE);
                    bt_top_up.setVisibility (View.GONE);
                    card_pricing.setVisibility(View.VISIBLE);

                    /* scroll to ib confirm purchase now height */
                    scrollToCOnfirmPurchaseNow();
                } else {

                    if (out_of_range) {
                        tv_press_to_end_purchase.setText(getResources().getString(R.string.out_of_range));
                        tv_press_to_end_purchase.setVisibility(View.VISIBLE);
                        ib_confirm_purchase_now.setVisibility(View.GONE);
                        card_pricing.setVisibility(View.GONE);
                    }
                    if (Integer.valueOf(price) > Integer.valueOf(balance)) {
                        /* hide buttonz and show that your balance is not sufficient*/
                        tv_press_to_end_purchase.setVisibility(View.GONE);
                        ib_confirm_purchase_now.setVisibility(View.GONE);
                        tv_balance_not_enough.setVisibility(View.VISIBLE);
                        bt_top_up.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void scrollToCOnfirmPurchaseNow() {

//        nestedScrollView
//    ib_confirm_purchase_now
        nestedScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int dest_Y = ib_confirm_purchase_now.getBottom() + ib_confirm_purchase_now.getHeight();
                nestedScrollView.smoothScrollTo(0, dest_Y);
                animatePurchaseNowButton();
            }
        }, 250);
    }

    private void animatePurchaseNowButton() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.heart_beat_pulse);
        ib_confirm_purchase_now.startAnimation(animation);
    }

    @Override
    public void inflateMySolde(final String balance) {

        /*  */
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                UtilFunctions.StoreBalance(ConfirmCommandDetailsActivity.this, balance);
                ConfirmCommandDetailsActivity.this.balance = balance;
                lny_balance.setVisibility(View.VISIBLE);
                tv_balance.setText("" + UtilFunctions.intToMoney(balance) + " FCFA");
            }
        });
    }

    @Override
    public void addToBasketSuccessfull(boolean isSuccessfull) {

        if (isSuccessfull) {

            /* this is ok */
            Intent data = new Intent();
            data.putExtra("message", getResources().getString(R.string.add_to_basket_successfull));
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void purchaseSuccessfull(final boolean isSuccessfull) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /* send to an activity */
                if (isSuccessfull) {
                    ib_confirm_purchase_now.setEnabled(false);
                    UtilFunctions.playPurchaseSuccessSound(ConfirmCommandDetailsActivity.this);
                    InfoDialogFragment infoDialogFragment = InfoDialogFragment.newInstance(R.drawable.ic_purchase_success, getResources().getString(R.string.purchase_success), InfoDialogFragment.PURCHASE_SUCCESS);
                    infoDialogFragment.show(getSupportFragmentManager(), "loadingbox");
                } else {
                    mToast(getString(R.string.purchase_failure));
                }
            }
        });
    }

    @Override
    public void inflateCommandDetails(Command command, DeliveryAddress deliveryAddress) {
    }

    @Override
    public void isRestaurantAvailableForBuy(final boolean isOpen) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isOpen) {

                    tv_press_to_add_to_basket.setVisibility(View.GONE);
                    ib_confirm_add_to_basket.setVisibility(View.GONE);
                    tv_press_to_end_purchase.setVisibility(View.VISIBLE);
                    ib_confirm_purchase_now.setVisibility(View.VISIBLE);
                    tv_press_to_end_purchase.setVisibility(View.VISIBLE);
                    tv_select_address.setVisibility(View.VISIBLE);
                    bt_cancel.setVisibility(View.GONE);
                    tv_press_to_end.setVisibility(View.GONE);
                } else {
                    /*  */
                    tv_press_to_add_to_basket.setVisibility(View.GONE);
                    ib_confirm_add_to_basket.setVisibility(View.GONE);
                    ib_confirm_purchase_now.setVisibility(View.GONE);
                    tv_press_to_end_purchase.setVisibility(View.GONE);
                    tv_select_address.setVisibility(View.GONE);

                    /* show a box saying that the purchase cant be made now. */
                    bt_cancel.setVisibility(View.VISIBLE);
                    tv_press_to_end.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_confirm_purchase_now:
                Animation animation = view.getAnimation();
                if (animation != null)
                    animation.cancel();
                confirmTransactionCode();
                break;
            case R.id.ib_confirm_add_to_basket:
                addToBasket();
                break;
            case R.id.tv_select_address:
//                choose_address
                selectAddress();
                break;
            case R.id.ib_delete_selected_address:
                reinitAddress();
                break;
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_top_up:
                topUp();
                break;
        }
    }

    private void topUp() {

        Intent intent = new Intent(this, SoldeActivity.class);
        startActivity(intent);
    }

    private void confirmTransactionCode() {
        Intent intent = new Intent(this, ConfirmPayActivity.class);
        intent.putExtra(ConfirmPayActivity.TRANS_TYPE, ConfirmPayActivity.PAY_COMMAND);
        intent.putExtra(ConfirmPayActivity.TRANS_AMOUNT, price);
        intent.putExtra(ConfirmPayActivity.STEP_COUNT, 1);
        startActivityForResult(intent, ConfirmCommandDetailsActivity.PASSWORD_RETRIEVE_SUCCESS);
    }

    private void reinitAddress() {
        rel_address_preview.setVisibility(View.GONE);
        currentSelectedAddress = null;
        card_pricing.setVisibility(View.GONE);
        ib_confirm_purchase_now.setVisibility(View.GONE);
        tv_press_to_end_purchase.setVisibility(View.GONE);
    }

    private void selectAddress() {

        /* cardview */
        card_pricing.setVisibility(View.GONE);
        tv_press_to_end_purchase.setVisibility(View.GONE);
        ib_confirm_purchase_now.setVisibility(View.GONE);

        Intent intent = new Intent(this, MyAdressesActivity.class);
        intent.putExtra("choose_address", true);
        startActivityForResult(intent, MyAdressesActivity.CHOOSEN_ADDRESS);

        /* clear the address that is here before and hide up the preview view */
        currentSelectedAddress = null;
        rel_address_preview.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MyAdressesActivity.CHOOSEN_ADDRESS:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    /*  */
                    DeliveryAddress address = data.getParcelableExtra("data");
                    currentSelectedAddress = address;
                    inflateSelectedAddress(address);
                    inflatePricing();
                }
                break;
            case ConfirmCommandDetailsActivity.PASSWORD_RETRIEVE_SUCCESS:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String code = data.getStringExtra(ConfirmPayActivity.PASSWORD);
                    pay(code);
                }
                break;
        }
    }

    private void inflatePricing() {

        /* send the food and send the address */
        lny_balance.setVisibility(View.GONE);

        presenter.computePricing(restaurantEntity, food_quantity, currentSelectedAddress);
    }

    private void inflateSelectedAddress(DeliveryAddress address) {

        rel_address_preview.setVisibility(View.VISIBLE);
        tv_address_description_preview.setText(address.description);
        tv_address_title_preview.setText(address.name);
    }

    private void pay(String code) {


//    go get code
        if (currentSelectedAddress == null) {
            mToast(getResources().getString(R.string.please_choose_delivery_address));
        } else {
//             check if price is greater or less
            presenter.purchaseNow(code, food_quantity, currentSelectedAddress);
        }
    }

    private void addToBasket() {

        /* send the list of foods with quantities and idz */
        /* get the string and make a get / post request to save it inside the shopping cart */
        presenter.addCommandToBasket(food_quantity);
    }

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }

    class SelectedFoodsAdapter extends RecyclerView.Adapter< SelectedFoodsAdapter.ViewHolder> {

        @Override
        public  SelectedFoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new  SelectedFoodsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_food_item, parent, false));
        }

        @Override
        public void onBindViewHolder( SelectedFoodsAdapter.ViewHolder holder, int position) {

            /* food idz */
            Restaurant_Menu_FoodEntity entity = (Restaurant_Menu_FoodEntity) food_quantity.keySet().toArray()[position];
            int quantity = food_quantity.get(entity);

            holder.tv_drink_price.setText(UtilFunctions.intToMoney(entity.price));
            holder.tv_quantity.setText("X "+quantity);
            holder.tv_name.setText(entity.name.toUpperCase());

            GlideApp.with(ConfirmCommandDetailsActivity.this)
                    .load(Constant.SERVER_ADDRESS+"/"+entity.pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                    .centerCrop()
                    .into(holder.iv_pic);
        }


        @Override
        public int getItemCount() {
            return food_quantity.keySet().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            TextView tv_quantity, tv_drink_price;
            TextView tv_name;
            ImageView iv_pic;

            public ViewHolder(View itemView) {
                super(itemView);

                this.iv_pic = itemView.findViewById(R.id.iv_pic);
                this.tv_quantity = itemView.findViewById(R.id.tv_drink_count);
                this.tv_name = itemView.findViewById(R.id.tv_drink_name);
                this.tv_drink_price = itemView.findViewById(R.id.tv_drink_price);
            }
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

}
