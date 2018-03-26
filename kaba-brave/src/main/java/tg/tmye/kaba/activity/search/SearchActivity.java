package tg.tmye.kaba.activity.search;

import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.search.fragment_search_history.SearchHistoryItemFragment;
import tg.tmye.kaba.activity.search.fragment_search_history.SearchPageHistoricPresenter;
import tg.tmye.kaba.activity.search.fragment_search_result.SearchResultItemFragment;
import tg.tmye.kaba.activity.search.fragment_search_result.SearchResultPresenter;
import tg.tmye.kaba.data.search.SearchResult;
import tg.tmye.kaba.data.search.source.HistoricalItemDataSource;
import tg.tmye.kaba.data.search.source.SearchItemDataSource;


public class SearchActivity extends AppCompatActivity implements
        SearchResultItemFragment.OnListFragmentInteractionListener,
        SearchHistoryItemFragment.OnListFragmentInteractionListener,
        SearchView.OnQueryTextListener, View.OnFocusChangeListener {

    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };

    private SimpleCursorAdapter mAdapter;
    private SearchView searchView;
    private Toolbar tb;

    // fragments
    private SearchHistoryItemFragment historicListFragment;
    private SearchResultItemFragment searchResultFragment;

    // historic fragment presenter and remote
    private SearchPageHistoricPresenter historicItempresenter;
    private HistoricalItemDataSource historicItemrepo;

    // result fragment presenter and remote
    private SearchResultPresenter searchResultPresenter;
    private SearchItemDataSource searchItemDataSource;

    //Appbar layout
    AppBarLayout appBarLayout;

    /* 是否在退出activity */
    private boolean exiting = false;
    private boolean isQuering = false;
    private Handler mHandler = new Handler();
//    private YoutubeLayout2 ytb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        initViews();
        setSupportActionBar(tb);


        // searchview 默认已经打开而且可以直接输入内容
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);

        ImageButton bt = (ImageButton) findViewById(R.id.iv_backtohome);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exiting = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

        /* 更改底部的颜色 。*/
        View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);

        // retrieve the data from the database and save put it in the fragment
        initRepo();
       initFragments();
//        initYoutubeLayout();
//        initMediaPlayer();


        // material design activity 共享视图
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Transition custom = TransitionInflater.from(this).inflateTransition(R.transition.exit);
//            custom.setDuration(1000);
//            getWindow().setExitTransition(custom);
//            getWindow().setEnterTransition(custom);
        }

        appBarLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);

    }



    private void initMediaPlayer() {
        if (getIntent().getBooleanExtra("isplaying", false)) {
//            ytb.continueVideo(false);
        }
    }



    private void initYoutubeLayout() {
//        if (ytb != null && ytb.getVisibility() != View.VISIBLE) {
//            ytb.setVisibility(View.VISIBLE);
//        }
    }

    private void initRepo() {
        searchItemDataSource = new SearchItemDataSource(SearchActivity.this);
    }

    private void mT(String s) {
        Toast.makeText(SearchActivity.this, s, Toast.LENGTH_SHORT).show();
    }


    private void initFragments() {

        if (historicListFragment == null) {
            historicListFragment = SearchHistoryItemFragment.newInstance();
            historicItemrepo = new HistoricalItemDataSource(getApplicationContext());
            historicItempresenter = new SearchPageHistoricPresenter(historicListFragment, historicItemrepo);
            historicListFragment.setPresenter(historicItempresenter);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            `04            inflateTransition(android.R.transition.slide_bottom);
//                explodeTransform.setDuration(300);
                // Setup exit transition on first fragment
//                historicListFragment.setSharedElementReturnTransition(explodeTransform);
//                historicListFragment.setEnterTransition(explodeTransform);
//                historicListFragment.setReenterTransition(explodeTransform);
            }
        }
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frame_layout, historicListFragment, SearchHistoryItemFragment.TAG);
        trans.commit();
    }

    private void initViews() {
        tb = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.mysearchview);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
//        ytb= (YoutubeLayout2) findViewById(R.id.youtubelayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);
    }


    /* 提交搜搜的操作*/
    @Override
    public boolean onQueryTextSubmit(final String query) {
        if (isQuering)
            return true;
        isQuering = true;
        final FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
     /*   searchItemDataSource.getHomePageVideos("", 20, new VideoDataSource.GetVideosCallBack() {

            @Override
            public void onVideoLoaded(List<Video> data) {
                if (searchResultFragment == null) {
                    searchResultFragment = SearchResultItemFragment.newInstance(data);
                    searchResultPresenter = new SearchResultPresenter(searchResultFragment, searchItemDataSource);
                    searchResultFragment.setPresenter(searchResultPresenter);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Transition explodeTransform = TransitionInflater.from(SearchActivity.this).
                                inflateTransition(android.R.transition.slide_bottom);
                        explodeTransform.setDuration(300);
                        // Setup exit transition on first fragment
                        searchResultFragment.setSharedElementReturnTransition(explodeTransform);
                        searchResultFragment.setEnterTransition(explodeTransform);
                        searchResultFragment.setReenterTransition(explodeTransform);
                    }
                } else {
                    searchResultFragment.sendRequest(query);
                }
                trans.replace(R.id.frame_layout, searchResultFragment, SearchResultItemFragment.TAG);
                trans.commit();
            }

            @Override
            public void onDataNotAvailable(String message) {
            }
        });*/

        searchView.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.clearFocus();
            }
        }, 1000);
        return true;
    }


    /* 当输入框字符串有变化*/
    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void suggest(String suggest) {
        searchView.setQuery(suggest, false);
    }

    @Override
    public void search(String search) {
        searchView.setQuery(search, true);
    }


   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }*/


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        /*由于在finish过程中有时候会调用onfocuschange方法，就应该判断一下、
        * 本方法只能在还没有finish之前运行*/
        try {
            if (hasFocus && !exiting) {
                // show the historic fragment
                final FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                if (searchResultFragment!=null)
                trans.replace(R.id.frame_layout, historicListFragment, SearchHistoryItemFragment.TAG);
                trans.commit();
                isQuering = false;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSearchResultResultSelected(SearchResult searchResult) {

    }
}
