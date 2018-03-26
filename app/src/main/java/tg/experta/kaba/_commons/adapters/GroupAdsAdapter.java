package tg.experta.kaba._commons.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Random;

import tg.experta.kaba.R;
import tg.experta.kaba._commons.decorator.AdsSingleLineDecorator;
import tg.experta.kaba._commons.decorator.SpacesItem_4_8_Decoration;
import tg.experta.kaba.data.advert.ProductAdvertItem;

/**
 * By abiguime on 11/01/2018.
 * email: 2597434002@qq.com
 */

public class GroupAdsAdapter extends RecyclerView.Adapter<GroupAdsAdapter.ViewHolder> {

    private final Context ctx;

    private int ITEM_COUNT = 3;

    public GroupAdsAdapter(Context ctx) {

        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_1_10_item_ads_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        inflateLevel(holder.rc_level_1, null, true);
        inflateLevel(holder.rc_level_2, null, false);
    }

    private void inflateLevel(RecyclerView rc, List<ProductAdvertItem> data, boolean isTop) {

        int rand = new Random().nextInt(2)%2;
        data = ProductAdvertItem.fakeList(rand == 0 ? 2 : 4);

        Ad_4_8_Adapter ad_4 = new Ad_4_8_Adapter(ctx, data);
        rc.setLayoutManager(new GridLayoutManager(ctx, rand == 0 ? 2 : 4));
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

        //

        public ViewHolder(View itemView) {
            super(itemView);
            rc_level_1 = itemView.findViewById(R.id.rec_ads_lvl_1);
            rc_level_2 = itemView.findViewById(R.id.rec_ads_lvl_2);
        }
    }
}
