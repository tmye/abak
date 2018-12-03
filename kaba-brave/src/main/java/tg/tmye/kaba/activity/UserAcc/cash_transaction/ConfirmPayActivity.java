package tg.tmye.kaba.activity.UserAcc.cash_transaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAuth.login.LoginActivity;
import tg.tmye.kaba.activity.UserAuth.register.RegisterActivity;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.config.Config;

public class ConfirmPayActivity extends AppCompatActivity implements View.OnClickListener {


    public final static int REGISTER = 0, LOGIN = 11, PAY_COMMAND = 12;

    /* keybaord should never appear in this activity */
    public final static String STEP_COUNT = "STEP_COUNT", TRANS_TYPE = "TRANS_TYPE";
    public static final String TRANS_AMOUNT = "TRANS_AMOUNT";
    public static final String PASSWORD = "PASSWORD";
    public static final String RESET_PASSWORD = "RESET_PASSWORD";

    EditText ed_1, ed_2, ed_3, ed_4;

    Button bt_1, bt_2, bt_3, bt_4, bt_5, bt_6, bt_7, bt_8, bt_9, bt_0, bt_delete;

    LinearLayout lny_transaction_libelle;

    TextView tv_action_libelle, tv_trans_amount;


    int step_count, type;
    String phonecode;


    private String nickname;
    private String phone_number;
    private String request_id;
    private String amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_toolbar_cross_24dp);

        initViews();

        ed_1.setFocusable(false);
        ed_2.setFocusable(false);
        ed_3.setFocusable(false);
        ed_4.setFocusable(false);

        /* keyboard listener */
        bt_0.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
        bt_5.setOnClickListener(this);
        bt_6.setOnClickListener(this);
        bt_7.setOnClickListener(this);
        bt_8.setOnClickListener(this);
        bt_9.setOnClickListener(this);
        bt_delete.setOnClickListener(this);


        phonecode = getIntent().getStringExtra(LoginActivity.PHONE_NUMBER);

        /* depending on different transactions
         * - login (need phone_number and password) after this - launch login -- step 2 (one here)
         * - register (need phone_number, password, nickname, confirmation_password -- step3 (two here)
         * - buy transaction, need password and command content and list of foods and address (step 1) one here
         * */

        step_count = getIntent().getIntExtra(STEP_COUNT, -1);
        type = getIntent().getIntExtra(TRANS_TYPE, -1);

        lny_transaction_libelle.setVisibility(View.INVISIBLE);
        tv_action_libelle.setVisibility(View.VISIBLE);

        switch (type) {
            case REGISTER:

                /* message will change */

                /* check step count -- later */
                if (step_count == 1) {
                    /* get mdp */
                    tv_action_libelle.setText(getResources().getString(R.string.create_register_password));
                } else if (step_count == 2) {
                    /* confirm mdp */
                    tv_action_libelle.setText(getResources().getString(R.string.confirm_register_password));
                }

                /* just in case */
                nickname = getIntent().getStringExtra(RegisterActivity.NICK_NAME);
                phone_number = getIntent().getStringExtra(RegisterActivity.PHONE_NUMBER);
                request_id = getIntent().getStringExtra(RegisterActivity.REQUEST_ID);


                break;
            case LOGIN:
                /* check step, and go back to login */
                if (step_count == 1) {
                    /* get mdp and return */
                    tv_action_libelle.setText(getResources().getString(R.string.input_login_password));
                }
                break;
            case PAY_COMMAND:

                amount = getIntent().getStringExtra(ConfirmPayActivity.TRANS_AMOUNT);
                lny_transaction_libelle.setVisibility(View.VISIBLE);
                tv_trans_amount.setText(UtilFunctions.intToMoney(amount)+" F");

                if ("".equals(amount) || amount == null) {
                    finish();
                    return;
                }
                /* check pay command */
                /* get mdp and process command */
                if (step_count == 1) {
                    tv_action_libelle.setText(getResources().getString(R.string.input_transaction_password));
                }
                break;
        }

