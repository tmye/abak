package tg.tmye.kaba.activity.UserAcc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.UserAcc.adresses.EditAddressActivity;
import tg.tmye.kaba.activity.UserAcc.adresses.contract.AdressesContract;
import tg.tmye.kaba.activity.UserAcc.adresses.presenter.AdressesPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.delivery.source.DeliveryAdresseRepo;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


public class MyAdressesActivity extends AppCompatActivity implements
        AdressesContract.View,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final int EDIT_ACTIVITY_RESULT = 1;

    /* start activity for result code */
    public static final int CHOOSEN_ADDRESS = 90;

    private static final int TYPE_HAVE_PIC = 10, TYPE_NO_PIC = 11;

    /* presenter */
    AdressesContract.Presenter presenter;

    /* repository */
    DeliveryAdresseRepo repo;

    /* views */
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rc;
    private Button bt_tryagain;

    TextView tv_message;
    LinearLayoutCompat lny_container;
    FloatingActionButton fab_new_address;

    /* */
    boolean returnChoosenAddress = false;

    private boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adresses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        initViews();
        swipeRefreshLayout.setOnRefreshListener(this);
        bt_tryagain.setOnClickListener(this);
        fab_new_address.setOnClickListener(this);
        /* request api to get the common addresses of the client */
        repo = new DeliveryAdresseRepo(this);
        presenter = new AdressesPresenter(this, repo);

        /* if we get here for selecting the address only, then the listener should also be different */
        returnChoosenAddress = getIntent().getBooleanExtra("choose_address", false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequestsRecursive();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
        Glide.with(this).resumeRequestsRecursive();
    }

    List<DeliveryAddress> address;

    private void initViews() {
        fab_new_address = findViewById(R.id.fab_new_address);
        rc = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        tv_message = findViewById(R.id.tv_message);
        lny_container = findViewById(R.id.lny_container);
        bt_tryagain = findViewById(R.id.bt_tryagain);
    }

    @Override
    public void inflateAdresses(DeliveryAddress address) {}

    @Override
    public void inflateAdresses(final List<DeliveryAddress> address) {

        this.address = address;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                if (address == null || address.size() == 0) {
                    fab_new_address.setVisibility(View.VISIBLE);
                    lny_container.setVisibility(View.VISIBLE);
                    tv_message.setText(getResources().getString(R.string.no_address_data));
                    tv_message.setVisibility(View.VISIBLE);
                } else {
                    lny_container.setVisibility(View.VISIBLE);
                    fab_new_address.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyAdressesActivity.this);
                    MyCommonAdressRecyclerAdapter adap = new MyCommonAdressRecyclerAdapter(MyAdressesActivity.this, address, MyAdressesActivity.this);
                    rc.setLayoutManager(linearLayoutManager);

                    if (rc.getItemDecorationCount() == 0) {

                        rc.addItemDecoration(new CommandListSpacesItemDecoration(0,
                                getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
                        ));
                    }
                    rc.setAdapter(adap);
                }
            }
        });
    }

    @Override
    public void showLoading(final boolean isLoading) {

        this.isLoading = isLoading;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isLoading);
                lny_container.setVisibility(View.GONE);
                tv_message.setVisibility(View.GONE);
                bt_tryagain.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void addressCreationSuccess() {

    }

    @Override
    public void addressCreationFailure() {

    }

    @Override
    public void addressDeletedFailure() {
        mToast(getResources().getString(R.string.a_problem_happenned));
    }

    @Override
    public void addressDeletedSuccess() {
        mToast(getResources().getString(R.string.address_deleted_success));
        presenter.populateViews();
    }

    @Override
    public void setPresenter(AdressesContract.Presenter presenter) {
        if (this.presenter == null && presenter != null)
            this.presenter = presenter;
    }

    @Override
    public void onRefresh() {
        presenter.populateViews();
    }

    @Override
    public void onAddressInteraction(DeliveryAddress address) {

    }

    @Override
    public void showCurrentAddressDetails(String quartier, String description_details) {

    }

    @Override
    public void onNetworkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_container.setVisibility(View.VISIBLE);
                tv_message.setText(getResources().getString(R.string.network_error));
                tv_message.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_container.setVisibility(View.VISIBLE);
                tv_message.setText(getResources().getString(R.string.sys_error));
                tv_message.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                presenter.populateViews();
                break;
            case R.id.fab_new_address:
                openCreateAddressActivtyForResult();
                break;
        }
    }

    @Override
    public void onLoggingTimeout() {
        swipeRefreshLayout.setRefreshing(false);
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }


    public class MyCommonAdressRecyclerAdapter extends RecyclerView.Adapter<MyCommonAdressRecyclerAdapter.ViewHolder> {

        private final Context context;
        private final List<DeliveryAddress> data;

        public MyCommonAdressRecyclerAdapter (Context context, List<DeliveryAddress> data, AdressesContract.View mListener) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getItemViewType(int position) {

            DeliveryAddress deliveryAddress = data.get(position);
            if (deliveryAddress.picture == null || deliveryAddress.picture.length == 0) {
                return TYPE_NO_PIC;
            } else {
                return TYPE_HAVE_PIC;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType == TYPE_NO_PIC ? R.layout.common_adress_list_item_no_pic : R.layout.common_adress_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final DeliveryAddress address = data.get(position);
            holder.tv_title.setText(address.name);
            holder.tv_description.setText(address.description);
            holder.tv_adress_date.setText(UtilFunctions.timeStampToDate(context, address.updated_at));
//            holder.iv_edit.setOnClickListener(new AddressDetailsEditOnClickListener(null));

            if (address.picture != null && address.picture.length > 0) {
                holder.iv_main_pic.setVisibility(View.VISIBLE);
                GlideApp.with(context)
                        .load(Constant.SERVER_ADDRESS + "/" + address.picture[0])
                        .transition(GenericTransitionOptions.with(((MyKabaApp) getApplicationContext()).getGlideAnimation()))
                        .placeholder(R.drawable.white_placeholder)
                        .centerCrop()
                        .into(holder.iv_main_pic);

                holder.iv_main_pic.setOnClickListener(new OnImageClickListener() {
                    @Override
                    public void onClick(View view) {

                        /* on transforme ca en ads */
                        AdsBanner[] adsBanners = new AdsBanner[address.picture.length];
                        /*   */
                        for (int i = 0; i < address.picture.length; i++) {
                            AdsBanner adsBanner = new AdsBanner();
                            adsBanner.name = address.name;
                            adsBanner.pic = address.picture[i];
                            adsBanner.description = ("Quartier: "+address.quartier+"\nNon Loin de: "+address.near +"\n"+address.description);
                       adsBanners[i] = adsBanner;
                        }
                        Intent intent = new Intent(context, ImagePreviewActivity.class);
                        intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, adsBanners);
                        context.startActivity(intent);
                    }
                });
            }


            /* inflate the little images of the bottom */

            /*
             *
             *
             * */
            if (returnChoosenAddress) {
                holder.itemView.setOnClickListener(new SelectAddressOnClickListener(address));
            }
//            else {
            /* preview address */
            holder.bt_edit_address.setOnClickListener(new AddressDetailsEditOnClickListener(address));
            /* delete address */
            holder.itemView.setOnLongClickListener(new DeleteAdressOnClickListener(address));
//            }
        }


        @Override
        public int getItemCount() {
            return data.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            //            RecyclerView recyclerview_address_details_image;
            //            ImageView iv_edit, iv_delete;
            ImageView bt_edit_address;
            ImageView iv_main_pic;
            TextView tv_title, tv_description, tv_country_city, tv_adress_date;

            public ViewHolder(View itemView) {
                super(itemView);
                this.iv_main_pic = itemView.findViewById(R.id.iv_head);
                this.bt_edit_address = itemView.findViewById(R.id.bt_edit_address);
                this.tv_description = itemView.findViewById(R.id.tv_adress_details);
                this.tv_title = itemView.findViewById(R.id.tv_title);
                this.tv_adress_date = itemView.findViewById(R.id.tv_adress_date);
//                this.recyclerview_address_details_image = itemView.findViewById(R.id.recyclerview_address_details_image);
//                this.iv_edit = itemView.findViewById(R.id.iv_edit);
//                this.iv_delete = itemView.findViewById(R.id.iv_delete);
//                this.tv_country_city = itemView.findViewById(R.id.tv_country_city);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.manage_address_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_address) {

            if (!isLoading) {
                openCreateAddressActivtyForResult();
                mToast("Create new address");
                // open edit address with a create intent
                // start activity for result
            } else {
                mToast(getResources().getString(R.string.wait_for_loading));
            }
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openCreateAddressActivtyForResult() {

        /* create if current count is less 5 */
      /*  if (address != null && address.size() > 5) {
            mToast(getResources().getString(R.string.address_limit));
            return;
        }*/
        Intent intent = new Intent(this, EditAddressActivity.class);
        startActivityForResult(intent, EDIT_ACTIVITY_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void mToast(String address) {
        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
    }


    private class AddressDetailsEditOnClickListener implements View.OnClickListener {

        private final DeliveryAddress address;

        public AddressDetailsEditOnClickListener(DeliveryAddress address) {
            this.address = address;
        }

        @Override
        public void onClick(View view) {
            Intent in = new Intent(view.getContext(), EditAddressActivity.class);
            /* preview address */
            in.putExtra("address", address);
            view.getContext().startActivity(in);
        }
    }

    private class PreviewAdressOnClickListener implements View.OnClickListener {
        private final DeliveryAddress address;

        public PreviewAdressOnClickListener(DeliveryAddress address) {
            this.address = address;
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class DeleteAdressOnClickListener implements View.OnClickListener, DialogInterface.OnClickListener, View.OnLongClickListener {
        private final DeliveryAddress address;

        public DeleteAdressOnClickListener(DeliveryAddress address) {
            this.address = address;
        }

        @Override
        public void onClick(View view) {
            action();
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            /*  */
            presenter.deleteAddress(address);
        }

        @Override
        public boolean onLongClick(View view) {
            action();
            return true;
        }

        private void action() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyAdressesActivity.this
            )
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.are_you_sure_delete)
                    .setCancelable(true)
                    .setOnCancelListener(null)
                    .setPositiveButton(R.string.bt_delete, this)
                    ;
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                    int color = getResources().getColor(R.color.colorPrimary);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
                }
            });
            dialog.show();
        }
    }

    private class EditAdressOnClickListener implements View.OnClickListener {
        private final DeliveryAddress address;

        public EditAdressOnClickListener(DeliveryAddress address) {
            this.address = address;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MyAdressesActivity.this, EditAddressActivity.class);
            intent.putExtra("address", this.address);
            startActivity(intent);
        }
    }

    private class SelectAddressOnClickListener implements View.OnClickListener {
        private final DeliveryAddress address;

        public SelectAddressOnClickListener(DeliveryAddress address) {
            this.address = address;
        }

        @Override
        public void onClick(View view) {
            /* send back as a response the current selection */
//            CHOOSEN_ADDRESS
            Intent intent = new Intent();
            intent.putExtra("data", address);
            setResult(Activity.RESULT_OK, intent);
            finish();
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


