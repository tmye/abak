package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.shoppingcart.BasketFoodForDb;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketForView;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */
public class ShoppingCartRecyclerAdapter extends RecyclerView.Adapter<ShoppingCartRecyclerAdapter.ViewHolder> {

    private final List<ShoppingBasketForView> data;
    private final Context ctx;
    private Drawable line_divider;

    public ShoppingCartRecyclerAdapter (Context ctx, List<ShoppingBasketForView> data) {

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
                .inflate(R.layout.fragment_shoppingcart_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.rc_food_list.setAdapter(new ShoppingCartInnerFoodViewAdapter(ctx, data.get(position).shopping_basket_item));
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
class ShoppingCartInnerFoodViewAdapter extends RecyclerView.Adapter<ShoppingCartInnerFoodViewAdapter.ViewHolder> {

    private final Context ctx;
    private final List<BasketFoodForDb> shopping_basket_item;

    public ShoppingCartInnerFoodViewAdapter(Context ctx, List<BasketFoodForDb> shopping_basket_item) {
        this.ctx = ctx;
        this.shopping_basket_item = shopping_basket_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_command_inner_food_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BasketFoodForDb basketFoodForDb = shopping_basket_item.get(position);
        if (basketFoodForDb.etags != null && basketFoodForDb.etags.size() > 0)
            initFoodAdapter (basketFoodForDb.etags, holder.rc_food_tags);
    }

    @Override
    public int getItemCount() {
        return shopping_basket_item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final RecyclerView rc_food_tags;

        public ViewHolder(View itemView) {
            super(itemView);
            rc_food_tags = itemView.findViewById(R.id.rc_food_tags);
        }
    }

    private void initFoodAdapter (List<Food_Tag> food_tags, RecyclerView rc) {

        FoodTagAdapter tagAdapter = new FoodTagAdapter(ctx, food_tags, false);
        GridLayoutManager lny = new GridLayoutManager(ctx, FoodTagAdapter.TAG_SPAN_COUNT);

        rc.setLayoutManager(lny);
        rc.setAdapter(tagAdapter);
    }

}
