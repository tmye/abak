package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;

/**
 * By abiguime on 17/06/2018.
 * email: 2597434002@qq.com
 */
public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {

    private final List<String> data;
    private final Context context;

    private int selected_position;

    public SimpleTextAdapter (Context context, List<String> menu_list) {

        this.context = context;
        this.data = menu_list;
        selected_position = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_title_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_name.setText(data.get(position));
        holder.tv_name.setOnClickListener(new OnMenuSelectedListener(position));

        if (position == selected_position) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tv_name.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.tv_name.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    private class OnMenuSelectedListener implements View.OnClickListener {

        private final int position;

        public OnMenuSelectedListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            ((RestaurantMenuActivity)context).onMenuInteraction(position);
            selected_position = position;
            notifyDataSetChanged();
        }
    }
}
