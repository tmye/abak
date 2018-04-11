package tg.tmye.kaba.activity.UserAcc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.UserAcc.adresses.EditAddressActivity;
import tg.tmye.kaba.activity.UserAcc.adresses.contract.AdressesContract;
import tg.tmye.kaba.activity.UserAcc.adresses.presenter.AdressesPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.delivery.source.DeliveryAdresseRepo;
import tg.tmye.kaba.syscore.GlideApp;


public class MyAdressesActivity extends AppCompatActivity implements AdressesContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final int EDIT_ACTIVITY_RESULT = 1;

    /* presenter */
    AdressesContract.Presenter presenter;

    /* repository */
    DeliveryAdresseRepo repo;

    /* views */
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adresses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_yellow_upward_navigation_24dp);

        initViews();
        swipeRefreshLayout.setOnRefreshListener(this);
        /* request api to get the common addresses of the client */
        repo = new DeliveryAdresseRepo(this);
        presenter = new AdressesPresenter(this, repo);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    List<DeliveryAddress> address;

    private void initViews() {
        rc = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
    }

    @Override
    public void inflateAdresses(DeliveryAddress address) {}

    @Override
    public void inflateAdresses(final List<DeliveryAddress> address) {

        this.address = address;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyAdressesActivity.this);
                MyCommonAdressRecyclerAdapter adap = new MyCommonAdressRecyclerAdapter(MyAdressesActivity.this, address);
                rc.setLayoutManager(linearLayoutManager);
                rc.setAdapter(adap);
            }
        });
    }

    @Override
    public void showLoading(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isLoading);
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
    public void showDeletingSuspendedLoadingBox() {
        /* show suspended */
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


    public class MyCommonAdressRecyclerAdapter extends RecyclerView.Adapter<MyCommonAdressRecyclerAdapter.ViewHolder> {

        private final Context context;
        private final List<DeliveryAddress> data;

        public MyCommonAdressRecyclerAdapter (Context context, List<DeliveryAddress> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_adress_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            DeliveryAddress address = data.get(position);
            holder.tv_title.setText(address.name);
            holder.tv_description.setText(address.description);
            holder.iv_edit.setOnClickListener(new AddressDetailsEditOnClickListener(null));

            if (address.picture.length > 0)
                GlideApp.with(context)
                        .load(Constant.SERVER_ADDRESS +"/"+ address.picture[0])
                        .placeholder(R.drawable.placeholder_kaba)
                        .centerCrop()
                        .into(holder.iv_main_pic);
            else
                GlideApp.with(context)
                        .load(R.drawable.placeholder_kaba)
                        .centerCrop()
                        .into(holder.iv_main_pic);

            holder.itemView.setOnClickListener(new PreviewAdressOnClickListener(address));
            holder.iv_delete.setOnClickListener(new DeleteAdressOnClickListener(address));
            holder.iv_edit.setOnClickListener(new EditAdressOnClickListener(address));
        }


        @Override
        public int getItemCount() {
            return data.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            private final View rootview;
            ImageView iv_edit, iv_delete;
            ImageView iv_main_pic;
            TextView tv_title, tv_description, tv_country_city;

            public ViewHolder(View itemView) {
                super(itemView);
                this.rootview = itemView;
                this.iv_main_pic = itemView.findViewById(R.id.iv_head);
                this.iv_edit = itemView.findViewById(R.id.iv_edit);
                this.iv_delete = itemView.findViewById(R.id.iv_delete);
//                this.tv_country_city = itemView.findViewById(R.id.tv_country_city);
                this.tv_description = itemView.findViewById(R.id.tv_adress_details);
                this.tv_title = itemView.findViewById(R.id.tv_title);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_address_menu, menu);
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
            openCreateAddressActivtyForResult();
            mToast("Create new address");
            // open edit address with a create intent
            // start activity for result
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openCreateAddressActivtyForResult() {

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

        public AddressDetailsEditOnClickListener(DeliveryAddress address) {
        }

        @Override
        public void onClick(View view) {
            Intent in = new Intent(view.getContext(), EditAddressActivity.class);
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

    private class DeleteAdressOnClickListener implements View.OnClickListener, DialogInterface.OnClickListener {
        private final DeliveryAddress address;

        public DeleteAdressOnClickListener(DeliveryAddress address) {
            this.address = address;
        }

        @Override
        public void onClick(View view) {
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

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            /*  */
            presenter.deleteAddress(address);
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
}
