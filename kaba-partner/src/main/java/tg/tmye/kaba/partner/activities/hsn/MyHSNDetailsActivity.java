package tg.tmye.kaba.partner.activities.hsn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.util.List;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner.activities.hsn.contract.HSNDetailsContract;
import tg.tmye.kaba.partner.activities.hsn.presenter.HSNDetailsPresenter;
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.partner.data.command.source.CommandRepository;
import tg.tmye.kaba.partner.data.hsn.HSN;
import tg.tmye.kaba.partner.data.shoppingcart.BasketInItem;

import static tg.tmye.kaba.partner.activities.hsn.MyHSNActivity.COOKING;
import static tg.tmye.kaba.partner.activities.hsn.MyHSNActivity.DELIVERED;
import static tg.tmye.kaba.partner.activities.hsn.MyHSNActivity.SHIPPING;
import static tg.tmye.kaba.partner.activities.hsn.MyHSNActivity.WAITING;

public class MyHSNDetailsActivity extends AppCompatActivity implements View.OnClickListener, HSNDetailsContract.View {


    public static final String ID = "MyHSNDetailsActivity.ID";
    public static final String COMMAND_ITEM = "MyHSNDetailsActivity.COMMAND_ITEM";
    private static final int PERMISSIONS_REQUEST_CODE = 90;

    private CommandRepository commandRepo;

    NestedScrollView nestedscrollview;

    /* static data */
    private TextView tv_contact_description_preview, tv_address_description_preview;

//    private ImageButton ib_action_shipping_done;
    private ImageButton ib_action_postpone_shipping;
    private TextView tv_shipping_address;

    private Button bt_location_overview, bt_contact_call;

    private LoadingDialogFragment loadingDialogFragment;

    /* ask for the permission once we get in this activity */
    TextView tv_additionnal_infos;
    LinearLayout lny_additionnal_infos;
    RelativeLayout rel_location;

    //    Command command;
    public static String HSN_ = "hsn";
    HSN hsn;

    HSNDetailsPresenter hsnPresenter;

    TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hsndetails);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        setTitle(R.string.hsn_details);

        initViews();

//        ib_action_shipping_done.setOnClickListener(this);
        ib_action_postpone_shipping.setOnClickListener(this);