//        mToast("phonecode is "+phonecode);
//        mToast("step_count is "+step_count);
//        mToast("trans_type is "+type);
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        ed_1 = findViewById(R.id.ed_field_1);
        ed_2 = findViewById(R.id.ed_field_2);
        ed_3 = findViewById(R.id.ed_field_3);
        ed_4 = findViewById(R.id.ed_field_4);

        bt_0 = findViewById(R.id.bt_0);
        bt_1 = findViewById(R.id.bt_1);
        bt_2 = findViewById(R.id.bt_2);
        bt_3 = findViewById(R.id.bt_3);
        bt_4 = findViewById(R.id.bt_4);
        bt_5 = findViewById(R.id.bt_5);
        bt_6 = findViewById(R.id.bt_6);
        bt_7 = findViewById(R.id.bt_7);
        bt_8 = findViewById(R.id.bt_8);
        bt_9 = findViewById(R.id.bt_9);
        bt_delete = findViewById(R.id.bt_delete);

        lny_transaction_libelle = findViewById(R.id.lny_transaction_libelle);
        tv_trans_amount = findViewById(R.id.tv_trans_amount);
        tv_action_libelle = findViewById(R.id.tv_action_libelle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            String value = (String) view.getTag();
            if (!"".equals(value) && value.length()==1) {
                int val = Integer.valueOf(value);
                /* des valeures, and we add it */
                if (val >= 0 && val <= 9) {
                    addCode(value);
                }
            } else if ("delete".equals(value)) {
                removeCode();
            }
        }
    }

    private void removeCode() {

        /* check the actuall position */
        if (!"".equals(ed_4.getText().toString())) {
            /* remove it */
            ed_4.setText("");
            return;
        }
        if (!"".equals(ed_3.getText().toString())) {
            /* remove it */
            ed_3.setText("");
            return;
        }
        if (!"".equals(ed_2.getText().toString())) {
            /* remove it */
            ed_2.setText("");
            return;
        }
        if (!"".equals(ed_1.getText().toString())) {
            /* remove it */
            ed_1.setText("");
        }
    }

    private void addCode(String code_item) {
        /* check the actuall position */
        if ("".equals(ed_1.getText().toString())) {
            /* remove it */
            ed_1.setText(code_item);
            return;
        }
        if ("".equals(ed_2.getText().toString())) {
            /* remove it */
            ed_2.setText(code_item);
            return;
        }
        if ("".equals(ed_3.getText().toString())) {
            /* remove it */
            ed_3.setText(code_item);
            return;
        }
        if ("".equals(ed_4.getText().toString())) {
            /* remove it */
            ed_4.setText(code_item);
            /* validate the transaction */
            validateTransaction();
        }
    }

    private void validateTransaction() {

        String mdp = ed_1.getText().toString() +
                ed_2.getText().toString() +
                ed_3.getText().toString() +
                ed_4.getText().toString();

        /* send back to login interface. */
        if (step_count == 1 && type == LOGIN) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.PHONE_NUMBER, phonecode);
            intent.putExtra(LoginActivity.PASSWORD, mdp);
            intent.putExtra(LoginActivity.IS_REGISTERED, true); /* we know what we are doing. */
            startActivity(intent);
            finish();
        }

        if (step_count == 1 && type == REGISTER) {

            /* jump and get confirmation again */
            Intent intent = new Intent(this, ConfirmPayActivity.class);
            intent.putExtra(ConfirmPayActivity.TRANS_TYPE, ConfirmPayActivity.REGISTER);
            intent.putExtra(ConfirmPayActivity.STEP_COUNT, 2);
            /* give it request id and phonenumber, and nickname*/

            intent.putExtra(RegisterActivity.NICK_NAME, nickname);
            intent.putExtra(RegisterActivity.PHONE_NUMBER, phone_number);
            intent.putExtra(RegisterActivity.PWD_1, mdp);

            startActivity(intent);
            finish();
        }

        if (step_count == 2 && type == REGISTER) {

            String first_pwd = getIntent().getStringExtra(RegisterActivity.PWD_1);
            String request_id = getIntent().getStringExtra(RegisterActivity.REQUEST_ID);
            /* pass request_id through activities s*/

            if (mdp.equals(first_pwd)) {

                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(RegisterActivity.PHONE_NUMBER, phone_number);
                intent.putExtra(RegisterActivity.NICK_NAME, nickname);
                intent.putExtra(RegisterActivity.PASSWORD, mdp);
                intent.putExtra(RegisterActivity.REQUEST_ID, request_id);
                intent.putExtra(LoginActivity.IS_REGISTERED, true);
                startActivity(intent);
                finish();
            } else {

                mToast(getResources().getString(R.string.password_confirmation_wrong), Toast.LENGTH_LONG);
                /* get password again */
                Intent intent = new Intent(this, ConfirmPayActivity.class);
                intent.putExtra(ConfirmPayActivity.TRANS_TYPE, ConfirmPayActivity.REGISTER);
                intent.putExtra(ConfirmPayActivity.STEP_COUNT, 2);
                /* give it request id and phonenumber, and nickname*/
                intent.putExtra(RegisterActivity.NICK_NAME, nickname);
                intent.putExtra(RegisterActivity.PHONE_NUMBER, phone_number);
                intent.putExtra(RegisterActivity.PWD_1, mdp);
                intent.putExtra(RegisterActivity.REQUEST_ID, request_id);
                startActivity(intent);
                finish();
            }
        }

        if (step_count == 1 && type == PAY_COMMAND) {

            /* send back as password get result */
            Intent dataIntent = new Intent();
            dataIntent.putExtra(ConfirmPayActivity.PASSWORD, mdp);
            setResult(Activity.RESULT_OK, dataIntent);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void mToast(String message, int lengthLong) {
        Toast.makeText(this, message, lengthLong).show();
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
