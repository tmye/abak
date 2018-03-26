package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.decorator.AdsSingleLineDecorator;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.advert.Group10AdvertItem;
import tg.tmye.kaba.data.advert.ProductAdvertItem;
import tg.tmye.kaba.syscore.GlideApp;


/**
 * By abiguime on 11/01/2018.
 * email: 2597434002@qq.com
 */

public class GroupAdsAdapter extends RecyclerView.Adapter<GroupAdsAdapter.ViewHolder> {

    private final Context ctx;
    private final List<Group10AdvertItem> data;

    private int ITEM_COUNT = 3;

    public GroupAdsAdapter(Context ctx, List<Group10AdvertItem> data) {

        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_1_10_item_ads_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Group10AdvertItem item = data.get(position);

        /* rectangle */
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS+  item.adOne.img_path)
                .placeholder(R.drawable.white_placeholder)
                .centerCrop()
                .into(holder.iv_rectangle);

         /* square */
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS+  item.adOnePrime.img_path)
                .placeholder(R.drawable.white_placeholder)
                .centerCrop()
                .into(holder.iv_square);

        holder.tv_title.setText(item.title);
        holder.tv_title.setBackgroundColor(Color.parseColor(item.title_code_color));

        inflateLevel(holder.rc_level_1, item.level1_ads, true);
        inflateLevel(holder.rc_level_2, item.level2_ads, false);
    }

    private void inflateLevel(RecyclerView rc, List<ProductAdvertItem> data, boolean isTop) {

        int rand = new Random().nextInt(2)%2;
//        data = ProductAdvertItem.fakeList(rand == 0 ? 2 : 4);

        Grid48ViewAdapter ad_4 = new Grid48ViewAdapter(ctx, data);
        rc.setLayoutManager(new GridLayoutManager(ctx, data.size()));
        // separation
        rc.addItemDecoration(new AdsSingleLineDecorator(
                ctx.getResources().getDimensionPixelSize(R.dimen.ad_4_spacing)
                ,isTop));
        // according to the count, you know how to make the ...
        rc.setAdapter(ad_4);
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView rc_level_1;
        public RecyclerView rc_level_2;

        public ImageView iv_rectangle;
        public ImageView iv_square;

        public TextView tv_rectangle_description, tv_square_description;

        public TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_rectangle = itemView.findViewById(R.id.iv_long2);
            iv_square = itemView.findViewById(R.id.iv_square1);
            tv_rectangle_description = itemView.findViewById(R.id.tv_long2);
            tv_square_description = itemView.findViewById(R.id.tv_square_1);
            rc_level_1 = itemView.findViewById(R.id.rec_ads_lvl_1);
            rc_level_2 = itemView.findViewById(R.id.rec_ads_lvl_2);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
