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

                String good = "";
                good = "8993";
              /* still launch notification with default image */
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                String good = "";
                good = "8993 -- cool one ";

                /* launch the notification */
                /* first download the image */
                /* when the image is ready, */
                /* the require json data for the action on the net, then do it. */

                NetworkRequestThreadBase networkRequestThreadBase =  ((MyKabaApp)getApplicationContext()).getNetworkRequestBase();

                /* get token */
                String token =  ((MyKabaApp) getApplicationContext()).getAuthToken();


                switch (item.destination.type) {

                    case NotificationItem.NotificationFDestination.ARTICLE_DETAILS:
                        NotificationBuilder.sendArticleDetails_N (getApplicationContext(), jsonData);
                        break;
                    case NotificationItem.NotificationFDestination.COMMAND_DETAILS:
                        NotificationBuilder.sendCommandDetails_N (getApplicationContext(), jsonData);
                        break;
                    case NotificationItem.NotificationFDestination.COMMAND_PAGE:
                        NotificationBuilder.sendCommandPage_N (getApplicationContext(), jsonData);
                        break;
                    case NotificationItem.NotificationFDestination.FOOD_DETAILS:
                        JSONObject object = new JSONObject();
                        try {
                            object.put("food_id", item.destination.product_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        networkRequestThreadBase.postJsonDataWithToken(Config.LINK_NOTIFICATION_FOOD_DATA, object.toString(), token, new NetworkRequestThreadBase.NetRequestIntf<String>() {
                            @Override
                            public void onNetworkError() {

                            }

                            @Override
                            public void onSysError() {

                            }

                            @Override
                            public void onSuccess(String jsonResponse) {
                                jsonData = jsonResponse;

                                NotificationBuilder.sendFood_N(getApplication(), jsonData, R.drawable.ic_refresh_icon, getBitmapFromDrawable(resource),
                                        item.title, item.body);
                            }
                        });


                        break;
                    case NotificationItem.NotificationFDestination.RESTAURANT_MENU:
                        NotificationBuilder.sendRestaurantMenu_N (getApplicationContext(), jsonData);
                        break;
                    case NotificationItem.NotificationFDestination.RESTAURANT_PAGE:
                        NotificationBuilder.sendRestaurantPage_N (getApplicationContext(), jsonData);
                        break;
                }


                /* send notification */


                return false;
            }
        }).submit();
        /* according to different types, should handle it differntly */

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
