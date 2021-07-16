package tg.tmye.kaba_i_deliver.service;

/**
 * By abiguime on 2021/7/13.
 * email: 2597434002@qq.com
 */

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import tg.tmye.kaba_i_deliver.activity.command.MyCommandsActivity;
import tg.tmye.kaba_i_deliver.data.model.FirebaseDataModel;
import tg.tmye.kaba_i_deliver.receiver.RestartBroadcastReceiver;
import tg.tmye.kaba_i_deliver.syscore.Constant;
import tg.tmye.kaba_i_deliver.syscore.ILog;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION;


public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int ID_SERVICE = 1001;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;

    public int counter = 0;
    private Timer timer;
    private TimerTask timerTask;

    DatabaseReference mRootRef= FirebaseDatabase.getInstance("https://kaba-livreur.firebaseio.com/").getReference();


    DatabaseReference locationRef=mRootRef.child("kirikou");

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, MyCommandsActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(new NotificationChannel(Constant.CHANNEL_ID, Constant.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constant.CHANNEL_ID);

        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Where is Kirikou ?")
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(ID_SERVICE, notification);
//        startForeground(ID_SERVICE, notification, FOREGROUND_SERVICE_TYPE_LOCATION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ILog.print("onStartCommand TrackingService");
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        ILog.print("onStartCommand TrackingService : connect ok");
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        updateUI();
        ILog.print("onLocationChanged TrackingService");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        ILog.print("onConnected TrackingService 1start");

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, TrackingService.this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
        }
        updateUI();
        ILog.print("onConnected TrackingService ok");
    }

    @Override
    public void onConnectionSuspended(int i) {
        ILog.print("onConnectionSuspended TrackingService");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
        ILog.print("onConnectionFailed TrackingService");
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        stopForeground(true);
        ILog.print("Destroying TrackingService");
        Intent broadcastIntent = new Intent(this, RestartBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    void updateUI() {
        // only if im authentificated
        if ("".equals(((MyKabaDeliverApp)getApplicationContext()).getAuthToken()))
            return;

        FirebaseDataModel firebaseDataModel = new FirebaseDataModel(
                ((MyKabaDeliverApp)getApplicationContext()).getAuthToken(),
                ((MyKabaDeliverApp)getApplicationContext()).getUsername(),
                Float.parseFloat(lat),
                Float.parseFloat(lon),
                ((MyKabaDeliverApp)getApplicationContext()).getDeliveryMode(),
                new Date().getTime(),
                getDateTime()
        );
        locationRef
                .child(((MyKabaDeliverApp)getApplicationContext()).getUsername())
                .setValue(firebaseDataModel);
        ILog.print("Update "+firebaseDataModel.toString());
    }

    private String getDateTime() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
    }


    public void startTimer() {
        timer = new Timer();

        //initialize the TimerTask's job
        initialiseTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 5000, 5000); //
    }

    public void initialiseTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ILog.print("Timer is running " + counter++);
            }
        };
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}