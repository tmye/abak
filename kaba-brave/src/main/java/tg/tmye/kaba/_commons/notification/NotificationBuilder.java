package tg.tmye.kaba._commons.notification;

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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.Web.WebActivity;
import tg.tmye.kaba.activity.WebArticle.WebArticleActivity;
import tg.tmye.kaba.activity.command.CommandDetailsActivity;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.activity.restaurant.RestaurantActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;
import static tg.tmye.kaba.config.Constant.CHANNEL_ID;
import static tg.tmye.kaba.config.Constant.CHANNEL_NAME;

/**
 * By abiguime on 24/12/2017.
 * email: 2597434002@qq.com
 */

public class NotificationBuilder {

    private static final int CHANNEL_NOTIFICATION_ID = 1994;

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

    /**
     *  open webpage with link and name
     */
    public static void sendOpenWebpageNotification (Context ctx, String name, String content, String link) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.stat_notify_voicemail)
                        .setContentTitle(name)
                        .setContentText(content);

        /* define notification action */
        Intent resultIntent = new Intent(ctx, WebArticleActivity.class);
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



    /* */

    public static void sendCommandPage_N(Context ctx, String jsonData) {

    }

    public static void sendCommandDetails_N(Context ctx, String jsonData) {

    }

    public static void sendFood_N(Context ctx, String jsonData, int smallIconId, Bitmap largeIconBitmap, String title, String body, int priority) {

        /* parse data and the hole data that is coming with it . */
        /* food - restaurant_entity - restaurant drinks */

        JsonObject obj = new JsonParser().parse(jsonData).getAsJsonObject();
        JsonObject data = obj.get("data").getAsJsonObject();
        RestaurantEntity restaurantEntity =
                gson.fromJson(data.get("restaurant"), new TypeToken<RestaurantEntity>(){}.getType());

        Restaurant_Menu_FoodEntity restaurantMenuFoodEntity =
                gson.fromJson(data.get("food"), new TypeToken<Restaurant_Menu_FoodEntity>(){}.getType());

        Intent intent = new Intent(ctx, FoodDetailsActivity.class);
        intent.putExtra(FoodDetailsActivity.FOOD
                , restaurantMenuFoodEntity);
        intent.putExtra(FoodDetailsActivity.RESTAURANT_ENTITY, restaurantEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String bigText = body;
        /* details of the food */


        sendNotification (ctx, smallIconId, largeIconBitmap, title, body, intent, priority, bigText);
    }

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
            int importance = priority;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mBuilder.setChannelId(CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
            mNotificationManager.notify(mNotificationId, mBuilder.build());
        }
    }


    public static void sendRestaurantMenu_N(Context ctx, String jsonData, int smallIconId, Bitmap largeIconBitmap, String title, String body, int priority) {

        /* parse data and the hole data that is coming with it . */
        /* food - restaurant_entity - restaurant drinks */
        Log.d(Constant.APP_TAG, jsonData);

        JsonObject obj = new JsonParser().parse(jsonData).getAsJsonObject();
        JsonObject data = obj.get("data").getAsJsonObject();
        RestaurantEntity restaurantEntity =
                gson.fromJson(data.get("restaurant"), new TypeToken<RestaurantEntity>(){}.getType());

        /* start activity with this */

        Intent intent = new Intent(ctx, RestaurantMenuActivity.class);
        intent.putExtra(RestaurantMenuActivity.RESTAURANT
                , restaurantEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String bigText = body;

        sendNotification (ctx, smallIconId, largeIconBitmap, title, body, intent, priority, bigText);
    }



    public static void sendRestaurantPage_N (Context ctx, String jsonData, int smallIconId, Bitmap largeIconBitmap, String title, String body, int priority) {

        /* parse data and the hole data that is coming with it . */

        /* food - restaurant_entity - restaurant drinks */
        Log.d(Constant.APP_TAG, jsonData);

        JsonObject obj = new JsonParser().parse(jsonData).getAsJsonObject();
        JsonObject data = obj.get("data").getAsJsonObject();
        RestaurantEntity restaurantEntity =
                gson.fromJson(data.get("restaurant"), new TypeToken<RestaurantEntity>(){}.getType());

        /* start activity with this */

        Intent intent = new Intent(ctx, RestaurantActivity.class);
        intent.putExtra(RestaurantMenuActivity.RESTAURANT
                , restaurantEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String bigText = body;

        sendNotification (ctx, smallIconId, largeIconBitmap, title, body, intent, priority, bigText);
    }



    public static void sendArticleDetails_N (Context ctx, String product_id, int smallIconId, Bitmap largeIconBitmap, String title, String body, int priority) {
        /* parse data and the hole data that is coming with it . */

        /* food - restaurant_entity - restaurant drinks */
        Intent intent = new Intent(ctx, WebArticleActivity.class);
        intent.putExtra(WebArticleActivity.ARTICLE_ID
                , Integer.valueOf(product_id));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // set priority of this
        String bigText = body;

        sendNotification (ctx, smallIconId, largeIconBitmap, title, body, intent, priority, bigText);
    }

    public static void sendCommandNotification(Context ctx, int command_state, int product_id) {

        Intent intent = new Intent(ctx, CommandDetailsActivity.class);
        intent.putExtra(CommandDetailsActivity.ID, product_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        int smallIconId = R.drawable.notification_kaba_drawable;
        Bitmap largeIconBitmap = ((BitmapDrawable)ctx.getResources().getDrawable(R.drawable.icon_my_command)).getBitmap();
        String title = ctx.getResources().getString(R.string.app_name);
        String body = "";
        int priority = 0;

        switch (command_state) {
            case  NotificationItem.NotificationFDestination.COMMAND_PREPARING:
                body = ctx.getResources().getString(R.string.command_preparing);
                priority = LOW;
                break;
            case  NotificationItem.NotificationFDestination.COMMAND_CANCELLED:
                body = ctx.getResources().getString(R.string.command_cancelled);
                priority = LOW; // won't never happend
                break;
            case  NotificationItem.NotificationFDestination.COMMAND_END_SHIPPING:
                body = ctx.getResources().getString(R.string.command_end_shipping);
                priority = MODERATE;
                break;
            case  NotificationItem.NotificationFDestination.COMMAND_REJECTED:
                body = ctx.getResources().getString(R.string.command_rejected);
                priority = BAD_NEWS;
                break;
            case  NotificationItem.NotificationFDestination.COMMAND_SHIPPING:
                body = ctx.getResources().getString(R.string.command_shipping);
                priority = HIGH;
                break;
        }

        String bigText = body;

        sendNotification (ctx, smallIconId, largeIconBitmap, title, body, intent, priority, bigText);
    }


    public static void sendMoneyMovmentNotification(Context context, int moneyMovment, String message) {


    }
}
