package tg.tmye.kaba_i_deliver.activity.readygo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.command.MyCommandsActivity;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

public class DeliveryReadyActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_GPS_LOCATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_ready);

        Button bt_go_online = findViewById(R.id.bt_go_online);
        bt_go_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* update shared preferences and go to command activity */

                // check if you have geoloc permission
                if (ActivityCompat.checkSelfPermission(DeliveryReadyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DeliveryReadyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    ((MyKabaDeliverApp)getApplicationContext()).setDeliveryModeOn();
                    startActivity(new Intent(DeliveryReadyActivity.this, MyCommandsActivity.class));
                } else {
                    requestGpsLocationPermission();
                }
            }
        });
    }

    private void requestGpsLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(DeliveryReadyActivity.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_GPS_LOCATION);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_GPS_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(DeliveryReadyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DeliveryReadyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ((MyKabaDeliverApp)getApplicationContext()).setDeliveryModeOn();
            startActivity(new Intent(DeliveryReadyActivity.this, MyCommandsActivity.class));
        } else {
           finish();
        }
    }
}