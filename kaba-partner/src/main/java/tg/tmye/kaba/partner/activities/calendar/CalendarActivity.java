package tg.tmye.kaba.partner.activities.calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.TimePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner.activities.calendar.contract.CalendarContract;
import tg.tmye.kaba.partner.activities.calendar.presenter.CalendarPresenter;
import tg.tmye.kaba.partner.activities.stats.StatsActivity;
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba.partner.data.calendar.CalendarModel;
import tg.tmye.kaba.partner.data.calendar.source.CalendarRepository;

public class CalendarActivity extends AppCompatActivity implements CalendarContract.View, View.OnClickListener {

    /* monday */
    LinearLayoutCompat lny_1_1;
    LinearLayoutCompat lny_1_2;

    CheckBox cb_opened_1; // is restaurant opened on this day
    CheckBox cb_pause_1; // is restaurant having pause on this day
    TextView tv_start_work_1; // start working time
    TextView tv_end_work_1; // end working time
    TextView tv_start_pause_work_1; // start pause time
    TextView tv_end_pause_work_1; // end pause time

    /* tuesday */

    LinearLayoutCompat lny_2_1;
    LinearLayoutCompat lny_2_2;

    CheckBox cb_opened_2; // is restaurant opened on this day
    CheckBox cb_pause_2; // is restaurant having pause on this day
    TextView tv_start_work_2; // start working time
    TextView tv_end_work_2; // end working time
    TextView tv_start_pause_work_2; // start pause time
    TextView tv_end_pause_work_2; // end pause time


    /* wednesday */

    LinearLayoutCompat lny_3_1;
    LinearLayoutCompat lny_3_2;

    CheckBox cb_opened_3; // is restaurant opened on this day
    CheckBox cb_pause_3; // is restaurant having pause on this day
    TextView tv_start_work_3; // start working time
    TextView tv_end_work_3; // end working time
    TextView tv_start_pause_work_3; // start pause time
    TextView tv_end_pause_work_3; // end pause time

    /* thursday */

    LinearLayoutCompat lny_4_1;
    LinearLayoutCompat lny_4_2;

    CheckBox cb_opened_4; // is restaurant opened on this day
    CheckBox cb_pause_4; // is restaurant having pause on this day
    TextView tv_start_work_4; // start working time
    TextView tv_end_work_4; // end working time
    TextView tv_start_pause_work_4; // start pause time
    TextView tv_end_pause_work_4; // end pause time

    /* friday */

    LinearLayoutCompat lny_5_1;
    LinearLayoutCompat lny_5_2;

    CheckBox cb_opened_5; // is restaurant opened on this day
    CheckBox cb_pause_5; // is restaurant having pause on this day
    TextView tv_start_work_5; // start working time
    TextView tv_end_work_5; // end working time
    TextView tv_start_pause_work_5; // start pause time
    TextView tv_end_pause_work_5; // end pause time

    /* saturday */

    LinearLayoutCompat lny_6_1;
    LinearLayoutCompat lny_6_2;

    CheckBox cb_opened_6; // is restaurant opened on this day
    CheckBox cb_pause_6; // is restaurant having pause on this day
    TextView tv_start_work_6; // start working time
    TextView tv_end_work_6; // end working time
    TextView tv_start_pause_work_6; // start pause time
    TextView tv_end_pause_work_6; // end pause time

    /* sunday */

    LinearLayoutCompat lny_7_1;
    LinearLayoutCompat lny_7_2;

    CheckBox cb_opened_7; // is restaurant opened on this day
    CheckBox cb_pause_7; // is restaurant having pause on this day
    TextView tv_start_work_7; // start working time
    TextView tv_end_work_7; // end working time
    TextView tv_start_pause_work_7; // start pause time
    TextView tv_end_pause_work_7; // end pause time

    private Toolbar mToolbar;

    CalendarRepository repository;
    CalendarPresenter presenter;

    Button bt_tryagain;
    TextView tv_messages;
    LinearLayoutCompat lny_error_box;
    private NestedScrollView nested_scroll_view;

