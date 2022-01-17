package tg.tmye.kaba.partner.activities.menu.Fragmentz;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.partner.R;
import tg.tmye.kaba.partner._commons.decorator.LastItemListSpaceDecoration;
import tg.tmye.kaba.partner._commons.utils.UtilFunctions;
import tg.tmye.kaba.partner.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.partner.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.partner.syscore.Constant;
import tg.tmye.kaba.partner.syscore.GlideApp;
import tg.tmye.kaba.partner.syscore.MyRestaurantApp;


/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantSubMenuFragment extends Fragment {


    private static final String DATA_1 = "subMenu";
    private OnFragmentInteractionListener mListener;

    public static RestaurantSubMenuFragment instantiate (Context ctx,  Restaurant_SubMenuEntity subMenu) {

        RestaurantSubMenuFragment frg = new RestaurantSubMenuFragment();
        Bundle args = new Bundle();
        args.putParcelable(DATA_1,  subMenu);
        frg.setArguments(args);
        return frg;
    }

    Restaurant_SubMenuEntity subMenuEntity;

    private RecyclerView recyclerview;

    private TextView tv_no_food_message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the bundle data
        Bundle args = getArguments();
        if (args!= null)
            subMenuEntity = args.getParcelable(DATA_1);
    }


    public RestaurantSubMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_submenu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.recyclerview);
        tv_no_food_message = view.findViewById(R.id.tv_no_food_message);

        initRecyclerview(subMenuEntity.foods);
    }

    private void initRecyclerview(List<Restaurant_Menu_FoodEntity> foods) {

        if (foods == null || foods.size() == 0) {
            // empty
            noFood();
        } else {

            showFood();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerview.setLayoutManager(linearLayoutManager);
            recyclerview.addItemDecoration(new LastItemListSpaceDecoration(
                    getContext().getResources().getDimensionPixelSize(R.dimen.menu_food_item_height)
            ));
            SubMenuRecyclerviewAdapter adap = new SubMenuRecyclerviewAdapter(getContext(), foods);
            recyclerview.setAdapter(adap);
        }
    }

    private void showFood() {

        tv_no_food_message.setVisibility(View.GONE);
        recyclerview.setVisibility(View.VISIBLE);
    }

    private void noFood() {

        tv_no_food_message.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.GONE);
    }


    public class SubMenuRecyclerviewAdapter extends RecyclerView.Adapter<SubMenuRecyclerviewAdapter.ViewHolder> {

        private final List<Restaurant_Menu_FoodEntity> data;
        private final Context ctx;

        SubMenuRecyclerviewAdapter (Context ctx,List<Restaurant_Menu_FoodEntity> data) {

            this.ctx = ctx;
            this.data = data;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType % 2 == 0) {
                return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.menu_food_item_left, parent, false));
            } else {
                return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.menu_food_item_left, parent, false));
            }
            // at the bottom of the list -- need to add the kaba gray icon... and few informations
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final Restaurant_Menu_FoodEntity item = data.get(position);
            if (item.name != null)
                holder.tv_food_title.setText(item.name.toUpperCase());
            if (item.price != null) {
                holder.tv_food_price.setText(UtilFunctions.intToMoney(item.price));
            }

            if (item.promotion == 0) { /* IS IN promotion*/
                holder.tv_food_price.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                holder.tv_food_price.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary_yellow));
                holder.tv_food_promotion_price.setVisibility(View.GONE);
            } else if (item.promotion == 1) { /* is not in promotion */
                holder.tv_food_price.setTextColor(ContextCompat.getColor(ctx, R.color.black));
                holder.tv_food_price.setPaintFlags(holder.tv_food_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tv_food_promotion_price.setVisibility(View.VISIBLE);
                holder.tv_food_promotion_price.setText(""+item.promotion_price);
                holder.tv_food_promotion_price.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary));
            }

            GlideApp.with(ctx)
                    .load(Constant.SERVER_ADDRESS +"/"+ item.pic)
                    .transition(GenericTransitionOptions.with(  ((MyRestaurantApp)ctx.getApplicationContext()).getGlideAnimation()  ))
                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(holder.iv_pic);

            holder.viewRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mListener.onFragmentInteraction(null);
                    mListener.onFoodInteraction(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public View viewRoot;
            public CircleImageView iv_pic;
            public TextView tv_food_title;
            public TextView tv_food_price, tv_food_promotion_price;

            public ViewHolder(View itemView) {
                super(itemView);
                this.viewRoot = itemView;
                iv_pic = itemView.findViewById(R.id.iv_pic);
                tv_food_title = itemView.findViewById(R.id.tv_food_name);
                tv_food_price = itemView.findViewById(R.id.tv_food_price);
                tv_food_promotion_price = itemView.findViewById(R.id.tv_food_promotion_price);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and restaurant_name
        void onFoodInteraction(Restaurant_Menu_FoodEntity food);
    }


}
