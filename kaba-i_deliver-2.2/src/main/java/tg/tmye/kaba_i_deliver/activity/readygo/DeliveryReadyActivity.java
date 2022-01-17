package tg.tmye.kaba_i_deliver.activity.readygo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.command.MyCommandsActivity;
import tg.tmye.kaba_i_deliver.activity.readygo.contract.DeliveryReadyModeContract;
import tg.tmye.kaba_i_deliver.activity.readygo.presenter.DeliveryReadyModePresenter;
import tg.tmye.kaba_i_deliver.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba_i_deliver.data.command.source.CommandRepository;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

public class DeliveryReadyActivity extends AppCompatActivity implements DeliveryReadyModeContract.View {

    private static final int MY_PERMISSION_REQUEST_GPS_LOCATION = 3000;

    CommandRepository repository;
    DeliveryReadyModePresenter presenter;
    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_ready);

        Button bt_go_online = findViewById(R.id.bt_go_online);
        bt_go_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* tell the user how he is going to be controlled if he press ok on here. */
                showDialog();

            }
        });

        repository = new CommandRepository(this);
        presenter = new DeliveryReadyModePresenter(this, repository);
    }

    private void showDialog () {

        // show this all the time, no matter what or when
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.tracking_warning, null, false);

        TextView bt_cancel = view.findViewById(R.id.bt_cancel);
        TextView bt_confirm = view.findViewById(R.id.bt_confirm);

        alertDialogBuilder.setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* update shared preferences and go to command activity */
                // check if you have geo-loc permission
                if (ActivityCompat.checkSelfPermission(DeliveryReadyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DeliveryReadyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    presenter.startDeliveryMode();
                } else {
                    requestGpsLocationPermission();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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
            presenter.startDeliveryMode();
        } else {
           finish();
        }
    }


    @Override
    public void networkError() {
        mToast(getResources().getString(R.string.network_error));
    }

    @Override
    public void syserror() {
        mToast(getString(R.string.sys_error));
        showLoading(false);
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
    }


    @Override
    public void enterDeliveryModeSuccess(boolean isSuccessfull) {
        if(isSuccessfull) {
            ((MyKabaDeliverApp)getApplicationContext()).setDeliveryModeOn();
            startActivity(new Intent(DeliveryReadyActivity.this, MyCommandsActivity.class));
        } else {
            mToast(getString(R.string.sys_error));
        }
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
    public void setPresenter(Object presenter) {

    }
}