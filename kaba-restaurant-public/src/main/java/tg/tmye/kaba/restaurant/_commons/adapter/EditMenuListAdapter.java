package tg.tmye.kaba.restaurant._commons.adapter;

/**
 * By abiguime on 2020/6/9.
 * email: 2597434002@qq.com
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.activities.menu.EditSingleMenuActivity;
import tg.tmye.kaba.restaurant.activities.menu.OnMenuInteractionListener;
import tg.tmye.kaba.restaurant.activities.menu.RestaurantMenuActivity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.GlideApp;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;


/**
 * By abiguime on 17/06/2018.
 * email: 2597434002@qq.com
 */
public class EditMenuListAdapter extends RecyclerView.Adapter<EditMenuListAdapter.ViewHolder> {

    private final List<Restaurant_SubMenuEntity> data;
    private final Context context;


    public EditMenuListAdapter(Context context, List<Restaurant_SubMenuEntity> menu_list) {

        this.context = context;
        this.data = menu_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.restaurant_menu_for_edit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Restaurant_SubMenuEntity entity = data.get(position);
        holder.tv_menu_name.setText(entity.name);
        holder.tv_description.setText(entity.description);

        if (entity.is_hidden == 0) { // not hidden

        } else { // is hidden

        }

        holder.bt_enter_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnMenuInteractionListener)(context)).enterSubMenuFoodList((int)entity.id);
            }
        });

        holder.bt_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditSingleMenuActivity.class);
                intent.putExtra("menu", entity);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_hidden;
        TextView tv_menu_name, tv_description;
        Button bt_hide, bt_edit_name, bt_enter_menu;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_menu_name = itemView.findViewById(R.id.tv_menu_name);
            tv_description = itemView.findViewById(R.id.tv_description);
            bt_hide = itemView.findViewById(R.id.bt_hide);
            bt_edit_name = itemView.findViewById(R.id.bt_edit_name);
            bt_enter_menu = itemView.findViewById(R.id.bt_enter);
            iv_hidden = itemView.findViewById(R.id.iv_hidden);
        }
    }


}
