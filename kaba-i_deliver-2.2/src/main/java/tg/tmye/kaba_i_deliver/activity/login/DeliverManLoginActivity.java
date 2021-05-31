package tg.tmye.kaba_i_deliver.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.command.MyCommandsActivity;
import tg.tmye.kaba_i_deliver.activity.home.HomeActivity;
import tg.tmye.kaba_i_deliver.activity.login.contract.LoginContract;
import tg.tmye.kaba_i_deliver.activity.login.presenter.LoginPresenter;
import tg.tmye.kaba_i_deliver.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

public class DeliverManLoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    Button bt_login;
    EditText ed_username, ed_password;
    LoginPresenter presenter;
    private DeliveryManRepository deliveryManRepository;

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverman_login);

        /* check if logged in -
         *   - if not,then go login interface
         *   - if yes, go to principal activity. */
        checkLogin ();

        initViews();
        deliveryManRepository = new DeliveryManRepository(this);
        presenter = new LoginPresenter(this, deliveryManRepository);
        bt_login.setOnClickListener(this);
    }

    private void checkLogin() {

        String authTOken = ((MyKabaDeliverApp)getApplicationContext()).getAuthToken();
        if (!"".equals(authTOken)) {
            startActivity(new Intent(this, MyCommandsActivity.class));
            finish();
        }
    }

    private void initViews() {
        bt_login = findViewById(R.id.bt_login);
        ed_password = findViewById(R.id.ed_password);
        ed_username = findViewById(R.id.ed_username);

//        ed_username.setText("kastas002");
//        ed_password.setText("123456");
    }

    @Override
    public void loginSuccess(boolean isSuccess) {
        if (isSuccess) {
            startActivity(new Intent(this, MyCommandsActivity.class));
            finish();
        }
    }

    @Override
    public void networkError() {
        mToast(getResources().getString(R.string.network_error));
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                showFragment(loadingDialogFragment, "loadingbox",false);
            } else {
                /*remove fragment */
                hideFragment();
            }
        }
        bt_login.setEnabled(!isLoading);
    }

    @Override
    public void sysError(int errorCode, String message) {

        mToast(""+errorCode+" - "+message);
        showLoading(false);
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
        }
    }


    private void login() {
        String username = ed_username.getText().toString();
        String password = ed_password.getText().toString();

        if ("".equals(username)) {
            mToast(getResources().getString(R.string.this_field_shouldnt_empty));
            ed_username.requestFocus();
        return;
        }

        if ("".equals(password)) {
            mToast(getResources().getString(R.string.this_field_shouldnt_empty));
            ed_password.requestFocus();
            return;
        }

        presenter.login(username, password);
    }
}
