package tg.tmye.kaba.activity.UserAcc.cash_transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.contract.SoldeContract;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.presenter.SoldePresenter;
import tg.tmye.kaba.data.customer.source.SoldeRepository;


public class SoldeActivity extends AppCompatActivity implements View.OnClickListener, SoldeContract.View {

    FloatingActionButton fab_historic, fab_top_up, fab_transfert;

    SoldePresenter presenter;
    SoldeRepository repository;

    TextView tv_balance;

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solde);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_historic = findViewById(R.id.fab_historic);
        fab_top_up = findViewById(R.id.fab_top_up);
        fab_transfert = findViewById(R.id.fab_transfert);

        tv_balance = findViewById(R.id.tv_balance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        fab_historic.setOnClickListener(this);
        fab_top_up.setOnClickListener(this);
        fab_transfert.setOnClickListener(this);

        repository = new SoldeRepository(this);
        presenter = new SoldePresenter(this, repository);

        presenter.checkSolde();
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
        } else if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_historic:
                mToast ("fab historic");
                launchHistoryTransactionBox();
                break;
            case R.id.fab_top_up:
                mToast ("fab top up");
                launchTopUp();
                break;
            case R.id.fab_transfert:
//                mToast ("fab transfer");
                break;
            case R.id.bt_tryagain:
//                presenter.
                finish();
                break;
        }
    }

    private void launchHistoryTransactionBox() {
        Intent intent = new Intent(this, TransactionHistoryActivity.class);
        startActivity(intent);
    }

    private void launchTopUp() {
        Intent intent = new Intent(this, TopUpActivity.class);
        startActivity(intent);
    }


    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inflateSolde(final String solde) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UtilFunctions.StoreBalance(SoldeActivity.this, solde);
                tv_balance.setText("XOF "+ UtilFunctions.intToMoney(solde));
            }
        });
    }

    @Override
    public void onSysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mToast(getResources().getString(R.string.sys_error));
            }
        });
    }

    @Override
    public void onNetworkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mToast(getResources().getString(R.string.network_error));
            }
        });
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
    public void setPresenter(SoldeContract.Presenter presenter) {}

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }
}