    Button bt_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);
        setTitle(R.string.calendar);

        initViews();

        initListeners();

        repository = new CalendarRepository(this);
        presenter = new CalendarPresenter(this, repository);

        presenter.loadCalendar();
    }

    private void initListeners() {

        bt_tryagain.setOnClickListener(this);

        cb_opened_1.setOnCheckedChangeListener(new RestaurantIsOpenCheckedChangeListener());
        cb_opened_2.setOnCheckedChangeListener(new RestaurantIsOpenCheckedChangeListener());
        cb_opened_3.setOnCheckedChangeListener(new RestaurantIsOpenCheckedChangeListener());
        cb_opened_4.setOnCheckedChangeListener(new RestaurantIsOpenCheckedChangeListener());
        cb_opened_5.setOnCheckedChangeListener(new RestaurantIsOpenCheckedChangeListener());
        cb_opened_6.setOnCheckedChangeListener(new RestaurantIsOpenCheckedChangeListener());
        cb_opened_7.setOnCheckedChangeListener(new RestaurantIsOpenCheckedChangeListener());

        cb_pause_1.setOnCheckedChangeListener(new RestaurantHasPauseCheckedChangeListener());
        cb_pause_2.setOnCheckedChangeListener(new RestaurantHasPauseCheckedChangeListener());
        cb_pause_3.setOnCheckedChangeListener(new RestaurantHasPauseCheckedChangeListener());
        cb_pause_4.setOnCheckedChangeListener(new RestaurantHasPauseCheckedChangeListener());
        cb_pause_5.setOnCheckedChangeListener(new RestaurantHasPauseCheckedChangeListener());
        cb_pause_6.setOnCheckedChangeListener(new RestaurantHasPauseCheckedChangeListener());
        cb_pause_7.setOnCheckedChangeListener(new RestaurantHasPauseCheckedChangeListener());

        tv_start_work_1.setOnClickListener(new OnStartWorkListener());
        tv_start_work_2.setOnClickListener(new OnStartWorkListener());
        tv_start_work_3.setOnClickListener(new OnStartWorkListener());
        tv_start_work_4.setOnClickListener(new OnStartWorkListener());
        tv_start_work_5.setOnClickListener(new OnStartWorkListener());
        tv_start_work_6.setOnClickListener(new OnStartWorkListener());
        tv_start_work_7.setOnClickListener(new OnStartWorkListener());

        tv_end_work_1.setOnClickListener(new OnEndWorkListener());
        tv_end_work_2.setOnClickListener(new OnEndWorkListener());
        tv_end_work_3.setOnClickListener(new OnEndWorkListener());
        tv_end_work_4.setOnClickListener(new OnEndWorkListener());
        tv_end_work_5.setOnClickListener(new OnEndWorkListener());
        tv_end_work_6.setOnClickListener(new OnEndWorkListener());
        tv_end_work_7.setOnClickListener(new OnEndWorkListener());

        tv_start_pause_work_1.setOnClickListener(new OnPauseStartListener());
        tv_start_pause_work_2.setOnClickListener(new OnPauseStartListener());
        tv_start_pause_work_3.setOnClickListener(new OnPauseStartListener());
        tv_start_pause_work_4.setOnClickListener(new OnPauseStartListener());
        tv_start_pause_work_5.setOnClickListener(new OnPauseStartListener());
        tv_start_pause_work_6.setOnClickListener(new OnPauseStartListener());
        tv_start_pause_work_7.setOnClickListener(new OnPauseStartListener());

        tv_end_pause_work_1.setOnClickListener(new OnPauseEndListener());
        tv_end_pause_work_2.setOnClickListener(new OnPauseEndListener());
        tv_end_pause_work_3.setOnClickListener(new OnPauseEndListener());
        tv_end_pause_work_4.setOnClickListener(new OnPauseEndListener());
        tv_end_pause_work_5.setOnClickListener(new OnPauseEndListener());
        tv_end_pause_work_6.setOnClickListener(new OnPauseEndListener());
        tv_end_pause_work_7.setOnClickListener(new OnPauseEndListener());

        bt_confirm.setOnClickListener(this);
    }

    private void initViews() {

        bt_confirm = findViewById(R.id.bt_confirm);

        bt_tryagain = findViewById(R.id.bt_tryagain);
        tv_messages = findViewById(R.id.tv_messages);
        lny_error_box = findViewById(R.id.lny_error_box);
        nested_scroll_view = findViewById(R.id.nested_scroll_view);

        lny_1_1 = findViewById(R.id.lny_1_1);
        lny_1_2 = findViewById(R.id.lny_1_2);
        lny_2_1 = findViewById(R.id.lny_2_1);
        lny_2_2 = findViewById(R.id.lny_2_2);
        lny_3_1 = findViewById(R.id.lny_3_1);
        lny_3_2 = findViewById(R.id.lny_3_2);
        lny_4_1 = findViewById(R.id.lny_4_1);
        lny_4_2 = findViewById(R.id.lny_4_2);
        lny_5_1 = findViewById(R.id.lny_5_1);
        lny_5_2 = findViewById(R.id.lny_5_2);
        lny_6_1 = findViewById(R.id.lny_6_1);
        lny_6_2 = findViewById(R.id.lny_6_2);
        lny_7_1 = findViewById(R.id.lny_7_1);
        lny_7_2 = findViewById(R.id.lny_7_2);


        cb_opened_1 = findViewById(R.id.cb_opened_1);
        cb_pause_1 = findViewById(R.id.cb_pause_1);
        tv_start_work_1 = findViewById(R.id.tv_start_work_1);
        tv_end_work_1 = findViewById(R.id.tv_end_work_1);
        tv_start_pause_work_1 = findViewById(R.id.tv_start_pause_work_1);
        tv_end_pause_work_1 = findViewById(R.id.tv_end_pause_work_1);

        cb_opened_2 = findViewById(R.id.cb_opened_2);
        cb_pause_2 = findViewById(R.id.cb_pause_2);
        tv_start_work_2 = findViewById(R.id.tv_start_work_2);
        tv_end_work_2 = findViewById(R.id.tv_end_work_2);
        tv_start_pause_work_2 = findViewById(R.id.tv_start_pause_work_2);
        tv_end_pause_work_2 = findViewById(R.id.tv_end_pause_work_2);

        cb_opened_3 = findViewById(R.id.cb_opened_3);
        cb_pause_3 = findViewById(R.id.cb_pause_3);
        tv_start_work_3 = findViewById(R.id.tv_start_work_3);
        tv_end_work_3 = findViewById(R.id.tv_end_work_3);
        tv_start_pause_work_3 = findViewById(R.id.tv_start_pause_work_3);
        tv_end_pause_work_3 = findViewById(R.id.tv_end_pause_work_3);

        cb_opened_4 = findViewById(R.id.cb_opened_4);
        cb_pause_4 = findViewById(R.id.cb_pause_4);
        tv_start_work_4 = findViewById(R.id.tv_start_work_4);
        tv_end_work_4 = findViewById(R.id.tv_end_work_4);
        tv_start_pause_work_4 = findViewById(R.id.tv_start_pause_work_4);
        tv_end_pause_work_4 = findViewById(R.id.tv_end_pause_work_4);

        cb_opened_5 = findViewById(R.id.cb_opened_5);
        cb_pause_5 = findViewById(R.id.cb_pause_5);
        tv_start_work_5 = findViewById(R.id.tv_start_work_5);
        tv_end_work_5 = findViewById(R.id.tv_end_work_5);
        tv_start_pause_work_5 = findViewById(R.id.tv_start_pause_work_5);
        tv_end_pause_work_5 = findViewById(R.id.tv_end_pause_work_5);

        cb_opened_6 = findViewById(R.id.cb_opened_6);
        cb_pause_6 = findViewById(R.id.cb_pause_6);
        tv_start_work_6 = findViewById(R.id.tv_start_work_6);
        tv_end_work_6 = findViewById(R.id.tv_end_work_6);
        tv_start_pause_work_6 = findViewById(R.id.tv_start_pause_work_6);
        tv_end_pause_work_6 = findViewById(R.id.tv_end_pause_work_6);

        cb_opened_7 = findViewById(R.id.cb_opened_7);
        cb_pause_7 = findViewById(R.id.cb_pause_7);
        tv_start_work_7 = findViewById(R.id.tv_start_work_7);
        tv_end_work_7 = findViewById(R.id.tv_end_work_7);
        tv_start_pause_work_7 = findViewById(R.id.tv_start_pause_work_7);
        tv_end_pause_work_7 = findViewById(R.id.tv_end_pause_work_7);

    }


    @Override
    public void networkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //
                nested_scroll_view.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void syserror() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //
                nested_scroll_view.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void modificationSuccess(boolean successful) {
        if (successful)
            finish();
        else
            mToast("Modification error");
    }

    @Override
    public void inflateCalendar(CalendarModel[] calendar) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_error_box.setVisibility(View.GONE);
                nested_scroll_view.setVisibility(View.VISIBLE);
            }
        });

        inflateCalendarDayIntoViews(calendar[0],
                lny_1_1, lny_1_2,
                cb_opened_1, cb_pause_1,
                tv_start_work_1, tv_end_work_1,
                tv_start_pause_work_1, tv_end_pause_work_1);

        inflateCalendarDayIntoViews(calendar[1],
                lny_2_1, lny_2_2,
                cb_opened_2, cb_pause_2,
                tv_start_work_2, tv_end_work_2,
                tv_start_pause_work_2, tv_end_pause_work_2);

        inflateCalendarDayIntoViews(calendar[2],
                lny_3_1, lny_3_2,
                cb_opened_3, cb_pause_3,
                tv_start_work_3, tv_end_work_3,
                tv_start_pause_work_3, tv_end_pause_work_3);

        inflateCalendarDayIntoViews(calendar[3],
                lny_4_1, lny_4_2,
                cb_opened_4, cb_pause_4,
                tv_start_work_4, tv_end_work_4,
                tv_start_pause_work_4, tv_end_pause_work_4);

        inflateCalendarDayIntoViews(calendar[4],
                lny_5_1, lny_5_2,
                cb_opened_5, cb_pause_5,
                tv_start_work_5, tv_end_work_5,
                tv_start_pause_work_5, tv_end_pause_work_5);

        inflateCalendarDayIntoViews(calendar[5],
                lny_6_1, lny_6_2,
                cb_opened_6, cb_pause_6,
                tv_start_work_6, tv_end_work_6,
                tv_start_pause_work_6, tv_end_pause_work_6);

        inflateCalendarDayIntoViews(calendar[6],
                lny_7_1, lny_7_2,
                cb_opened_7, cb_pause_7,
                tv_start_work_7, tv_end_work_7,
                tv_start_pause_work_7, tv_end_pause_work_7);
    }

    private void inflateCalendarDayIntoViews(
            CalendarModel calendarModel,
            LinearLayoutCompat lny_1, LinearLayoutCompat lny_2,
            CheckBox cb_opened, CheckBox cb_pause,
            TextView tv_start_work, TextView tv_end_work,
            TextView tv_start_pause_work, TextView tv_end_pause_work) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cb_opened.setChecked(calendarModel.open == 1);
                cb_pause.setChecked(calendarModel.pause == 1);
                if (calendarModel.open == 1) {
                    lny_1.setVisibility(View.VISIBLE);
                    tv_start_work.setText(calendarModel.start);
                    tv_end_work.setText(calendarModel.end);

                    cb_pause.setEnabled(true);
                    if (calendarModel.pause == 1){
                        lny_2.setVisibility(View.VISIBLE);
                        tv_start_pause_work.setText(calendarModel.pause_start);
                        tv_end_pause_work.setText(calendarModel.pause_end);
                    } else {
                        lny_2.setVisibility(View.VISIBLE);
//                        lny_2.setVisibility(View.GONE);
                    }

                } else {
                    cb_pause.setEnabled(false);
//                    lny_1.setVisibility(View.GONE);
//                    lny_2.setVisibility(View.GONE);
                    lny_1.setVisibility(View.VISIBLE);
                    lny_2.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_tryagain:
                presenter.loadCalendar();
                break;
            case R.id.bt_confirm:
                _updateCalendar();
                break;
        }
    }

    private void _updateCalendar() {

        CalendarModel[] calendars = new CalendarModel[7];

        int errors = 0;

        calendars[0] = new CalendarModel("monday",
                cb_opened_1.isChecked() ? 1 : 0, cb_pause_1.isChecked() ? 1 : 0,
                tv_start_work_1.getText().toString(), tv_end_work_1.getText().toString(),
                tv_start_pause_work_1.getText().toString(), tv_end_pause_work_1.getText().toString()
        );

//        if (cb_opened_1.isChecked())
            errors += controlField(lny_1_1, tv_start_work_1, tv_end_work_1);
//        if (cb_pause_1.isChecked())
            errors +=  controlField(lny_1_2, tv_start_pause_work_1, tv_end_pause_work_1);

        calendars[1] = new CalendarModel("tuesday",
                cb_opened_2.isChecked() ? 1 : 0, cb_pause_2.isChecked() ? 1 : 0,
                tv_start_work_2.getText().toString(), tv_end_work_2.getText().toString(),
                tv_start_pause_work_2.getText().toString(), tv_end_pause_work_2.getText().toString()
        );

//        if (cb_opened_2.isChecked())
        errors += controlField(lny_2_1, tv_start_work_2, tv_end_work_2);
//        if (cb_pause_2.isChecked())
        errors +=  controlField(lny_2_2, tv_start_pause_work_2, tv_end_pause_work_2);


        calendars[2] = new CalendarModel("wednesday",
                cb_opened_3.isChecked() ? 1 : 0, cb_pause_3.isChecked() ? 1 : 0,
                tv_start_work_3.getText().toString(), tv_end_work_3.getText().toString(),
                tv_start_pause_work_3.getText().toString(), tv_end_pause_work_3.getText().toString()
        );

//        if (cb_opened_3.isChecked())
        errors += controlField(lny_3_1, tv_start_work_3, tv_end_work_3);
//        if (cb_pause_3.isChecked())
        errors +=  controlField(lny_3_2, tv_start_pause_work_3, tv_end_pause_work_3);

        calendars[3] = new CalendarModel("thursday",
                cb_opened_4.isChecked() ? 1 : 0, cb_pause_4.isChecked() ? 1 : 0,
                tv_start_work_4.getText().toString(), tv_end_work_4.getText().toString(),
                tv_start_pause_work_4.getText().toString(), tv_end_pause_work_4.getText().toString()
        );

//        if (cb_opened_4.isChecked())
        errors += controlField(lny_4_1, tv_start_work_4, tv_end_work_4);
//        if (cb_pause_4.isChecked())
        errors +=  controlField(lny_4_2, tv_start_pause_work_4, tv_end_pause_work_4);

        calendars[4] = new CalendarModel("friday",
                cb_opened_5.isChecked() ? 1 : 0, cb_pause_5.isChecked() ? 1 : 0,
                tv_start_work_5.getText().toString(), tv_end_work_5.getText().toString(),
                tv_start_pause_work_5.getText().toString(), tv_end_pause_work_5.getText().toString()
        );

//        if (cb_opened_5.isChecked())
        errors += controlField(lny_5_1, tv_start_work_5, tv_end_work_5);
//        if (cb_pause_5.isChecked())
        errors +=  controlField(lny_5_2, tv_start_pause_work_5, tv_end_pause_work_5);


        calendars[5] = new CalendarModel("saturday",
                cb_opened_6.isChecked() ? 1 : 0, cb_pause_6.isChecked() ? 1 : 0,
                tv_start_work_6.getText().toString(), tv_end_work_6.getText().toString(),
                tv_start_pause_work_6.getText().toString(), tv_end_pause_work_6.getText().toString()
        );

//        if (cb_opened_6.isChecked())
        errors += controlField(lny_6_1, tv_start_work_6, tv_end_work_6);
//        if (cb_pause_6.isChecked())
        errors +=  controlField(lny_6_2, tv_start_pause_work_6, tv_end_pause_work_6);


        calendars[6] = new CalendarModel("sunday",
                cb_opened_7.isChecked() ? 1 : 0, cb_pause_7.isChecked() ? 1 : 0,
                tv_start_work_7.getText().toString(), tv_end_work_7.getText().toString(),
                tv_start_pause_work_7.getText().toString(), tv_end_pause_work_7.getText().toString()
        );

//        if (cb_opened_7.isChecked())
        errors += controlField(lny_7_1, tv_start_work_7, tv_end_work_7);
//        if (cb_pause_7.isChecked())
        errors +=  controlField(lny_7_2, tv_start_pause_work_7, tv_end_pause_work_7);

        /* control each of the time frame */
        if (errors != 0) {
            mToast("error");
        } else {
            /* update the rest to the server */
            presenter.updateCalendar(calendars);
        }
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private int controlField(LinearLayoutCompat lny, TextView tv_start_work, TextView tv_end_work) {
        String start = tv_start_work.getText().toString();
        String end = tv_end_work.getText().toString();

        String[] startTime = start.split(":");
        String[] endTime = end.split(":");

        if (Integer.parseInt(startTime[0])*60+Integer.parseInt(startTime[1]) >=   Integer.parseInt(endTime[0])*60+Integer.parseInt(endTime[1])){
            // error
            lny.setBackgroundColor(getColor(R.color.transparent_white_red));
            return -1;
        } else {
            lny.setBackgroundColor(getColor(R.color.transparent));
            return 0;
        }
    }


    private class RestaurantIsOpenCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.cb_opened_1:
//                    lny_1_1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//                    lny_1_2.setVisibility(isChecked && cb_pause_1.isChecked() ? View.VISIBLE : View.GONE);
                    cb_pause_1.setEnabled(isChecked);
                    break;
                case R.id.cb_opened_2:
//                    lny_2_1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//                    lny_2_2.setVisibility(isChecked && cb_pause_1.isChecked() ? View.VISIBLE : View.GONE);
                    cb_pause_2.setEnabled(isChecked);
                    break;
                case R.id.cb_opened_3:
//                    lny_3_1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//                    lny_3_2.setVisibility(isChecked && cb_pause_1.isChecked() ? View.VISIBLE : View.GONE);
                    cb_pause_3.setEnabled(isChecked);
                    break;
                case R.id.cb_opened_4:
//                    lny_4_1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//                    lny_4_2.setVisibility(isChecked && cb_pause_1.isChecked() ? View.VISIBLE : View.GONE);
                    cb_pause_4.setEnabled(isChecked);
                    break;
                case R.id.cb_opened_5:
//                    lny_5_1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//                    lny_5_2.setVisibility(isChecked && cb_pause_1.isChecked() ? View.VISIBLE : View.GONE);
                    cb_pause_5.setEnabled(isChecked);
                    break;
                case R.id.cb_opened_6:
//                    lny_6_1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//                    lny_6_2.setVisibility(isChecked && cb_pause_1.isChecked() ? View.VISIBLE : View.GONE);
                    cb_pause_6.setEnabled(isChecked);
                    break;
                case R.id.cb_opened_7:
//                    lny_7_1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//                    lny_7_2.setVisibility(isChecked && cb_pause_1.isChecked() ? View.VISIBLE : View.GONE);
                    cb_pause_7.setEnabled(isChecked);
                    break;
            }
        }
    }


    private class RestaurantHasPauseCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.cb_pause_1:
