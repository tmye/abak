package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.home.views.fragment.F_Restaurant_2_Fragment;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.syscore.GlideApp;

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
    }

    public void updateData (List<RestaurantEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

           /* // if you are the last ...
            if (viewType == data.size()-1) {
                return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.menu_food_item_space, parent, false));
            }*/
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final RestaurantEntity resto = data.get(position);

        holder.tv_resto_name.setText(resto.restaurant_name.toUpperCase());
        holder.tv_resto_address.setText(resto.getContact().address);
        holder.tv_resto_contact.setText(resto.getContact().phone);

        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS+resto.restaurant_logo)
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.cic_resto);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRestaurantInteraction(resto);
            }
        });
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
        public TextView tv_resto_contact;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            this.cic_resto = view.findViewById(R.id.iv_restaurant_icon);
            this.tv_resto_name = view.findViewById(R.id.tv_restaurant_name);
            this.tv_resto_address = view.findViewById(R.id.tv_restaurant_address);
            this.tv_resto_contact = view.findViewById(R.id.tv_restaurant_contact);
        }
    }
}

