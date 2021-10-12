package tg.tmye.kaba.restaurant._commons.adapter;

/**
 * By abiguime on 2020/6/9.
 * email: 2597434002@qq.com
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class EditMenuListAdapter extends RecyclerView.Adapter<EditMenuListAdapter.ViewHolder> implements Filterable {

    private final List<Restaurant_SubMenuEntity> data;
    private final Context context;
    private List<Restaurant_SubMenuEntity> usedData;
    private List<Restaurant_SubMenuEntity> restaurantListFiltered;


    public EditMenuListAdapter(Context context, List<Restaurant_SubMenuEntity> menu_list) {

        this.context = context;
        this.usedData = menu_list;
        this.data = menu_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.restaurant_menu_for_edit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Restaurant_SubMenuEntity entity = usedData.get(position);
        holder.tv_menu_name.setText(entity.name);
        holder.tv_description.setText(entity.description);
        holder.tv_priority.setText(String.valueOf(entity.priority));

        if (entity.is_hidden == 0) { // not hidden
            holder.iv_hidden.setVisibility(View.GONE);
        } else { // is hidden
            holder.iv_hidden.setVisibility(View.VISIBLE);
        }

        holder.bt_enter_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnMenuInteractionListener)(context)).enterSubMenuFoodList((int)entity.id);
            }
        });

        holder.bt_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnMenuInteractionListener)(context)).hideSubMenu((int)entity.id);
            }
        });

        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnMenuInteractionListener)(context)).deleteSubMenu((int)entity.id);
            }
        });

        holder.bt_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* call activity for editing in a dialog box */
                Intent intent = new Intent(context, EditSingleMenuActivity.class);
                intent.putExtra("menu", entity);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usedData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    restaurantListFiltered = data;
                } else {
                    List<Restaurant_SubMenuEntity> filteredList = new ArrayList<>();
                    for (Restaurant_SubMenuEntity row : data) {
                        // filter the EditText w
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    restaurantListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = restaurantListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usedData = (List<Restaurant_SubMenuEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_hidden;
        TextView tv_menu_name, tv_description, tv_priority;
        TextView bt_hide, bt_edit_name, bt_enter_menu, bt_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_menu_name = itemView.findViewById(R.id.tv_menu_name);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_priority = itemView.findViewById(R.id.tv_priority);
            bt_hide = itemView.findViewById(R.id.bt_hide);
            bt_edit_name = itemView.findViewById(R.id.bt_edit_name);
            bt_enter_menu = itemView.findViewById(R.id.bt_enter);
            bt_delete = itemView.findViewById(R.id.bt_delete);
            iv_hidden = itemView.findViewById(R.id.iv_hidden);
        }
    }


}
