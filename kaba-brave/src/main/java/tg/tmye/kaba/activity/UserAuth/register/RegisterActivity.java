package tg.tmye.kaba.activity.UserAuth.register;

import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.UserAuth.register.contract.RegisterContract;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View, View.OnClickListener {

    RegisterPresenter registerPresenter;
    CustomerDataRepository customerDataRepository;


    /* views */
    EditText ed_phone;
    EditText ed_password;
    EditText ed_confirm_password;
    EditText ed_nickname;

    Button bt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_yellow_upward_navigation_24dp);

        /* send registering phone informations */
        /* you can only buy food if you are in togo */
        initViews();
        initEdittextDrawables();

        customerDataRepository = new CustomerDataRepository(this);
        registerPresenter = new RegisterPresenter(this, customerDataRepository);

        bt_register.setOnClickListener(this);
    }

    private void initEdittextDrawables() {

        Drawable drawable_phone = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_myaccount_phone, null);
        Drawable drawable_password = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_myaccount_password_field, null);
        Drawable drawable_user_id = VectorDrawableCompat.create(getResources(),
                R.drawable.myaccount_address_userid, null);

        ed_confirm_password.setCompoundDrawablesWithIntrinsicBounds (drawable_phone, null, null, null);
        ed_password.setCompoundDrawablesWithIntrinsicBounds (drawable_phone, null, null, null);
        ed_phone.setCompoundDrawablesWithIntrinsicBounds (drawable_password, null, null, null);
        ed_nickname.setCompoundDrawablesWithIntrinsicBounds (drawable_password, null, null, null);
    }

    private void initViews() {

        ed_confirm_password = findViewById(R.id.ed_confirm_passwd);
        ed_nickname = findViewById(R.id.ed_nickname);
        ed_password = findViewById(R.id.ed_passwd);
        ed_phone = findViewById(R.id.ed_phone);
        bt_register = findViewById(R.id.bt_register);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.action_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading(boolean isLoading) {

        /* show loading */
    }

    @Override
    public void registerSuccess() {

    }

    @Override
    public void registerFailure(String message) {

    }

    @Override
    public void toast(String message) {

    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_register:
                /* register */
                tryRegister();
                break;
        }
    }

    private void tryRegister() {

        /* retrieve all the usernames from the database and make every request instantly from local */
        String phone = ed_phone.getText().toString();
        String password = ed_password.getText().toString();
        String nickname = ed_nickname.getText().toString();
        /* check all fields

        * 1- emptyness
        * 2- password at least 6 chars
        * 3- username --> at least 6chars
        * 4- phonenumber must follow a regex
         *
         * */

        registerPresenter.register(phone, password, nickname);
    }
}
