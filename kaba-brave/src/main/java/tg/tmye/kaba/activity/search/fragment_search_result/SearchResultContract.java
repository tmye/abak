package tg.tmye.kaba.activity.search.fragment_search_result;

import java.util.List;

import tg.tmye.kaba.BasePresenter;
import tg.tmye.kaba.BaseView;
import tg.tmye.kaba.data.search.SearchResult;

/**
 * Created by abiguime on 2016/12/7.
 */

public class SearchResultContract {

    public interface View extends BaseView<Presenter> {

        /* 获取最新的 搜索记录 */
        void showResult(List<SearchResult> items);

    }


    interface Presenter extends BasePresenter {

        // 加载视频
        void searchItemsForItem(String query);
    }
}
