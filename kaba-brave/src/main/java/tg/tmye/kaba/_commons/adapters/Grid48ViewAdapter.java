package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.home.views.fragment.F_Home_1_Fragment;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


/**
 * By abiguime on 09/01/2018.
 * email: 2597434002@qq.com
 */

public class Grid48ViewAdapter extends RecyclerView.Adapter<Grid48ViewAdapter.ViewHolder> {


    private static final int STANDARD_SIZE = 2;
    private final List<AdsBanner> data;
    private final Context ctx;
    private final F_Home_1_Fragment.OnFragmentInteractionListener listener;


    public Grid48ViewAdapter(Context ctx, List<AdsBanner> data, F_Home_1_Fragment.OnFragmentInteractionListener listener) {

        this.ctx = ctx;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.ad_line_4_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        int width = UtilFunctions.getScreenSize(ctx)[0];
        int item_height = 9*(width/2)/16;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iv_background_content.getLayoutParams();
        if (params == null)
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item_height);
        params.height = item_height;
        holder.iv_background_content.setLayoutParams(params);

        if (position == 0) {
            holder.iv_the_title.setImageResource(R.drawable.meilleures_ventes);
            GlideApp.with(ctx)
                    .load(Constant.SERVER_ADDRESS+"/"+data.get(position).pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                    .placeholder(R.drawable.white_placeholder)
                    .centerCrop()
                    .into(holder.iv_background_content);
        }
        if (position == 1) {
            holder.iv_the_title.setImageResource(R.drawable.evenement);
            GlideApp.with(ctx)
                    .load(Constant.SERVER_ADDRESS+"/"+data.get(position).pic)
                    .transition(GenericTransitionOptions.with(((MyKabaApp) ctx.getApplicationContext()).getGlideAnimation()))
                    .placeholder(R.drawable.white_placeholder)
                    .centerCrop()
                    .into(holder.iv_background_content);
        }

        AdsBanner ad = data.get(position);

        /* we need 4 different ads elements, that will come with pictures and text already on the pic */
        /* they will also point where they should go on click ... dynamic list of  products */
        holder.itemView.setOnClickListener(new OnAdsClickListener(ad));
    }

    @Override
    public int getItemCount() {
        return STANDARD_SIZE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        ImageView iv_ads_pic_no_title;
//        ImageView iv_ads_pic_with_title;
        ImageView iv_background_content;
        ImageView iv_the_title;

        public ViewHolder(View itemView) {
            super(itemView);
//            iv_ads_pic_no_title = itemView.findViewById(R.id.iv_ads_pic_no_title);
//            iv_ads_pic_with_title = itemView.findViewById(R.id.iv_ads_pic_with_title);
            iv_the_title = itemView.findViewById(R.id.iv_the_title);
            iv_background_content = itemView.findViewById(R.id.iv_background_content);
        }
    }

    private class OnAdsClickListener implements View.OnClickListener {

        private final AdsBanner pub;

        public OnAdsClickListener(AdsBanner pub) {
            this.pub = pub;
        }

        @Override
        public void onClick(View view) {
            listener.onAdsInteraction(pub);
        }
    }

}