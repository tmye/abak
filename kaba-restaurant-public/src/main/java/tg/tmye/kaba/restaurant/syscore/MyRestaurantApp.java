package tg.tmye.kaba.restaurant.syscore;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.view.View;

import androidx.multidex.MultiDexApplication;

import com.bumptech.glide.request.transition.ViewPropertyTransition;

import tg.tmye.kaba.restaurant._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;


/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class MyRestaurantApp extends MultiDexApplication {

    NetworkRequestThreadBase networkRequestBase;
    DatabaseRequestThreadBase databaseRequestThreadBase;
    private String authToken;
    private ViewPropertyTransition.Animator animationObject;

    @Override
    public void onCreate() {
        super.onCreate();

        loadToken();
    }

    public NetworkRequestThreadBase getNetworkRequestBase() {
        if (networkRequestBase == null)
            networkRequestBase = new NetworkRequestThreadBase(this);
        return networkRequestBase;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        if (authToken == null || authToken.equals(""))
            return loadToken();
        return this.authToken;
    }

    private String loadToken() {
        SharedPreferences pref = getSharedPreferences(Config.RESTAURANT_SHARED_PREFS, MODE_PRIVATE);
        return pref.getString(Config.SYSTOKEN, "");
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
}
