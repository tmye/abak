package tg.tmye.kaba.activity.UserAuth.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.ConfirmPayActivity;
import tg.tmye.kaba.activity.UserAuth.getbackpassword.ForgotPasswordActivity;
import tg.tmye.kaba.activity.UserAuth.login.contract.LoginContract;
import tg.tmye.kaba.activity.UserAuth.register.RegisterActivity;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.syscore.MyKabaApp;


public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String PASSWORD = "PASSWORD";
    public static final String IS_REGISTERED = "IS_REGISTERED";


    private int dest = -1;
    private int prev = -1;


    private LoginContract.Presenter presenter;
    CustomerDataRepository customerDataRepository;

    /* Views */
    EditText /*ed_password, */ed_phone;
    Button bt_login, bt_register;
    TextView tv_forgot_password;


    private LoadingDialogFragment loadingDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

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
        bt_register.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);

        /* get PHONE_NUMBER, PASSWORD */
        String phone_number = getIntent().getStringExtra(PHONE_NUMBER);
        String password = getIntent().getStringExtra(PASSWORD);
        boolean is_registered = getIntent().getBooleanExtra(IS_REGISTERED, false);

        if (!"".equals(password) && !"".equals(phone_number)  && is_registered) {
            ed_phone.setText(phone_number);
            presenter.login(password, phone_number);
            bt_login.setEnabled(false);
        }
    }

    private void initEdittextDrawable() {

        Drawable drawable_phone = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_myaccount_phone, null);

        Drawable drawable_password = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_myaccount_password_field, null);

//        ed_password.setCompoundDrawablesWithIntrinsicBounds (drawable_password, null, null, null);
        ed_phone.setCompoundDrawablesWithIntrinsicBounds (drawable_phone, null, null, null);
    }

    private void initViews() {

//        this.ed_password = findViewById(R.id.ed_password);
        this.ed_phone = findViewById(R.id.ed_phone);
        this.bt_login = findViewById(R.id.bt_login);
        this.bt_register = findViewById(R.id.bt_register);
        this.tv_forgot_password = findViewById(R.id.tv_forgot_password);
    }


    public void iLogin () {

        /* get code from the other fields,then come back here to launch everything */

//        String password = getPassword();
        String phonecode = getPhoneNumber();

        /* check phone number */
        if (checkPhoneNumber(phonecode)) {
            /* jump to get password activity */
            Intent intent = new Intent(this, ConfirmPayActivity.class);
            intent.putExtra(PHONE_NUMBER, phonecode);
            intent.putExtra(ConfirmPayActivity.TRANS_TYPE, ConfirmPayActivity.LOGIN);
            intent.putExtra(ConfirmPayActivity.STEP_COUNT, 1);
            startActivity(intent);
            finish();
        }
    }


    private boolean checkPhoneNumber(String phone_number) {

        boolean phoneNumber_tgo = UtilFunctions.isPhoneNumber_TGO(phone_number);

        if (phoneNumber_tgo) {
            return true;
        }
        mToast(getResources().getString(R.string.please_enter_a_local_number));
        return false;
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        bt_login.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bt_login.setEnabled(true);
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private String getPhoneNumber() {
        return ed_phone.getText().toString();
    }

//    private String getPassword() {
//        return ed_password.getText().toString();
//    }

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
            case R.id.bt_register:
                iRegister();
                break;
            case R.id.tv_forgot_password:
                getForgotPassword();
                break;
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }

    private void iRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void getForgotPassword() {

        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
