package tg.tmye.kaba.activity.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.decorator.LastItemListSpaceDecoration;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.syscore.GlideApp;

/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantSubMenuFragment extends Fragment {


    private static final String DATA_1 = "subMenu";
    private OnFragmentInteractionListener mListener;

    public static RestaurantSubMenuFragment instantiate (Context ctx,  Restaurant_SubMenuEntity  subMenu) {

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

    private void initRecyclerview(List<Restaurant_Menu_FoodEntity>  foods) {


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
                return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.menu_food_item_right, parent, false));
            }
            // at the bottom of the list -- need to add the kaba gray icon... and few informations
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {



            final Restaurant_Menu_FoodEntity item = data.get(position);
            holder.tv_food_title.setText(item.title.toUpperCase());
            holder.tv_food_price.setText(item.price.toUpperCase());

            GlideApp.with(ctx)
                    .load(Constant.SERVER_ADDRESS+item.food_pic)
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
            public TextView tv_food_price;

            public ViewHolder(View itemView) {
                super(itemView);
                this.viewRoot = itemView;
                iv_pic = itemView.findViewById(R.id.iv_pic);
                tv_food_title = itemView.findViewById(R.id.tv_food_name);
                tv_food_price = itemView.findViewById(R.id.tv_food_price);
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
        // TODO: Update argument type and name
        void onFoodInteraction(Restaurant_Menu_FoodEntity food);
    }


}
