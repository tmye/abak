package tg.tmye.kaba.restaurant._commons.adapter;

/**
 * By abiguime on 2020/6/9.
 * email: 2597434002@qq.com
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.activities.menu.EditSingleFoodActivity;
import tg.tmye.kaba.restaurant.activities.menu.EditSingleMenuActivity;
import tg.tmye.kaba.restaurant.activities.menu.RestaurantMenuActivity;
import tg.tmye.kaba.restaurant.cviews.SlidingBanner_LilRound;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.restaurant.data.advert.AdsBanner;


/**
 * By abiguime on 17/06/2018.
 * email: 2597434002@qq.com
 */
public class EditFoodListAdapter extends RecyclerView.Adapter<EditFoodListAdapter.ViewHolder> {

    private final List<Restaurant_Menu_FoodEntity> data;
    private final Context context;
    private final FragmentManager fragmentManager;

    public EditFoodListAdapter(Context context, FragmentManager fragmentManager, List<Restaurant_Menu_FoodEntity> menu_list) {

        this.context = context;
        this.fragmentManager = fragmentManager;
        this.data = menu_list;
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

        initSlidingBanner(holder.sliding_banner, data.get(position));

        if (entity.is_hidden == 0){
            holder.tv_food_price.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.tv_food_promotion_price.setPaintFlags(holder.tv_food_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.iv_hidden.setVisibility(View.GONE);
            holder.tv_is_food_hidden.setVisibility(View.GONE);
        } else {
            holder.tv_food_price.setPaintFlags(holder.tv_food_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_food_promotion_price.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.iv_hidden.setVisibility(View.VISIBLE);
            holder.tv_is_food_hidden.setVisibility(View.VISIBLE);
        }

        holder.bt_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditSingleFoodActivity.class);
                intent.putExtra("food", entity);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SlidingBanner_LilRound sliding_banner;
        TextView tv_food_name, tv_description, tv_is_food_hidden, tv_food_promotion_price, tv_food_price;
        ImageView iv_hidden;
        Button/* bt_hide,*/ bt_edit_name;
        Switch switch_is_promotion;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            switch_is_promotion = itemView.findViewById(R.id.switch_is_promotion);
            tv_description = itemView.findViewById(R.id.tv_description);
            sliding_banner = itemView.findViewById(R.id.sliding_banner);
            iv_hidden = itemView.findViewById(R.id.iv_hidden);
//            bt_hide = itemView.findViewById(R.id.bt_hide);
            bt_edit_name = itemView.findViewById(R.id.bt_edit_name);
            tv_food_promotion_price = itemView.findViewById(R.id.tv_food_promotion_price);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            tv_is_food_hidden = itemView.findViewById(R.id.tv_is_food_hidden);
        }
    }

    private void initSlidingBanner(SlidingBanner_LilRound slidingBanner_lilRound, Restaurant_Menu_FoodEntity foodEntity) {

        /* transform image items into kabashowpics */
        List<AdsBanner> adsBanners = new ArrayList<>();
        try {
            for (int i = 0; i < foodEntity.food_details_pictures.size(); i++) {
                AdsBanner adsBanner = new AdsBanner();
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
