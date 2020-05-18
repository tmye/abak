package tg.tmye.kaba.restaurant.activities.commands;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.restaurant._commons.adapter.ShippingManRecyclerAdapter;
import tg.tmye.kaba.restaurant._commons.utils.UtilFunctions;
import tg.tmye.kaba.restaurant.activities.LoadingIsOkActivtity;
import tg.tmye.kaba.restaurant.activities.commands.contract.CommandDetailsContract;
import tg.tmye.kaba.restaurant.activities.commands.presenter.CommandDetailsPresenter;
import tg.tmye.kaba.restaurant.cviews.OffRecyclerview;
import tg.tmye.kaba.restaurant.cviews.command_details_view.CommandProgressView;
import tg.tmye.kaba.restaurant.cviews.dialog.CancelConfirmationDialogFragment;
import tg.tmye.kaba.restaurant.cviews.dialog.InfoDialogFragment;
import tg.tmye.kaba.restaurant.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.restaurant.data.command.Command;
import tg.tmye.kaba.restaurant.data.command.source.CommandRepository;
import tg.tmye.kaba.restaurant.data.delivery.DeliveryAddress;
import tg.tmye.kaba.restaurant.data.delivery.KabaShippingMan;
import tg.tmye.kaba.restaurant.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.GlideApp;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;
import tg.tmye.kaba.restaurant.R;


public class CommandDetailsActivity extends LoadingIsOkActivtity implements CommandDetailsContract.View, View.OnClickListener {

    private static final int WAITING = 0;
    private static final int COOKING = 1;
    private static final int SHIPPING = 2;
    private static final int DELIVERED = 3;
    private static final int OTHERS = 4;


    public static final String ID = "CommandDetailsActivity.ID";
    private static final int PERMISSIONS_REQUEST_CODE = 10;

    CommandDetailsPresenter presenter;

    CommandProgressView commandProgressView;

    private CommandRepository commandRepo;

    private OffRecyclerview recyclerview_items_bought;

    NestedScrollView nestedscrollview;

    TextView tv_bottom_explain;

    ImageButton ib_action_button, ib_action_cancel;

    /* static data */
    private TextView tv_contact_description_preview;
    private TextView tv_address_description_preview;
    private TextView tv_quartier, tv_contact_near_preview;
    private TextView tv_last_update;
    private RecyclerView recycler_kabaman_list;
    TextView tv_select_shipping_man;
    Button bt_contact_call;

    private Command main_command_item;
    private List<KabaShippingMan> shippingMen = null;

    private LoadingDialogFragment loadingDialogFragment;

    /* lny for delivery */
    LinearLayout lny_delivery;
    private TextView tv_kabaman_name, tv_kabaman_phone;
    private CircleImageView cic_kaba_man;
    private CancelConfirmationDialogFragment cancel_dialog;
    private ShippingManRecyclerAdapter adapter;


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

        presenter = new CommandDetailsPresenter( commandRepo, this);

        // init content details
        presenter.loadCommandDetails(command_id);

