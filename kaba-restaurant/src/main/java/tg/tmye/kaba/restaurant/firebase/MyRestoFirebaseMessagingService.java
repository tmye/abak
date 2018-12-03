package tg.tmye.kaba.restaurant.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import tg.tmye.kaba.restaurant._commons.notification.NotificationItem;
import tg.tmye.kaba.restaurant._commons.notification.RestaurantKabaNotificationJobService;
import tg.tmye.kaba.restaurant.syscore.Constant;

/**
 * By abiguime on 30/05/2018.
 * email: 2597434002@qq.com
 */

public class MyRestoFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyRestoFirebaseMessagingService";
    private Gson gson = new Gson();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /*
        * */
        Log.d(Constant.APP_TAG, "here we are boss");

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(Constant.APP_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(Constant.APP_TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(Constant.APP_TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String body = remoteMessage.getData().get("data");

        /* create notification item - and make him handle it */

        /* DEPENDING ON THE TYPE OF NOFICATIONS, SOMETHING DIFFERENT IS DONE */

        JsonObject obj = new JsonParser().parse(body).getAsJsonObject();

        JsonObject data = obj.get("data").getAsJsonObject();

        NotificationItem notificationItem =
                gson.fromJson(data.get("notification"), new TypeToken<NotificationItem>(){}.getType());

        Intent intent = new Intent();
        intent.putExtra("data", notificationItem);

        RestaurantKabaNotificationJobService.enqueueWork(this, RestaurantKabaNotificationJobService.class,
                RestaurantKabaNotificationJobService.JOB_ID, intent);
    }
    // [END receive_message]

}
