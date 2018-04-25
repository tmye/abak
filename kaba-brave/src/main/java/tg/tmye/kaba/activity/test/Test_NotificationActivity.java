package tg.tmye.kaba.activity.test;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.notification.KabaNotificationJobService;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.syscore.GlideApp;


public class Test_NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void menu_update(View view) {

        /* Title Wing's Shake*/

        /* GO get our menu! */

    }

    public void new_article(View view) {

        /* Wings'n shake */

        /* this is our new article */
    }

    public void resto_promotion(View view) {

        /* restaurant_promotion - please go check! */
    }

    public void command_state(View view) {

        /* your command  has entered the delivery process */

    }

    public void simple(View view) {

        /* you can get a job that downloads the icons, and that
        * sends the notification once done. */
        /* just image, title - text */

        /* parse the json text */

        Intent intent =  new Intent();
        /* title text */
        intent.putExtra("title", "Astalavi...");
        /* picture link */
        intent.putExtra("pic_link", "");
        /* body text */
        intent.putExtra("body", "");

        /* try to put a destination activity */
        /* create an action item that brings to a different acitivty */

        KabaNotificationJobService.enqueueWork(this, KabaNotificationJobService.class,
                KabaNotificationJobService.JOB_ID, intent);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.stat_notify_voicemail)
                        .setContentTitle("W'nShake got you twisted")
                        .setContentText("Innoubliable evenement. A ne surtout pas manquer!");

        Intent resultIntent = new Intent(this, FoodDetailsActivity.class);

//        resultIntent.putExtra(FoodDetailsActivity.FOOD, food_id);
//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        ctx,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//        mBuilder.setContentIntent(resultPendingIntent);
//        int mNotificationId = 102;

        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1994, mBuilder.build());
    }


        /*
            The most hard thing to work on,
             is the action right after we've clicked
             on the activity.

            Possible actions will be:
                - open restaurant _ menu / activity
                - open food / product details activity
                - open little article about promotions or activity or whatsoever
                - open command details activity
          */


}
