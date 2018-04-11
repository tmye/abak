package tg.tmye.kaba.activity.UserAcc.personnalinfo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.UserAcc.personnalinfo.contract.PersonnalInfoContract;
import tg.tmye.kaba.activity.UserAcc.personnalinfo.presenter.PersonnalInfoPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.syscore.GlideApp;


public class PersonnalInfoActivity extends AppCompatActivity implements
        View.OnClickListener, PersonnalInfoContract.View {

    private List<String> images;


    private FloatingActionButton take_pic_fab;
    public static TextView tv_birth_date;
    private ImageView photoImageView;

    private TextView tv_realname, tv_nickname;

    private RadioButton rd_gender_female;
    private RadioButton rd_gender_male;

    private RadioGroup rd_gender_group;

    /* presenter */
    private PersonnalInfoContract.Presenter presenter;
    private CustomerDataRepository customerDataRepo;

    /* has gone through result */
    private boolean activityResultSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnal_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_yellow_upward_navigation_24dp);

        initViews();

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

    private void initViews() {
        photoImageView = findViewById(R.id.cic_pic);
        tv_birth_date = findViewById(R.id.tv_birth_date);
        take_pic_fab = findViewById(R.id.take_pic_fab);
        tv_realname = findViewById(R.id.tv_realname);
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

                    GlideApp.with(PersonnalInfoActivity.this)
                            .load(images.get(0))
                            .centerCrop()
                            .into(photoImageView);
                    activityResultSuccess = true;
                }
                break;
        }
    }

    private void pickBirthDate() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void inflateCustomerData(Customer customer) {
        this.tv_nickname.setText(customer.username);
        this.tv_birth_date.setText(customer.birthday);

        /*  show the image */
        GlideApp.with(PersonnalInfoActivity.this)
                .load(Constant.SERVER_ADDRESS+customer.profile_picture)
                .centerCrop()
                .into(photoImageView);
        if (customer.is_gender_to_set == 0) {
            rd_gender_female.setEnabled(false);
            rd_gender_male.setEnabled(false);
        }
        else {
            rd_gender_female.setEnabled(true);
            rd_gender_male.setEnabled(true);
        }
    }

    @Override
    public void setPresenter(PersonnalInfoContract.Presenter presenter) {
        this.presenter = presenter;
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
            tv_birth_date.setText(""+dayOfMonth+" - "+month+" "+year);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* we need an interface for changing all the informations */
}
