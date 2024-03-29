package tg.tmye.kaba.partner._commons.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.JobIntentService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.partner.syscore.Constant;
import tg.tmye.kaba.partner.syscore.MyRestaurantApp;


/**
 * By abiguime on 25/04/2018.
 * email: 2597434002@qq.com
 */

public class RestaurantKabaNotificationJobService extends JobIntentService {

    /**
     * Unique job ID for this service.
     */
    public static final int JOB_ID = 1995;

    static final String TAG = "RestaurantKabaNotificationJobService";
    private static final int RESTAURANT_MENU_SMALL_ICON = R.drawable.ic_refresh_icon;
    private static final int RESTAURANT_SMALL_ICON = R.drawable.ic_refresh_icon;
    private static final int ARTICLE_MENU_SMALL_ICON = R.drawable.ic_refresh_icon;

    String jsonData = "";

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RestaurantKabaNotificationJobService.class, JOB_ID, work);
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
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

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

        NetworkRequestThreadBase networkRequestThreadBase =  ((MyRestaurantApp)getApplicationContext()).getNetworkRequestBase();

        /* get token */
        String token =  ((MyRestaurantApp) getApplicationContext()).getAuthToken();

        JSONObject object = null;

        switch (item.destination.type) {

            case NotificationItem.NotificationFDestination.NEW_COMMAND:
                /* get command product id */
                NotificationBuilder.sendCommandNotification(getApplicationContext(), NotificationItem.NotificationFDestination.NEW_COMMAND, item.destination.product_id);
                break;
            case NotificationItem.NotificationFDestination.COMMAND_END_SHIPPING:
                /* get command product id */
                NotificationBuilder.sendCommandNotification(getApplicationContext(), NotificationItem.NotificationFDestination.COMMAND_END_SHIPPING, item.destination.product_id);
                break;
            case NotificationItem.NotificationFDestination.COMMAND_DETAILS:
                /* get command product id */
                NotificationBuilder.sendCommandNotification(getApplicationContext(), NotificationItem.NotificationFDestination.COMMAND_DETAILS, item.destination.product_id);
                break;
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {

        return  ((BitmapDrawable)drawable).getBitmap();
    }


    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override public void run() {
                Toast.makeText(RestaurantKabaNotificationJobService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
