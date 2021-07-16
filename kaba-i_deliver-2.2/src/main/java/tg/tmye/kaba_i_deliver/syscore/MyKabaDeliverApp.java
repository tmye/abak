package tg.tmye.kaba_i_deliver.syscore;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;
import android.view.View;

import androidx.multidex.MultiDexApplication;

import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.google.android.gms.maps.model.LatLng;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;


/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class MyKabaDeliverApp extends MultiDexApplication {

    NetworkRequestThreadBase networkRequestBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;
    private ViewPropertyTransition.Animator animationObject;
    private LatLng latLng;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public NetworkRequestThreadBase getNetworkRequestBase() {
        if (networkRequestBase == null)
            networkRequestBase = new NetworkRequestThreadBase(this);
        return networkRequestBase;
    }

    public String getAuthToken() {
        SharedPreferences pref = getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, MODE_PRIVATE);
        return pref.getString(Config.SYSTOKEN, "");
    }

    public String getUsername() {
        SharedPreferences pref = getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, MODE_PRIVATE);
        return pref.getString(Config.DELIVERMAN_NAME, "");
    }

    public String getDeliveryMode() {
        SharedPreferences pref = getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, MODE_PRIVATE);
        return pref.getString(Config.DELIVERY_MODE_ON_OFF, "off");
    }

    public void setDeliveryModeOn() {
        SharedPreferences pref = getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Config.DELIVERY_MODE_ON_OFF, "on");
        editor.apply();
    }

    public void setDeliveryModeOff() {
        SharedPreferences pref = getSharedPreferences(Config.DELIVERMAN_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Config.DELIVERY_MODE_ON_OFF, "off");
        editor.apply();
    }

    public DatabaseRequestThreadBase getDatabaseRequestThreadBase() {
        return databaseRequestThreadBase;
    }

    public ViewPropertyTransition.Animator getGlideAnimation() {

        if (animationObject == null) {
            animationObject = new ViewPropertyTransition.Animator() {
                @Override
                public void animate(View view) {
                    // if it's a custom view class, cast it here
                    // then find subviews and do the animations
                    // here, we just use the entire view for the fade animation
                    view.setAlpha(0f);

                    ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                    fadeAnim.setDuration(300);
                    fadeAnim.start();
                }
            };
        }
        return animationObject;
    }

    public void setLastLocation(Location location) {

        if (location != null)
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public LatLng getLastLocation() {
        return this.latLng;
    }

}
