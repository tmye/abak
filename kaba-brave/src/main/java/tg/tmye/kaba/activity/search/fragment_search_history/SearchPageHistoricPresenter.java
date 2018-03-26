package tg.tmye.kaba.activity.search.fragment_search_history;

import java.util.List;

import tg.tmye.kaba.data.search.HistoricalItem;
import tg.tmye.kaba.data.search.source.HistoricalItemDataSource;

/**
 * Created by abiguime on 2016/12/7.
 */

public class SearchPageHistoricPresenter implements SearchPageContract.Presenter {


    private final HistoricalItemDataSource repo;
    private final SearchPageContract.View view;

    public SearchPageHistoricPresenter(SearchPageContract.View view, HistoricalItemDataSource repo) {
    this.view = view;
        this.repo = repo;
    }

    @Override
    public void getLastItems() {
        // no call back registering because we dont need it for now
        List<HistoricalItem> lastItems = repo.getLastItems();
        view.showHistoric(lastItems);
    }

    @Override
    public void saveHistoricalItem(int id) {

    }

    @Override
    public void start() {

    }
}
