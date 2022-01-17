package tg.tmye.kaba.partner._commons.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner._commons.utils.UtilFunctions;
import tg.tmye.kaba.partner.data._OtherEntities.StatsEntity;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class StatsRecyclerAdapter extends RecyclerView.Adapter<StatsRecyclerAdapter.ViewHolder> {

    private List<StatsEntity> data;
    private final Context ctx;
    private Drawable line_divider;

    public StatsRecyclerAdapter (Context ctx, List<StatsEntity> data) {

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
                .inflate(R.layout.resto_stats_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        StatsEntity entity = data.get(position);

        holder.tv_command_count.setText(""+entity.nb_app_command);
        holder.tv_command_ca.setText(""+entity.ca_app);
        holder.tv_hsn_ca.setText(""+entity.ca_hsn);
        holder.tv_hsn_count.setText(""+entity.nb_hsn);
        holder.tv_date_content.setText(entity.date);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<StatsEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tv_date_content/*, tv_date_day*/;
        TextView tv_command_count, tv_command_ca;
        TextView tv_hsn_count, tv_hsn_ca;

        public ViewHolder(View view) {
            super(view);
            this.tv_command_count = view.findViewById(R.id.tv_commands_count);
            this.tv_command_ca = view.findViewById(R.id.tv_command_ca);
            this.tv_hsn_count = view.findViewById(R.id.tv_hsn_count);
            this.tv_hsn_ca = view.findViewById(R.id.tv_hsn_ca);
            this.tv_date_content = view.findViewById(R.id.tv_date_content);
        }

    }

}
