package tg.tmye.kaba.restaurant._commons.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.data.delivery.KabaShippingMan;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.syscore.GlideApp;

/**
 * By abiguime on 2018/10/1.
 * email: 2597434002@qq.com
 */
public class ShippingManRecyclerAdapter extends RecyclerView.Adapter<ShippingManRecyclerAdapter.ViewHolder> implements View.OnClickListener {


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

        if (kabaShippingMan.is_selected == KabaShippingMan.SELECTED){
            holder.iv_is_checked.setVisibility(View.VISIBLE);
        } else {
            holder.iv_is_checked.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return kabaShippingManList.size();
    }

    public int getSelected () {
        return my_choice;
    }

    @Override
    public void onClick(View view) {

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

    View previousView;

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cic_deliveryman;
        TextView tv_kaba_man_name, tv_kaba_man_no;
        ImageView iv_is_checked;

        public ViewHolder(View itemView) {
            super(itemView);

            this.cic_deliveryman = itemView.findViewById(R.id.cic_deliveryman);
            this.tv_kaba_man_no = itemView.findViewById(R.id.tv_kaba_man_no);
            this.tv_kaba_man_name = itemView.findViewById(R.id.tv_kaba_man_name);
            this.iv_is_checked = itemView.findViewById(R.id.iv_confirm_icon);
        }
    }
}
