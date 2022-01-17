package tg.tmye.kaba.partner.activities.hsn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner.activities.commands.contract.MyCommandContract;
import tg.tmye.kaba.partner.activities.hsn.contract.HSNContract;
import tg.tmye.kaba.partner.activities.hsn.presenter.HsnPresenter;
import tg.tmye.kaba.partner.cviews.dialog.InfoDialogFragment;
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.partner.data.command.source.CommandRepository;
import tg.tmye.kaba.partner.data.district.DeliveryDistrict;

public class CreateHSNActivity extends AppCompatActivity implements HSNContract.View, View.OnClickListener {

    private LoadingDialogFragment loadingDialogFragment;

    AutoCompleteTextView ed_location_filter;

    Button bt_reset, bt_accept_fees, bt_confirm;

    TextView tv_delivery_fees;

    EditText ed_phone_number, ed_food_amount, ed_order_description;

    LinearLayoutCompat lny_form, lny_delivery_fees;

    HsnPresenter presenter;

    public List<DeliveryDistrict> districts;
    private Toolbar toolbar;
    private InfoDialogFragment infoDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hsnactivity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.create_hsn));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        initViews();

        CommandRepository repository = new CommandRepository(this);
        presenter = new HsnPresenter(repository, this);

        presenter.loadDeliveryDistricts();

        // download district list

        // filter list with input

        // confirm delivery fees

        // next enter the other informations

        // confirm hsn and launch it


        bt_reset.setOnClickListener(this);
        ed_location_filter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    reset(false);
            }
        });
        bt_confirm.setOnClickListener(this);
        bt_accept_fees.setOnClickListener(this);
    }

    private static String[] DISTRICT = new String[] {

    };

    public void reset (boolean fromButton){
        lny_form.setVisibility(View.GONE);
        lny_delivery_fees.setVisibility(View.GONE);
        if(fromButton) {
            ed_location_filter.setText("");
            ed_phone_number.setText("");
            ed_food_amount.setText("");
            ed_order_description.setText("");
        }

    }

    private void initViews() {
        lny_form = findViewById(R.id.lny_form);
        lny_delivery_fees = findViewById(R.id.lny_delivery_fees);
        ed_location_filter = findViewById(R.id.ed_location_filter);
        bt_confirm = findViewById(R.id.bt_confirm);
        bt_accept_fees = findViewById(R.id.bt_accept_fees);
        bt_reset = findViewById(R.id.bt_reset);
        tv_delivery_fees = findViewById(R.id.tv_delivery_fees);
        ed_phone_number = findViewById(R.id.ed_phone_number);
        ed_food_amount = findViewById(R.id.ed_food_amount);
        ed_order_description = findViewById(R.id.ed_order_description);
    }

    private void _confirm() {

        DeliveryDistrict selectedDistrict = null;
        for (DeliveryDistrict district : districts) {
            selectedDistrict = district;
        }

        presenter.createHSN(""+selectedDistrict.id, ed_phone_number.getText().toString(), ed_food_amount.getText().toString(), ed_order_description.getText().toString());

      /*  mToast(""+ed_phone_number.getText()+" / "+
                ed_food_amount.getText() + " / " +
                ed_order_description.getText() + " / " +
                selectedDistrict.id
        );*/
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void mToastL(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading(boolean isLoading) {

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

    private void showFragment(Fragment loadingDialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(loadingDialogFragment, tag);
        else
            ft.show(loadingDialogFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void sysError() {

    }

    @Override
    public void networkError() {

    }

    @Override
    public void inflateDeliveryDistricts(List<DeliveryDistrict> districts_) {

        /* district to list */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                districts = districts_;
                districtToStringList(districts);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateHSNActivity.this,
                        android.R.layout.simple_dropdown_item_1line, DISTRICT);

                ed_location_filter.setAdapter(adapter);

                ed_location_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _showDeliveryFees(adapter.getItem(position).toString());
                        //         mToast("position "+id+" / "+adapter.getItem(position).toString());

                        ed_location_filter.clearFocus();
                    }
                });
            }
        });
    }

    private void _showDeliveryFees(String selection) {


        DeliveryDistrict selectedDistrict = null;
        for (DeliveryDistrict district : districts) {
            selectedDistrict = district;
        }

        presenter.computeDeliveryBilling(selectedDistrict);

    }


    private void districtToStringList(List<DeliveryDistrict> districts) {
        List<String> tmp = new ArrayList<>();
        districts.forEach(new Consumer<DeliveryDistrict>() {
            @Override
            public void accept(DeliveryDistrict deliveryDistrict) {
                tmp.add(deliveryDistrict.name);
            }
        });
        DISTRICT = tmp.toArray(new String[0]);
    }

    @Override
    public void inflateDeliveryPrice(DeliveryDistrict deliveryDistrict, String deliveryFees) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_delivery_fees.setVisibility(View.VISIBLE);
                tv_delivery_fees.setText(deliveryFees);
            }
        });
    }

    @Override
    public void hsnCreateState(boolean isCreatedSuccessfully) {
        if (isCreatedSuccessfully) {
            mToastL(getString(R.string.hsn_creation_success));
            finish();
        } else {
            mToast(getString(R.string.hsn_creation_error));
        }
    }

    @Override
    public void inflateDeliveryDistrictsError() {
    }

    @Override
    public void inflateDeliveryPriceError() {

    }

    @Override
    public void outOfRange() {
        /* this address is out of range */
        infoDialogFragment = InfoDialogFragment.newInstance(R.drawable.close_cross,
                getResources().getString(R.string.sorry_out_of_range_hsn),
                false);
        showFragment(infoDialogFragment, InfoDialogFragment.TAG, true);
    }

    @Override
    public void setPresenter(MyCommandContract.Presenter presenter) {}


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_reset:
                reset(true);
                break;
            case R.id.bt_confirm:
                _confirm();
                break;
            case R.id.bt_accept_fees:
                lny_form.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }



}