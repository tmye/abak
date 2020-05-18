package tg.tmye.kaba_i_deliver._commons.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba_i_deliver._commons.utils.UtilFunctions;
import tg.tmye.kaba_i_deliver.activity.command.frag.RestaurantSubCommandListFragment;
import tg.tmye.kaba_i_deliver.data.command.Command;
import tg.tmye.kaba_i_deliver.data.shoppingcart.BasketInItem;
import tg.tmye.kaba_i_deliver.syscore.Constant;
import tg.tmye.kaba_i_deliver.syscore.GlideApp;
import tg.tmye.kaba_i_deliver.syscore.ILog;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

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
        holder.itemView.setOnClickListener(new OnCommandClickListener(command));

        holder.tv_date_time.setText(UtilFunctions.timeStampToDate(ctx, command.last_update));
        /* set up restaurant restaurant_logo */
        holder.rc_food_list.setAdapter(new CommandInnerFoodViewAdapter(ctx, data.get(position), new OnCommandClickListener(command)));
        holder.tv_quartier.setText(command.shipping_address.quartier.toUpperCase());

        holder.tv_pay_at_arrival.setVisibility(command.is_payed_at_arrival ? View.VISIBLE : View.GONE);
        holder.tv_shipping_price.setText(UtilFunctions.intToMoney(command.shipping_pricing)+" F");

        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS + "/" + command.restaurant_entity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaDeliverApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.header_resto_cic);

        if (command.preorder == 1 && command.preorder_hour != null){
            holder.lny_preorder_infos.setVisibility(View.VISIBLE);
            holder.tv_delivery_day.setText(UtilFunctions.timeStampToDayDate(ctx, command.preorder_hour.start));
            holder.tv_delivery_time.setText(UtilFunctions.timeStampToHourMinute(ctx, command.preorder_hour.start)+" à "+UtilFunctions.timeStampToHourMinute(ctx, command.preorder_hour.end));
        } else {
            holder.lny_preorder_infos.setVisibility(View.GONE);
        }

        // if pre-order, apply promotion to delivery price
        if (command.preorder == 1) {
            try {
                int pdis = Integer.valueOf(command.preorder_discount);
                if (pdis < 0){
                    throw new Exception();
                }
                int shippingP = Integer.valueOf(command.shipping_pricing);
                shippingP = (100-pdis)*shippingP/100;
                holder.tv_shipping_price.setText(shippingP+" F");

            } catch (Exception e) {
                e.printStackTrace();
                holder.tv_shipping_price.setText(UtilFunctions.intToMoney(command.shipping_pricing)+" F");
            }
        } else
            holder.tv_shipping_price.setText(UtilFunctions.intToMoney(command.shipping_pricing)+" F");


        switch (command.state) {
            case 0:
                holder.tv_total.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_0));
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_0));
                break;
            case 1:
                holder.tv_total.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_1));
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_1));
                break;
            case 2:
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_yet));
                holder.tv_total.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_yet));
//                holder.tv_header_customer_contact.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_yet));
                break;
            case 3:
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_done));
                holder.tv_total.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_done));
//                holder.tv_header_customer_contact.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_done));
                break;
        }

        // set up prices of the order whether promotion or not
        if (command.is_preorder == 1){
            // we setup pre-order price
            holder.tv_shipping_price.setText(command.preorder_shipping_pricing);
            holder.tv_total.setText(command.preorder_total_pricing);
        } else if (command.is_promotion == 1) {
            holder.tv_shipping_price.setText(command.promotion_shipping_pricing);
            holder.tv_total.setText(command.promotion_total_pricing);
        } else if (command.is_promotion == 0 && command.is_preorder == 0) {
            // have nothing
            holder.tv_shipping_price.setText(command.shipping_pricing);
            holder.tv_total.setText(command.total_pricing);
        } else {
            holder.tv_shipping_price.setText(command.shipping_pricing);
            holder.tv_total.setText(command.total_pricing);
        }

