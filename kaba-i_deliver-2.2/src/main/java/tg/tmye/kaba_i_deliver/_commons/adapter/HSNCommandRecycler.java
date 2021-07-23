package tg.tmye.kaba_i_deliver._commons.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.command.frag.HsnListFragment;
import tg.tmye.kaba_i_deliver.data.hsn.HSN;

/**
 * By abiguime on 19/07/2021.
 * email: 2597434002@qq.com
 */

public class HSNCommandRecycler extends RecyclerView.Adapter<HSNCommandRecycler.ViewHolder> {

    private List<HSN> hsnList;
    private final Context ctx;

    public HSNCommandRecycler (Context ctx, List<HSN> hsnList) {

        this.ctx = ctx;
        this.hsnList = hsnList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hsn_command_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        HSN hsn = this.hsnList.get(position);
        /* set up restaurant restaurant_name*/
        holder.tv_resto_name.setText(hsn.resto_name == null || "".equals(hsn.resto_name) ? hsn.restaurant_entity.name : hsn.resto_name);
        holder.tv_delivery_address.setText("ADRESSE: "+hsn.shipping_address); // adresses
//        holder.tv_delivery_info.setText("Informations: "+hsn.infos); // infos
        holder.tv_food_price.setText(hsn.food_pricing);
        holder.tv_shipping_price.setText(hsn.shipping_pricing);
        holder.tv_total.setText(hsn.total);
        holder.tv_hsn_id.setText(""+hsn.id);
        holder.root_card.setCardBackgroundColor(hsn.state == 0 ?
                ctx.getColor(R.color.white) : Color.parseColor("#FFEB3B"));

        holder.itemView.setOnClickListener(new OnHSNClickListener(hsn));
//        holder.tv_date_time.setText(UtilFunctions.timeStampToDate(ctx, hsn.last_update));
//        holder.itemView.setOnClickListener(new OnHSNClickListener(hsn));
    }

    private void strikeThru(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        textView.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        textView.setTextColor(ContextCompat.getColor(ctx, R.color.facebook_blue));
    }


    @Override
    public int getItemCount() {
        return hsnList.size();
    }

    public void updateData(List<HSN> data) {
        this.hsnList = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View rootView;
        public TextView tv_resto_name;
        public TextView tv_delivery_address;
//        public TextView tv_delivery_info;
        public TextView tv_food_price;
        public TextView tv_shipping_price;
        public TextView tv_total;
        public TextView tv_hsn_id;
        public CardView root_card;

        // make the recyclerview show his total elements

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            this.tv_resto_name = view.findViewById(R.id.tv_resto_name);
            this.tv_delivery_address = view.findViewById(R.id.tv_delivery_address);
//            this.tv_delivery_info = view.findViewById(R.id.tv_delivery_info);
            this.tv_food_price = view.findViewById(R.id.tv_food_price);
            this.tv_shipping_price = view.findViewById(R.id.tv_shipping_price);
            this.tv_total = view.findViewById(R.id.tv_total);
            this.tv_hsn_id = view.findViewById(R.id.tv_hsn_id);
            this.root_card = view.findViewById(R.id.root_card);
        }

    }

    public class OnHSNClickListener implements View.OnClickListener {

        private final HSN HSN;

        public OnHSNClickListener(HSN HSN) {
            this.HSN = HSN;
        }

        @Override
        public void onClick(View view) {

            ((HsnListFragment.OnFragmentInteractionListener)view.getContext()).onHSNInteraction(HSN);
        }
    }
}
