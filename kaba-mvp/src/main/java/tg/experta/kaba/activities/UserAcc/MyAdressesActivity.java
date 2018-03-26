package tg.experta.kaba.activities.UserAcc;

import android.content.Intent;
import android.os.Bundle;
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

import tg.experta.kaba.R;
import tg.experta.kaba.data.delivery.DeliveryAdress;


public class MyAdressesActivity extends AppCompatActivity {

    private static final int EDIT_ACTIVITY_RESULT = 1;
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

        /* request api to get the common addresses of the client */
        fakeLoadList();
    }

    List<DeliveryAdress> address;

    private void fakeLoadList() {

        LinearLayoutManager lMng = new LinearLayoutManager(this);
        MyCommonAdressRecyclerAdapter adap = new MyCommonAdressRecyclerAdapter();
        rc.setLayoutManager(lMng);
        rc.setAdapter(adap);
    }


    private void initViews() {
        rc = findViewById(R.id.recyclerview);
    }

    //
    public class MyCommonAdressRecyclerAdapter extends RecyclerView.Adapter<MyCommonAdressRecyclerAdapter.ViewHolder> {

        @Override
        public MyCommonAdressRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_adress_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyCommonAdressRecyclerAdapter.ViewHolder holder, int position) {

            holder.iv_edit.setOnClickListener(new AddressDetailsEditOnClickListener(null));
        }

        @Override
        public int getItemCount() {
            return 8;
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
                this.tv_country_city = itemView.findViewById(R.id.tv_country_city);
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

        public AddressDetailsEditOnClickListener(DeliveryAdress address) {
        }

        @Override
        public void onClick(View view) {
            Intent in = new Intent(view.getContext(), EditAddressActivity.class);
            view.getContext().startActivity(in);
        }

    }
}