//                    lny_1_2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    break;
                case R.id.cb_pause_2:
//                    lny_2_2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    break;
                case R.id.cb_pause_3:
//                    lny_3_2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    break;
                case R.id.cb_pause_4:
//                    lny_4_2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    break;
                case R.id.cb_pause_5:
//                    lny_5_2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    break;
                case R.id.cb_pause_6:
//                    lny_6_2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    break;
                case R.id.cb_pause_7:
//                    lny_7_2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    break;
            }
        }
    }


    private class OnStartWorkListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showTimePickerDialog(v.getId());
        }
    }

    private class OnEndWorkListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showTimePickerDialog(v.getId());
        }
    }

    private class OnPauseStartListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showTimePickerDialog(v.getId());
        }
    }

    private class OnPauseEndListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showTimePickerDialog(v.getId());
        }
    }

    public void showTimePickerDialog(int viewId) {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(viewId);
        newFragment.show(getSupportFragmentManager(), "TimePickerFragment");
    }

    private void setToDate(String date) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                tv_to_date.setText(date);
            }
        });
    }

    private void setFromDate(String date) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                tv_from_date.setText(date);
            }
        });
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private String date;

        private int viewId;

        public static TimePickerFragment newInstance(int viewId) {

            Bundle args = new Bundle();
            TimePickerFragment fragment = new TimePickerFragment();
            args.putInt("viewId", viewId);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            viewId = getArguments().getInt("viewId", -1);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(),this, 12, 0, true);
        }

