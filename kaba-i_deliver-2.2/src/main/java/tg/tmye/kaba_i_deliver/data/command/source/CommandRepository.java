package tg.tmye.kaba_i_deliver.data.command.source;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba_i_deliver._commons.MultiThreading.DatabaseRequestThreadBase;
import tg.tmye.kaba_i_deliver._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.delivery.KabaShippingMan;
import tg.tmye.kaba_i_deliver.syscore.Config;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;


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
        networkRequestBase = ((MyKabaDeliverApp) context.getApplicationContext()).getNetworkRequestBase();
        databaseRequestThreadBase = ((MyKabaDeliverApp) context.getApplicationContext()).getDatabaseRequestThreadBase();
    }

    public void getUpdateCommandList(NetworkRequestThreadBase.NetRequestIntf intf) {

        /* send back a map that contains restaurants as a key, and list of basketItem as value */


        /* give my token also */
       /* Map<String, Object> map = new HashMap<String, Object>();
        String token = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();

        ((MyKabaDeliverApp)context.getApplicationContext()).getNetworkRequestBase().postMapDataWithToken(
                Config.LINK_MY_COMMANDS_GET_CURRENT, map, token, intf
        );
*/
    }

    public void getRestaurantCommands_Deliver(NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        /* make the request being aware of id of the restaurant / token */

        Map<String, Object> map = new HashMap<>();
        String token = ((MyKabaDeliverApp) context.getApplicationContext()).getAuthToken();
        networkRequestBase.postMapDataWithToken(
                Config.LINK_DELIVERYMAN_GET_MY_COMMANDS, map, token, intf
        );
    }

    public void getCommandDetails(String command_id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("command_id",command_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_GET_COMMAND_DETAILS, command_obj.toString(), authToken, intf);
    }

    public void acceptCommand(Command command, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("id",command.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        networkRequestBase.postJsonDataWithToken(Config.LINK_ACCEPT_COMMAND, command_obj.toString(), authToken, intf);
    }


    public void setShippingToDone (int id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_SET_COMMAND_TO_SHIPPING, command_obj.toString(), authToken, intf);
    }

    public void startShipping(int id, NetworkRequestThreadBase.NetRequestIntf<String> intf) {

        String authToken = ((MyKabaDeliverApp)context.getApplicationContext()).getAuthToken();
        JSONObject command_obj = new JSONObject();
        try {
            command_obj.put("command_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        networkRequestBase.postJsonDataWithToken(Config.LINK_START_SHIPPING, command_obj.toString(), authToken, intf);
    }
}
