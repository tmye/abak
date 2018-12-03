package tg.tmye.kaba.activity.command;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.cviews.CustomProgressbar;
import tg.tmye.kaba._commons.cviews.OffRecyclerview;
import tg.tmye.kaba._commons.cviews.command_details_view.CommandProgressView;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.trans.contract.CommandDetailsContract;
import tg.tmye.kaba.activity.trans.presenter.CommandDetailsPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.data.command.source.CommandRepository;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

public class CommandDetailsActivity extends AppCompatActivity implements CommandDetailsContract.View {


    public static final String ID = "CommandDetailsActivity.ID";

    CommandDetailsPresenter presenter;

    CommandProgressView commandProgressView;

    private CommandRepository commandRepo;

    private OffRecyclerview recyclerview_items_bought;

    NestedScrollView nestedscrollview;


    private CustomProgressbar customProgressBar;

    /* static data */
    private TextView tv_contact_description_preview;
    private TextView tv_address_description_preview;
    private TextView tv_password_delivery_key;
    private TextView tv_last_update;
    private TextView tv_contact_near_preview;

    /* facture values */
    TextView tv_price_delivery, tv_price_total, tv_price_remise, tv_price_net_to_pay, tv_price_command;

    /* rel delivery contact */
    RelativeLayout rel_delivery_man_contact, rel_delivery_man_name;
    TextView tv_delivery_man_name, tv_delivery_man_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        int command_id_int = getIntent().getIntExtra(ID, -1);

        if (command_id_int == -1) {
            mToast(getResources().getString(R.string.sys_error));
            finish();
        }

        //content
        String command_id = ""+command_id_int;

        initViews();

        commandRepo = new CommandRepository(this);

        presenter = new CommandDetailsPresenter(null, commandRepo, null, this);


        // init content details
        presenter.loadCommandDetails(command_id);
    }

    private void initViews() {
        commandProgressView = findViewById(R.id.command_progress_view);
        recyclerview_items_bought = findViewById(R.id.recyclerview_items_bought);
        nestedscrollview = findViewById(R.id.nestedscrollview);
        customProgressBar = findViewById(R.id.progress_bar);
        tv_contact_description_preview = findViewById(R.id.tv_contact_description_preview);
        tv_address_description_preview = findViewById(R.id.tv_address_description_preview);
        tv_last_update = findViewById(R.id.tv_last_update);

        this.tv_price_command = findViewById(R.id.tv_price_command);
        this.tv_price_delivery = findViewById(R.id.tv_price_delivery);
        this.tv_price_net_to_pay = findViewById(R.id.tv_price_net_to_pay);
        this.tv_price_remise = findViewById(R.id.tv_price_remise);
        this.tv_price_total = findViewById(R.id.tv_price_total);
        this.tv_contact_near_preview = findViewById(R.id.tv_contact_near_preview);

        this.rel_delivery_man_name = findViewById(R.id.rel_delivery_man_name);
        this.rel_delivery_man_contact = findViewById(R.id.rel_delivery_man_contact);

        this.tv_delivery_man_contact = findViewById(R.id.tv_contact_delivery_man);
        this.tv_delivery_man_name = findViewById(R.id.tv_name_delivery_man);
        this.tv_password_delivery_key = findViewById(R.id.tv_password_delivery_key);
    }


    @Override
    public void showLoading(final boolean isVisible) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                customProgressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                nestedscrollview.setVisibility(!isVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void networkError() {
        mToast(getResources().getString(R.string.sys_error));
//        finish();
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addToBasketSuccessfull(boolean isSuccessfull) {

    }

    @Override
    public void purchaseSuccessfull(boolean isSuccessfull) {

    }

    @Override
    public void inflateCommandDetails(final Command command, final DeliveryAddress deliveryAddress) {

        /* inflate the commands into the view */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //
                commandProgressView.setCommandState(command.state);

                /* switch */
                if (command.state >= 2) {
                    rel_delivery_man_contact.setVisibility(View.VISIBLE);
                    rel_delivery_man_name.setVisibility(View.VISIBLE);

                    tv_delivery_man_contact.setText(command.livreur.workcontact);
                    tv_delivery_man_name.setText(command.livreur.name);

                } else {
                    rel_delivery_man_contact.setVisibility(View.GONE);
                    rel_delivery_man_name.setVisibility(View.GONE);
                }

                if (command.state >= 1) {
                    /* show command key */
                    tv_password_delivery_key.setText(command.passphrase);
                } else {
                    tv_password_delivery_key.setText("---");
                }

                inflateCommandFood(command.food_list);
                inflateShippingAddress(deliveryAddress, command.last_update);
            }
        });
    }

    @Override
    public void isRestaurantAvailableForBuy(boolean isOpen) {

    }

    @Override
    public void sysError() {

        /* hide everything and show some error text */

    }

    @Override
    public void inflateBillingComputations(boolean out_of_range, final String price_total, final String price_delivery, final String price_remise, final String price_net_to_pay, final String price_command) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tv_price_total.setText(price_total);
                tv_price_remise.setText(price_remise);
                tv_price_delivery.setText(price_delivery);
                tv_price_net_to_pay.setText(price_net_to_pay);
                tv_price_command.setText(price_command);
            }
        });
    }

    @Override
    public void inflateMySolde(String solde) {

    }


    private void inflateShippingAddress(DeliveryAddress deliveryAddress, String last_update) {

        tv_address_description_preview.setText(deliveryAddress.description);
        tv_contact_description_preview.setText(deliveryAddress.phone_number);
        tv_last_update.setText(UtilFunctions.timeStampToDate(this, last_update));
        tv_contact_near_preview.setText(deliveryAddress.near);
    }

    private void inflateCommandFood(List<BasketInItem> food_list) {

        SelectedFoodsAdapter adapter = new  SelectedFoodsAdapter(this, food_list);
        recyclerview_items_bought.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_items_bought.setAdapter(adapter);
    }

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }

    class SelectedFoodsAdapter extends RecyclerView.Adapter<SelectedFoodsAdapter.ViewHolder> {

        private final List<BasketInItem> food_list;
        private final Context context;

        public SelectedFoodsAdapter(Context context, List<BasketInItem> food_list) {
            this.context = context;
            this.food_list = food_list;
        }

        @Override
        public  SelectedFoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SelectedFoodsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.selected_food_item, parent, false));
        }

        @Override
        public void onBindViewHolder(SelectedFoodsAdapter.ViewHolder holder, int position) {

            /* food idz */
            final BasketInItem entity = food_list.get(position);

            holder.tv_quantity.setText("X "+entity.quantity);
            holder.tv_name.setText(entity.name.toUpperCase());
            holder.tv_drink_price.setText(UtilFunctions.intToMoney(entity.price));

            GlideApp.with(CommandDetailsActivity.this)
                    .load(Constant.SERVER_ADDRESS+"/"+entity.pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                    .centerCrop()
                    .into(holder.iv_pic);

            holder.iv_pic.setOnClickListener(new OnImageClickListener() {
                @Override
                public void onClick(View view) {

                    /* on transforme ca en ads */
                    AdsBanner adsBanner = new AdsBanner();
                    adsBanner.name = entity.name;
                    adsBanner.pic = entity.pic;
                    adsBanner.description = entity.details;

                    Intent intent = new Intent(context, ImagePreviewActivity.class);

                    intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, new AdsBanner[]{adsBanner});

                    context.startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return food_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_quantity;
            TextView tv_name;
            ImageView iv_pic;
            TextView tv_drink_price;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    /* everytime come back, let's do something like update the fragment_3
     * if he's the one under focus.
     * */

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
