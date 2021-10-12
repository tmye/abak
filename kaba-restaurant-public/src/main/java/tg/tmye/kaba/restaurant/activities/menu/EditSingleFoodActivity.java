package tg.tmye.kaba.restaurant.activities.menu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.GenericTransitionOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;
import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant._commons.utils.UtilFunctions;
import tg.tmye.kaba.restaurant.activities.menu.contract.EditSingleFoodContract;
import tg.tmye.kaba.restaurant.activities.menu.presenter.EditSingleFoodPresenter;
import tg.tmye.kaba.restaurant.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.restaurant.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.restaurant.data.Restaurant.source.RestaurantOnlineRepository;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.GlideApp;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;


public class EditSingleFoodActivity extends AppCompatActivity implements EditSingleFoodContract.View {

    EditText ed_food_name, ed_food_description;
    EditText ed_normal_price, ed_promotion_price;
    CheckBox checkBox_promotion, checkBox_active;
    Button bt_cancel, bt_confirm, bt_pick;
    ImageView iv_placeholder;
    private Toolbar toolbar;

    Restaurant_Menu_FoodEntity food;

    EditSingleFoodPresenter presenter;

    private MenuDb_OnlineRepository repository;

    private LoadingDialogFragment loadingDialogFragment;

    private int menu_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_food);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        food = getIntent().getParcelableExtra("food");
        menu_id = getIntent().getIntExtra("menu_id", -1);

        if (menu_id == -1) {

        mToast("Menu cannot be null");
        finish();
        }

        if (food == null) {
            food = new Restaurant_Menu_FoodEntity();
            food.menu_id = menu_id;
            toolbar.setTitle(getString(R.string.create_food));
        } else {
            toolbar.setTitle(food.name);
        }

        ed_food_name = findViewById(R.id.ed_food_name);
        ed_food_description = findViewById(R.id.ed_food_description);
        ed_normal_price = findViewById(R.id.ed_food_price);
        ed_promotion_price = findViewById(R.id.ed_food_promotion_price);
        iv_placeholder = findViewById(R.id.iv_placeholder);

        checkBox_active = findViewById(R.id.checkbox_food_active);
        checkBox_promotion = findViewById(R.id.checkbox_food_promotion);

        // add a listener on it
        bt_confirm = findViewById(R.id.bt_confirm);
        bt_cancel = findViewById(R.id.bt_cancel);
        bt_pick = findViewById(R.id.bt_pick);

        //inflate data
        ed_food_name.setText(food.name);
        ed_food_description.setText(food.description);
        ed_normal_price.setText(food.price);
        ed_promotion_price.setText(food.promotion_price);

        // inflate image if exists
        if (food.pic != null && food.pic.length() < 100)
            GlideApp.with(this)
                    .load(Constant.SERVER_ADDRESS + "/" + food.pic)
                    .transition(GenericTransitionOptions.with(((MyRestaurantApp) getApplicationContext()).getGlideAnimation()))
                    .centerCrop()
                    .into(iv_placeholder);


//        checkBox_active.setChecked(food.is_hidden == 0);

        checkBox_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    checkBox_active.setText(R.string.yes);
                else
                    checkBox_active.setText(R.string.no);
            }
        });

        checkBox_active.setChecked(food.is_hidden == 1);
        checkBox_active.setText(food.is_hidden == 0 ? R.string.no : R.string.yes);

        checkBox_promotion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    checkBox_promotion.setText(R.string.yes);
                else
                    checkBox_promotion.setText(R.string.no);
            }
        });
        checkBox_promotion.setChecked(food.promotion == 1);
        checkBox_promotion.setText(food.promotion == 1 ? R.string.yes : R.string.no);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* pick photo and send back the link */
                pickPhoto();
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
        presenter = new EditSingleFoodPresenter(repository, this);
    }

    private void _launchAndConfirm() {

        String name = ed_food_name.getText().toString();
        String description = ed_food_description.getText().toString();
        String price = ed_normal_price.getText().toString();
        String promotion_price = ed_promotion_price.getText().toString();
        int is_hidden = (!checkBox_active.isChecked() ? 0 : 1);
        int is_promotion_active = (checkBox_promotion.isChecked() ? 1 : 0);

        food.name = name;
        food.description = description;
        food.price = price;
        food.promotion_price = promotion_price;
        food.is_hidden = is_hidden;
        food.promotion = is_promotion_active;

        if (food.pic == null) {
            // we send a default picture here
            // we change a ressource to b64
            //encode image to base64 string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.food_default);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            food.pic = imageString;
        }

        presenter.updateFood(food);
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
    public void foodUpdateSuccess() {
        // finish and update in menu
        // go back and update food element
        finish();
    }

    @Override
    public void foodUpdateError() {
        mToast("Please try again, there a system error");
    }

    @Override
    public void setPresenter(EditSingleFoodContract.Presenter presenter) {
    }

    private void pickPhoto() {

        /* check if carema and storage rights */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
            return;
        }

        /* u cant crop picture here for now */
        FilePickerBuilder.getInstance().setMaxCount(1)
//                .setSelectedFiles(null)
                .setActivityTheme(R.style.AppTheme_NoActionBar)
                .setCameraPlaceholder(R.drawable.ic_photo_camera_white_24dp)
                .pickPhoto(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
            bt_pick.performClick();
        } else {
            mToast(getResources().getString(R.string.failed_in_allowing_permission));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* on activity result */
        Log.d(Constant.APP_TAG, "onactivity result");
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    List<Uri> images = new ArrayList<>();
//                    images.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    images.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    /* set image */
                    try {
                        if (images != null && images.size() > 0) {
                            GlideApp.with(this)
                                    .load(ContentUriUtils.INSTANCE.getFilePath(this, images.get(0)))
                                    .transition(GenericTransitionOptions.with(((MyRestaurantApp) getApplicationContext()).getGlideAnimation()))
                                    .centerCrop()
                                    .into(iv_placeholder);

                            food.pic = UtilFunctions.changeAndRotatePathToBase64(ContentUriUtils.INSTANCE.getFilePath(this, images.get(0)));

                            /*this is base64, check if we have to return it or not */
//                        activityResultSuccess = true;
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        mToast(getString(R.string.image_picking_error));
                    }
                }
                break;
        }
    }

}
