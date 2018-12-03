package tg.tmye.kaba.activity.UserAcc.feeds.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.notification.NotificationItem;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.UserAcc.feeds.FeedActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Feeds.Feeds;
import tg.tmye.kaba.data.Feeds.NewsFeed;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 26/06/2018.
 * email: 2597434002@qq.com
 */
public class FeedsSingleFragment extends Fragment {


    private static final String DATA_LIST = "DATA_LIST";

    private RecyclerView recyclerView;
    private TextView tv_message;

    List<Feeds> feeds_data;

    public static FeedsSingleFragment newInstance(List<Feeds> feeds_data) {

        Bundle args = new Bundle();
        FeedsSingleFragment fragment = new FeedsSingleFragment();
        args.putParcelableArrayList(DATA_LIST, (ArrayList<? extends Parcelable>) feeds_data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feeds_data = getArguments().getParcelableArrayList(DATA_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feeds_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initAdapter();
    }

    private void initAdapter() {

        if (feeds_data == null || feeds_data.size() == 0) {
            /* nothing to show */
            tv_message.setText(getResources().getString(R.string.no_data));
            tv_message.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        NewsFeedRecAdapter  adap = new NewsFeedRecAdapter(getContext(), feeds_data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adap);
        recyclerView.setVisibility(View.VISIBLE);
        tv_message.setVisibility(View.GONE);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        tv_message = view.findViewById(R.id.tv_message);
    }

    /**/
    NewsFeedRecAdapter adapter;

    public void inflateData (List<Feeds> data) {

        /* */
        this.feeds_data = data;
        if (adapter == null) {
            initAdapter();
        } else {
            if (feeds_data == null || feeds_data.size() == 0) {
                /* nothing to show */
                tv_message.setText(getResources().getString(R.string.no_data));
                tv_message.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return;
            }
            recyclerView.setVisibility(View.VISIBLE);
            tv_message.setVisibility(View.GONE);
            adapter.setData(feeds_data);
        }
    }


    class NewsFeedRecAdapter extends RecyclerView.Adapter<NewsFeedRecAdapter.ViewHolder> {

        private final Context ctx;
        private List<Feeds> data;

        public NewsFeedRecAdapter (Context ctx, List<Feeds> nf) {

            this.ctx = ctx;
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

            final Feeds feed = data.get(position);

            holder.tv_title.setText(feed.title);
            holder.tv_description.setText(feed.content);
            holder.tv_created_at.setText(feed.created_at);

            /* light up with the picture */
            GlideApp.with(ctx)
                    .load(Constant.SERVER_ADDRESS + "/" + feed.pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(holder.iv_pic);

            holder.itemView.setOnClickListener(new OnArticleClickListener(feed.destination.type, feed.destination.product_id));

            holder.iv_pic.setOnClickListener(new OnImageClickListener() {
                @Override
                public void onClick(View view) {
                    /* on transforme ca en ads */
                    AdsBanner adsBanner = new AdsBanner();
                    adsBanner.name = feed.title;
                    adsBanner.pic = feed.pic;
                    adsBanner.description = feed.content.substring(0, 30)+"...";

                    Intent intent = new Intent(ctx, ImagePreviewActivity.class);
                    intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, new AdsBanner[]{adsBanner});
                    ctx.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<Feeds> feeds_data) {

            this.data = feeds_data;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public View rootview;
            public View bottomdivider;

            /*  */
            public TextView tv_title, tv_description, tv_created_at;
            public ImageView iv_pic;

            public ViewHolder(View itemView) {
                super(itemView);
                this.rootview = itemView;
                this.bottomdivider = itemView.findViewById(R.id.bottomdivider);
                tv_title = itemView.findViewById(R.id.tv_title);
                tv_description = itemView.findViewById(R.id.tv_description);
                tv_created_at = itemView.findViewById(R.id.tv_created_at);
                iv_pic = itemView.findViewById(R.id.cic_newsfeed_image);
            }
        }
    }

    private class OnArticleClickListener implements View.OnClickListener {


        private final int product_id;
        private final int type;

        public OnArticleClickListener(int type, int product_id) {
            this.type = type;
            this.product_id = product_id;
        }

        @Override
        public void onClick(View view) {
            ((FeedActivity)getActivity()).onFeedItemSelected (type, product_id);
        }
    }
}
