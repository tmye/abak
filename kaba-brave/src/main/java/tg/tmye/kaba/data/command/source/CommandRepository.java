package tg.tmye.kaba.data.command.source;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Food_TagDao;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntityDao;
import tg.tmye.kaba.data.Restaurant.RestaurantEntityDao;
import tg.tmye.kaba.data._OtherEntities.Error;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class CommandRepository {

    private final Context context;

    Gson gson = new Gson();

    public CommandRepository (Context context) {
        this.context = context;
    }

    public void getUpdateCommandList(final YesOrNoWithResponse yesOrNoWithResponse) {

        /* send back a map that contains restaurants as a key, and list of basketItem as value */
        ((MyKabaApp)context.getApplicationContext()).getNetworkRequestBase().run(
                Config.LINK_MY_COMMANDS, new NetworkRequestThreadBase.NetRequestIntf() {
                    @Override
                    public void onNetworkError() {
                        yesOrNoWithResponse.no(new Error.NetworkError(), true);
                    }

                    @Override
                    public void onSysError() {
                        yesOrNoWithResponse.no(new Error.LocalError(), true);
                    }

                    @Override
                    public void onSuccess(String jsonResponse) {

                        // work on the data, and get it to the view
//                        yesOrNoWithResponse.yes(Command.fakeList(6));
                        /* inflate the result into a list of things */
                        try {
                            JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                            JsonObject data = obj.get("data").getAsJsonObject();
                            // get daily restaurants
                            Command[] commands =
                                    gson.fromJson(data.get("commands"), new TypeToken<Command[]>() {
                                    }.getType());
                            List<Command> commandList = Arrays.asList(commands);

                            /* complete whatever we  have to complete like fetching data from local database and so on */
                            RestaurantEntityDao restaurantEntityDao = ((MyKabaApp)context.getApplicationContext()).getDaoSession().getRestaurantEntityDao();
                            Restaurant_Menu_FoodEntityDao foodEntityDao = ((MyKabaApp)context.getApplicationContext()).getDaoSession().getRestaurant_Menu_FoodEntityDao();
                            Food_TagDao foodTagDao = ((MyKabaApp)context.getApplicationContext()).getDaoSession().getFood_TagDao();

                            for (int i = 0; i < commandList.size(); i++) {
                                commandList.set(i,commandList.get(i).fetchAll(restaurantEntityDao, foodEntityDao, foodTagDao));
                            }

                            yesOrNoWithResponse.yes(commandList, true);
                        } catch (Exception e){
                            e.printStackTrace();
                            yesOrNoWithResponse.no(new Error.LocalError(), true);
                        }
                    }
                }
        );

    }
}
