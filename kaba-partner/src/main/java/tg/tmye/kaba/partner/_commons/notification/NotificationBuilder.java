package tg.tmye.kaba.partner._commons.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner.activities.commands.CommandDetailsActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;
import static tg.tmye.kaba.partner.syscore.Constant.CHANNEL_ID;
import static tg.tmye.kaba.partner.syscore.Constant.CHANNEL_NAME;

/**
 * By abiguime on 24/12/2017.
 * email: 2597434002@qq.com
 */

public class NotificationBuilder {


    private static final int LOW = 1;
    private static final int MODERATE = 2;
    private static final int HIGH = 3;
    private static final int COMMAND_NORMAL = 4;
    private static final int COMMAND_DELIVERYING = 5;
    private static final int VERY_HIGH = 6;
    private static final int BAD_NEWS = -1;
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


    private static Gson gson = new Gson();


    public int FOOD_PRODUCT;



    /* */

    public static void sendCommandPage_N(Context ctx, String jsonData) {

    }

    public static void sendCommandDetails_N(Context ctx, String jsonData) {

    }

    /* ultimate function */
   /* private static void sendNotification(Context ctx, int smallIconId, Bitmap largeIconBitmap, String title, String body, Intent intent, int prority) {

        *//* add priority
         *
         * - LOW
         * - MODERATE
         * - HIGH
         * - COMMAND_NORMAL
         * - COMMAND_DELIVERYING
         * - VERY_HIGH
          * *//*

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(smallIconId)
                        .setLargeIcon(largeIconBitmap)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

        //Define sound URI
        Uri soundUri;

        switch (prority) {
            case LOW:
                break;
            case MODERATE:
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(soundUri);
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
                break;
            case HIGH:
                mBuilder.setSound(Uri.parse("android.resource://"+ctx.getPackageName()+"/"+ R.raw.high_notif_slapping_three_faces));
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                break;
            case COMMAND_DELIVERYING:
                // on deliverying
                mBuilder.setSound(Uri.parse("android.resource://"+ctx.getPackageName()+"/"+ R.raw.deliverying));
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                break;
            case VERY_HIGH:
                *//* new very high notification *//*
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(soundUri);
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                break;
            case BAD_NEWS:
                // on deliverying
                mBuilder.setSound(Uri.parse("android.resource://"+ctx.getPackageName()+"/"+ R.raw.bad_news_clang_and_wobble));
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
                break;
        }

        *//* cannal of the command notifications must be the same as the command id *//*

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);




        int mNotificationId = NotificationItem.NotificationFDestination.FOOD_DETAILS;
        NotificationManager mNotifyMgr =
                (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
*/


    /* ultimate function */
    private static void sendNotification(Context ctx, int smallIconId, Bitmap largeIconBitmap, String title, String body, Intent intent, int prority, @Nullable String bigText) {

        /* add priority
         *
         * - LOW
         * - MODERATE
         * - HIGH
         * - COMMAND_NORMAL
         * - COMMAND_DELIVERYING
         * - VERY_HIGH
         * */

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx, CHANNEL_ID)
                        .setSmallIcon(smallIconId)
                        .setLargeIcon(largeIconBitmap)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVisibility(VISIBILITY_PUBLIC);

        if (bigText != null) {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));
        }

        //Define sound URI
        Uri soundUri;
        int priority = NotificationCompat.PRIORITY_DEFAULT;

        switch (prority) {
            case LOW:
                break;
            case MODERATE:
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(soundUri);
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
                break;
            case HIGH:
                mBuilder.setSound(Uri.parse("android.resource://"+ctx.getPackageName()+"/"+ R.raw.high_notif_slapping_three_faces));
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                priority = NotificationCompat.PRIORITY_HIGH;
                mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                break;
            case COMMAND_NORMAL:
                // nothing
                break;
            case COMMAND_DELIVERYING:
                // on deliverying
                mBuilder.setSound(Uri.parse("android.resource://"+ctx.getPackageName()+"/"+ R.raw.deliverying));
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                priority = NotificationCompat.PRIORITY_HIGH;
                break;
            case VERY_HIGH:
                /* new very high notification */
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(soundUri);
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
                priority = NotificationCompat.PRIORITY_MAX;
                break;
            case BAD_NEWS:
                // on deliverying
                mBuilder.setSound(Uri.parse("android.resource://"+ctx.getPackageName()+"/"+ R.raw.bad_news_clang_and_wobble));
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
                priority = NotificationCompat.PRIORITY_HIGH;
                break;
        }

        /* cannal of the command notifications must be the same as the command id */

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = NotificationItem.NotificationFDestination.FOOD_DETAILS;
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);

        /*  */
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            mNotificationManager.notify(mNotificationId, mBuilder.build());
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mBuilder.setChannelId(CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
            mNotificationManager.notify(mNotificationId, mBuilder.build());
        }
    }


    public static void sendCommandNotification(Context context, int command_state, int product_id) {

        Intent intent = new Intent(context, CommandDetailsActivity.class);
        intent.putExtra(CommandDetailsActivity.ID, product_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        int smallIconId = R.drawable.notification_kaba_drawable;
        Bitmap largeIconBitmap = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.icon_my_command)).getBitmap();
        String title = context.getResources().getString(R.string.app_name);
        String body = "";
        int priority = 0;

        switch (command_state) {
            case  NotificationItem.NotificationFDestination.NEW_COMMAND:
                body = context.getResources().getString(R.string.command_new);
                priority = HIGH; // won't never happend
                break;
            case  NotificationItem.NotificationFDestination.COMMAND_END_SHIPPING:
                body = context.getResources().getString(R.string.command_end_shipping);
                priority = MODERATE;
                break;
        }

        sendNotification (context, smallIconId, largeIconBitmap, title, body, intent, priority, null);
    }


}
