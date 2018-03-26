package tg.tmye.kaba.activity.search.fragment_search_result;

import tg.tmye.kaba.data.search.source.SearchItemDataSource;

/**
 * Created by abiguime on 2016/12/7.
 */

public class SearchResultPresenter implements SearchResultContract.Presenter {


    private final SearchItemDataSource repo;
    private final SearchResultContract.View view;

    public SearchResultPresenter(SearchResultContract.View view, SearchItemDataSource repo) {
    this.view = view;
        this.repo = repo;
    }

    @Override
    public void start() {

    }


    @Override
    public void searchItemsForItem(String query) {

        int lastVideo = 0;
       /* repo.getHomePageVideos("", lastVideo, new VideoDataSource.GetVideosCallBack() {

            @Override
            public void onVideoLoaded(List<Video> data) {
                view.showResult(data);
            }

            @Override
            public void onDataNotAvailable(String message) {
            }

        });*/
    }
}
