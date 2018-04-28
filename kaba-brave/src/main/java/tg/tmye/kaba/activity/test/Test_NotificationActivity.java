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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.notification.KabaNotificationJobService;
import tg.tmye.kaba._commons.notification.NotificationItem;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;
import tg.tmye.kaba.syscore.GlideApp;


public class Test_NotificationActivity extends AppCompatActivity {

    private Gson gson = new Gson();

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

        JSONObject destinationObject = new JSONObject();
        JSONObject object = new JSONObject();
        JSONObject resp = new JSONObject();
        try {


            destinationObject.put("type", 373);
            destinationObject.put("product_id", 331);

            object.put("title", "Alleluia");
            object.put("image_link", "/menu_pic/menu_591524743606.jpg");
            object.put("body", "Here comes the news of the century!");
            object.put("destination", destinationObject);


            resp.put("message", "no message for you.");
            resp.put("error", 0);
            resp.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonResponse = resp.toString();

        /* deserialize object and add it to the intent */
        JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
        JsonObject data = obj.get("data").getAsJsonObject();
        NotificationItem notificationItem =
                gson.fromJson(data, new TypeToken<NotificationItem>(){}.getType());


        Intent intent = new Intent();
        intent.putExtra("data", notificationItem);

        /* try to put a destination activity */
        /* create an action item that brings to a different acitivty */

        KabaNotificationJobService.enqueueWork(this, KabaNotificationJobService.class,
                KabaNotificationJobService.JOB_ID, intent);

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
