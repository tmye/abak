package tg.tmye.kaba.partner._commons.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.partner.ILog;
import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba.partner._commons.utils.UtilFunctions;
import tg.tmye.kaba.partner.activities.commands.frag.RestaurantSubCommandListFragment;
import tg.tmye.kaba.partner.data.command.Command;
import tg.tmye.kaba.partner.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.partner.syscore.Constant;

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
        line_divider = ctx.getResources().getDrawable(R.drawable.command_inner_food_item_divider);
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
        holder.tv_total.setText(UtilFunctions.intToMoney(command.total));
        holder.tv_date_time.setText(UtilFunctions.timeStampToDate(ctx, command.last_update));
        /* set up restaurant restaurant_logo */
        holder.rc_food_list.setAdapter(new CommandInnerFoodViewAdapter(ctx, data.get(position).food_list, new OnCommandClickListener(command)));
        holder.tv_header_customer_contact.setText(command.shipping_address.phone_number);
        holder.tv_header_customer_address_quartier.setText(command.shipping_address.quartier);

        holder.tv_pay_at_arrival.setVisibility(command.is_payed_at_arrival ? View.VISIBLE : View.GONE);


        switch (command.state) {
            case 0:
//                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[0]);
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_0));
                break;
            case 1:
//                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[1]);
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_1));
                break;
            case 2:
//                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[2]);
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_2));
                break;
            case 3:
//                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[3]);
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_3));
                break;
            case 4:
//                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[4]);
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_4));
                break;
            case 5:
//                holder.tv_top_status.setText(ctx.getResources().getStringArray(R.array.status_list)[5]);
                holder.rel_top.setBackgroundColor(ctx.getResources().getColor(R.color.command_state_5));
                break;
        }
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
        private final TextView tv_total, /*tv_top_status,*/ tv_date_time, tv_header_customer_contact, tv_header_customer_address_quartier, tv_pay_at_arrival;
        public RecyclerView rc_food_list;
        public CircleImageView header_resto_cic;
        public RelativeLayout rel_top;

        // make the recyclerview show his total elements

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            tv_total = view.findViewById(R.id.tv_total);
            rel_top = view.findViewById(R.id.rel_top);
            rc_food_list = view.findViewById(R.id.recycler_inner_food);
//            tv_top_status = view.findViewById(R.id.tv_top_status);
            tv_date_time = view.findViewById(R.id.tv_date_time);
            tv_header_customer_contact = view.findViewById(R.id.tv_header_customer_contact);
            tv_header_customer_address_quartier = view.findViewById(R.id.tv_header_customer_address_quartier);
            tv_pay_at_arrival = view.findViewById(R.id.tv_pay_at_arrival);
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

            ((RestaurantSubCommandListFragment.OnFragmentInteractionListener)view.getContext()).onCommandInteraction(command);
        }
    }
}


/* inner food adapter */
class CommandInnerFoodViewAdapter extends RecyclerView.Adapter<CommandInnerFoodViewAdapter.ViewHolder> {

    private final Context ctx;
    private final List<BasketInItem> command_list;
    private final CommandRecyclerAdapter.OnCommandClickListener onCommandClickListener;
    private int COMMAND_FOOD_COUNT = 4;

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

       ILog.print("binding inner objects -- position : "+position);
//        initFoodAdapter (command_list.get(position).etags, holder.rc_food_tags);

        BasketInItem basketInItem = this.command_list.get(position);

     /* set up restaurant restaurant_name*/
        holder.tv_food_name.setText(basketInItem.name);
        holder.tv_quantity.setText(String.valueOf(basketInItem.quantity));
        holder.tv_food_price.setText(String.valueOf(basketInItem.price));

        holder.itemView.setOnClickListener(onCommandClickListener);

      /* set up restaurant restaurant_logo */
        if (basketInItem.promotion == 0) { /* IS IN promotion*/
            holder.tv_food_price.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.tv_food_price.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary_yellow));
            holder.tv_food_promotion_price.setVisibility(View.GONE);
        } else if (basketInItem.promotion == 1) { /* is not in promotion */
            holder.tv_food_price.setTextColor(ContextCompat.getColor(ctx, R.color.black));
            holder.tv_food_price.setPaintFlags(holder.tv_food_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_food_promotion_price.setVisibility(View.VISIBLE);
            holder.tv_food_promotion_price.setText(""+basketInItem.promotion_price);
            holder.tv_food_promotion_price.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary));
        }

        holder.itemView.setOnClickListener(new ShowBasketItemDetails(basketInItem));
    }

    @Override
    public int getItemCount() {
        return command_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        public final RecyclerView rc_food_tags;
//        public CircleImageView iv_food_pic;
        public TextView tv_food_name;
        public TextView tv_food_price;
        public TextView tv_quantity;
        public TextView tv_food_promotion_price;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_food_promotion_price = itemView.findViewById(R.id.tv_food_promotion_price);
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
            builder.setIcon(R.mipmap.kaba_icon_orange);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, ctx.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });

            Button b = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
            if(b != null) {
                b.setTextColor(ctx.getResources().getColor(R.color.white));
                b.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
            }

            alertDialog.show();
        }

    }
}
