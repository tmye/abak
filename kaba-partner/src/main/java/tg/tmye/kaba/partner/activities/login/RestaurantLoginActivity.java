package tg.tmye.kaba.partner.activities.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import tg.tmye.kaba.partner.activities.home.HomeActivity;
import tg.tmye.kaba.partner.activities.login.contract.LoginContract;
import tg.tmye.kaba.partner.activities.login.presenter.LoginPresenter;
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.partner.data.Restaurant.source.RestaurantOnlineRepository;
import tg.tmye.kaba.partner.syscore.MyRestaurantApp;
import tg.tmye.kaba.partner.R;

public class RestaurantLoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    Button bt_login;
    EditText ed_username, ed_password;
    LoginPresenter presenter;
    private RestaurantOnlineRepository restaurantRepo;

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_login);

        /* check if logged in -
         *   - if not,then go login interface
         *   - if yes, go to principal activity. */
        checkLogin ();

        initViews();
        restaurantRepo = new RestaurantOnlineRepository(this);
        presenter = new LoginPresenter(this, restaurantRepo);
        bt_login.setOnClickListener(this);
    }

    private void checkLogin() {

        String authTOken = ((MyRestaurantApp)getApplicationContext()).getAuthToken();
        if (!"".equals(authTOken)) {
            Intent intent = new Intent(this, HomeActivity.class);
            Bundle dead_notification_extras = getIntent().getExtras();
            if (dead_notification_extras != null) {
                intent.putExtras(dead_notification_extras);
            }
            startActivity(intent);
            finish();
        }
    }

    private void initViews() {
        bt_login = findViewById(R.id.bt_login);
        ed_password = findViewById(R.id.ed_password);
        ed_username = findViewById(R.id.ed_username);


//        ed_username.setText("vadout");
//        ed_password.setText("123456");
    }

    @Override
    public void loginSuccess(boolean isSuccess) {
        if (isSuccess) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void networkError() {
        mToast(getResources().getString(R.string.network_error));
    }

    @Override
    public void sysError() {
        mToast(getResources().getString(R.string.sys_error));
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
        presenter.login(username, password);
    }
}
