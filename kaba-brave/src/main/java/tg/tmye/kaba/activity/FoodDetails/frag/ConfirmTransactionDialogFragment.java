package tg.tmye.kaba.activity.FoodDetails.frag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.HashMap;
import java.util.Map;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


/**
 * By abiguime on 2017/12/6.
 * email: 2597434002@qq.com
 */

public class ConfirmTransactionDialogFragment extends DialogFragment {


    private static final String DATA = "DATA";
    public static final String TAG = "ConfirmTransactionDialogFragment";
    Listener mListener;

    private double latitude, longitude;
    private HashMap<Restaurant_Menu_FoodEntity, Integer> food_quantity;

    // TODO: Customize parameters
    public static ConfirmTransactionDialogFragment newInstance(HashMap<Restaurant_Menu_FoodEntity, Integer> selection_map) {
        final ConfirmTransactionDialogFragment fragment = new ConfirmTransactionDialogFragment();

        /* send food and quantity list */
        /* map food, and quantities */

        Bundle args = new Bundle();
        args.putSerializable(ConfirmTransactionDialogFragment.DATA, selection_map);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        food_quantity = (HashMap<Restaurant_Menu_FoodEntity, Integer>) getArguments().getSerializable(DATA);
    }

    @Override
    public void onResume() {

        super.onResume();

        int width = getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    ImageButton ib_confirm;
    TextView tv_select_address;

    RecyclerView recyclerview_items_to_buy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirm_transaction_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ib_confirm = view.findViewById(R.id.ib_confirm);
        tv_select_address = view.findViewById(R.id.tv_select_address);
        recyclerview_items_to_buy = view.findViewById(R.id.recyclerview_items_to_buy);

        ib_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                selectAddress();
            }
        });

        /* inflate the recyclerview */
        inflateRecyclerview();
    }

    private void inflateRecyclerview() {

        SelectedFoodsAdapter adapter = new SelectedFoodsAdapter();
        recyclerview_items_to_buy.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview_items_to_buy.setAdapter(adapter);
    }

    private void selectAddress() {

        // startActivityForResult
    }

    private void selectAdress() {

        // select address of the delivery
    }

    private static final int TWO_MINUTES = 1000 * 60 * 2;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {

    }

    class SelectedFoodsAdapter extends RecyclerView.Adapter<SelectedFoodsAdapter.ViewHolder> {

        @Override
        public SelectedFoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SelectedFoodsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_food_item, parent, false));
        }

        @Override
        public void onBindViewHolder(SelectedFoodsAdapter.ViewHolder holder, int position) {

            /* food idz */

            Restaurant_Menu_FoodEntity entity = (Restaurant_Menu_FoodEntity) food_quantity.keySet().toArray()[position];
            int quantity = food_quantity.get(entity);

            holder.tv_quantity.setText("X "+quantity);
            holder.tv_name.setText(entity.name.toUpperCase());


       GlideApp.with(getContext())
                    .load(Constant.SERVER_ADDRESS+"/"+entity.pic)
//                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getContext().getApplicationContext()).getGlideAnimation()  ))
                    .centerCrop()
                    .into(holder.iv_pic);


         /*   Object[] objects = food_id_quantity.keySet().toArray();

            Restaurant_Menu_FoodEntity drinkEntity = getDrinkEntityWithId((Integer) objects[position]);

            holder.tv_name.setText(drinkEntity.name);

            GlideApp.with(FoodDetailsActivity.this)
                    .load(Constant.SERVER_ADDRESS+"/"+drinkEntity.pic)
                    .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                    .centerCrop()
                    .into(holder.iv_pic);

            holder.tv_quantity.setText(
                    "X "+ ((int)food_id_quantity.get(objects[position]))
            );

            holder.itemView.setOnClickListener(new FoodDetailsActivity.SelectedDrinksAdapter.OnDecreaseDrinkClickListener(position));
      */
        }


        @Override
        public int getItemCount() {
            return food_quantity.keySet().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            TextView tv_quantity;
            TextView tv_name;
            ImageView iv_pic;

            public ViewHolder(View itemView) {
                super(itemView);

                this.iv_pic = itemView.findViewById(R.id.iv_pic);
                this.tv_quantity = itemView.findViewById(R.id.tv_drink_count);
                this.tv_name = itemView.findViewById(R.id.tv_drink_name);
            }

        }
    }


}
