package tg.tmye.kaba.activity.home.presenter;

import java.util.HashMap;
import java.util.List;

import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;
import tg.tmye.kaba.activity.home.contracts.F_CommandContract;
import tg.tmye.kaba.activity.home.contracts.F_HomeContract;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.source.RestaurantRepository;
import tg.tmye.kaba.data._OtherEntities.Error;
import tg.tmye.kaba.data.advert.source.AdvertRepository;
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
        command3View.showIsLoading(true);
        command3View.showErrorPage(false);
        commandRepository.getUpdateCommandList(new YesOrNoWithResponse(){
            @Override
            public void yes(Object data, boolean isFromOnline) {
                command3View.inflateCommandList((List<Command>) data);
                command3View.showIsLoading(false);
                isPopulateRunning = false;
            }

            @Override
            public void no(Object data, boolean isFromOnline) {
                /* */
                if (data instanceof Error.NetworkError)
                    command3View.networkError();
                else if (data instanceof Error.LocalError) {
                    command3View.networkError();
                }
                command3View.showIsLoading(false);
                isPopulateRunning = false;
                /* load from local database */
            }
        });

    }

    @Override
    public void update() {
        populateViews();
    }
}
