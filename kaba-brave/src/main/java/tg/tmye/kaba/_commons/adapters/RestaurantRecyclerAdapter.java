package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Priority;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.intf.ImagePreviewerListener;
import tg.tmye.kaba.activity.home.views.fragment.F_Restaurant_2_Fragment;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ViewHolder> {

    private final F_Restaurant_2_Fragment.OnFragmentInteractionListener mListener;
    private List<RestaurantEntity> data;
    private final Context ctx;

    public RestaurantRecyclerAdapter(Context ctx, List<RestaurantEntity> data, F_Restaurant_2_Fragment.OnFragmentInteractionListener mListener) {
        this.ctx = ctx;
        this.data = data;
        this.mListener = mListener;

     /*   for (int i = 0; i < data.size(); i++) {
            data.get(i).is_coming_soon = (UtilFunctions.getRandomBoolean () ? F_Restaurant_2_Fragment.COMING_SOON : 0);
        }*/
    }

    public void updateData (List<RestaurantEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        RestaurantEntity entity = data.get(position);
        return entity.coming_soon;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        /* according to coming soon, we need to build another type of view. */
        if (viewType == F_Restaurant_2_Fragment.COMING_SOON) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_restaurant_list_item_coming_soon, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_restaurant_list_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final RestaurantEntity resto = data.get(position);

        holder.tv_resto_name.setText(resto.name.toUpperCase());
        holder.tv_resto_address.setText(resto.address);

        if (resto.distance == null || "".equals(resto.distance)) {
            holder.view_separator.setVisibility(View.GONE);
            holder.tv_distance.setVisibility(View.GONE);
        } else {
            holder.view_separator.setVisibility(View.VISIBLE);
            holder.tv_distance.setVisibility(View.VISIBLE);
            holder.tv_distance.setText(resto.distance + ctx.getResources().getString(R.string.kilometer));
        }

        if (holder.tv_is_open != null) {
            if (resto.is_open == 1) {
                /* open*/
                holder.tv_is_open.setTextColor(Color.WHITE);
                holder.tv_is_open.setText(ctx.getText(R.string.is_opened));
                holder.tv_is_open.setBackgroundResource(R.drawable.bg_green_rounded);
            } else {
                /* closed */
                holder.tv_is_open.setTextColor(Color.WHITE);
                holder.tv_is_open.setText(ctx.getText(R.string.is_closed));
                holder.tv_is_open.setBackgroundResource(R.drawable.bg_resto_closed_rounded);
            }
        }

        if (holder.tv_is_coming_soon != null) {
            if (resto.coming_soon == F_Restaurant_2_Fragment.COMING_SOON) {
                /* is coming soon */
                holder.tv_is_coming_soon.setTextColor(Color.WHITE);
                holder.tv_is_coming_soon.setText(ctx.getText(R.string.is_coming_soon));
                holder.tv_is_coming_soon.setBackgroundResource(R.drawable.bg_red_cornered);
                holder.tv_is_coming_soon.setVisibility(View.VISIBLE);
            } else {
                holder.tv_is_coming_soon.setVisibility(View.GONE);
            }
        }

        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS +"/"+ resto.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .priority(Priority.NORMAL)
                .centerCrop()
                .into(holder.cic_resto);

        holder.cic_resto.setOnClickListener(new PreviewImageListener(resto));


        if (resto.coming_soon != F_Restaurant_2_Fragment.COMING_SOON) {
            holder.itemView.setOnClickListener(new OnEnterMenuClickListener(resto));
            holder.itemView.setOnLongClickListener(new OnEnterRestaurantClickListener(resto));
        } else {
            holder.itemView.setOnClickListener(new ComingSoonClickListener(resto));
            holder.itemView.setOnLongClickListener(new ComingSoonClickListener(resto));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View rootView;
        public CircleImageView cic_resto;
        public TextView tv_resto_name;
        public TextView tv_resto_address;
        public ImageView iv_restaurant_theme_image;

        public TextView tv_enter_menu;
        public TextView tv_enter_restaurant;
        public TextView tv_distance;
        public View view_separator;
        public TextView tv_is_open;
        public TextView tv_is_coming_soon;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            this.cic_resto = view.findViewById(R.id.iv_restaurant_icon);
            this.tv_resto_name = view.findViewById(R.id.tv_restaurant_name);
            this.tv_resto_address = view.findViewById(R.id.tv_restaurant_address);
            tv_distance = view.findViewById(R.id.tv_distance);
            view_separator = view.findViewById(R.id.view_separator);
            tv_is_open = view.findViewById(R.id.tv_is_open);
            tv_is_coming_soon =  view.findViewById(R.id.tv_is_coming_soon);
        }
    }

    private class OnEnterMenuClickListener implements View.OnClickListener {
        private final RestaurantEntity resto;

        public OnEnterMenuClickListener(RestaurantEntity resto) {
            this.resto = resto;
        }

        @Override
        public void onClick(View view) {
            mListener.onRestaurantInteraction(resto, F_Restaurant_2_Fragment.OnFragmentInteractionListener.MENU);
        }
    }

    private class OnEnterRestaurantClickListener implements View.OnLongClickListener {
        private final RestaurantEntity resto;

        public OnEnterRestaurantClickListener(RestaurantEntity resto) {
            this.resto = resto;
        }

        @Override
        public boolean onLongClick(View view) {
            mListener.onRestaurantInteraction(resto, F_Restaurant_2_Fragment.OnFragmentInteractionListener.RESTAURANT);
            return true;
        }
    }

    public class ComingSoonClickListener implements View.OnLongClickListener, View.OnClickListener {

        private final RestaurantEntity resto;

        public ComingSoonClickListener(RestaurantEntity resto) {
            this.resto = resto;
        }

        @Override
        public boolean onLongClick(View view) {
            mListener.onComingSoonInteractionListener(resto);
            return true;
        }

        @Override
        public void onClick(View view) {
            mListener.onComingSoonInteractionListener(resto);
        }
    }

    private class PreviewImageListener implements View.OnClickListener {

        AdsBanner adsBanner;

        public PreviewImageListener(RestaurantEntity resto) {

            adsBanner = new AdsBanner();
            adsBanner.pic = resto.pic;
            adsBanner.description = resto.name;
        }

        @Override
        public void onClick(View view) {

            ((ImagePreviewerListener)ctx).onShowPic(view, adsBanner);
        }
    }
}