//        bt_contact_call
        Drawable drawable_phone = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_phone_forwarded_white_24dp, null);

        Drawable drawable_location = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_my_location_white_24dp, null);

        bt_contact_call.setCompoundDrawablesWithIntrinsicBounds (drawable_phone, null, null, null);
        bt_location_overview.setCompoundDrawablesWithIntrinsicBounds (drawable_location, null, null, null);

        hsn = getIntent().getParcelableExtra(HSN_);

        if (hsn == null) {
            mToast(getResources().getString(R.string.sys_error));
            finish();
        } else {
            inflateHsnDetails();
        }

        commandRepo = new CommandRepository(this);
        hsnPresenter = new HSNDetailsPresenter(this, commandRepo);

    }

    private void initViews() {
        nestedscrollview = findViewById(R.id.nestedscrollview);
        tv_contact_description_preview = findViewById(R.id.tv_contact_description_preview);
        tv_address_description_preview = findViewById(R.id.tv_address_description_preview);
        tv_shipping_address = findViewById(R.id.tv_shipping_address);

        ib_action_postpone_shipping = findViewById(R.id.ib_action_postpone_shipping);

        bt_location_overview = findViewById(R.id.bt_location_overview);
        bt_contact_call = findViewById(R.id.bt_contact_call);

        tv_additionnal_infos = findViewById(R.id.tv_additionnal_infos);
        lny_additionnal_infos = findViewById(R.id.lny_additionnal_infos);

        rel_location = findViewById(R.id.rel_location);
        tv_status = findViewById(R.id.tv_status);
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

    @Override
    public void sysError() {

    }

    private void hideFragment() {
        if (loadingDialogFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(loadingDialogFragment);
            loadingDialogFragment = null;
            ft.commitAllowingStateLoss();
        }
    }

    private void showFragment(DialogFragment dialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(dialogFragment, tag);
        else
            ft.show(dialogFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void networkError() {

    }

    @Override
    public void cancelHsnSuccessful(boolean isSuccessfull) {

    }


    public void inflateHsnDetails() {
        /* inflate the commands into the view */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                /* inflate shipping man informations */
                bt_contact_call.setText(getString(R.string.call)+(hsn.phone_number != null ? (" "+hsn.phone_number) : ""));
                bt_contact_call.setOnClickListener(new OnContactViewClickListener(MyHSNDetailsActivity.this, hsn.phone_number));
                tv_shipping_address.setText(hsn.shipping_address);
                tv_status.setText(getStatus(hsn.state));
                tv_status.setBackgroundResource(hsn.state == 0? R.drawable.bg_green_rounded : R.drawable.bg_already_paid_rounded);
                if (hsn.shipping_location_link == null || "".equals(hsn.shipping_location_link.trim())) {
                    rel_location.setVisibility(View.GONE);
                } else {
                    rel_location.setVisibility(View.VISIBLE);
                    bt_location_overview.setOnClickListener(new OnLocationOverviewClickListener(hsn.phone_number, hsn.shipping_location_link));
                }
                /* according to command state, put in the background that we need. */
                switch (hsn.state) {
                    case WAITING:
                        /* deliverying state*/
                        ib_action_postpone_shipping.setBackgroundResource(R.drawable.icon_red_circle);
                        /* set up color of the background of the button */
                        ib_action_postpone_shipping.setVisibility(View.VISIBLE);
                        /* customer contact */
                        ib_action_postpone_shipping.setOnClickListener(new MyHSNDetailsActivity.OnActionButtonClickListener(hsn));
//                        ib_action_shipping_done.setOnClickListener(new MyHSNDetailsActivity.OnActionButtonClickListener(hsn));
                        bt_location_overview.setOnClickListener(new MyHSNDetailsActivity.OnLocationOverviewClickListener(hsn.phone_number, hsn.shipping_location_link));
                        break;
                    default:
                        ib_action_postpone_shipping.setVisibility(View.GONE);
                        break;
                }
                if (!(hsn.infos == null || "".equals(hsn.infos))) {
                    lny_additionnal_infos.setVisibility(View.VISIBLE);
                    tv_additionnal_infos.setText(hsn.infos);
                } else {
                    lny_additionnal_infos.setVisibility(View.GONE);
                }
            }
        });
    }

    private String getStatus(int state) {
        switch (state){
            case WAITING:
                return getString(R.string.waiting);
            case COOKING:
                return getString(R.string.cooking);
            case SHIPPING:
                return getString(R.string.shipping);
            case DELIVERED:
                return getString(R.string.delivered);
            default:
                // cancelled or rejected
                return getString(R.string.title_rejected);
        }
    }


    private void mToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    public void lanchRefund(int orderAmount, int givenAmount, int leftAmount) {
        // launch request
//        presenter.launchRefund(this.command.id, orderAmount, givenAmount, leftAmount);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(HSNDetailsContract.Presenter presenter) {

    }

    class SelectedFoodsAdapter extends RecyclerView.Adapter<SelectedFoodsAdapter.ViewHolder> {

        private final List<BasketInItem> food_list;
        private final Context context;

        public SelectedFoodsAdapter(Context context, List<BasketInItem> food_list) {
            this.context = context;
            this.food_list = food_list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.selected_food_item, parent, false));
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
                ActivityCompat.requestPermissions(MyHSNDetailsActivity.this, new String[]{
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

        private final HSN hsn;

        public OnActionButtonClickListener(HSN hsn) {
            this.hsn = hsn;
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.ib_action_postpone_shipping:
                    /* */
                    finish();
//                    presenter.setPostponeCommand(command);
                    break;
            }
        }
    }

    private class OnLocationOverviewClickListener implements View.OnClickListener {

        private final String location;
        String customer_name;

        public OnLocationOverviewClickListener(String customer_name, String location) {
            /* split it and put in the array */
            this.customer_name = customer_name;
            this.location = location;
        }

        @Override
        public void onClick(View view) {
            // launch link for view

            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(location));
                startActivity(browserIntent);
            } catch (Exception e){
                e.printStackTrace();
                mToast(getString(R.string.sys_error));
            }
        }

    }
}