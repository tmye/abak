package tg.tmye.kaba.activity.UserAcc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.activity.UserAcc.service_client.contract.ServiceClientContract;
import tg.tmye.kaba.activity.UserAcc.service_client.presenter.ServiceClientPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;


public class ServiceClientActivity extends AppCompatActivity implements View.OnClickListener, ServiceClientContract.View {

    private FrameLayout lny_dial;
    private Button bt_confirm_suggestion;
    private EditText /*ed_client_name, */ ed_client_message;


    CustomerDataRepository repository;
    ServiceClientPresenter presenter;


    private LoadingDialogFragment loadingDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        lny_dial = findViewById(R.id.lny_dial);
        bt_confirm_suggestion = findViewById(R.id.bt_confirm_suggestion);
//        ed_client_name = findViewById(R.id.ed_client_name);
        ed_client_message = findViewById(R.id.ed_client_message);

        lny_dial.setOnClickListener(this);
        bt_confirm_suggestion.setOnClickListener(this);

        repository = new CustomerDataRepository(this);
        presenter = new ServiceClientPresenter(this, repository);
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
            case R.id.lny_dial:
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.CLIENT_SERVICE));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
                break;
            case R.id.bt_confirm_suggestion:
                confirmSuggestion();
                break;
        }
    }

    private void confirmSuggestion() {

        /* check the fields data */
//        String name = ed_client_name.getText().toString();
        String message = ed_client_message.getText().toString();
        presenter.postSuggestion(message);
    }

    @Override
    public void isSuccess(final boolean isSuccess) {
        /* do what you can */

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showLoading(false);
                if (isSuccess) {
                    mToast(getResources().getString(R.string.suggestion_success));
                    // exit after 2 seconds.
                    ed_client_message.setText("");
                } else {
                    mToast(getResources().getString(R.string.suggestion_failure));
                }
                ed_client_message.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    }
                }, 1500);

            }
        });
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    private void showFragment(LoadingDialogFragment loadingDialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(loadingDialogFragment, tag);
        else
            ft.show(loadingDialogFragment);
        ft.commitAllowingStateLoss();
    }


    @Override
    public void setPresenter(ServiceClientContract.Presenter presenter) {

    }

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }


}
