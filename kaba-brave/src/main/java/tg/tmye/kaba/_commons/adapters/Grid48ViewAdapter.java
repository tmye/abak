package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.advert.ProductAdvertItem;
import tg.tmye.kaba.syscore.GlideApp;


/**
 * By abiguime on 09/01/2018.
 * email: 2597434002@qq.com
 */

public class Grid48ViewAdapter extends RecyclerView.Adapter<Grid48ViewAdapter.ViewHolder> {


    private final List<ProductAdvertItem> data;
    private final Context ctx;


    public Grid48ViewAdapter(Context ctx, List<ProductAdvertItem> data) {

        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.ad_line_4_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductAdvertItem item = data.get(position);

        // bind glide image and link to call when pressed
//        mListener.onProductAddPressed(item);


        holder.iv_add_pic.setImageResource(item.cloo);
       /* holder.iv_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, WebActivity.class);
                ctx.startActivity(in);
            }
        });*/

        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS+item.img_path)
                .placeholder(R.drawable.white_placeholder)
                .into(holder.iv_add_pic);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_add_pic;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_add_pic = itemView.findViewById(R.id.iv_ads_pic);
        }
    }
}