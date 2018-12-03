package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.favorite.FavoriteActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.favorite.Favorite;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */
public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {

    private final List<Favorite> data;
    private final Context ctx;
    private Drawable line_divider;

    public FavoriteRecyclerAdapter (Context ctx, List<Favorite> data) {

        this.ctx = ctx;
        this.data = data;
        line_divider = ContextCompat.getDrawable(ctx, R.drawable.command_inner_food_item_divider);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorite_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tv_header_resto_name.setText(data.get(position).restaurant_entity.name.toUpperCase());
        holder.rc_food_list.setAdapter(new FavoriteInnerFoodViewAdapter(ctx, data.get(position).food_list, data.get(position).restaurant_entity));

        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS +"/"+ data.get(position).restaurant_entity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.header_resto_cic);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View rootView;
        private final TextView tv_header_resto_name;
        public RecyclerView rc_food_list;
        public CircleImageView header_resto_cic;

        // make the recyclerview show his total elements

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            rc_food_list = view.findViewById(R.id.recycler_inner_food);
            header_resto_cic = view.findViewById(R.id.header_resto_cic);
            tv_header_resto_name = view.findViewById(R.id.tv_header_resto_name);

            initNetstedRecyclerview();
        }

        private void initNetstedRecyclerview() {

            rc_food_list.addItemDecoration(new CommandInnerFoodLineDecorator(line_divider));
            rc_food_list.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
            {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            // disable inner list scrolling
            rc_food_list.setNestedScrollingEnabled(false);
        }
    }
}


/* inner food adapter */
class FavoriteInnerFoodViewAdapter extends RecyclerView.Adapter<FavoriteInnerFoodViewAdapter.ViewHolder> {

    private final Context ctx;
    private final List<Restaurant_Menu_FoodEntity> favorite_item;
    private final RestaurantEntity restaurantEntity;

    public FavoriteInnerFoodViewAdapter(Context ctx, List<Restaurant_Menu_FoodEntity> favorite_item, RestaurantEntity restaurantEntity) {
        this.ctx = ctx;
        this.favorite_item = favorite_item;
        this.restaurantEntity = restaurantEntity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_favorite_inner_food_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Restaurant_Menu_FoodEntity favoriteFoodEntity = favorite_item.get(position);
        holder.tv_food_name.setText(favoriteFoodEntity.name.toUpperCase());

        holder.itemView.setOnClickListener(new FoodOnClickListener(favoriteFoodEntity, restaurantEntity));
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS +"/"+ favorite_item.get(position).pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.iv_food_pic);

        holder.iv_food_pic.setOnClickListener(new OnImageClickListener() {
            @Override
            public void onClick(View view) {

                /* on transforme ca en ads */
                AdsBanner adsBanner = new AdsBanner();
                adsBanner.name = favoriteFoodEntity.name;
                adsBanner.pic = favoriteFoodEntity.pic;
                adsBanner.description = favoriteFoodEntity.details;

                Intent intent = new Intent(ctx, ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, new AdsBanner[]{adsBanner});
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorite_item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_food_pic;
        TextView tv_food_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            iv_food_pic = itemView.findViewById(R.id.iv_pic);
        }
    }

    private void initFoodAdapter (List<Food_Tag> food_tags, RecyclerView rc) {

        FoodTagAdapter tagAdapter = new FoodTagAdapter(ctx, food_tags, false);
        GridLayoutManager lny = new GridLayoutManager(ctx, FoodTagAdapter.TAG_SPAN_COUNT);

        rc.setLayoutManager(lny);
        rc.setAdapter(tagAdapter);
    }

    private class FoodOnClickListener implements View.OnClickListener {

        private final Restaurant_Menu_FoodEntity favoriteFoodEntity;
        private final RestaurantEntity restaurantEntity;

        public FoodOnClickListener(Restaurant_Menu_FoodEntity favoriteFoodEntity, RestaurantEntity restaurantEntity) {
            this.favoriteFoodEntity = favoriteFoodEntity;
            this.restaurantEntity = restaurantEntity;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent((FavoriteActivity)ctx, FoodDetailsActivity.class);
            intent.putExtra(FoodDetailsActivity.FOOD
                    , favoriteFoodEntity);
            intent.putExtra(FoodDetailsActivity.RESTAURANT_ENTITY, restaurantEntity);
            ctx.startActivity(intent);
        }
    }
}
