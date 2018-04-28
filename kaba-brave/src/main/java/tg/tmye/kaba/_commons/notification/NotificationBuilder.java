package tg.tmye.kaba._commons.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.Web.WebActivity;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;

import static android.content.Context.NOTIFICATION_SERVICE;
import static tg.tmye.kaba.activity.menu.RestaurantMenuActivity.RESTAURANT_ITEM_RESULT;

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


    private static Gson gson = new Gson();


    public int FOOD_PRODUCT;

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


    /* */

    public static void sendCommandPage_N(Context ctx, String jsonData) {

    }

    public static void sendArticleDetails_N(Context ctx, String jsonData) {

    }

    public static void sendCommandDetails_N(Context ctx, String jsonData) {

    }

    public static void sendFood_N(Context ctx, String jsonData, int smallIconId, Bitmap largeIconBitmap, String title, String body) {

        /* parse data and the hole data that is coming with it . */

        /* food - restaurant_entity - restaurant drinks */

        JsonObject obj = new JsonParser().parse(jsonData).getAsJsonObject();
        JsonObject data = obj.get("data").getAsJsonObject();
        RestaurantEntity restaurantEntity =
                gson.fromJson(data.get("restaurant"), new TypeToken<RestaurantEntity>(){}.getType());

        Restaurant_Menu_FoodEntity restaurantMenuFoodEntity =
                gson.fromJson(data.get("food"), new TypeToken<Restaurant_Menu_FoodEntity>(){}.getType());

       /* Restaurant_Menu_FoodEntity[] drink_list =
                gson.fromJson(data.get("restaurant_drinks"), new TypeToken<Restaurant_Menu_FoodEntity[]>(){}.getType());

        List<Restaurant_Menu_FoodEntity> drinksEntities = Arrays.asList(drink_list);*/

        /* start activity with this */

        Intent intent = new Intent(ctx, FoodDetailsActivity.class);
        intent.putExtra(FoodDetailsActivity.FOOD
                , restaurantMenuFoodEntity);
        intent.putExtra(FoodDetailsActivity.RESTAURANT_ENTITY, restaurantEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        sendNotification (ctx, smallIconId, largeIconBitmap, title, body, intent);
    }

    private static void sendNotification(Context ctx, int smallIconId, Bitmap largeIconBitmap, String title, String body, Intent intent) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(smallIconId)
                        .setLargeIcon(largeIconBitmap)
                        .setContentTitle(title)
                        .setContentText(body);

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

    public static void sendRestaurantMenu_N(Context ctx, String jsonData) {

    }

    public static void sendRestaurantPage_N(Context ctx, String jsonData) {

    }
}
