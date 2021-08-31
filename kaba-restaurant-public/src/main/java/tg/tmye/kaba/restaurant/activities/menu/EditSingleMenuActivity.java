package tg.tmye.kaba.restaurant.activities.menu;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.activities.menu.contract.EditSingleMenuContract;
import tg.tmye.kaba.restaurant.activities.menu.presenter.EditSingleMenuPresenter;
import tg.tmye.kaba.restaurant.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.restaurant.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.restaurant.data.Restaurant.source.RestaurantOnlineRepository;

public class EditSingleMenuActivity extends AppCompatActivity implements EditSingleMenuContract.View {

    EditText ed_menu_name, ed_priority;
    CheckBox checkBox;
    Button bt_cancel, bt_confirm;
    private Toolbar toolbar;

    /* use a repository and a presenter to update data and leave if ok */
    EditSingleMenuPresenter presenter;
    MenuDb_OnlineRepository repository;

    //
    Restaurant_SubMenuEntity menu;

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_menu);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        setTitle("Modify Menu");

        menu = getIntent().getParcelableExtra("menu");

        toolbar.setTitle(menu.name);

        ed_menu_name = findViewById(R.id.ed_menu_name);
        checkBox = findViewById(R.id.checkbox_menu_active);
        ed_priority = findViewById(R.id.ed_priority);

        // add a listener on it
        bt_confirm = findViewById(R.id.bt_confirm);
        bt_cancel = findViewById(R.id.bt_cancel);

        //inflate data
        ed_menu_name.setText(menu.name);

        checkBox.setChecked(menu.is_hidden == 1);
        checkBox.setText(menu.is_hidden == 1 ? "NO" : "YES");

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    checkBox.setText("YES");
                else
                    checkBox.setText("NO");
            }
        });

        checkBox.setChecked(menu.is_hidden == 1);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch a presenter and upload this.
                _launchAndConfirm();
            }
        });

        RestaurantOnlineRepository restaurantOnlineRepository = new RestaurantOnlineRepository(this);
        RestaurantEntity resto = restaurantOnlineRepository.getRestaurant();

        repository = new MenuDb_OnlineRepository(this, resto);
        presenter = new EditSingleMenuPresenter(repository, this);
    }

    private void _launchAndConfirm() {
        String newMenuName = ed_menu_name.getText().toString();
        String priority = ed_priority.getText().toString();
        boolean isActive = checkBox.isChecked();

        /* check all before uploading in the app of course */

        // control the fields before
        menu.name = newMenuName;
        menu.priority = priority;
        menu.is_hidden = (isActive ? 0 : 1);

        presenter.updateMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void showIsLoading(final boolean isLoading) {

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

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSysError() {
        mToast(getResources().getString(R.string.sys_error));
    }

    @Override
    public void onNetworkError() {
        mToast(getResources().getString(R.string.network_error));
    }

    @Override
    public void menuUpdateSuccess() {
       // finish and update-in-menu
        finish();
    }

    @Override
    public void menuUpdateError() {
        mToast("Please try again, there a system error");
    }

    @Override
    public void setPresenter(EditSingleMenuContract.Presenter presenter) {

    }
}
