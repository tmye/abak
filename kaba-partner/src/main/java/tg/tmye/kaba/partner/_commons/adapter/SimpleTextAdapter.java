package tg.tmye.kaba.partner._commons.adapter;

/**
 * By abiguime on 2020/6/9.
 * email: 2597434002@qq.com
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner.activities.menu.RestaurantMenuActivity;
import tg.tmye.kaba.partner.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.partner.syscore.Constant;
import tg.tmye.kaba.partner.syscore.GlideApp;
import tg.tmye.kaba.partner.syscore.MyRestaurantApp;


/**
 * By abiguime on 17/06/2018.
 * email: 2597434002@qq.com
 */
public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {

    private final List<Restaurant_SubMenuEntity> data;
    private final Context context;

    private int selected_position;

    public SimpleTextAdapter (Context context, List<Restaurant_SubMenuEntity> menu_list) {

        this.context = context;
        this.data = menu_list;
        selected_position = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_title_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_name.setText(data.get(position).name);
        holder.tv_name.setOnClickListener(new OnMenuSelectedListener(position));


        /* set up glide */
//        http://app1.kaba-delivery.com/web/assets/app_icons/mo.gif

        if (data.get(position).promotion == 0) {
            holder.iv_mo.setVisibility(View.GONE);
            holder.iv_pro.setVisibility(View.GONE);
        } else {

            holder.iv_mo.setVisibility(View.VISIBLE);
            holder.iv_pro.setVisibility(View.VISIBLE);

            GlideApp.with(context)
                    .load(Constant.SERVER_ADDRESS +"/web/assets/app_icons/mo.gif")
                    .transition(GenericTransitionOptions.with(  ((MyRestaurantApp)context.getApplicationContext()).getGlideAnimation()  ))
                    .placeholder(R.drawable.placeholder_kaba)
                    .into(holder.iv_mo);
            GlideApp.with(context)
                    .load(Constant.SERVER_ADDRESS +"/web/assets/app_icons/pro.gif")
                    .transition(GenericTransitionOptions.with(  ((MyRestaurantApp)context.getApplicationContext()).getGlideAnimation()  ))
                    .placeholder(R.drawable.placeholder_kaba)
                    .into(holder.iv_pro);
        }


        if (position == selected_position) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tv_name.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.tv_name.setTextColor(Color.BLACK);
        }
    }

    public void setSelected_position (int selected_position) {
        this.selected_position = selected_position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        ImageView iv_pro, iv_mo;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_mo = itemView.findViewById(R.id.iv_mo);
            iv_pro = itemView.findViewById(R.id.iv_pro);
        }
    }

    private class OnMenuSelectedListener implements View.OnClickListener {

        private final int position;

        public OnMenuSelectedListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            ((RestaurantMenuActivity)context).onMenuInteraction(position);
            selected_position = position;
            notifyDataSetChanged();
        }
    }
}
