package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.UserAuth.login.LoginActivity;
import tg.tmye.kaba.activity.cart.ShoppingCartActivity;
import tg.tmye.kaba.activity.cart.presenter.ShoppingCartPresenter;
import tg.tmye.kaba.activity.favorite.FavoriteActivity;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
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
    private final ShoppingCartPresenter presenter;
    private Drawable line_divider;

    public ShoppingCartRecyclerAdapter (Context ctx, List<ShoppingBasketGroupItem> data, ShoppingCartPresenter presenter) {

        this.ctx = ctx;
        this.data = data;
        line_divider = ContextCompat.getDrawable(ctx, R.drawable.command_inner_food_item_divider);
        this.presenter = presenter;
        adapterMap = new HashMap<>();
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

    Map<Integer, ShoppingCartInnerFoodViewAdapter> adapterMap;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.groupItemPosition = position;

        ShoppingCartInnerFoodViewAdapter adapter = adapterMap.get(position);
        if (adapter == null) {
            adapter = new ShoppingCartInnerFoodViewAdapter(ctx, holder, position);
            adapterMap.put(position, adapter);
        }

        holder.rc_food_list.setAdapter(adapter);

        holder.tv_header_resto_name.setText(data.get(position).restaurant_entity.name);
        holder.tv_total.setText(""+data.get(position).total);

        /*  */
        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS +"/"+ data.get(position).restaurant_entity.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .centerCrop()
                .into(holder.header_resto_cic);

        // add restaurant item here
        holder.tv_buy_now.setOnClickListener(new BuyNowOnClickListener(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItemAt(int groupPosition, int itemPosition) {

        if (data.get(groupPosition).food_list.size() == 1) {
            try {
                data.remove(groupPosition);
                notifyItemRemoved(groupPosition);
            } catch (Exception e) {
                e.printStackTrace();
                /* ask presenter to update data */
                ((ShoppingCartActivity)ctx).updateBasketItem();
            }

        } else {
            try {
                data.get(groupPosition).food_list.remove(itemPosition);
                ShoppingCartInnerFoodViewAdapter adapter = adapterMap.get(groupPosition);
                if (adapter != null) {
                    adapter.notifyItemRemoved(itemPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((ShoppingCartActivity)ctx).updateBasketItem();
            }
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View rootView;
        private final TextView tv_header_resto_name;
        public RecyclerView rc_food_list;
        public CircleImageView header_resto_cic;
        public TextView tv_total;
        public TextView tv_buy_now;

        // make the recyclerview show his total elements
        int groupItemPosition;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            tv_total = view.findViewById(R.id.tv_total);
            rc_food_list = view.findViewById(R.id.recycler_inner_food);
            header_resto_cic = view.findViewById(R.id.header_resto_cic);
            tv_header_resto_name = view.findViewById(R.id.tv_header_resto_name);
            tv_buy_now = view.findViewById(R.id.tv_buy_now);

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

        public void updatePrice() {
            /* run the list, get the price, and upate tv_buy... */
            ShoppingBasketGroupItem tmpGroup = data.get(groupItemPosition);
            int total = 0;
            for (int i = 0; i < tmpGroup.food_list.size(); i++) {
                total += (Integer.valueOf(tmpGroup.food_list.get(i).price)  * tmpGroup.food_list.get(i).quantity);
            }
            tmpGroup.total = total;
            tv_total.setText(""+tmpGroup.total);
        }

    }

    private int MAX_FOOD_PER_DELIVERY = 9;

    /* inner food adapter */
    class ShoppingCartInnerFoodViewAdapter extends RecyclerView.Adapter<ShoppingCartInnerFoodViewAdapter.ViewHolder> {

        private final Context ctx;
        //        private final List<BasketInItem> shopping_basket_item;
        private final int groupItemoPosition;
        private final ShoppingCartRecyclerAdapter.ViewHolder viewHolder;

        /* can i get the englobing entity */

        public ShoppingCartInnerFoodViewAdapter(Context ctx, ShoppingCartRecyclerAdapter.ViewHolder viewHolder, int groupItemoPosition) {
            this.ctx = ctx;
            this.viewHolder = viewHolder;
            this.groupItemoPosition = groupItemoPosition;
//            this.shopping_basket_item = groupItem.food_list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder
                    (LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_shoppingcart_inner_food_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            BasketInItem basketInItem = data.get(groupItemoPosition).food_list.get(position);
            basketInItem.quantity = 1;

            holder.innerElementPosition = position;
            holder.tv_food_name.setText(basketInItem.name);

            holder.quantity = basketInItem.quantity;
            holder.ed_quantity.setText("" + basketInItem.quantity);

            holder.tv_food_price.setText("" + basketInItem.price);

            GlideApp.with(ctx)
                    .load(Constant.SERVER_ADDRESS + "/" + basketInItem.pic)
                    .transition(GenericTransitionOptions.with(((MyKabaApp) ctx.getApplicationContext()).getGlideAnimation()))
//                .placeholder(R.drawable.white_placeholder)
                    .centerCrop()
                    .into(holder.iv_pic);

            holder.ib_basket_delete.setOnClickListener(new DeleteFromItemBasket(basketInItem, groupItemoPosition, position));
            holder.itemView.setOnClickListener(new FoodInteractionClickListener(basketInItem));
        }

        @Override
        public int getItemCount() {
            return  data.get(groupItemoPosition).food_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageButton ib_quantity_del;
            ImageButton ib_quantity_add;
            ImageButton ib_basket_delete;
            ImageView iv_pic;
            TextView tv_food_name;
            TextView tv_food_price;
            EditText ed_quantity;

            int innerElementPosition;

            public ViewHolder(View itemView) {

                super(itemView);
                iv_pic = itemView.findViewById(R.id.iv_pic);
                tv_food_name = itemView.findViewById(R.id.tv_food_name);
                tv_food_price = itemView.findViewById(R.id.tv_food_price);
                ed_quantity = itemView.findViewById(R.id.ed_quantity);
                ib_quantity_add = itemView.findViewById(R.id.ib_quantity_del);
                ib_quantity_del = itemView.findViewById(R.id.ib_quantity_add);
                ib_basket_delete = itemView.findViewById(R.id.ib_basket_delete);

                ib_quantity_add.setOnClickListener(this);
                ib_quantity_del.setOnClickListener(this);
            }


            public int quantity;


            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ib_quantity_add:

                        /* get the total of the list */
                        if (getInnerListTotalCount() >= MAX_FOOD_PER_DELIVERY) {
                            mToast(ctx.getResources().getString(R.string.quantity_to_much));
                            return;
                        }

                        if (data.get(groupItemoPosition).food_list.get(innerElementPosition).quantity == 9) {
                            mToast(ctx.getResources().getString(R.string.quantity_to_much));
                        } else {
                            data.get(groupItemoPosition).food_list.get(innerElementPosition).quantity++;
                            ed_quantity.setText("" + data.get(groupItemoPosition).food_list.get(innerElementPosition).quantity);
                        }
                        /* adjust price of englobing item */
                        viewHolder.updatePrice();
                        break;
                    case R.id.ib_quantity_del:

                        if (data.get(groupItemoPosition).food_list.get(innerElementPosition).quantity == 1) {
                            mToast(ctx.getResources().getString(R.string.quantity_cannot_less));
                        } else {
                            data.get(groupItemoPosition).food_list.get(innerElementPosition).quantity--;
                            ed_quantity.setText("" + data.get(groupItemoPosition).food_list.get(innerElementPosition).quantity);
                        }
                        /* adjust price of englobing item */
                        viewHolder.updatePrice();
                        break;
                }
            }

            private void mToast(String message) {
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            }
        }

        private int getInnerListTotalCount() {
            int total = 0;
            for (int i = 0; i < data.get(groupItemoPosition).food_list.size(); i++) {
                total += data.get(groupItemoPosition).food_list.get(i).quantity;
            }
            return total;
        }

        private void initFoodAdapter(List<Food_Tag> food_tags, RecyclerView rc) {

            FoodTagAdapter tagAdapter = new FoodTagAdapter(ctx, food_tags, false);
            GridLayoutManager lny = new GridLayoutManager(ctx, FoodTagAdapter.TAG_SPAN_COUNT);

            rc.setLayoutManager(lny);
            rc.setAdapter(tagAdapter);
        }

        private class DeleteFromItemBasket implements View.OnClickListener {
            private final BasketInItem basketInItem;
            private final int itemPosition;
            private final int groupPosition;

            public DeleteFromItemBasket(BasketInItem basketInItem, int groupPosition, int itemPosition) {
                this.basketInItem = basketInItem;
                this.groupPosition = groupPosition;
                this.itemPosition = itemPosition;
            }

            @Override
            public void onClick(View view) {

                /* confirm and delete basket item */
                action();
            }


            private void action() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx
                )
                        .setTitle(R.string.confirmation)
                        .setMessage(R.string.are_you_sure_delete)
                        .setCancelable(true)
                        .setOnCancelListener(null)
                        .setPositiveButton(R.string.bt_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                /* the presenter should tell the activity to keep a trace of loading */
                                presenter.deleteBasketItem(basketInItem, groupPosition, itemPosition);
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        int color = ctx.getResources().getColor(R.color.colorPrimary);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
                    }
                });

                dialog.show();
            }
        }
    }

    private class BuyNowOnClickListener implements View.OnClickListener {

        private final ShoppingBasketGroupItem groupItem;
        private final RestaurantEntity restaurantEntity;

        public BuyNowOnClickListener(ShoppingBasketGroupItem groupItem) {
            this.groupItem = groupItem;
            this.restaurantEntity = groupItem.restaurant_entity;
        }

        @Override
        public void onClick(View view) {
            /* launch an activity to finish with the transaction around this command */
            ((ShoppingCartActivity)ctx).confirmSelectionBox(restaurantEntity, confirmSelectionBox());
        }

        /**
         * args.
         *  - buy
         *  - addToBasket
         */
        private HashMap<Restaurant_Menu_FoodEntity, Integer> confirmSelectionBox() {

            HashMap<Restaurant_Menu_FoodEntity, Integer> selection_map = new HashMap<>();
            // get list of everything to buy
            for (int i = 0; i < groupItem.food_list.size(); i++) {
                selection_map.put(UtilFunctions.basketItemToFoodEntity(groupItem.food_list.get(i)), groupItem.food_list.get(i).quantity);
            }
            return selection_map;
        }
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
