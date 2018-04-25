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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba.activity.cart.ShoppingCartActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.data.shoppingcart.ShoppingBasketGroupItem;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */
public class ShoppingCartRecyclerAdapter extends RecyclerView.Adapter<ShoppingCartRecyclerAdapter.ViewHolder> {

    private final List<ShoppingBasketGroupItem> data;
    private final Context ctx;
    private Drawable line_divider;

    public ShoppingCartRecyclerAdapter (Context ctx, List<ShoppingBasketGroupItem> data) {

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

        holder.rc_food_list.setAdapter(new ShoppingCartInnerFoodViewAdapter(ctx, data.get(position).food_list));
        holder.tv_header_resto_name.setText(data.get(position).restaurant_entity.name);
        holder.tv_total.setText(""+data.get(position).total);

        /*  */
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS +"/"+ data.get(position).restaurant_entity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
//                .placeholder(R.drawable.white_placeholder)
                .centerCrop()
                .into(holder.header_resto_cic);

        holder.iv_edit.setOnClickListener(new DeleteCommandOnClickListener(data.get(position)));
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
        public TextView tv_total;
        public ImageView iv_edit;

        // make the recyclerview show his total elements

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            tv_total = view.findViewById(R.id.tv_total);
            rc_food_list = view.findViewById(R.id.recycler_inner_food);
            header_resto_cic = view.findViewById(R.id.header_resto_cic);
            tv_header_resto_name = view.findViewById(R.id.tv_header_resto_name);
            iv_edit = view.findViewById(R.id.iv_edit);

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

    private class DeleteCommandOnClickListener implements View.OnClickListener {
        private final ShoppingBasketGroupItem shoppingBasketGroupItem;

        public DeleteCommandOnClickListener(ShoppingBasketGroupItem shoppingBasketGroupItem) {
            this.shoppingBasketGroupItem = shoppingBasketGroupItem;
        }

        @Override
        public void onClick(View view) {
            /* delete this one */
            ((ShoppingCartActivity) ctx ).mToast(ctx.getResources().getString(R.string.feature_not_yet_available));
        }
    }
}


/* inner food adapter */
class ShoppingCartInnerFoodViewAdapter extends RecyclerView.Adapter<ShoppingCartInnerFoodViewAdapter.ViewHolder> {

    private final Context ctx;
    private final List<BasketInItem> shopping_basket_item;

    public ShoppingCartInnerFoodViewAdapter(Context ctx, List<BasketInItem> shopping_basket_item) {
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

        BasketInItem basketInItem = shopping_basket_item.get(position);
//        if (basketInItem.etags != null && basketInItem.etags.size() > 0)
//            initFoodAdapter (basketInItem.etags, holder.rc_food_tags);

        holder.tv_food_name.setText(basketInItem.name);
        holder.tv_quantity.setText(""+basketInItem.quantity);
        holder.tv_food_price.setText(""+basketInItem.price);

        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS +"/"+ basketInItem.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
//                .placeholder(R.drawable.white_placeholder)
                .centerCrop()
                .into(holder.iv_pic);
    }

    @Override
    public int getItemCount() {
        return shopping_basket_item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_pic;
        TextView tv_food_name;
        TextView tv_food_price;
        TextView tv_quantity;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_pic = itemView.findViewById(R.id.iv_pic);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
        }
    }

    private void initFoodAdapter (List<Food_Tag> food_tags, RecyclerView rc) {

        FoodTagAdapter tagAdapter = new FoodTagAdapter(ctx, food_tags, false);
        GridLayoutManager lny = new GridLayoutManager(ctx, FoodTagAdapter.TAG_SPAN_COUNT);

        rc.setLayoutManager(lny);
        rc.setAdapter(tagAdapter);
    }

}
