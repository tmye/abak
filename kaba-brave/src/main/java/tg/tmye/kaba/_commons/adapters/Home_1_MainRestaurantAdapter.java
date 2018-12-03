package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Priority;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.home.views.fragment.F_Home_1_Fragment;
import tg.tmye.kaba.activity.home.views.fragment.F_Restaurant_2_Fragment;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data._OtherEntities.LightRestaurant;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class Home_1_MainRestaurantAdapter extends RecyclerView.Adapter<Home_1_MainRestaurantAdapter.ViewHolder> {


    private final F_Home_1_Fragment.OnFragmentInteractionListener mListener;
    private List<RestaurantEntity> data;
    private final Context ctx;

    public Home_1_MainRestaurantAdapter(Context context, List<RestaurantEntity> data,
                                        F_Home_1_Fragment.OnFragmentInteractionListener mListener) {

        this.ctx = context;
        this.data = data;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.main_restaurant_icon, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final RestaurantEntity item = data.get(position);

        if (item == null)
            return;

        // bind the data.
        holder.tv_resto_name.setText(item.name.toUpperCase());

        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS + "/" +item.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.kaba_pic)
                .priority(Priority.IMMEDIATE)
                .centerCrop()
                .into(holder.iv_resto_icon);


        if (item.coming_soon != F_Restaurant_2_Fragment.COMING_SOON) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onRestaurantInteraction(
                            item
                    );
                }
            });
        } else {
            holder.itemView.setOnClickListener(new ComingSoonClickListener(item));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void updateData(List<RestaurantEntity> daily_restaurants) {
        this.data = daily_restaurants;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView tv_resto_name;
        public ImageView iv_resto_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
            this.tv_resto_name = itemView.findViewById(R.id.tv_restaurant_name);
            this.iv_resto_icon = itemView.findViewById(R.id.iv_restaurant_icon);
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
}
