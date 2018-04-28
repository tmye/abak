package tg.tmye.kaba.activity.trans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.ArrayList;
import java.util.HashMap;

import droidninja.filepicker.FilePickerConst;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.activity.FoodDetails.frag.ConfirmTransactionDialogFragment;
import tg.tmye.kaba.activity.UserAcc.MyAdressesActivity;
import tg.tmye.kaba.activity.trans.contract.CommandDetailsContract;
import tg.tmye.kaba.activity.trans.presenter.CommandDetailsPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.command.source.CommandRepository;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.shoppingcart.source.BasketRepository;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

public class ConfirmCommandDetailsActivity extends AppCompatActivity implements CommandDetailsContract.View, View.OnClickListener {


    public static final String DATA = "DATA";
    public static final String ACTION = "ACTION";

    /* actions of the activity */
    public static final int ACTION_BUY = 90;
    public static final int ACTION_BASKET = 80;

    private HashMap<Restaurant_Menu_FoodEntity, Integer> food_quantity;
    private RecyclerView recyclerview_items_to_buy;
    private ImageButton ib_confirm_purchase_now;
    private ImageButton ib_confirm_add_to_basket;
    private TextView tv_select_address;
    private CheckBox checkbox_pay_arrival;
    private TextView tv_address_description_preview;
    private TextView tv_address_title_preview;
    private RelativeLayout rel_address_preview;
    private TextView tv_press_to_add_to_basket;
    private View ib_delete_selected_address;

    /* actions */
    private int myACTION;
    /* address */
    private DeliveryAddress currentSelectedAddress;

    /* repositories */
    private BasketRepository basketRepository;
    private CommandRepository commandRepository;
    private CustomerDataRepository customerDataRepository;


    /* presenter */
    private CommandDetailsPresenter presenter;

    /* loading dialog fragment */
    private LoadingDialogFragment loadingDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_command_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        /* get command items */

        food_quantity = (HashMap<Restaurant_Menu_FoodEntity, Integer>) getIntent().getSerializableExtra(DATA);

        /* action */
        this.myACTION = getIntent().getIntExtra(ACTION, ACTION_BASKET);

        initViews();
        inflateRecyclerview();

        /* inflate action */
        inflateAction();

        ib_confirm_purchase_now.setOnClickListener(this);
        tv_select_address.setOnClickListener(this);
        ib_confirm_add_to_basket.setOnClickListener(this);
        ib_delete_selected_address.setOnClickListener(this);


        basketRepository = new BasketRepository(this);
        commandRepository = new CommandRepository(this);
        customerDataRepository = new CustomerDataRepository(this);
        presenter = new CommandDetailsPresenter(basketRepository, commandRepository, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    private void inflateAction() {

        if (myACTION == ACTION_BASKET) {
            tv_press_to_add_to_basket.setVisibility(View.VISIBLE);
            ib_confirm_add_to_basket.setVisibility(View.VISIBLE);
            ib_confirm_purchase_now.setVisibility(View.GONE);
            tv_select_address.setVisibility(View.GONE);
            checkbox_pay_arrival.setVisibility(View.GONE);
        } else if (myACTION == ACTION_BUY) {
            tv_press_to_add_to_basket.setVisibility(View.GONE);
            ib_confirm_add_to_basket.setVisibility(View.GONE);
            ib_confirm_purchase_now.setVisibility(View.VISIBLE);
            tv_select_address.setVisibility(View.VISIBLE);
            checkbox_pay_arrival.setVisibility(View.VISIBLE);
        }
    }

    private void inflateRecyclerview() {

        SelectedFoodsAdapter adapter = new  SelectedFoodsAdapter();
        recyclerview_items_to_buy.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_items_to_buy.setAdapter(adapter);
    }

    private void initViews() {
        this.ib_confirm_purchase_now = findViewById(R.id.ib_confirm_purchase_now);
        this.ib_confirm_add_to_basket = findViewById(R.id.ib_confirm_add_to_basket);
        this.tv_select_address = findViewById(R.id.tv_select_address);
        this.recyclerview_items_to_buy = findViewById(R.id.recyclerview_items_to_buy);
        this.checkbox_pay_arrival = findViewById(R.id.checkbox_pay_arrival);
        this.tv_address_description_preview = findViewById(R.id.tv_address_description_preview);
        this.tv_address_title_preview = findViewById(R.id.tv_address_title_preview);
        this.rel_address_preview = findViewById(R.id.rel_address_preview);
        this.tv_press_to_add_to_basket = findViewById(R.id.tv_press_to_add_to_basket);
        this.ib_delete_selected_address = findViewById(R.id.ib_delete_selected_address);
    }

    @Override
    public void showLoading(boolean isLoading) {

        /* get a loading box on the top */
        if (loadingDialogFragment == null) {
            if (isLoading) {
                loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.content_on_loading));
                loadingDialogFragment.show(getSupportFragmentManager(), "loadingbox");
            } else {
            }
        } else {
            if (!isLoading) {
                loadingDialogFragment.dismiss();
            }
        }
    }

    @Override
    public void networkError() {

    }

    @Override
    public void addToBasketSuccessfull(boolean isSuccessfull) {

        mToast(getResources().getString(R.string.add_to_basket_successfull));
        finish();
    }

    @Override
    public void purchaseSuccessfull(boolean isSuccessfull) {

        mToast("Purchase successfull "+isSuccessfull);
        if (isSuccessfull)
            finish();
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
                boolean payAtArrival = checkbox_pay_arrival.isChecked();
                pay(payAtArrival);
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
        }
    }

    private void reinitAddress() {
        rel_address_preview.setVisibility(View.GONE);
        currentSelectedAddress = null;
    }

    private void selectAddress() {
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
                }
                break;
        }
    }

    private void inflateSelectedAddress(DeliveryAddress address) {

        rel_address_preview.setVisibility(View.VISIBLE);
        tv_address_description_preview.setText(address.description);
        tv_address_title_preview.setText(address.name);
    }

    private void pay(boolean payAtArrival) {

     /* */
        if (currentSelectedAddress == null) {
            mToast(getResources().getString(R.string.please_choose_delivery_address));
        } else {
            presenter.purchaseNow(payAtArrival, food_quantity, currentSelectedAddress);
        }
    }

    private void addToBasket() {

        /* send the list of foods with quantities and idz */
        /* get the string and make a get / post request to save it inside the shopping cart */
        presenter.addCommandToBasket(food_quantity);
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

            holder.tv_quantity.setText("X "+quantity);
            holder.tv_name.setText(entity.name.toUpperCase());

            GlideApp.with(ConfirmCommandDetailsActivity.this)
                    .load(Constant.SERVER_ADDRESS+"/"+entity.pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                    .centerCrop()
                    .into(holder.iv_pic);


         /*   Object[] objects = food_id_quantity.keySet().toArray();

            Restaurant_Menu_FoodEntity drinkEntity = getDrinkEntityWithId((Integer) objects[position]);

            holder.tv_name.setText(drinkEntity.name);

            GlideApp.with(FoodDetailsActivity.this)
                    .load(Constant.SERVER_ADDRESS+"/"+drinkEntity.pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                    .centerCrop()
                    .into(holder.iv_pic);

            holder.tv_quantity.setText(
                    "X "+ ((int)food_id_quantity.get(objects[position]))
            );

            holder.itemView.setOnClickListener(new FoodDetailsActivity.SelectedDrinksAdapter.OnDecreaseDrinkClickListener(position));
      */
        }


        @Override
        public int getItemCount() {
            return food_quantity.keySet().size();
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
    }

}
