package tg.tmye.kaba.restaurant._commons.adapter;

/**
 * By abiguime on 2020/6/9.
 * email: 2597434002@qq.com
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.activities.menu.EditSingleFoodActivity;
import tg.tmye.kaba.restaurant.activities.menu.EditSingleMenuActivity;
import tg.tmye.kaba.restaurant.activities.menu.OnFoodInteractionListener;
import tg.tmye.kaba.restaurant.activities.menu.OnMenuInteractionListener;
import tg.tmye.kaba.restaurant.activities.menu.RestaurantMenuActivity;
import tg.tmye.kaba.restaurant.cviews.SlidingBanner_LilRound;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.advert.AdsBanner;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.GlideApp;
import tg.tmye.kaba.restaurant.syscore.MyRestaurantApp;


/**
 * By abiguime on 17/06/2018.
 * email: 2597434002@qq.com
 */
public class EditFoodListAdapter extends RecyclerView.Adapter<EditFoodListAdapter.ViewHolder> implements Filterable {

    private final List<Restaurant_Menu_FoodEntity> data;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final int sub_menu_id;
    private List<Restaurant_Menu_FoodEntity> usedData;
    private List<Restaurant_Menu_FoodEntity> restaurantListFiltered;

    public EditFoodListAdapter(Context context, FragmentManager fragmentManager, List<Restaurant_Menu_FoodEntity> menu_list, int sub_menu_id) {

        this.context = context;
        this.fragmentManager = fragmentManager;
        this.data = menu_list;
        this.usedData = menu_list;
        this.sub_menu_id = sub_menu_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.restaurant_food_for_edit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Restaurant_Menu_FoodEntity entity = data.get(position);
        holder.tv_description.setText(entity.description);
        holder.tv_food_name.setText(entity.name);

        // set a line
        holder.tv_food_price.setText(""+entity.price);
        holder.tv_food_promotion_price.setText(""+(entity.promotion_price == null ? 0 : entity.promotion_price));
        holder.switch_is_promotion.setChecked(entity.promotion == 1);
        holder.lny_promotion_price.setVisibility(entity.promotion == 1 ? View.VISIBLE : View.GONE);

//        iv_food_picture
        GlideApp.with(context)
                .load(Constant.SERVER_ADDRESS + "/" + entity.pic)
                .transition(GenericTransitionOptions.with(((MyRestaurantApp) context.getApplicationContext()).getGlideAnimation()))
                .centerCrop()
                .into(holder.iv_food_picture);

//        initSlidingBanner(holder.sliding_banner, entity);

        if (entity.is_hidden == 0){
           holder.iv_hidden.setVisibility(View.GONE);
            holder.tv_is_food_hidden.setVisibility(View.GONE);
        } else {
            holder.iv_hidden.setVisibility(View.VISIBLE);
            holder.tv_is_food_hidden.setVisibility(View.VISIBLE);
        }

        if (entity.promotion == 0) {
            holder.tv_food_price.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.tv_food_promotion_price.setPaintFlags(holder.tv_food_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.tv_food_promotion_price.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_food_price.setTextColor(context.getResources().getColor(R.color.colorPrimary_yellow));

        } else {
            holder.tv_food_price.setPaintFlags(holder.tv_food_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_food_promotion_price.setPaintFlags(Paint.ANTI_ALIAS_FLAG);

            holder.tv_food_promotion_price.setTextColor(context.getResources().getColor(R.color.colorPrimary_yellow));
            holder.tv_food_price.setTextColor(context.getResources().getColor(R.color.black));

        }

        holder.bt_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, EditSingleFoodActivity.class);
                intent.putExtra("food", entity);
                intent.putExtra("menu_id", sub_menu_id);
                context.startActivity(intent);
            }
        });

        holder.bt_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        ((OnFoodInteractionListener)(context)).hide((int)entity.id);
            }
        });

        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnFoodInteractionListener)(context)).delete((int)entity.id);
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
                    List<Restaurant_Menu_FoodEntity> filteredList = new ArrayList<>();
                    for (Restaurant_Menu_FoodEntity row : data) {
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
                usedData = (List<Restaurant_Menu_FoodEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        SlidingBanner_LilRound sliding_banner;
        TextView tv_food_name, tv_description, tv_is_food_hidden, tv_food_promotion_price, tv_food_price;
        ImageView iv_hidden, iv_food_picture;
        TextView/* bt_hide,*/ bt_edit_name, bt_hide, bt_delete;
        Switch switch_is_promotion;
        LinearLayout lny_promotion_price;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            switch_is_promotion = itemView.findViewById(R.id.switch_is_promotion);
            tv_description = itemView.findViewById(R.id.tv_description);
//            sliding_banner = itemView.findViewById(R.id.sliding_banner);
            iv_food_picture = itemView.findViewById(R.id.iv_food_picture);
            iv_hidden = itemView.findViewById(R.id.iv_hidden);
            bt_edit_name = itemView.findViewById(R.id.bt_edit_name);
            bt_hide = itemView.findViewById(R.id.bt_hide);
            bt_delete = itemView.findViewById(R.id.bt_delete);
            tv_food_promotion_price = itemView.findViewById(R.id.tv_food_promotion_price);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            tv_is_food_hidden = itemView.findViewById(R.id.tv_is_food_hidden);
            lny_promotion_price = itemView.findViewById(R.id.lny_promotion_price);
        }
    }

    private void initSlidingBanner(SlidingBanner_LilRound slidingBanner_lilRound, Restaurant_Menu_FoodEntity foodEntity) {

        /* transform image items into kabashowpics */
        List<AdsBanner> adsBanners = new ArrayList<>();
        try {
            // add front image also
            AdsBanner adsBanner = new AdsBanner();
            adsBanner.pic = foodEntity.pic;
            adsBanners.add(adsBanner);

            for (int i = 0; i < foodEntity.food_details_pictures.size(); i++) {
                  adsBanner = new AdsBanner();
                adsBanner.pic = foodEntity.food_details_pictures.get(i);
                adsBanners.add(adsBanner);
            }
            slidingBanner_lilRound.load(adsBanners, fragmentManager);
        } catch (Exception e) {
            e.printStackTrace();
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
            notifyDataSetChanged();
        }
    }
}
