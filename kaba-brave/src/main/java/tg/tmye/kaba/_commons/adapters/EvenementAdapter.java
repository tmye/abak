package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.ad_categories.evenements.contract.EvenementContract;
import tg.tmye.kaba.activity.menu.Fragmentz.RestaurantSubMenuFragment;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.evenement.Evenement;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;

/**
 * By abiguime on 09/07/2018.
 * email: 2597434002@qq.com
 */
public class EvenementAdapter extends RecyclerView.Adapter<EvenementAdapter.ViewHolder> {

    private final Context context;
    public List<Evenement> evenements;

    public EvenementAdapter(Context context, List<Evenement> evenements) {

        this.context = context;
        this.evenements = evenements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.evenement_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Evenement evenement = evenements.get(position);

        /* setup size of the image */
        int width = UtilFunctions.getScreenSize(context)[0] - context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_half);
        int height = (width * 9)/16;

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.iv_evenement_pic.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = height;

        holder.iv_evenement_pic.setLayoutParams(layoutParams);

//        holder.tv_evenement_date.settex
        holder.tv_evenement_description.setText(evenement.description);
        holder.tv_evenement_title.setText(evenement.name);
        holder.bt_tag.setText(evenement.category);
        holder.tv_evenement_date.setText(UtilFunctions.timeStampToDate(context, evenement.created_at));

        /* Glide setup picture of the food */
        GlideApp.with(context)
                .load(Constant.SERVER_ADDRESS +"/"+evenement.pic)
                .transition(GenericTransitionOptions.with(  ((MyKabaApp)context.getApplicationContext()).getGlideAnimation()  ))
                .placeholder(R.drawable.placeholder_kaba)
                .into(holder.iv_evenement_pic);

        holder.iv_evenement_pic.setOnClickListener(new OnEventPreviewListener(evenement));
    }

    private class OnEventPreviewListener implements View.OnClickListener {

        private final Evenement evenement;

        public OnEventPreviewListener(Evenement evenement) {
            this.evenement = evenement;
        }

        @Override
        public void onClick(View view) {

            // or load with glide
            ((EvenementContract.View)(context)).onEventClicked(evenement);
        }
    }

    @Override
    public int getItemCount() {
        return evenements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /**/
        TextView tv_evenement_description, tv_evenement_title, tv_evenement_date;
        ImageView iv_evenement_pic;
        Button bt_tag;


        public ViewHolder(View itemView) {
            super(itemView);

            tv_evenement_description = itemView.findViewById(R.id.tv_evenement_description);
            tv_evenement_title = itemView.findViewById(R.id.tv_evenement_title);
            tv_evenement_date = itemView.findViewById(R.id.tv_evenement_date);
            iv_evenement_pic = itemView.findViewById(R.id.iv_evenement_pic);
            bt_tag = itemView.findViewById(R.id.bt_tag);
        }


    }

    private class onFoodClickListener implements View.OnClickListener {

        private final Restaurant_Menu_FoodEntity food;

        public onFoodClickListener(Restaurant_Menu_FoodEntity food) {
            this.food = food;
        }

        @Override
        public void onClick(View view) {

            ((RestaurantSubMenuFragment.OnFragmentInteractionListener)context).onFoodInteraction(food);
        }
    }
}
