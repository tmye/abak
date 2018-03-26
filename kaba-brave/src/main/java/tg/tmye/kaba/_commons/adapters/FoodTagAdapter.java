package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.data.Food.Food_Tag;

/**
 * By abiguime on 09/01/2018.
 * email: 2597434002@qq.com
 */

public class FoodTagAdapter extends RecyclerView.Adapter<FoodTagAdapter.ViewHolder> {

    public static final int TAG_SPAN_COUNT = 5;
    private final Context ctx;
    private final List<Food_Tag> data;
    private boolean isCheckable = true;


    public FoodTagAdapter(Context ctx, List<Food_Tag> food_tags, boolean isCheckable) {

        this.ctx = ctx;
        this.data = food_tags;
        this.isCheckable = isCheckable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.food_details_tag_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (isCheckable)
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    data.get(position).switchState();
                    bindOrUpdateTextView(holder);
                }
            });
        bindOrUpdateTextView(holder);
    }

    private void bindOrUpdateTextView(ViewHolder holder) {

        ((TextView) holder.rootView).setText(data.get(holder.getAdapterPosition()).name.toUpperCase());
        ((TextView) holder.rootView).setBackgroundColor(
                data.get(holder.getAdapterPosition()).state == 1 ? ctx.getResources().getColor(R.color.selected_yellow) : ctx.getResources().getColor(R.color.unselected_gray)
        );
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
        }
    }
}
