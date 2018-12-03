package tg.tmye.kaba.activity.UserAcc.personnalinfo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.UserAcc.personnalinfo.contract.PersonnalInfoContract;
import tg.tmye.kaba.activity.UserAcc.personnalinfo.presenter.PersonnalInfoPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


public class PersonnalInfoActivity extends AppCompatActivity implements
        View.OnClickListener, PersonnalInfoContract.View {

    public static final int CONTENT_MODIFIED = 901;
    private List<String> images;


    private FloatingActionButton take_pic_fab;
    public static TextView tv_birth_date;
    private ImageView photoImageView;

    private TextView tv_phonenumber, tv_nickname;

    private RadioButton rd_gender_female;
    private RadioButton rd_gender_male;

    private RadioGroup rd_gender_group;

    /* presenter */
    private PersonnalInfoContract.Presenter presenter;
    private CustomerDataRepository customerDataRepo;

    /* has gone through result */
    private boolean activityResultSuccess = false;
    private Customer customer;
    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnal_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        initViews();

        bindViews();

        tv_birth_date.setOnClickListener(this);
        take_pic_fab.setOnClickListener(this);
        rd_gender_female.setOnClickListener(this);
        rd_gender_male.setOnClickListener(this);
//        rd_gender_group.setOnClickListener(this);
        /* ask for different permissions
         * - accessing map : you dont go ahead if you are not here
         * */

        /* instantiate presenter*/
        customerDataRepo = new CustomerDataRepository(this);
        presenter = new PersonnalInfoPresenter(customerDataRepo, this);
    }

    private void bindViews() {

        tv_nickname.setCompoundDrawablesWithIntrinsicBounds(null, null,
                VectorDrawableCompat.create(getResources(), R.drawable.myaccount_address_edit_red, null),
                 null);
        /* tv_nickname
        android:drawableRight="@drawable/myaccount_address_edit_red"
        android:drawableEnd="@drawable/myaccount_address_edit_red"
                        */
    }

    private void initViews() {
        photoImageView = findViewById(R.id.cic_pic);
        tv_birth_date = findViewById(R.id.tv_birth_date);
        take_pic_fab = findViewById(R.id.take_pic_fab);
        tv_phonenumber = findViewById(R.id.tv_phone_number);
        tv_nickname = findViewById(R.id.tv_nickname);
        rd_gender_female = findViewById(R.id.rd_bt_gender_female);
        rd_gender_male = findViewById(R.id.rd_bt_gender_male);
        rd_gender_group = findViewById(R.id.rd_gender_group);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* check if activity is on result */
        if (presenter != null && !activityResultSuccess) {
            presenter.start();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_birth_date:
                pickBirthDate();
                break;
            case R.id.take_pic_fab:
                pickPhoto();
                break;
            case R.id.rd_bt_gender_female:
                break;
            case R.id.rd_bt_gender_male:
                break;
        }
    }

    private void pickPhoto() {

        /* u cant crop picture here for now */
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(null)
                .setActivityTheme(R.style.AppTheme)
                .setCameraPlaceholder(R.drawable.ic_photo_camera_white_24dp)
                .pickPhoto(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* on activity result */
        Log.d(Constant.APP_TAG, "onactivity result");
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    images = new ArrayList<>();
                    images.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    /* set image */
                    if (images != null && images.size() > 0) {
                        GlideApp.with(PersonnalInfoActivity.this)
                                .load(images.get(0))
                                .transition(GenericTransitionOptions.with(((MyKabaApp) getApplicationContext()).getGlideAnimation()))
                                .centerCrop()
                                .into(photoImageView);
                        customer.profilePicture = UtilFunctions.changePathToBase64(images.get(0));
                        activityResultSuccess = true;
                    }
                }
                break;
        }
    }

    private void pickBirthDate() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void inflateCustomerData(final Customer customer) {

        this.customer = customer;

        this.tv_nickname.setText(customer.nickname);
        this.tv_phonenumber.setText(customer.phone_number);
        this.tv_birth_date.setText(customer.birthday);

        /*  show the image */
        GlideApp.with(PersonnalInfoActivity.this)
                .load(Constant.SERVER_ADDRESS +"/"+ customer.profilePicture)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                .centerCrop()
                .into(photoImageView);
        if (customer.isSet == 0) {
            rd_gender_female.setEnabled(false);
            rd_gender_male.setEnabled(false);
        }
        else {
            rd_gender_female.setEnabled(true);
            rd_gender_male.setEnabled(true);
        }

        photoImageView.setOnClickListener(new OnImageClickListener() {
            @Override
            public void onClick(View view) {
                /* on transforme ca en ads */
                AdsBanner adsBanner = new AdsBanner();
                adsBanner.name = "";
                adsBanner.pic = customer.profilePicture;
                adsBanner.description = "";

                Intent intent = new Intent(PersonnalInfoActivity.this, ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, new AdsBanner[]{adsBanner});
                PersonnalInfoActivity.this.startActivity(intent);
            }
        });
    }


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
    public void finishActivity() {

        setResult(PersonnalInfoActivity.CONTENT_MODIFIED);
        finish();
    }

    @Override
    public void toast(int message_resource_id) {
        Toast.makeText(this, getResources().getString(message_resource_id), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(PersonnalInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoggingTimeout() {
        /* show some alert box to mention that the loggin is out */
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();

            // get it from user information bundle, if doenst exist then...

            // Create a new instance of TimePickerDialog and return it
            int dayOfMonth = c.getTime().getDay();
            int month = c.getTime().getMonth();
            int year = c.getTime().getYear();

            return new DatePickerDialog(getActivity(), this, year, month,
                    dayOfMonth);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }

        @Override
        public void onDateSet (DatePicker view, int year, int month, int dayOfMonth) {
            tv_birth_date.setText(dayOfMonth+" - "+month+" - "+year);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personnal_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_confirm) {
            this.confirm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirm() {

        customer.nickname = tv_nickname.getText().toString();
        /* change picture */
        /* change birthday */
        customer.birthday = tv_birth_date.getText().toString();
        customer.gender = rd_gender_female.isChecked() ? 1 : 0;

        presenter.updateUserInformations(customer);
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


    /* we need an interface for changing all the informations */
}
