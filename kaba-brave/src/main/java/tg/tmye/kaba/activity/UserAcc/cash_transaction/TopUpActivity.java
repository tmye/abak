package tg.tmye.kaba.activity.UserAcc.cash_transaction;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.InfoDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.contract.SoldeContract;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.contract.TopUpContract;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.presenter.TopUpPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.transaction.OperatorInfo;
import tg.tmye.kaba.data.transaction.source.OperatorInfoRepository;

public class TopUpActivity extends AppCompatActivity implements TopUpContract.View, TextWatcher, View.OnClickListener {

    TopUpPresenter presenter;
    OperatorInfoRepository repository;

//    LinearLayoutCompat lny_topup, lny_radiobuttons;

    private LoadingDialogFragment loadingDialogFragment;

    EditText ed_phone_number, ed_amount;

    Button bt_top_up;

    TextView tv_top_up_mode;

    private InfoDialogFragment floozInfoDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        initViews();

        repository = new OperatorInfoRepository(this);
        presenter = new TopUpPresenter(this, repository);

//        presenter.checkAvailableOperator();
        ed_phone_number.addTextChangedListener(this);
        bt_top_up.setOnClickListener(this);
    }

    private void initViews() {
        ed_phone_number = findViewById(R.id.ed_phone_number);
        ed_amount = findViewById(R.id.ed_amount);
        bt_top_up = findViewById(R.id.bt_top_up);
        tv_top_up_mode = findViewById(R.id.tv_top_up_mode);
    }


    private Drawable getResourceWithMarging(Drawable resource, RadioButton radioButton) {
        final int iconInsetPadding = getResources().getDimensionPixelSize(R.dimen.icon_padding);
        final Drawable insetDrawable = new InsetDrawable(resource, iconInsetPadding,iconInsetPadding,iconInsetPadding,iconInsetPadding);
        return insetDrawable;
    }

    @Override
    public void onSysError() {
        String message = "";
//        mStickyErrorRetrySnack(message);
    }

    @Override
    public void onNetworkError() {
        String message = "";
//        mStickyErrorRetrySnack(message);
    }


    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

               /* if (isLoading)
                    lny_topup.setVisibility(View.GONE);
                else
                    lny_topup.setVisibility(View.VISIBLE);
*/
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
    public void mToast(String jsonResponse) {
//        Toast.makeText(this, jsonResponse, Toast.LENGTH_LONG).show();
    }




    /* tmoney success - redirect on the webpage - */
    @Override
    public void onTopUpLaunchSuccess(String link) {
        /* show a message that brings out of the app */
        /* - start listening to anymessage with *155*27# inside of it. */
        /* launch a service that listens to all mails that come in the row */
        Log.d(Constant.APP_TAG, link);
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//        startActivity(browserIntent);
        Intent browserIntent = new Intent(this, TopUpWebActivity.class);
        browserIntent.putExtra(TopUpWebActivity.DATA, link);
        startActivity(browserIntent);
        finish();
    }


    /* flooz success listen for the sms */
    @Override
    public void onTopUpLaunchSuccess() {

        /* show a dialog box informing of what is next - sms and message */
        floozInfoDialogFragment = InfoDialogFragment.newInstance(R.drawable.ic_attach_money_colorprimary_24dp, getResources().getString(R.string.flooz_success), 0);
        floozInfoDialogFragment.show(getSupportFragmentManager(), "loadingbox");
    }

    @Override
    public void launchTMoneyTopUp(String phone_number, String amount) {

        /* launch data up to */
        presenter.launchTMoneyTopUp(phone_number, amount);
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
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }

    @Override
    public void setPresenter(SoldeContract.Presenter presenter) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkUpInputContent() {

        String phone_number = ed_phone_number.getText().toString();

        String type = "";

        disactivate (bt_top_up);
        disactivateTv (tv_top_up_mode);

        /* check if the number is togolese number and moov / tgcel */
        if (UtilFunctions.isPhoneNumber_Moov(phone_number)) {
            type = OperatorInfo.FLOOZ;
            activate (bt_top_up, type);
            activateTv (tv_top_up_mode, type);
        }
        if (UtilFunctions.isPhoneNumber_Tgcel(phone_number)) {
            type = OperatorInfo.TMONEY;
            activateTv (tv_top_up_mode,type );
            activate (bt_top_up, type);
        }

        /* according to the value, work on the radiobuttons */

        return true;
    }

    private void disactivateTv(TextView tv_top_up_mode) {
        if ("-----".equals(tv_top_up_mode.getText().toString())) {
            return;
        }
        tv_top_up_mode.setText("-----");
    }

    private void activateTv(TextView tv_top_up_mode, String type) {

        bt_top_up.setVisibility(View.INVISIBLE);
        switch (type) {
            case OperatorInfo.FLOOZ:
                if ("FLOOZ".equals(tv_top_up_mode.getText().toString())) {
                    return;
                }
                tv_top_up_mode.setText("FLOOZ");
                bt_top_up.setVisibility(View.VISIBLE);
                break;
            case OperatorInfo.TMONEY:
                if ("T-MONEY".equals(tv_top_up_mode.getText().toString())) {
                    return;
                }
                tv_top_up_mode.setText("T-MONEY");
                bt_top_up.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void activate(Button bt_top_up, String type) {

        bt_top_up.setOnClickListener(this);
        if (bt_top_up.isActivated())
            return ;
        bt_top_up.setBackgroundResource(R.drawable.bt_primary_red_rounded);
        bt_top_up.setActivated(true);
        bt_top_up.setTag(type);
    }

    private void disactivate(Button bt_top_up) {
        bt_top_up.setOnClickListener(null);
        if (!bt_top_up.isActivated())
            return;
        bt_top_up.setBackgroundResource(R.drawable.bt_gray_rounded);
        bt_top_up.setActivated(false);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        checkUpInputContent();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_top_up:
                /*  */
                String phone_number = ed_phone_number.getText().toString();
                String amount = ed_amount.getText().toString();

                if (Integer.valueOf(amount) <= 0) {
                    mToast(getResources().getString(R.string.amount_is_wrong));
                    return;
                }

                String tag = (String) bt_top_up.getTag();
                if (tag != null) {
                    switch (tag) {
                        case OperatorInfo.FLOOZ:
                            presenter.launchFloozTopUp(phone_number, amount);
                            break;
                        case OperatorInfo.TMONEY:
                            presenter.launchTMoneyTopUp(phone_number, amount);
                            break;
                    }
                }
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

}
