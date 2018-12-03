package tg.tmye.kaba._commons.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 25/04/2018.
 * email: 2597434002@qq.com
 */

public class KabaNotificationJobService extends JobIntentService {

    /**
     * Unique job ID for this service.
     */
    public static final int JOB_ID = 1994;

    static final String TAG = "KabaNotfJobService";
    private static final int RESTAURANT_MENU_SMALL_ICON = R.drawable.notification_kaba_drawable;
    private static final int RESTAURANT_SMALL_ICON = R.drawable.notification_kaba_drawable;
    private static final int ARTICLE_MENU_SMALL_ICON = R.drawable.notification_kaba_drawable;

    String jsonData = "";

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, KabaNotificationJobService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.

        final NotificationItem item = intent.getParcelableExtra("data");

        if (item == null)
            return;

        /* if there is an image download it here */
        Glide.with(this).load(Constant.SERVER_ADDRESS+"/"+item.image_link).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                manageNotification(getApplicationContext().getResources().getDrawable(R.drawable.icon_commander), item);
              /* still launch notification with default image */
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                String good = "";
                good = "8993 -- cool one ";
                manageNotification(resource, item);
                /* send notification */
                return false;
            }
        }).submit();
        /* according to different types, should handle it differntly */
    }

    private void manageNotification(final Drawable resource, final NotificationItem item) {

             /* launch the notification */
                /* first download the image */
                /* when the image is ready, */
                /* the require json data for the action on the net, then do it. */

        NetworkRequestThreadBase networkRequestThreadBase =  ((MyKabaApp)getApplicationContext()).getNetworkRequestBase();

                /* get token */
        String token =  ((MyKabaApp) getApplicationContext()).getAuthToken();

        JSONObject object = null;

        switch (item.destination.type) {

            case NotificationItem.NotificationFDestination.ARTICLE_DETAILS:
                NotificationBuilder.sendArticleDetails_N(getApplication(), ""+item.destination.product_id, ARTICLE_MENU_SMALL_ICON, getBitmapFromDrawable(resource),
                        item.title, item.body, item.priority);
                break;
            case NotificationItem.NotificationFDestination.FOOD_DETAILS:
                object = new JSONObject();
                try {
                    object.put("food_id", item.destination.product_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                networkRequestThreadBase.postJsonDataWithToken(Config.LINK_NOTIFICATION_FOOD_DATA, object.toString(), token, new NetworkRequestThreadBase.NetRequestIntf<String>() {
                    @Override
                    public void onNetworkError() {}

                    @Override
                    public void onSysError() {}

                    @Override
                    public void onSuccess(String jsonResponse) {
                        jsonData = jsonResponse;

                        Log.d(Constant.APP_TAG, jsonResponse);

                        NotificationBuilder.sendFood_N(getApplication(), jsonData, RESTAURANT_SMALL_ICON, getBitmapFromDrawable(resource),
                                item.title, item.body, item.priority);
                    }
                });
                break;
            case NotificationItem.NotificationFDestination.RESTAURANT_MENU:
                object = new JSONObject();
                try {
                    object.put("menu_id", item.destination.product_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                networkRequestThreadBase.postJsonDataWithToken(Config.LINK_NOTIFICATION_MENU_DATA, object.toString(), token, new NetworkRequestThreadBase.NetRequestIntf<String>() {
                    @Override
                    public void onNetworkError() {}

                    @Override
                    public void onSysError() {}

                    @Override
                    public void onSuccess(String jsonResponse) {
                        jsonData = jsonResponse;

                        NotificationBuilder.sendRestaurantMenu_N(getApplication(), jsonData, RESTAURANT_MENU_SMALL_ICON, getBitmapFromDrawable(resource),
                                item.title, item.body, item.priority);
                    }
                });
                break;
            case NotificationItem.NotificationFDestination.RESTAURANT_PAGE:
                object = new JSONObject();
                try {
                    object.put("restaurant_id", item.destination.product_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                networkRequestThreadBase.postJsonDataWithToken(Config.LINK_NOTIFICATION_RESTAURANT_DATA, object.toString(), token, new NetworkRequestThreadBase.NetRequestIntf<String>() {
                    @Override
                    public void onNetworkError() {}

                    @Override
                    public void onSysError() {}

                    @Override
                    public void onSuccess(String jsonResponse) {
                        jsonData = jsonResponse;

                        NotificationBuilder.sendRestaurantPage_N(getApplication(), jsonData, RESTAURANT_MENU_SMALL_ICON, getBitmapFromDrawable(resource),
                                item.title, item.body, item.priority);
                    }
                });
                break;
            case NotificationItem.NotificationFDestination.COMMAND_PREPARING:
                /* get command product id */
                Log.d(Constant.APP_TAG, "COMMAND_PREPARING");
                NotificationBuilder.sendCommandNotification(getApplicationContext(), NotificationItem.NotificationFDestination.COMMAND_PREPARING, item.destination.product_id);
                break;
            case NotificationItem.NotificationFDestination.COMMAND_CANCELLED:
                Log.d(Constant.APP_TAG, "COMMAND_CANCELLED");
                NotificationBuilder.sendCommandNotification(getApplicationContext(),NotificationItem.NotificationFDestination.COMMAND_CANCELLED, item.destination.product_id);
                break;
            case NotificationItem.NotificationFDestination.COMMAND_END_SHIPPING:
                Log.d(Constant.APP_TAG, "COMMAND_END_SHIPPING");
                NotificationBuilder.sendCommandNotification(getApplicationContext(),NotificationItem.NotificationFDestination.COMMAND_END_SHIPPING, item.destination.product_id);
                break;
            case NotificationItem.NotificationFDestination.COMMAND_REJECTED:
                Log.d(Constant.APP_TAG, "COMMAND_REJECTED");
                NotificationBuilder.sendCommandNotification(getApplicationContext(),NotificationItem.NotificationFDestination.COMMAND_REJECTED, item.destination.product_id);
                break;
            case NotificationItem.NotificationFDestination.COMMAND_SHIPPING:
                Log.d(Constant.APP_TAG, "COMMAND_SHIPPING");
                NotificationBuilder.sendCommandNotification(getApplicationContext(),NotificationItem.NotificationFDestination.COMMAND_SHIPPING, item.destination.product_id);
                break;
            case NotificationItem.NotificationFDestination.MONEY_MOVMENT:
                Log.d(Constant.APP_TAG, "MONEY_MOVMENT");
                String message = ""; /* - > ; + < rechargment ; remboursement ? */
                NotificationBuilder.sendMoneyMovmentNotification(getApplicationContext(),NotificationItem.NotificationFDestination.MONEY_MOVMENT, message);
                break;
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {

        return  ((BitmapDrawable)drawable).getBitmap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toast("All work complete");
    }

    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override public void run() {
                Toast.makeText(KabaNotificationJobService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
