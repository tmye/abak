package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.favorite.FavoriteActivity;
import tg.tmye.kaba.activity.UserAcc.MyAdressesActivity;
import tg.tmye.kaba.activity.UserAcc.NewsFeedActivity;
import tg.tmye.kaba.activity.UserAcc.personnalinfo.PersonnalInfoActivity;
import tg.tmye.kaba.activity.UserAcc.ServiceClientActivity;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class UserAccountAdapter extends RecyclerView.Adapter<UserAccountAdapter.ViewHolder> {

    Context ctx;

    public UserAccountAdapter (Context ctx) {

        this.ctx = ctx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_account_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (position) {
            case 0:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_infos));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_infos);
                holder.rootview.setOnClickListener(new OpenSubActivity(PersonnalInfoActivity.class));
                break;
            case 1:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_bon_dachat));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_bon_dachat);
                break;
            case 2:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_favorite_plat));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_favorite_food);
                holder.rootview.setOnClickListener(new OpenSubActivity(FavoriteActivity.class));
                break;
            case 3:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_delivery_address));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_addresses);
                holder.rootview.setOnClickListener(new OpenSubActivity(MyAdressesActivity.class));
                break;
            case 4:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_client_service));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_client_service);
                holder.rootview.setOnClickListener(new OpenSubActivity(ServiceClientActivity.class));
                break;
            case 5:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_newsletter));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_newsletter);
                holder.rootview.setOnClickListener(new OpenSubActivity(NewsFeedActivity.class));
                break;
            case 6:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_shopping_card));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_shopping_card_yellow_24dp);
                break;
            case 7:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_commands));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_list_command);
                break;
            case 8:
                holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_settings));
                holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_settings);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View rootview;
        ImageView iv_menu_icon;
        TextView tv_menu_title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.rootview = itemView;
            this.iv_menu_icon = itemView.findViewById(R.id.iv_menu_icon);
            this.tv_menu_title = itemView.findViewById(R.id.tv_menu_title);
        }
    }

    private class OpenSubActivity implements View.OnClickListener {

        private final Class<?> activity;

        public OpenSubActivity(Class<?> activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), activity);
            view.getContext().startActivity(intent);
        }
    }
}
