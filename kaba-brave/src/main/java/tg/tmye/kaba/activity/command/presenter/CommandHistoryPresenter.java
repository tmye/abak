package tg.tmye.kaba.activity.command.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.activity.command.contract.CommandHistoryContract;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.data.command.source.CommandRepository;

/**
 * By abiguime on 05/07/2018.
 * email: 2597434002@qq.com
 */
public class CommandHistoryPresenter implements CommandHistoryContract.Presenter {


    private final CommandRepository repository;
    private final CommandHistoryContract.View view;

    private Gson gson = new Gson();

    public CommandHistoryPresenter (CommandHistoryContract.View view, CommandRepository repository) {

        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadCommand() {
        loadCommandFrom(0);
    }

    @Override
    public void loadCommandFrom(int from) {

        view.showLoading (true);
        repository.getAllCommandListFrom (from, new NetworkRequestThreadBase.AuthNetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                view.showLoading (false);
                view.onNetworkError();
            }

            @Override
            public void onSysError() {
                view.showLoading (false);
                view.onSysEror();
            }

            @Override
            public void onSuccess(String jsonResponse) {

                view.showLoading (false);

                try {
                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();
                    JsonObject data = obj.get("data").getAsJsonObject();
                    // get daily restaurants
                    Command[] commands =
                            gson.fromJson(data.get("commands"), new TypeToken<Command[]>() {
                            }.getType());
                    List<Command> mCommands = new ArrayList<>(Arrays.asList(commands));
                    view.inflateList(mCommands);
                } catch (Exception e){
                    e.printStackTrace();
                    view.onSysEror();
                }
            }

            @Override
            public void onLoggingTimeout() {
                view.onLoggingTimeout();
            }
        });

    }

    @Override
    public void start() {
    }
}
