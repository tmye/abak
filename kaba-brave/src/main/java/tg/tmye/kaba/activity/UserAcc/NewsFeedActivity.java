package tg.tmye.kaba.activity.UserAcc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.data.Feeds.NewsFeed;


public class NewsFeedActivity extends AppCompatActivity {



    RecyclerView rec_newsfeed;
    SwipeRefreshLayout swp;

    // adapter
    private NewsFeedRecAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_yellow_upward_navigation_24dp);


        initViews();

        // load feeds
        // -- firstly load from local dabatase
        // -- load from online next
        initAdapter();
    }

    private void initAdapter() {
        adapter = new NewsFeedRecAdapter (this, null);
        rec_newsfeed.setLayoutManager(new LinearLayoutManager(this));
        rec_newsfeed.setAdapter(adapter);
    }

    private void initViews() {

        swp = findViewById(R.id.swp);
        rec_newsfeed = findViewById(R.id.rec_newsfeed);
    }

    class NewsFeedRecAdapter extends RecyclerView.Adapter<NewsFeedRecAdapter.ViewHolder> {

        private final List<NewsFeed> data;

        public NewsFeedRecAdapter (Context ctx, List<NewsFeed> nf) {

            this.data = nf;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.newsfeed_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (position == getItemCount()-1)
                holder.bottomdivider.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public View rootview;
            public View bottomdivider;

            public ViewHolder(View itemView) {
                super(itemView);
                this.rootview = itemView;
                this.bottomdivider = itemView.findViewById(R.id.bottomdivider);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
