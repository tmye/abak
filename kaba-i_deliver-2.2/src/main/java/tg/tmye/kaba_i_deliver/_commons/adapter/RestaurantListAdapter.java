package tg.tmye.kaba_i_deliver._commons.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Priority;

import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba_i_deliver.syscore.Constant;
import tg.tmye.kaba_i_deliver.syscore.GlideApp;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;



public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>  implements Filterable {

    private List<RestaurantEntity> data;
    private List<RestaurantEntity> usedData;
    private List<RestaurantEntity> restaurantListFiltered;
    private final Context ctx;

    public RestaurantListAdapter(Context ctx, List<RestaurantEntity> data) {
        this.ctx = ctx;
        this.data = data;
        this.usedData = data;
     /*   for (int i = 0; i < data.size(); i++) {
            data.get(i).is_coming_soon = (UtilFunctions.getRandomBoolean () ? F_Restaurant_2_Fragment.COMING_SOON : 0);
        }*/
    }

    public void updateData(List<RestaurantEntity> data) {
        this.usedData = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final RestaurantEntity resto = usedData.get(position);

        holder.tv_resto_name.setText(resto.name.toUpperCase());
        holder.tv_resto_address.setText(resto.address);

        if (resto.distance == null || "".equals(resto.distance)) {
            holder.view_separator.setVisibility(View.GONE);
            holder.tv_distance.setVisibility(View.GONE);
        } else {
            holder.view_separator.setVisibility(View.VISIBLE);
            holder.tv_distance.setVisibility(View.VISIBLE);
            holder.tv_distance.setText(resto.distance + ctx.getResources().getString(R.string.kilometer));
        }

        /* add a preorder tag to tell the user. */
        holder.tv_is_open.setVisibility(View.GONE);

        if (holder.tv_is_coming_soon != null) {
            holder.tv_is_coming_soon.setVisibility(View.GONE);
        }

        GlideApp.with(ctx)
                .load(Constant.SERVER_ADDRESS + "/" + resto.pic)
                .transition(GenericTransitionOptions.with(((MyKabaDeliverApp) ctx.getApplicationContext()).getGlideAnimation()))
                .placeholder(R.drawable.placeholder_kaba)
                .priority(Priority.NORMAL)
                .centerCrop()
                .into(holder.cic_resto);

        int distancePrincingInt = 0;
        try {
            distancePrincingInt = Integer.valueOf(resto.delivery_pricing);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resto.delivery_pricing != null && !"".equals(resto.delivery_pricing) && distancePrincingInt > 0) {
            holder.tv_distance_pricing.setText(resto.delivery_pricing + " F");
            holder.tv_distance_pricing.setVisibility(View.VISIBLE);
        } else
            holder.tv_distance_pricing.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new OnShowRestaurantPosition(resto));
    }

    @Override
    public int getItemCount() {
        return usedData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View rootView;
        public CircleImageView cic_resto;
        public TextView tv_resto_name;
        public TextView tv_resto_address;
        public ImageView iv_restaurant_theme_image;
        public TextView tv_enter_menu;
        public TextView tv_enter_restaurant;
        public TextView tv_distance;
        public View view_separator;
        public TextView tv_is_open;
        public TextView tv_is_coming_soon;
        public TextView tv_distance_pricing;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            this.cic_resto = view.findViewById(R.id.iv_restaurant_icon);
            this.tv_resto_name = view.findViewById(R.id.tv_restaurant_name);
            this.tv_resto_address = view.findViewById(R.id.tv_restaurant_address);
            tv_distance = view.findViewById(R.id.tv_distance);
            view_separator = view.findViewById(R.id.view_separator);
            tv_is_open = view.findViewById(R.id.tv_is_open);
            tv_distance_pricing = view.findViewById(R.id.tv_distance_pricing);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    restaurantListFiltered = data;
                } else {
                    List<RestaurantEntity> filteredList = new ArrayList<>();
                    for (RestaurantEntity row : data) {
                        // filter the EditText w
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    restaurantListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = restaurantListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usedData = (List<RestaurantEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private class OnShowRestaurantPosition implements View.OnClickListener {

        private final RestaurantEntity resto;

        public OnShowRestaurantPosition(RestaurantEntity resto) {
            this.resto = resto;
        }

        @Override
        public void onClick(View view) {

            /* jump into google maps activity */
            openInMap(resto);
        }
    }

    private void mToast(String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    void openInMap (RestaurantEntity restaurant) {

        if (restaurant.location == null || restaurant.location.length() == 0) {
            mToast("Erreur");
            return;
        }

        String[] splitted_location = restaurant.location.split(":");
        String latitude = splitted_location[0];
        String longitude = splitted_location[1];

        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+latitude+","+longitude+"("+Uri.encode(restaurant.name)+")");
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(ctx.getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            ctx.startActivity(mapIntent);
        } else {
            mToast(ctx.getString(R.string.google_maps_not_installed));
        }
    }
}
