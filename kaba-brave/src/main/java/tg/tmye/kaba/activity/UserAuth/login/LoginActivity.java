package tg.tmye.kaba.activity.UserAuth.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.UserAuth.login.contract.LoginContract;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.syscore.MyKabaApp;


public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    private int dest = -1;
    private int prev = -1;


    private LoginContract.Presenter presenter;
    CustomerDataRepository customerDataRepository;

    /* Views */
    EditText ed_password, ed_phone;
    Button bt_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_yellow_upward_navigation_24dp);

        initViews();
        initEdittextDrawable();

        // check launch origin
        dest = getIntent().getIntExtra(Config.HOME_SWITCH_FRAG_DESTINATION, -1);
        prev = getIntent().getIntExtra(Config.HOME_SWITCH_FRAG_PREVIOUS, -1);

        Intent data = new Intent();
        data.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, prev);
        setResult(Config.LOGIN_FAILURE, data);

        customerDataRepository = new CustomerDataRepository(this);
        presenter = new LoginPresenter(this, customerDataRepository);

        bt_login.setOnClickListener(this);
    }

    private void initEdittextDrawable() {

        Drawable drawable_phone = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_myaccount_phone, null);

        Drawable drawable_password = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_myaccount_password_field, null);

        ed_password.setCompoundDrawablesWithIntrinsicBounds (drawable_phone, null, null, null);
        ed_phone.setCompoundDrawablesWithIntrinsicBounds (drawable_password, null, null, null);
    }

    private void initViews() {

        this.ed_password = findViewById(R.id.ed_password);
        this.ed_phone = findViewById(R.id.ed_phone);
        this.bt_login = findViewById(R.id.bt_login);
    }


    public void iLogin () {

        String password = getPassword();
        String phonecode = getPhoneNumber();

        this.presenter.login(password, phonecode);
    }

    public void loginSuccess () {

        String token = ((MyKabaApp)getApplicationContext()).getAuthToken();
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();

        Intent dataIntent = new Intent();
        dataIntent.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, dest);
        setResult(Config.LOGIN_SUCCESS, dataIntent);
        finish(); /* and make the move straight to the original fragment or activity */
    }

    public void loginFailure (String message) {
        toast(message);
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private String getPhoneNumber() {
        return ed_phone.getText().toString();
    }

    private String getPassword() {
        return ed_password.getText().toString();
    }

   /* public void iLogin(View view) {

        *//* check the contents and validate *//*
        login("", "", new YesOrNoWithResponse() {

            @Override
            public void yes(Object data, boolean isFromOnline) {

                String token = (String) data;
                storeToken(token);
                *//* store into app *//*
                ((MyKabaApp)getApplication()).setAuthToken(token);
                Intent dataIntent = new Intent();
                dataIntent.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, dest);
                setResult(Config.LOGIN_SUCCESS, dataIntent);
                finish();
            }

            @Override
            public void no(Object data, boolean isFromOnline) {
                Intent dataIntent = new Intent();
                dataIntent.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, prev);
                setResult(Config.LOGIN_FAILURE, dataIntent);
            }
        });
    }*/


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


   /* @Override
    public void login(String username, String password, YesOrNoWithResponse yesOrNo) {

        *//* we have random token, and user basic informations*//*


        *//* send request down to network thread *//*
        yesOrNo.yes(UtilFunctions.randomToken(), false); // yes if the login is successfull
    }*/

    /*@Override
    public void checkLogin(YesOrNo yesOrNo) {

        *//**
     * Token Length must also be 33
     * if token last chars = last 4 chars of the phone number, keep it and check later, else logout immediately
     **//*

        String token = "";
        String phoneNumber = "";

        if (token.length() != 33) {
            if (phoneNumber.substring(3).equals(token.substring(28)))
                yesOrNo.yes();
            else yesOrNo.no();
        } else {
            yesOrNo.no();
        }
    }*/


    private void storeToken(String token) {
        SharedPreferences pref = getSharedPreferences(Config.SYS_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Config.SYSTOKEN, token);
        editor.commit();
    }

    @Override
    public void showLoading(boolean isLoading) {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(View view) {

        /*  */
        switch (view.getId()) {
            case R.id.bt_login:
                iLogin();
                break;
        }
    }
}
