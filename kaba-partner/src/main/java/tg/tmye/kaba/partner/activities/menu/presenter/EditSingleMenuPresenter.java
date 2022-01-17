package tg.tmye.kaba.partner.activities.menu.presenter;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.partner.ILog;
import tg.tmye.kaba.partner._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.partner.activities.menu.contract.EditSingleMenuContract;
import tg.tmye.kaba.partner.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.partner.data.Menu.source.MenuDb_OnlineRepository;
import tg.tmye.kaba.partner.data.command.Command;

/**
 * By abiguime on 01/04/2018.
 * email: 2597434002@qq.com
 */

public class EditSingleMenuPresenter implements EditSingleMenuContract.Presenter {

    MenuDb_OnlineRepository menuDb_onlineRepository;
    EditSingleMenuContract.View view;

    public EditSingleMenuPresenter(MenuDb_OnlineRepository menuDb_onlineRepository,
                                   EditSingleMenuContract.View view) {

        /* view, datarepo */
        this.view = view;
        this.menuDb_onlineRepository = menuDb_onlineRepository;
    }

    @Override
    public void start() {

        populateViews();
    }

    @Override
    public void populateViews() {
    }

    @Override
    public void updateMenu(Restaurant_SubMenuEntity subMenuEntity) {

        view.showIsLoading(true);
        menuDb_onlineRepository.updateSubMenuEntity(subMenuEntity, new NetworkRequestThreadBase.NetRequestIntf<String>() {

            @Override
            public void onNetworkError() {
                view.showIsLoading(false);
                view.onNetworkError();
            }

            @Override
            public void onSysError() {
                view.showIsLoading(false);
                view.onSysError();
            }

            @Override
            public void onSuccess(String response) {
                /* send something around here */

                view.showIsLoading(false);

                try {
                    ILog.print(response);
                    JsonObject obj = new JsonParser().parse(response).getAsJsonObject();

                    // get the error object and make sure it's == 0
                    int error = obj.get("error").getAsInt();

                    if (error == 0)
                        view.menuUpdateSuccess();
                    else
                        view.menuUpdateError();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
    }

}
