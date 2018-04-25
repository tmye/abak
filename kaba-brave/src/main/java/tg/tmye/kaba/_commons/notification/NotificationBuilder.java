package tg.tmye.kaba._commons.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.Web.WebActivity;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.config.Constant;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * By abiguime on 24/12/2017.
 * email: 2597434002@qq.com
 */

public class NotificationBuilder {

    /**
     * Creating notifications for different
     *
     * - simple notifications (pict+text+icon), redirect to a webpage
     * - food notification (...), redirect to a food and allow us to have reduction
     * - restaurant notification (), redirect to the home page of a restaurant
     * - notif about a command (redirect to the command details / to the command page)
     * - redirect to a certain activity ()
     *
     */

    /**
     *  open webpage with link and name
     */
    public static void sendOpenWebpageNotification (Context ctx, String name, String content, String link) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx, Constant.CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.stat_notify_voicemail)
                        .setContentTitle(name)
                        .setContentText(content);

        /* define notification action */
        Intent resultIntent = new Intent(ctx, WebActivity.class);
        resultIntent.putExtra(WebActivity.DATA, link);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 101;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


    /**
     *  open webpage with link and name
     */
    public static void sendFoodDetailsNotification (Context ctx, String name, String content, long food_id) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(android.R.drawable.stat_notify_voicemail)
                        .setContentTitle(name)
                        .setContentText(content);

        Intent resultIntent = new Intent(ctx, FoodDetailsActivity.class);
        resultIntent.putExtra(FoodDetailsActivity.FOOD, food_id);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 102;
        NotificationManager mNotifyMgr =
                (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    /* restaurant home page */
    public static void sendShowRestaurantPageNotification (Context ctx, String name, String content, int restaurant_id) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(android.R.drawable.stat_notify_voicemail)
                        .setContentTitle(name)
                        .setContentText(content);

        /* define notification action */
        Intent resultIntent = new Intent(ctx, RestaurantMenuActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 103;
        NotificationManager mNotifyMgr =
                (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
