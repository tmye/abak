package tg.experta.kaba.activities.Home.Fragmentz;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.experta.kaba.R;
import tg.experta.kaba._commons.adapters.FoodTagAdapter;
import tg.experta.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.experta.kaba._commons.decorator.CommandListSpacesItemDecoration;
import tg.experta.kaba.config.Constant;
import tg.experta.kaba.data.Food.Food_Tag;
import tg.experta.kaba.data.command.Command;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Home_3_CommandList extends BaseFragment {


    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private RecyclerView recyclerView;

    private Context context;

    public static Home_3_CommandList instantiate (Context ctx) {

        Home_3_CommandList frg = new Home_3_CommandList();
        return frg;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Home_3_CommandList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_command_list, container, false);

        // launch update in the activity
        ((OnListFragmentInteractionListener) getContext()).updateCommandList();

        // Set the adapter
        context = view.getContext();
        recyclerView =   view.findViewById(R.id.recyclerview);
        return view;
    }

    public void update() {

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new CommandListSpacesItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.list_item_spacing),
                getContext().getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
        ));
        recyclerView.setAdapter(new MyCommandRecyclerViewAdapter(context, Command.fakeList(3)));
    }

    public class MyCommandRecyclerViewAdapter extends RecyclerView.Adapter<MyCommandRecyclerViewAdapter.ViewHolder> {

        private final List<Command> data;
        private final Context ctx;
        private Drawable line_divider;

        public MyCommandRecyclerViewAdapter(Context ctx, List<Command> data) {

            this.ctx = ctx;
            this.data = data;
            line_divider = ContextCompat.getDrawable(ctx, R.drawable.command_inner_food_item_divider);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_command_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.rc_food_list.setAdapter(new CommandInnerFoodViewAdapter());

//            holder.rc_food_tags.setAdapter(new FoodTagAdapter());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View rootView;
            private final TextView tv_header_resto_name;
            public RecyclerView rc_food_list;
            public CircleImageView header_resto_cic;

            // make the recyclerview show his total elements

            public ViewHolder(View view) {
                super(view);
                rootView = view;
                rc_food_list = view.findViewById(R.id.recycler_inner_food);
                header_resto_cic = view.findViewById(R.id.header_resto_cic);
                tv_header_resto_name = view.findViewById(R.id.tv_header_resto_name);

                initNetstedRecyclerview();
            }

            private void initNetstedRecyclerview() {

                rc_food_list.addItemDecoration(new CommandInnerFoodLineDecorator(line_divider));
                rc_food_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
                {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
                // disable inner list scrolling
                rc_food_list.setNestedScrollingEnabled(false);
            }
        }
    }


    /* inner food adapter */
    public class CommandInnerFoodViewAdapter extends RecyclerView.Adapter<CommandInnerFoodViewAdapter.ViewHolder> {

        private int COMMAND_FOOD_COUNT = 4;


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder
                    (LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_command_inner_food_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Log.d(Constant.APP_TAG, "binding inner objects -- position : "+position);

            initFoodAdapter (Food_Tag.generate(3), holder.rc_food_tags);
        }

        @Override
        public int getItemCount() {
            return COMMAND_FOOD_COUNT;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final RecyclerView rc_food_tags;

            public ViewHolder(View itemView) {
                super(itemView);
                rc_food_tags = itemView.findViewById(R.id.rc_food_tags);
            }
        }

        private void initFoodAdapter (List<Food_Tag> food_tags, RecyclerView rc) {

            FoodTagAdapter tagAdapter = new FoodTagAdapter(getContext(), food_tags, false);
            GridLayoutManager lny = new GridLayoutManager(context, FoodTagAdapter.TAG_SPAN_COUNT);

            rc.setLayoutManager(lny);
            rc.setAdapter(tagAdapter);
        }

    }


    /* inner food tag adapter */
    /*

        private void initFoodTags() {

            // gridview with selected items ...
            FoodTagAdapter = new FoodTagAdapter(this, foodEntity);
            GridLayoutManager lny = new GridLayoutManager(this, TAG_SPAN_COUNT);

            holder.recyclerViewFoodTags.setLayoutManager(lny);
            holder.recyclerViewFoodTags.setAdapter(tagAdapter);
        }

    */


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
        // TODO: Update argument type and name
        void onCommandInteractionListener (Command command);

        void updateCommandList ();
    }
}