        ib_action_button.setOnClickListener(this);
        ib_action_cancel.setOnClickListener(this);
        bt_contact_call.setOnClickListener(this);
    }

    private void initViews() {
        commandProgressView = findViewById(R.id.command_progress_view);
        recyclerview_items_bought = findViewById(R.id.recyclerview_items_bought);
        nestedscrollview = findViewById(R.id.nestedscrollview);
        tv_contact_description_preview = findViewById(R.id.tv_contact_description_preview);
        tv_address_description_preview = findViewById(R.id.tv_address_description_preview);
        tv_last_update = findViewById(R.id.tv_last_update);
        recycler_kabaman_list = findViewById(R.id.recycler_kabaman_list);
        tv_quartier = findViewById(R.id.tv_quartier);
        tv_contact_near_preview = findViewById(R.id.tv_contact_near_preview);
        bt_contact_call = findViewById(R.id.bt_contact_call);

        tv_bottom_explain = findViewById(R.id.tv_explain_content);
        ib_action_button = findViewById(R.id.ib_action_button);
        ib_action_cancel = findViewById(R.id.ib_action_cancel);
        tv_select_shipping_man = findViewById(R.id.tv_select_shipping_man);

        lny_delivery = findViewById(R.id.lny_delivery_man);

        /* items */
        tv_kabaman_name = findViewById(R.id.tv_kaba_man_name);
        tv_kabaman_phone = findViewById(R.id.tv_kaba_man_phone);
        cic_kaba_man = findViewById(R.id.cic_kaba_man);


        Drawable drawable_phone = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_phone_forwarded_white_24dp, null);
        bt_contact_call.setCompoundDrawablesWithIntrinsicBounds (drawable_phone, null, null, null);

    }


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


    private void hideFragment(DialogFragment loadingDialogFragment) {
        if (loadingDialogFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(loadingDialogFragment);
            ft.commitAllowingStateLoss();
            loadingDialogFragment = null;
        }
    }

    private void showFragment(DialogFragment loadingDialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(loadingDialogFragment, tag);
        else
            ft.show(loadingDialogFragment);
        ft.commitAllowingStateLoss();
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
    public void inflateCommandDetails(final Command command, final List<KabaShippingMan> shippingMEN, final DeliveryAddress deliveryAddress) {

        /* inflate the commands into the view */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /* according to command state, put in the background that we need. */
                switch (command.state) {
                    case WAITING:
                        ib_action_cancel.setVisibility(View.VISIBLE);
                        ib_action_button.setBackgroundResource(R.drawable.icon_orange_circle);
                        tv_select_shipping_man.setVisibility(View.GONE);
                        recycler_kabaman_list.setVisibility(View.GONE);
                        break;
                    case COOKING:
                        ib_action_button.setBackgroundResource(R.drawable.icon_green_circle);
                        ib_action_cancel.setVisibility(View.GONE);

                        // for the restaurants, i'm hiding this
                        if (Constant.IS_RESTAURANT_APP){
                            tv_select_shipping_man.setVisibility(View.GONE);
                            recycler_kabaman_list.setVisibility(View.GONE);
                            ib_action_button.setVisibility(View.GONE);
                        }
                        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                        break;
                    default:
                        tv_select_shipping_man.setVisibility(View.GONE);
                        recycler_kabaman_list.setVisibility(View.GONE);
                        ib_action_cancel.setVisibility(View.GONE);
                        break;
                }
                //
//                inflateShippingMenList(shippingMEN);
                if (command.state < SHIPPING) {
                    String[] action_explain = getResources().getStringArray(R.array.command_details_explain);
                    tv_bottom_explain.setText(action_explain[command.state]);
                    /* set up color of the background of the button */
                    tv_bottom_explain.setVisibility(View.VISIBLE);
                    ib_action_button.setVisibility(View.VISIBLE);

                    if (command.state == 1) {
                        /* is cooking, we need to list the livreurs */
                        if (shippingMEN != null && shippingMEN.size() > 0 && !Constant.IS_RESTAURANT_APP) {
                            inflateShippingMenList(shippingMEN);
                            tv_select_shipping_man.setVisibility(View.VISIBLE);
                            recycler_kabaman_list.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tv_select_shipping_man.setVisibility(View.GONE);
                        recycler_kabaman_list.setVisibility(View.GONE);
                    }
                    lny_delivery.setVisibility(View.GONE);
                } else if (command.state >= SHIPPING && command.state <= DELIVERED){
                    tv_bottom_explain.setVisibility(View.GONE);
                    ib_action_button.setVisibility(View.GONE);
                    lny_delivery.setVisibility(View.VISIBLE);
                    if (command.livreur != null)
                        inflateKabaManInfos(command.livreur);
                }
                CommandDetailsActivity.this.main_command_item = command;
                commandProgressView.setCommandState(command.state);
                inflateCommandFood(command.food_list);
                inflateShippingAddress(deliveryAddress, command.last_update);

                if (Constant.IS_RESTAURANT_APP){
                    tv_select_shipping_man.setVisibility(View.GONE);
                    ib_action_button.setVisibility(View.GONE);
                    tv_bottom_explain.setText(getResources().getString(R.string.wait_until_dispatched));
                }

                bt_contact_call.setOnClickListener(new OnContactViewClickListener(CommandDetailsActivity.this, command.shipping_address.phone_number));
            }
        });
    }

    private void inflateKabaManInfos(KabaShippingMan livreur) {

        tv_kabaman_name.setText(livreur.name);
        tv_kabaman_phone.setText(livreur.workcontact);

        GlideApp.with(this)
                .load(Constant.SERVER_ADDRESS+ "/" +livreur.pic)
                .centerCrop()
                .into(cic_kaba_man);
    }

    private void inflateShippingMenList(List<KabaShippingMan> shippingMEN) {

        /* feels like using a recyclerview instead of this -- > */

        this.shippingMen = shippingMEN;

        if (shippingMen.size() > 0) {

            tv_select_shipping_man.setText(getResources().getString(R.string.select_shipping_man));
            adapter = new ShippingManRecyclerAdapter(this, shippingMEN);
            recycler_kabaman_list.setLayoutManager(new LinearLayoutManager(this));
            recycler_kabaman_list.setAdapter(adapter);
            recycler_kabaman_list.setVisibility(View.VISIBLE);
            ib_action_button.setVisibility(View.VISIBLE);
        } else {
            tv_select_shipping_man.setText(getResources().getString(R.string.there_is_no_shipping_man));
            recycler_kabaman_list.setVisibility(View.GONE);
            ib_action_button.setVisibility(View.GONE);
        }

       /* if (shippingMEN.size() > 0)
            for (int i = 0; i < shippingMEN.size(); i++) {

                RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.shipping_man_radio_item, recycler_kabaman_list, false);
                radioButton.setText(shippingMEN.get(i).name);
                radioButton.setTextColor(getResources().getColor(R.color.black));
                recycler_kabaman_list.addView(radioButton);
                if (i == 0) {
                    radioButton.setChecked(true);
                }

                *//* get drawable from image -  *//*
         *//*   radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        VectorDrawableCompat.create(getResources(), R.drawable.ic_launcher_background, null),
                        null);*//*
            }
        else {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.shipping_man_radio_item, recycler_kabaman_list, false);
            radioButton.setTextColor(getResources().getColor(R.color.light_gray));
            radioButton.setActivated(false);
            radioButton.setEnabled(false);
         *//*   radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_launcher_background, null),
                    null);*//*
            recycler_kabaman_list.addView(radioButton);
        }*/
    }


    @Override
    public void syserror() {}


    @Override
    public void success(final boolean succesfull, final int newState) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                InfoDialogFragment infoDialogFragment = null;
                /* according to the state, do something*/
                if (succesfull) {
                    ib_action_button.setVisibility(View.GONE);
                    tv_bottom_explain.setVisibility(View.GONE);
                    /* icon for be cooking */
                    if (newState == 1) {
                        infoDialogFragment = InfoDialogFragment.newInstance(R.drawable.ic_sent_to_cooking, getResources().getString(R.string.sent_to_cooking), succesfull);
                    } else if (newState == 2) {
                        infoDialogFragment = InfoDialogFragment.newInstance(R.drawable.ic_sent_to_delivering, getResources().getString(R.string.sent_to_delivering), succesfull);
                    }
                    UtilFunctions.playSuccessSound (getBaseContext());
                } else {
                    infoDialogFragment = InfoDialogFragment.newInstance(R.drawable.ic_sent_to_cooking, getResources().getString(R.string.sys_error), succesfull);
                }
                if (infoDialogFragment != null)
                    infoDialogFragment.show(getSupportFragmentManager(), "loadingbox");
            }
        });
    }

    @Override
    public void cancelCommand(int motif) {

        presenter.cancelCommand(main_command_item.id, motif);
    }

    @Override
    public void onCancelSuccess() {
        finish();
    }

    private void inflateShippingAddress(DeliveryAddress deliveryAddress, String last_update) {
        tv_address_description_preview.setText(deliveryAddress.description);
        tv_contact_description_preview.setText(deliveryAddress.phone_number);
        tv_last_update.setText(UtilFunctions.timeStampToDate(this, last_update));
        tv_quartier.setText(deliveryAddress.quartier);
        tv_contact_near_preview.setText(deliveryAddress.near);
    }

    private void inflateCommandFood(List<BasketInItem> food_list) {
        SelectedFoodsAdapter adapter = new  SelectedFoodsAdapter(this, food_list);
        recyclerview_items_bought.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_items_bought.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_action_cancel:
                cancelCommand();
                break;
            default:
                if (main_command_item != null) {
                    sendAction();
                } else {
                    mToast(getResources().getString(R.string.sys_error));
                }
                break;
        }
    }

    private void cancelCommand() {

        /* ask the restaurant what is the purpose? */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cancel_dialog == null) {
                    cancel_dialog = CancelConfirmationDialogFragment.newInstance();
                    showFragment(cancel_dialog, "cancel_dialog", true);
                } else {
                    showFragment(cancel_dialog, "cancel_dialog", true);
                }
            }
        });
    }

    private void sendAction() {
        if (main_command_item.state == 0)
            presenter.acceptCommand(main_command_item);
        if (main_command_item.state == 1) {
            if (adapter == null || (adapter != null && adapter.getSelected() == -1)) {
                mToast(getResources().getString(R.string.please_select_shipping_man));
            } else {
                int choice = adapter.getSelected();
                KabaShippingMan shippingMan = shippingMen.get(choice);
                presenter.sendCommandToShipping(main_command_item, shippingMan);
            }
        }
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
            BasketInItem entity = food_list.get(position);
            holder.tv_quantity.setText("X "+entity.quantity);
            holder.tv_name.setText(entity.name.toUpperCase());

            GlideApp.with(CommandDetailsActivity.this)
                    .load(Constant.SERVER_ADDRESS+"/"+entity.pic)
                    .transition(GenericTransitionOptions.with(  ((MyRestaurantApp)getApplicationContext()).getGlideAnimation()  ))
                    .centerCrop()
                    .into(holder.iv_pic);
        }


        @Override
        public int getItemCount() {
            return food_list.size();
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



    class OnContactViewClickListener implements View.OnClickListener {

        private final String phone_number;
        private final Context context;

        public OnContactViewClickListener(Context context, String phone_number) {
            this.context = context;
            this.phone_number = phone_number;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CommandDetailsActivity.this, new String[]{
                        Manifest.permission.CALL_PHONE
                }, PERMISSIONS_REQUEST_CODE);
                return;
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            mToast(getResources().getString(R.string.failed_in_allowing_permission));
        }else {
            bt_contact_call.performClick();
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

}
