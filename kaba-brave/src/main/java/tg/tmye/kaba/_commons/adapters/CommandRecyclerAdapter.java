package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.data.command.Command;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class CommandRecyclerAdapter extends RecyclerView.Adapter<CommandRecyclerAdapter.ViewHolder> {

    private List<Command> data;
    private final Context ctx;
    private Drawable line_divider;

    public CommandRecyclerAdapter (Context ctx, List<Command> data) {

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
                .inflate(R.layout.fragment_command_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Command command = this.data.get(position);
        /* set up restaurant restaurant_name*/
        holder.tv_header_resto_name.setText(command.restaurantEntity.name);
        /* set up restaurant restaurant_logo */
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS+command.restaurantEntity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.header_resto_cic);
        holder.rc_food_list.setAdapter(new CommandInnerFoodViewAdapter(ctx, data.get(position).command_list));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Command> data) {
        this.data = data;
        notifyDataSetChanged();
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
class CommandInnerFoodViewAdapter extends RecyclerView.Adapter<CommandInnerFoodViewAdapter.ViewHolder> {

    private final Context ctx;
    private final List<BasketInItem> command_list;
    private int COMMAND_FOOD_COUNT = 4;

    public CommandInnerFoodViewAdapter(Context ctx, List<BasketInItem> command_list) {
        this.ctx = ctx;
        this.command_list = command_list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_command_inner_food_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d(Constant.APP_TAG, "binding inner objects -- position : "+position);
//        initFoodAdapter (command_list.get(position).etags, holder.rc_food_tags);

        BasketInItem basketInItem = this.command_list.get(position);

     /* set up restaurant restaurant_name*/
        holder.tv_food_name.setText(basketInItem.name);
        holder.tv_quantity.setText(String.valueOf(basketInItem.quantity));
        holder.tv_food_price.setText(String.valueOf(basketInItem.price));
      /* set up restaurant restaurant_logo */
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS+ basketInItem.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.iv_food_pic);
    }

    @Override
    public int getItemCount() {
        return command_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final RecyclerView rc_food_tags;
        public CircleImageView iv_food_pic;
        public TextView tv_food_name;
        public TextView tv_food_price;
        public TextView tv_quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            rc_food_tags = itemView.findViewById(R.id.rc_food_tags);
            iv_food_pic = itemView.findViewById(R.id.iv_pic);
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
