package tg.tmye.kaba_i_deliver._commons.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        holder.tv_total.setText(""+command.total);
        holder.tv_date_time.setText(UtilFunctions.timeStampToDate(ctx, command.last_update));
        /* set up restaurant restaurant_logo */
        holder.rc_food_list.setAdapter(new CommandInnerFoodViewAdapter(ctx, data.get(position).food_list, new OnCommandClickListener(command)));
        holder.tv_header_customer_contact.setText(command.restaurant_entity.name.toUpperCase());
        holder.tv_quartier.setText(command.shipping_address.quartier.toUpperCase());


        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS + "/" + command.restaurant_entity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaDeliverApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.header_resto_cic);

        switch (command.state) {
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
        private final TextView tv_total, tv_date_time, tv_header_customer_contact, tv_quartier;
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
            tv_date_time = view.findViewById(R.id.tv_date_time);
            tv_header_customer_contact = view.findViewById(R.id.tv_header_customer_contact);
            tv_quartier = view.findViewById(R.id.tv_quartier);
            header_resto_cic = view.findViewById(R.id.header_resto_cic);
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

        Log.d(Constant.APP_TAG, "binding inner objects -- position : "+position);
//        initFoodAdapter (command_list.get(position).etags, holder.rc_food_tags);

        BasketInItem basketInItem = this.command_list.get(position);

        /* set up restaurant restaurant_name*/
        holder.tv_food_name.setText(basketInItem.name.toUpperCase());
        holder.tv_quantity.setText(String.valueOf(basketInItem.quantity));
        holder.tv_food_price.setText(String.valueOf(basketInItem.price));

        holder.itemView.setOnClickListener(onCommandClickListener);
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

        public ViewHolder(View itemView) {
            super(itemView);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
        }

    }


}
