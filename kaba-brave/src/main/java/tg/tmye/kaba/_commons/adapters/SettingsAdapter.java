package tg.tmye.kaba._commons.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.UserAcc.ServiceClientActivity;
import tg.tmye.kaba.activity.appinfo.AppInfoActivity;
import tg.tmye.kaba.activity.faq.FaqActivity;
import tg.tmye.kaba.activity.terms_and_condition.TermsAndConditionsActivity;

/**
 * By abiguime on 2018/10/22.
 * email: 2597434002@qq.com
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    public final int[] titles = {R.string.contact_us,  R.string.terms_and_conditions, R.string.app_info, R.string.faq};
    public final int[] titles_details = {};
    public final int[] icons = {R.drawable.ic_contact_us, R.drawable.ic_terms_and_condition, R.drawable.ic_app_info, R.drawable.ic_faq};

    public final Class[] destination_activity = new Class[]{
            ServiceClientActivity.class,
            TermsAndConditionsActivity.class,
            AppInfoActivity.class,
            FaqActivity.class
    };

    private final Context context;

    int MENU_COUNT = 4;

    public SettingsAdapter (Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_settings_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String title = "", details = "";
        if (titles.length > position)
            title = context.getString(titles[position]);
        if (titles_details.length > position) {
            details = context.getString(titles_details[position]);
        }

        holder.tv_title.setText(title);
        holder.tv_title_details.setText(details);

        if (icons.length > position) {
            holder.iv_icons.setImageResource(icons[position]);
        }

        holder.itemView.setOnClickListener(new SettingItemClickListener(position));
    }

    @Override
    public int getItemCount() {
        return MENU_COUNT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_title_details;
        ImageView iv_icons;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_title_details = itemView.findViewById(R.id.tv_title_details);
            iv_icons = itemView.findViewById(R.id.iv_icon);
        }

    }

    private class SettingItemClickListener implements View.OnClickListener {
        private final int position;

        public SettingItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, destination_activity[position]));
        }

    }
}
