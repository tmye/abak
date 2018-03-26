package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.data.Feeds.Feeds;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class HomeFeedsAdapter extends RecyclerView.Adapter<HomeFeedsAdapter.ViewHolder> {

    private final List<Feeds> feeds;
    private final Context ctx;

    public HomeFeedsAdapter (Context ctx, List<Feeds> feeds) {
        this.ctx = ctx;
        this.feeds = feeds;
    }

    @Override
    public HomeFeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeFeedsAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.feed_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HomeFeedsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}