package tg.tmye.kaba.restaurant.data.command.source;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba.restaurant._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.data.command.Command;
import tg.tmye.kaba.restaurant.data.delivery.KabaShippingMan;
import tg.tmye.kaba.restaurant.syscore.Config;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;


/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class CommandRepository {

    private final Context context;
    private final NetworkRequestThreadBase networkRequestBase;
    private final DatabaseRequestThreadBase databaseRequestThreadBase;

    Gson gson = new Gson();

    public CommandRepository (Context context) {
        this.context = context;

        /* get threads */
        networkRequestBase = ((MyRestaurantApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyRestaurantApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }

    public void getUpdateCommandList(NetworkRequestThreadBase.NetRequestIntf intf) {

        /* send back a map that contains restaurants as a key, and list of basketItem as value */


        /* give my token also */
       /* Map<String, Object> map = new HashMap<String, Object>();
        String token = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();

        ((MyRestaurantApp)context.getApplicationContext()).getNetworkRequestBase().postMapDataWithToken(
                Config.LINK_MY_COMMANDS_GET_CURRENT, map, token, intf
        );
*/
    }

    public void getRestaurantCommands(NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        /* make the request being aware of id of the restaurant / token */

        Map<String, Object> map = new HashMap<>();
        String token = ((MyRestaurantApp) context.getApplicationContext()).getAuthToken();
        networkRequestBase.postMapDataWithToken(
                Config.LINK_RESTAURANT_GET_MY_COMMANDS, map, token, intf
        );
    }

    public void getCommandDetails(String command_id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("command_id",command_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_GET_COMMAND_DETAILS, command_obj.toString(), authToken, intf);
    }

    public void acceptCommand(Command command, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("id",command.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_ACCEPT_COMMAND, command_obj.toString(), authToken, intf);
    }

    public void sendCommandToShipping(Command command, KabaShippingMan shippingMan, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("id",command.id);
            command_obj.put("livreur_id", shippingMan.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_SENDTOSHIPPING_COMMAND, command_obj.toString(), authToken, intf);
    }

    public void loadStats(NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject obj = new JSONObject();

        networkRequestBase.postJsonDataWithToken(Config.LINK_GET_RESTAURANT_STATS, obj.toString(), authToken, intf);
    }

    public void cancelCommand(int command_id, int motif, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject params = new JSONObject();
        try {
            params.put("id",command_id);
            params.put("motif", motif);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_RESTAURANT_CANCEL_COMMAND, params.toString(), authToken, intf);
    }

    public void checkOpened(boolean isOpened, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyRestaurantApp)context.getApplicationContext()).getAuthToken();
        JSONObject params = new JSONObject();
        try {
            params.put("is_opened",isOpened);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_RESTAURANT_CHECK_OPENED, params.toString(), authToken, intf);
    }
}