/*

        public void onDateSet(TimePicker view, int year, int month, int day) {
            month+=1;
            switch (getTag()){
                case "tv_to_date":
                    ((StatsActivity)getActivity()).setToDate(year+"-"+(month < 10 ? "0":"")+month+"-"+(day < 10 ? "0":"")+day);
                    break;
                case "tv_from_date":
                    ((StatsActivity)getActivity()).setFromDate(year+"-"+(month < 10 ? "0":"")+month+"-"+(day < 10 ? "0":"")+day);
                    break;
            }
        }
*/

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            try {
                ((CalendarActivity) getActivity()).setTimeForViewId(viewId, (hourOfDay < 10 ? "0" : "") + hourOfDay + ":" + (minute < 10 ? "0" : "") + minute);
            } catch (Exception e) {
                e.printStackTrace();
                mToast("ontimeset error");
            }
        }

        private void mToast(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void setTimeForViewId(int viewId, String time){
        TextView tv = findViewById(viewId);
        tv.setText(time);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (loadingDialogFragment == null) {
                    if (isLoading) {
                        loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.content_on_loading));
                        showFragment(loadingDialogFragment, "loadingbox", true);

                        lny_error_box.setVisibility(View.GONE);
                        nested_scroll_view.setVisibility(View.GONE);
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

}