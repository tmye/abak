package tg.tmye.kaba_i_deliver.activity.command;

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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver._commons.utils.UtilFunctions;
import tg.tmye.kaba_i_deliver.activity.command.contract.CommandDetailsContract;
import tg.tmye.kaba_i_deliver.cviews.OffRecyclerview;
import tg.tmye.kaba_i_deliver.cviews.command_details_view.CommandProgressView;
import tg.tmye.kaba_i_deliver.cviews.dialog.InfoDialogFragment;
import tg.tmye.kaba_i_deliver.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.data.delivery.DeliveryAddress;
import tg.tmye.kaba_i_deliver.data.delivery.KabaShippingMan;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;
import tg.tmye.kaba_i_deliver.data.shoppingcart.BasketInItem;


public class CommandDetailsActivity extends AppCompatActivity implements CommandDetailsContract.View, View.OnClickListener {

    public static final String ID = "CommandDetailsActivity.ID";
    public static final String COMMAND_ITEM = "CommandDetailsActivity.COMMAND_ITEM";
    private static final int PERMISSIONS_REQUEST_CODE = 90;

    CommandDetailsPresenter presenter;

    CommandProgressView commandProgressView;

    private DeliveryManRepository deliverRepo;
    private CommandRepository commandRepo;

    private OffRecyclerview recyclerview_items_bought;

    NestedScrollView nestedscrollview;

    TextView tv_bottom_explain;

    /* static data */
    private TextView tv_contact_description_preview, tv_contact_district_preview, tv_address_description_preview;
    private TextView tv_password_delivery_key;
    private TextView tv_last_update;

    RelativeLayout rel_contact;
    RelativeLayout rel_district;

    private Command main_command_item;
    private List<KabaShippingMan> shippingMen = null;
    private ImageButton ib_action_shipping_done;
    private ImageButton ib_action_postpone_shipping;
    private ImageButton ib_action_start_shipping;
    private TextView tv_shipping_man_code;
    private TextView tv_address_near;

    private Button bt_location_overview, bt_contact_call;

    private LoadingDialogFragment loadingDialogFragment;

    /* ask for the permission once we get in this activity */
    TextView tv_additionnal_infos, tv_additionnal_infos_title, tv_delivery_day, tv_delivery_time;
    LinearLayout  lny_additionnal_infos, lny_preorder_infos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        initViews();

