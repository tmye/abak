package tg.experta.kaba.activities.Home.Fragmentz;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.experta.kaba._commons.decorator.LastItemListSpaceDecoration;
import tg.experta.kaba.activities.Home.contracts.F_RestaurantContract;
import tg.experta.kaba.activities.Menu.RestaurantMenuActivity;
import tg.experta.kaba.activities.Menu.RestaurantSubMenuFragment;
import tg.experta.kaba.R;
import tg.experta.kaba.config.Constant;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;
import tg.experta.kaba.syscore.GlideApp;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Home_2_Restaurant extends BaseFragment implements F_RestaurantContract.View {


    private static String RESTOZ = "RESTOZ";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;


    private MyRestaurantRecyclerViewAdapter resListAdapter;

    /* views */
    RecyclerView restaurantRecyclerView;

    /* presenter */
    private F_RestaurantContract.Presenter presenter;

    public static Home_2_Restaurant instantiate (Context ctx, List<RestaurantEntity> restoz) {

        Home_2_Restaurant frg = new Home_2_Restaurant();
        Bundle args = new Bundle();
        args.putParcelableArrayList(RESTOZ, (ArrayList<? extends Parcelable>) restoz);
        frg.setArguments(args);
        return frg;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Home_2_Restaurant() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allRestos = getArguments().getParcelableArrayList(RESTOZ);
    }

    List<RestaurantEntity> allRestos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }


    public void updateRestoz (List<RestaurantEntity> restoz) {

        this.allRestos = restoz;
        if (resListAdapter != null)
            resListAdapter.updateData(allRestos);
    }

    @Override
    public void showRestaurantList() {

        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        restaurantRecyclerView.addItemDecoration(new LastItemListSpaceDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.menu_food_item_height)));
        resListAdapter = new MyRestaurantRecyclerViewAdapter(getContext(), allRestos);
        restaurantRecyclerView.setAdapter(resListAdapter);
    }

    @Override
    public void setPresenter(F_RestaurantContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public class MyRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<MyRestaurantRecyclerViewAdapter.ViewHolder> {

        private List<RestaurantEntity> data;
        private final Context ctx;

        public MyRestaurantRecyclerViewAdapter(Context ctx, List<RestaurantEntity> data) {
            this.ctx = ctx;
            this.data = data;
        }

        public void updateData (List<RestaurantEntity> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

           /* // if you are the last ...
            if (viewType == data.size()-1) {
                return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.menu_food_item_space, parent, false));
            }*/
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_restaurant_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final RestaurantEntity resto = data.get(position);

            holder.tv_resto_name.setText(resto.restaurant_name.toUpperCase());
            holder.tv_resto_address.setText(resto.getContact().address);
            holder.tv_resto_contact.setText(resto.getContact().phone);

            GlideApp.with(ctx)
                    .load(Constant.SERVER_ADDRESS+resto.restaurant_logo)
                    .placeholder(R.drawable.placeholder_kaba)
                    .centerCrop()
                    .into(holder.cic_resto);

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onRestaurantInteraction(resto);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View rootView;
            public CircleImageView cic_resto;
            public TextView tv_resto_name;
            public TextView tv_resto_address;
            public TextView tv_resto_contact;

            public ViewHolder(View view) {
                super(view);
                rootView = view;
                this.cic_resto = view.findViewById(R.id.iv_restaurant_icon);
                this.tv_resto_name = view.findViewById(R.id.tv_restaurant_name);
                this.tv_resto_address = view.findViewById(R.id.tv_restaurant_address);
                this.tv_resto_contact = view.findViewById(R.id.tv_restaurant_contact);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onRestaurantInteraction(RestaurantEntity entity);
    }

    private void initViews(View rootView) {
        restaurantRecyclerView = rootView.findViewById(R.id.list);
    }

}
