package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.activity.home.views.fragment.F_Commands_3_Fragment;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.advert.AdsBanner;
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

        final Command command = this.data.get(position);
        /* set up restaurant restaurant_name*/
        holder.itemView.setOnClickListener(new OnCommandClickListener(command));

        holder.tv_header_resto_name.setText(command.restaurant_entity.name);
        holder.tv_total.setText(UtilFunctions.intToMoney(command.total_pricing) +" F");
        holder.tv_quartier.setText(command.shipping_address.quartier);
        holder.tv_date_time.setText(UtilFunctions.timeStampToDate(ctx, command.last_update));
        holder.tv_shipping_price.setText(UtilFunctions.intToMoney(command.shipping_pricing)+" F");
        /* set up restaurant restaurant_logo */
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS + "/" + command.restaurant_entity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.header_resto_cic);
        holder.rc_food_list.setAdapter(new CommandInnerFoodViewAdapter(ctx, data.get(position).food_list, new OnCommandClickListener(command)));

        switch (command.state) {
            case 0:
                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[0]);

                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_0));
                holder.tv_header_resto_name.setTextColor(ctx.getResources().getColor(R.color.text_command_state_0));
                break;
            case 1:
                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[1]);

                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_1));
                holder.tv_header_resto_name.setTextColor(ctx.getResources().getColor(R.color.text_command_state_1));
                break;
            case 2:
                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[2]);

                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_2));
                holder.tv_header_resto_name.setTextColor(ctx.getResources().getColor(R.color.text_command_state_2));
                break;
            case 3:
                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[3]);

                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_3));
                holder.tv_header_resto_name.setTextColor(ctx.getResources().getColor(R.color.text_command_state_3));
                break;
            case 4:
                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[4]);

                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_4));
                holder.tv_header_resto_name.setTextColor(ctx.getResources().getColor(R.color.text_command_state_4));
                break;
            case 5:
                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[5]);

                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_5));
                holder.tv_header_resto_name.setTextColor(ctx.getResources().getColor(R.color.text_command_state_5));
                break;
        }

        holder.header_resto_cic.setOnClickListener(new OnImageClickListener() {
            @Override
            public void onClick(View view) {

                /* on transforme ca en ads */
                AdsBanner adsBanner = new AdsBanner();
                adsBanner.name = command.restaurant_entity.name;
                adsBanner.pic = command.restaurant_entity.pic;
                adsBanner.description = command.restaurant_entity.name;

                Intent intent = new Intent(ctx, ImagePreviewActivity.class);

                intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, new AdsBanner[]{adsBanner});

                ctx.startActivity(intent);
            }
        });
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
        private final TextView tv_quartier, tv_header_resto_name, tv_total, tv_top_status, tv_date_time;
        public RecyclerView rc_food_list;
        public CircleImageView header_resto_cic;
        public RelativeLayout rel_top;
        public TextView tv_shipping_price;

        // make the recyclerview show his total elements

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            tv_total = view.findViewById(R.id.tv_total);
            rel_top = view.findViewById(R.id.rel_top);
            rc_food_list = view.findViewById(R.id.recycler_inner_food);
            header_resto_cic = view.findViewById(R.id.header_resto_cic);
            tv_header_resto_name = view.findViewById(R.id.tv_header_resto_name);
            tv_top_status = view.findViewById(R.id.tv_top_status);
            tv_date_time = view.findViewById(R.id.tv_date_time);
            tv_quartier = view.findViewById(R.id.tv_quartier);
            tv_shipping_price = view.findViewById(R.id.tv_shipping_price);

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

    protected class OnCommandClickListener implements View.OnClickListener {

        private final Command command;

        public OnCommandClickListener(Command command) {
            this.command = command;
        }

        @Override
        public void onClick(View view) {
            ((F_Commands_3_Fragment.OnFragmentInteractionListener)view.getContext()).onCommandInteraction(command);
        }
    }
}


/* inner food adapter */
class CommandInnerFoodViewAdapter extends RecyclerView.Adapter<CommandInnerFoodViewAdapter.ViewHolder> {

    private final Context ctx;
    private final List<BasketInItem> command_list;
    private int COMMAND_FOOD_COUNT = 4;
    private final CommandRecyclerAdapter.OnCommandClickListener onCommandClickListener;

    public CommandInnerFoodViewAdapter(Context ctx, List<BasketInItem> command_list, CommandRecyclerAdapter.OnCommandClickListener onCommandClickListener) {
        this.ctx = ctx;
        this.command_list = command_list;
        this.onCommandClickListener = onCommandClickListener;
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

        final BasketInItem basketInItem = this.command_list.get(position);

        /* set up restaurant restaurant_name*/
        holder.tv_food_name.setText(basketInItem.name);
        holder.tv_quantity.setText(String.valueOf(basketInItem.quantity));
        holder.tv_food_price.setText(UtilFunctions.intToMoney(String.valueOf(basketInItem.price)));
        /* set up restaurant restaurant_logo */
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS+ "/" +basketInItem.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.iv_food_pic);

        holder.itemView.setOnClickListener(onCommandClickListener);

        holder.iv_food_pic.setOnClickListener(new OnImageClickListener() {
            @Override
            public void onClick(View view) {

                /* on transforme ca en ads */
                AdsBanner adsBanner = new AdsBanner();
                adsBanner.name = basketInItem.name;
                adsBanner.pic = basketInItem.pic;
                adsBanner.description = basketInItem.details;

                Intent intent = new Intent(ctx, ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, new AdsBanner[]{adsBanner});
                ctx.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new FoodInteractionClickListener(basketInItem));
    }

    @Override
    public int getItemCount() {
        return command_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        public final RecyclerView rc_food_tags;
        public CircleImageView iv_food_pic;
        public TextView tv_food_name;
        public TextView tv_food_price;
        public TextView tv_quantity;
        public ImageButton ib_quantity_del;
        public ImageButton ib_quantity_add;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_food_pic = itemView.findViewById(R.id.iv_pic);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            ib_quantity_add = itemView.findViewById(R.id.ib_quantity_add);
            ib_quantity_del = itemView.findViewById(R.id.ib_quantity_del);
        }

    }

    private void initFoodAdapter (List<Food_Tag> food_tags, RecyclerView rc) {

        FoodTagAdapter tagAdapter = new FoodTagAdapter(ctx, food_tags, false);
        GridLayoutManager lny = new GridLayoutManager(ctx, FoodTagAdapter.TAG_SPAN_COUNT);

        rc.setLayoutManager(lny);
        rc.setAdapter(tagAdapter);
    }


    private class FoodInteractionClickListener implements View.OnClickListener {

        private final BasketInItem basketInItem;

        public FoodInteractionClickListener(BasketInItem basketInItem) {
            this.basketInItem = basketInItem;
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(ctx, FoodDetailsActivity.class);
            intent.putExtra(FoodDetailsActivity.FOOD_ID, basketInItem.id);
            ctx.startActivity(intent);
        }
    }
}
