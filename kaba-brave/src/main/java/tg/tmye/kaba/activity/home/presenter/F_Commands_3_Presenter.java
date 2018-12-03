package tg.tmye.kaba.activity.home.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.home.contracts.F_CommandContract;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.data.command.source.CommandRepository;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class F_Commands_3_Presenter implements F_CommandContract.Presenter {


    private final CommandRepository commandRepository;

    private final F_CommandContract.View command3View;

    private boolean isPopulateRunning = false;
    private Gson gson = new Gson();

    public F_Commands_3_Presenter(CommandRepository commandRepository,
                                  F_CommandContract.View command3View) {

        this.commandRepository = commandRepository;
        this.command3View = command3View;

        command3View.setPresenter((F_CommandContract.Presenter) this);
    }


    @Override
    public void start() {

        populateViews();
    }

    private void populateViews() {

        if (isPopulateRunning)
            return;

        isPopulateRunning = true;
        /* populate command list */
        /* populate restaurant list */

        command3View.showErrorPage(false);
        command3View.showIsLoading(true);

        commandRepository.getUpdateCommandList(new NetworkRequestThreadBase.AuthNetRequestIntf<String>(){

            @Override
            public void onNetworkError() {
                command3View.showIsLoading(false);
                command3View.networkError();
//                command3View.showErrorPage(true);
                isPopulateRunning = false;
            }

            @Override
            public void onSysError() {
                command3View.showErrorPage(true);
                command3View.showIsLoading(false);
                isPopulateRunning = false;
            }

            @Override
            public void onSuccess(String jsonResponse) {

                Log.d(Constant.APP_TAG, jsonResponse);

                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    int error = obj.get("error").getAsInt();
                    if (error == -1) {
                        command3View.showIsLoading(false);
                        command3View.inflateCommandList(null);
                    } else {
                        JsonObject data = obj.get("data").getAsJsonObject();
                        // get daily restaurants
                        Command[] commands =
                                gson.fromJson(data.get("commands"), new TypeToken<Command[]>() {
                                }.getType());
                        List<Command> commandList = Arrays.asList(commands);
                        command3View.showIsLoading(false);
                        command3View.inflateCommandList(commandList);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    command3View.showIsLoading(false);
                    command3View.showErrorPage(true);
                }
                isPopulateRunning = false;
            }

            @Override
            public void onLoggingTimeout() {

                command3View.onLoggingTimeout();
            }
        });
    }

    @Override
    public void update() {
        populateViews();
    }
}
