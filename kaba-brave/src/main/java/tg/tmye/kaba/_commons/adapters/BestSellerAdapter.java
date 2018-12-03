package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.menu.Fragmentz.RestaurantSubMenuFragment;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.bestsellers.BestSeller;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 09/07/2018.
 * email: 2597434002@qq.com
 */
public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.ViewHolder> {

    private final Context context;
    public List<BestSeller> bestSellers;

    public BestSellerAdapter (Context context, List<BestSeller> bestSellers) {

        this.context = context;
        this.bestSellers = bestSellers;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.best_seller_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final BestSeller bestSeller = bestSellers.get(position);

//        holder.tv_restaurant_name.setText(bestSeller..);
        holder.tv_restaurant_name.setText(bestSeller.food_entity.restaurant_entity.name);
        holder.tv_ranking.setText(""+(position+1)+".");
        holder.tv_food_name.setText(bestSeller.food_entity.name);
        holder.tv_food_price.setText(UtilFunctions.intToMoney(bestSeller.food_entity.price));

        /* get the actual day, thing that'll permit me to know other days */

        holder.iv_food_pic.setOnClickListener(new OnImageClickListener() {
            @Override
            public void onClick(View view) {

                /* on transforme ca en ads */
                AdsBanner adsBanner = new AdsBanner();
                adsBanner.name = bestSeller.food_entity.name;
                adsBanner.pic = bestSeller.food_entity.pic;
                adsBanner.description = bestSeller.food_entity.description;

                Intent intent = new Intent(context, ImagePreviewActivity.class);

                intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, new AdsBanner[]{adsBanner});

                context.startActivity(intent);
            }
        });

        /* Glide setup picture of the food */
        GlideApp.with(context)
                .load(Constant.SERVER_ADDRESS +"/"+bestSeller.food_entity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)context.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .into(holder.iv_food_pic);

        setUp3PastDaysOfWeekNames(holder);

        setUp3PastDaysOfWeekGraphs(holder, bestSeller.history);

        /* */
        holder.itemView.setOnClickListener(new onFoodClickListener(bestSeller.food_entity));
    }

    private void setUp3PastDaysOfWeekGraphs(ViewHolder holder, int[] history) {

        if (history[0] == -1) {
            holder.iv_day_1.setImageResource(R.drawable.ic_trending_down_black_24dp);
        } else if (history[0] == 0) {
            holder.iv_day_1.setImageResource(R.drawable.ic_trending_flat_black_24dp);
        } else if (history[0] == 1) {
            holder.iv_day_1.setImageResource(R.drawable.ic_trending_up_black_24dp);
        }

        if (history[1] == -1) {
            holder.iv_day_2.setImageResource(R.drawable.ic_trending_down_black_24dp);
        } else if (history[1] == 0) {
            holder.iv_day_2.setImageResource(R.drawable.ic_trending_flat_black_24dp);
        } else if (history[1] == 1) {
            holder.iv_day_2.setImageResource(R.drawable.ic_trending_up_black_24dp);
        }

        if (history[2] == -1) {
            holder.iv_day_3.setImageResource(R.drawable.ic_trending_down_black_24dp);
        } else if (history[2] == 0) {
            holder.iv_day_3.setImageResource(R.drawable.ic_trending_flat_black_24dp);
        } else if (history[2] == 1) {
            holder.iv_day_3.setImageResource(R.drawable.ic_trending_up_black_24dp);
        }
    }

    private void setUp3PastDaysOfWeekNames(ViewHolder holder) {
        Calendar instance = Calendar.getInstance();
        int day_of_week = instance.getTime().getDay();

        String[] days_of_week = context.getResources().getStringArray(R.array.week_days_short);

        /* indices of days */
        int day_1 = day_of_week - 3;
        int day_2 = day_of_week - 2;
        int day_3 = day_of_week - 1;

        holder.tv_day_1.setText(days_of_week[(day_1>0 ? day_1 : day_1+7) - 1]);
        holder.tv_day_2.setText(days_of_week[(day_2>0 ? day_2 : day_2+7) - 1]);
        holder.tv_day_3.setText(days_of_week[(day_3>0 ? day_3 : day_3+7) - 1]);

    }

    @Override
    public int getItemCount() {
        return bestSellers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /**/
        TextView tv_restaurant_name, tv_food_name, tv_food_price, tv_ranking;
        TextView tv_day_1, tv_day_2, tv_day_3;
        ImageView iv_day_1, iv_day_2, iv_day_3;
        CircleImageView iv_food_pic;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_restaurant_name = itemView.findViewById(R.id.tv_restaurant_name);
            tv_ranking = itemView.findViewById(R.id.tv_ranking);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);

            tv_day_1 = itemView.findViewById(R.id.tv_day_1);
            tv_day_2 = itemView.findViewById(R.id.tv_day_2);
            tv_day_3 = itemView.findViewById(R.id.tv_day_3);

            iv_day_1 = itemView.findViewById(R.id.iv_day_1);
            iv_day_2 = itemView.findViewById(R.id.iv_day_2);
            iv_day_3 = itemView.findViewById(R.id.iv_day_3);

            iv_food_pic = itemView.findViewById(R.id.iv_food_pic);
        }


    }

    private class onFoodClickListener implements View.OnClickListener {

        private final Restaurant_Menu_FoodEntity food;

        public onFoodClickListener(Restaurant_Menu_FoodEntity food) {
            this.food = food;
        }

        @Override
        public void onClick(View view) {

            ((RestaurantSubMenuFragment.OnFragmentInteractionListener)context).onFoodInteraction(food);
        }
    }
}
