package tg.experta.kaba.activities.UserAcc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import tg.experta.kaba.R;
import tg.experta.kaba.syscore.GlideApp;


public class PersonnalInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> images;



    private FloatingActionButton take_pic_fab;
    public static TextView tv_birth_date;
    private ImageView photoImageView;

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
    }

    private void initViews() {
        photoImageView = findViewById(R.id.cic_pic);
        tv_birth_date = findViewById(R.id.tv_birth_date);
        take_pic_fab = findViewById(R.id.take_pic_fab);
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
                }
                break;
        }
    }

    private void pickBirthDate() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
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
}
