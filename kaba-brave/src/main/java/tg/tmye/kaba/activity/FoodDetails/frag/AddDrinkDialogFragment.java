package tg.tmye.kaba.activity.FoodDetails.frag;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;


public class AddDrinkDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    public static final String TAG = "AddDrinkDialogFragment";
    private Listener mListener;


    private static final int SPAN_COUNT = 3;

    private List<Restaurant_Menu_FoodEntity> drinks;

    // TODO: Customize parameters
    public static AddDrinkDialogFragment newInstance(List<Restaurant_Menu_FoodEntity> drinks) {
        final AddDrinkDialogFragment fragment = new AddDrinkDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ITEM_COUNT, (ArrayList<? extends Parcelable>) drinks);

        /* send list of drinks items for */

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drinks = getArguments().getParcelableArrayList(ARG_ITEM_COUNT);
        if (drinks == null) {
            mToast(getResources().getString(R.string.no_additionnal_drinks));
            dismiss();
        }
    }

    private void mToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        recyclerView.setAdapter(new DrinkListAdapter(drinks));
    }

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
        void onDrinkSelected(Restaurant_Menu_FoodEntity entity);
    }

    private class DrinkListAdapter extends RecyclerView.Adapter<DrinkListAdapter.ViewHolder> {


        private final List<Restaurant_Menu_FoodEntity> drinks;

        public DrinkListAdapter(List<Restaurant_Menu_FoodEntity> drinks) {
            this.drinks = drinks;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_add_drink_list_dialog_item, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Restaurant_Menu_FoodEntity drink = drinks.get(position);

            holder.tv_drink_name.setText(drink.name);

            holder.itemView.setOnClickListener(new OnAddDrinkClickListener(drink));
        }

        @Override
        public int getItemCount() {
            return drinks.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            private final View rootView;
            TextView tv_drink_name;
            ImageView iv_drink_icon;

            public ViewHolder(View itemView) {
                super(itemView);
                this.rootView = itemView;
                tv_drink_name = itemView.findViewById(R.id.tv_drink_name);
                iv_drink_icon = itemView.findViewById(R.id.iv_drink_icon);
            }
        }
    }

    private class OnAddDrinkClickListener implements View.OnClickListener {


        private final Restaurant_Menu_FoodEntity drink;

        public OnAddDrinkClickListener(Restaurant_Menu_FoodEntity drink) {
            this.drink = drink;
        }

        @Override
        public void onClick(View view) {

            mListener.onDrinkSelected(drink);
            dismiss();
        }
    }
}