        ib_action_shipping_done.setOnClickListener(this);
        ib_action_postpone_shipping.setOnClickListener(this);


//        bt_contact_call
        Drawable drawable_phone = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_phone_forwarded_white_24dp, null);

        /*Drawable drawable_location = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_my_location_white_24dp, null);*/

        Drawable drawable_location = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_my_location_white_24dp, null);

        bt_contact_call.setCompoundDrawablesWithIntrinsicBounds (drawable_phone, null, null, null);
        bt_location_overview.setCompoundDrawablesWithIntrinsicBounds (drawable_location, null, null, null);


        if (!getIntent().getExtras().containsKey(COMMAND_ITEM)) {
            int command_id_int = getIntent().getIntExtra(ID, -1);

            if (command_id_int == -1) {
                mToast(getResources().getString(R.string.sys_error));
                finish();
            }

            //content
            String command_id = "" + command_id_int;

            commandRepo = new CommandRepository(this);
            presenter = new CommandDetailsPresenter(commandRepo, this);

            // init content details
            presenter.loadCommandDetails(command_id);

        } else {
            commandRepo = new CommandRepository(this);
            presenter = new CommandDetailsPresenter(commandRepo, this);
            Command command = getIntent().getParcelableExtra(COMMAND_ITEM);
            inflateCommandDetails(command, command.shipping_address);
        }

    }

    private void initViews() {
        commandProgressView = findViewById(R.id.command_progress_view);
        recyclerview_items_bought = findViewById(R.id.recyclerview_items_bought);
        nestedscrollview = findViewById(R.id.nestedscrollview);
        tv_contact_description_preview = findViewById(R.id.tv_contact_description_preview);
        tv_address_description_preview = findViewById(R.id.tv_address_description_preview);
        tv_contact_district_preview = findViewById(R.id.tv_contact_district_preview);
        tv_last_update = findViewById(R.id.tv_last_update);
        tv_address_near = findViewById(R.id.tv_contact_near_preview);

        tv_bottom_explain = findViewById(R.id.tv_explain_content);
        ib_action_shipping_done = findViewById(R.id.ib_action_shipping_done);
        ib_action_postpone_shipping = findViewById(R.id.ib_action_postpone_shipping);
        ib_action_start_shipping = findViewById(R.id.ib_action_start_shipping);
        tv_shipping_man_code = findViewById(R.id.tv_shipping_man_code);
        tv_password_delivery_key = findViewById(R.id.tv_password_delivery_key);
        rel_contact = findViewById(R.id.rel_contact);
        rel_district = findViewById(R.id.rel_district);

        bt_location_overview = findViewById(R.id.bt_location_overview);
        bt_contact_call = findViewById(R.id.bt_contact_call);



        tv_additionnal_infos = findViewById(R.id.tv_additionnal_infos);
        tv_additionnal_infos_title = findViewById(R.id.tv_additionnal_infos_title);
        lny_additionnal_infos = findViewById(R.id.lny_additionnal_infos);
        lny_preorder_infos = findViewById(R.id.lny_preorder_infos);
      tv_delivery_day = findViewById(R.id.tv_delivery_day);
       tv_delivery_time = findViewById(R.id.tv_delivery_time);
    }


    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (loadingDialogFragment == null) {
                    if (isLoading) {
                        loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.content_on_loading));
                        showFragment(loadingDialogFragment, "loadingbox", true);
                    }
                } else {
                    if (isLoading) {
                        showFragment(loadingDialogFragment, "loadingbox",false);
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

    }


    private void mToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inflateCommandDetails(final Command command, final DeliveryAddress deliveryAddress) {

        /* inflate the commands into the view */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                /* inflate shipping man informations */
                KabaShippingMan theShippingMan = DeliveryManRepository.getShippingMan(CommandDetailsActivity.this);
                tv_address_near.setText(deliveryAddress.near);
                tv_shipping_man_code.setText(command.state > 1 && command.state < 4 ?  theShippingMan.name : "");
                bt_contact_call.setOnClickListener(new OnContactViewClickListener(CommandDetailsActivity.this, command.shipping_address.phone_number));
                bt_location_overview.setOnClickListener(new OnLocationOverviewClickListener(command.shipping_address.phone_number, command.shipping_address.location));
                /* according to command state, put in the background that we need. */
                ib_action_start_shipping.setOnClickListener(new OnActionButtonClickListener(command));
                ib_action_start_shipping.setVisibility(View.GONE);
                switch (command.state) {
                    case 2:
                        /* deliverying state*/
                        ib_action_postpone_shipping.setBackgroundResource(R.drawable.icon_red_circle);
                        ib_action_shipping_done.setBackgroundResource(R.drawable.icon_green_circle);

                        /* set up color of the background of the button */
                        tv_bottom_explain.setVisibility(View.VISIBLE);
                        ib_action_shipping_done.setVisibility(View.VISIBLE);
                        ib_action_postpone_shipping.setVisibility(View.VISIBLE);
                        /* customer contact */
                        tv_contact_description_preview.setText(command.shipping_address.phone_number);
                        tv_contact_district_preview.setText(command.shipping_address.quartier);
                        ib_action_postpone_shipping.setOnClickListener(new OnActionButtonClickListener(command));
                        ib_action_shipping_done.setOnClickListener(new OnActionButtonClickListener(command));
                        bt_location_overview.setOnClickListener(new OnLocationOverviewClickListener(command.shipping_address.phone_number, command.shipping_address.location));
                        break;
                    case 1:
                        ib_action_start_shipping.setVisibility(View.VISIBLE);
                    default:
                        tv_bottom_explain.setVisibility(View.GONE);
                        ib_action_shipping_done.setVisibility(View.GONE);
                        ib_action_postpone_shipping.setVisibility(View.GONE);
                        break;
                }

                tv_password_delivery_key.setText(command.passphrase);
                CommandDetailsActivity.this.main_command_item = command;
                commandProgressView.setCommandState(command.state);
                inflateCommandFood(command.food_list);
                inflateShippingAddress(deliveryAddress, command.last_update);

                if (!(command.infos == null || "".equals(command.infos))) {
                    lny_additionnal_infos.setVisibility(View.VISIBLE);
                    tv_additionnal_infos.setText(command.infos);
                } else {
                    lny_additionnal_infos.setVisibility(View.GONE);
                }

                lny_preorder_infos.setVisibility(command.is_preorder == 0 ? View.GONE : View.VISIBLE);
                if (command.is_preorder != 0) {
                    tv_delivery_day.setText(UtilFunctions.timeStampToDayDate(CommandDetailsActivity.this, command.preorder_hour.start));
                    tv_delivery_time.setText(UtilFunctions.timeStampToHourMinute(CommandDetailsActivity.this, command.preorder_hour.start)+" à "+UtilFunctions.timeStampToHourMinute(CommandDetailsActivity.this, command.preorder_hour.end));
                }
            }
        });
    }

    @Override
    public void syserror() {
    }


    @Override
    public void success(final boolean succesfull, final int newState) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                InfoDialogFragment infoDialogFragment = null;
                /* according to the state, do something*/
                if (succesfull) {
                    ib_action_shipping_done.setVisibility(View.GONE);
                    ib_action_postpone_shipping.setVisibility(View.GONE);
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
    public void setShippingDoneSuccess(boolean isSuccess) {

        /* not easy */
        if (isSuccess) {
            /* send to an activity */
            InfoDialogFragment infoDialogFragment = InfoDialogFragment.newInstance(R.drawable.ic_deliver_success, getResources().getString(R.string.confirm_deliver_success), InfoDialogFragment.CONFIRM_DELIVERING_SUCCESS);
            infoDialogFragment.show(getSupportFragmentManager(), "loadingbox");
        } else {
            showLoading(false);
        }
    }

    @Override
    public void startShippingSuccess(final boolean isSuccessfull) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               /* not easy */
               if (isSuccessfull) {
                   /* send to an activity */
                   ib_action_start_shipping.setVisibility(View.GONE);
                   InfoDialogFragment infoDialogFragment = InfoDialogFragment.newInstance(R.drawable.rocket_start, getResources().getString(R.string.start_shipping_success), InfoDialogFragment.CONFIRM_DELIVERING_SUCCESS);
                   infoDialogFragment.show(getSupportFragmentManager(), "infodialog");
               } else {
                   showLoading(false);
               }
           }
       });
    }

    private void inflateShippingAddress(DeliveryAddress deliveryAddress, String last_update) {

        tv_address_description_preview.setText(deliveryAddress.description);
        tv_contact_description_preview.setText(deliveryAddress.phone_number);
        tv_last_update.setText(UtilFunctions.timeStampToDate(this, last_update));
    }

    private void inflateCommandFood(List<BasketInItem> food_list) {

        SelectedFoodsAdapter adapter = new  SelectedFoodsAdapter(this, food_list);
        recyclerview_items_bought.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_items_bought.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {


        if (main_command_item != null) {

            mToast("IN TO CONSTRUCTION!");
        } else {
            mToast(getResources().getString(R.string.sys_error));
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

        }


        @Override
        public int getItemCount() {
            return food_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_quantity;
            TextView tv_name;

            public ViewHolder(View itemView) {
                super(itemView);

                this.tv_quantity = itemView.findViewById(R.id.tv_drink_count);
                this.tv_name = itemView.findViewById(R.id.tv_drink_name);
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

    private class OnActionButtonClickListener implements View.OnClickListener {

        private final Command command;

        public OnActionButtonClickListener(Command command) {
            this.command = command;
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.ib_action_shipping_done:
                    /* if success - congratz */
                    presenter.setShippingToDone(command);
                    break;
                case R.id.ib_action_postpone_shipping:
                    /* */
                    presenter.setPostponeCommand(command);
                    break;
                case R.id.ib_action_start_shipping:
                    presenter.startShipping(command);
                    break;
            }
        }
    }

    private class OnLocationOverviewClickListener implements View.OnClickListener {

        private boolean isOk = true;
        String longitude, latitude;
        String customer_name;

        public OnLocationOverviewClickListener(String customer_name, String location) {
            /* split it and put in the array */
            this.customer_name = customer_name;
            try {
                String[] splitted_location = location.split(":");
                latitude = splitted_location[0];
                longitude = splitted_location[1];
            } catch (Exception e) {
                e.printStackTrace();
                isOk = false;
            }
        }

        @Override
        public void onClick(View view) {
            if (!isOk) {
                mToast(getResources().getString(R.string.sys_error));
            } else {
                openInMap();
            }
        }

        void openInMap () {

            // Create a Uri from an intent string. Use the result to create an Intent.
            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+latitude+","+longitude+"("+customer_name+")");
            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
            } else {
                mToast(getResources().getString(R.string.google_maps_not_installed));
            }
        }

    }
}
