package tg.tmye.kaba.activity.Splash;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.home.HomeActivity;
import tg.tmye.kaba.activity.terms_and_condition.TermsAndConditionsActivity;
import tg.tmye.kaba.config.Constant;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static tg.tmye.kaba.config.Constant.CHANNEL_ID;


public class SplashActivity extends AppCompatActivity {

    public final static int LAPSE = 2000;

    private static final int MY_PERMISSION_REQUEST_STORAGE = 0;
    private static final int MY_PERMISSION_REQUEST_CALL = 2;
    private static final int MY_PERMISSION_REQUEST_GPS_LOCATION = 3;
    private static final int MY_PERMISSION_LOCATION_PERMISSION = 9;

    private static final int PERMISSIONS_REQUEST_CODE = 1001;

    public static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    private static final String FIRST_TIME_SP = "FIRST_TIME_SP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View mContentView = findViewById(R.id.full_screen_content);

        createNotificationChannel();

        /* show the activity for seconds and jump to home activity */
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (isFirstTimeOpenApplication()) {
//                    isNoMoreFirstTime();
            Intent intent = new Intent(SplashActivity.this, KabaPreviewSlidingActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (!checkNeededPermissions()) {
                requestAllPermissionsThatAreNotAllowed();
            } else {
                continueLoading(LAPSE);
            }
        }
    }

    String[] permisssions = {Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, /*CAMERA,*/ CALL_PHONE, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS};

    private void requestAllPermissionsThatAreNotAllowed() {

        List<String> inner_permissions = new ArrayList<>();

        /* check if permissions are around */
        for (int i = 0; i < permisssions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permisssions[i]) != PackageManager.PERMISSION_GRANTED) {
                inner_permissions.add(permisssions[i]);
            }
        }

        if (inner_permissions.size() == 0) {
            return;
        }

        String[] inner_permissions_array = new String[inner_permissions.size()];
        for (int i = 0; i < inner_permissions.size(); i++) {
            inner_permissions_array[i] = inner_permissions.get(i);
        }

        ActivityCompat.requestPermissions(SplashActivity.this, inner_permissions_array, PERMISSIONS_REQUEST_CODE);
    }

    private boolean checkNeededPermissions () {
        for (int i = 0; i < permisssions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permisssions[i]) != PackageManager.PERMISSION_GRANTED) {
                Log.d(Constant.APP_TAG, " ----- "+ permisssions[i]+" ---- NOT GRANTED");
                return false;
            }
        }
        return true;
    }

    private void continueLoading(@Nullable int lapse) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /* first of all, check if first time */
                if (!TermsAndConditionsActivity.checkHasAcceptedTermsAndConditions(SplashActivity.this)) {
                    Intent intent = new Intent(SplashActivity.this, TermsAndConditionsActivity.class);
                    intent.putExtra(TermsAndConditionsActivity.DATA, "main");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    Bundle dead_notification_extras = getIntent().getExtras();
                    if (dead_notification_extras != null) {
                        intent.putExtras(dead_notification_extras);
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    finish();
                }
            }
        }, lapse);
    }


    public static void isNoMoreFirstTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FIRST_TIME_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_FIRST_TIME, false);
        editor.commit();
    }


    private boolean isFirstTimeOpenApplication() {

        /* check inside a sharedpreference if it is the first time to open this app. */
        SharedPreferences preferences = getSharedPreferences(FIRST_TIME_SP, MODE_PRIVATE);
        return preferences.getBoolean(IS_FIRST_TIME, true);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setImportance(importance);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {

        boolean is_good = true;
        if (grantResults != null && grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    is_good = false;
                }

                Log.d(Constant.APP_TAG, permissions[i]+" - "+ (grantResults[i] != PackageManager.PERMISSION_GRANTED ?
                        "NOT GRANTED" : "GRANTED"));
            }
        }

        if (is_good == false) {
            finish();
        } else {
            if (!checkNeededPermissions()) {
                Log.d(Constant.APP_TAG, "SOME_PERMISSIONS_NOT_ALLOWED");
                finish();
            } else {
                Log.d(Constant.APP_TAG, "ALL PERMISSIONS ALLOWED");
                continueLoading(0);
            }
        }
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }

}
