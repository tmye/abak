package tg.tmye.kaba.activity.UserAuth.register;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.ConfirmPayActivity;
import tg.tmye.kaba.activity.UserAuth.login.LoginActivity;
import tg.tmye.kaba.activity.UserAuth.register.contract.RegisterContract;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.syscore.MyKabaApp;
import tg.tmye.kaba.syscore.ParallelThreadBase;

import static tg.tmye.kaba.activity.UserAuth.login.LoginActivity.IS_REGISTERED;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View, View.OnClickListener {

    public static final String LAST_TIME_CODE_SEND = "LAST_TIME_CODE_SEND";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String NICK_NAME = "NICK_NAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String REQUEST_ID = "REQUEST_ID";
    public static final String PWD_1 = "PWD_1";

    private static final int MY_PERMISSION_REQUEST_RECEIVE_SMS = 12;
    private static final int MY_PERMISSION_REQUEST_READ_SMS = 13;


    private static final int MIN =  60 * 1000;
    private long MIN_ELLAPSE_TIME = 5 * MIN;


    private boolean life = true;

    RegisterPresenter registerPresenter;
    CustomerDataRepository customerDataRepository;

    /* views */
    EditText ed_phone;
    //    EditText ed_password;
//    EditText ed_confirm_password;
    EditText ed_nickname;

    Button bt_register, bt_send_verification_code;
    EditText ed_code;

    private LoadingDialogFragment loadingDialogFragment;

    private String request_id = "";

    private String current_phone_number = "";

    private RegisterSmsBroadcastListener br;

    private int PASSWORD_MIN_LENGTH = 4;

    private int CODE_LENGTH = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_DENIED) {
            requestReceiveSmsPermission();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_DENIED) {
            requestReadSmsPermission();
            return;
        }

        /* if permission is there ? startActivity */
        runApp();
    }

    private void runApp() {

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        /* send registering phone informations */
        /* you can only buy food if you are in togo */
        initViews();
        initEdittextDrawables();

        customerDataRepository = new CustomerDataRepository(this);
        registerPresenter = new RegisterPresenter(this, customerDataRepository);

        bt_register.setOnClickListener(this);
        bt_send_verification_code.setOnClickListener(this);

        /* launch discount of seconds if there is a waiting in row. */
        initSendCodeBasics();
        registerPresenter.start();

        /* get informations */
        String phone_number = getIntent().getStringExtra(PHONE_NUMBER);
        String password = getIntent().getStringExtra(PASSWORD);
        String nickname = getIntent().getStringExtra(NICK_NAME);

        boolean is_registered = getIntent().getBooleanExtra(IS_REGISTERED, false);

        /* we get back here with all informations required,and we create the thing*/
        if (!"".equals(password) && !"".equals(phone_number) && !"".equals(nickname) && is_registered) {
            ed_phone.setText(phone_number);
            ed_nickname.setText(nickname);
            registerPresenter.register(phone_number,password, nickname, request_id);
        }
    }

    private void requestReadSmsPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[] {
                    Manifest.permission.READ_SMS
            }, MY_PERMISSION_REQUEST_READ_SMS);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_SMS
            }, MY_PERMISSION_REQUEST_READ_SMS);
        }
    }

    private void requestReceiveSmsPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[] {
                    Manifest.permission.RECEIVE_SMS
            }, MY_PERMISSION_REQUEST_RECEIVE_SMS);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.RECEIVE_SMS
            }, MY_PERMISSION_REQUEST_RECEIVE_SMS);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (!(requestCode == MY_PERMISSION_REQUEST_RECEIVE_SMS || requestCode == MY_PERMISSION_REQUEST_READ_SMS)) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            /* start the activity */
        } else {
            Toast.makeText(this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    CodeDiscountThread discountThread;

    private void initSendCodeBasics() {

        long diff = getNowTime() - getLastTimeCodeSent();

        if (diff > MIN_ELLAPSE_TIME) {
            bt_send_verification_code.setTag(true);
        } else {

            this.request_id = getRequestId();
            //
            bt_send_verification_code.setTag(false);
            /* launch discount thread*/
            life = true;
            if (parallelThreadBase == null) {
                parallelThreadBase = ((MyKabaApp)getApplicationContext()).getParaThreadBase();
            }
            discountThread = new CodeDiscountThread();
            parallelThreadBase.getmHandler().post(discountThread);
        }
    }

    ParallelThreadBase parallelThreadBase;


    private void initEdittextDrawables() {

        Drawable drawable_phone = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_myaccount_phone, null);
        Drawable drawable_password = VectorDrawableCompat.create(getResources(),
                R.drawable.ic_myaccount_password_field, null);
        Drawable drawable_user_id = VectorDrawableCompat.create(getResources(),
                R.drawable.myaccount_address_userid, null);

//        ed_confirm_password.setCompoundDrawablesWithIntrinsicBounds(drawable_password, null, null, null);
//        ed_password.setCompoundDrawablesWithIntrinsicBounds(drawable_password, null, null, null);
        ed_phone.setCompoundDrawablesWithIntrinsicBounds(drawable_phone, null, null, null);
        ed_nickname.setCompoundDrawablesWithIntrinsicBounds(drawable_user_id, null, null, null);
    }

    private void initViews() {

//        ed_confirm_password = findViewById(R.id.ed_confirm_passwd);
//        ed_password = findViewById(R.id.ed_passwd);
        ed_nickname = findViewById(R.id.ed_nickname);
        ed_phone = findViewById(R.id.ed_phone);
        bt_register = findViewById(R.id.bt_register);

        ed_code = findViewById(R.id.ed_code);
        bt_send_verification_code = findViewById(R.id.bt_send_code);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        /* get a loading box on the top */
        if (loadingDialogFragment == null) {
            if (isLoading) {
                loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.content_on_loading));
                showFragment(loadingDialogFragment, "loadingbox", true);
            }
        } else {
            if (isLoading) {
                showFragment(loadingDialogFragment, "loadingbox", false);
            } else {
                /*remove fragment */
                hideFragment();
            }
        }
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
    public void registerSuccess(String phone_number, String password) {

        /* get the registration data, and fill it automatically inside the fields */
        /* go back to login activity with these informations */
        mToast(getResources().getString(R.string.register_successfull));
        clearRequetCodesAndTime();
        /* start activity for login */
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.PHONE_NUMBER, phone_number);
        intent.putExtra(PASSWORD, password);
        intent.putExtra(IS_REGISTERED, true);
        startActivity(intent);
        finish();
    }

    private void clearRequetCodesAndTime() {
        SharedPreferences pref = getSharedPreferences(Config.PRE_LOGIN_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        /* check if the code expired or not */
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void registerFailure(String message) {
    }*/

    @Override
    public void toast(String message) {
        mToast(message);
    }

    @Override
    public void onNetworkError() {
        mToast(getResources().getString(R.string.network_error));
    }

    @Override
    public void onSysError() {
        mToast(getResources().getString(R.string.concurrent_error));
    }

    @Override
    public void onSysError(String message) {
        mToast(message);
    }

    @Override
    public void keepRequestId(String phone_number, String request_id) {

        /* save req id in the activity */
        this.request_id = request_id;

        persistPhoneNumber(phone_number);
        persistRequestId(request_id);
        persistLastTimeCodeSent();
        initSendCodeBasics();
        /* start listening for the messages, in case there is a message as we know that comes.
         * get the message automatically and put it in the field. */
        /* save current phone number and request id. */
    }

    private String getRequestId() {
        SharedPreferences pref = getSharedPreferences(Config.PRE_LOGIN_SHARED_PREFS, MODE_PRIVATE);
        /* check if the code expired or not */
        return pref.getString(REQUEST_ID, "");
    }

    private void persistRequestId(String request_id) {
        SharedPreferences pref = getSharedPreferences(Config.PRE_LOGIN_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(REQUEST_ID, request_id);
        editor.commit();
    }

    private void persistPhoneNumber(String phone_number) {

        SharedPreferences pref = getSharedPreferences(Config.PRE_LOGIN_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PHONE_NUMBER, phone_number);
        editor.commit();
    }

    @Override
    public void disableCodeButton(final boolean isDisabled) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bt_send_verification_code.setEnabled(!isDisabled);
            }
        });
    }

    @Override
    public void codeIsOk(boolean isOk) {

        /* the confirmation code is ok  */
        if (isOk) {
            /* start code activty*/
            Intent intent = new Intent(this, ConfirmPayActivity.class);
            intent.putExtra(ConfirmPayActivity.TRANS_TYPE, ConfirmPayActivity.REGISTER);
            intent.putExtra(ConfirmPayActivity.STEP_COUNT, 1);
            /* give it request id and phonenumber, and nickname*/

            intent.putExtra(RegisterActivity.NICK_NAME, ed_nickname.getText().toString());
            intent.putExtra(RegisterActivity.PHONE_NUMBER, ed_phone.getText().toString());

            if (!"".equals(request_id)) {
                intent.putExtra(RegisterActivity.REQUEST_ID, request_id);
                startActivity(intent);
                finish();
            } else {
                mToast(getResources().getString(R.string.verification_code_expired));
            }
        } else {
            if (life == true) {
                mToast(getResources().getString(R.string.verification_code_wrong));
            } else {
                mToast(getResources().getString(R.string.verification_code_expired));
            }
        }
    }

    @Override
    public void userExistsAlready() {
        mToast(getResources().getString(R.string.user_already_exists));
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
            case R.id.bt_send_code:
                sendVerificationCode();
                break;
        }
    }


    private void sendVerificationCode() {

        if (!checkPhoneNumber(ed_phone.getText().toString()))
            return;

        /* launch a timer. */
        if (canISendCode()) {
            current_phone_number = ed_phone.getText().toString();
            registerPresenter.sendVerificationCode(current_phone_number);
        } else {
            mToast(getResources().getString(R.string.you_cant_send_verification_code));
        }
        /* we also need to make sure, the interval of time in which a request can be sent is 5min. */
        /*  use shared preference to ensure that things arent going to change when the activity is changing
         *   coming back. */
    }

    private boolean checkPhoneNumber(String phone_number) {

        boolean phoneNumber_tgo = UtilFunctions.isPhoneNumber_TGO(phone_number);

        if (phoneNumber_tgo) {
            return true;
        }
        mToast(getResources().getString(R.string.please_enter_a_local_number));
        return false;
    }

    private boolean canISendCode() {

        /* check shared preference, last time sent. and do the difference */
        if (bt_send_verification_code.getTag() != null && ((boolean) bt_send_verification_code.getTag()) == true)
            return true;
        else
            return false;
    }

    private void tryRegister() {

        /* retrieve all the usernames from the database and make every request instantly from local */
        String phone = ed_phone.getText().toString();
        String nickname = ed_nickname.getText().toString();
        String code = ed_code.getText().toString();

//        String password = ed_password.getText().toString();
        /* check all fields
         * 1- emptyness
         * 2- password at least 6 chars
         * 3- username --> at least 6chars
         * 4- phonenumber must follow a regex
         * */

        if (!UtilFunctions.isPhoneNumber_TGO(phone)) {
            mToast(getResources().getString(R.string.field_phone_number_error));
            return;
        }

        /* check if phone_number is conform to the phone number that sent the request_id */
        if (!(getRequestIdPhoneNumber().equals("") || getRequestIdPhoneNumber().equals(phone))) {
            mToast(getResources().getString(R.string.make_sure_phone_is_similar));
            return;
        }

        if ("".equals(request_id)) {
            /* send the code again */
            mToast(getResources().getString(R.string.verification_code_expired));
            return;
        }

        /* verification de verification code */
        registerPresenter.check_verification_code(code, request_id);
    }

    private String getRequestIdPhoneNumber() {

        /* check if the save has not expired before doing so ... */
        long diff = getNowTime() - getLastTimeCodeSent();
        if (diff > MIN_ELLAPSE_TIME) {
            /* return string phone saved in sp */
            SharedPreferences pref = getSharedPreferences(Config.PRE_LOGIN_SHARED_PREFS, MODE_PRIVATE);
            return pref.getString(PHONE_NUMBER, "");
        }

        return "";
    }


    private boolean checkPassword(String password) {

        if (password.length() >= PASSWORD_MIN_LENGTH) {
            return true;
        }
        return false;
    }

    public long getLastTimeCodeSent() {
        SharedPreferences pref = getSharedPreferences(Config.PRE_LOGIN_SHARED_PREFS, MODE_PRIVATE);
        return pref.getLong(LAST_TIME_CODE_SEND, 0);
    }

    public void persistLastTimeCodeSent() {
        SharedPreferences pref = getSharedPreferences(Config.PRE_LOGIN_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(LAST_TIME_CODE_SEND, getNowTime());
        editor.commit();
    }

    private long getNowTime() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.getTime();
    }


    class CodeDiscountThread extends Thread implements Runnable {

        @Override
        public void run() {
            super.run();
            /* each second, we-self-call and check the time difference. */
            while (life) {
                try {
                    sleep(1000);
                    long diff = getNowTime() - getLastTimeCodeSent();
                    if (diff > MIN_ELLAPSE_TIME) {
                        /* set back text, and kill me */
                        resetGetCodeButton();
                    } else {
                        codeButtonSetRemainingTime(MIN_ELLAPSE_TIME - (getNowTime() - getLastTimeCodeSent()));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void codeButtonSetRemainingTime(final long time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bt_send_verification_code.setText(convertSecondsToTime(time));
            }
        });
    }

    private String convertSecondsToTime(long time) {

        long seconds = time/1000;
        long min = seconds / 60;
        seconds = seconds%60;
        String tm = "" + min + "min " + seconds + getResources().getString(R.string.seconds);
        return tm;
    }

    private void resetGetCodeButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                life = false;
                bt_send_verification_code.setText(getResources().getString(R.string.ed_code));
                bt_send_verification_code.setTag(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        br = new RegisterSmsBroadcastListener();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        life = false;
        if (br != null)
            unregisterReceiver(br);
    }

    @Override
    protected void onPause() {
        super.onPause();
        life = false;
    }

    public class RegisterSmsBroadcastListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            String msgBody = msgs[i].getMessageBody();
                            if (msgBody != null) {
                                String code = msgBody.split(":")[1].split("\\.")[0].trim();
                                ed_code.setText(code);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
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
        life = false;
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }

}