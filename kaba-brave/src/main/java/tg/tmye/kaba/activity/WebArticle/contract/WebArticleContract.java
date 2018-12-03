package tg.tmye.kaba.activity.WebArticle.contract;

import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.syscore.baseobj.BaseView;
import tg.tmye.kaba.data.article.WebArticle;

/**
 * By abiguime on 13/03/2018.
 * email: 2597434002@qq.com
 */

public class WebArticleContract {

    public interface View extends BaseView<WebArticleContract.Presenter> {

        void showLoading(boolean isLoading);

        void inflateArticle (WebArticle article);

        void toast(String message);
    }


    public interface Presenter extends BasePresenter {

        void loadArticle (int id);
    }

}
