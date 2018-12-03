package tg.tmye.kaba.activity.UserAcc.feeds.contract;

import java.util.List;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.Feeds.Feeds;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBasePresenter;
import tg.tmye.kaba.syscore.baseobj.auth.AuthBaseView;

/**
 * By abiguime on 03/03/2018.
 * email: 2597434002@qq.com
 */

public interface FeedContract {

    public interface View extends AuthBaseView<Presenter> {

        void inflateFeeds (List<Feeds> articles, List<Feeds> notifications);

        void showLoading(boolean isVisible);

        void finishActivity();

        void toast(int data_error);

        void sysError();

        void networkError();
    }

    public interface Presenter extends AuthBasePresenter {

        void loadFeeds ();



    }
}
