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
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
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
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnSuccessListener<Location> {

    private static final int ID_SERVICE = 1001;
    private static final long SLOWER_POSITION_RETRIEVE_PERIOD = 30000; // 30000;
    private static final long FASTEST_POSITION_RETRIEVE_PERIOD =  10000; // 10000;

    Location mLastLocation;
        private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    FusedLocationProviderClient fusedLocationClient;
    String lat, lon;


    private ActivityRecognitionClient mActivityRecognitionClient;

    public int counter = 0;
    private Timer timer;
    private TimerTask timerTask;

    DatabaseReference mRootRef= FirebaseDatabase.getInstance("https://kaba-livreur.firebaseio.com/").getReference();

    DatabaseReference locationRef=mRootRef.child("kirikou");

    private LocationCallback locationCallback;

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, MyCommandsActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(Constant.CHANNEL_ID, Constant.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constant.CHANNEL_ID);

        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Where is Kirikou ?")
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(ID_SERVICE, notification);

        ILog.print("onCreate TrackingService");

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

        mLocationRequest = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setFastestInterval(FASTEST_POSITION_RETRIEVE_PERIOD)
                .setInterval(SLOWER_POSITION_RETRIEVE_PERIOD) // Update location every second
//                .setSmallestDisplacement(10)
        ;

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
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, TrackingService.this);
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        fusedLocationClient.getLastLocation().addOnSuccessListener(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location: locationResult.getLocations()){
                    if (location != null) {
                        mLastLocation = location;
                        lat = String.valueOf(mLastLocation.getLatitude());
                        lon = String.valueOf(mLastLocation.getLongitude());
                        updateUI();
                        ILog.print("onConnected TrackingService ok");
                        /* get tracking state */
                        if ("off".equals(((MyKabaDeliverApp)getApplicationContext()).getDeliveryMode())) {
                            fusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates
                (
                        mLocationRequest,
                        locationCallback,
                        null
                );
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

        try {
            FirebaseDataModel firebaseDataModel = new FirebaseDataModel(
                    ((MyKabaDeliverApp) getApplicationContext()).getAuthToken(),
                    ((MyKabaDeliverApp) getApplicationContext()).getUsername()+"_"+Constant.VERSION,
                    Float.parseFloat(lat),
                    Float.parseFloat(lon),
                    ((MyKabaDeliverApp) getApplicationContext()).getDeliveryMode(),
                    new Date().getTime(),
                    getDateTime()
            );
            locationRef
                    .child(((MyKabaDeliverApp) getApplicationContext()).getUsername()+"_"+Constant.VERSION)
                    .setValue(firebaseDataModel);
            ILog.print("Update " + firebaseDataModel.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
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

    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            mLastLocation = location;
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
            updateUI();
            ILog.print("onConnected TrackingService ok");
        }
    }

}