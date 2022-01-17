package tg.tmye.kaba.partner._commons.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner.data.delivery.KabaShippingMan;
import tg.tmye.kaba.partner.syscore.Constant;
import tg.tmye.kaba.partner.syscore.GlideApp;

/**
 * By abiguime on 2018/10/1.
 * email: 2597434002@qq.com
 */
public class ShippingManRecyclerAdapter extends RecyclerView.Adapter<ShippingManRecyclerAdapter.ViewHolder> {


    private final Context context;
    private final List<KabaShippingMan> kabaShippingManList;
    private int my_choice = -1;

    public ShippingManRecyclerAdapter (Context context, List<KabaShippingMan> kabaShippingManList){
        this.context = context;
        this.kabaShippingManList = kabaShippingManList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_shippingman_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        KabaShippingMan kabaShippingMan = kabaShippingManList.get(position);

        holder.tv_kaba_man_name.setText(kabaShippingMan.name);
        holder.tv_kaba_man_no.setText(kabaShippingMan.workcontact);
        GlideApp.with(context)
                .load(Constant.SERVER_ADDRESS + "/" + kabaShippingMan.pic)
                .centerCrop()
                .into(holder.cic_deliveryman);

        holder.tv_kaba_man_no.setText(kabaShippingMan.workcontact);

        holder.tv_kaba_man_no.setCompoundDrawablesWithIntrinsicBounds(
                VectorDrawableCompat.create(context.getResources(), R.drawable.ic_call_black_24dp, null),
                null, null, null);

        if (kabaShippingMan.is_selected == KabaShippingMan.SELECTED){
            holder.iv_is_checked.setVisibility(View.VISIBLE);
        } else {
            holder.iv_is_checked.setVisibility(View.INVISIBLE);
        }

        if (kabaShippingMan.is_available == true) {
            holder.tv_is_available.setBackgroundResource(R.drawable.bg_green_cornered);
            holder.tv_is_available.setText(context.getResources().getString(R.string.livreur_is_available));
        } else {
            holder.tv_is_available.setBackgroundResource(R.drawable.bg_red_cornered);
            holder.tv_is_available.setText(context.getResources().getString(R.string.livreur_is_busy));
        }

        holder.itemView.setOnClickListener(new OnLivreurSelectListener(this, kabaShippingMan));
    }

    @Override
    public int getItemCount() {
        return kabaShippingManList.size();
    }

    public int getSelected () {
        return my_choice;
    }

    View previousView;

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cic_deliveryman;
        TextView tv_kaba_man_name, tv_kaba_man_no, tv_is_available;
        ImageView iv_is_checked;

        public ViewHolder(View itemView) {
            super(itemView);

            this.cic_deliveryman = itemView.findViewById(R.id.cic_deliveryman);
            this.tv_kaba_man_no = itemView.findViewById(R.id.tv_kaba_man_no);
            this.tv_kaba_man_name = itemView.findViewById(R.id.tv_kaba_man_name);
            this.iv_is_checked = itemView.findViewById(R.id.iv_confirm_icon);
            this.tv_is_available = itemView.findViewById(R.id.tv_is_available);
        }
    }

    private class OnLivreurSelectListener implements View.OnClickListener {

        private final KabaShippingMan kabaShippingMan;

        public OnLivreurSelectListener(ShippingManRecyclerAdapter shippingManRecyclerAdapter, KabaShippingMan kabaShippingMan) {
            this.kabaShippingMan = kabaShippingMan;
        }

        @Override
        public void onClick(View view) {

            if (!kabaShippingMan.is_available) {
                mToast(context.getResources().getString(R.string.sorry_kabaman_not_available));
                return;
            }

            if (view == previousView)
                return;
            /* work on previous view */
            if (previousView != null) {
                ViewHolder previousHolder = new ViewHolder(previousView);
                previousHolder.iv_is_checked.setVisibility(View.INVISIBLE);
            }
            /*  */
            ViewHolder currentHolder = new ViewHolder(view);
            currentHolder.iv_is_checked.setVisibility(View.VISIBLE);
            /*  */
            for (int i = 0; i < kabaShippingManList.size(); i++) {
                int s = currentHolder.getLayoutPosition();
                int f = currentHolder.getAdapterPosition();
                if (kabaShippingManList.get(i).name.equals(currentHolder.tv_kaba_man_name.getText().toString())) {
                    kabaShippingManList.get(i).is_selected = KabaShippingMan.SELECTED;
                    my_choice = i;
                } else {
                    kabaShippingManList.get(i).is_selected = KabaShippingMan.NO_SELECTED;
                }
            }
            previousView = view;
        }
    }

    private void mToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