holder.tv_header_resto_name.setText(command.restaurant_entity.name);
holder.tv_order_id.setText(String.valueOf(command.id));
        holder.itemView.setOnClickListener(new OnCommandClickListener(command));
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
        private final TextView tv_quartier, tv_header_resto_name, tv_total, tv_date_time, tv_pay_at_arrival;
        public RecyclerView rc_food_list;
        public CircleImageView header_resto_cic;
        public RelativeLayout rel_top;
        public TextView tv_shipping_price;
        public LinearLayout lny_preorder_infos;
        public TextView tv_delivery_time, tv_delivery_day, tv_order_id;

        // make the recyclerview show his total elements

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            tv_total = view.findViewById(R.id.tv_total);
            tv_order_id = view.findViewById(R.id.tv_order_id);
            rel_top = view.findViewById(R.id.rel_top);
            rc_food_list = view.findViewById(R.id.recycler_inner_food);
            header_resto_cic = view.findViewById(R.id.header_resto_cic);
            tv_header_resto_name = view.findViewById(R.id.tv_header_resto_name);
            tv_date_time = view.findViewById(R.id.tv_date_time);
            tv_quartier = view.findViewById(R.id.tv_quartier);
            tv_shipping_price = view.findViewById(R.id.tv_shipping_price);
            tv_pay_at_arrival = view.findViewById(R.id.tv_pay_at_arrival);
            lny_preorder_infos = view.findViewById(R.id.lny_preorder_infos);
            tv_delivery_time = view.findViewById(R.id.tv_delivery_time);
            tv_delivery_day = view.findViewById(R.id.tv_delivery_day);

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

    public class OnCommandClickListener implements View.OnClickListener {

        private final Command command;

        public OnCommandClickListener(Command command) {
            this.command = command;
        }

        @Override
        public void onClick(View view) {

            ((RestaurantSubCommandListFragment.OnFragmentInteractionListener)view.getContext()).onCommandInteraction(command);
        }
    }
}


/* inner food adapter */
class CommandInnerFoodViewAdapter extends RecyclerView.Adapter<CommandInnerFoodViewAdapter.ViewHolder> {

    private final Context ctx;
    private final List<BasketInItem> command_list;
    private final CommandRecyclerAdapter.OnCommandClickListener onCommandClickListener;
    private final Command command;
    private int COMMAND_FOOD_COUNT = 4;

    public CommandInnerFoodViewAdapter(Context ctx, Command command, CommandRecyclerAdapter.OnCommandClickListener onCommandClickListener) {
        this.ctx = ctx;
        this.command = command;
        this.command_list = command.food_list;
        this.onCommandClickListener = onCommandClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_command_inner_food_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ILog.print("binding inner objects -- position : "+position);
//        initFoodAdapter (command_list.get(position).etags, holder.rc_food_tags);

        BasketInItem basketInItem = this.command_list.get(position);

        /* set up restaurant restaurant_name*/
        holder.tv_food_name.setText(basketInItem.name.toUpperCase());
        holder.tv_quantity.setText(String.valueOf(basketInItem.quantity));
        holder.tv_food_price.setText(String.valueOf(basketInItem.price));

        /* set up restaurant restaurant_logo */
        if (basketInItem.promotion == 1 && command.is_preorder != 1) { /* is not in promotion */
            holder.tv_food_price.setTextColor(ContextCompat.getColor(ctx, R.color.black));
            holder.tv_food_price.setPaintFlags(holder.tv_food_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_food_promotion_price.setVisibility(View.VISIBLE);
            holder.tv_food_promotion_price.setText(""+basketInItem.promotion_price);
            holder.tv_food_promotion_price.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary));
        } else {
            holder.tv_food_price.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.tv_food_price.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary_yellow));
            holder.tv_food_promotion_price.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new ShowBasketItemDetails(basketInItem));
    }

    @Override
    public int getItemCount() {
        return command_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        public final RecyclerView rc_food_tags;
        public TextView tv_food_name;
        public TextView tv_food_price;
        public TextView tv_quantity;
        public TextView tv_food_promotion_price;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            tv_food_promotion_price = itemView.findViewById(R.id.tv_food_promotion_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
        }
    }


    private class ShowBasketItemDetails implements View.OnClickListener {

        private final BasketInItem basketInItem;

        public ShowBasketItemDetails(BasketInItem basketInItem) {
            this.basketInItem = basketInItem;
        }

        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setTitle(basketInItem.name);
            builder.setMessage(basketInItem.description);
            builder.setIcon(R.mipmap.kaba_icon_green);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, ctx.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }
}
